package com.samsung.retailexperience.retailtmo.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.samsung.retailexperience.retailtmo.R;
import com.samsung.retailexperience.retailtmo.analytics.FragmentChangeCause;
import com.samsung.retailexperience.retailtmo.gson.models.ArgumentsModel;
import com.samsung.retailexperience.retailtmo.util.AppConst;
import com.samsung.retailexperience.retailtmo.video.annotation.OnChapter;
import com.samsung.retailexperience.retailtmo.video.annotation.OnChapterEnded;
import com.samsung.retailexperience.retailtmo.video.gson.models.Chapter.ActionInfo;

/**
 * Created by jaekim on 3/7/16.
 */
public class SuperVideoFragment extends BaseVideoFragment {

    private static final String TAG = SuperVideoFragment.class.getSimpleName();

    private TextView mSuperView;
    private TextView mDisclaimerView;

    @Override
    public void onFragmentCreated(ArgumentsModel args) {
    }

    @Override
    public void onViewCreated(View view) {
        if (view != null) {
            mSuperView = (TextView) view.findViewById(R.id.super_view);
            mDisclaimerView = (TextView) view.findViewById(R.id.disclaimer_view);
        }
    }

    @Override
    public void onPlayVideo() {
        if (isLandscape() == false) return;
            playVideo(mVideoView);
    }

    @Override
    public void onPageTransitionStart(boolean enter, int nextAnim, AppConst.TransactionDir dir) {

    }

    @Override
    public void onPageTransitionEnd(boolean enter, int nextAnim, AppConst.TransactionDir dir) {

    }

    @Override
    public void onPageTransitionCancel(boolean enter, int nextAnim, AppConst.TransactionDir dir) {

    }

    @Override
    public void onBackPressed() {
        if (getFragmentModel() == null || getFragmentModel().getActionBackKey() == null) return;
        changeFragment(getFragmentModel().getActionBackKey(),
                AppConst.TransactionDir.TRANSACTION_DIR_BACKWARD, FragmentChangeCause.BACK_PRESSED);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mSuperView != null) {
            mSuperView.setVisibility(View.GONE);
        }
        if (mDisclaimerView != null) {
            mDisclaimerView.setVisibility(View.GONE);
        }
        if (isLandscape() == false) {
            playVideo(mVideoView);
        }
    }

    private void performAction(ActionInfo actionInfo) {
        switch (actionInfo.getAction()) {
            case Super:
                if (mSuperView != null) {
                    mSuperView.setText(actionInfo.getExtra());
                    mSuperView.setVisibility(View.VISIBLE);
                }
                break;
            case Disclaimer:
                if (mDisclaimerView != null) {
                    mDisclaimerView.setText(actionInfo.getExtra());
                    mDisclaimerView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void finishAction (ActionInfo actionInfo) {
        switch (actionInfo.getAction()) {
            case Super:
                if (mSuperView != null) {
                    mSuperView.setVisibility(View.GONE);
                }
                break;
            case Disclaimer:
                if (mDisclaimerView != null) {
                    mDisclaimerView.setVisibility(View.GONE);
                }
                break;
        }
    }

    @OnChapter(chapterIndex = 0)
    public void onChaper_0(ActionInfo action) {
        Log.i(TAG, "Chapter 0 " + action);

        performAction(action);
    }

    @OnChapterEnded(chapterIndex = 0)
    public void onChaperEnded_0(ActionInfo actionInfo) {
        Log.i(TAG, "Chapter Ended 0");

        finishAction(actionInfo);
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1(ActionInfo action) {
        Log.i(TAG, "Chaper 1");

        performAction(action);
    }

    @OnChapterEnded(chapterIndex = 1)
    public void onChaperEnded_1(ActionInfo actionInfo) {
        Log.i(TAG, "Chapter Ended 1");

        finishAction(actionInfo);
    }

    @OnChapter(chapterIndex = 2)
    public void onChaper_2(ActionInfo action) {
        Log.i(TAG, "Chaper 2");

        performAction(action);
    }

    @OnChapterEnded(chapterIndex = 2)
    public void onChaperEnded_2(ActionInfo actionInfo) {
        Log.i(TAG, "Chapter Ended 2");

        finishAction(actionInfo);
    }

    @OnChapter(chapterIndex = 3)
    public void onChaper_3(ActionInfo action) {
        Log.i(TAG, "Chaper 3");

        performAction(action);
    }

    @OnChapterEnded(chapterIndex = 3)
    public void onChaperEnded_3(ActionInfo actionInfo) {
        Log.i(TAG, "Chapter Ended 3");

        finishAction(actionInfo);
    }

    @OnChapter(chapterIndex = 4)
    public void onChaper_4(ActionInfo action) {
        Log.i(TAG, "Chaper 4");

        performAction(action);
    }

    @OnChapterEnded(chapterIndex = 4)
    public void onChaperEnded_4(ActionInfo actionInfo) {
        Log.i(TAG, "Chapter Ended 4");

        finishAction(actionInfo);
    }

    @OnChapter(chapterIndex = 5)
    public void onChaper_5(ActionInfo action) {
        Log.i(TAG, "Chaper 5");

        performAction(action);
    }

    @OnChapterEnded(chapterIndex = 5)
    public void onChaperEnded_5(ActionInfo actionInfo) {
        Log.i(TAG, "Chapter Ended 5");

        finishAction(actionInfo);
    }
}