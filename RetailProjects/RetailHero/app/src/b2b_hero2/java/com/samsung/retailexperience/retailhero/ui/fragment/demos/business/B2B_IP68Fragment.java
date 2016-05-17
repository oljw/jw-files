package com.samsung.retailexperience.retailhero.ui.fragment.demos.business;


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
public class B2B_IP68Fragment extends BaseVideoFragment {

    private static final String TAG = B2B_IP68Fragment.class.getSimpleName();

    private ImageView mIp68SuperIv;
    private ImageView mI68DisclaimerIv;

    public static B2B_IP68Fragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        B2B_IP68Fragment fragment = new B2B_IP68Fragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        mIp68SuperIv = (ImageView) view.findViewById(R.id.ip68_super_iv);
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

        if (mIp68SuperIv != null) {
            mIp68SuperIv.setVisibility(View.GONE);
        }

        if (mI68DisclaimerIv != null) {
            mI68DisclaimerIv.setVisibility(View.GONE);
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
        Log.i(TAG, "onChaper_0");

        if (mIp68SuperIv != null) {
            mIp68SuperIv.setVisibility(View.VISIBLE);
        }
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");

        if (mIp68SuperIv != null) {
            mIp68SuperIv.setVisibility(View.GONE);
        }
    }

    @OnChapter(chapterIndex = 2)
    public void onChaper_2() {
        Log.i(TAG, "onChaper_2");

        if (mI68DisclaimerIv != null) {
            mI68DisclaimerIv.setVisibility(View.VISIBLE);
        }
    }

    @OnChapter(chapterIndex = 3)
    public void onChaper_3() {
        Log.i(TAG, "onChaper_3");

        if (mI68DisclaimerIv != null) {
            mI68DisclaimerIv.setVisibility(View.GONE);
        }
    }

}
