package com.jaewoolee.api19camera;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.List;

/**
 * Created by Jaewoo on 2016-01-12.
 */
public class CameraVideoActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = "CameraVideoActivity";

    private com.jaewoolee.api19camera.CameraPreview mPreview;
    private Camera mCamera;
    private SurfaceView cameraSurfaceView = null;
    private SurfaceHolder mHolder;

    private MediaRecorder mMediaRecorder;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.video_layout);

        // Create an instance of Camera
        mCamera = getCameraInstance();
        Log.d(TAG, "##### mCamera = " + mCamera);

        RelativeLayout relativeLayout  = (RelativeLayout) findViewById(R.id.container);
        relativeLayout.setDrawingCacheEnabled(true);

        cameraSurfaceView = (SurfaceView)
                findViewById(R.id.camera_preview);
        //  cameraSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(640, 480));
        mHolder = cameraSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);



        // Add a listener to the Video Capture button
        final ImageButton stopButton = (ImageButton) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //setContentView(R.layout.activity_camera);
                        Intent intent = new Intent(CameraVideoActivity.this, CameraActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        Camera.Parameters params = mCamera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        mCamera.setParameters(params);
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Log.d(TAG, "getCameraInstance called");

        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e(TAG, "Camera is not available or in use or does not exist: " + e.getMessage());
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
//            mCamera.setPreviewDisplay(holder);

            Log.d(TAG, "##### surfaceCreated : mHolder = " + mHolder);
            Log.d(TAG, "##### surfaceCreated : holder = " + holder);

            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "##### Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
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

        mCamera.setDisplayOrientation(90);

        Camera.Size size = getBestPreviewSize(w, h, parameters);

        Log.d(TAG, "w = " + w + ", h = " + h);
        Log.d(TAG, "Preview : width = " + size.width + ", height = " + size.height);

        parameters.setPreviewSize(size.width, size.height);

        mCamera.setParameters(parameters);


        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters){
        Camera.Size bestSize = null;
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();

        bestSize = sizeList.get(0);

        for(int i = 1; i < sizeList.size(); i++){
            Log.d(TAG, "##### getBestPreviewSize : width = " + sizeList.get(i).width + ", height = " + sizeList.get(i).height);
            if((sizeList.get(i).width * sizeList.get(i).height) >
                    (bestSize.width * bestSize.height)){
                bestSize = sizeList.get(i);
            }
        }

        return bestSize;
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed called");

        // empty. Take care of releasing the Camera preview in your activity.
    }

}
