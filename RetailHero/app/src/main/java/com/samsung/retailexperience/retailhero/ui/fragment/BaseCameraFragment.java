package com.samsung.retailexperience.retailhero.ui.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.ui.activity.MainActivity;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.BottomMenuBarFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.TopMenuBarFragment;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.view.CameraSurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by icanmobile on 1/14/16.
 */
public class BaseCameraFragment extends BaseVideoFragment
    implements BottomMenuBarFragment.BottomMenuBarListener,
    CameraSurfaceView.CameraSurfaceListener {
    private static final String TAG = BaseCameraFragment.class.getSimpleName();

    public static BaseCameraFragment newInstance(String fragmentModel) {
        Log.d(TAG, "##### CameraFragment newInstance Called");

        BaseCameraFragment fragment = new BaseCameraFragment();

        Bundle args = new Bundle();
        args.putString(AppConsts.ARG_JSON_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    protected TopMenuBarFragment mTopMenuBar = null;
    protected BottomMenuBarFragment mBottomMenuBar = null;
    protected RelativeLayout mPreview = null;
    protected Camera mCamera = null;
    protected CameraSurfaceView mCameraSurface = null;
    protected ImageView mFocusIcon = null;
    protected ImageView mGallerybtn = null;
    protected String path = "/sdcard/Pictures/MyCameraApp";

    protected int mCameraId = 0;
    protected boolean mCameraBack = true;
    protected int mScreenOrientation = 90;
    protected MediaPlayer mediaPlayer;

    public void onViewCreated(View view) {
        Log.d(TAG, "##### CameraFragment onViewCreated Called");

        mTopMenuBar = (TopMenuBarFragment) getChildFragmentManager().findFragmentById(R.id.top_fragment);
        mBottomMenuBar = (BottomMenuBarFragment) getChildFragmentManager().findFragmentById(R.id.bottom_fragment);
        mBottomMenuBar.setListener(this);

        mCamera = getCameraInstance(-1);
        mPreview = (RelativeLayout) view.findViewById(R.id.camera_preview);

        mediaPlayer.create(getActivity(), R.raw.camera_shutter_1);

        mCameraSurface = new CameraSurfaceView((MainActivity)getActivity(), mCamera);
        mCameraSurface.setListener(this);
        mPreview.addView(mCameraSurface);

        mFocusIcon = (ImageView) view.findViewById(R.id.focus_icon);
        mFocusIcon.bringToFront();

//        mGallerybtn =(ImageView) view.findViewById(R.id.gallery_button);
//        mGallerybtn.setRotation(90);
    }

    @Override
    public void onDestroyView () {
        releaseCamera();

        super.onDestroyView();
    }

    @Override
    public void onBackPressed() {
        Log.d("TAG", "##################CameraFragment onBackPressed called");
//        changeFragment(AppConsts.UIState.UI_STATE_CAMERA,
//                AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
    }

    @Override
    public void onStillClicked() {
        Log.d(TAG, "onClick captureButton called");
//        mCameraSurface.setStillShotParam(mCameraBack);

        // get an image from the camera
//        mCamera.takePicture(shutter, null, preview);
//        mediaPlayer.start();
        Toast.makeText(getActivity(), "스틸샷찍힘", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSwitchClicked() {
        //get the number of cameras
        int camerasNumber = Camera.getNumberOfCameras();
        if (camerasNumber > 1) {
            chooseCamera(!mCameraBack);
        } else {
            //dude
        }
    }

    @Override
    public void onGalleryClicked() {
//        changeFragment(AppConsts.UIState.UI_STATE_GALLERY, AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
    }

    protected void releaseCamera(){
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
        releaseCamera();              // release the camera immediately on pause event
    }

    public void onResume() {
        Log.d(TAG, "onResume called");

        super.onResume();
        if (mCamera == null)
            chooseCamera(true);
    }

    @Override
    public void onSetDrawer() {

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

    protected Camera.ShutterCallback shutter = new Camera.ShutterCallback(){
        public void onShutter() {
            Log.d(TAG, "shutter !!");
        }
    };

    protected Camera.PictureCallback preview = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            FileOutputStream outStream = null;
            Calendar c = Calendar.getInstance();
            File videoDirectory = new File(path);

            if (!videoDirectory.exists()) {
                videoDirectory.mkdirs();
            }

            try {
                // Write to SD Card
                outStream = new FileOutputStream(path + c.getTime().getSeconds() + ".jpg");
                outStream.write(data);
                outStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }

            Bitmap realImage;
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 5;

            options.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared

            options.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future

            realImage = BitmapFactory.decodeByteArray(data,0,data.length,options);
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(path + c.getTime().getSeconds()
                        + ".jpg");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                Log.d("EXIF value",
                        exif.getAttribute(ExifInterface.TAG_ORIENTATION));
                if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                        .equalsIgnoreCase("1")) {
                    realImage = rotate(realImage, 90);
                } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                        .equalsIgnoreCase("8")) {
                    realImage = rotate(realImage, 90);
                } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                        .equalsIgnoreCase("3")) {
                    realImage = rotate(realImage, 90);
                } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                        .equalsIgnoreCase("0")) {
                    realImage = rotate(realImage, 90);
                }
            } catch (Exception e) {

            }
            mGallerybtn.setImageBitmap(realImage);
            chooseCamera(mCameraBack);
        }
    };

    public static Bitmap rotate(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, false);
    }

    protected int findFrontFacingCamera() {
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
    protected int findBackFacingCamera() {
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

    @Override
    public void changeScreenOrientation(int screenOrientation) {
        mScreenOrientation = screenOrientation;
    }

    protected ObjectAnimator rotateIconAnimator = null;
    @Override
    public void drawFocusIcon(final float x, final float y) {
        Log.d(TAG, "########## BaseCameraFragment drawFocusIcon");

        if (rotateIconAnimator != null && rotateIconAnimator.isRunning())
            rotateIconAnimator.cancel();

            rotateIconAnimator = ObjectAnimator.ofFloat(mFocusIcon , "rotation", 0f, 180f);
            rotateIconAnimator.setDuration(200);
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
