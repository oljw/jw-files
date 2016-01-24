package com.samsung.retailexperience.retailhero.ui.fragment.demos.camera;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.activity.MainActivity;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseCameraFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.BottomGalleryBarFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.BottomMenuBarFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.TopGalleryBarFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.TopMenuBarFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.view.CameraSurfaceView;
import com.samsung.retailexperience.retailhero.view.GalleryZoomView;

/**
 * Created by smheo on 1/15/2016.
 */
public class PhotoQualityFragment extends BaseCameraFragment {

    private static final String TAG = PhotoQualityFragment.class.getSimpleName();

    private TopGalleryBarFragment mTopGalleryBar = null;
    private BottomGalleryBarFragment mBottomGalleryBar = null;
    private GalleryZoomView mGalleryPreview;
    private ImageView mCaptureSuper;
    private RelativeLayout mCameraLayout;
    private RelativeLayout mGalleryLayout;
    private ImageButton mCaptureBtn;

    public static PhotoQualityFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        PhotoQualityFragment fragment = new PhotoQualityFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {

        mCameraLayout = (RelativeLayout) view.findViewById(R.id.camera_layout);
        mGalleryLayout = (RelativeLayout) view.findViewById(R.id.gallery_layout);

        mTopMenuBar = (TopMenuBarFragment) getChildFragmentManager().
                findFragmentById(R.id.top_fragment_test);
        mBottomMenuBar = (BottomMenuBarFragment) getChildFragmentManager().
                findFragmentById(R.id.bottom_fragment_test);
        mTopGalleryBar = (TopGalleryBarFragment) getChildFragmentManager().
                findFragmentById(R.id.top_gallery_fragment_test);
        mBottomGalleryBar = (BottomGalleryBarFragment) getChildFragmentManager().
                findFragmentById(R.id.bottom_gallery_fragment_test);

        mGalleryPreview = (GalleryZoomView) view.findViewById(R.id.gallery_view_test);

        mPreview = (RelativeLayout) view.findViewById(R.id.camera_view_test);

        mCaptureSuper = (ImageView) view.findViewById(R.id.capture_super);

        mCamera = getCameraInstance(-1);
        mCameraSurface = new CameraSurfaceView((MainActivity)getActivity(), mCamera);

        mCaptureBtn = (ImageButton) view.findViewById(R.id.capture_button);
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setForcedSeekToChapter(2);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
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

        setFadeIn(mCaptureSuper);

        mCameraLayout.setVisibility(View.VISIBLE);
        mPreview.addView(mCameraSurface);
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");

        mCaptureBtn.setClickable(true);
    }

    @OnChapter(chapterIndex = 2)
    public void onChaper_2() {
        Log.i(TAG, "onChaper_2");

        //Blinking Effect
        setBlinkAnimation(mPreview);

        //Shutter Sound
        final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.camera_shutter_1);
        mp.start();
    }

    @OnChapter(chapterIndex = 3)
    public void onChaper_3() {
        Log.i(TAG, "onChaper_3");

        releaseCamera();
        mCameraLayout.setVisibility(View.GONE);
        mGalleryLayout.setVisibility(View.VISIBLE);

    }
}