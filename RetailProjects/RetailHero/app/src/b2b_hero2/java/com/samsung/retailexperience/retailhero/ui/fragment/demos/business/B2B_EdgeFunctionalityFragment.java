package com.samsung.retailexperience.retailhero.ui.fragment.demos.business;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.TimerHandler;

/**
 * Created by jaekim on 1/24/16.
 */
public class B2B_EdgeFunctionalityFragment extends BaseVideoFragment implements
        TimerHandler.OnTimeoutListener, View.OnTouchListener, View.OnClickListener {

    private static final String TAG = B2B_EdgeFunctionalityFragment.class.getSimpleName();

    private static final int CHAPTER_0_SWIPE_LEFT_TO_OPEN_DRAWER_INTERACTION = 0;  // 27
    private static final int CHAPTER_1_SWIPING_DRAWER_VIDEO = 1;  // 32.5
    private static final int CHAPTER_2_TAP_TO_OPEN_EMAIL_INTERACTION = 2;  // 36
    private static final int CHAPTER_3_TAP_TO_MACRO_VIDEO = 3;  // 41.5
    private static final int CHAPTER_4_TAP_TO_CALENDAR_INTERACTION = 4;  // 76
    private static final int CHAPTER_5_ADDING_EVENT_VIDEO = 5;  // 81.5
    private int mChapterIndex = -1;  // no chapter index info when starting a video

    private static final float SWIPING_SQUARED_DISTANCE_MIN = 1000f;
    private float mTouchStartX, mTouchStartY;

    private static final int USER_INTERACTION_DURATION = 6000;
    private TimerHandler mTimerHandler;

    private View mSwipeShortcut;
    private View mTapEmail;
    private View mTapCalendar;

    public static B2B_EdgeFunctionalityFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        B2B_EdgeFunctionalityFragment fragment = new B2B_EdgeFunctionalityFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        if (view != null) {
            mSwipeShortcut = view.findViewById(R.id.edge_swipe_shortcut);
            mTapEmail = view.findViewById(R.id.edge_tap_email);
            mTapCalendar = view.findViewById(R.id.edge_tap_calendar);

            if (mSwipeShortcut != null) {
                mSwipeShortcut.setOnTouchListener(this);
            }

            View tempView = view.findViewById(R.id.edge_tap_email_clickable_area);
            if (tempView != null) {
                tempView.setOnClickListener(this);
            }
            tempView = view.findViewById(R.id.edge_tap_calendar_clickable_area);
            if (tempView != null) {
                tempView.setOnClickListener(this);
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

        playVideo();

        mSwipeShortcut.setVisibility(View.GONE);
        mTapEmail.setVisibility(View.GONE);
        mTapCalendar.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mTimerHandler != null) {
            mTimerHandler.stop();
        }
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
                Log.e(TAG, "Failed to play a video. " +  e);
            }
        }
    }

    private void jumpToChapter(int chapter) {
        setForcedSeekToChapter(chapter);
        playVideo();
    }


    // move from interaction video.
    private void moveToNextVideo() {
        switch (mChapterIndex) {
            case CHAPTER_0_SWIPE_LEFT_TO_OPEN_DRAWER_INTERACTION:
                //jumpToChapter(CHAPTER_1_TAP_TO_OPEN_EMAIL_VIDEO);
                //mEdgeRoot.forceSwipingAnimation();
                jumpToChapter(CHAPTER_1_SWIPING_DRAWER_VIDEO);
                break;
            case CHAPTER_2_TAP_TO_OPEN_EMAIL_INTERACTION :
                jumpToChapter(CHAPTER_3_TAP_TO_MACRO_VIDEO);
                break;
            case CHAPTER_4_TAP_TO_CALENDAR_INTERACTION:
                jumpToChapter(CHAPTER_5_ADDING_EVENT_VIDEO);
                break;
        }
    }

    private boolean isSwiping(float x, float y) {
        float offsetX = mTouchStartX - x;
        float offsetY = mTouchStartY - y;

        return offsetX * offsetX + offsetY * offsetY > SWIPING_SQUARED_DISTANCE_MIN;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mTouchStartX = event.getX();
            mTouchStartY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE && isSwiping(event.getX(), event.getY())) {
            switch (v.getId()) {
                case R.id.edge_swipe_shortcut:
                    if (mChapterIndex == CHAPTER_0_SWIPE_LEFT_TO_OPEN_DRAWER_INTERACTION) {
                        jumpToChapter(CHAPTER_1_SWIPING_DRAWER_VIDEO);
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    public void onTimeout() {
        moveToNextVideo();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edge_tap_email_clickable_area:
                if (mChapterIndex == CHAPTER_2_TAP_TO_OPEN_EMAIL_INTERACTION) {
                    jumpToChapter(CHAPTER_3_TAP_TO_MACRO_VIDEO);
                }
                break;
            case R.id.edge_tap_calendar_clickable_area:
                if (mChapterIndex == CHAPTER_4_TAP_TO_CALENDAR_INTERACTION) {
                    jumpToChapter(CHAPTER_5_ADDING_EVENT_VIDEO);
                }
                break;
        }
    }

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = CHAPTER_0_SWIPE_LEFT_TO_OPEN_DRAWER_INTERACTION)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");

        // show swipe left to show drawer
        pauseVideo();
        mSwipeShortcut.setVisibility(View.VISIBLE);

        if (mTimerHandler != null) {
            mTimerHandler.start(USER_INTERACTION_DURATION);
        }
        mChapterIndex = CHAPTER_0_SWIPE_LEFT_TO_OPEN_DRAWER_INTERACTION;
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");

        mSwipeShortcut.setVisibility(View.GONE);

        mChapterIndex = CHAPTER_1_SWIPING_DRAWER_VIDEO;
    }

    @OnChapter(chapterIndex = 2)
    public void onChaper_2() {
        Log.i(TAG, "onChaper_2");

        // show swipe left to show drawer
        pauseVideo();

        mTapEmail.setVisibility(View.VISIBLE);

        if (mTimerHandler != null) {
            mTimerHandler.start(USER_INTERACTION_DURATION);
        }
        mChapterIndex = CHAPTER_2_TAP_TO_OPEN_EMAIL_INTERACTION;
    }


    @OnChapter(chapterIndex = 3)
    public void onChaper_3() {
        Log.i(TAG, "onChaper_3");

        mTapEmail.setVisibility(View.GONE);

        mChapterIndex = CHAPTER_3_TAP_TO_MACRO_VIDEO;
    }


    @OnChapter(chapterIndex = 4)
    public void onChaper_4() {
        Log.i(TAG, "onChaper_4");

        // show swipe left to show drawer
        pauseVideo();

        mTapCalendar.setVisibility(View.VISIBLE);

        if (mTimerHandler != null) {
            mTimerHandler.start(USER_INTERACTION_DURATION);
        }

        mChapterIndex = CHAPTER_4_TAP_TO_CALENDAR_INTERACTION;
    }


    @OnChapter(chapterIndex = 5)
    public void onChaper_5() {
        Log.i(TAG, "onChaper_5");

        mTapCalendar.setVisibility(View.GONE);

        mChapterIndex = CHAPTER_5_ADDING_EVENT_VIDEO;
    }
}