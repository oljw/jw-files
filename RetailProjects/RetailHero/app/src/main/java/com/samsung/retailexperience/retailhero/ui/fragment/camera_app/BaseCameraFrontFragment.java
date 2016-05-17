package com.samsung.retailexperience.retailhero.ui.fragment.camera_app;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.view.camera_view.CameraSurfaceViewFront;

import java.io.IOException;

/**
 * Created by JW on 1/23/2016.
 */
public class BaseCameraFrontFragment extends BaseVideoFragment
        implements BottomMenuBarFragment.BottomMenuBarListener,
        CameraSurfaceViewFront.CameraSurfaceFrontListener {

    private static final String TAG = BaseCameraFrontFragment.class.getSimpleName();

    public static BaseCameraFrontFragment newInstance(String fragmentModel) {

        BaseCameraFrontFragment fragment = new BaseCameraFrontFragment();

        Bundle args = new Bundle();
        args.putString(AppConsts.ARG_JSON_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    protected CameraSurfaceViewFront mCameraSurface = null;
    protected RelativeLayout mPreview = null;
    protected ImageView mOutputImage = null;
    protected Camera mCamera = null;
    protected int mChapterIndex = -1;

    //Does not save any file.
    protected String path = "/sdcard/TecAce";

    protected int mCameraId = -1;
    protected boolean mIsCameraBack;
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

    protected void releaseShutter(){
        Log.d(TAG, "##### releaseShutter called / Check if shutter released is logged");
        if(cameraShutter != null){
            cameraShutter.stop();
            cameraShutter.release();
            cameraShutter = null;
            Log.d(TAG, "##### Shutter Released");
        }
    }

    protected void releaseCamera(){
        Log.d(TAG, "##### releaseCamera called / Check if camera release is logged");

        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.release();        // release the camera for other applications
            mCamera = null;
            mCameraSurface.getHolder().removeCallback(mCameraSurface);
            Log.d(TAG, "##### Camera released");
        }
    }

    protected MediaPlayer cameraShutter;
    protected void playShutterSound(){
        Log.d(TAG, "########playshuttersound + ");
        cameraShutter = MediaPlayer.create(getActivity(), R.raw.camera_shutter_1);
        cameraShutter.start();
        Log.d(TAG, "########playshuttersound - ");
    }

    protected Bitmap realImage = null;
    protected Camera.PictureCallback preview = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;

            options.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared

            options.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future

            realImage = BitmapFactory.decodeByteArray(data,0,data.length,options);

            ExifInterface exif = null;
            try {
                exif = new ExifInterface(path);
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
            mOutputImage.setImageBitmap(realImage);
        }
    };

    public Bitmap rotate(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        if(mIsCameraBack){
            matrix.postRotate(90);
        } else {
            matrix.postRotate(270);
        }
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, false);
    }

    //Animation Set
    protected void setFadeIn(View view) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2500);
        view.setAnimation(fadeIn);
    }

    protected void setFadeOut(View view) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setDuration(1000);
        view.setAnimation(fadeOut);
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

    protected void setBlinkAnimation (View view) {
        Animation blink = new AlphaAnimation(1, 0);
        blink.setDuration(400);
        blink.setInterpolator(new LinearInterpolator());
        blink.setRepeatCount(0);
        view.startAnimation(blink);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "##### onPause called");

        super.onPause();
        releaseCamera();
        releaseShutter();

        if (mOutputImage != null && mOutputImage.getDrawable() != null) {
            BitmapDrawable b = (BitmapDrawable) mOutputImage.getDrawable();
            if (b != null && b.getBitmap() != null) {
                b.getBitmap().recycle();
                Log.d(TAG, "#####Photo bitmap recycled");
            }
        }
        mOutputImage.setImageBitmap(null);
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