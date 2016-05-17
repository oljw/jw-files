package com.samsung.retailexperience.retailhero.ui.fragment.demos.design;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.TimerHandler;

/**
 * Created by smheo on 1/15/2016.
 */
public class EdgeShortcutFragment extends BaseVideoFragment implements
        TimerHandler.OnTimeoutListener, View.OnTouchListener, View.OnClickListener{

    private static final String TAG = EdgeShortcutFragment.class.getSimpleName();

    private static final int DEFAULT_CHAPTER = -1;
    private static final int CHAPTER_0_SWIPE_LEFT_TO_OPEN_SHORTCUT = 0;  // 23    (super: Swipe to open shortcut)
    private static final int CHAPTER_1_SWIPE_OPENING_SHORTCUT_VIDEO = 1;  // 29.2
    private static final int CHAPTER_2_SWIPE_BLUE_TAP_INTERACTION = 2;  // 38.3 37.2   (super: Swipe to see message)
    private static final int CHAPTER_3_SMS_MESSAGE_RIGHT_IN_VIDEO = 3;  // 43.7
    private static final int CHAPTER_4_SWIPE_MORE_FEED_INTERACTION = 4;  // 62 (Super: Swipe to see more feeds)
    private static final int CHAPTER_5_TASK_EDGE_VIDEO = 5;           // 69.2
    private static final int CHAPTER_6_TASK_EDGE_INTERACTION = 6;           // 84.5
    private static final int CHAPTER_7_TASK_EDGE_VIDEO = 7;           // 90.5
    private int mChapterIndex = DEFAULT_CHAPTER;  // no chapter index info when starting a video

    private static final int USER_INTERACTION_DURATION = 6000;
    private TimerHandler mTimerHandler;

    // edge_swipe_shortcut
    private View mSwipeShortcutSuper;
    // edge_swipe_message
    private View mSwipeMessageSuper;
    // edge_swipe_feeds
    private View mSwipeFeeds;
    // edge_tap_calendar
    private View mTapCalendar;

    private static final float SWIPING_SQUARED_DISTANCE_MIN = 1000f;
    private float mTouchStartX, mTouchStartY;

    public static EdgeShortcutFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        EdgeShortcutFragment fragment = new EdgeShortcutFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        if (view != null) {
            mSwipeShortcutSuper = view.findViewById(R.id.edge_swipe_shortcut);
            mSwipeMessageSuper = view.findViewById(R.id.edge_swipe_message);
            mSwipeFeeds = view.findViewById(R.id.edge_swipe_feeds);
            mTapCalendar = view.findViewById(R.id.edge_tap_calendar);

            if (mSwipeShortcutSuper != null) {
                mSwipeShortcutSuper.setOnTouchListener(this);
            }
            if (mSwipeMessageSuper != null) {
                mSwipeMessageSuper.setOnTouchListener(this);
            }
            if (mSwipeFeeds != null) {
                mSwipeFeeds.setOnTouchListener(this);
            }
            if (view.findViewById(R.id.edge_tap_calendar_clickable_area) != null) {
                view.findViewById(R.id.edge_tap_calendar_clickable_area).setOnClickListener(this);
            }
        }

        mTimerHandler = new TimerHandler();
        if (mTimerHandler != null) {
            mTimerHandler.setOnTimeoutListener(this);
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
            case CHAPTER_0_SWIPE_LEFT_TO_OPEN_SHORTCUT:
                jumpToChapter(CHAPTER_1_SWIPE_OPENING_SHORTCUT_VIDEO);
                break;
            case CHAPTER_2_SWIPE_BLUE_TAP_INTERACTION:
                jumpToChapter(CHAPTER_3_SMS_MESSAGE_RIGHT_IN_VIDEO);
                break;
            case CHAPTER_4_SWIPE_MORE_FEED_INTERACTION:
                jumpToChapter(CHAPTER_5_TASK_EDGE_VIDEO);
                break;
            case CHAPTER_6_TASK_EDGE_INTERACTION:
                jumpToChapter(CHAPTER_7_TASK_EDGE_VIDEO);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

//        if (mVideoView != null) {
//            try {
//                mVideoView.play();
//            } catch (Exception e) {
//                Log.e(TAG, e + " : video play");
//            }
//        }
        playVideo();

        hideView(mSwipeShortcutSuper);
        hideView(mSwipeMessageSuper);
        hideView(mSwipeFeeds);
        hideView(mTapCalendar);

        mChapterIndex = DEFAULT_CHAPTER;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mTimerHandler != null) {
            mTimerHandler.stop();
        }
    }

    private void showView(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void hideView(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    private boolean isSwiping(float x, float y) {
        float offsetX = mTouchStartX - x;
        float offsetY = mTouchStartY - y;

//        if (offsetX * offsetX + offsetY * offsetY > SWIPING_SQUARED_DISTANCE_MIN) {
//            return true;
//        }
//        return false;

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
                    hideView(mSwipeShortcutSuper);
                    if (mChapterIndex == CHAPTER_0_SWIPE_LEFT_TO_OPEN_SHORTCUT) {
                        jumpToChapter(CHAPTER_1_SWIPE_OPENING_SHORTCUT_VIDEO);
                    }
                    break;
                case R.id.edge_swipe_message:
                    hideView(mSwipeMessageSuper);
                    if (mChapterIndex == CHAPTER_2_SWIPE_BLUE_TAP_INTERACTION) {
                        jumpToChapter(CHAPTER_3_SMS_MESSAGE_RIGHT_IN_VIDEO);
                    }
                    break;
                case R.id.edge_swipe_feeds:
                    hideView(mSwipeFeeds);
                    if (mChapterIndex == CHAPTER_4_SWIPE_MORE_FEED_INTERACTION) {
                        jumpToChapter(CHAPTER_5_TASK_EDGE_VIDEO);
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.edge_tap_calendar_clickable_area:
                if (mChapterIndex == CHAPTER_6_TASK_EDGE_INTERACTION) {
                    jumpToChapter(CHAPTER_7_TASK_EDGE_VIDEO);
                }
                break;
        }
    }


    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = CHAPTER_0_SWIPE_LEFT_TO_OPEN_SHORTCUT)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");

        // show swipe left to show drawer
        pauseVideo();
        showView(mSwipeShortcutSuper);

        if (mTimerHandler != null) {
            mTimerHandler.start(USER_INTERACTION_DURATION);
        }

        mChapterIndex = CHAPTER_0_SWIPE_LEFT_TO_OPEN_SHORTCUT;
    }

    @OnChapter(chapterIndex = CHAPTER_1_SWIPE_OPENING_SHORTCUT_VIDEO)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");

        hideView(mSwipeShortcutSuper);

        mChapterIndex = CHAPTER_1_SWIPE_OPENING_SHORTCUT_VIDEO;
    }

    @OnChapter(chapterIndex = CHAPTER_2_SWIPE_BLUE_TAP_INTERACTION)
    public void onChaper_2() {
        Log.i(TAG, "onChaper_2");

        // show swipe left to show drawer
        pauseVideo();
        showView(mSwipeMessageSuper);

        if (mTimerHandler != null) {
            mTimerHandler.start(USER_INTERACTION_DURATION);
        }

        mChapterIndex = CHAPTER_2_SWIPE_BLUE_TAP_INTERACTION;
    }


    @OnChapter(chapterIndex = CHAPTER_3_SMS_MESSAGE_RIGHT_IN_VIDEO)
    public void onChaper_3() {
        Log.i(TAG, "onChaper_3");

        hideView(mSwipeMessageSuper);

        mChapterIndex = CHAPTER_3_SMS_MESSAGE_RIGHT_IN_VIDEO;
    }


    @OnChapter(chapterIndex = CHAPTER_4_SWIPE_MORE_FEED_INTERACTION)
    public void onChaper_4() {
        Log.i(TAG, "onChaper_4");

        // show swipe left to show drawer
        pauseVideo();
        showView(mSwipeFeeds);
        if (mTimerHandler != null) {
            mTimerHandler.start(USER_INTERACTION_DURATION);
        }

        mChapterIndex = CHAPTER_4_SWIPE_MORE_FEED_INTERACTION;
    }

    @OnChapter(chapterIndex = CHAPTER_5_TASK_EDGE_VIDEO)
    public void onChaper_5() {
        Log.i(TAG, "onChaper_5");

        hideView(mSwipeFeeds);

        mChapterIndex = CHAPTER_5_TASK_EDGE_VIDEO;
    }

    @OnChapter(chapterIndex = CHAPTER_6_TASK_EDGE_INTERACTION)
    public void onChaper_6() {
        Log.i(TAG, "onChaper_6");

        pauseVideo();
        showView(mTapCalendar);
        if (mTimerHandler != null) {
            mTimerHandler.start(USER_INTERACTION_DURATION);
        }


        mChapterIndex = CHAPTER_6_TASK_EDGE_INTERACTION;
    }

    @OnChapter(chapterIndex = CHAPTER_7_TASK_EDGE_VIDEO)
    public void onChaper_7() {
        Log.i(TAG, "onChaper_7");

        hideView(mTapCalendar);

        mChapterIndex = CHAPTER_7_TASK_EDGE_VIDEO;
    }

}
