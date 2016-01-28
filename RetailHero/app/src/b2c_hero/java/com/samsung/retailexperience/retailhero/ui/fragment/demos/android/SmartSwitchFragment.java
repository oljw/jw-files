package com.samsung.retailexperience.retailhero.ui.fragment.demos.android;

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
 * Created by smheo on 1/15/2016.
 */
public class SmartSwitchFragment extends BaseVideoFragment implements View.OnClickListener {

    private static final String TAG = SmartSwitchFragment.class.getSimpleName();

    // TODO confirm
    private static final int CHAPTER_0_TAP_SMART_SWITCH_INTERACTION = 0;   // 10.3 sec
    private static final int CHAPTER_1_TAP_SMART_SWITCH_INTERACTION = 1;   // 16.3 sec

    public static SmartSwitchFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        SmartSwitchFragment fragment = new SmartSwitchFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    private View mSmartSwitch;
    private View mSmartSwitchIconClickableArea;
    private int mChapterIndex = -1; // -1 is default

    @Override
    public void onViewCreated(View view) {
        mSmartSwitch = view.findViewById(R.id.gear_smart_switch_container);
        mSmartSwitchIconClickableArea = view.findViewById(R.id.gear_smart_switch_icon_clickable);

        mSmartSwitchIconClickableArea.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();

        mSmartSwitch.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gear_smart_switch_icon_clickable:
                if (mChapterIndex == CHAPTER_0_TAP_SMART_SWITCH_INTERACTION) {
                    setForcedSeekToChapter(CHAPTER_1_TAP_SMART_SWITCH_INTERACTION);
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

        mSmartSwitch.setVisibility(View.VISIBLE);
        mChapterIndex = CHAPTER_0_TAP_SMART_SWITCH_INTERACTION;
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");

        mSmartSwitch.setVisibility(View.GONE);
        mChapterIndex = CHAPTER_1_TAP_SMART_SWITCH_INTERACTION;
    }
}
