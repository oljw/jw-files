package com.samsung.retailexperience.retailhero.ui.fragment.demos.camera;

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
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.BaseCameraFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.BottomMenuBarFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.view.GalleryZoomView;
import com.samsung.retailexperience.retailhero.view.camera_view.CameraSurfaceView;

/**
 * Created by smheo on 1/15/2016.
 */
public class PhotoQualityFragment extends BaseCameraFragment {

    private static final String TAG = PhotoQualityFragment.class.getSimpleName();

    private GalleryZoomView mGalleryPreview;
    private RelativeLayout mCameraLayout;
    private RelativeLayout mGalleryLayout;
    private ImageButton mCaptureBtn;
    private ImageView mCaptureSuper;
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

        mGalleryPreview = (GalleryZoomView) view.findViewById(R.id.gallery_view_output);

        mPreview = (RelativeLayout) view.findViewById(R.id.camera_preview_screen);

        mCaptureSuper = (ImageView) view.findViewById(R.id.capture_super);

        mCaptureBtn = (ImageButton) view.findViewById(R.id.capture_button);
        mCaptureBtn.setSoundEffectsEnabled(false);
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChapterIndex == 0) {
                    setForcedSeekToChapter(1);
                    mCaptureBtn.setClickable(false);
                }
            }
        });

        mZoomOverlay = (ImageView) view.findViewById(R.id.zoom_overlay);
        mZoomOverlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mZoomOverlay.setVisibility(View.GONE);
                setFadeOut(mZoomOverlay);
                if (isClickedFirstTime) {
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

        if (mVideoView != null) {
            try {
                mVideoView.play();
            } catch (Exception e) {
                Log.e(TAG, e + " : video play");
            }
        }

        mCameraLayout.setVisibility(View.GONE);
        mGalleryLayout.setVisibility(View.GONE);
        mGalleryPreview.resetZoom();

        mZoomOverlay.setEnabled(true);
        mZoomOverlay.setVisibility(View.GONE);
        isClickedFirstTime = true;
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

    //Camera preview is visible. Then also capture button is clickable.
    @OnChapter(chapterIndex = 0)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");
        mChapterIndex = 0;

        if(mCamera == null && !mIsCameraOpened){

            Log.d(TAG, "#####Attempt to open Camera");
            mCamera = getCameraInstance(-1);
            Log.d(TAG, "#####Camera is opened on chapter 0.");
            mCameraSurface = new CameraSurfaceView(getActivity(), mCamera);
            Log.d(TAG, "#####Set camera surface");
            mIsCameraOpened = true;
            Log.d(TAG, "#####mIsCameraOpened = " + mIsCameraOpened);
        }

        mPreview.addView(mCameraSurface);
        animateGrow(mCameraLayout);
        mCameraLayout.setVisibility(View.VISIBLE);

        mCaptureBtn.setClickable(true);
        mCaptureBtn.bringToFront();

        mCaptureSuper.setVisibility(View.VISIBLE);
        setFadeIn(mCaptureSuper);
    }

    //Take photo animation with sound
    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");
        mChapterIndex = 1;

        //Blinking Effect
        setBlinkAnimation(mPreview);

        //Shutter Sound
        playShutterSound();
    }

    //Zoom the photo screen
    @OnChapter(chapterIndex = 2)
    public void onChaper_2() {
        Log.i(TAG, "onChaper_2");
        mChapterIndex = 2;

        mZoomOverlay.setVisibility(View.VISIBLE);
        mZoomOverlay.bringToFront();
        setFadeIn(mZoomOverlay);
        mCameraLayout.setVisibility(View.GONE);
        mGalleryLayout.setVisibility(View.VISIBLE);
        animateRightIn(mGalleryLayout);
    }

    //Zoom Overlay is gone if it hasn't done so yet.
    @OnChapter(chapterIndex = 3)
    public void onChaper_3() {
        Log.i(TAG, "onChaper_3");
        mChapterIndex = 3;

        mZoomOverlay.setVisibility(View.GONE);
    }
}