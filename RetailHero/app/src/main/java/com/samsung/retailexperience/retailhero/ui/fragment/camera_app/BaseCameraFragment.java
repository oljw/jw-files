package com.samsung.retailexperience.retailhero.ui.fragment.camera_app;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.view.camera_view.CameraSurfaceView;

/**
 * Created by JW on 1/14/16.
 * This Class is used for Hero/Hero2 B2C's Photo Quality Demo.
 */

public class BaseCameraFragment extends BaseVideoFragment
    implements CameraSurfaceView.CameraSurfaceListener {

    private static final String TAG = BaseCameraFragment.class.getSimpleName();

    public static BaseCameraFragment newInstance(String fragmentModel) {
        BaseCameraFragment fragment = new BaseCameraFragment();

        Bundle args = new Bundle();
        args.putString(AppConsts.ARG_JSON_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    protected boolean mIsCameraBack = true;
    protected int mChapterIndex = -1;
    protected int mCameraId;
    protected CameraSurfaceView mCameraSurface = null;
    protected RelativeLayout mPreview = null;
    protected MediaPlayer cameraShutter;
    protected Camera mCamera = null;
    protected boolean mIsCameraOpened = false;

    public void onViewCreated(View view) {

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

    protected void playShutterSound(){
        Log.d(TAG, "########playshuttersound + ");
        cameraShutter = MediaPlayer.create(getActivity(), R.raw.camera_shutter_1);
        cameraShutter.start();
        Log.d(TAG, "########playshuttersound - ");
    }

    protected void releaseShutter(){
        Log.d(TAG, "##### releaseShutter called");
        if(cameraShutter != null){
            cameraShutter.stop();
            cameraShutter.release();
            cameraShutter = null;
            Log.d(TAG, "##### Shutter released");
        }
    }

    protected void releaseCamera(){
        Log.d(TAG, "##### releaseCamera called");

        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();        // release the camera for other applications
            mCamera = null;
            mCameraSurface.getHolder().removeCallback(mCameraSurface);
            Log.d(TAG, "##### Camera released");
        }
    }

    //Animation Series
    protected void setFadeIn(View view) {
        final Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(2500);
        view.setAnimation(fadeIn);
    }

    protected void setFadeOut(View view) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(500);
        view.setAnimation(fadeOut);
    }

    protected void setBlinkAnimation (View view) {
        Animation blink = new AlphaAnimation(1, 0);
        blink.setDuration(400);
        blink.setInterpolator(new LinearInterpolator());
        blink.setRepeatCount(0);
        view.startAnimation(blink);
    }

    public void animateRightIn(View view) {
        view.setX(1440f);
        view.setScaleX(0.5f);
        view.setScaleY(0.5f);

        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "x", 0);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(300);
        ObjectAnimator animScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f);
        ObjectAnimator animScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f);
        ObjectAnimator bgAlpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);

        animSet.play(animX).with(bgAlpha).with(animScaleX).with(animScaleY);
        animSet.start();
    }

    public void animateGrow(View view) {
        view.setScaleX(0.5f);
        view.setScaleY(0.5f);

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(300);
        ObjectAnimator animScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f);
        ObjectAnimator animScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f);
        ObjectAnimator bgAlpha = ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1f);

        animSet.play(bgAlpha).with(animScaleX).with(animScaleY);
        animSet.start();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "##### onPause called");

        super.onPause();
        releaseCamera();
        releaseShutter();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume called");

        super.onResume();
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
    }

    @Override
    public void onBackPressed() {

    }
}
