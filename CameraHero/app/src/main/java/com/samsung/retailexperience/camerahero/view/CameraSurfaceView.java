package com.samsung.retailexperience.camerahero.view;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by icanmobile on 1/14/16.
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = CameraSurfaceView.class.getSimpleName();

    private SurfaceHolder mHolder;
    private Camera mCamera;

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
        void onDummy();
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

            Log.d(TAG, "##### Start the goddamn preview");
            mCamera.startPreview();
            Log.d(TAG, "##### Goddamn preview started");
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
        Log.d(TAG, "##### surfaceChanged +");

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

        Camera.Size size = null;
        size = getBestPreviewSize(w, h);
        parameters.setPreviewSize(size.width, size.height);
        mCamera.setParameters(parameters);

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
        Log.d(TAG, "##### surfaceChanged -");
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

//    public void refreshCamera(Camera camera) {
//        Log.d(TAG, "##### refreshCamera)+ ");
//
//        if (mHolder.getSurface() == null) {
//            // preview surface does not exist
//            return;
//        }
//        // stop preview before making changes
//        try {
//            mCamera.stopPreview();
//        } catch (Exception e) {
//            // ignore: tried to stop a non-existent preview
//        }
//        // set preview size and make any resize, rotate or
//        // reformatting changes here
////        camera.setDisplayOrientation(90);
//
//        // start preview with new settings
//        setCamera(camera);
//        try {
//            mCamera.setPreviewDisplay(mHolder);
//            mCamera.startPreview();
//        } catch (Exception e) {
//            Log.d(VIEW_LOG_TAG, "Error starting camera preview: " + e.getMessage());
//        }
//
//        Log.d(TAG, "##### refreshCamera)- ");
//    }

    public void setCamera(Camera camera) {
        Log.d(TAG, "setCamera called");

        //method to set a camera instance
        mCamera = camera;

    }
}


