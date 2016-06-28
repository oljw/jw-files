package com.tecace.app.manager.ui.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.tecace.app.manager.R;
import com.tecace.app.manager.gson.model.ArgumentsModel;
import com.tecace.app.manager.gson.model.FragmentModel;
import com.tecace.app.manager.gson.model.VideoModel;
import com.tecace.app.manager.util.AppManagerConst;
import com.tecace.retail.appmanager.ui.view.camera.CameraPreviewLayout;
import com.tecace.retail.appmanager.ui.view.camera.DrawingView;
import com.tecace.retail.appmanager.util.JsonUtil;

/**
 * Created by JW on 2016-05-24.
 */
public abstract class BaseCameraFragment extends BaseVideoFragment implements
        CameraPreviewLayout.OnTextureViewTouchedListener,
        CameraPreviewLayout.OnEyeDetectedListener {

    private static final String TAG = BaseCameraFragment.class.getSimpleName();

    public static final String CLICKED = "clicked";
    private static final String READY = "ready";

    private AppManagerConst.TransactionDir mTransactionDir;
    private FragmentModel<VideoModel> mFragmentModel;

    protected ImageButton mCaptureBtn;
    private CameraPreviewLayout mCameraPreviewLayout;
    private ObjectAnimator mRotateIconAnimator;
    private MediaPlayer mCameraShutter;
    private ImageView mFocusIcon;

    private DrawingView mDrawingView;
    private ImageView mRightCircle;
    private ImageView mLeftCircle;
    private Rect mEyeDetectedRect;
    private Rect mRightCircleRect;
    private Rect mLeftCircleRect;

    //TEST PURPOSE//
    private TextView mEyeDetectionCountView;
    private int mEyeDetectionCount = 0;
    ////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ArgumentsModel args = (ArgumentsModel) getArguments().getSerializable(AppManagerConst.ARGUMENTS_MODEL);
            mFragmentModel = JsonUtil.getInstance().loadJsonModel(getActivity().getApplicationContext(),
                    args.getFragmentJson(), new TypeToken<FragmentModel<VideoModel>>() {}.getType());
            mTransactionDir = (AppManagerConst.TransactionDir) AppManagerConst.TransactionDir.valueOf(args.getTransitionDir());
            onFragmentCreated(args);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = super.onCreateView(inflater, container, savedInstanceState);

        mCameraPreviewLayout = (CameraPreviewLayout) mView.findViewById(R.id.camera_preview);
        mCameraPreviewLayout.setManualFocusEnabled(mEnableManualFocus);
        mCameraPreviewLayout.setScreenSize(mScreenSize);
        mCameraPreviewLayout.setCameraId(mCameraId);
        mCameraPreviewLayout.setIrisEnabled(mIrisEnabled);

        mCameraPreviewLayout.setOnTextureViewTouchedListener(this);
        mCameraPreviewLayout.setOnEyeDetectedListener(this);

        if (!mIrisEnabled)
            initCameraDemo();

        if (mIrisEnabled)
            initIrisDemo();

        return mView;
    }

    private void initIrisDemo() {
        mDrawingView = (DrawingView) mView.findViewById(R.id.overlayView);
        mRightCircle = (ImageView) mView.findViewById(R.id.white_circle_right);
        mLeftCircle = (ImageView) mView.findViewById(R.id.white_circle_left);
        mEyeDetectionCountView = (TextView) mView.findViewById(R.id.eye_count);

        mCameraPreviewLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                try {
                    mLeftCircleRect = new Rect(mLeftCircle.getLeft(), mLeftCircle.getTop(), mLeftCircle.getRight(), mLeftCircle.getBottom());
                    mRightCircleRect = new Rect(mRightCircle.getLeft(), mRightCircle.getTop(), mRightCircle.getRight(), mRightCircle.getBottom());
                    mDrawingView.setCircleRects(mLeftCircleRect, mRightCircleRect);
                } catch (NullPointerException e) {
                    throw new NullPointerException("CHANGE THE LAYOUT FILE NAME IN JSON MODEL TO: '@layout/fragment_security'");
                }
            }
        });
    }

    private void initCameraDemo() {
        mFocusIcon = (ImageView) mView.findViewById(R.id.draw_focus_icon);
        mCaptureBtn = (ImageButton) mView.findViewById(R.id.capture_btn);
        try {
            mCaptureBtn.setSoundEffectsEnabled(false);
            mCaptureBtn.setTag(READY);
            mCaptureBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCaptureBtn.setClickable(false);
                    if (mCaptureBtn.getTag().equals(CLICKED)) return;
                    mCaptureBtn.setTag(CLICKED);
                    performBlinkAnim();
                    playShutterSound();
                    mCameraPreviewLayout.takePicture();
                    setForcedSeekToChapter(++mCurrentIndex);
                }
            });
        } catch (NullPointerException e) {
            throw new NullPointerException("CHANGE THE LAYOUT FILE NAME IN JSON MODEL TO: '@layout/fragment_camera'");
        }
    }

    private void playShutterSound(){
        mCameraShutter = MediaPlayer.create(getActivity(), R.raw.camera_shutter);
        mCameraShutter.start();
    }

    private void releaseShutter() {
        if (mCameraShutter != null){
            mCameraShutter.stop();
            mCameraShutter.release();
            mCameraShutter = null;
            Log.d(TAG, "#### Camera Shutter Released");
        }
    }

    public boolean checkForEyeCollision(Rect rect1, Rect rect2) {
        return Rect.intersects(rect1, rect2);
    }

    @Override
    public void onEyeDetected(Rect rect) {
        this.mEyeDetectedRect = rect;
        mDrawingView.setRect(rect);
        if (mLeftCircleRect != null && mEyeDetectedRect != null) {
            if (checkForEyeCollision(mLeftCircleRect, mEyeDetectedRect)
                    || checkForEyeCollision(mRightCircleRect, mEyeDetectedRect)) {
                mEyeDetectionCountView.setTextColor(Color.WHITE);
                mEyeDetectionCountView.setText("Eye Detection Count: " + mEyeDetectionCount);
                mEyeDetectionCount++;
                if (mEyeDetectionCount >= 10) {
                    setForcedSeekToChapterEnd(mCurrentIndex);
                    Toast.makeText(getActivity(), "IRIS SCAN COMPLETED", Toast.LENGTH_LONG).show();
                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(500);
                }
                Log.d(TAG, "#### EYE IN REGION");
            }
        }
    }

    @Override
    public void drawFocusIcon(final float x, final float y) {
        if (mRotateIconAnimator != null && mRotateIconAnimator.isRunning())
            mRotateIconAnimator.cancel();

        mRotateIconAnimator = ObjectAnimator.ofFloat(mFocusIcon , "rotation", 0f, 180f);
        mRotateIconAnimator.setDuration(200);
        mRotateIconAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mFocusIcon.setVisibility(View.VISIBLE);
                mFocusIcon.bringToFront();

                ViewGroup.MarginLayoutParams marginLayoutParams =
                        (ViewGroup.MarginLayoutParams)mFocusIcon.getLayoutParams();
                marginLayoutParams.setMargins(
                        (int)(x - 150),
                        (int)(y - 150),
                        (int)(-x + 150),
                        (int)(-y + 150));
                mFocusIcon.setLayoutParams(marginLayoutParams);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mFocusIcon.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mFocusIcon.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mRotateIconAnimator.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCameraPreviewLayout != null) {
            mCameraPreviewLayout.closeCamera();
            mCameraPreviewLayout.stopBackgroundThread();
            mCameraPreviewLayout.recycleOutputImage();
            mCameraPreviewLayout.setOnTextureViewTouchedListener(null);
            mCameraPreviewLayout.setOnEyeDetectedListener(null);
            if (mIrisEnabled)
                mCameraPreviewLayout.deinitEngine();
            if (!mIrisEnabled)
                mCaptureBtn.setVisibility(View.GONE);
            releaseShutter();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIrisEnabled)
            mCaptureBtn.setClickable(true);

        if (isLandscape() == false)
            playVideo(mVideoView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy()  {
        super.onDestroy();
    }

    abstract public void onFragmentCreated(ArgumentsModel args);
    abstract public void onViewCreated(View view);

    protected FragmentModel<VideoModel> getFragmentModel() {
        return mFragmentModel;
    }
    protected AppManagerConst.TransactionDir getTransactionDir() {
        return this.mTransactionDir;
    }

    abstract public void onPageTransitionStart(boolean enter, int nextAnim, AppManagerConst.TransactionDir dir);
    abstract public void onPageTransitionEnd(boolean enter, int nextAnim, AppManagerConst.TransactionDir dir);
    abstract public void onPageTransitionCancel(boolean enter, int nextAnim, AppManagerConst.TransactionDir dir);


    //*******************************************************************************************//
    //                            Camera Demo Controller Methods                                 //
    //*******************************************************************************************//

    protected static final boolean MANUAL_FOCUS_ENABLED = true;
    protected static final boolean MANUAL_FOCUS_DISABLED = false;

    protected static final boolean IRIS_ENABLED = true;
    protected static final boolean IRIS_DISABLED = false;

    protected static final String FRONT_FACING_CAMERA_ID = "1";
    protected static final String BACK_FACING_CAMERA_ID = "0";

    protected static final String NORMAL_SCREEN_SIZE = "normal";
    protected static final String FULL_SCREEN_SIZE = "full";
    protected static final String ONE_ON_ONE_SIZE = "1on1";
    protected static final String IRIS_UI_SIZE = "iris";

    protected boolean mEnableManualFocus;
    protected boolean mIrisEnabled;
    protected int mCurrentIndex;
    protected String mScreenSize;
    protected String mCameraId;

    /**
     * Initializing and configuring the Camera.
     * This method will set up the Camera demo in the environment it needs.
     *
     * @param cameraId              The ID of the camera to open specified for specific demo.
     *                                  - "1" for FRONT_FACING_CAMERA_ID.
     *                                  - "0" for BACK_FACING_CAMERA_ID.
     *
     * @param enableManualFocus     Boolean flag to enable or disable the manual focus for the
     *                              specific demo that do/does't require the user to tap the
     *                              preview screen.
     *                                  - MANUAL_FOCUS_ENABLED  = true
     *                                  - MANUAL_FOCUS_DISABLED = false
     *
     * @param enableIris            Enable Iris Demo.
     *                                  - IRIS_ENABLED  = true
     *                                  - IRIS_DISABLED = false
     *
     * @param screenSize            Sets the display size of the preview.
     *                                  - FULL_SCREEN_SIZE    = 16:9  Screen Size (2560x1440)
     *                                  - NORMAL_SCREEN_SIZE  = 4:3   Screen Size (4032x3024)
     *                                  - ONE_ON_ONE_SIZE     = 1:1   Screen Size (3024x3024)
     *                                  - IRIS_UI_SIZE        = 1:1   Screen Size (1440x1440)
     */
    protected void initializeDemo(String cameraId, boolean enableManualFocus, boolean enableIris, String screenSize) {
        this.mCameraId = cameraId;
        this.mEnableManualFocus = enableManualFocus;
        this.mIrisEnabled = enableIris;
        this.mScreenSize = screenSize;

        if (mCameraPreviewLayout != null)
            mCameraPreviewLayout.setUpCameraProperties(mCameraId, mEnableManualFocus, mIrisEnabled, mScreenSize);
    }

    /**
     * Turn on/open the Camera with the Camera ID that is specified in the
     * initializeDemo method.
     */
    protected void turnCameraOn() {
        if (mCameraPreviewLayout != null) {
            mCameraPreviewLayout.startBackgroundThread();
            mCameraPreviewLayout.openCamera(mCameraId);
            mCameraPreviewLayout.setVisibility(View.VISIBLE);
            if (!mIrisEnabled) {
                mCaptureBtn.setVisibility(View.VISIBLE);
            }
            if (mIrisEnabled) {
                mDrawingView.setVisibility(View.VISIBLE);
                mRightCircle.setVisibility(View.VISIBLE);
                mLeftCircle.setVisibility(View.VISIBLE);
                mEyeDetectionCountView.setVisibility(View.VISIBLE);
                mCameraPreviewLayout.irisAnimateTextureView();
            }
            mVideoView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Turn off/close the Camera.
     */
    protected void turnCameraOff() {
        if (mCameraPreviewLayout != null) {
            mCameraPreviewLayout.closeCamera();
            if (!mIrisEnabled) {
                mCaptureBtn.setVisibility(View.INVISIBLE);
            }
            if (mIrisEnabled) {
                mDrawingView.setVisibility(View.GONE);
                mRightCircle.setVisibility(View.GONE);
                mLeftCircle.setVisibility(View.GONE);
                mEyeDetectionCountView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Display the Captured Image. Specify this in the Chapter method with the corresponding
     * index number when the Output Image should show up.
     */
    protected void displayCapturedImage() {
        if (mCameraPreviewLayout != null) {
            mVideoView.setVisibility(View.INVISIBLE);
            mCaptureBtn.setVisibility(View.GONE);
            mCameraPreviewLayout.updateTextureViewVisibility(View.GONE);
            mCameraPreviewLayout.updateOutputImageVisibility(View.VISIBLE);
        }
    }

    /**
     * Automatically click the capture button when user didn't tap the capture button in time.
     * Specify this in the Chapter method with the corresponding index number when necessary.
     */
    protected void autoClickCaptureBtn() {
        mCaptureBtn.performClick();
    }

    /**
     * Perform a blinking animation on preview.
     */
    protected void performBlinkAnim() {
        mCameraPreviewLayout.performBlinkAnimation();
    }

    /**
     * Clean the screen and go back to the video.
     */
    protected void goBackToVideo() {
        mCameraPreviewLayout.updateTextureViewVisibility(View.GONE);
        mCameraPreviewLayout.updateOutputImageVisibility(View.GONE);
        if (!mIrisEnabled) {
            mCaptureBtn.setVisibility(View.GONE);
        }
        mVideoView.setVisibility(View.VISIBLE);
    }

    /**
     * Set onClickListener to chapter end at tap.
     */
    protected void setOnClickToChapterEnd(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setForcedSeekToChapterEnd(mCurrentIndex);
            }
        });
    }
}
