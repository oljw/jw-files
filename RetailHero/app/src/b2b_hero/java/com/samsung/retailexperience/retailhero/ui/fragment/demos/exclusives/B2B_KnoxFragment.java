package com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusives;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConsts;

/**
 * Created by smheo on 1/17/2016.
 */
public class B2B_KnoxFragment extends BaseVideoFragment {

    private static final String TAG = B2B_KnoxFragment.class.getSimpleName();

    private static final int CHAPTER_0_SHOW_SUPER_0 = 0;
    private static final int CHAPTER_1_END_SUPER_0 = 1;
    private static final int CHAPTER_2_SHOW_SUPER_1 = 2;
    private static final int CHAPTER_3_END_SUPER_1 = 3;
    private static final int CHAPTER_4_TAP_ON_KNOX_ICON_INTERACTION = 4;
    private static final int CHAPTER_5_TAP_ON_KNOX_ICON_VIDEO = 5;

    private ImageView mKnoxSuperIv;
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
        mKnoxSuperIv = (ImageView) view.findViewById(R.id.knox_super_iv);
        mKnoxIconContainer = view.findViewById(R.id.knox_icon_container);

        mKnoxIconClickableArea = view.findViewById(R.id.knox_icon_clickable);
        if (mKnoxIconClickableArea != null) {
            mKnoxIconClickableArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mChapterIndex == CHAPTER_4_TAP_ON_KNOX_ICON_INTERACTION) {
                        setForcedSeekToChapter(CHAPTER_5_TAP_ON_KNOX_ICON_VIDEO);
                    }
                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        mChapterIndex = -1; // -1 is default

        if (mKnoxSuperIv != null) {
            mKnoxSuperIv.setVisibility(View.GONE);
        }

        if (mKnoxIconContainer != null) {
            mKnoxIconContainer.setVisibility(View.GONE);
        }

        if (mVideoView != null) {
            try {
                mVideoView.play();
            } catch (Exception e) {
                Log.e(TAG, e + " : video play");
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = CHAPTER_0_SHOW_SUPER_0)
    public void onChaper_0() {
        Log.d(TAG, "Chapter 0 : show the super 0");

        mChapterIndex = CHAPTER_0_SHOW_SUPER_0;
        if (mKnoxSuperIv != null) {
            mKnoxSuperIv.setImageResource(R.drawable.knox_super_0);
            mKnoxSuperIv.setVisibility(View.VISIBLE);
        }
    }

    @OnChapter(chapterIndex = CHAPTER_1_END_SUPER_0)
    public void onChaper_1() {
        Log.d(TAG, "Chapter 1 : end the super 0");

        mChapterIndex = CHAPTER_1_END_SUPER_0;
        if (mKnoxSuperIv != null) {
            mKnoxSuperIv.setVisibility(View.GONE);
        }
    }

    @OnChapter(chapterIndex = CHAPTER_2_SHOW_SUPER_1)
    public void onChaper_2() {
        Log.d(TAG, "Chapter 2 : show the super 0");

        mChapterIndex = CHAPTER_2_SHOW_SUPER_1;
        if (mKnoxSuperIv != null) {
            mKnoxSuperIv.setImageResource(R.drawable.knox_super_1);
            mKnoxSuperIv.setVisibility(View.VISIBLE);
        }

    }

    @OnChapter(chapterIndex = CHAPTER_3_END_SUPER_1)
    public void onChaper_3() {
        Log.d(TAG, "Chapter 3 : end the super 0");

        mChapterIndex = CHAPTER_3_END_SUPER_1;
        if (mKnoxSuperIv != null) {
            mKnoxSuperIv.setVisibility(View.GONE);
        }
    }

    @OnChapter(chapterIndex = CHAPTER_4_TAP_ON_KNOX_ICON_INTERACTION)
    public void onChaper_4() {
        Log.d(TAG, "Chapter 4 : show the tap on icon");

        mChapterIndex = CHAPTER_4_TAP_ON_KNOX_ICON_INTERACTION;
        mKnoxIconContainer.setVisibility(View.VISIBLE);
    }

    @OnChapter(chapterIndex = CHAPTER_5_TAP_ON_KNOX_ICON_VIDEO)
    public void onChaper_5() {
        Log.d(TAG, "Chapter 5 : show the next video frame");

        mKnoxIconContainer.setVisibility(View.GONE);
        mChapterIndex = CHAPTER_5_TAP_ON_KNOX_ICON_VIDEO;
    }
}
