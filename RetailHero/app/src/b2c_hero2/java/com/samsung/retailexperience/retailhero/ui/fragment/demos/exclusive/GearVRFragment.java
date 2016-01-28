package com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusive;

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

/**
 * Created by smheo on 1/15/2016.
 */
public class GearVRFragment extends BaseVideoFragment implements View.OnClickListener, TimerHandler.OnTimeoutListener {

    private static final String TAG = GearVRFragment.class.getSimpleName();

    // 38, 47
    // TODO confirm
    private static final int CHAPTER_0_TAP_ON_A_SCENE_INTERACTION = 0;   // 38 sec
    private static final int CHAPTER_1_GEAR_REMAIN_VIDEO = 1;   // 47 sec


    private View mTapOnASceneView;
    private TimerHandler mTimerHandler;
    private int mChapterIndex = -1; // -1 is default


    public static GearVRFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        GearVRFragment fragment = new GearVRFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        mTapOnASceneView = view.findViewById(R.id.gear_tap_on_a_scene);
        mTimerHandler = new TimerHandler();
        mTimerHandler.setOnTimeoutListener(this);

        mTapOnASceneView.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        mTapOnASceneView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
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
            case CHAPTER_0_TAP_ON_A_SCENE_INTERACTION:
                jumpToChapter(CHAPTER_1_GEAR_REMAIN_VIDEO);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gear_tap_on_a_scene:
                if (mChapterIndex == CHAPTER_0_TAP_ON_A_SCENE_INTERACTION) {
                    jumpToChapter(CHAPTER_1_GEAR_REMAIN_VIDEO);
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

        mTapOnASceneView.setVisibility(View.VISIBLE);
        pauseVideo();
        mChapterIndex = CHAPTER_0_TAP_ON_A_SCENE_INTERACTION;
        mTimerHandler.start(10000);
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");

        mTapOnASceneView.setVisibility(View.GONE);
        mChapterIndex = CHAPTER_1_GEAR_REMAIN_VIDEO;
    }

}
