package com.samsung.retailexperience.retailhero.view.camera_view;

/**
 * Created by JW on 1/30/2016.
 */

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JW on 1/14/16.
 * This Controller Controls HeroHero2 B2C's Auto-Focus Demo.
 */

public class CameraSurfaceViewBack extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = CameraSurfaceViewBack.class.getSimpleName();

    private Context mContext;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int mWidth = 0;
    private int mHeight = 0;

    public CameraSurfaceViewBack(Context context, Camera camera) {
        super(context);
        mContext = context;
        mCamera = camera;

        mHolder = getHolder();
        mHolder.addCallback(this);

        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setOnTouchListener(mPreviewTouchListener);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "##### cameraSurfaceViewBack surfaceCreated +");

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setDisplayOrientation(90);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.e(TAG, "Error setting camera preview: " + e.getMessage());
        }
        Log.d(TAG, "##### cameraSurfaceViewBack surfaceCreated -");
    }

    public interface CameraSurfaceBackListener {
        void drawFocusIcon(float x, float y);
    }

    private CameraSurfaceBackListener mListener = null;
    public void setListener(CameraSurfaceBackListener listener) {
        mListener = listener;
    }
    public CameraSurfaceBackListener getListener() {
        return mListener;
    }
    private int mScreenOrientation = 0;

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "##### surfaceDestroyed");
        // empty.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        mWidth = w;
        mHeight = h;

        stopCameraPreview();
        refreshCamera(mCamera, mCameraBack);
        startCameraPreview();
    }

    private boolean mCameraBack = true;
    public void refreshCamera(Camera camera, boolean cameraBack) {
        mCameraBack = cameraBack;

        if(mHolder.getSurface() == null) return;
        setCamera(camera);

        Camera.Parameters parameters = mCamera.getParameters();
        mCamera.setDisplayOrientation(90);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        Camera.Size size = getBestPreviewSize(mWidth, mHeight);
        parameters.setPreviewSize(size.width, size.height);
        mCamera.setParameters(parameters);
        Log.d(TAG, "mCamera refreshCamera parameters set.");
    }

    public void setStillShotParam(boolean cameraBack) {
        Log.d(TAG, "###### setStillShotparam called +");
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setRotation(mScreenOrientation);
        parameters.setPictureSize(2560, 1920);
        mCamera.setParameters(parameters);
        Log.d(TAG, "###### setStillShotparam called -");
    }

    private Camera.Size getBestPreviewSize(int width, int height) {
        Camera.Size result = null;
        Camera.Parameters p = mCamera.getParameters();
        for (Camera.Size size : p.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result=size;
                } else {
                    int resultArea=result.width*result.height;
                    int newArea=size.width*size.height;

                    if (newArea>resultArea) {
                        result=size;
                    }
                }
            }
        }
        return result;
    }

    public void setCamera(Camera camera) {
        //method to set a camera instance
        mCamera = camera;
    }

    public void stopCameraPreview() {
        Log.d(TAG, "cameraSurfaceViewBack ###################### stopCameraPreview)+");
        // stop preview before making changes
        try {
            if (mCamera != null)
                mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        Log.d(TAG, "cameraSurfaceViewBack ###################### stopCameraPreview)-");
    }

    public void startCameraPreview() {
        Log.d(TAG, "cameraSurfaceViewBack ###################### startcameraPreview )+");
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            }
        } catch (Exception e) {
            Log.d(VIEW_LOG_TAG, "Error starting camera preview: mCameraback" + e.getMessage());
        }
        Log.d(TAG, "cameraSurfaceViewBack ###################### startcameraPreview )- ");
    }

    //Tap To Focus
    private Rect focusRect = null;
    private OnTouchListener mPreviewTouchListener = (new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP && mCameraBack && mCamera != null) {
                Log.d(TAG, "##### onTouch)+ ");
                Camera camera = mCamera;
                camera.cancelAutoFocus();
                focusRect = calculateTapArea(event.getX(), event.getY(), 1f);
                Log.d(TAG, "##### (X, Y) = (" + event.getX() + ", " + event.getY());
                Log.d(TAG, "##### (" + focusRect.left + ", " + focusRect.top + ", " + focusRect.right + ", " + focusRect.bottom + ")");

                if (mListener != null)
                    mListener.drawFocusIcon(event.getX(), event.getY());

                Camera.Parameters parameters = camera.getParameters();
                if (parameters.getFocusMode() != Camera.Parameters.FOCUS_MODE_AUTO) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }
                if (parameters.getMaxNumFocusAreas() > 0) {
                    List<Camera.Area> mylist = new ArrayList<Camera.Area>();
                    mylist.add(new Camera.Area(focusRect, 1000));
                    parameters.setFocusAreas(mylist);
                }

                try {
                    camera.cancelAutoFocus();
                    camera.setParameters(parameters);
//                    camera.startPreview();
                    camera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
//                            if (camera.getParameters().getFocusMode() != Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) {
//                                Camera.Parameters parameters = camera.getParameters();
//                                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//                                if (parameters.getMaxNumFocusAreas() > 0) {
//                                    //Auto-focuses 3 times when setFocusAreas(null).
//                                    parameters.setFocusAreas(null);
//                                }
//                                camera.setParameters(parameters);
//                                camera.startPreview();
//                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    });

    /**
     * Convert touch position x:y to {@link Camera.Area} position -1000:-1000 to 1000:1000.
     */
    private int focusAreaSize = 100;
    private Rect calculateTapArea(float x, float y, float coefficient) {
        int width = 0;
        int height = 0;
        float input_x = 0.0f;
        float input_y = 0.0f;
        if (mScreenOrientation == 90) {
            width = getWidth();
            height = getHeight();
            input_x = x;
            input_y = y;
        }
        else {
            width = getHeight();
            height = getWidth();
            input_x = y;
            input_y = (width - x);
        }
        Log.d(TAG, "##### width = " + width + ", height = " + height);

        float focus_x = 0.0f;
        float focus_y = 0.0f;
        float unit_x = ((width/2.0f)/1000.0f);
        float unit_y = ((height/2.0f)/1000.0f);
        int left = 0;
        int top = 0;

        Log.d(TAG, "unit_x = " + unit_x + ", unit_y = " + unit_y);
        if (input_x <= width/2 && input_y <= height/2) {   //1
            //-1000< x <=0, -1000 < y <= 0
            focus_x = -1 * (input_x * unit_x);
            focus_y = -1 * (input_y * unit_y);

            Log.d(TAG, "focus_x = " + focus_x + ", focus_y = " + focus_y);

            left = clamp( (int)Math.round(focus_x - focusAreaSize / 2), -1000, 0);
            top = clamp ( (int)Math.round(focus_y - focusAreaSize / 2), -1000, 0);
        }
        else if(input_x > width/2 && input_y <= height/2) { //2
            focus_x = (input_x - width/2) * unit_x;
            focus_y = -1 * (input_y * unit_y);

            Log.d(TAG, "focus_x = " + focus_x + ", focus_y = " + focus_y);

            left = clamp( (int)Math.round(focus_x - focusAreaSize / 2), 0, 1000);
            top = clamp ( (int)Math.round(focus_y - focusAreaSize / 2), -1000, 0);
        }
        else if(input_x <= width/2 && input_y > height/2) { //3
            focus_x = -1 * (input_x * unit_x);
            focus_y = (input_y - height/2) * unit_y;

            Log.d(TAG, "focus_x = " + focus_x + ", focus_y = " + focus_y);

            left = clamp( (int)Math.round(focus_x - focusAreaSize / 2), -1000, 0);
            top = clamp ( (int)Math.round(focus_y - focusAreaSize / 2), 0, 1000);
        }
        else if(input_x > width/2 && input_y > height/2) {  //4
            focus_x = (input_x - width/2) * unit_x;
            focus_y = (input_y - height/2) * unit_y;

            Log.d(TAG, "focus_x = " + focus_x + ", focus_y = " + focus_y);

            left = clamp( (int)Math.round(focus_x - focusAreaSize / 2), 0, 1000);
            top = clamp ( (int)Math.round(focus_y - focusAreaSize / 2), 0, 1000);
        }

        return new Rect(left, top, left+(focusAreaSize/2), top+(focusAreaSize/2));
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
}


