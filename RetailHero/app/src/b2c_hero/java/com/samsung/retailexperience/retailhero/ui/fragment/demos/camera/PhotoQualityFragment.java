package com.samsung.retailexperience.retailhero.ui.fragment.demos.camera;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.activity.MainActivity;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseCameraFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.BottomMenuBarFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.view.CameraSurfaceView;
import com.samsung.retailexperience.retailhero.view.GalleryZoomView;

/**
 * Created by smheo on 1/15/2016.
 */
public class PhotoQualityFragment extends BaseCameraFragment
        implements BottomMenuBarFragment.BottomMenuBarListener,
        CameraSurfaceView.CameraSurfaceListener {

    private static final String TAG = PhotoQualityFragment.class.getSimpleName();

    private GalleryZoomView mGalleryPreview;
    private ImageView mCaptureSuper;
    private RelativeLayout mCameraLayout;
    private RelativeLayout mGalleryLayout;
    private ImageButton mCaptureBtn;
    private ImageView mZoomOverlay;
    private boolean isClickedFirstTime = true;


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

        mGalleryPreview = (GalleryZoomView) view.findViewById(R.id.gallery_view_test);

        mPreview = (RelativeLayout) view.findViewById(R.id.camera_view_test);

        mCaptureSuper = (ImageView) view.findViewById(R.id.capture_super);

        mCamera = getCameraInstance(-1);
        mCameraSurface = new CameraSurfaceView((MainActivity)getActivity(), mCamera);
        mCameraSurface.setEnabled(false);

        mCaptureBtn = (ImageButton) view.findViewById(R.id.capture_button);
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setForcedSeekToChapter(2);
            }
        });

        mZoomOverlay = (ImageView) view.findViewById(R.id.zoom_overlay);
        mZoomOverlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mZoomOverlay.setVisibility(View.GONE);
                setFadeOut(mZoomOverlay);
                if(isClickedFirstTime) {
                    mZoomOverlay.setEnabled(false);
                    isClickedFirstTime = false;
                } else {
                    mZoomOverlay.setEnabled(true);
                    isClickedFirstTime = true;
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mCameraLayout.setVisibility(View.GONE);
        mGalleryLayout.setVisibility(View.GONE);
        mZoomOverlay.setVisibility(View.GONE);
        mGalleryPreview.resetZoom();
        mCameraSurface.setEnabled(false);

        mZoomOverlay.setEnabled(true);
        isClickedFirstTime = true;
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

        mCameraLayout.setVisibility(View.VISIBLE);
        animateGrow(mCameraLayout);
        mPreview.addView(mCameraSurface);
        mCaptureSuper.setVisibility(View.GONE);
        mCaptureBtn.setClickable(true);
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_3");

        mCaptureSuper.setVisibility(View.VISIBLE);
        setFadeIn(mCaptureSuper);
    }

    @OnChapter(chapterIndex = 2)
    public void onChaper_2() {
        Log.i(TAG, "onChaper_1");

        //Blinking Effect
        setBlinkAnimation(mPreview);

        //Shutter Sound
        playShutterSound();
    }

    @OnChapter(chapterIndex = 3)
    public void onChaper_3() {
        Log.i(TAG, "onChaper_3");

        releaseCamera();
        mZoomOverlay.setVisibility(View.VISIBLE);
        mZoomOverlay.bringToFront();
        setFadeIn(mZoomOverlay);
        mCameraLayout.setVisibility(View.GONE);
        mGalleryLayout.setVisibility(View.VISIBLE);
        animateRightIn(mGalleryLayout);
    }
}