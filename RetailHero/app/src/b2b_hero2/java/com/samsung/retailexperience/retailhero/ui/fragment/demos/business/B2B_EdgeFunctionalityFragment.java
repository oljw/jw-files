package com.samsung.retailexperience.retailhero.ui.fragment.demos.business;

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
import com.samsung.retailexperience.retailhero.view.MockedEdge;

/**
 * Created by jaekim on 1/24/16.
 */
public class B2B_EdgeFunctionalityFragment extends BaseVideoFragment implements MockedEdge.EdgeDrawerEventsListener, TimerHandler.OnTimeoutListener {

    private static final String TAG = B2B_EdgeFunctionalityFragment.class.getSimpleName();
    // 26 sec : Swipe - Swipe to open shortcuts
    // 35 sec : Tap - Tap to open email
    // 73 sec : Tap - Tap screen to open macro
    private static final int CHAPTER_0_SWIPE_LEFT_TO_OPEN_DRAWER_INTERACTION = 0;  // 26
    private static final int CHAPTER_1_TAP_TO_OPEN_EMAIL_VIDEO = 1;  // 33
    private static final int CHAPTER_2_TAP_TO_OPEN_EMAIL_INTERACTION = 2;  // 36
    private static final int CHAPTER_3_TAP_TO_MACRO_VIDEO = 3;  // 42
    private static final int CHAPTER_4_TAP_TO_CALENDAR_INTERACTION = 4;  // 73
    private static final int CHAPTER_5_ADDING_EVENT_VIDEO = 5;
    private int mChapterIndex = -1;  // no chapter index info when starting a video

    private static final int USER_INTERACTION_DURATION = 6000;
    private TimerHandler mTimerHandler;

    private MockedEdge mEdgeRoot;

    public static B2B_EdgeFunctionalityFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        B2B_EdgeFunctionalityFragment fragment = new B2B_EdgeFunctionalityFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        mEdgeRoot = (MockedEdge) view.findViewById(R.id.edge_root_container);
        mEdgeRoot.setEdgeDrawerEventsListener(this);

        mTimerHandler = new TimerHandler();
        mTimerHandler.setOnTimeoutListener(this);
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

    private void jumpToChapter(int chapter) {
        setForcedSeekToChapter(chapter);
        playVideo();
    }

    @Override
    public void onTimeout() {
        switch (mChapterIndex) {
            case CHAPTER_0_SWIPE_LEFT_TO_OPEN_DRAWER_INTERACTION:
                //jumpToChapter(CHAPTER_1_TAP_TO_OPEN_EMAIL_VIDEO);
                mEdgeRoot.forceSwipingAnimation();
                break;
            case CHAPTER_2_TAP_TO_OPEN_EMAIL_INTERACTION :
                jumpToChapter(CHAPTER_3_TAP_TO_MACRO_VIDEO);
                break;
            case CHAPTER_4_TAP_TO_CALENDAR_INTERACTION:
                jumpToChapter(CHAPTER_5_ADDING_EVENT_VIDEO);
                break;
        }
    }

    @Override
    public void swipeLeftTouchStarted() {

    }

    @Override
    public void swipeLeftAnimationStarted() {

    }

    @Override
    public void edgeDrawerIsOpened() {
        mTimerHandler.stop();

        jumpToChapter(CHAPTER_1_TAP_TO_OPEN_EMAIL_VIDEO);
    }

    @Override
    public void emailTapped() {
        mTimerHandler.stop();

        jumpToChapter(CHAPTER_3_TAP_TO_MACRO_VIDEO);
    }


    @Override
    public void calendarTapped() {
        mTimerHandler.stop();

        jumpToChapter(CHAPTER_5_ADDING_EVENT_VIDEO);
    }

    @Override
    public void onResume() {
        super.onResume();

        mEdgeRoot.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();

        mTimerHandler.stop();
    }

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = 0)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");

        // show swipe left to show drawer
        pauseVideo();
        mEdgeRoot.setVisibility(View.VISIBLE);
        mTimerHandler.start(USER_INTERACTION_DURATION);

        mChapterIndex = CHAPTER_0_SWIPE_LEFT_TO_OPEN_DRAWER_INTERACTION;
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");

        mEdgeRoot.setVisibility(View.GONE);

        mChapterIndex = CHAPTER_1_TAP_TO_OPEN_EMAIL_VIDEO;
    }

    @OnChapter(chapterIndex = 2)
    public void onChaper_2() {
        Log.i(TAG, "onChaper_2");

        // show swipe left to show drawer
        pauseVideo();
        mEdgeRoot.setVisibility(View.VISIBLE);
        mEdgeRoot.showTapAnEmail();
        mTimerHandler.start(USER_INTERACTION_DURATION);

        mChapterIndex = CHAPTER_2_TAP_TO_OPEN_EMAIL_INTERACTION;
    }


    @OnChapter(chapterIndex = 3)
    public void onChaper_3() {
        Log.i(TAG, "onChaper_3");

        mEdgeRoot.setVisibility(View.GONE);

        mChapterIndex = CHAPTER_3_TAP_TO_MACRO_VIDEO;
    }


    @OnChapter(chapterIndex = 4)
    public void onChaper_4() {
        Log.i(TAG, "onChaper_4");

        // show swipe left to show drawer
        pauseVideo();
        mEdgeRoot.showTaskMode();
        mEdgeRoot.setVisibility(View.VISIBLE);
        mEdgeRoot.showTapCalendar();
        mTimerHandler.start(USER_INTERACTION_DURATION);

        mChapterIndex = CHAPTER_4_TAP_TO_CALENDAR_INTERACTION;
    }


    @OnChapter(chapterIndex = 5)
    public void onChaper_5() {
        Log.i(TAG, "onChaper_5");

        mEdgeRoot.setVisibility(View.GONE);

        mChapterIndex = CHAPTER_5_ADDING_EVENT_VIDEO;
    }

}