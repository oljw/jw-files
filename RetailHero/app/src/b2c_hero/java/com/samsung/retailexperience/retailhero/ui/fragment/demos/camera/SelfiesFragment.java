package com.samsung.retailexperience.retailhero.ui.fragment.demos.camera;

import android.media.MediaPlayer;
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
import com.samsung.retailexperience.retailhero.ui.fragment.BaseCameraFrontFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.BottomMenuBarFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.view.CameraSurfaceViewFront;

/**
 * Created by smheo on 1/15/2016.
 */
public class SelfiesFragment extends BaseCameraFrontFragment
        implements BottomMenuBarFragment.BottomMenuBarListener,
        CameraSurfaceViewFront.CameraSurfaceFrontListener  {

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

        //Selfie Output. not Gallery Button
        mGallerybtn =(ImageView) view.findViewById(R.id.gallery_button);

        mPreview = (RelativeLayout) view.findViewById(R.id.camera_view_test);

        mCaptureSuper = (ImageView) view.findViewById(R.id.capture_super);

        mCamera = getCameraInstance(-1);
        mCameraSurface = new CameraSurfaceViewFront(getActivity(), mCamera);
        mCameraSurface.setEnabled(false);

        mNotSavedSuper = (ImageView) view.findViewById(R.id.not_saved);

        mCaptureBtn = (ImageButton) view.findViewById(R.id.capture_button);
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraSurface.setStillShotParam(mCameraBack);
                mCamera.takePicture(null, null, preview);
                setForcedSeekToChapter(3);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mCameraLayout.setVisibility(View.GONE);
        mGallerybtn.setVisibility(View.GONE);
        mNotSavedSuper.setVisibility(View.GONE);
        mCaptureSuper.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        changeFragment(AppConst.UIState.valueOf(getFragmentModel().getActionBackKey()),
                AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
    }

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = 0)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");
        mPreview.addView(mCameraSurface);
        mCameraLayout.setVisibility(View.INVISIBLE);

    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");
        mCameraLayout.setVisibility(View.VISIBLE);
        animateGrow(mCameraLayout);
        mCaptureBtn.setClickable(true);
    }

    @OnChapter(chapterIndex = 2)
    public void onChaper_2() {
        Log.i(TAG, "onChaper_2");

        setFadeIn(mCaptureSuper);
        mCaptureSuper.setVisibility(View.VISIBLE);
    }

    @OnChapter(chapterIndex = 3)
    public void onChaper_3() {
        Log.i(TAG, "onChaper_3");

        mCaptureBtn.performClick();
        mCaptureBtn.setClickable(false);

        //Blinking Effect
        setBlinkAnimation(mPreview);

        //Shutter Sound
        playShutterSound();
    }

    @OnChapter(chapterIndex = 4)
    public void onChaper_4() {
        Log.i(TAG, "onChaper_4");

        mGallerybtn.setVisibility(View.VISIBLE);
        mGallerybtn.bringToFront();
        animateRightIn(mGallerybtn);

        mNotSavedSuper.setVisibility(View.VISIBLE);
        setFadeIn(mNotSavedSuper);
        mNotSavedSuper.bringToFront();

        mCameraLayout.setVisibility(View.GONE);
        releaseCamera();
    }
}