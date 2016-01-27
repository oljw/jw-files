package com.samsung.retailexperience.retailhero.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.config.environment.Environments;
import com.samsung.retailexperience.retailhero.gson.models.Chapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.AssetUtil;
import com.samsung.retailexperience.retailhero.view.CoreSuperTextView;
import com.samsung.retailexperience.retailhero.view.SuperTextView;
import com.samsung.retailexperience.retailhero.view.VideoTextureView;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by icanmobile on 1/12/16.
 */
public abstract class BaseVideoFragment extends BaseFragment implements
        VideoTextureView.VideoTextureViewListener, MediaPlayer.OnCompletionListener,
        VideoTextureView.VideoBreakPointListener, MediaPlayer.OnTimedTextListener {

    private static final String TAG = BaseVideoFragment.class.getSimpleName();

    protected View mView = null;
    protected String mJsonModel = null;
    protected FragmentModel<VideoModel> mFragmentModel = null;

    protected VideoTextureView mVideoView;
    private SparseArray<Method> onChapterMethods;
    protected SuperTextView mSuperTV;

    public BaseVideoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // It brings the chapter method
        getChapterMethods();

        if (getArguments() != null) {
            mFragmentModel = (FragmentModel<VideoModel>) getArguments().getSerializable(AppConsts.ARG_FRAGMENT_MODEL);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        mView = inflater.inflate(mFragmentModel.getLayoutResId(), container, false);

        //set drawer lock/unlock
        setDrawer(mFragmentModel.getDrawerId());

        //set background color
        if (mFragmentModel.getBackgroundResId() > 0)
            mView.setBackgroundResource(mFragmentModel.getBackgroundResId());

        if (mView != null) {
            mVideoView = (VideoTextureView) mView.findViewById(R.id.video_view);
            if (mVideoView != null) {
                // set files
                if (mFragmentModel.getFragment().getVideoFile() != null) {
                    mVideoView.setVideoFile(mFragmentModel.getFragment().getVideoFile());
                }
                if (mFragmentModel.getFragment().getFrameFile() != null) {
                    mVideoView.setVideoFrameFile(mFragmentModel.getFragment().getFrameFile());
                }
                if (mFragmentModel.getFragment().getChapterFile() != null) {
                    mVideoView.setChapterFile(mFragmentModel.getFragment().getChapterFile());
                }
                if (mFragmentModel.getFragment().getSubTitleFile() != null) {
                    mVideoView.setSubTitleFile(mFragmentModel.getFragment().getSubTitleFile());
                }

                // set a video view and listener
                mVideoView.setVideoTextureViewListener(this);
                mVideoView.setVideoBreakpointListener(this);
            }

            //add super text view
            FrameLayout videoContainer = (FrameLayout) mView.findViewById(R.id.video_container);
            if (mEnvMgr != null) {
                if (mEnvMgr.getBooleanValue(Environments.ENABLE_VIDEO_TITLE)) {
                    mSuperTV = new CoreSuperTextView(getActivity());
                    if (mSuperTV != null) {
                        videoContainer.addView(mSuperTV, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    }
                }
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

        Log.d(TAG, "##### BaseVideoFragment : setDrawer : mFragmentModel.getDrawerResId() = " + mFragmentModel.getDrawerId());
        setDrawer(mFragmentModel.getDrawerId());

        onViewCreated(mView);
        return mView;
    }

    abstract public void onViewCreated(View view);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();

        setMaxVolume();

        if (mVideoView != null) {
            try {
                mVideoView.play();
            } catch (Exception e) {
                Log.e(TAG, e + " : video play");
            }
        }

        if (mSuperTV != null) {
            mSuperTV.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "########## onPause)+ ");

        Log.d(TAG, "########## onPause : mSuperTV = " + mSuperTV);
        Log.d(TAG, "########## onPause : mVideoView = " + mVideoView);

        if (mVideoView != null) {
            mVideoView.release();
        }

        if (mSuperTV != null) {
            mSuperTV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy()  {
        super.onDestroy();
    }

    public View getView() {
        return mView;
    }

    @Override
    public void onBackPressed() {
        if (mFragmentModel != null) {
            changeFragment(AppConst.UIState.valueOf(mFragmentModel.getActionBackKey()),
                    AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
        }
    }

    protected FragmentModel<VideoModel> loadJsonFragmentModel() {
        Context context = getActivity().getApplicationContext();
        String data = AssetUtil.GetTextFromAsset(context, mJsonModel);

        Gson gson = new Gson();
        Type fragmentType = new TypeToken<FragmentModel<VideoModel>>() {}.getType();
        return gson.fromJson(data, fragmentType);
    }

    protected FragmentModel<VideoModel> getFragmentModel() {
        return mFragmentModel;
    }


    /**
     * methods for the VideoTextureView
     * 1. callback methods of listener for the VideoTextureView
     * 2. function settings of the VideoTextureView
     */

    // collect the chapter methods
    private void getChapterMethods() {
        // It brings the chapter method
        onChapterMethods = new SparseArray<Method>();
        final Method[] methods = ((Object) this).getClass().getDeclaredMethods();
        for (Method m : methods) {
            // is OnChapter?
            if (m.isAnnotationPresent(OnChapter.class)) {
                int chapterIndex = m.getAnnotation(OnChapter.class).chapterIndex();
                onChapterMethods.put(chapterIndex, m);
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
    public void onChaptersLoaded(List<Chapter> chapters) {
        Log.d(TAG, "Loaded a chapter file");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "Video Completion");
    }

    @Override
    public void onVideoBreakPoint(VideoTextureView view, int nextChapter) {
        final Method m = onChapterMethods.get(nextChapter);
        if (m != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        m.invoke(BaseVideoFragment.this, (Object[]) null);
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
        if (video != null) {
            try {
                video.play();
            } catch (Exception e) {
                Log.e(TAG, e + " : problem starting video!");
            }
        }
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
        videoContainer.addView(titleTv, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));

        // Skip Button
        Button skipBtn = new Button(mAppContext);
        if (skipBtn != null) {
            skipBtn.setText("SKIP VIDEO");
            skipBtn.setGravity(Gravity.CENTER);
            videoContainer.addView(skipBtn, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.BOTTOM));
            skipBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // end page
                    changeEndDemoFragment(AppConst.UIState.valueOf(mFragmentModel.getActionBackKey()),
                            AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
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
        View overlay_dev = getActivity().getLayoutInflater().inflate(R.layout.video_dev_tool_layout, null);
        videoContainer.addView(overlay_dev, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

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
