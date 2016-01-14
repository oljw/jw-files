package com.jaewoolee.api19camera;

/**
 * Created by Jaewoo on 1/8/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;

import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraPreview";

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private CameraPreviewListener mListener = null;
    private List<Camera.Size> mSupportedPreviewSizes;


    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
        for(Camera.Size str: mSupportedPreviewSizes)
            Log.e(TAG, str.width + "/" + str.height);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        Log.d(TAG, "##### CameraPreview : mHolder = " + mHolder);
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public CameraPreview(Context context, Camera camera, CameraPreviewListener listener) {
        this(context, camera);
        mListener = listener;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "##### surfaceCreated)+ ");
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(mHolder);

            Log.d(TAG, "##### surfaceCreated : mHolder = " + mHolder);
            Log.d(TAG, "##### surfaceCreated : holder = " + holder);

            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "##### Error setting camera preview: " + e.getMessage());
        }
        Log.d(TAG, "##### surfaceCreated)- ");
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "##### surfaceDestroyed)+ ");

        // empty. Take care of releasing the Camera preview in your activity.

        Log.d(TAG, "##### surfaceDestroyed)- ");
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Log.d(TAG, "##### surfaceChanged)+ ");

        if (mCameraMode == false) return;

        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        Camera.Parameters parameters = mCamera.getParameters();

        Log.d(TAG, "##### parameters = " + parameters);

        mCamera.setDisplayOrientation(90);
        parameters.setPictureSize(4032, 3024);
        parameters.setRotation(90);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        readRotation();

        Camera.Size size = null;
        if (mCameraMode)
            size = getBestPreviewSize(w, h);
        else
            size = getBestPreviewSizeForFull(w, h, parameters);

        parameters.setPreviewSize(size.width, size.height);
        mCamera.setParameters(parameters);
        if (mListener != null)
            mListener.setSurfaceViewSize(w, h);

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }

        Log.d(TAG, "##### surfaceChanged)- ");
    }

    public void readRotation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        int rotation = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRotation();
        Camera.Parameters parameters = mCamera.getParameters();

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            parameters.setRotation(90);
        }
        mCamera.setParameters(parameters);
    }

    public void stopCameraPreview() {
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
    }
    public void startCameraPreview() {
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(VIEW_LOG_TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    boolean mCameraMode = true;
    public void changeCameraMode(boolean bCamera, int w, int h) {
        Log.d(TAG, "##### changeCameraMode)+ bCamera = " + bCamera);
        mCameraMode = bCamera;

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        Camera.Parameters parameters = mCamera.getParameters();
        mCamera.setDisplayOrientation(90);
        parameters.setRotation(270);
        parameters.setPictureSize(2592, 1944);


        Camera.Size size;
        if (bCamera) {
            size = getBestPreviewSize(w, h);
        } else {
            size = getBestPreviewSizeForFull(w, h, parameters);
        }

        Log.d(TAG, "w = " + w + ", h = " + h);
        Log.d(TAG, "Preview : width = " + size.width + ", height = " + size.height);

        parameters.setPreviewSize(size.width, size.height);
        mCamera.setParameters(parameters);

        Log.d(TAG, "##### changeCameraMode)- " );
    }

    private Camera.Size getBestPreviewSize(int width, int height) {
        Log.d(TAG, "getBestPreviewSize called");

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

    public void refreshCamera(Camera camera) {
        Log.d(TAG, "##### refreshCamera)+ ");

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        camera.setDisplayOrientation(90);

        // start preview with new settings
        setCamera(camera);
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(VIEW_LOG_TAG, "Error starting camera preview: " + e.getMessage());
        }

        Log.d(TAG, "##### refreshCamera)- ");
    }

    public void setCamera(Camera camera) {
        Log.d(TAG, "setCamera called");

        //method to set a camera instance
        mCamera = camera;

    }



}