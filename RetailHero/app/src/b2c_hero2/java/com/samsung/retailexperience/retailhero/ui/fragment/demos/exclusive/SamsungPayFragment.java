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
import com.samsung.retailexperience.retailhero.view.SamsungPayView;

/**
 * Created by smheo on 1/15/2016.
 */
public class SamsungPayFragment extends BaseVideoFragment implements
        SamsungPayView.SamsungPayEventsListener, TimerHandler.OnTimeoutListener {

    private static final String TAG = SamsungPayFragment.class.getSimpleName();

    private static final int USER_INTERACTION_DURATION = 6000;

    private static final int CHAPTER_0_SWIPE_TO_SHOW_A_CARD_INTERACTION = 0;   // 13060
    private static final int CHAPTER_1_SWIPE_TO_SHOW_A_CARD_VIDEO = 1;   // 19070
    private static final int CHAPTER_2_MOVING_UPWARD_CARD_VIDEO = 2;   // 19070
    private static final int CHAPTER_3_SWIPE_LEFT_RIGHT_CARDS_INTERACTION = 3;   // 2505
    private static final int CHAPTER_4_TAP_THE_CARD_VIDEO_AUDIO = 4;  // 32120
    private static final int CHAPTER_5_AUTHORIZING_CARD_VIDEO = 5;      // 45100

    private SamsungPayView mSamsungPayView;
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
        if (view != null) {
            mSamsungPayView = (SamsungPayView) view.findViewById(R.id.pay_root_container);
            if (mSamsungPayView != null) {
                mSamsungPayView.setSamsungPayEventsListener(this);
            }
        }

        mTimerHandler = new TimerHandler();
        if (mTimerHandler != null) {
            mTimerHandler.setOnTimeoutListener(this);
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

        if (mSamsungPayView != null) {
            mSamsungPayView.initSamsungPay();
            mSamsungPayView.setVisibility(View.GONE);
        }
        mChapterIndex = -1; // -1 is default value
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mTimerHandler != null) {
            mTimerHandler.stop();
        }

        if (mSamsungPayView != null) {
            mSamsungPayView.relaseTimers();
        }
    }

    @Override
    public void swipeMotionDetected() {
    }

    @Override
    public void swipeUpTouchStarted() {
        if (mChapterIndex == CHAPTER_0_SWIPE_TO_SHOW_A_CARD_INTERACTION) {
            jumpToChapter(CHAPTER_2_MOVING_UPWARD_CARD_VIDEO);
        }
    }

    @Override
    public void swipeUpAnimationStarted() {
        if (mTimerHandler != null) {
            mTimerHandler.stop();
        }
    }

    @Override
    public void swipUpAnimationEnded() {
//        if (mChapterIndex == CHAPTER_0_SWIPE_TO_SHOW_A_CARD_INTERACTION) {
//            jumpToChapter(CHAPTER_2_SWIPE_LEFT_RIGHT_VIDEO_SOUND);
//        }
    }

    @Override
    public void paymentSelected() {
//        playVideo();
//        mSamsungPayView.setVisibility(View.GONE);
//        if (mChapterIndex == CHAPTER_4_TAP_THE_CARD_VIDEO_AUDIO) {
//            jumpToChapter(CHAPTER_5_AUTHORIZING_CARD_VIDEO);
//        }
        if (mChapterIndex == CHAPTER_4_TAP_THE_CARD_VIDEO_AUDIO || mChapterIndex == CHAPTER_3_SWIPE_LEFT_RIGHT_CARDS_INTERACTION) {
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
                jumpToChapter(CHAPTER_1_SWIPE_TO_SHOW_A_CARD_VIDEO);
                break;
            case CHAPTER_3_SWIPE_LEFT_RIGHT_CARDS_INTERACTION:
                jumpToChapter(CHAPTER_4_TAP_THE_CARD_VIDEO_AUDIO);
                break;
            case CHAPTER_4_TAP_THE_CARD_VIDEO_AUDIO:
                jumpToChapter(CHAPTER_5_AUTHORIZING_CARD_VIDEO);
                break;
        }
    }

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = CHAPTER_0_SWIPE_TO_SHOW_A_CARD_INTERACTION)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");
        mChapterIndex = CHAPTER_0_SWIPE_TO_SHOW_A_CARD_INTERACTION;

        // show swipe to show a card
        pauseVideo();
        if (mSamsungPayView != null) {
            mSamsungPayView.setVisibility(View.VISIBLE);
            mSamsungPayView.showKnob();
        }
        if (mTimerHandler != null) {
            mTimerHandler.start(USER_INTERACTION_DURATION);
        }
    }

    @OnChapter(chapterIndex = CHAPTER_1_SWIPE_TO_SHOW_A_CARD_VIDEO)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");
        mChapterIndex = CHAPTER_1_SWIPE_TO_SHOW_A_CARD_VIDEO;

        // play video (tell users that they can swipe cards)
        if (mSamsungPayView != null) {
            mSamsungPayView.setVisibility(View.GONE);
        }
    }

//    @OnChapter(chapterIndex = CHAPTER_2_SWIPE_LEFT_RIGHT_VIDEO_SOUND)
//    public void onChaper_2() {
//        Log.i(TAG, "onChaper_2");
//        Log.e(TAG, "2 " + mVideoView.getPlayer().getCurrentPosition());
//        mChapterIndex = CHAPTER_2_SWIPE_LEFT_RIGHT_VIDEO_SOUND;
//
//        // show a card list, so customer can swipe left and right
//        mSamsungPayView.showCardList();
//        mSamsungPayView.setVisibility(View.VISIBLE);
//        mSamsungPayView.showSwipeLeftSuper();
////        mTimerHandler.start(USER_INTERACTION_DURATION);
//    }

    @OnChapter(chapterIndex = CHAPTER_2_MOVING_UPWARD_CARD_VIDEO)
    public void onChaper_2() {
        Log.i(TAG, "onChaper_2");

        mChapterIndex = CHAPTER_2_MOVING_UPWARD_CARD_VIDEO;

        if (mSamsungPayView != null) {
            mSamsungPayView.setVisibility(View.GONE);
        }
    }


    @OnChapter(chapterIndex = CHAPTER_3_SWIPE_LEFT_RIGHT_CARDS_INTERACTION)
    public void onChaper_3() {
        Log.i(TAG, "onChaper_3");

        pauseVideo();

        mChapterIndex = CHAPTER_3_SWIPE_LEFT_RIGHT_CARDS_INTERACTION;

        if (mSamsungPayView != null) {
            mSamsungPayView.showCardList();
            mSamsungPayView.setVisibility(View.VISIBLE);
            mSamsungPayView.showSwipeLeftSuper();
        }

        if (mTimerHandler != null) {
            mTimerHandler.start(USER_INTERACTION_DURATION);
        }
    }

    @OnChapter(chapterIndex = CHAPTER_4_TAP_THE_CARD_VIDEO_AUDIO)
    public void onChaper_4() {
        Log.i(TAG, "onChaper_3");
        mChapterIndex = CHAPTER_4_TAP_THE_CARD_VIDEO_AUDIO;

        if (mSamsungPayView != null) {
            mSamsungPayView.showTapACardSuper();
            mSamsungPayView.disableScrolling();
        }

        if (mTimerHandler != null) {
            mTimerHandler.start(USER_INTERACTION_DURATION);
        }
    }

    @OnChapter(chapterIndex = CHAPTER_5_AUTHORIZING_CARD_VIDEO)
    public void onChaper_5() {
        Log.i(TAG, "onChaper_5");
        mChapterIndex = CHAPTER_5_AUTHORIZING_CARD_VIDEO;

        if (mSamsungPayView != null) {
            mSamsungPayView.setVisibility(View.GONE);
        }
    }
}
