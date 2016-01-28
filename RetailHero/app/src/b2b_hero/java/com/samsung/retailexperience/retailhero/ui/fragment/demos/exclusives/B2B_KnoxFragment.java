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
public class B2B_KnoxFragment extends BaseVideoFragment implements View.OnClickListener {

    private static final String TAG = B2B_KnoxFragment.class.getSimpleName();

    // TODO confirm
    private static final int CHAPTER_0_TAP_ON_KNOX_ICON_INTERACTION = 0;   // 30 sec
    private static final int CHAPTER_1_TAP_ON_KNOX_ICON_VIDEO = 1;   // 36 sec

    private View mKnoxIconContainer;
    private View mKnoxIconClickableArea;
    private int mChapterIndex;

    public static B2B_KnoxFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        B2B_KnoxFragment fragment = new B2B_KnoxFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        mKnoxIconContainer = view.findViewById(R.id.knox_icon_container);
        mKnoxIconClickableArea = view.findViewById(R.id.knox_icon_clickable);

        mKnoxIconClickableArea.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();

        mKnoxIconContainer.setVisibility(View.GONE);
        mChapterIndex = -1; // -1 is default
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.knox_icon_clickable:
                if (mChapterIndex == CHAPTER_0_TAP_ON_KNOX_ICON_INTERACTION) {
                    setForcedSeekToChapter(CHAPTER_1_TAP_ON_KNOX_ICON_VIDEO);
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

        mKnoxIconContainer.setVisibility(View.VISIBLE);
        mChapterIndex = CHAPTER_0_TAP_ON_KNOX_ICON_INTERACTION;
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");

        mKnoxIconContainer.setVisibility(View.GONE);
        mChapterIndex = CHAPTER_1_TAP_ON_KNOX_ICON_VIDEO;
    }

}
