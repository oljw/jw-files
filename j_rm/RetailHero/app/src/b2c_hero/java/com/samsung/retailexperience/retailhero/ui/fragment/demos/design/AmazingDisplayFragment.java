package com.samsung.retailexperience.retailhero.ui.fragment.demos.design;


import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.TimerHandler;

import com.samsung.retailexperience.retailhero.view.VideoTextureView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AmazingDisplayFragment extends BaseVideoFragment implements
        View.OnClickListener, TimerHandler.OnTimeoutListener {

    private static final String TAG = AmazingDisplayFragment.class.getSimpleName();

    private static final int USER_INTERACTION_WAIT_TIME = 6000;

    private static final int CHAPTER_0_JUMP_TO_VOICE_OVER = 0;
    private static final int CHAPTER_1_WAIT_USER_TO_SELECT_A_VIDEO_INTERACTION = 1;
    private int mChapterIndex = -1;

    private OrientationEventListener mOrientationListener;

    private View mSelectSample;
    private VideoTextureView mGameVideoView;
    private View mRotate;
    private View mRotateContainer;

    private String mDefaultSelectedVideo;

    private TimerHandler mTimerHandler;
    private boolean mMainVideoStarted = false;
    private boolean mForceGameListView = false;
    private boolean mIsPaused = true;

    private TimerHandler mDelayGoToChapterTimer;
    private int mAccumulateTime = 0;
    private static final int DELAY_TIMEOUT = 250; //250ms
    private static final int MAX_TIMEOUT = 5000;

    public static AmazingDisplayFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        AmazingDisplayFragment fragment = new AmazingDisplayFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        if (view != null) {
            mSelectSample = view.findViewById(R.id.display_select_sample);
            mRotate = view.findViewById(R.id.display_rotate_device);
            mRotateContainer = view.findViewById(R.id.display_rotate_container);

            mGameVideoView = (VideoTextureView)view.findViewById(R.id.game_video_view);

            if (view.findViewById(R.id.display_game_one) != null) {
                view.findViewById(R.id.display_game_one).setOnClickListener(this);
            }
            if (view.findViewById(R.id.display_game_two) != null) {
                view.findViewById(R.id.display_game_two).setOnClickListener(this);
            }
            if (view.findViewById(R.id.display_game_three) != null) {
                view.findViewById(R.id.display_game_three).setOnClickListener(this);
            }
            if (view.findViewById(R.id.display_game_four) != null) {
                view.findViewById(R.id.display_game_four).setOnClickListener(this);
            }
            if (view.findViewById(R.id.display_game_four) != null) {
                mDefaultSelectedVideo = (String) view.findViewById(R.id.display_game_four).getTag();
            }
        }

        mTimerHandler = new TimerHandler();
        if (mTimerHandler != null) {
            mTimerHandler.setOnTimeoutListener(this);
        }

        if (getFragmentModel().getReservedData() != null) {
            mForceGameListView = Boolean.valueOf(getFragmentModel().getReservedData());
        }

        if (!mForceGameListView) {
            initOrientationListener();
        }

        mDelayGoToChapterTimer = new TimerHandler();
        if (mDelayGoToChapterTimer != null) {
            mDelayGoToChapterTimer.setTimeout(DELAY_TIMEOUT);
            mDelayGoToChapterTimer.setOnTimeoutListener(new TimerHandler.OnTimeoutListener() {
                @Override
                public void onTimeout() {
                    mAccumulateTime += DELAY_TIMEOUT;

                    if (mVideoView != null) {
                        if (mVideoView.isPlaying()) {
                            Log.d(TAG, "@@@ isPlaying is TRUE and stop timer");
                            mAccumulateTime = 0;
                            setForcedSeekToChapter(0);
                        } else {
                            if (mAccumulateTime <= MAX_TIMEOUT) {
                                Log.d(TAG, "@@@ isPlaying is FALSE and restart timer");
                                mDelayGoToChapterTimer.start();
                            } else {
                                Log.d(TAG, "@@@ exceed time");
                            }
                        }
                    }
                }
            });
        }

    }

    private void initOrientationListener() {
        mOrientationListener = new OrientationEventListener(getActivity().getApplicationContext(),
                SensorManager.SENSOR_DELAY_UI) {
            public void onOrientationChanged (int orientation) {
                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) return;

                int degrees = 270;
                if (orientation < 45 || orientation > 315) degrees=0;
                else if (orientation < 135) degrees = 90;
                else if (orientation < 225) degrees = 180;

                if (degrees == 90) {
                    if (mOrientationListener != null) {
                        mOrientationListener.disable();
                    }

                    if (mDelayGoToChapterTimer != null) {
                        mDelayGoToChapterTimer.start();
                    }
                }
            }
        };
    }

    private void hideView(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }
    private void showView(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void startRotateAnimation() {
        showView(mRotateContainer);
        // minimum api is 23 to use getContext().  we are targetting 23 or higher, so ignore now
        Animation rotateAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotator);
        if (rotateAnimation != null && mRotate != null) {
            mRotate.startAnimation(rotateAnimation);
        }
    }

    private void stopRotateAnimation() {
        if (mRotate != null) {
            mRotate.clearAnimation();
        }
        hideView(mRotateContainer);
    }

    private void initializeAmazoingDisplay() {
        playVideo();
        startRotateAnimation();

        hideView(mGameVideoView);
        hideView(mSelectSample);

        mChapterIndex = -1;
        mMainVideoStarted = false;

        if (mOrientationListener != null) {
            mOrientationListener.enable();
        }
    }

    private void initializeGameListView() {
        setUpForGameGalleryListView();

        hideView(mRotateContainer);
    }

    @Override
    public void onResume() {
        super.onResume();

        mAccumulateTime = 0;

        if (mForceGameListView) {
            initializeGameListView();
        } else {
            initializeAmazoingDisplay();
        }
        mIsPaused = false;
    }

    @Override
    public void onPause() {
        super.onPause();

        mIsPaused = true;
        if (mTimerHandler != null) {
            mTimerHandler.stop();
        }
        stopRotateAnimation();

        if (mOrientationListener != null) {
            mOrientationListener.disable();
        }

        if (mGameVideoView != null) {
            if (mGameVideoView.isPlaying()) {
                mGameVideoView.stop();
            }
            mGameVideoView.release();
        }

        if (mDelayGoToChapterTimer != null) {
            mDelayGoToChapterTimer.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mTimerHandler != null) {
            mTimerHandler.setOnTimeoutListener(null);
            mTimerHandler = null;
        }

        if (mOrientationListener != null) {
            mOrientationListener = null;
        }

        if (mDelayGoToChapterTimer != null) {
            mDelayGoToChapterTimer.setOnTimeoutListener(null);
            mDelayGoToChapterTimer = null;
        }
    }

    // TODO update name of function
    void playVideo() {
        Log.d(TAG, "Play Retail mode demo video");
        //mRotate.setVisibility(View.GONE);
        stopRotateAnimation();

        if (mVideoView != null) {
            try {
                synchronized (AmazingDisplayFragment.class) {
                    if (mMainVideoStarted) {
                        return;
                    }
                    mMainVideoStarted = true;
                }
                mVideoView.play();
            } catch (Exception e) {
                Log.e(TAG, e + " : video play");
            }
        }
    }


    @Override
    protected void onVideoCompleted() {
        // don't move to legal video.  Move to legal page when game video is completed
        // super.onVideoCompleted();
    }

    private void gameVideoCompleted() {
        super.onVideoCompleted();
    }

    private String getVideoPath(String videoFileName) {
        //video/b2c_hero_display_demo_0.mp4
        return String.format("video/%s.mp4", videoFileName);
    }

    private String getVideoFramePath(String frameFileName) {
        //video/b2c_hero_display_demo_0.mp4
        // frame/b2c_hero_display_demo_0_frame.jpg
        return String.format("frame/%s_frame.jpg", frameFileName);
    }

    private void playGameVideo(String videoFileName) {
        Log.d(TAG, "Play Game demo video");
        if (mTimerHandler != null) {
            mTimerHandler.stop();
        }

        if (mVideoView.isPlaying()) {
            mVideoView.stop();
        }

        if (mGameVideoView != null) {
            mGameVideoView.release();
        }

        hideView(mSelectSample);

        String videoPath = getVideoPath(videoFileName);
        String framePath = getVideoFramePath(videoFileName);

        //video/b2c_hero_display_demo_3.mp4
        //frame/b2c_hero_display_demo_0_frame.jpg
        if (mGameVideoView != null) {
            mGameVideoView.setVisibility(View.VISIBLE);
            mGameVideoView.setVideoFile(videoPath);
            mGameVideoView.setVideoFrameFile(framePath);
//            mGameVideoView.setVisibility(View.VISIBLE);
            try {
                mGameVideoView.play();
                mGameVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        gameVideoCompleted();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, e + " : video play");
            }
        }
    }


    @Override
    public void onClick(View v) {
        Log.e(TAG, "v.getId(): " + v.getId() + ", same: " + (v.getId() == R.id.display_select_sample));
        if (v.getTag() != null) {
            String videoFilePath = (String)v.getTag();
            playGameVideo(videoFilePath);
        }
    }

    @Override
    public void onTimeout() {
        if (mIsPaused) return;

        switch (mChapterIndex) {
            case -1:
                // default chapter index
//                playVideo();
                break;
            case CHAPTER_1_WAIT_USER_TO_SELECT_A_VIDEO_INTERACTION:
                playGameVideo(mDefaultSelectedVideo);
                break;
        }
    }

    private void setUpForGameGalleryListView() {
        mChapterIndex = CHAPTER_1_WAIT_USER_TO_SELECT_A_VIDEO_INTERACTION;

        if (mSelectSample != null) {
            mSelectSample.setVisibility(View.VISIBLE);
        }
        if (mTimerHandler != null) {
            mTimerHandler.start(USER_INTERACTION_WAIT_TIME);
        }

    }

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = CHAPTER_0_JUMP_TO_VOICE_OVER)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");

        if (mDelayGoToChapterTimer != null) {
            mDelayGoToChapterTimer.stop();
        }

        // disable the listener for orientation
        if (mOrientationListener != null) {
            mOrientationListener.disable();
        }

        mChapterIndex = CHAPTER_0_JUMP_TO_VOICE_OVER;

        stopRotateAnimation();
    }

    @OnChapter(chapterIndex = CHAPTER_1_WAIT_USER_TO_SELECT_A_VIDEO_INTERACTION)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");

        setUpForGameGalleryListView();
    }
}
