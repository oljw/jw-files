package com.samsung.retailexperience.retailhero.view.camera_view;

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
 */
public class CameraSurfaceViewFront extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = CameraSurfaceViewFront.class.getSimpleName();

    private Camera mCamera;
    private Context mContext;
    private SurfaceHolder mHolder;

    private int mWidth = 0;
    private int mHeight = 0;
    private boolean mIsCameraBack = true;


    public CameraSurfaceViewFront(Context context, Camera camera) {
        super(context);
        mContext = context;
        mCamera = camera;

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.e(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public interface CameraSurfaceFrontListener {
    }

    private CameraSurfaceFrontListener mListener = null;
    public void setListener(CameraSurfaceFrontListener listener) {
        mListener = listener;
    }
    public CameraSurfaceFrontListener getListener() {
        return mListener;
    }
    private int mScreenOrientation = 0;

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        mWidth = w;
        mHeight = h;

        stopCameraPreview();
        refreshCamera(mCamera, mIsCameraBack);
        startCameraPreview();
    }

    public void refreshCamera(Camera camera, boolean cameraBack) {
        mIsCameraBack = cameraBack;

        if(mHolder.getSurface() == null) return;
        setCamera(camera);

        Camera.Parameters parameters = mCamera.getParameters();
        mCamera.setDisplayOrientation(90);
        Camera.Size size = getBestPreviewSize(mWidth, mHeight);
        parameters.setPreviewSize(size.width, size.height);
        mCamera.setParameters(parameters);
    }

    public void setStillShotParam(boolean cameraBack) {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setRotation(mScreenOrientation);
        parameters.setPictureSize(2592, 1944);
        mCamera.setParameters(parameters);
    }

    private Camera.Size getBestPreviewSize(int width, int height) {
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
        //method to set a camera instance
        mCamera = camera;
    }

    public void stopCameraPreview() {
        // stop preview before making changes
        try {
            if (mCamera != null)
                mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
    }
    public void startCameraPreview() {
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            }
        } catch (Exception e) {
            Log.d(VIEW_LOG_TAG, "Error starting camera preview: mCameraback" + e.getMessage());
        }
    }
}


