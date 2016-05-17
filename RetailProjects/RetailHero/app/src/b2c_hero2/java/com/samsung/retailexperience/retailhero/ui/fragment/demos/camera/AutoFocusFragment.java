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
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.BaseCameraBackFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.BottomMenuBarFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.view.camera_view.CameraSurfaceViewBack;

/**
 * Created by smheo on 1/15/2016.
 */
public class AutoFocusFragment extends BaseCameraBackFragment
        implements BottomMenuBarFragment.BottomMenuBarListener,
        CameraSurfaceViewBack.CameraSurfaceBackListener {

    private static final String TAG = AutoFocusFragment.class.getSimpleName();

    private ImageView mCaptureSuper;
    private RelativeLayout mCameraLayout;
    private ImageButton mCaptureBtn;
    private ImageView mNotSavedSuper;
    private ImageView mTapSuper;

    public static AutoFocusFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        AutoFocusFragment fragment = new AutoFocusFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        mCameraLayout = (RelativeLayout) view.findViewById(R.id.camera_layout);

        mIsCameraBack = true;

        mTapSuper = (ImageView) view.findViewById(R.id.tap_super);
        mTapSuper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setForcedSeekToChapter(3);
                mTapSuper.setClickable(false);
            }
        });

        mOutputImage = (ImageView) view.findViewById(R.id.output_image);

        mPreview = (RelativeLayout) view.findViewById(R.id.camera_preview_screen);

        mFocusIcon = (ImageView) view.findViewById(R.id.draw_focus_icon);

        mCaptureSuper = (ImageView) view.findViewById(R.id.capture_super);

        mNotSavedSuper = (ImageView) view.findViewById(R.id.not_saved_super);

        mCaptureBtn = (ImageButton) view.findViewById(R.id.capture_button);
        mCaptureBtn.setSoundEffectsEnabled(false);
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChapterIndex == 3 || mChapterIndex == 4) {
                    mCameraSurface.setStillShotParam(mIsCameraBack);

                    mCamera.takePicture(null, null, preview);
                    setForcedSeekToChapter(4);
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
        mTapSuper.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "########ONPAUSE");

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

    //Preparing the Camera Preview to prevent sudden camera preview show up
    @OnChapter(chapterIndex = 0)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");
        mChapterIndex = 0;

        if(mCamera == null && !mIsCameraOpened){
            Log.d(TAG, "#####Attempt to open Camera");
            mCamera = getCameraInstance(0);
            Log.d(TAG, "#####Camera is opened on chapter 0.");
            mCameraSurface = new CameraSurfaceViewBack(getActivity(), mCamera);
            Log.d(TAG, "#####Set camera surface");
            mIsCameraOpened = true;
            Log.d(TAG, "#####mIsCameraOpened = " + mIsCameraOpened);
        }
        mCameraSurface.setListener(this);
        mPreview.addView(mCameraSurface);
        mCameraLayout.setVisibility(View.INVISIBLE);
    }

    //Camera Preview is visible and focus icon is visible.
    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");
        mChapterIndex = 1;

        mCameraLayout.setVisibility(View.VISIBLE);
        mFocusIcon.bringToFront();
        animateGrow(mCameraLayout);
    }

    //Tap the screen super is visible
    @OnChapter(chapterIndex = 2)
    public void onChaper_2() {
        Log.i(TAG, "onChaper_2");
        mChapterIndex = 2;

        mTapSuper.setClickable(true);
        setFadeIn(mTapSuper);
        mTapSuper.setVisibility(View.VISIBLE);
        mTapSuper.bringToFront();
    }

    //Capture button super is visible and is clickable
    @OnChapter(chapterIndex = 3)
    public void onChaper_3() {
        Log.i(TAG, "onChaper_3");
        mChapterIndex = 3;

        setFadeOut(mTapSuper);
        mTapSuper.setVisibility(View.GONE);

        setFadeIn(mCaptureSuper);
        mCaptureSuper.setVisibility(View.VISIBLE);
        mCaptureBtn.setClickable(true);
    }

    //Perform and click capture button automatically.
    @OnChapter(chapterIndex = 4)
    public void onChaper_4() {
        Log.i(TAG, "onChaper_4");
        mChapterIndex = 4;

        mCaptureBtn.performClick();

        //Blinking Effect
        setBlinkAnimation(mPreview);

        //Shutter Sound
        playShutterSound();
    }

    //Camera Preview is gone and an output image is shown with "Your photo will not be saved" super.
    @OnChapter(chapterIndex = 5)
    public void onChaper_5() {
        Log.i(TAG, "onChaper_5");
        mChapterIndex = 5;

        mCameraLayout.setVisibility(View.GONE);

        mOutputImage.setVisibility(View.VISIBLE);
        mOutputImage.bringToFront();
        animateRightIn(mOutputImage);

        mNotSavedSuper.setVisibility(View.VISIBLE);
        mNotSavedSuper.bringToFront();
        setFadeIn(mNotSavedSuper);
    }
}