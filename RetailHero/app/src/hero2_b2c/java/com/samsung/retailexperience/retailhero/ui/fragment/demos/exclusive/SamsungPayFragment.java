package com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusive;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.view.SamsungPayView;

/**
 * Created by smheo on 1/15/2016.
 */
public class SamsungPayFragment extends BaseVideoFragment implements SamsungPayView.SamsungPayEventsListener {

    private static final String TAG = SamsungPayFragment.class.getSimpleName();

    private SamsungPayView mSamsungPayView;

    public static SamsungPayFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        SamsungPayFragment fragment = new SamsungPayFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
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
    public void onViewCreated(View view) {
        ViewGroup viewGroup = (ViewGroup) view;
        mSamsungPayView = (SamsungPayView) view.findViewById(R.id.pay_root_container);
        mSamsungPayView.setSamsungPayEventsListener(this);
        mSamsungPayView.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();

        mSamsungPayView.setVisibility(View.GONE);
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
        pauseVideo();
        mSamsungPayView.setVisibility(View.VISIBLE);
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");
        pauseVideo();
        mSamsungPayView.setVisibility(View.VISIBLE);
        mSamsungPayView.showCardList();
    }

    @Override
    public void swipeUpTouchStarted() {

    }

    @Override
    public void swipeUpAnimationStarted() {

    }

    @Override
    public void swipUpAnimationEnded() {
        playVideo();
        mSamsungPayView.setVisibility(View.GONE);
    }

    @Override
    public void paymentSelected() {
        playVideo();
        mSamsungPayView.setVisibility(View.GONE);
    }
}
