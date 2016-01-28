package com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusive;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.TimerHandler;
import com.samsung.retailexperience.retailhero.view.SamsungPayView_b2c;

/**
 * Created by smheo on 1/15/2016.
 */
public class SamsungPayFragment extends BaseVideoFragment implements
        SamsungPayView_b2c.SamsungPayEventsListener, TimerHandler.OnTimeoutListener {

    private static final String TAG = SamsungPayFragment.class.getSimpleName();

    private static final int USER_INTERACTION_DURATION = 6000;

    private static final int CHAPTER_0_SWIPE_TO_SHOW_A_CARD_INTERACTION = 0;   // 14000
    private static final int CHAPTER_1_SWIPE_LEFT_AND_RIGHT_VIDEO = 1;       // 19000
    private static final int CHAPTER_2_SWIPE_LEFT_AND_RIGHT_INTERACTION = 2;  // 23500
    private static final int CHAPTER_3_TAP_THE_CARD_VIDEO_INTERACTION = 3;  // 30200
    private static final int CHAPTER_4_TAP_THE_CARD_VIDEO_END = 4;  // 32000
    private static final int CHAPTER_5_AUTHORIZING_CARD_VIDEO = 5;      // 37800

    private SamsungPayView_b2c mSamsungPayView;
    private TimerHandler mTimerHandler;
    private int mChapterIndex;

    public static SamsungPayFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        SamsungPayFragment fragment = new SamsungPayFragment();

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
        mSamsungPayView = (SamsungPayView_b2c) view.findViewById(R.id.pay_root_container);
        mSamsungPayView.setSamsungPayEventsListener(this);

        mTimerHandler = new TimerHandler();
        mTimerHandler.setOnTimeoutListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        mSamsungPayView.initSamsungPay();
        mSamsungPayView.setVisibility(View.GONE);
        mChapterIndex = -1; // -1 is default value
    }

    @Override
    public void onPause() {
        super.onPause();

        mTimerHandler.stop();
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
        jumpToChapter(CHAPTER_1_SWIPE_LEFT_AND_RIGHT_VIDEO);
    }

    @Override
    public void paymentSelected() {
//        playVideo();
//        mSamsungPayView.setVisibility(View.GONE);
        if (mChapterIndex == CHAPTER_3_TAP_THE_CARD_VIDEO_INTERACTION ||
                mChapterIndex == CHAPTER_4_TAP_THE_CARD_VIDEO_END) {
            jumpToChapter(CHAPTER_5_AUTHORIZING_CARD_VIDEO);
        }
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
            case CHAPTER_0_SWIPE_TO_SHOW_A_CARD_INTERACTION:
                // User didn't swipe to show a card.  start animation manually
                forceSwipingUpAnimation();
                break;
            case CHAPTER_2_SWIPE_LEFT_AND_RIGHT_INTERACTION:
                jumpToChapter(CHAPTER_3_TAP_THE_CARD_VIDEO_INTERACTION);
                break;
            case CHAPTER_4_TAP_THE_CARD_VIDEO_END:
                jumpToChapter(CHAPTER_5_AUTHORIZING_CARD_VIDEO);
                break;
        }
    }

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = 0)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");
        mChapterIndex = CHAPTER_0_SWIPE_TO_SHOW_A_CARD_INTERACTION;

        // show swipe to show a card
        pauseVideo();
        mSamsungPayView.setVisibility(View.VISIBLE);
        mTimerHandler.start(USER_INTERACTION_DURATION);
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");
        mChapterIndex = CHAPTER_1_SWIPE_LEFT_AND_RIGHT_VIDEO;

        // play video (tell users that they can swipe cards)
        mSamsungPayView.showCardList();
        mSamsungPayView.setVisibility(View.VISIBLE);
        mSamsungPayView.showSuper("Swipe payment card to left");
    }

    @OnChapter(chapterIndex = 2)
    public void onChaper_2() {
        Log.i(TAG, "onChaper_2");
        mChapterIndex = CHAPTER_2_SWIPE_LEFT_AND_RIGHT_INTERACTION;

        // show a card list, so customer can swipe left and right
        pauseVideo();
//        mSamsungPayView.showCardList();
        mSamsungPayView.setVisibility(View.VISIBLE);
        mTimerHandler.start(USER_INTERACTION_DURATION);
    }

    @OnChapter(chapterIndex = 3)
    public void onChaper_3() {
        Log.i(TAG, "onChaper_3");
        mChapterIndex = CHAPTER_3_TAP_THE_CARD_VIDEO_INTERACTION;
        mSamsungPayView.setVisibility(View.VISIBLE);
        mSamsungPayView.showSuper("Tap the card");
        mSamsungPayView.disableScrolling();
    }

    @OnChapter(chapterIndex = 4)
    public void onChaper_4() {
        Log.i(TAG, "onChaper_4");
        mChapterIndex = CHAPTER_4_TAP_THE_CARD_VIDEO_END;
        mSamsungPayView.setVisibility(View.VISIBLE);

        pauseVideo();
        mTimerHandler.start(USER_INTERACTION_DURATION);
    }

    @OnChapter(chapterIndex = 5)
    public void onChaper_5() {
        Log.i(TAG, "onChaper_5");
        mChapterIndex = CHAPTER_5_AUTHORIZING_CARD_VIDEO;
        mSamsungPayView.hideSuper();
        mSamsungPayView.setVisibility(View.GONE);
    }
}
