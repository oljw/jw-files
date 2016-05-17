package com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusive;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;

/**
 * Created by smheo on 1/15/2016.
 */
public class AllDayBatteryFragment extends BaseVideoFragment {

    private static final String TAG = AllDayBatteryFragment.class.getSimpleName();

    private static final int SUPER_0_SHOW = 0;
    private static final int SUPER_0_END = 1;
    private ImageView mSuper_IV_0;

    public static AllDayBatteryFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        AllDayBatteryFragment fragment = new AllDayBatteryFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        mSuper_IV_0 = (ImageView) view.findViewById(R.id.super_battery);
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

        if (mSuper_IV_0 != null) {
            mSuper_IV_0.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = SUPER_0_SHOW)
    public void onChaper_0() {
        Log.i(TAG, "Super 0 : show");
        if (mSuper_IV_0 != null) {
            mSuper_IV_0.setVisibility(View.VISIBLE);
        }
    }

    @OnChapter(chapterIndex = SUPER_0_END)
    public void onChaper_1() {
        Log.i(TAG, "Super 0 : end");
        if (mSuper_IV_0 != null) {
            mSuper_IV_0.setVisibility(View.GONE);
        }
    }

}
