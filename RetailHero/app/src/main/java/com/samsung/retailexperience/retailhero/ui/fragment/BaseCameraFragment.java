package com.samsung.retailexperience.retailhero.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.ui.activity.MainActivity;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.BottomMenuBarFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.TopMenuBarFragment;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.view.CameraSurfaceView;

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
    protected int mScreenOrientation = 90;
    protected MediaPlayer mediaPlayer;

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
    }

    protected void setFadeIn(View view)
    {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(2500);
        view.setAnimation(fadeIn);
    }

    protected void setFadeOut(View view) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(1000);
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

    protected MediaPlayer cameraShutter;
    protected void playShutterSound(){
        Log.d(TAG, "########playshuttersound + ");
        cameraShutter = MediaPlayer.create(getActivity(), R.raw.camera_shutter_1);
        try {
            if (cameraShutter.isPlaying()) {
                cameraShutter.stop();
                cameraShutter.release();
                Log.d(TAG, "camerashutter released");
                cameraShutter = MediaPlayer.create(getActivity(), R.raw.camera_shutter_1);
            }
            cameraShutter.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "########playshuttersound - ");

    }

    protected void releaseCamera(){
        Log.d(TAG, "##### releaseCamera called");

        if (mCamera != null){

            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
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

    @Override
    public void onPause() {
        Log.d(TAG, "##### onPause called");

        super.onPause();
        mCameraSurface.getHolder().removeCallback(mCameraSurface);
        releaseCamera();
        if (cameraShutter != null) {
            cameraShutter.release();
            cameraShutter = null;
        }
    }

    public void onResume() {
        Log.d(TAG, "onResume called");

        super.onResume();
        mCamera = getCameraInstance(-1);
        mCameraSurface = new CameraSurfaceView((MainActivity)getActivity(), mCamera);
    }

    @Override
    public void onDestroyView () {
        releaseCamera();

        super.onDestroyView();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onStillClicked() {
        Toast.makeText(getActivity(), "스틸샷찍힘", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSwitchClicked() {

    }

    @Override
    public void onGalleryClicked() {

    }
}
