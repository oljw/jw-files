package com.jaewoolee.api19camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraActivity extends Activity {

    private static final String TAG = "CameraActivity";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static  final int FOCUS_AREA_SIZE = 300;

    private boolean isRecording = false;
    private boolean cameraFront = false;

    private Camera.PictureCallback mPicture;
    private Context mContext;
    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private com.jaewoolee.api19camera.CameraPreview mPreview;

    private CameraPreviewListener cameraPreviewListener = new CameraPreviewListener() {
        @Override
        public void setSurfaceViewSize(int w, int h) {
            Log.d(TAG, "##### CURRENT SURFACE VIEW SIZE : " + w + " x " + h);
            mPreviewW = w;
            mPreviewH = h;
        }
    };

    private int mPreviewW;
    private int mPreviewH;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");

        super.onCreate(savedInstanceState);

        //remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_camera_test);

        // Create an instance of Camera
        mCamera = getCameraInstance();
        Log.d(TAG, "##### mCamera = " + mCamera);

        mPreview = new com.jaewoolee.api19camera.CameraPreview(this, mCamera, cameraPreviewListener);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) preview.getLayoutParams();
        preview.setLayoutParams(params);

        //STOP BUTTON LISTENER
        final ImageButton camcorder_stop_button = (ImageButton) findViewById(R.id.camcorder_stop_btn);
        camcorder_stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "##### go back to capture photo screen");

                if (isRecording) {
                    // stop recording and release camera
                    mMediaRecorder.stop();  // stop the recording
                    releaseMediaRecorder(); // release the MediaRecorder object
                    isRecording = false;
                }

                mPreview.stopCameraPreview();
                mCamera = getCameraInstance();
                mPreview.setCamera(mCamera);
                mPreview.changeCameraMode(true, mPreviewW, mPreviewH);
                mPreview.startCameraPreview();

                FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
                ((LinearLayout) findViewById(R.id.top_menu)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.bottom_menu)).setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.BELOW, R.id.top_menu);
                params.addRule(RelativeLayout.ABOVE, R.id.bottom_menu);
                preview.setLayoutParams(params);
            }
        });

        //CAMCORDER START BUTTON LISTENER
        final ImageButton camcorder_start_button = (ImageButton) findViewById(R.id.camcorder_start_btn);
        camcorder_start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "##### CHANGE CAMERA TO CAMCORDER !!!");

                ((LinearLayout) findViewById(R.id.top_menu)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.bottom_menu)).setVisibility(View.GONE);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.
                        LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
                preview.setLayoutParams(params);



                mPreview.stopCameraPreview();
                mPreview.changeCameraMode(false, mPreviewW, mPreviewH);
                initializeCamera();
                mPreview.startCameraPreview();
            }
        });

        //switch button
        ImageButton switchCamera = (ImageButton) findViewById(R.id.button_change);
        switchCamera.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get the number of cameras
                        int camerasNumber = Camera.getNumberOfCameras();
                        if (camerasNumber > 1) {
                            //release the old camera instance
                            //switch camera, from the front and the back and vice versa

                            releaseCamera();
                            chooseCamera();
                        } else {
                            Toast toast = Toast.makeText(mContext, "Sorry, your phone has only one camera!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });

        // Add a listener to the Photo Capture button
        ImageButton captureButton = (ImageButton) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick captureButton called called");

                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );

        mPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    focusOnTouch(event);
                }
                return true;
            }
        });
        addDummyButtons();
    }

    // initialize video camera
    private void initializeCamera() {
        Log.d(TAG, "##### initializeCamera)+ ");
        if (prepareVideoRecorder()) {
            mMediaRecorder.start();

            isRecording = true;
        } else {
            releaseMediaRecorder();
        }
        Log.d(TAG, "##### initializeCamera)- ");
    }

    private void focusOnTouch(MotionEvent event) {
        if (mCamera != null ) {

            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.getMaxNumMeteringAreas() > 0){
                Log.i(TAG,"fancy !");
                Rect rect = calculateFocusArea(event.getX(), event.getY());

                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
                meteringAreas.add(new Camera.Area(rect, 800));
                parameters.setFocusAreas(meteringAreas);

                mCamera.setParameters(parameters);
                mCamera.autoFocus(mAutoFocusTakePictureCallback);
            }else {
                mCamera.autoFocus(mAutoFocusTakePictureCallback);
            }
        }
    }

    private Rect calculateFocusArea(float x, float y) {
        int left = clamp(Float.valueOf((x / mPreview.getWidth()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        int top = clamp(Float.valueOf((y / mPreview.getHeight()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);

        return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);
    }

    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if (Math.abs(touchCoordinateInCameraReper)+focusAreaSize/2>1000){
            if (touchCoordinateInCameraReper>0){
                result = 1000 - focusAreaSize/2;
            } else {
                result = -1000 + focusAreaSize/2;
            }
        } else{
            result = touchCoordinateInCameraReper - focusAreaSize/2;
        }
        return result;
    }

    private Camera.AutoFocusCallback mAutoFocusTakePictureCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                // do something...
                Log.i("tap_to_focus","success!");
            } else {
                // do something...
                Log.i("tap_to_focus","fail!");
            }
        }
    };

    private int findFrontFacingCamera() {
        Log.d(TAG, "findFrontFacingCamera called");

        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        Log.d(TAG, "findBackFacingCamera called");

        int cameraId = -1;
        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    public void onResume() {
        Log.d(TAG, "onResume called");

        super.onResume();
        mCamera = Camera.open(findBackFacingCamera());
        mPicture = getPictureCallback();
        mPreview.refreshCamera(mCamera);
    }

    public boolean prepareVideoRecorder() {
        Log.d(TAG, "##### prepareVideoRecorder)+ ");

        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(90);
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setOrientationHint(90);
        Camera.Parameters params = mCamera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        mCamera.setParameters(params);

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_1080P));

        // Step 4: Set output file
        mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }

        Log.d(TAG, "##### prepareVideoRecorder)- ");
        return true;
    }

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

    private Camera.PictureCallback getPictureCallback() {
        Log.d(TAG, "getPictureCallBack called");

        Camera.PictureCallback mPicture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                if (pictureFile == null) {
                    Log.d(TAG, "Error creating media file, check storage permissions: ");      //+ e.getMessage());

                    return;
                }
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
                mPreview.refreshCamera(mCamera);
            }
        };
        return mPicture;
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause called");

        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        Log.d(TAG, "releaseMediaRecorder called");

        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        Log.d(TAG, "getOutputMediaFile called");

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }
     return mediaFile;
    }

    public void chooseCamera() {
        Log.d(TAG, "chooseCamera called");

        //if the camera preview is the front
        if (cameraFront) {
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {

                mCamera = Camera.open(cameraId);
                mPreview.refreshCamera(mCamera);
                mPreview.stopCameraPreview();
                mPreview.changeCameraMode(true, mPreviewW, mPreviewH);
                mPreview.startCameraPreview();
            }
        } else {
                int cameraId = findFrontFacingCamera();

                mCamera = Camera.open(cameraId);
                mPreview.refreshCamera(mCamera);
                mPreview.changeCameraMode(true, mPreviewW, mPreviewH);

                }
    }

    private void releaseCamera(){
        Log.d(TAG, "releaseCamera called");

        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    private void addDummyButtons() {
        //add mode button
        ImageButton modeButton = (ImageButton) findViewById(R.id.mode_button);
        modeButton.isShown();

        //add setting button
        ImageButton setting_button = (ImageButton) findViewById(R.id.setting_button);
        setting_button.isShown();

        //add ratio button
        ImageButton ratio_button = (ImageButton) findViewById(R.id.ratio_button);
        ratio_button.isShown();

        //add flash button
        ImageButton flash_button = (ImageButton) findViewById(R.id.flash_button);
        flash_button.isShown();

        //add timer button
        ImageButton timer_button = (ImageButton) findViewById(R.id.timer_button);
        timer_button.isShown();

        //add hdr button
        ImageButton hdr_button = (ImageButton) findViewById(R.id.hdr_button);
        hdr_button.isShown();

        //add effect button
        ImageButton effect_button = (ImageButton) findViewById(R.id.effect_button);
        effect_button.isShown();

        //add arrow button
        ImageButton arrow_button = (ImageButton) findViewById(R.id.arrow_button);
        arrow_button.isShown();

    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart called");

        super.onStart();
    }

    @Override
    public void onRestart() {
        Log.d(TAG, "onRestart called");

        super.onRestart();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop called");

        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy called");

        super.onDestroy();
    }

}

