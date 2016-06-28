package com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusive;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.panframe.android.lib.PFAsset;
import com.panframe.android.lib.PFAssetObserver;
import com.panframe.android.lib.PFAssetStatus;
import com.panframe.android.lib.PFNavigationMode;
import com.panframe.android.lib.PFObjectFactory;
import com.panframe.android.lib.PFView;
import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.SubVideoModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.TimerHandler;

import java.io.File;

/**
 * Created by smheo on 1/15/2016.
 */
public class GearVRFragment extends BaseVideoFragment implements AdapterView.OnItemClickListener,
        PFAssetObserver {

    private static final String TAG = GearVRFragment.class.getSimpleName();

    private static final int CHAPTER_0_END_ROTATION = 0;
    private static final int CHAPTER_1_SHOW_DISCLAIMER = 1;
    private static final int CHAPTER_2_END_DISCLAIMER = 2;
    private static final int CHAPTER_3_SHOW_TAP_ON_A_SCENE = 3;
    private static final int CHAPTER_4_END_TAP_ON_A_SCENE = 4;

    private FrameLayout mRotateContainer = null;
    private ImageView mRotateDeviceIv = null;
    private FrameLayout mSceneContainer = null;
    private ImageView mGearVrDisclaimerIv = null;
    private GridView mVrGridView = null;

    private View mVrBackgroundView;
    private ViewGroup mVrVideoContainer = null;
    private PFView mpfVideoView;
    private PFAsset mpfVideoAsset;

    private int mVideoIndex = -1;
    //private int mChapterIndex = -1;
    private boolean gotoGallery = false;

    // for the screen orientation
    private OrientationEventListener mOrientationEventListener = null;
    private TimerHandler mStartVideoTimer;
    private int mAccumulateTime = 0;
    private static final int PLAY_VIDEO_TIMEOUT = 150;
    private static final int ROTATE_TIMEOUT = 250; //150ms
    private static final int MAX_TIMEOUT = 3000;

    // gear vr demo video
    private static final SubVideoModel[] mVrItemList = {
            new SubVideoModel("video/b2c_hero_vr_demo_0.mp4", "frame/b2c_hero_vr_demo_0_frame.jpg"),
            new SubVideoModel("video/b2c_hero_vr_demo_1.mp4", "frame/b2c_hero_vr_demo_1_frame.jpg"),
            new SubVideoModel("video/b2c_hero_vr_demo_2.mp4", "frame/b2c_hero_vr_demo_2_frame.jpg"),
            new SubVideoModel("video/b2c_hero_vr_demo_3.mp4", "frame/b2c_hero_vr_demo_3_frame.jpg"),
            new SubVideoModel("video/b2c_hero_vr_demo_4.mp4", "frame/b2c_hero_vr_demo_4_frame.jpg"),
            new SubVideoModel("video/b2c_hero_vr_demo_5.mp4", "frame/b2c_hero_vr_demo_5_frame.jpg")
    };

    public static GearVRFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        GearVRFragment fragment = new GearVRFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        if (view != null) {
            // image view for the screen rotation
            mRotateContainer = (FrameLayout) view.findViewById(R.id.vr_rotate_container);
            mRotateDeviceIv = (ImageView) view.findViewById(R.id.vr_rotate_device);

            mGearVrDisclaimerIv = (ImageView) view.findViewById(R.id.gear_vr_disclaimer_iv);

            // container view include to grid view(tap the vr demo)
            mSceneContainer = (FrameLayout) view.findViewById(R.id.gear_vr_scene_container);
            mVrGridView = (GridView) view.findViewById(R.id.vr_grid);
            if (mVrGridView != null) {
                mVrGridView.setAdapter(new GearVrListAdapter());
                mVrGridView.setOnItemClickListener(this);
            }

            // vr scene video view
            mVrVideoContainer = (ViewGroup) view.findViewById(R.id.vr_video_view);

            // vr background view
            mVrBackgroundView = view.findViewById(R.id.vr_background);
        }

        mOrientationEventListener = new OrientationEventListener(getActivity().getApplicationContext(),
                SensorManager.SENSOR_DELAY_UI) {
            public void onOrientationChanged (int orientation) {
                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) return;

                int degrees = 270;
                if (orientation < 45 || orientation > 315) degrees=0;
                else if (orientation < 135) degrees = 90;
                else if (orientation < 225) degrees = 180;

                if (degrees == 90) {
                    Log.d(TAG,"@@@ degree is 90");
                    // disable the listener for orientation
                    if (mOrientationEventListener != null) {
                        mOrientationEventListener.disable();
                    }

                    if (mStartVideoTimer != null) {
                        mStartVideoTimer.setTimeout(ROTATE_TIMEOUT);
                        mStartVideoTimer.start();
                    }
                }
            }
        };

        mStartVideoTimer = new TimerHandler();
        if (mStartVideoTimer != null) {
            mStartVideoTimer.setOnTimeoutListener(new TimerHandler.OnTimeoutListener() {
                @Override
                public void onTimeout() {

                    if (gotoGallery) {
                        mAccumulateTime += PLAY_VIDEO_TIMEOUT;
                    } else {
                        mAccumulateTime += ROTATE_TIMEOUT;
                    }

                    if (mVideoView != null) {
                        if (mVideoView.isPlaying()) {
                            Log.i(TAG, "@@@ timer - isPlaying : true");
                            mAccumulateTime = 0;

                            if (gotoGallery) {
                                setForcedSeekToChapter(CHAPTER_3_SHOW_TAP_ON_A_SCENE);
                            } else {
                                setForcedSeekToChapter(CHAPTER_0_END_ROTATION);
                            }
                        } else {
                            if (mAccumulateTime <= MAX_TIMEOUT) {
                                Log.i(TAG, "@@@ timer - isPlaying : false..restart timer");
                                mStartVideoTimer.start();
                            } else {
                                Log.d(TAG, "@@@ exceed time");
                                mAccumulateTime = 0;
                                mStartVideoTimer.stop();
                            }
                        }
                    }
                }
            });
        }

        if (getFragmentModel().getReservedData() != null)
            gotoGallery = Boolean.valueOf(getFragmentModel().getReservedData());
    }

    @Override
    public void onResume() {
        super.onResume();

        mVideoIndex = -1;
//        mChapterIndex = -1;
        mAccumulateTime = 0;

        if (mVrBackgroundView != null) {
            mVrBackgroundView.setVisibility(View.GONE);
        }

        if (mVrVideoContainer != null) {
            mVrVideoContainer.setVisibility(View.GONE);
        }

        if (mSceneContainer != null) {
            mSceneContainer.setVisibility(View.GONE);
        }

        if (mVrGridView != null) {
            mVrGridView.setEnabled(true);
        }

        if (mVideoView != null) {
            mVideoView.setVisibility(View.GONE);
        }

        if (mGearVrDisclaimerIv != null) {
            mGearVrDisclaimerIv.setVisibility(View.GONE);
        }

        if (gotoGallery) {
            Log.i(TAG, "@@@ in Gallery");

            if (mRotateContainer != null) {
                mRotateContainer.setVisibility(View.GONE);
            }
            stopRotateAnimation();

            if (mOrientationEventListener != null) {
                mOrientationEventListener.disable();
            }

            // main play video & visible
            if (mVideoView != null) {
                mVideoView.setShowVideoFrame(false);
                mVideoView.setVisibility(View.VISIBLE);
                playVideo(mVideoView);
            }

            if (mStartVideoTimer != null) {
                mStartVideoTimer.setTimeout(PLAY_VIDEO_TIMEOUT);
                mStartVideoTimer.start();
            }

        } else {
            if (mRotateContainer != null) {
                mRotateContainer.setVisibility(View.VISIBLE);
            }
            startRotateAnimation();

            if (mOrientationEventListener != null) {
                mOrientationEventListener.enable();
            }

            // main play video & visible
            if (mVideoView != null) {
                mVideoView.setVisibility(View.VISIBLE);
                playVideo(mVideoView);
            }
        }

    }

    @Override
    public void onPause() {
        if (gotoGallery == true) {
            if (mStartVideoTimer != null) {
                mStartVideoTimer.stop();
            }
        }

        super.onPause();

        // release resource for vr demo video assets
        if (mpfVideoAsset != null) {
            mpfVideoAsset.stop();
            mpfVideoAsset.release();
            mpfVideoAsset = null;
        }

        if (mOrientationEventListener != null) {
            mOrientationEventListener.disable();
        }

        stopRotateAnimation();
    }

    @Override
    public void onDestroy()  {
        super.onDestroy();

        if (mVrVideoContainer != null) {
            mVrVideoContainer = null;
        }

        if (mVrVideoContainer != null) {
            if (mpfVideoView != null) {
                mpfVideoView.release();
                View view = mpfVideoView.getView();
                if (view != null) {
                    mVrVideoContainer.removeView(view);
                }
                mpfVideoView = null;
            }
            mVrVideoContainer = null;
        }

        if (mVrBackgroundView != null) {
            mVrBackgroundView = null;
        }

        if (mRotateDeviceIv != null) {
            mRotateDeviceIv = null;
        }

        if (mGearVrDisclaimerIv != null) {
            mGearVrDisclaimerIv = null;
        }

        if (mOrientationEventListener != null) {
            mOrientationEventListener = null;
        }

        if (mStartVideoTimer != null) {
            mStartVideoTimer.setOnTimeoutListener(null);
            mStartVideoTimer = null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mVideoView != null) {
            mVideoView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int position = i;
        int totalCnt = adapterView.getCount();
        if (totalCnt > 0 && position < totalCnt) {
            mVideoIndex = position;
            //Log.d(TAG, "@@@@@@ onItemClick : " + mVideoIndex);
            setForcedSeekToChapter(CHAPTER_4_END_TAP_ON_A_SCENE);
        }
    }

    private void startRotateAnimation() {
        if (mRotateDeviceIv != null) {
            Animation rotateAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotator);
            if (rotateAnimation != null) {
                mRotateDeviceIv.startAnimation(rotateAnimation);
            }
        }
    }

    private void stopRotateAnimation() {
        if (mRotateDeviceIv != null) {
            mRotateDeviceIv.clearAnimation();
        }

        if (mRotateContainer != null) {
            mRotateContainer.setVisibility(View.GONE);
        }
    }


    public void loadVrVideo(int videoIndex) {
        if (videoIndex < 0) return;

        String vrVideoFileName = null;
        if (mVrItemList != null) {
            vrVideoFileName = mVrItemList[videoIndex].getVideoFile();
        }
        if (vrVideoFileName == null) return;
        if (mResMgr.isMissingContentFile(vrVideoFileName)) return;

        Uri fileUri = Uri.fromFile(new File(mResMgr.getContentFilePath(vrVideoFileName)));
        if (fileUri == null) return;


        if (mpfVideoView != null) {
            mpfVideoView.release();
            View view = mpfVideoView.getView();
            if (view != null && mVrVideoContainer != null) {
                mVrVideoContainer.removeView(view);
            }
            mpfVideoView = null;
        }

        if (mpfVideoView == null) {
            mpfVideoView = PFObjectFactory.view(getActivity());
        }

        if (mpfVideoView != null) {
            mpfVideoAsset = PFObjectFactory.assetFromUri(getActivity(), fileUri, this);
            if (mpfVideoAsset != null) {
                mpfVideoView.displayAsset(mpfVideoAsset);
                mpfVideoView.setNavigationMode(PFNavigationMode.MOTION);
                if (mVrVideoContainer != null) {
                    mVrVideoContainer.addView(mpfVideoView.getView(), 0);
                }
            }
        }
    }

    /**
     * Status callback from the PFAsset instance.
     * Based on the status this function selects the appropriate action.
     *
     * @param  asset  The asset who is calling the function
     * @param  status The current status of the asset.
     */
    public void onStatusMessage(final PFAsset asset, PFAssetStatus status) {
        switch (status)
        {
            case LOADED:
                Log.d(TAG, "Vr Video : Loaded");

                if (mpfVideoAsset != null) {
                    Log.d(TAG, "Vr Video : starting play");
                    mpfVideoAsset.play();
                }

                // again play the main video
                if(mVideoView != null) {
                    playVideo(mVideoView);
                }
                break;
            case COMPLETE:
                Log.d(TAG, "Vr Video : Complete");
                if (mVrBackgroundView != null) {
                    mVrBackgroundView.setVisibility(View.VISIBLE);
                }

                // competed a gear-vr sub-video -> go to the end page
                onVideoCompleted();

                break;
            case PLAYING:
                Log.d(TAG, "Vr Video : Playing");
                if (mVrBackgroundView != null) {
                    mVrBackgroundView.setVisibility(View.GONE);
                }
                break;
            case ERROR:
                Log.e(TAG, "Vr Video : Error");
                break;
        }
    }


    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = CHAPTER_0_END_ROTATION)
    public void onChaper_0() {
        Log.i(TAG, "Chapter 0 : stop rotation and then play video");

//        mChapterIndex = CHAPTER_0_END_ROTATION;

        // disable the listener for orientation
        if (mOrientationEventListener != null) {
            mOrientationEventListener.disable();
        }

        if (mVideoView != null) {
            mVideoView.setVisibility(View.VISIBLE);
        }

        if (mRotateContainer != null) {
            mRotateContainer.setVisibility(View.GONE);
        }
        stopRotateAnimation();
    }

    @OnChapter(chapterIndex = CHAPTER_1_SHOW_DISCLAIMER)
    public void onChaper_1() {
        Log.i(TAG, "Chapter 1 : show a disclaimer");

//        mChapterIndex = CHAPTER_1_SHOW_DISCLAIMER;

        if (mGearVrDisclaimerIv != null) {
            mGearVrDisclaimerIv.setVisibility(View.VISIBLE);
        }
    }

    @OnChapter(chapterIndex = CHAPTER_2_END_DISCLAIMER)
    public void onChaper_2() {
        Log.i(TAG, "Chapter 2 : end a disclaimer");

//        mChapterIndex = CHAPTER_2_END_DISCLAIMER;

        if (mGearVrDisclaimerIv != null) {
            mGearVrDisclaimerIv.setVisibility(View.GONE);
        }
    }

    @OnChapter(chapterIndex = CHAPTER_3_SHOW_TAP_ON_A_SCENE)
    public void onChaper_3() {
        Log.i(TAG, "@@@ Chapter 3 : tap on a scene");

//        mChapterIndex = CHAPTER_3_SHOW_TAP_ON_A_SCENE;

        // stop timer
        if (mStartVideoTimer != null) {
            mStartVideoTimer.stop();
        }

        // stop the orientation listener
        if (mOrientationEventListener != null) {
            mOrientationEventListener.disable();
        }

        // invisible the main video view
        if (mVideoView != null) {
            mVideoView.setVisibility(View.INVISIBLE);
        }

        // visible list of gear vr
        if (mSceneContainer != null) {
            mSceneContainer.setVisibility(View.VISIBLE);
        }

        if (mVrGridView != null) {
            mVrGridView.setEnabled(true);
            mVrGridView.setFocusable(true);
        }
    }

    @OnChapter(chapterIndex = CHAPTER_4_END_TAP_ON_A_SCENE)
    public void onChaper_4() {
        Log.i(TAG, "Chapter 4 : end the tap on a scene");

//        mChapterIndex = CHAPTER_4_END_TAP_ON_A_SCENE;

        if (mVrGridView != null) {
            mVrGridView.setEnabled(false);
        }

        // paused the main video. and i will replay after play a vr video
        if(mVideoView != null) {
            mVideoView.pause();
        }

        if (mVrBackgroundView != null) {
            mVrBackgroundView.setVisibility(View.VISIBLE);
        }

        if (mVrVideoContainer != null) {
            // setting the video resources and playing each demo videos
            if (mVideoIndex == -1) {
                // play a default gear vr demo video
                loadVrVideo(4);
            } else {
                // play selected video
                loadVrVideo(mVideoIndex);
            }

            // showing the vr demo video view
            mVrVideoContainer.setVisibility(View.VISIBLE);
        }

        if (mSceneContainer != null) {
            mSceneContainer.setVisibility(View.GONE);
        }

    }

    private class GearVrListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mVrItemList.length;
        }

        @Override
        public Object getItem(int position) {
            return mVrItemList[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                Context context = getActivity().getApplicationContext();
                convertView = LayoutInflater.from(context).inflate(R.layout.gear_vr_item, mVrGridView, false);
            }
            return convertView;
        }

    }
}
