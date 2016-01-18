package com.samsung.retailexperience.camerahero.fragment;

import android.app.FragmentTransaction;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.samsung.retailexperience.camerahero.R;
import com.samsung.retailexperience.camerahero.activity.MainActivity;
import com.samsung.retailexperience.camerahero.util.AppConsts;
import com.samsung.retailexperience.camerahero.view.CameraSurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by icanmobile on 1/14/16.
 */
public class CameraFragment extends BaseCameraFragment
    implements BottomMenuBarFragment.BottomMenuBarListener,
    CameraSurfaceView.CameraSurfaceListener {
    private static final String TAG = CameraFragment.class.getSimpleName();

    public static CameraFragment newInstance(String fragmentModel) {
        Log.d(TAG, "##### CameraFragment newInstance Called");

        CameraFragment fragment = new CameraFragment();

        Bundle args = new Bundle();
        args.putString(AppConsts.ARG_JSON_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private TopMenuBarFragment mTopMenuBar = null;
    private BottomMenuBarFragment mBottomMenuBar = null;
    private FrameLayout mPreview = null;
    private Camera mCamera = null;
    private CameraSurfaceView mCameraSurface = null;
    private Camera.PictureCallback mPicture;
    private MediaRecorder mMediaRecorder;
    private int mPreviewW;
    private int mPreviewH;

    private boolean cameraFront = false;
    private boolean isRecording = false;

    private CameraSurfaceView.CameraSurfaceListener cameraSurfaceListener = new CameraSurfaceView.CameraSurfaceListener() {
        @Override
        public void setSurfaceViewSize(int w, int h) {
            mPreviewW = w;
            mPreviewH = h;
        }
    };



    @Override
    public void onViewCreated(View view) {
        Log.d(TAG, "##### CameraFragment onViewCreated Called");

        mTopMenuBar = (TopMenuBarFragment) getChildFragmentManager().findFragmentById(R.id.top_fragment);
        mBottomMenuBar = (BottomMenuBarFragment) getChildFragmentManager().findFragmentById(R.id.bottom_fragment);
        mBottomMenuBar.setListener(this);

        Log.d(TAG, "GetCameraInstance ##############");
        mCamera = getCameraInstance();
        Log.d(TAG, "GotCameraInstance #########");

        mPreview = (FrameLayout) view.findViewById(R.id.camera_preview);
        mPreview.setOnTouchListener(mPreviewTouchListener);

        mCameraSurface = new CameraSurfaceView((MainActivity)getActivity(), mCamera);
        mCameraSurface.setListener(this);
        mPreview.addView(mCameraSurface);

    }

    @Override
    public void onBackPressed() {
        changeFragment(AppConsts.UIState.valueOf(getFragmentModel().getActionBackKey()),
                AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
    }

    @Override
    public void onStillClicked() {
//        Toast.makeText((MainActivity)getActivity(), "Still Clicked !!!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onClick captureButton called called");

        // get an image from the camera
        mCamera.takePicture(null, null, mPicture);
    }

    @Override
    public void onSwitchClicked() {
        //get the number of cameras
        int camerasNumber = Camera.getNumberOfCameras();
        if (camerasNumber > 1) {
            //release the old camera instance
            //switch camera, from the front and the back and vice versa

            releaseCamera();
            chooseCamera();
        } else {
            //dude
        }
    }

    @Override
    public void onVideoClicked() {
        Log.d(TAG, "##### CHANGE CAMERA TO CAMCORDER !!!");
        mCameraSurface.videoMode(false);

        getFragmentManager().beginTransaction().hide(mTopMenuBar).hide(mBottomMenuBar).commit();

        if (prepareVideoRecorder()) {
            mMediaRecorder.start();
            isRecording = true;
        } else {
            releaseMediaRecorder();
        }

        ImageButton mStopbtn = (ImageButton) mView.findViewById(R.id.stop_button);
        mStopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTopMenuBar.isHidden()) {
                    getFragmentManager().beginTransaction().show(mTopMenuBar).commit();
                    getFragmentManager().beginTransaction().show(mBottomMenuBar).commit();
                }
                if (isRecording) {
                    // stop recording and release camera
                    mMediaRecorder.stop();  // stop the recording
                    releaseMediaRecorder(); // release the MediaRecorder object
                    mCamera.lock();         // take camera access back from MediaRecorder
                    isRecording = false;

                }
            }
        });
    }

    @Override
    public void onGalleryClicked() {
        Log.d(TAG, "##### Gallery Clicked!");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        Log.d(TAG, "##### Transaction begun");

        ft.replace(R.layout.fragment_gallery, new GalleryFragment(), "GalleryFragment");
        Log.d(TAG, "##### Replace fragment!");

        ft.commit();
        Log.d(TAG, "##### committed");

    }

    View.OnTouchListener mPreviewTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    };

    private void releaseCamera(){
        Log.d(TAG, "##### releaseCamera called");

        if (mCamera != null){
//            mCamera.stopPreview();
//            mCamera.setPreviewCallback(null);
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "##### onPause called");

        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    public void onResume() {
        Log.d(TAG, "onResume called");

        super.onResume();
        mCamera = Camera.open(findBackFacingCamera());
        mPicture = getPictureCallback();
        mCameraSurface.refreshCamera(mCamera);
    }


    public static Camera getCameraInstance(){
        Log.d(TAG, "getCameraInstance called");

        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
            Log.d(TAG, "##### Camera Opened");
        }
        catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e(TAG, "Camera is not available or in use or does not exist: " + e.getMessage());
        }
        return c; // returns null if camera is unavailable
    }

    //Picture CallBack Method
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
                mCameraSurface.refreshCamera(mCamera);
            }
        };
        return mPicture;
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

    //Find Back Facing Camera
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

    //Choose Camera Method
    public void chooseCamera() {
        Log.d(TAG, "chooseCamera called");

        //if the camera preview is the front
        if (cameraFront) {
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {

                mCamera = Camera.open(cameraId);
                mCameraSurface.refreshCamera(mCamera);

            }
        } else {
            int cameraId = findFrontFacingCamera();

            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview

                mCamera = Camera.open(cameraId);
                // mPicture = getPictureCallback();
                mCameraSurface.refreshCamera(mCamera);

            }

        }
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
        mMediaRecorder.setPreviewDisplay(mCameraSurface.getHolder().getSurface());

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

    //Release Media Recorder
    private void releaseMediaRecorder(){
        Log.d(TAG, "releaseMediaRecorder called");

        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    @Override
    public void setSurfaceViewSize(int w, int h) {

    }


//    private void setMenuBar() {
//        if (mTopMenuBar.isHidden()) {
//            getFragmentManager().beginTransaction().setCustomAnimations(
//                    R.animator.left_out, R.animator.left_in
//            ).show( mTopMenuBar ).setCustomAnimations(
//                    R.animator.right_out, R.animator.right_in
//            ).show( mBottomMenuBar ).commit();
//        }
//        else {
//            getFragmentManager().beginTransaction().setCustomAnimations(
//                    R.animator.left_in, R.animator.left_out
//            ).hide( mTopMenuBar ).setCustomAnimations(
//                    R.animator.right_in, R.animator.right_out
//            ).hide( mBottomMenuBar ).commit();
//        }
//    }
}
