package com.samsung.retailexperience.retailhero.view.camera_view;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by JW on 1/14/16.
 * This Class is used for Hero/Hero2 B2C's Photo Quality Demo.
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = CameraSurfaceView.class.getSimpleName();

    private Context mContext;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int mWidth = 0;
    private int mHeight = 0;

    public CameraSurfaceView(Context context, Camera camera) {
        super(context);
        mContext = context;
        mCamera = camera;

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public interface CameraSurfaceListener {

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

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setDisplayOrientation(90);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.e(TAG, "##### Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "##### surfaceDestroyed");
        // empty.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Log.d(TAG, "##### surfaceChanged called -");
        mWidth = w;
        mHeight = h;

        stopCameraPreview();
        refreshCamera(mCamera, mCameraBack);
        startCameraPreview();
        Log.d(TAG, "##### surfaceChanged called -");
    }

    boolean mCameraBack = true;
    public void refreshCamera(Camera camera, boolean cameraBack) {
        Log.d(TAG, "###################### refreshCamera)+");
        mCameraBack = cameraBack;

        if(mHolder.getSurface() == null) return;
        setCamera(camera);

        Camera.Parameters parameters = mCamera.getParameters();
        mCamera.setDisplayOrientation(90);
        Camera.Size size = getBestPreviewSize(mWidth, mHeight);
        parameters.setPreviewSize(size.width, size.height);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.setParameters(parameters);
        Log.d(TAG, "###################### refreshCamera)- ");
    }

    private Camera.Size getBestPreviewSize(int width, int height) {
        Log.d(TAG, "#####getBestPreviewSize called");

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
            Log.d(TAG, "##### Camera Preview Stopped");
        } catch (Exception e) {
            Log.d(VIEW_LOG_TAG, "###### Error stopping camera preview: " + e.getMessage());
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
                Log.d(TAG, "##### Camera Preview Started");
            }
        } catch (Exception e) {
            Log.d(VIEW_LOG_TAG, "###### Error starting camera preview: " + e.getMessage());
        }
        Log.d(TAG, "###################### startCameraPreview)- ");
    }
}


