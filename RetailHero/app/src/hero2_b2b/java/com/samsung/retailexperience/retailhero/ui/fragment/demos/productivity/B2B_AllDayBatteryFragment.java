package com.samsung.retailexperience.retailhero.ui.fragment.demos.productivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;

/**
 * Created by jaekim on 1/24/16.
 */
public class B2B_AllDayBatteryFragment extends BaseVideoFragment implements View.OnClickListener {

    private static final String TAG = B2B_AllDayBatteryFragment.class.getSimpleName();

    // TODO confirm
    private static final int CHAPTER_0_TAP_ON_ULTRA_POWER_SAVING_INTERACTION = 0;   // 44 sec
    private static final int CHAPTER_1_TAP_ON_ULTRA_POWER_SAVING_CLICK_VIDEO = 1;   // 49.5 sec
    private static final int CHAPTER_2_TAP_ON_ULTRA_POWER_SAVING_REMAIN_VIDEO = 2;  // 53 sec

    private View mUltraPowerSaving;
    private View mUltraPowerSavingIconClickableArea;
    private int mChapterIndex = -1; // -1 is default

    public static B2B_AllDayBatteryFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        B2B_AllDayBatteryFragment fragment = new B2B_AllDayBatteryFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        mUltraPowerSaving = view.findViewById(R.id.battery_ultra_power_saving_container);
        mUltraPowerSavingIconClickableArea = view.findViewById(R.id.battery_ultra_power_saving_icon_clickable);

        mUltraPowerSavingIconClickableArea.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        mUltraPowerSaving.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.battery_ultra_power_saving_icon_clickable:
                if (mChapterIndex == CHAPTER_0_TAP_ON_ULTRA_POWER_SAVING_INTERACTION) {
                    setForcedSeekToChapter(CHAPTER_2_TAP_ON_ULTRA_POWER_SAVING_REMAIN_VIDEO);
                }
                break;
        }
    }

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = 0)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");

        // TODO should stop video and start?  Try without stopping the video.

        mUltraPowerSaving.setVisibility(View.VISIBLE);
        mChapterIndex = CHAPTER_0_TAP_ON_ULTRA_POWER_SAVING_INTERACTION;
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");

        mUltraPowerSaving.setVisibility(View.GONE);
        mChapterIndex = CHAPTER_1_TAP_ON_ULTRA_POWER_SAVING_CLICK_VIDEO;
    }

    @OnChapter(chapterIndex = 2)
    public void onChaper_2() {
        Log.i(TAG, "onChaper_2");

        mUltraPowerSaving.setVisibility(View.GONE);
        mChapterIndex = CHAPTER_2_TAP_ON_ULTRA_POWER_SAVING_REMAIN_VIDEO;
    }

}
