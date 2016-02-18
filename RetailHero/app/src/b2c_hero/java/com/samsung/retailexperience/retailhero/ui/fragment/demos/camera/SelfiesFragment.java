package com.samsung.retailexperience.retailhero.ui.fragment.demos.camera;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.BaseCameraFrontFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.BottomMenuBarFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.view.camera_view.CameraSurfaceViewFront;

/**
 * Created by smheo on 1/15/2016.
 */
public class SelfiesFragment extends BaseCameraFrontFragment {

    private static final String TAG = SelfiesFragment.class.getSimpleName();

    private ImageView mCaptureSuper;
    private RelativeLayout mCameraLayout;
    private ImageButton mCaptureBtn;
    private ImageView mNotSavedSuper;

    public static SelfiesFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        SelfiesFragment fragment = new SelfiesFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {

        mCameraLayout = (RelativeLayout) view.findViewById(R.id.camera_layout);

        mOutputImage =(ImageView) view.findViewById(R.id.output_image);

        mPreview = (RelativeLayout) view.findViewById(R.id.camera_preview_screen);

        mCaptureSuper = (ImageView) view.findViewById(R.id.capture_super);

        mIsCameraBack = false;

        mNotSavedSuper = (ImageView) view.findViewById(R.id.not_saved_super);

        mCaptureBtn = (ImageButton) view.findViewById(R.id.capture_button);
        mCaptureBtn.setSoundEffectsEnabled(false);
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChapterIndex == 2 || mChapterIndex == 3) {
                    mCameraSurface.setStillShotParam(mIsCameraBack);
                    mCamera.takePicture(null, null, preview);
                    setForcedSeekToChapter(3);
                    mCaptureBtn.setClickable(false);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mVideoView != null) {
            try {
                mVideoView.play();
            } catch (Exception e) {
                Log.e(TAG, e + " : video play");
            }
        }

        mCameraLayout.setVisibility(View.GONE);
        mOutputImage.setVisibility(View.GONE);
        mNotSavedSuper.setVisibility(View.GONE);
        mCaptureSuper.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsCameraOpened = false;
        mPreview.removeView(mCameraSurface);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentModel() != null && getFragmentModel().getActionBackKey() != null) {
            changeFragment(AppConst.UIState.valueOf(getFragmentModel().getActionBackKey()),
                    AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
        }
    }

    /**
     * Chapter callback methods
     */

    //Prepare camera preview for sudden preview show up
    @OnChapter(chapterIndex = 0)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");
        mChapterIndex = 0;

        if(mCamera == null && !mIsCameraOpened){

            Log.d(TAG, "#####Attempt to open Camera");
            mCamera = getCameraInstance(1);
            Log.d(TAG, "#####Camera is opened on chapter 0.");
            mCameraSurface = new CameraSurfaceViewFront(getActivity(), mCamera);
            Log.d(TAG, "#####Set camera surface");
            mIsCameraOpened = true;
            Log.d(TAG, "#####mIsCameraOpened = " + mIsCameraOpened);
        }

        mPreview.addView(mCameraSurface);
        mCameraLayout.setVisibility(View.INVISIBLE);
    }

    //Camera preview is shown
    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");
        mChapterIndex = 1;

        mCameraLayout.setVisibility(View.VISIBLE);
        animateGrow(mCameraLayout);
    }

    //Capture super and capture button is clickable
    @OnChapter(chapterIndex = 2)
    public void onChaper_2 () {
        Log.i(TAG, "onChaper_2");
        mChapterIndex = 2;

        setFadeIn(mCaptureSuper);
        mCaptureSuper.setVisibility(View.VISIBLE);
        mCaptureBtn.setClickable(true);
    }

    //Perform click and also sound and blink.
    @OnChapter(chapterIndex = 3)
    public void onChaper_3() {
        Log.i(TAG, "onChaper_3");
        mChapterIndex = 3;

        mCaptureBtn.performClick();

        //Blinking Effect
        setBlinkAnimation(mPreview);

        //Shutter Sound
        playShutterSound();
    }

    //Output image is shown.
    @OnChapter(chapterIndex = 4)
    public void onChaper_4() {
        Log.i(TAG, "onChaper_4");
        mChapterIndex = 4;

        mCameraLayout.setVisibility(View.GONE);

        mOutputImage.setVisibility(View.VISIBLE);
        mOutputImage.bringToFront();
        animateRightIn(mOutputImage);

        mNotSavedSuper.setVisibility(View.VISIBLE);
        setFadeIn(mNotSavedSuper);
        mNotSavedSuper.bringToFront();
    }
}