package com.samsung.retailexperience.retailhero.ui.fragment.demos.business;

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
import com.samsung.retailexperience.retailhero.util.TimerHandler;
import com.samsung.retailexperience.retailhero.view.VideoTextureView;
import com.samsung.retailexperience.retailhero.view.camera_view.CameraSurfaceViewBack;

/**
 * Created by smheo on 1/17/2016.
 */
public class B2B_CameraFragment extends BaseCameraBackFragment
        implements BottomMenuBarFragment.BottomMenuBarListener, TimerHandler.OnTimeoutListener {

    private static final String TAG = B2B_CameraFragment.class.getSimpleName();

    private static final int USER_INTERACTION_DURATION = 6000;

    private ImageView mCaptureSuper;
    private RelativeLayout mCameraLayout;
    private ImageButton mCaptureBtn;
    private ImageView mTapIcon;
    private View mCameraClickableArea;
    private TimerHandler mTimerHandler;
    private ImageView mNotSavedSuper;

    public static B2B_CameraFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        B2B_CameraFragment fragment = new B2B_CameraFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {

        mCameraLayout = (RelativeLayout) view.findViewById(R.id.camera_layout);

        mPreview = (RelativeLayout) view.findViewById(R.id.camera_preview_screen);

        mVideoView = (VideoTextureView) view.findViewById(R.id.video_view);

        mTapIcon = (ImageView) view.findViewById(R.id.tap_camera_icon);
        mTapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTapIcon.setVisibility(View.GONE);
                jumpToChapter(1);
            }
        });

        mNotSavedSuper = (ImageView) view.findViewById(R.id.not_saved_super);

        mTimerHandler = new TimerHandler();
        mTimerHandler.setOnTimeoutListener(this);

        mOutputImage =(ImageView) view.findViewById(R.id.output_image);

        mCaptureSuper = (ImageView) view.findViewById(R.id.capture_super);

        mCameraClickableArea = (View) view.findViewById(R.id.camera_icon_clickable);
        mCameraClickableArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToChapter(1);
                mCameraClickableArea.setVisibility(View.GONE);
                mVideoView.setVisibility(View.INVISIBLE);
                mTimerHandler.stop();
            }
        });

        mIsCameraBack = true;

        mCaptureBtn = (ImageButton) view.findViewById(R.id.capture_button);
        mCaptureBtn.setSoundEffectsEnabled(false);
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChapterIndex == 1 || mChapterIndex == 2) {
                    mCameraSurface.setStillShotParam(mIsCameraBack);
                    mCamera.takePicture(null, null, preview);
                    mCaptureBtn.setClickable(false);
                    jumpToChapter(2);
                }
            }
        });
    }

    void pauseVideo() {
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    void playVideo() {
        if (mVideoView != null) {
            try {
                mVideoView.play();
            } catch (Exception e) {
                Log.e(TAG, e + " : video play");
            }
        }
    }

    @Override
    public void onTimeout() {
        switch (mChapterIndex) {
            case 0:
                jumpToChapter(mChapterIndex = 1);
                break;
            case 1:
                jumpToChapter(mChapterIndex = 2);
                break;
            case 2:
                jumpToChapter(mChapterIndex = 3);
                break;
            case 3:
                jumpToChapter(mChapterIndex = 4);
                break;
        }
    }

    private void jumpToChapter(int chapter) {
        setForcedSeekToChapter(chapter);
        playVideo();
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
        mTapIcon.setVisibility(View.GONE);
        mCameraClickableArea.setVisibility(View.GONE);
        mOutputImage.setVisibility(View.GONE);
        mNotSavedSuper.setVisibility(View.GONE);
        mVideoView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        mTimerHandler.stop();
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

    @OnChapter(chapterIndex = 0)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");
        mChapterIndex = 0;

        pauseVideo();
        mTapIcon.setVisibility(View.VISIBLE);
        mTapIcon.bringToFront();
        setFadeInFast(mTapIcon);
        mCameraClickableArea.setClickable(true);
        mCameraClickableArea.setVisibility(View.VISIBLE);
        mTimerHandler.start(USER_INTERACTION_DURATION);
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");
        mChapterIndex = 1;

        if(mCamera == null && !mIsCameraOpened){

            Log.d(TAG, "#####Attempt to open Camera");
            mCamera = getCameraInstance(0);
            Log.d(TAG, "#####Camera is opened on chapter 1.");
            mCameraSurface = new CameraSurfaceViewBack(getActivity(), mCamera);
            Log.d(TAG, "#####Set camera surface");
            mCameraSurface.setEnabled(false);
            mIsCameraOpened = true;
            Log.d(TAG, "#####mIsCameraOpened = " + mIsCameraOpened);
        }

        mTapIcon.setVisibility(View.GONE);
        pauseVideo();
        mVideoView.setVisibility(View.INVISIBLE);
        mCameraClickableArea.setVisibility(View.GONE);

        mCameraLayout.setVisibility(View.VISIBLE);
        animateGrow(mCameraLayout);
        mPreview.addView(mCameraSurface);

        setFadeIn(mCaptureSuper);
        mCaptureSuper.setVisibility(View.VISIBLE);

        mCaptureBtn.setClickable(true);
        mCaptureBtn.bringToFront();

        mTimerHandler.start(USER_INTERACTION_DURATION);
    }

    @OnChapter(chapterIndex = 2)
    public void onChaper_2() {
        Log.i(TAG, "onChaper_2");
        mChapterIndex = 2;

        pauseVideo();

        mCaptureBtn.performClick();

        //Blinking Effect
        setBlinkAnimation(mPreview);
        //Shutter Sound
        playShutterSound();

        mTimerHandler.start(1000);
    }

    @OnChapter(chapterIndex = 3)
    public void onChaper_3() {
        Log.i(TAG, "onChaper_3");
        mChapterIndex = 3;

        pauseVideo();

        mCameraLayout.setVisibility(View.GONE);

        mOutputImage.setVisibility(View.VISIBLE);
        mOutputImage.bringToFront();
        animateRightIn(mOutputImage);

        mNotSavedSuper.setVisibility(View.VISIBLE);
        mNotSavedSuper.bringToFront();
        setFadeIn(mNotSavedSuper);

        mTimerHandler.start(3000);
    }

    @OnChapter(chapterIndex = 4)
    public void onChaper_4() {
        Log.i(TAG, "onChaper_4");
        mChapterIndex = 4;

        mVideoView.setVisibility(View.VISIBLE);
        mNotSavedSuper.setVisibility(View.GONE);
        mOutputImage.setVisibility(View.GONE);
        mVideoView.setVisibility(View.VISIBLE);
        animateRightIn(mVideoView);
        mCameraLayout.setVisibility(View.GONE);
    }
}