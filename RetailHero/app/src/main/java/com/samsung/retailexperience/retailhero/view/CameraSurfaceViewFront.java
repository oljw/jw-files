package com.samsung.retailexperience.retailhero.view;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by JW on 1/23/2016.
 */
public class CameraSurfaceViewFront extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = CameraSurfaceViewFront.class.getSimpleName();

    private Context mContext;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int mWidth = 0;
    private int mHeight = 0;


    public CameraSurfaceViewFront(Context context, Camera camera) {
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
    }

    public interface CameraSurfaceFrontListener {
        void changeScreenOrientation(int screenOrientation);
    }

    private CameraSurfaceFrontListener mListener = null;
    public void setListener(CameraSurfaceFrontListener listener) {
        mListener = listener;
    }
    public CameraSurfaceFrontListener getListener() {
        return mListener;
    }
    private OrientationEventListener mOrientationListener;
    private int mScreenOrientation = 0;

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "##### surfaceCreated +");

        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(mHolder);
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
        mWidth = w;
        mHeight = h;

        stopCameraPreview();
        refreshCamera(mCamera, mCameraFront);
        startCameraPreview();
    }

    boolean mCameraFront = true;
    public void refreshCamera(Camera camera, boolean cameraFront) {
        Log.d(TAG, "###################### refreshCamera)+");
        mCameraFront = cameraFront;

        if(mHolder.getSurface() ==null) return;
        setCamera(camera);

        Camera.Parameters parameters = mCamera.getParameters();
        mCamera.setDisplayOrientation(90);
        Camera.Size size = getBestPreviewSize(mWidth, mHeight);
        parameters.setPreviewSize(size.width, size.height);
        mCamera.setParameters(parameters);

        Log.d(TAG, "###################### refreshCamera)- ");
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

    public void setStillShotParam(boolean cameraBack) {
        Camera.Parameters parameters = mCamera.getParameters();
        if (cameraBack) {
            if (mScreenOrientation == 90)
                parameters.setRotation(270);
                parameters.setPictureSize(2592, 1944);        }
        else {
            if (mScreenOrientation == 90)
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
