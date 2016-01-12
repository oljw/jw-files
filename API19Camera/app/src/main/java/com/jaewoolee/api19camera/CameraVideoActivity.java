package com.jaewoolee.api19camera;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.io.IOException;

/**
 * Created by Jaewoo on 2016-01-12.
 */
public class CameraVideoActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = "CameraVideoActivity";

    private com.jaewoolee.api19camera.CameraPreview mPreview;
    private Camera mCamera;
    private SurfaceView preview=null;
    private SurfaceHolder previewHolder=null;
    private Camera camera=null;
    private SurfaceView cameraSurfaceView = null;
    private SurfaceHolder cameraSurfaceHolder = null;
    private SurfaceHolder mHolder;

    RelativeLayout relativeLayout;



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
                    }
                }
        );
//        // Create our Preview view and set it as the content of our activity.
//        mPreview = new com.jaewoolee.api19camera.CameraPreview(this, mCamera);
//        preview.addView(mPreview);
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

        Camera.Size size = getBestPreviewSize(w, h);
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
    private Camera.Size getBestPreviewSize(int width, int height) {
        Log.d(TAG, "getBestPreviewSize called");

        Camera.Size result=null;
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


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed called");

        // empty. Take care of releasing the Camera preview in your activity.
    }

}
