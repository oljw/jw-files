package com.samsung.retailexperience.retailhero.ui.fragment.demos.design;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConsts;

/**
 * A simple {@link Fragment} subclass.
 */
public class IP68Fragment extends BaseVideoFragment {

    private static final String TAG = IP68Fragment.class.getSimpleName();

    private ImageView mI68DisclaimerIv;

    public static IP68Fragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        IP68Fragment fragment = new IP68Fragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        mI68DisclaimerIv = (ImageView) view.findViewById(R.id.ip68_disclaimer_iv);
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

        if (mI68DisclaimerIv != null) {
            mI68DisclaimerIv.setVisibility(View.VISIBLE);
        }
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
        Log.i(TAG, "onChaper : hide chapter");

        if (mI68DisclaimerIv != null) {
            mI68DisclaimerIv.setVisibility(View.GONE);
        }
    }

//    @OnChapter(chapterIndex = 0)
//    public void onChaper_0() {
//        Log.i(TAG, "onChaper_0");
//
//        if (mI68DisclaimerIv != null) {
//            mI68DisclaimerIv.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @OnChapter(chapterIndex = 1)
//    public void onChaper_1() {
//        Log.i(TAG, "onChaper_1");
//
//        if (mI68DisclaimerIv != null) {
//            mI68DisclaimerIv.setVisibility(View.GONE);
//        }
//    }

}
