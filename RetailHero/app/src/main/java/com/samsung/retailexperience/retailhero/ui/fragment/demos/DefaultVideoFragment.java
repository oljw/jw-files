package com.samsung.retailexperience.retailhero.ui.fragment.demos;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;

/**
 * Created by smheo on 1/16/2016.
 */
public class DefaultVideoFragment extends BaseVideoFragment {

    private static final String TAG = DefaultVideoFragment.class.getSimpleName();

    public static DefaultVideoFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        DefaultVideoFragment fragment = new DefaultVideoFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
    }

    @Override
    public void onBackPressed() {
        changeFragment(AppConst.UIState.valueOf(getFragmentModel().getActionBackKey()),
                AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        super.onCompletion(mp);
        changeEndDemoFragment(AppConst.UIState.valueOf(getFragmentModel().getActionBackKey()),
                AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
    }

}
