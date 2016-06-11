package com.tecace.retail.appmanager.ui.view;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tecace.retail.appmanager.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by JW on 2016-05-26.
 */
public class CameraPreviewLayout extends RelativeLayout {
    private static final String TAG = CameraPreviewLayout.class.getSimpleName();

    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    /**
     * Timeout for the pre-capture sequence.
     */
    private static final long PRECAPTURE_TIMEOUT_MS = 1000;

    /**
     * Tolerance when comparing aspect ratios.
     */
    private static final double ASPECT_RATIO_TOLERANCE = 0.005;

    /**
     * Max preview width that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_WIDTH = 2560;

    /**
     * Max preview height that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_HEIGHT = 1440;

    /**
     * Camera state: Device is closed.
     */
    private static final int STATE_CLOSED = 0;

    /**
     * Camera state: Device is opened, but is not capturing.
     */
    private static final int STATE_OPENED = 1;

    /**
     * Camera state: Showing camera preview.
     */
    private static final int STATE_PREVIEW = 2;

    /**
     * Camera state: Waiting for 3A convergence before capturing a photo.
     */
    private static final int STATE_WAITING_FOR_3A_CONVERGENCE = 3;

    /**
     * The state of the camera device. Default: Closed.
     */
    private int mState = STATE_CLOSED;

    /**
     * TextureView.SurfaceTextureListener handles several lifecycle events of a TextureView.
     */
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            configureTransform(width, height, mScreenSize);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            configureTransform(width, height, mScreenSize);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            synchronized (mCameraStateLock) {
                mPreviewSize = null;
            }
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }

    };

    /**
     * An AutoFitTextureView for camera preview.
     */
    public AutoFitTextureView mTextureView;

    /**
     * An additional thread for running tasks that shouldn't block the UI.  This is used for all
     * callbacks from the CameraDevice and CameraCaptureSessions.
     */
    private HandlerThread mBackgroundThread;

    /**
     * A counter for tracking corresponding CaptureRequest}s and CaptureResults
     * across the CameraCaptureSession capture callbacks.
     */
    private final AtomicInteger mRequestCounter = new AtomicInteger();

    /**
     * A Semaphore to prevent the app from exiting before closing the camera.
     */
    private final Semaphore mCameraOpenCloseLock = new Semaphore(1);

    /**
     * A lock protecting camera state.
     */
    private final Object mCameraStateLock = new Object();
    // *********************************************************************************************
    // State protected by mCameraStateLock.
    //
    // The following state is used across both the UI and background threads.  Methods with "Locked"

    // in the name expect mCameraStateLock to be held while calling.

    /**
     * A CameraCaptureSession for camera preview.
     */
    private CameraCaptureSession mCaptureSession;

    /**
     * A reference to the opened CameraDevice.
     */
    private CameraDevice mCameraDevice;

    /**
     * The Size of camera preview.
     */
    private Size mPreviewSize;

    /**
     * The CameraCharacteristics for the currently configured camera device.
     */
    private CameraCharacteristics mCharacteristics;

    /**
     * A Handler for running tasks in the background.
     */
    private Handler mBackgroundHandler;

    /**
     * A reference counted holder wrapping the ImageReader that handles JPEG image
     * captures. This is used to allow us to clean up the ImageReader when all background
     * tasks using its Images have completed.
     */
    private RefCountedAutoCloseable<ImageReader> mJpegImageReader;

    /**
     * Number of pending user requests to capture a photo.
     */
    private int mPendingUserCaptures = 0;

    /**
     * Request ID to ImageSaver.ImageSaverBuilder mapping for in-progress JPEG captures.
     */
    private final TreeMap<Integer, ImageSaver.ImageSaverBuilder> mJpegResultQueue = new TreeMap<>();

    /**
     * CaptureRequest.Builder for the camera preview
     */
    private CaptureRequest.Builder mPreviewRequestBuilder;

    /**
     * Timer to use with pre-capture sequence to ensure a timely capture if 3A convergence is
     * taking too long.
     */
    private long mCaptureTimer;

    /**
     * Current Context of the app.
     */
    private Context mContext;

    /**
     * An instance of a LayoutInflater.
     */
    private LayoutInflater mInflater;

    /**
     * ID of the current CameraDevice. It is static because it needs to share its information
     * with inner static class.
     */
    private static String mCameraId;

    /**
     * Output image saved on an ImageView after the user captures a photo. It is static because
     * it needs to share its information with inner static class.
     */
    public static ImageView mOutputImage;

    /**
     * A boolean value to enable Manual Focus (Tap to focus).
     */
    private boolean mEnableManualFocus;

    /**
     * A boolean value to enable Manual Focus (Tap to focus).
     */
    private String mScreenSize;

    /**
     * OnTextureViewTouchedListener. Communicating between BaseCameraFragment.
     */
    public interface OnTextureViewTouchedListener {
        void drawFocusIcon(final float x, final float y);
    }

    private OnTextureViewTouchedListener mListener;
    public void setListener(OnTextureViewTouchedListener listener) {
        mListener = listener;
    }
    public OnTextureViewTouchedListener getListener() {
        return mListener;
    }

    //********************************************************************************************/

    public CameraPreviewLayout(Context context) {
        super(context);
        init(context, null);
    }

    public CameraPreviewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CameraPreviewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * Setting up the Camera properties for the Camera demos.
     * This method communicates with BaseCameraFragment and
     * the CameraFragment demo itself.
     * @param cameraId              The ID of the camera to open specified for specific demo.
     * @param enableManualFocus     Boolean flag to enable or disable the manual focus for the
     *                              specific demo that do/doesn't require the user to tap the
     *                              preview screen.
     * @param screenSize            Sets the display size of the preview.
     */
    public void setUpCameraProperties(String cameraId, boolean enableManualFocus, String screenSize) {
        this.mCameraId = cameraId;
        this.mEnableManualFocus = enableManualFocus;
        this.mScreenSize = screenSize;
    }

    /**
     * Initializing Camera Preview Layout and methods to communicate with BaseCameraFragment.
     */
    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.layout_camera_view, this, true);

        mOutputImage = (ImageView) findViewById(R.id.output_image);

        mTextureView = (AutoFitTextureView) findViewById(R.id.camera_texture_view);
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
    }

    public void updateOutputImageVisibility(int visible) {
        mOutputImage.setVisibility(visible);
    }

    public void updateTextureViewVisibility(int visible) {
        mTextureView.setVisibility(visible);
    }

    public void setCameraId(String cameraId) {
        this.mCameraId = cameraId;
    }

    public String getCameraId() {
        return this.mCameraId;
    }

    public void setManualFocusEnabled(boolean enableManualFocus) {
        this.mEnableManualFocus = enableManualFocus;
    }

    public boolean getManualFocusEnabled() {
        return this.mEnableManualFocus;
    }

    public void setScreenSize(String screenRatio) {
        this.mScreenSize = screenRatio;
    }

    public String getScreenSize() {
        return this.mScreenSize;
    }

    public void performBlinkAnimation() {
        setBlinkAnimation(mTextureView);
    }

    /**
     * CameraDevice.StateCallback is called when the currently active CameraDevice
     * changes its state.
     */
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice cameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here if
            // the TextureView displaying this has been set up.
            synchronized (mCameraStateLock) {
                Log.e(TAG, "#### onOpened: Camera Opened");
                mState = STATE_OPENED;
                mCameraOpenCloseLock.release();
                mCameraDevice = cameraDevice;

                // Start the preview session if the TextureView has been set up already.
                if (mPreviewSize != null && mTextureView.isAvailable()) {
                    createCameraPreviewSessionLocked();
                }
            }
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            synchronized (mCameraStateLock) {
                Log.e(TAG, "#### onDisconnected: Camera Closed");
                mState = STATE_CLOSED;
                mCameraOpenCloseLock.release();
                cameraDevice.close();
                mCameraDevice = null;
            }
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error) {
            Log.e(TAG, "Received camera device error: " + error);
            synchronized (mCameraStateLock) {
                Log.e(TAG, "#### onError: Camera error");
                mState = STATE_CLOSED;
                mCameraOpenCloseLock.release();
                cameraDevice.close();
                mCameraDevice = null;
            }
        }
    };

    /**
     * This a callback object for the ImageReader. "onImageAvailable" will be called when a
     * JPEG image is ready to be saved.
     */
    private final ImageReader.OnImageAvailableListener mOnJpegImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
            dequeueAndSaveImage(mJpegResultQueue, mJpegImageReader);
        }
    };

    /**
     * Calculating the Tap Area.
     */
    private Rect calculateTapArea(float x, float y, float coefficient) {
        int focusAreaSize = 100;

        int width = 0;
        int height = 0;
        float input_x = 0.0f;
        float input_y = 0.0f;

        width = getHeight();
        height = getWidth();
        input_x = y;
        input_y = (width - x);
        Log.d(TAG, "##### width = " + width + ", height = " + height);

        float focus_x = 0.0f;
        float focus_y = 0.0f;
        float unit_x = ((width / 2.0f) / 1000.0f);
        float unit_y = ((height / 2.0f) / 1000.0f);
        int left = 0;
        int top = 0;

        Log.d(TAG, "unit_x = " + unit_x + ", unit_y = " + unit_y);
        if (input_x <= width / 2 && input_y <= height / 2) {   //1
            //-1000< x <=0, -1000 < y <= 0
            focus_x = -1 * (input_x * unit_x);
            focus_y = -1 * (input_y * unit_y);

            Log.d(TAG, "focus_x = " + focus_x + ", focus_y = " + focus_y);

            left = clamp((int) Math.round(focus_x - focusAreaSize / 2), -1000, 0);
            top = clamp((int) Math.round(focus_y - focusAreaSize / 2), -1000, 0);
        } else if (input_x > width / 2 && input_y <= height / 2) { //2
            focus_x = (input_x - width / 2) * unit_x;
            focus_y = -1 * (input_y * unit_y);

            Log.d(TAG, "focus_x = " + focus_x + ", focus_y = " + focus_y);

            left = clamp((int) Math.round(focus_x - focusAreaSize / 2), 0, 1000);
            top = clamp((int) Math.round(focus_y - focusAreaSize / 2), -1000, 0);
        } else if (input_x <= width / 2 && input_y > height / 2) { //3
            focus_x = -1 * (input_x * unit_x);
            focus_y = (input_y - height / 2) * unit_y;

            Log.d(TAG, "focus_x = " + focus_x + ", focus_y = " + focus_y);

            left = clamp((int) Math.round(focus_x - focusAreaSize / 2), -1000, 0);
            top = clamp((int) Math.round(focus_y - focusAreaSize / 2), 0, 1000);
        } else if (input_x > width / 2 && input_y > height / 2) {  //4
            focus_x = (input_x - width / 2) * unit_x;
            focus_y = (input_y - height / 2) * unit_y;

            Log.d(TAG, "focus_x = " + focus_x + ", focus_y = " + focus_y);

            left = clamp((int) Math.round(focus_x - focusAreaSize / 2), 0, 1000);
            top = clamp((int) Math.round(focus_y - focusAreaSize / 2), 0, 1000);
        }
        return new Rect(left, top, left + (focusAreaSize / 2), top + (focusAreaSize / 2));
    }

    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    /**
     * A CameraCaptureSession.CaptureCallback that handles events for the preview and
     * pre-capture sequence.
     */
    private Rect focusRect;
    private CameraCaptureSession.CaptureCallback mPreCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        private void process(CaptureResult result) {
            synchronized (mCameraStateLock) {
                switch (mState) {
                    case STATE_PREVIEW: {
                        mTextureView.setOnTouchListener(new OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (!mEnableManualFocus){return false;}
                                if (event.getAction() == MotionEvent.ACTION_UP && mTextureView != null) {
                                    focusRect = calculateTapArea(event.getX(), event.getY(), 1f);
                                    Log.d(TAG, "##### (X, Y) = (" + event.getX() + ", " + event.getY());
                                    Log.d(TAG, "##### (" + focusRect.left + ", " + focusRect.top + ", " + focusRect.right + ", " + focusRect.bottom + ")");

                                    if (mListener != null)
                                        mListener.drawFocusIcon(event.getX(), event.getY());

                                    MeteringRectangle[] focusArea = new MeteringRectangle[1];
                                    focusArea[0] = new MeteringRectangle((int)event.getX(), (int)event.getY(), 100, 100, 300);

                                    triggerManualFocus(focusArea);
                                }
                                return true;
                            }
                        });
                        break;
                    }

                    case STATE_WAITING_FOR_3A_CONVERGENCE: {
                        if (mPendingUserCaptures > 0) {
                            // Capture once for each user tap of the "Picture" button.
                            while (mPendingUserCaptures > 0) {
                                captureStillPictureLocked();
                                mPendingUserCaptures--;
                            }
                            // After this, the camera will go back to the normal state of preview.
                            mState = STATE_PREVIEW;
                        }
                    }
                }
            }
        }

        @Override
        public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request,
                                        CaptureResult partialResult) {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request,
                                       TotalCaptureResult result) {
            process(result);
        }
    };

    /**
     * Method to trigger Manual Focus. (Tap to focus)
     */
    public void triggerManualFocus(MeteringRectangle[] meteringRectangle) {
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_REGIONS, meteringRectangle);

        mPreviewRequestBuilder
                .set(CaptureRequest.CONTROL_AF_REGIONS, meteringRectangle);
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                CaptureRequest.CONTROL_AF_MODE_AUTO);

        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
            CameraMetadata.CONTROL_AF_TRIGGER_START);
        try {
            mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void refresh3AModes() {
        Log.d(TAG, "#### 왜안되노?");
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);

        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                CameraMetadata.CONTROL_AF_TRIGGER_IDLE);

        try {
            mCaptureSession.setRepeatingRequest(
                    mPreviewRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * A CameraCaptureSession.CaptureCallback that handles the still JPEG capture request.
     */
    private final CameraCaptureSession.CaptureCallback mCaptureCallback
            = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request,
                                     long timestamp, long frameNumber) {
            File jpegFile = new File(Environment.
                    getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "NOT SAVED");

            // Look up the ImageSaverBuilder for this request and update it with the file name
            // based on the capture start time.
            ImageSaver.ImageSaverBuilder jpegBuilder;
            int requestId = (int) request.getTag();
            synchronized (mCameraStateLock) {
                jpegBuilder = mJpegResultQueue.get(requestId);
            }

            if (jpegBuilder != null) jpegBuilder.setFile(jpegFile);
        }

        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request,
                                       TotalCaptureResult result) {
            int requestId = (int) request.getTag();
            ImageSaver.ImageSaverBuilder jpegBuilder;
            StringBuilder sb = new StringBuilder();

            // Look up the ImageSaverBuilder for this request and update it with the CaptureResult
            synchronized (mCameraStateLock) {
                jpegBuilder = mJpegResultQueue.get(requestId);

                // If we have all the results necessary, save the image to a file in the background.
                handleCompletionLocked(requestId, jpegBuilder, mJpegResultQueue);

                if (jpegBuilder != null) {
                    jpegBuilder.setResult(result);
                    sb.append("FILE NOT SAVED");
                }
                finishedCaptureLocked();
            }
//            showToast(sb.toString());
        }

        @Override
        public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request,
                                    CaptureFailure failure) {
            int requestId = (int) request.getTag();
            synchronized (mCameraStateLock) {
                mJpegResultQueue.remove(requestId);
                finishedCaptureLocked();
            }
            showToast("Capture failed!");
        }

    };


    /**
     * A Handler for showing Toasts on the UI thread.
     */
    private final Handler mMessageHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (mContext != null) {
                Toast.makeText(mContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Sets up state related to camera that is needed before opening a CameraDevice.
     */
    private boolean setUpCameraOutputs() {
        CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics
                        = manager.getCameraCharacteristics(cameraId);

                StreamConfigurationMap map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                // For still image captures, we use the largest available size.
                Size normalJpeg = Collections.max(
                        Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                        new CompareSizesByArea());
                Size fullJpeg = new Size (2560, 1440);
                Size oneOnOneJpeg = new Size (3024, 3024);

                synchronized (mCameraStateLock) {
                    // Set up ImageReaders for JPEG outputs.  Place these in a reference
                    // counted wrapper to ensure they are only closed when all background tasks
                    // using them are finished.
                    if (mJpegImageReader == null || mJpegImageReader.getAndRetain() == null) {
                        if (mScreenSize.equals("normal")) {
                            mJpegImageReader = new RefCountedAutoCloseable<>(
                                    ImageReader.newInstance(normalJpeg.getWidth(),
                                            normalJpeg.getHeight(), ImageFormat.JPEG, /*maxImages*/5));
                        } else if (mScreenSize.equals("full")) {
                            mJpegImageReader = new RefCountedAutoCloseable<>(
                                    ImageReader.newInstance(fullJpeg.getWidth(),
                                            fullJpeg.getHeight(), ImageFormat.JPEG, 5));
                        } else if (mScreenSize.equals("1on1")) {
                            mJpegImageReader = new RefCountedAutoCloseable<>(
                                    ImageReader.newInstance(oneOnOneJpeg.getWidth(),
                                            oneOnOneJpeg.getHeight(), ImageFormat.JPEG, 5));
                        }
                    }
                    mJpegImageReader.get().setOnImageAvailableListener(
                            mOnJpegImageAvailableListener, mBackgroundHandler);

                    mCharacteristics = characteristics;
                    mCameraId = cameraId;
                }
                return true;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Opens the camera specified by {@link #mCameraId}.
     */
    public void openCamera(String cameraId) {
        if (!setUpCameraOutputs()) {
            return;
        }
        updateTextureViewVisibility(VISIBLE);

        CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            Handler backgroundHandler;
            synchronized (mCameraStateLock) {
                mCameraId = cameraId;
                backgroundHandler = mBackgroundHandler;
            }

            // Attempt to open the camera. mStateCallback will be called on the background handler's
            // thread when this succeeds or fails.
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.openCamera(mCameraId, mStateCallback, backgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
        }
    }

    /**
     * Closes the current CameraDevice.
     */
    public void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            synchronized (mCameraStateLock) {

                // Reset state and clean up resources used by the camera.
                // Note: After calling this, the ImageReaders will be closed after any background
                // tasks saving Images from these readers have been completed.
                mPendingUserCaptures = 0;
                mState = STATE_CLOSED;

                if (null != mCaptureSession) {
                    mCaptureSession.close();
                    mCaptureSession = null;
                }
                if (null != mCameraDevice) {
                    mCameraDevice.close();
                    mCameraDevice = null;
                }
                if (null != mJpegImageReader) {
                    mJpegImageReader.close();
                    mJpegImageReader = null;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    /**
     * Starts a background thread and its Handler.
     */
    public void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        synchronized (mCameraStateLock) {
            mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        }
    }

    /**
     * Stops the background thread and its Handler.
     */
    public void stopBackgroundThread() {
        if(mBackgroundHandler == null) return;
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            synchronized (mCameraStateLock) {
                mBackgroundHandler = null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new CameraCaptureSession for camera preview.
     * Call this only with mCameraStateLock held.
     */
    private void createCameraPreviewSessionLocked() {
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);

            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder
                    = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            // Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice.createCaptureSession(Arrays.asList(surface,
                    mJpegImageReader.get().getSurface()),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                            synchronized (mCameraStateLock) {
                                // The camera is already closed
                                if (null == mCameraDevice) {
                                    return;
                                }

                                try {
                                    setup3AControlsLocked(mPreviewRequestBuilder);
                                    // Finally, we start displaying the camera preview.
                                    cameraCaptureSession.setRepeatingRequest(
                                            mPreviewRequestBuilder.build(),
                                            mPreCaptureCallback, mBackgroundHandler);
                                    mState = STATE_PREVIEW;
                                } catch (CameraAccessException | IllegalStateException e) {
                                    e.printStackTrace();
                                    return;
                                }
                                // When the session is ready, we start displaying the preview.
                                mCaptureSession = cameraCaptureSession;
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                            showToast("Failed to configure camera.");
                        }
                    }, mBackgroundHandler
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configure the given CaptureRequest.Builder to use auto-focus, auto-exposure, and
     * auto-white-balance controls.
     * Call this only with mCameraStateLock held.
     * @param builder the builder to configure.
     */
    private void setup3AControlsLocked(CaptureRequest.Builder builder) {
        // Enable auto-magical 3A run by camera device
        builder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);

        // If there is a "continuous picture" mode available, use it, otherwise default to AUTO.
        if (contains(mCharacteristics.get(
                CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES),
                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)) {
            builder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        } else {
            builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
        }

        // If there is an auto-magical flash control mode available, use it, otherwise default to
        // the "on" mode, which is guaranteed to always be available.
        if (contains(mCharacteristics.get(
                CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES),
                CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)) {
            builder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        } else {
            builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
        }

        // If there is an auto-magical white balance control mode available, use it.
        if (contains(mCharacteristics.get(
                CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES),
                CaptureRequest.CONTROL_AWB_MODE_AUTO)) {
            // Allow AWB to run auto-magically if this device supports this
            builder.set(CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_AWB_MODE_AUTO);
        }
    }

    /**
     * Configure the necessary Matrix transformation to `mTextureView`,
     * and start/restart the preview capture session if necessary.
     * This method should be called after the camera state has been initialized in
     * setUpCameraOutputs.
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     * @param screenSize The screen size of 'mTextureView'
     */
    private void configureTransform(int viewWidth, int viewHeight, String screenSize) {
        synchronized (mCameraStateLock) {
            if (null == mTextureView || null == mContext) {
                return;
            }
            Size screenSizeRatio = null;
            StreamConfigurationMap map = mCharacteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            //Normal Size is the biggest size (4032x3024)
            Size normalScreenSize = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                    new CompareSizesByArea());
            Size fullScreenSize = new Size(2560, 1440);
            Size oneOnOneSize = new Size(3024, 3024);

            switch (screenSize) {
                case "full": screenSizeRatio = fullScreenSize;
                    break;
                case "normal": screenSizeRatio = normalScreenSize;
                    break;
                case "1on1": screenSizeRatio = oneOnOneSize;
                    break;
            }

            Point displaySize = new Point();
            ((Activity)mContext).getWindowManager().getDefaultDisplay().getSize(displaySize);

            // Swap the view dimensions for calculation as needed if they are rotated relative to
            // the sensor.
            int rotatedViewWidth = viewHeight;
            int rotatedViewHeight = viewWidth;
            int maxPreviewWidth = displaySize.y;
            int maxPreviewHeight = displaySize.x;

            // Preview should not be larger than display size and 1080p.
            if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                maxPreviewWidth = MAX_PREVIEW_WIDTH;
            }

            if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                maxPreviewHeight = MAX_PREVIEW_HEIGHT;
            }

            // Find the best preview size for these view dimensions and configured JPEG size.
            Size previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                    rotatedViewWidth, rotatedViewHeight, maxPreviewWidth, maxPreviewHeight,
                    screenSizeRatio);

            mTextureView.setAspectRatio(previewSize.getHeight(), previewSize.getWidth());

            // Start or restart the active capture session if the preview was initialized or
            // if its aspect ratio changed significantly.
            if (mPreviewSize == null || !checkAspectsEqual(previewSize, mPreviewSize)) {
                mPreviewSize = previewSize;
                if (mState != STATE_CLOSED) {
                    createCameraPreviewSessionLocked();
                }
            }
        }
    }

    /**
     * Initiate a still image capture.
     * This function sends a capture request that initiates a pre-capture sequence in our state
     * machine that waits for auto-focus to finish, ending in a "locked" state where the lens is no
     * longer moving, waits for auto-exposure to choose a good exposure value, and waits for
     * auto-white-balance to converge.
     */
    public void takePicture() {
        synchronized (mCameraStateLock) {
            mPendingUserCaptures++;

            try {
                // Trigger an auto-focus run if camera is capable. If the camera is already focused,
                // this should do nothing.
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                        CameraMetadata.CONTROL_AF_TRIGGER_START);

                // Tell the camera to lock focus.
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                        CameraMetadata.CONTROL_AE_PRECAPTURE_TRIGGER_START);

                // Update state machine to wait for auto-focus, auto-exposure, and
                // auto-white-balance (aka. "3A") to converge.
                mState = STATE_WAITING_FOR_3A_CONVERGENCE;

                // Start a timer for the pre-capture sequence.
                startTimerLocked();

                // Replace the existing repeating request with one with updated 3A triggers.
                mCaptureSession.capture(mPreviewRequestBuilder.build(), mPreCaptureCallback,
                        mBackgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Send a capture request to the camera device that initiates a capture targeting the JPEG
     * Call this only with mCameraStateLock held.
     */
    private void captureStillPictureLocked() {
        try {
            if (null == mContext || null == mCameraDevice) {
                return;
            }
            // This is the CaptureRequest.Builder that we use to take a picture.
            final CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);

            captureBuilder.addTarget(mJpegImageReader.get().getSurface());

            // Use the same AE and AF modes as the preview.
            setup3AControlsLocked(captureBuilder);

            // Set request tag to easily track results in callbacks.
            captureBuilder.setTag(mRequestCounter.getAndIncrement());

            CaptureRequest request = captureBuilder.build();

            // Create an ImageSaverBuilder in which to collect results, and add it to the queue
            // of active requests.
            ImageSaver.ImageSaverBuilder jpegBuilder = new ImageSaver.ImageSaverBuilder(mContext)
                    .setCharacteristics(mCharacteristics);

            mJpegResultQueue.put((int) request.getTag(), jpegBuilder);

            mCaptureSession.capture(request, mCaptureCallback, mBackgroundHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void recycleOutputImage() {
        if (mOutputImage != null &&
                mOutputImage.getDrawable() != null) {
            BitmapDrawable b = (BitmapDrawable) mOutputImage.getDrawable();
            if (b != null && b.getBitmap() != null) {
                b.getBitmap().recycle();
                Log.d(TAG, "#### mOutputImage bitmap recycled");
            }
            mOutputImage.setImageBitmap(null);
        }
    }

    /**
     * Called after a JPEG capture has completed; resets the AF trigger state for the
     * pre-capture sequence.
     * Call this only with mCameraStateLock held.
     */
    private void finishedCaptureLocked() {
        try {
            // Reset the auto-focus trigger in case AF didn't run quickly enough.
            Log.d(TAG, "finishedCaptureLocked: picture is taken");
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);

            mCaptureSession.capture(mPreviewRequestBuilder.build(), mPreCaptureCallback,
                    mBackgroundHandler);

            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_IDLE);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve the next Image from a reference counted ImageReader, retaining that ImageReader
     * until that Image is no longer in use, and set this Image as the result for the next request
     * in the queue of pending requests. If all necessary information is available,
     * begin saving the image to a file in a background thread.
     * @param pendingQueue the currently active requests.
     * @param reader       a reference counted wrapper containing an ImageReader from which
     *                     to acquire an image.
     */
    private void dequeueAndSaveImage(TreeMap<Integer, ImageSaver.ImageSaverBuilder> pendingQueue,
                                     RefCountedAutoCloseable<ImageReader> reader) {
        synchronized (mCameraStateLock) {
            Map.Entry<Integer, ImageSaver.ImageSaverBuilder> entry =
                    pendingQueue.firstEntry();
            ImageSaver.ImageSaverBuilder builder = entry.getValue();

            // Increment reference count to prevent ImageReader from being closed while we
            // are saving its Images in a background thread (otherwise their resources may
            // be freed while we are writing to a file).
            if (reader == null || reader.getAndRetain() == null) {
                Log.e(TAG, "Paused the activity before we could save the image," +
                        " ImageReader already closed.");
                pendingQueue.remove(entry.getKey());
                return;
            }

            Image image;
            try {
                image = reader.get().acquireNextImage();
            } catch (IllegalStateException e) {
                Log.e(TAG, "Too many images queued for saving, dropping image for request: " +
                        entry.getKey());
                pendingQueue.remove(entry.getKey());
                return;
            }

            builder.setRefCountedReader(reader).setImage(image);

            handleCompletionLocked(entry.getKey(), builder, pendingQueue);
        }
    }

    /**
     * Runnable that saves an Image into the specified File, and updates
     * android.provider.MediaStore to include the resulting file.
     * This can be constructed through an ImageSaverBuilder as the necessary image and
     * result information becomes available.
     */
    private static class ImageSaver implements Runnable {

        /**
         * The image to save.
         */
        private final Image mImage;
        /**
         * The file we save the image into.
         */
        private final File mFile;

        /**
         * The CaptureResult for this image capture.
         */
        private final CaptureResult mCaptureResult;

        /**
         * The CameraCharacteristics for this camera device.
         */
        private final CameraCharacteristics mCharacteristics;

        /**
         * The Context to use when updating MediaStore with the saved images.
         */
        private final Context mContext;

        /**
         * A Byte instance to use with ByteBuffer.
         */
        private byte[] bytes;

        /**
         * A reference counted wrapper for the ImageReader that owns the given image.
         */
        private final RefCountedAutoCloseable<ImageReader> mReader;

        /**
         * A fake path to make sure the photo isn't saved after capture. Can be anything.
         */
        private String path = "/sdcard/TecAce";

        private ImageSaver(Image image, File file, CaptureResult result,
                           CameraCharacteristics characteristics, Context context,
                           RefCountedAutoCloseable<ImageReader> reader) {
            mImage = image;
            mFile = file;
            mCaptureResult = result;
            mCharacteristics = characteristics;
            mContext = context;
            mReader = reader;
        }

        @Override
        public void run() {
            boolean success = false;
            int format = mImage.getFormat();
            switch (format) {
                case ImageFormat.JPEG: {
                    ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
                    bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    FileOutputStream output = null;
                    try {
                        output = new FileOutputStream(mFile);
                        output.write(bytes);
                        success = true;

                        //Method to show the Output Image.
                        showOutputImage();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        mImage.close();
                        closeOutput(output);
                    }
                    break;
                }
                default: {
                    Log.e(TAG, "Cannot save image, unexpected image format:" + format);
                    break;
                }
            }

            // Decrement reference count to allow ImageReader to be closed to free up resources.
            mReader.close();

            // If saving the file was successful, update MediaStore.
            if (success) {
                MediaScannerConnection.scanFile(mContext, new String[]{path},
            /*mimeTypes*/null, new MediaScannerConnection.MediaScannerConnectionClient() {
                            @Override
                            public void onMediaScannerConnected() {
                                // Do nothing
                            }

                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.d(TAG, "onScanCompleted called "
                                        + "mFile.delete(): " + mFile.delete());
                            }
                        });
            }
        }

        private void showOutputImage() {
            ((Activity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Bitmap realImage = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    ExifInterface exif = null;
                        try {
                            exif = new ExifInterface(path);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        try {
                            Log.d("EXIF value", exif.getAttribute(ExifInterface.TAG_ORIENTATION));
                            if (CameraPreviewLayout.mCameraId.equals("1")) {
                                realImage = rotateOutputImage(realImage, 270);          //this one is used for front-facing camera.
                            } else if (CameraPreviewLayout.mCameraId.equals("0")) {
                                realImage = rotateOutputImage(realImage, 90);           //this one is used for back facing camera.
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    CameraPreviewLayout.mOutputImage.setImageBitmap(realImage);
                }
            });
        }

        private Bitmap rotateOutputImage(Bitmap source, float angle) {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                    source.getHeight(), matrix, false);
        }

        /**
         * Builder class for constructing ImageSavers.
         * This class is thread safe.
         */
        private static class ImageSaverBuilder {
            private Image mImage;
            private File mFile;
            private CaptureResult mCaptureResult;
            private CameraCharacteristics mCharacteristics;
            private Context mContext;
            private RefCountedAutoCloseable<ImageReader> mReader;

            /**
             * Construct a new ImageSaverBuilder using the given Context.
             * @param context a Context to for accessing the
             *                android.provider.MediaStore.
             */
            public ImageSaverBuilder(final Context context) {
                mContext = context;
            }

            public synchronized ImageSaverBuilder setRefCountedReader(
                    RefCountedAutoCloseable<ImageReader> reader) {
                if (reader == null) throw new NullPointerException();

                mReader = reader;
                return this;
            }

            public synchronized ImageSaverBuilder setImage(final Image image) {
                if (image == null) throw new NullPointerException();
                mImage = image;
                return this;
            }

            public synchronized ImageSaverBuilder setFile(final File file) {
                if (file == null) throw new NullPointerException();
                mFile = file;
                return this;
            }

            public synchronized ImageSaverBuilder setResult(final CaptureResult result) {
                if (result == null) throw new NullPointerException();
                mCaptureResult = result;
                return this;
            }

            public synchronized ImageSaverBuilder setCharacteristics(
                    final CameraCharacteristics characteristics) {
                if (characteristics == null) throw new NullPointerException();
                mCharacteristics = characteristics;
                return this;
            }

            public synchronized ImageSaver buildIfComplete() {
                if (!isComplete()) {
                    return null;
                }
                return new ImageSaver(mImage, mFile, mCaptureResult, mCharacteristics, mContext,
                        mReader);
            }

            public synchronized String getSaveLocation() {
                return (mFile == null) ? "Unknown" : mFile.toString();
            }

            private boolean isComplete() {
                return mImage != null && mFile != null && mCaptureResult != null
                        && mCharacteristics != null;
            }
        }
    }

    // Utility classes and methods:
    // *********************************************************************************************

    /**
     * Comparator based on area of the given Size objects.
     */
    private static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    /**
     * A wrapper for an AutoCloseable object that implements reference counting to allow
     * for resource management.
     */
    private static class RefCountedAutoCloseable<T extends AutoCloseable> implements AutoCloseable {
        private T mObject;
        private long mRefCount = 0;

        /**
         * Wrap the given object.
         * @param object an object to wrap.
         */
        public RefCountedAutoCloseable(T object) {
            if (object == null) throw new NullPointerException();
            mObject = object;
        }

        /**
         * Increment the reference count and return the wrapped object.
         * @return the wrapped object, or null if the object has been released.
         */
        public synchronized T getAndRetain() {
            if (mRefCount < 0) {
                return null;
            }
            mRefCount++;
            return mObject;
        }

        /**
         * Return the wrapped object.
         * @return the wrapped object, or null if the object has been released.
         */
        public synchronized T get() {
            return mObject;
        }

        /**
         * Decrement the reference count and release the wrapped object if there are no other
         * users retaining this object.
         */
        @Override
        public synchronized void close() {
            if (mRefCount >= 0) {
                mRefCount--;
                if (mRefCount < 0) {
                    try {
                        mObject.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        mObject = null;
                    }
                }
            }
        }
    }

    /**
     * Given choices of Sizes supported by a camera, choose the smallest one that
     * is at least as large as the respective texture view size, and that is at most as large as the
     * respective max size, and whose aspect ratio matches with the specified value. If such size
     * doesn't exist, choose the largest one that is at most as large as the respective max size,
     * and whose aspect ratio matches with the specified value.
     * @param choices           The list of sizes that the camera supports for the intended output
     *                          class
     * @param textureViewWidth  The width of the texture view relative to sensor coordinate
     * @param textureViewHeight The height of the texture view relative to sensor coordinate
     * @param maxWidth          The maximum width that can be chosen
     * @param maxHeight         The maximum height that can be chosen
     * @param aspectRatio       The aspect ratio
     * @return The optimal Size, or an arbitrary one if none were big enough
     */
    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    /**
     * Cleanup the given OutputStream.
     * @param outputStream the stream to close.
     */
    private static void closeOutput(OutputStream outputStream) {
        if (null != outputStream) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Return true if the given array contains the given integer.
     * @param modes array to check.
     * @param mode  integer to get for.
     * @return true if the array contains the given integer, otherwise false.
     */
    private static boolean contains(int[] modes, int mode) {
        if (modes == null) {
            return false;
        }
        for (int i : modes) {
            if (i == mode) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true if the two given Sizes have the same aspect ratio.
     * @param a first Size to compare.
     * @param b second Size to compare.
     * @return true if the sizes have the same aspect ratio, otherwise false.
     */
    private static boolean checkAspectsEqual(Size a, Size b) {
        double aAspect = a.getWidth() / (double) a.getHeight();
        double bAspect = b.getWidth() / (double) b.getHeight();
        return Math.abs(aAspect - bAspect) <= ASPECT_RATIO_TOLERANCE;
    }

    /**
     * Shows a Toast on the UI thread.
     * @param text The message to show.
     */
    private void showToast(String text) {
        // We show a Toast by sending request message to mMessageHandler. This makes sure that the
        // Toast is shown on the UI thread.
        Message message = Message.obtain();
        message.obj = text;
        mMessageHandler.sendMessage(message);
    }

    /**
     * If the given request has been completed, remove it from the queue of active requests and
     * send an ImageSaver with the results from this request to a background thread to
     * save a file.
     * Call this only with mCameraStateLock held.
     * @param requestId the ID of the CaptureRequest to handle.
     * @param builder   the ImageSaver.ImageSaverBuilder for this request.
     * @param queue     the queue to remove this request from, if completed.
     */
    private void handleCompletionLocked(int requestId, ImageSaver.ImageSaverBuilder builder,
                                        TreeMap<Integer, ImageSaver.ImageSaverBuilder> queue) {
        if (builder == null) return;
        ImageSaver saver = builder.buildIfComplete();
        if (saver != null) {
            queue.remove(requestId);
            AsyncTask.THREAD_POOL_EXECUTOR.execute(saver);
        }
    }

    /**
     * Start the timer for the pre-capture sequence.
     * Call this only with mCameraStateLock held.
     */
    private void startTimerLocked() {
        mCaptureTimer = SystemClock.elapsedRealtime();
    }

    /**
     * Check if the timer for the pre-capture sequence has been hit.
     * Call this only with mCameraStateLock held.
     * @return true if the timeout occurred.
     */
    private boolean hitTimeoutLocked() {
        return (SystemClock.elapsedRealtime() - mCaptureTimer) > PRECAPTURE_TIMEOUT_MS;
    }


    //*******************************************************************************************//
    //                            Animation Sets used for Camera                                 //
    //*******************************************************************************************//

    public void setBlinkAnimation (View view) {
        Animation blink = new AlphaAnimation(1, 0);
        blink.setDuration(100);
        blink.setInterpolator(new LinearInterpolator());
        blink.setRepeatCount(0);
        view.startAnimation(blink);
    }

    protected void setFadeIn(View view)
    {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2500);
        view.setAnimation(fadeIn);
    }

    protected void setFadeInFast(View view)
    {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);
        view.setAnimation(fadeIn);
    }

    protected void setFadeOut(View view) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setDuration(1000);
        view.setAnimation(fadeOut);
    }

    public void animateRightIn(View view) {
        view.setX(1440f);
        view.setScaleX(0.5f);
        view.setScaleY(0.5f);

        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "x", 0);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(300);
        ObjectAnimator animScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f);
        ObjectAnimator animScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f);
        ObjectAnimator bgAlpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);

        animSet.play(animX).with(bgAlpha).with(animScaleX).with(animScaleY);
        animSet.start();
    }

    public void animateGrow(View view) {
        view.setScaleX(0.5f);
        view.setScaleY(0.5f);

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(300);
        ObjectAnimator animScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f);
        ObjectAnimator animScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f);
        ObjectAnimator bgAlpha = ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1f);

        animSet.play(bgAlpha).with(animScaleX).with(animScaleY);
        animSet.start();
    }

}
