package com.samsung.retailexperience.retailhero.ui.fragment.demos.productivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConsts;

/**
 * Created by jaekim on 1/24/16.
 */
public class B2B_AllDayBatteryFragment extends BaseVideoFragment {

    private static final String TAG = B2B_AllDayBatteryFragment.class.getSimpleName();

    private static final int DISCLAIMER_BOTTOM_MARGIN = 151;
    private static final int SUPER_BOTTOM_MARGIN = 399;

    private static final int CHAPTER_0_SHOW_DISCLAIMER = 0;
    private static final int CHAPTER_1_END_DISCLAIMER = 1;
    private static final int CHAPTER_2_SHOW_SUPER = 2;
    private static final int CHAPTER_3_END_SHPER = 3;
    private static final int CHAPTER_4_START_TAP_THE_ICON = 4;
    private static final int CHAPTER_5_END_TAP_THE_ICON = 5;

    private ImageView mSuperIv = null;
    private View mTapContainer = null;
    private View mTapIcon = null;
    private int mChapterState = -1;

    public static B2B_AllDayBatteryFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        B2B_AllDayBatteryFragment fragment = new B2B_AllDayBatteryFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        if (view != null) {
            mSuperIv = (ImageView) view.findViewById(R.id.super_battery);
            mTapContainer = view.findViewById(R.id.battery_tap_container);
            mTapIcon = view.findViewById(R.id.battery_tap_icon);
            if (mTapIcon != null) {
                mTapIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mChapterState == CHAPTER_4_START_TAP_THE_ICON) {
                            setForcedSeekToChapter(CHAPTER_5_END_TAP_THE_ICON);
                        }
                    }
                });
            }
        }
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

        if (mSuperIv != null) {
            mSuperIv.setVisibility(View.GONE);
        }

        if (mTapContainer != null) {
            mTapContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mSuperIv != null) {
            mSuperIv.setVisibility(View.GONE);
        }

        if (mTapContainer != null) {
            mTapContainer.setVisibility(View.GONE);
        }

        if (mTapIcon != null) {
            mTapIcon.setOnClickListener(null);
            mTapIcon = null;
        }
    }

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = CHAPTER_0_SHOW_DISCLAIMER)
    public void onChaper_0() {
        Log.i(TAG, "Chaper 0 : show super");

        mChapterState = CHAPTER_0_SHOW_DISCLAIMER;

        if (mSuperIv != null) {
            setBottomMargin(DISCLAIMER_BOTTOM_MARGIN);
            mSuperIv.setImageResource(R.drawable.battery_disclaimer);
            mSuperIv.setVisibility(View.VISIBLE);
        }
    }

    @OnChapter(chapterIndex = CHAPTER_1_END_DISCLAIMER)
    public void onChaper_1() {
        Log.i(TAG, "Chapter 1 : end super");

        mChapterState = CHAPTER_1_END_DISCLAIMER;

        if (mSuperIv != null) {
            mSuperIv.setVisibility(View.GONE);
        }
    }

    @OnChapter(chapterIndex = CHAPTER_2_SHOW_SUPER)
    public void onChaper_2() {
        Log.i(TAG, "Chaper 2 : show super");

        mChapterState = CHAPTER_2_SHOW_SUPER;

        if (mSuperIv != null) {
            setBottomMargin(SUPER_BOTTOM_MARGIN);
            mSuperIv.setImageResource(R.drawable.battery_super);
            mSuperIv.setVisibility(View.VISIBLE);
        }
    }

    @OnChapter(chapterIndex = CHAPTER_3_END_SHPER)
    public void onChaper_3() {
        Log.i(TAG, "Chapter 3 : end super");

        mChapterState = CHAPTER_3_END_SHPER;

        if (mSuperIv != null) {
            mSuperIv.setVisibility(View.GONE);
        }
    }

    @OnChapter(chapterIndex = CHAPTER_4_START_TAP_THE_ICON)
    public void onChaper_4() {
        Log.i(TAG, "Chapter 4 : show tap a icon");

        mChapterState = CHAPTER_4_START_TAP_THE_ICON;
        if (mTapContainer != null) {
            mTapContainer.setVisibility(View.VISIBLE);
        }
    }

    @OnChapter(chapterIndex = CHAPTER_5_END_TAP_THE_ICON)
    public void onChaper_5() {
        Log.i(TAG, "Chaper 5 : end tap a icon");

        mChapterState = CHAPTER_5_END_TAP_THE_ICON;

        if (mTapContainer != null) {
            mTapContainer.setVisibility(View.GONE);
        }
    }

    private void setBottomMargin(int bottomMargine) {
        if (mSuperIv != null) {
            FrameLayout.LayoutParams lparms = (FrameLayout.LayoutParams) mSuperIv.getLayoutParams();
            lparms.bottomMargin = bottomMargine;
            mSuperIv.setLayoutParams(lparms);
        }
    }

}
