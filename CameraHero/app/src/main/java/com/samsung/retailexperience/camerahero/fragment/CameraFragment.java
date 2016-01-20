package com.samsung.retailexperience.camerahero.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.samsung.retailexperience.camerahero.CameraHeroApplication;
import com.samsung.retailexperience.camerahero.R;
import com.samsung.retailexperience.camerahero.activity.MainActivity;
import com.samsung.retailexperience.camerahero.util.AppConsts;
import com.samsung.retailexperience.camerahero.view.CameraSurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private RelativeLayout mPreview = null;
    private Camera mCamera = null;
    private CameraSurfaceView mCameraSurface = null;
    private ImageView mFocusIcon = null;
    private MediaRecorder mMediaRecorder;

    private int mCameraId = 0;
    private boolean mCameraBack = true;
    private int mScreenOrientation = 90;
    private boolean isRecording = false;

    private MediaPlayer mMediaPlayer = null;

    @Override
    public void onViewCreated(View view) {
        Log.d(TAG, "##### CameraFragment onViewCreated Called");

        mTopMenuBar = (TopMenuBarFragment) getChildFragmentManager().findFragmentById(R.id.top_fragment);
        mBottomMenuBar = (BottomMenuBarFragment) getChildFragmentManager().findFragmentById(R.id.bottom_fragment);
        mBottomMenuBar.setListener(this);

        mCamera = getCameraInstance(-1);
        mPreview = (RelativeLayout) view.findViewById(R.id.camera_preview);

        mCameraSurface = new CameraSurfaceView((MainActivity)getActivity(), mCamera);
        mCameraSurface.setListener(this);
        mPreview.addView(mCameraSurface);

        mFocusIcon = (ImageView) view.findViewById(R.id.focus_icon);
        mFocusIcon.bringToFront();

        mMediaPlayer = MediaPlayer.create(CameraHeroApplication.getContext(), R.raw.camera_shutter_1);
    }

    @Override
    public void onDestroyView () {
        mMediaPlayer.release();
        mMediaPlayer = null;

        releaseMediaRecorder();
        releaseCamera();

        super.onDestroyView();
    }

    @Override
    public void onBackPressed() {
        changeFragment(AppConsts.UIState.valueOf(getFragmentModel().getActionBackKey()),
                AppConsts.TransactionDir.TRANSACTION_DIR_NONE);
    }

    @Override
    public void onStillClicked() {
        Log.d(TAG, "onClick captureButton called called");

        mCameraSurface.setStillShotParam(mCameraBack);

        // get an image from the camera
        mCamera.takePicture(shutter, null, preview);
    }

    @Override
    public void onSwitchClicked() {
        //get the number of cameras
        int camerasNumber = Camera.getNumberOfCameras();
        if (camerasNumber > 1) {
            //release the old camera instance
            //switch camera, from the front and the back and vice versa

            chooseCamera(!mCameraBack);
        } else {
            //dude
        }
    }

    @Override
    public void onVideoClicked() {
        Log.d(TAG, "##### CHANGE CAMERA TO CAMCORDER !!!");

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
                    mMediaRecorder.stop();  // stop the recording
                    releaseMediaRecorder(); // release the MediaRecorder object
                    isRecording = false;

                    Log.d(TAG, "##### mStopbtn : mCameraBack = " + mCameraBack);

                    chooseCamera(mCameraBack);
                }
            }
        });
    }

    @Override
    public void onGalleryClicked() {
        changeFragment(AppConsts.UIState.UI_STATE_GALLERY, AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);

    }

    private void releaseCamera(){
        Log.d(TAG, "##### releaseCamera called");

        if (mCamera != null){
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
        if (mCamera == null)
            chooseCamera(true);
    }

    public static Camera getCameraInstance(int cameraId){
        Log.d(TAG, "getCameraInstance called");

        Camera c = null;
        try {
            if (cameraId == -1)
                c = Camera.open();
            else
                c = Camera.open(cameraId); // attempt to get a Camera instance
            Log.d(TAG, "##### Camera Opened");
        }
        catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e(TAG, "Camera is not available or in use or does not exist: " + e.getMessage());
        }
        return c; // returns null if camera is unavailable
    }

    private Camera.ShutterCallback shutter = new Camera.ShutterCallback(){
        public void onShutter() {
            // No action to be perfomed on the Shutter callback.
            Log.d(TAG, "shutter !!");
            mMediaPlayer.start();
        }
    };

    private Camera.PictureCallback preview = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d(TAG, "getPictureCallBack called");
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
            chooseCamera(mCameraBack);
        }
    };

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
                break;
            }
        }
        return cameraId;
    }

    //Choose Camera Method
    public void chooseCamera(boolean cameraBack) {
        Log.d(TAG, "chooseCamera called : cameraBack = " + cameraBack);

        mCameraSurface.stopCameraPreview();

        releaseCamera();
        int cameraId = 0;

        //if the camera preview is the front
        if (cameraBack) {
            cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                Log.d(TAG, "##### chooseCamera : cameraId = " + cameraId);
                mCamera = getCameraInstance(cameraId);
                mCameraSurface.refreshCamera(mCamera, cameraBack);
            }
        } else {
            cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                Log.d(TAG, "##### chooseCamera : cameraId = " + cameraId);
                mCamera = getCameraInstance(cameraId);
                mCameraSurface.refreshCamera(mCamera, cameraBack);
            }
        }
        mCameraSurface.startCameraPreview();
        mCameraBack = cameraBack;
        Log.d(TAG, "##### CHOOSECAMERA : mCameraBack = " + mCameraBack);
        mCameraId = cameraId;
    }

    public boolean prepareVideoRecorder() {
        Log.d(TAG, "##### prepareVideoRecorder)+ ");

        mCameraSurface.stopCameraPreview();

        mCamera = getCameraInstance(mCameraId);
        mCameraSurface.refreshCamcorder(mCamera, mCameraBack);

        mMediaRecorder = new MediaRecorder();
        if (mCameraBack)
            mMediaRecorder.setOrientationHint(mScreenOrientation);
        else {
            if (mScreenOrientation == 90)
                mMediaRecorder.setOrientationHint(270);
            else
                mMediaRecorder.setOrientationHint(mScreenOrientation);
        }

        Log.d(TAG, "##### prepareVideoRecorder : mCameraBack = " + mCameraBack);

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
        mCameraSurface.startCameraPreview();
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
    public void changeScreenOrientation(int screenOrientation) {
        mScreenOrientation = screenOrientation;
    }

    ObjectAnimator rotateIconAnimator = null;
    @Override
    public void drawFocusIcon(final float x, final float y) {
        if (rotateIconAnimator != null && rotateIconAnimator.isRunning())
            rotateIconAnimator.cancel();

        rotateIconAnimator = ObjectAnimator.ofFloat(mFocusIcon , "rotation", 0f, 180f);
        rotateIconAnimator.setDuration(150);
        rotateIconAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mFocusIcon.setVisibility(View.VISIBLE);

                ViewGroup.MarginLayoutParams marginLayoutParams =
                        (ViewGroup.MarginLayoutParams)mFocusIcon.getLayoutParams();
                marginLayoutParams.setMargins
                        ((int)(x - 300/2),
                        (int)(y - 300/2),
                        (int)(-x + 300/2),
                        (int)(-y + 300 / 2));
                mFocusIcon.setLayoutParams(marginLayoutParams);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mFocusIcon.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mFocusIcon.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        rotateIconAnimator.start();
    }
}
