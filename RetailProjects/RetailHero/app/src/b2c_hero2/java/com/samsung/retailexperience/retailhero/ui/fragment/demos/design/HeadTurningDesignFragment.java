package com.samsung.retailexperience.retailhero.ui.fragment.demos.design;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * A simple {@link Fragment} subclass.
 */
public class HeadTurningDesignFragment extends BaseVideoFragment {

    private static final String TAG = HeadTurningDesignFragment.class.getSimpleName();

    private static final int SUPER_BOTTOM_MARGIN = 399;
    private static final int DISCLAIMER_BOTTOM_MARGIN = 151;

    private static final int SUPER_0_SHOW = 0;
    private static final int SUPER_1_END = 1;
    private static final int SUPER_2_SHOW_DISCLAIMER = 2;
    private static final int SUPER_3_END_DISCLAIMER = 3;
    private ImageView mSuperView;

    public static HeadTurningDesignFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        HeadTurningDesignFragment fragment = new HeadTurningDesignFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        mSuperView = (ImageView) view.findViewById(R.id.super_design);
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

        // reset
        if (mSuperView != null) {
            mSuperView.setVisibility(View.GONE);
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

        if (mSuperView != null) {
            mSuperView.setImageResource(R.drawable.design_super_0);
            setBottomMargine(SUPER_BOTTOM_MARGIN);
            mSuperView.setVisibility(View.VISIBLE);
        }

    }

    @OnChapter(chapterIndex = SUPER_1_END)
    public void onChaper_1() {
        Log.i(TAG, "Super 0 : end");

        if (mSuperView != null) {
            mSuperView.setVisibility(View.GONE);
        }
    }

    @OnChapter(chapterIndex = SUPER_2_SHOW_DISCLAIMER)
    public void onChaper_2() {
        Log.i(TAG, "Super 2 : show");

        if (mSuperView != null) {
            mSuperView.setImageResource(R.drawable.design_disclaimer);
            setBottomMargine(DISCLAIMER_BOTTOM_MARGIN);
            mSuperView.setVisibility(View.VISIBLE);
        }

    }

    @OnChapter(chapterIndex = SUPER_3_END_DISCLAIMER)
    public void onChaper_3() {
        Log.i(TAG, "Super 3 : end");

        if (mSuperView != null) {
            mSuperView.setVisibility(View.GONE);
        }
    }

    private void setBottomMargine(int bottomMargine) {
        if (mSuperView != null) {
            FrameLayout.LayoutParams lparms = (FrameLayout.LayoutParams) mSuperView.getLayoutParams();
            lparms.bottomMargin = bottomMargine;
            mSuperView.setLayoutParams(lparms);
        }
    }
}
