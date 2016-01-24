package com.samsung.retailexperience.camerahero.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Matrix;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.samsung.retailexperience.camerahero.R;

/**
 * Created by JW on 1/14/16.
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = CameraSurfaceView.class.getSimpleName();

    private Context mContext;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int mWidth = 0;
    private int mHeight = 0;
//    private ImageButton mStillBtn;


    public CameraSurfaceView(Context context, Camera camera) {
        super(context);
        mContext = context;
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        Log.d(TAG, "##### getHolder and addCallBack +");

        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        setOnTouchListener(mPreviewTouchListener);
    }

    public interface CameraSurfaceListener {
        void changeScreenOrientation(int screenOrientation);
        void drawFocusIcon(float x, float y);
    }

    private CameraSurfaceListener mListener = null;
    public void setListener(CameraSurfaceListener listener) {
        mListener = listener;
    }
    public CameraSurfaceListener getListener() {
        return mListener;
    }
    private OrientationEventListener mOrientationListener;
    private int mScreenOrientation = 0;

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "##### surfaceCreated +");

//        mStillBtn =(ImageButton) findViewById(R.id.still_button);

        mOrientationListener = new OrientationEventListener(mContext, SensorManager.SENSOR_DELAY_UI) {
            public void onOrientationChanged (int orientation) {
                //Log.d (TAG, "onOrientationChanged : " + orientation);
                int prevScreenOrientation = mScreenOrientation;

                if( orientation > 340 || (orientation >= 0 && orientation <= 20))
                    mScreenOrientation = 90;
                else if( orientation >= 250 && orientation <= 290 )
                    mScreenOrientation = 0;

                //icon change when orientation is changed.
                if( prevScreenOrientation != mScreenOrientation) {
                    Log.d(TAG, "####### PLEASE ROTATE ICON : " + mScreenOrientation);
                    if (mListener != null) {
                        mListener.changeScreenOrientation(mScreenOrientation);

//                        mStillBtn.setRotation(90);
                    }
                }
            }
        };
        mOrientationListener.enable();

        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            Log.d(TAG, "##### surfaceCreated : mholder = " + mHolder);
            mCamera.setPreviewDisplay(mHolder);
            Log.d(TAG, "##### surfaceCreated : holder = " + holder);

            Log.d(TAG, "##### surfaceCreated Start preview");
            mCamera.startPreview();
            Log.d(TAG, "#####  surfaceCreated preview started");
        } catch (IOException e) {
            Log.e(TAG, "Error setting camera preview: " + e.getMessage());
        }
        Log.d(TAG, "##### surfaceCreated -");
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "##### surfaceDestroyed");
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (!mVideoMode) return;

        mWidth = w;
        mHeight = h;

        stopCameraPreview();
        refreshCamera(mCamera, mCameraBack);
        startCameraPreview();
    }
    boolean mVideoMode = true;
    boolean mCameraBack = true;
    public void refreshCamera(Camera camera, boolean cameraBack) {
        Log.d(TAG, "###################### refreshCamera)+ cameraBack = " + cameraBack);
        mVideoMode = true;
        mCameraBack = cameraBack;

        if(mHolder.getSurface() ==null) return;
        setCamera(camera);

        Camera.Parameters parameters = mCamera.getParameters();
        mCamera.setDisplayOrientation(90);
        Camera.Size size = getBestPreviewSize(mWidth, mHeight);
        parameters.setPreviewSize(size.width, size.height);
        if (cameraBack)
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.setParameters(parameters);

        Log.d(TAG, "###################### refreshCamera)- ");
    }

    public void refreshCamcorder(Camera camera, boolean cameraBack) {
        Log.d(TAG, "###################### refreshCamcorder)+ ");
        mVideoMode = false;
        mCameraBack = cameraBack;

        if(mHolder.getSurface() ==null) return;
        setCamera(camera);

        mCamera.setDisplayOrientation(90);

        Camera.Parameters parameters = mCamera.getParameters();
        if (cameraBack)
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        Camera.Size size = getBestPreviewSizeForFull(mWidth, mHeight, parameters);
        parameters.setPreviewSize(size.width, size.height);
        mCamera.setParameters(parameters);

        Log.d(TAG, "###################### refreshCamcorder)- ");
    }

    private Camera.Size getBestPreviewSize(int width, int height) {
        Log.d(TAG, "###getBestPreviewSize called");

        Camera.Size result = null;
        Camera.Parameters p = mCamera.getParameters();
        for (Camera.Size size : p.getSupportedPreviewSizes()) {
            if (size.width<=width && size.height<=height) {
                if (result==null) {
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


    private Camera.Size getBestPreviewSizeForFull(int width, int height, Camera.Parameters parameters){
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();

        Camera.Size bestSize = sizeList.get(0);

        for(int i = 1; i < sizeList.size(); i++){
            if((sizeList.get(i).width * sizeList.get(i).height) >
                    (bestSize.width * bestSize.height)){
                bestSize = sizeList.get(i);
            }
        }
        return bestSize;
    }

    public void setStillShotParam(boolean cameraBack) {
        Camera.Parameters parameters = mCamera.getParameters();
        if (cameraBack) {
            parameters.setRotation(mScreenOrientation);
            parameters.setPictureSize(4032, 3024);
        }
        else {
            if (mScreenOrientation == 90)
                parameters.setRotation(270);
            else
                parameters.setRotation(mScreenOrientation);

            parameters.setPictureSize(2592, 1944);
        }
        mCamera.setParameters(parameters);
    }


    public void setCamera(Camera camera) {
        Log.d(TAG, "####setCamera called");

        //method to set a camera instance
        mCamera = camera;
    }

    public void stopCameraPreview() {
        Log.d(TAG, "###################### stopCameraPreview)+ ");
        // stop preview before making changes
        try {
            if (mCamera != null)
                mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        Log.d(TAG, "###################### stopCameraPreview)- ");
    }
    public void startCameraPreview() {
        Log.d(TAG, "###################### startCameraPreview)+ ");
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            }
        } catch (Exception e) {
            Log.d(VIEW_LOG_TAG, "Error starting camera preview: " + e.getMessage());
        }
        Log.d(TAG, "###################### startCameraPreview)- ");
    }

    //tap to focus example
    private Rect focusRect = null;
    private OnTouchListener mPreviewTouchListener = (new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && mCameraBack && mCamera != null) {
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
                    camera.startPreview();
                    camera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (camera.getParameters().getFocusMode() != Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) {
                                Camera.Parameters parameters = camera.getParameters();
                                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                                if (parameters.getMaxNumFocusAreas() > 0) {
                                    parameters.setFocusAreas(null);
                                }
                                camera.setParameters(parameters);
                                camera.startPreview();
                            }
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
    private int focusAreaSize = 300;
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


