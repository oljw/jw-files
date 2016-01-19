package com.samsung.retailexperience.camerahero.view;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by JW on 1/14/16.
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = CameraSurfaceView.class.getSimpleName();

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int mWidth = 0;
    private int mHeight = 0;

    public CameraSurfaceView(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        Log.d(TAG, "##### getHolder and addCallBack +");

        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public interface CameraSurfaceListener {

        void setSurfaceViewSize(int w, int h);
    }

    private CameraSurfaceListener mListener = null;
    public void setListener(CameraSurfaceListener listener) {
        mListener = listener;
    }
    public CameraSurfaceListener getListener() {
        return mListener;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "##### surfaceCreated +");
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

//    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
//        Log.d(TAG, "############################################# surfaceChanged +");
////        if(mVideoMode == false) return;
//
//        try {
//            mCamera.stopPreview();
//        } catch (Exception e) {
//            // ignore: tried to stop a non-existent preview
//        }
//
//        mCamera.setDisplayOrientation(90);
//
//        Camera.Parameters parameters = mCamera.getParameters();
//        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//
//        Camera.Size size = null;
//        size = getBestPreviewSize(w, h);
//        parameters.setPreviewSize(size.width, size.height);
//
//        mCamera.setParameters(parameters);
//
////        refreshCamera(mCamera);
//
//        try {
//            mCamera.setPreviewDisplay(mHolder);
//            Log.d(TAG, "##### surfaceChanged start preview");
//
//            mCamera.startPreview();
//            Log.d(TAG, "##### surfaceChanged preview started");
//
//        } catch (Exception e){
//            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
//        }
//        Log.d(TAG, "############################################# surfaceChanged -");
//    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (!mVideoMode) return;

        mWidth = w;
        mHeight = h;

        stopCameraPreview();
        refreshCamera(mCamera, true);
        startCameraPreview();
    }
    boolean mVideoMode = true;

    public void refreshCamera(Camera camera, boolean cameraBack) {
        Log.d(TAG, "###################### refreshCamera)+ ");
        mVideoMode = true;

        if(mHolder.getSurface() ==null) return;
        setCamera(camera);

        Log.d(TAG, "##### refreshCamera : " + mWidth + " x " + mHeight);


        Camera.Parameters parameters = mCamera.getParameters();
//        if (cameraBack)
            mCamera.setDisplayOrientation(90);
//        else
//            mCamera.setDisplayOrientation(90);

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

        if(mHolder.getSurface() ==null) return;
        setCamera(camera);

//        if (cameraBack)
            mCamera.setDisplayOrientation(90);
//        else
//            mCamera.setDisplayOrientation(90);

        Camera.Parameters parameters = mCamera.getParameters();

//        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        Camera.Size size = getBestPreviewSizeForFull(mWidth, mHeight, parameters);
        Log.d(TAG, "refreshCamcorder : " + size.width + " x " + size.height);
        parameters.setPreviewSize(size.width, size.height);
        mCamera.setParameters(parameters);

        Log.d(TAG, "###################### refreshCamcorder)- ");
    }

    private Camera.Size getBestPreviewSize(int width, int height) {
        Log.d(TAG, "###getBestPreviewSize called");

        Camera.Size result = null;
        Camera.Parameters p = mCamera.getParameters();
        Log.d(TAG, "###getBestPreviewSize : mCamera.getParameters()");

        for (Camera.Size size : p.getSupportedPreviewSizes()) {
            Log.d(TAG, "###getBestPreviewSize : " + size.width + " x " + size.height);
            if (size.width<=width && size.height<=height) {
                if (result==null) {
                    Log.d(TAG, "###getBestPreviewSize : result==null : " + size.width + " x " + size.height);
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

        Log.d(TAG, "###getBestPreviewSize : result : " + result.width + " x " + result.height);
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
            parameters.setRotation(90);
            parameters.setPictureSize(4032, 3024);
        }
        else {
            parameters.setRotation(270);
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
}


