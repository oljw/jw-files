package com.samsung.retailexperience.retailecosystem.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.samsung.retailexperience.retailecosystem.R;
import com.samsung.retailexperience.retailecosystem.gson.models.FragmentModel;
import com.samsung.retailexperience.retailecosystem.gson.models.VideoModel;
import com.samsung.retailexperience.retailecosystem.util.AppConst;
import com.samsung.retailexperience.retailecosystem.util.JsonUtil;
import com.samsung.retailexperience.retailecosystem.video.annotation.OnChapter;
import com.samsung.retailexperience.retailecosystem.video.annotation.OnChapterEnded;
import com.samsung.retailexperience.retailecosystem.video.config.environment.Environments;
import com.samsung.retailexperience.retailecosystem.video.gson.models.Chapter;
import com.samsung.retailexperience.retailecosystem.video.gson.models.ExtendedChapter;
import com.samsung.retailexperience.retailecosystem.video.ui.view.CoreSuperTextView;
import com.samsung.retailexperience.retailecosystem.video.ui.view.SuperTextView;
import com.samsung.retailexperience.retailecosystem.video.ui.view.VideoTextureView;
import com.samsung.retailexperience.retailecosystem.video.util.TimerHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by icanmobile on 3/3/16.
 */
public abstract class BaseVideoFragment extends BaseFragment implements
        VideoTextureView.VideoTextureViewListener, MediaPlayer.OnCompletionListener,
        VideoTextureView.VideoBreakPointListener, MediaPlayer.OnTimedTextListener {
    private static final String TAG = BaseVideoFragment.class.getSimpleName();

    private FragmentModel<VideoModel> mFragmentModel = null;
    private AppConst.TransactionDir mTransactionDir = null;

    private OrientationEventListener mOrientationEventListener = null;
    protected FrameLayout mRotationContainer = null;
    protected ImageView mRotationDeviceIv = null;
    protected int mOrientation = 0;
    protected boolean isLandscape = false;
    private TimerHandler mTimerHandler = null;

    protected VideoTextureView mVideoView = null;
    private SparseArray<Method> onChapterMethods = null;
    private SparseArray<Method> onChapterEndedMethods = null;
    protected SuperTextView mSuperTV = null;

    public BaseVideoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // It brings the chapter method
        getChapterMethods();

        if (getArguments() != null) {
            String json = getArguments().getString(AppConst.ARG_FRAGMENT_MODEL);
            Type fragmentType = new TypeToken<FragmentModel<VideoModel>>() {
            }.getType();
            mFragmentModel = JsonUtil.loadJsonModel(getActivity().getApplicationContext(), json, fragmentType);
            mTransactionDir = (AppConst.TransactionDir) getArguments().getSerializable(AppConst.ARG_FRAGMENT_TRANSACTION_DIR);
        }

        onFragmentCreated();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(mFragmentModel.getLayoutResId(), container, false);

        // set background
        if (getFragmentModel().getBackgroundResId() > 0)
            mView.setBackgroundResource(mFragmentModel.getBackgroundResId());

        // set view pivot X,Y
        if (getFragmentModel().getPivotXValue() != 0)
            mView.setPivotX(getResources().getInteger(getFragmentModel().getPivotXValue()));
        if (getFragmentModel().getPivotYValue() != 0)
            mView.setPivotY(getResources().getInteger(getFragmentModel().getPivotYValue()));
        
        mVideoView = (VideoTextureView) mView.findViewById(R.id.video_view);
        if (mVideoView != null) {
            // set files
            if (getFragmentModel().getFragment().getVideoFile() != null) {
                mVideoView.setVideoFile(getFragmentModel().getFragment().getVideoFile());
            }
            if (getFragmentModel().getFragment().getFrameFile() != null) {
                mVideoView.setVideoFrameFile(getFragmentModel().getFragment().getFrameFile());
            }
            if (getFragmentModel().getFragment().getChapterFile() != null) {
                mVideoView.setChapterFile(getFragmentModel().getFragment().getChapterFile());
            }
            if (getFragmentModel().getFragment().getSubTitleFile() != null) {
                mVideoView.setSubTitleFile(getFragmentModel().getFragment().getSubTitleFile());
            }

            // set a video view and listener
            mVideoView.setVideoTextureViewListener(this);
            mVideoView.setVideoBreakpointListener(this);

            // looped and autoPlay
            mVideoView.setLooped(getFragmentModel().getFragment().getLooped());
            mVideoView.setAutoPlay(getFragmentModel().getFragment().getAutoPlay());

            //add super text view
            FrameLayout videoContainer = (FrameLayout) mView.findViewById(R.id.video_container);
            if (mEnvMgr != null) {
                if (mEnvMgr.getBooleanValue(Environments.ENABLE_VIDEO_TITLE)) {
                    mSuperTV = new CoreSuperTextView(getActivity());
                    if (mSuperTV != null) {
                        videoContainer.addView(mSuperTV, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                }
            }

            //add click listener
            if (getFragmentModel().getFragment().getActionClick() != null) {
                mView.setOnClickListener(clickListener);
            }

            /**
             *  development tools
             *
             */
            if (mEnvMgr != null) {
                // verify page and contents file for video, next page
                if (mEnvMgr.getBooleanValue(Environments.SHOW_VIDEO_PAGE_INFO)) {
                    setVideoPageDevTools();
                }

                // for play, pause and seek button
                if (mEnvMgr.getBooleanValue(Environments.SHOW_PLAY_PAUSE_SEEK_BUTTON)) {
                    setOverlayDevTools();
                }
            }
        }

        if (getFragmentModel().getFragment().getOrientation() != null) {
            mOrientation = getResources().getInteger(getFragmentModel().getFragment().getOrientationResId());
            if ( mOrientation == 90 || mOrientation == 270  ) {
                mRotationContainer = (FrameLayout) mView.findViewById(R.id.rotate_container);
                mRotationDeviceIv = (ImageView) mView.findViewById(R.id.rotate_device);
                setOrientation(true);

                mOrientationEventListener = new OrientationEventListener( getActivity().getApplicationContext(),
                        SensorManager.SENSOR_DELAY_UI) {
                    @Override
                    public void onOrientationChanged(int orientation) {
                        Log.d(TAG, "##### onOrientationChanged)+ orientation = " + orientation );
                        if (orientation == ORIENTATION_UNKNOWN) return;
                        int degree = 0;
                        if (orientation < 45 || orientation > 315) degree = 0;
                        else if (orientation < 135) degree = 90;
                        else if (orientation < 225) degree = 180;

                        Log.d(TAG, "##### degree = " + degree + ", mOrientation = " + mOrientation);
                        if (degree == mOrientation) {
                            mOrientationEventListener.disable();

                            if (mTimerHandler != null)
                                mTimerHandler.start(250);
                        }
                    }
                };

                mTimerHandler = new TimerHandler();
                if (mTimerHandler != null) {
                    mTimerHandler.setOnTimeoutListener(new TimerHandler.OnTimeoutListener() {
                        @Override
                        public void onTimeout() {
                            // disable the listener for orientation
                            if (mOrientationEventListener != null) {
                                mOrientationEventListener.disable();
                            }
                            onPlayVideo();
                        }
                    });
                }
            }
        }

        onViewCreated(mView);
        return mView;
    }

    @Override
    public Animator onCreateAnimator(int transit, final boolean enter, int nextAnim) {
        Animator animator = null;

//        Log.d(TAG, "##### enter = " + enter + ", nextAnim = " + nextAnim);
        if (nextAnim == 0) return null;

        animator = AnimatorInflater.loadAnimator(getActivity(), nextAnim);
        if (animator != null) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    onPageTransitionCancel(enter, getTransactionDir());
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    onPageTransitionEnd(enter, getTransactionDir());
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    onPageTransitionStart(enter, getTransactionDir());
                }
            });
        }

        return animator;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        
        if (isLandscape()) {
            if (mVideoView != null)
                mVideoView.setVisibility(View.GONE);
            if (mRotationContainer != null)
                mRotationContainer.setVisibility(View.VISIBLE);

            setRotateAnimation(true);
            if (mOrientationEventListener != null) {
                mOrientationEventListener.enable();
            }
            if (mTimerHandler != null) {
                mTimerHandler.start(AppConst.TIMEOUT_SIX_SECOND);
            }
        }
        else {
            if (mVideoView != null)
                mVideoView.setVisibility(View.VISIBLE);
        }
        
        setMaxVolume();

        if (mSuperTV != null) {
            mSuperTV.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (isLandscape()) {
            setRotateAnimation(false);
            if (mOrientationEventListener != null) {
                mOrientationEventListener.disable();
            }
            if (mTimerHandler != null) {
                mTimerHandler.stop();
            }
        }

        if (mVideoView != null) {
            mVideoView.release();
        }

        if (mSuperTV != null) {
            mSuperTV.setVisibility(View.GONE);
        }
    }

    abstract public void onFragmentCreated();
    abstract public void onViewCreated(View view);
    abstract public void onPlayVideo();

    protected FragmentModel<VideoModel> getFragmentModel() {
        return mFragmentModel;
    }
    protected AppConst.TransactionDir getTransactionDir() {
        return this.mTransactionDir;
    }

    abstract public void onPageTransitionStart(boolean enter, AppConst.TransactionDir dir);
    abstract public void onPageTransitionEnd(boolean enter, AppConst.TransactionDir dir);
    abstract public void onPageTransitionCancel(boolean enter, AppConst.TransactionDir dir);


    /*
     * Video View Landsacpe Mode
     */
    protected boolean isLandscape() {
        return this.isLandscape;
    }
    private void setOrientation(boolean isLandscape) {
        this.isLandscape = isLandscape;
    }
    private void setRotateAnimation(boolean onOff) {
        if (onOff == true) {
            Animation rotateAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotator);
            if (rotateAnimation != null) {
                if (mRotationDeviceIv != null) {
                    mRotationDeviceIv.startAnimation(rotateAnimation);
                }
            }
        } else {
            if (mRotationDeviceIv != null) {
                mRotationDeviceIv.clearAnimation();
            }
            if (mRotationContainer != null) {
                mRotationContainer.setVisibility(View.GONE);
            }
        }
    }


    /**
     * When a video is completed, move to End page
     * Override this function if moving to end page is unnecessary
     */
    protected void onVideoCompleted() {
        moveToEndPage();
    }

    private void moveToEndPage() {
        if (getFragmentModel() == null || getFragmentModel().getFragment().getAction() == null) return;
        changeFragment(getFragmentModel().getFragment().getAction(),
                AppConst.TransactionDir.TRANSACTION_DIR_FORWARD);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (getFragmentModel() == null || getFragmentModel().getFragment().getActionClick() == null) return;
            changeFragment(getFragmentModel().getFragment().getActionClick(),
                    AppConst.TransactionDir.TRANSACTION_DIR_FORWARD);
        }
    };

    /**
     * methods for the VideoTextureView
     * 1. callback methods of listener for the VideoTextureView
     * 2. function settings of the VideoTextureView
     */

    // collect the chapter methods
    private void getChapterMethods() {
        // It brings the chapter method
        onChapterMethods = new SparseArray<Method>();
        onChapterEndedMethods = new SparseArray<Method>();
        final Method[] methods = ((Object) this).getClass().getDeclaredMethods();
        for (Method m : methods) {
            // is OnChapter?
            if (m.isAnnotationPresent(OnChapter.class)) {
                int chapterIndex = m.getAnnotation(OnChapter.class).chapterIndex();
                onChapterMethods.put(chapterIndex, m);
            } else if (m.isAnnotationPresent(OnChapterEnded.class)) {
                int chapterIndex = m.getAnnotation(OnChapterEnded.class).chapterIndex();
                onChapterEndedMethods.put(chapterIndex, m);
            }
        }
    }

    @Override
    public void onVideoPrepared(VideoTextureView view, int width, int height) {
        if (view != null) {
            view.setOnCompletionListener(this);
            view.setOnTimedTextListener(this);
        }
    }

    @Override
    public void onChaptersLoaded(List<ExtendedChapter> chapters) {
        Log.d(TAG, "Loaded a chapter file");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "Video Completion");

        onVideoCompleted();
    }

    @Override
    public void onVideoBreakPoint(VideoTextureView view, int nextChapter, final Chapter.ActionInfo actionInfo) {
        final Method m = onChapterMethods.get(nextChapter);
        if (m != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        m.invoke(BaseVideoFragment.this, new Object[]{actionInfo});
                    } catch (Exception ex) {
                        Log.e(TAG, ex + "problem invoking OnChapter method");
                    }
                }
            });
        } else {
            Log.w(TAG, "received next chapter " + nextChapter + " but no method was defined to handle it");
        }
    }

    @Override
    public void onChapterEnded(VideoTextureView view, int chapterEndedIndex, final Chapter.ActionInfo actionInfo) {
        final Method m = onChapterEndedMethods.get(chapterEndedIndex);
        if (m != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        m.invoke(BaseVideoFragment.this, new Object[]{actionInfo} );
                    } catch (Exception ex) {
                        Log.e(TAG, ex + "problem invoking OnChapterEnded method");
                    }
                }
            });
        } else {
            Log.w(TAG, "received chapterEndedIndex " + chapterEndedIndex + " but no method was defined to handle it");
        }
    }

    @Override
    public void onTimedText(MediaPlayer mp, TimedText text) {
        if (mSuperTV != null) {
            String superText = null;
            if (text != null) {
                superText = text.getText();
            }

            if (superText == null) {
                Log.d(TAG, "clearing super");
                mSuperTV.setVisibility(View.INVISIBLE);
            } else {
                Log.d(TAG, "super: " + superText);
                mSuperTV.setVisibility(View.VISIBLE);
            }

            mSuperTV.setSuperText(superText);
        }
    }

    protected void setBaseVideoContents(VideoTextureView video,
                                        List<String> videoContents,
                                        boolean displayVideoFrame) {
        if (video == null || videoContents == null || videoContents.isEmpty()) {
            Log.e(TAG, "the video contents is null");
            return;
        }

        if (mResMgr.isMissingFileList(videoContents)) {
            return;
        }

        for (String filename: videoContents) {
            String[] splitStr = filename.split("/");
            switch(splitStr[0]) {
                case "video":
                    video.setVideoFile(filename);
                    break;
                case "frame":
                    if (displayVideoFrame == true) {
                        video.setVideoFrameFile(filename);
                    }
                    break;
                case "chapter":
                    video.setChapterFile(filename);
                    break;
                case "subtitle":
                    video.setSubTitleFile(filename);
                    break;
                default:
                    Log.w(TAG, "unknown video contents format : " + filename );
                    break;
            }
        }
    }

    protected void setForcedSeekToChapter(int index) {
        Log.d(TAG, "next chapter is " + index);
        if (mVideoView != null) {
            if (mVideoView.hasChapters()) {
                if (index < mVideoView.getTotalChapters()) {
                    mVideoView.seekToChapter(index);
                }
            }
        } else {
            Log.w(TAG, "VideoView is null");
        }
    }

    protected void playVideo(VideoTextureView video) {
        Log.d(TAG, "##### playVideo)+ ");
        if (mRotationContainer != null) {
            mRotationContainer.setVisibility(View.INVISIBLE);
        }

        video.setVisibility(View.VISIBLE);

        if (video != null) {
            try {
                Log.d(TAG, "##### playVideo : video.play() ");
                video.play();
            } catch (Exception e) {
                Log.e(TAG, e + " : problem starting video!");
            }
        }
        Log.d(TAG, "##### playVideo)- ");
    }


    /*=====================================================================
    *
    *   development tool for page
    *
    ======================================================================*/
    private void setVideoPageDevTools() {
        if (mView == null) {
            return;
        }

        FrameLayout videoContainer = (FrameLayout) mView.findViewById(R.id.video_container);

        // Play Video
        String message = new StringBuilder()
                .append(mFragmentModel.getFragment().getVideoFile())
                .append("\n" + mFragmentModel.getFragment().getFrameFile())
                .append("\n" + mFragmentModel.getFragment().getChapterFile())
                .append("\n" + mFragmentModel.getFragment().getSubTitleFile())
                .toString();
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();

        // Show Title
        String strModelName = null;
        if (mEnvMgr != null) {
            strModelName = mEnvMgr.getStringValue(Environments.FLAVOR);
        }
        TextView titleTv = new TextView(mAppContext);
        if (mFragmentModel.getFragment().getTitleResId() != 0) {
            titleTv.setText(strModelName.toUpperCase() + "\n" + getString(mFragmentModel.getFragment().getTitleResId()));
        } else {
            titleTv.setText(strModelName.toUpperCase() + "\n" + mFragmentModel.getFragment().getTitle());
        }
        titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30.f);
        titleTv.setTextColor(Color.parseColor("#FFFF0000"));
        videoContainer.addView(titleTv, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));

        // Skip Button
        Button skipBtn = new Button(mAppContext);
        if (skipBtn != null) {
            skipBtn.setText("SKIP VIDEO");
            skipBtn.setGravity(Gravity.CENTER);
            videoContainer.addView(skipBtn, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM));
            skipBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getFragmentModel() == null || getFragmentModel().getFragment().getAction() == null) return;
                    changeFragment(getFragmentModel().getFragment().getAction(),
                            AppConst.TransactionDir.TRANSACTION_DIR_FORWARD);
                }
            });
        }

    }


    /*=====================================================================
    *
    *   development tool for play, pause and seek button
    *
    ======================================================================*/
    private void setOverlayDevTools() {
        if (mView == null) {
            return;
        }

        FrameLayout videoContainer = (FrameLayout) mView.findViewById(R.id.video_container);
        View overlay_dev = getActivity().getLayoutInflater().inflate(R.layout.video_dev_tool, null);
        videoContainer.addView(overlay_dev, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ImageView imgSeekButton = (ImageView) overlay_dev.findViewById(R.id.chapterSeekButt);
        ImageView imgPlayPauseButton = (ImageView) overlay_dev.findViewById(R.id.playPauseButt);

        // Seek button Click
        imgSeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoView != null) {
                    if (mVideoView.hasChapters()) {
                        Integer ind = mVideoView.getCurrentChapterIndex();
                        if (ind == null || ind < -1) {
                            return;
                        }
                        setForcedSeekToChapter(++ind);
                    }
                } else {
                    Log.w(TAG, "Video View is null");
                }
            }
        });

        // play or pause button click
        imgPlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoView != null) {
                    if (mVideoView.isPlaying()) {
                        mVideoView.pause();
                        ((ImageView)v).setImageResource(R.drawable.play_button);
                    } else {
                        try {
                            mVideoView.play();
                        } catch (Exception ex) {
                            Log.e(TAG, ex + "problem playing video player");
                        }
                        ((ImageView)v).setImageResource(R.drawable.pause_button);
                    }
                } else {
                    Log.w(TAG, "Video View is null");
                }
            }
        });
    }
}
