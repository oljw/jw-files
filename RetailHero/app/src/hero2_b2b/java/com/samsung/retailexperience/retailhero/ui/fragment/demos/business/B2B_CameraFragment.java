package com.samsung.retailexperience.retailhero.ui.fragment.demos.business;

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
import com.samsung.retailexperience.retailhero.ui.activity.MainActivity;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseCameraFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.BottomMenuBarFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.view.CameraSurfaceView;

/**
 * Created by smheo on 1/17/2016.
 */
public class B2B_CameraFragment extends BaseCameraFragment
        implements BottomMenuBarFragment.BottomMenuBarListener,
        CameraSurfaceView.CameraSurfaceListener {

    private static final String TAG = B2B_CameraFragment.class.getSimpleName();

    private ImageView mCaptureSuper;
    private RelativeLayout mCameraLayout;
    private ImageButton mCaptureBtn;
    private ImageView mTapIcon;

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

        mPreview = (RelativeLayout) view.findViewById(R.id.camera_view_test);

        mCaptureSuper = (ImageView) view.findViewById(R.id.capture_super);

        mCamera = getCameraInstance(-1);
        mCameraSurface = new CameraSurfaceView((MainActivity) getActivity(), mCamera);
        mCameraSurface.setListener(this);

        mTapIcon = (ImageView) view.findViewById(R.id.tap_camera_icon);
        mTapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setForcedSeekToChapter(1);
            }
        });

        mCaptureBtn = (ImageButton) view.findViewById(R.id.capture_button);
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setForcedSeekToChapter(2);
                //Blinking Effect
                setBlinkAnimation(mPreview);

                //Shutter Sound
                final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.camera_shutter_1);
                mp.start();
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

        mTapIcon.setVisibility(View.VISIBLE);
        setFadeIn(mTapIcon);
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");

        mTapIcon.setVisibility(View.GONE);
        mCameraLayout.setVisibility(View.VISIBLE);
        mPreview.addView(mCameraSurface);

        setFadeIn(mCaptureSuper);
        mCaptureSuper.setVisibility(View.VISIBLE);
        mCaptureBtn.setClickable(true);
    }

    @OnChapter(chapterIndex = 2)
    public void onChaper_2() {
        Log.i(TAG, "onChaper_2");

        releaseCamera();
        mCameraLayout.setVisibility(View.GONE);
    }

    @OnChapter(chapterIndex = 3)
    public void onChaper_3() {
        Log.i(TAG, "onChaper_3");

    }
}