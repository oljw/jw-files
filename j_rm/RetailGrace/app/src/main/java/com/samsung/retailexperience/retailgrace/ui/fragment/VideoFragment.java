package com.samsung.retailexperience.retailgrace.ui.fragment;

import android.view.View;

import com.tecace.app.manager.analytics.FragmentChangeCause;
import com.tecace.app.manager.gson.model.ArgumentsModel;
import com.tecace.app.manager.ui.fragment.BaseVideoFragment;
import com.tecace.app.manager.util.AppManagerConst;

//import com.samsung.retailexperience.retailgrace.util.AppConst;

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
    public void onPageTransitionStart(boolean enter, int nextAnim, AppManagerConst.TransactionDir dir) {

    }

    @Override
    public void onPageTransitionEnd(boolean enter, int nextAnim, AppManagerConst.TransactionDir dir) {

    }

    @Override
    public void onPageTransitionCancel(boolean enter, int nextAnim, AppManagerConst.TransactionDir dir) {

    }


    @Override
    public void onBackPressed() {
        if (getFragmentModel() == null || getFragmentModel().getActionBackKey() == null) return;
        changeFragment(getFragmentModel().getActionBackKey(),
                AppManagerConst.TransactionDir.TRANSACTION_DIR_BACKWARD, FragmentChangeCause.BACK_PRESSED);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isLandscape() == false) {
            playVideo(mVideoView);
        }
    }
}
