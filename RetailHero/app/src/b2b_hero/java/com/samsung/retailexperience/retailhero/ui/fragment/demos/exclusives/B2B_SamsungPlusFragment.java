package com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusives;

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
 * Created by smheo on 1/17/2016.
 */
public class B2B_SamsungPlusFragment extends BaseVideoFragment implements View.OnClickListener {

    private static final String TAG = B2B_SamsungPlusFragment.class.getSimpleName();

    // TODO confirm
    private static final int CHAPTER_0_TAP_ON_SCAN_NOW_ICON_INTERACTION = 0;   // 24.8 sec
    private static final int CHAPTER_1_TAP_ON_SCAN_NOW_ICON_VIDEO = 1;   // 30.8 sec

    private View mScanNowIconContainer;
    private View mScanNowIconClickableArea;
    private int mChapterIndex;


    public static B2B_SamsungPlusFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        B2B_SamsungPlusFragment fragment = new B2B_SamsungPlusFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        mScanNowIconContainer = view.findViewById(R.id.plus_tap_scan_now_container);
        mScanNowIconClickableArea = view.findViewById(R.id.plus_tap_scan_now_icon_clickable);

        mScanNowIconClickableArea.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        mScanNowIconContainer.setVisibility(View.GONE);
        mChapterIndex = -1; // -1 is default
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plus_tap_scan_now_icon_clickable:
                if (mChapterIndex == CHAPTER_0_TAP_ON_SCAN_NOW_ICON_INTERACTION) {
                    setForcedSeekToChapter(CHAPTER_1_TAP_ON_SCAN_NOW_ICON_VIDEO);
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

        mScanNowIconContainer.setVisibility(View.VISIBLE);
        mChapterIndex = CHAPTER_0_TAP_ON_SCAN_NOW_ICON_INTERACTION;
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");

        mScanNowIconContainer.setVisibility(View.GONE);
        mChapterIndex = CHAPTER_1_TAP_ON_SCAN_NOW_ICON_VIDEO;
    }
}
