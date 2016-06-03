package com.samsung.retailexperience.retailtmo.ui.fragment;

import android.view.View;

import com.samsung.retailexperience.retailtmo.analytics.FragmentChangeCause;
import com.samsung.retailexperience.retailtmo.gson.models.ArgumentsModel;
import com.samsung.retailexperience.retailtmo.util.AppConst;

/**
 * Created by icanmobile on 3/4/16.
 */
public class VideoFragment extends BaseVideoFragment {
    private static final String TAG = VideoFragment.class.getSimpleName();

    @Override
    public void onFragmentCreated(ArgumentsModel args) {

    }

    @Override
    public void onViewCreated(View view) {

    }

    @Override
    public void onPlayVideo() {
        if (isLandscape() == false) return;
        playVideo(mVideoView);
    }

    @Override
    public void onPageTransitionStart(boolean enter, int nextAnim, AppConst.TransactionDir dir) {

    }

    @Override
    public void onPageTransitionEnd(boolean enter, int nextAnim, AppConst.TransactionDir dir) {

    }

    @Override
    public void onPageTransitionCancel(boolean enter, int nextAnim, AppConst.TransactionDir dir) {

    }


    @Override
    public void onBackPressed() {
        if (getFragmentModel() == null || getFragmentModel().getActionBackKey() == null) return;
        changeFragment(getFragmentModel().getActionBackKey(),
                AppConst.TransactionDir.TRANSACTION_DIR_BACKWARD, FragmentChangeCause.BACK_PRESSED);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isLandscape() == false) {
            playVideo(mVideoView);
        }
    }
}
