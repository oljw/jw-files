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
import com.samsung.retailexperience.retailhero.util.TimerHandler;
import com.samsung.retailexperience.retailhero.view.SamsungPayView;

/**
 * Created by smheo on 1/17/2016.
 */
public class B2B_SamsungPayFragment extends BaseVideoFragment implements SamsungPayView.SamsungPayEventsListener, TimerHandler.OnTimeoutListener {

    private static final String TAG = B2B_SamsungPayFragment.class.getSimpleName();
    private static final int USER_INTERACTION_DURATION = 6000;

    private SamsungPayView mSamsungPayView;
    private TimerHandler mTimerHandler;
    private int mChapterIndex = -1;

    public static B2B_SamsungPayFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        B2B_SamsungPayFragment fragment = new B2B_SamsungPayFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    void pauseVideo() {
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    void playVideo() {
        if (mVideoView != null) {
            try {
                mVideoView.play();
            } catch (Exception e) {
                Log.e(TAG, e + " : video play");
            }
        }
    }

    @Override
    public void onViewCreated(View view) {
        mSamsungPayView = (SamsungPayView) view.findViewById(R.id.pay_root_container);
        mSamsungPayView.setSamsungPayEventsListener(this);
        mSamsungPayView.setVisibility(View.GONE);

        mTimerHandler = new TimerHandler();
        mTimerHandler.setOnTimeoutListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void swipeUpTouchStarted() {

    }

    @Override
    public void swipeUpAnimationStarted() {
        mTimerHandler.stop();
    }

    @Override
    public void swipUpAnimationEnded() {
        jumpToChapter(FRAME_1_NOW_PICK_A_CARD_VIDEO);
    }

    @Override
    public void paymentSelected() {
        playVideo();
        mSamsungPayView.setVisibility(View.GONE);
    }

    private void forceSwipingUpAnimation() {
        mSamsungPayView.forceSwipingUpAnimation();
    }

    private void jumpToChapter(int chapter) {
        setForcedSeekToChapter(chapter);
        playVideo();
    }

    @Override
    public void onTimeout() {
        switch (mChapterIndex) {
            case FRAME_0_SWIPE_TO_SHOW_A_CARD_VIEW:
                // User didn't swipe to show a card.  start animation manually
                forceSwipingUpAnimation();
                break;
            case FRAME_2_SWIPE_CARD_LEFT_RIGHT_VIEW:
                jumpToChapter(FRAME_3_AUTHORIZING_CARD_VIDEO);
                break;
        }
    }

    private static final int FRAME_0_SWIPE_TO_SHOW_A_CARD_VIEW = 0;
    private static final int FRAME_1_NOW_PICK_A_CARD_VIDEO = 1;
    private static final int FRAME_2_SWIPE_CARD_LEFT_RIGHT_VIEW = 2;
    private static final int FRAME_3_AUTHORIZING_CARD_VIDEO = 3;

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = 0)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");
        mChapterIndex = FRAME_0_SWIPE_TO_SHOW_A_CARD_VIEW;

        // show swipe to show a card
        pauseVideo();
        mSamsungPayView.setVisibility(View.VISIBLE);
        mTimerHandler.start(USER_INTERACTION_DURATION);
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");
        mChapterIndex = FRAME_1_NOW_PICK_A_CARD_VIDEO;

        // play video (tell users that they can swipe cards)
        mSamsungPayView.setVisibility(View.GONE);
    }

    @OnChapter(chapterIndex = 2)
    public void onChaper_2() {
        Log.i(TAG, "onChaper_2");
        mChapterIndex = FRAME_2_SWIPE_CARD_LEFT_RIGHT_VIEW;

        // show a card list, so customer can swipe left and right
        pauseVideo();
        mSamsungPayView.showCardList();
        mSamsungPayView.setVisibility(View.VISIBLE);
        mTimerHandler.start(USER_INTERACTION_DURATION);
    }

    @OnChapter(chapterIndex = 3)
    public void onChaper_3() {
        Log.i(TAG, "onChaper_3");
        mChapterIndex = FRAME_3_AUTHORIZING_CARD_VIDEO;
        mSamsungPayView.setVisibility(View.GONE);
    }
}