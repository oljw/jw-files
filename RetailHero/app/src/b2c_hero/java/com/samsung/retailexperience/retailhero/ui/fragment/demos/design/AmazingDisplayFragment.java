package com.samsung.retailexperience.retailhero.ui.fragment.demos.design;


import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Toast;

import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;

/**
 * A simple {@link Fragment} subclass.
 */
public class AmazingDisplayFragment extends BaseVideoFragment {

    private static final String TAG = AmazingDisplayFragment.class.getSimpleName();

    private OrientationEventListener mOrientationListener;
    private int mScreenOrientation = 0;

    public static AmazingDisplayFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        AmazingDisplayFragment fragment = new AmazingDisplayFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = 0)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");

        mOrientationListener = new OrientationEventListener(getActivity(), SensorManager.SENSOR_DELAY_UI) {
            public void onOrientationChanged (int orientation) {
                Log.d (TAG, "onOrientationChanged : " + orientation);
                if (orientation > 70 && orientation < 120)
                    setForcedSeekToChapter(1);
            }
        };
        mOrientationListener.enable();
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");
        mOrientationListener.disable();
    }
}
