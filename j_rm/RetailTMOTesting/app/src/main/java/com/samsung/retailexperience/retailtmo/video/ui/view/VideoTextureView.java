package com.samsung.retailexperience.retailtmo.video.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.samsung.retailexperience.retailtmo.R;
import com.samsung.retailexperience.retailtmo.video.config.ConfigProvider;
import com.samsung.retailexperience.retailtmo.video.config.environment.Environments;
import com.samsung.retailexperience.retailtmo.video.config.environment.IEnvironments;
import com.samsung.retailexperience.retailtmo.video.gson.models.Chapter;
import com.samsung.retailexperience.retailtmo.video.gson.models.ExtendedChapter;
import com.samsung.retailexperience.retailtmo.video.util.ChapterParseTask;
import com.samsung.retailexperience.retailtmo.video.util.ChapterParseUtil;
import com.samsung.retailexperience.retailtmo.video.util.ResourceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by smheo on 9/29/2015.
 */
public class VideoTextureView  extends FrameLayout implements
        TextureView.SurfaceTextureListener, MediaPlayer.OnPreparedListener, ChapterParseTask.ChapterParseListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = "VideoTextureView";

    private static final boolean ENABLE_ENDCHAPTER = true;

    public interface VideoTextureViewListener {
        void onVideoPrepared(VideoTextureView view, int width, int height);
        void onChaptersLoaded(List<ExtendedChapter> chapters);
    }

    public interface VideoBreakPointListener {
        void onVideoBreakPoint(VideoTextureView view, int nextChapter, Chapter.ActionInfo action);
        void onChapterEnded(VideoTextureView view, int nextChapter, Chapter.ActionInfo action);
    }

    private Context mContext;
    private TextureView mTV;
    private MediaPlayer mPlayer;
    private SurfaceTexture mSurface;
    private VideoTextureViewListener mListener;
    private VideoBreakPointListener mVideoBreakPointListener;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private final Handler mHandler = new Handler();
    private Runnable mRunnableVideoBreak;
    private Runnable mRunnableChapterEnded;

    private ResourceUtil mResourceUtil;
    private IEnvironments mEnv;

    private ImageView mImgView_VideoFrame;
    private String mVideoFileName;
    private String[] mPlaylist;
    private int mTrack;

    private String mVideoFrameFileName;
    private Date mLastFrameFileDate = new Date(System.currentTimeMillis());
    private Bitmap mBgBitmap;
    private boolean mIsShowVideoFrame;

    private String mChapterFileName;
    private List<ExtendedChapter> mChapters;
    private ChapterParseTask mChapterParseTask;
    private int mNextChapter = 0;

    private String mSubTitleFileName;
    private Uri mSubtitleUri;
    private String mSubtitleMimeType;

    private boolean mIsAutoPlay;
    private boolean mShouldLoop;
    private boolean mIsPrepared;
    private boolean mShouldPlay;
    private boolean mSurfaceReady;
    private boolean mScaleToVideoSize;
    private boolean mHasChapters;
    private boolean mIsPaused;

    private float mSizeMultiplier = 1;
    private int mLayout_width = 0;
    private int mLayout_height = 0;

    // for development
    private VideoTimerView mVideoTimer;

    public VideoTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public VideoTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public VideoTextureView(Context context) {
        super(context);
        init(context, null);
    }


    private void setResource(){
        mResourceUtil = ((ConfigProvider)mContext.getApplicationContext()).getResourceUtil();
        mEnv = ((ConfigProvider)mContext.getApplicationContext()).getEnvironmentConfig();

        mChapterParseTask = new ChapterParseTask(new ChapterParseUtil(mContext.getResources()));
    }

    protected void init(Context context, AttributeSet attrs) {
        if (isInEditMode())
            return;

        this.mContext = context;
        setResource();

        TypedArray a = mContext.obtainStyledAttributes(attrs,
                R.styleable.VideoTextureView);

        mVideoFileName = a.getString(R.styleable.VideoTextureView_videoFile);
        mVideoFrameFileName = a.getString(R.styleable.VideoTextureView_videoFrameFile);
        mChapterFileName = a.getString(R.styleable.VideoTextureView_chapterFile);
        mSubTitleFileName = a.getString(R.styleable.VideoTextureView_subtitleFile);
        final int playlistResourceId = a.getResourceId(R.styleable.VideoTextureView_playlistResource, -1);
        mSizeMultiplier = a.getFloat(R.styleable.VideoTextureView_sizeMultiplier, 1);
        mIsAutoPlay = a.getBoolean(R.styleable.VideoTextureView_autoPlay, false);
        mShouldLoop = a.getBoolean(R.styleable.VideoTextureView_looped, false);
        mScaleToVideoSize = a.getBoolean(R.styleable.VideoTextureView_scaleToVideoSize, false);
        mIsShowVideoFrame = a.getBoolean(R.styleable.VideoTextureView_isShowVideoFrame, false);
        a.recycle();

        if(playlistResourceId != -1){
            mTrack = 1;
            mPlaylist = getResources().getStringArray(playlistResourceId);
            Log.i(TAG, mPlaylist[mTrack - 1]);
            mVideoFileName = mPlaylist[mTrack-1];
        }

        // Surface
        mTV = new TextureView(mContext);
        LayoutParams tvLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mTV.setLayoutParams(tvLayoutParams);
        mTV.setSurfaceTextureListener(this);
        addView(mTV);

        // ImageView : background image
        if (mIsShowVideoFrame) {
            mImgView_VideoFrame = new ImageView(mContext);
            addView(mImgView_VideoFrame);
        }

        if( mEnv != null && mEnv.getBooleanValue(Environments.SHOW_VIDEO_TIMER)){
            mVideoTimer = new VideoTimerView(mContext);
            LayoutParams videoTimerLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            videoTimerLayoutParams.setMargins(0, 120, 0, 0);
            mVideoTimer.setLayoutParams(videoTimerLayoutParams);
            addView(mVideoTimer);
        }

        mRunnableVideoBreak = new Runnable() {
            @Override
            public void run() {
                if (mVideoBreakPointListener != null) {
                    ExtendedChapter chapter = mChapters.get(mNextChapter);
                    Chapter.ActionInfo action = (chapter == null ? null : chapter.getAction());

                    mVideoBreakPointListener.onVideoBreakPoint(
                            VideoTextureView.this, mNextChapter, action);
                    if (ENABLE_ENDCHAPTER) {
                        if (chapter != null) {
                            chapter.startedChapter();
                        }
                        updateChapterEndedRunnable();
                    }
                }
                mNextChapter++;
                if (mChapters.size() > mNextChapter)
                    setChapterListener(mNextChapter);
            }
        };

        if (ENABLE_ENDCHAPTER) {
            mRunnableChapterEnded = new Runnable() {
                @Override
                public void run() {
                    notifyChapterEnded();
                }
            };
        }

        // load chapter
        loadChapterFile();

        // load subtitle
        loadSubTitleFile();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        this.mSurface = surface;
        Log.i(TAG, "Surface Available, " + getContext());

        mSurfaceReady = true;
        try {
            preparePlayer();
        } catch (Exception e) {
            Log.e(TAG, "problem preparing video player", e);
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.i(TAG, "onSurfaceTextureDestroyed");
        mSurface.release();
        mSurface = null;
        mSurfaceReady = false;

        // important - don't remove
        this.release();

        return true;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.d(TAG, "onSurfaceTextureSizeChanged - " + width + "*" + height);
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        //
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        Log.d(TAG, "onDetachedFromWindow");
    }

    public void setLooped(boolean looped) {
        this.mShouldLoop = looped;
    }

    public boolean isLooped(){
        return mShouldLoop;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.mIsAutoPlay = autoPlay;
    }

    public boolean isAutoPlay() {
        return mIsAutoPlay;
    }

    public void setScaleToVideoSize(boolean scaleToVideoSize) {
        this.mScaleToVideoSize = scaleToVideoSize;
    }

    public void setShowVideoFrame(boolean bSet) {
        this.mIsShowVideoFrame = bSet;
    }

    public void setVolume(float lower, float upper) {
        if (mPlayer != null) {
            mPlayer.setVolume(lower, upper);
        }
    }

    public MediaPlayer getPlayer() {
        if (mPlayer != null)
            return mPlayer;

        Log.e(TAG, "Player is null");
        return null;
    }

    public void setVideoFile(String fileName) {
        this.mVideoFileName = fileName;
    }

    public String getVideoFile() {
        return mVideoFileName;
    }

    public void setVideoFrameFile(String filename) {
        mVideoFrameFileName = filename;
    }

    public void showVideoFrame(){
        if(mImgView_VideoFrame != null){
            mImgView_VideoFrame.setVisibility(View.VISIBLE);
        }
    }

    public void hideVideoFrame(){
        if(mImgView_VideoFrame != null){
            mImgView_VideoFrame.setVisibility(View.INVISIBLE);
        }
    }

    public void setChapterFile(String filename) {
        mChapterFileName = filename;
        loadChapterFile();

        mHandler.removeCallbacks(mRunnableVideoBreak);
        if (ENABLE_ENDCHAPTER) {
            mHandler.removeCallbacks(mRunnableChapterEnded);
        }

        mChapters = new ArrayList<ExtendedChapter>();
        mNextChapter = 0;
    }

    public void setSubTitleFile(String filename) {
        mSubTitleFileName = filename;
        loadSubTitleFile();
    }

    public int getNextChapter() {
        return mNextChapter;

    }

    public void setTimedTextSource(Uri uri, String mimeType)
            throws IllegalArgumentException, IllegalStateException, IOException {
        mSubtitleUri = uri;
        mSubtitleMimeType = mimeType;
    }

    // ----- listeners -----

    public void setVideoTextureViewListener(VideoTextureViewListener listener) {
        this.mListener = listener;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        this.mOnCompletionListener = listener;
    }

    public void setOnTimedTextListener(MediaPlayer.OnTimedTextListener listener) {
        if (mPlayer != null) {
            mPlayer.setOnTimedTextListener(listener);
        }
    }

    public void setVideoBreakpointListener(
            VideoBreakPointListener videoBreakPointListener) {
        this.mVideoBreakPointListener = videoBreakPointListener;
    }

    // ----- playback -----

    public void play() throws IllegalArgumentException, SecurityException,
            IllegalStateException, IOException {

        mShouldPlay = true;

        if (mIsPrepared && !mIsPaused) {
            if (mIsShowVideoFrame && mImgView_VideoFrame != null) {
                mImgView_VideoFrame.postOnAnimationDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!mIsPrepared || mIsPaused) {
                            return;
                        }

                        if (mPlayer != null) {
                            if (mPlayer.isPlaying() && mPlayer.getCurrentPosition() > 200) { // 70 -> 150 -> 200
                                mImgView_VideoFrame.setVisibility(View.INVISIBLE);
                            } else {
                                mImgView_VideoFrame.postOnAnimation(this);
                            }
                        }
                    }
                }, 100); //150 -> 50 -> 100
            }

            if (mPlayer != null) mPlayer.start();
            if (mVideoTimer != null) mVideoTimer.start();
            Log.i(TAG, "Playing Player");

            setChapterListener(mNextChapter);
        } else if (mPlayer == null && mSurface != null) {
            preparePlayer();
            Log.i(TAG, "Preparing player");
        }
    }

    public void stop(){
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                mPlayer.seekTo(0);
                if (mVideoTimer != null) mVideoTimer.stop();
            }
        }
    }

    public void pause() {
        if (mPlayer != null) {
            if (mIsPrepared) {
                mPlayer.pause();
                if (mVideoTimer != null) mVideoTimer.pause();
                removeVideoBreakpoint();
                if (ENABLE_ENDCHAPTER) {
                    removeChapterEndedRunnable();
                }
            } else {
                mShouldPlay = false;
            }
        }
    }

    public int getTrackID(){
        return mTrack;
    }

    public int getTrackSize(){
        return mPlaylist.length;
    }

    public void setTrack(int track, boolean show){
        if(mPlaylist != null && mPlaylist[track-1] != null){
            this.mTrack = track;
            mVideoFrameFileName = mPlaylist[mTrack - 1];
            Log.v(TAG, "Track is : " + mVideoFileName);

            if (mIsShowVideoFrame) {
                loadBackgroundImage(show);
            }
        }
    }

    public void setVisibleVideoThumbnail(boolean visible) {
        if (mIsShowVideoFrame) {
            loadBackgroundImage(true);
        }
    }

    public void preparePlayer() throws IllegalArgumentException,
            SecurityException, IllegalStateException, IOException {

        if (mSurface == null) {
            Log.e(TAG, "Surface is null");
            return;
        }

        if (mPlayer != null) {
            release();
        }

        if (hasVideoResourceFile(mVideoFileName) == false) {
            return;
        }

        // calculated resolution for layout
        calLayoutWidthHeight();

        // display the background image (as thumbnail)
        if (mIsShowVideoFrame) {
            loadBackgroundImage(true);
        }

        //=================
        mNextChapter = 0;
        //=================

        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(this);
        mPlayer.setDataSource(mResourceUtil.getContentFilePath(mVideoFileName));
        Log.i(TAG, "Loading video, filename is: " + mVideoFileName);

        if (mSubtitleUri != null && mSubtitleMimeType != null) {

            Log.i(TAG, "preparePlayer - add subtitle file");

            // we have subtitles; select our subtitle track
            mPlayer.addTimedTextSource(mContext, mSubtitleUri, mSubtitleMimeType);
            int trackIndex = findTrackIndexFor(
                    MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT, mPlayer.getTrackInfo());

            if (trackIndex >= 0) {
                mPlayer.selectTrack(trackIndex);
            }
        }

        mPlayer.setSurface(new Surface(mSurface));
        mPlayer.setOnPreparedListener(this);
        mPlayer.prepareAsync();
        mIsPrepared = false;
    }

    public void resume() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
        if (mPlayer != null) {
            if (mIsPrepared) {
                play();
            }
            else {
                mShouldPlay = true;
            }
        }
    }

    public void release() {
        if (mPlayer != null) {
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;

            if (mVideoTimer != null) {
                mVideoTimer.stop();
            }

            removeVideoBreakpoint();
            if (ENABLE_ENDCHAPTER) {
                removeChapterEndedRunnable();
            }
            mIsPrepared = false;
            mShouldPlay = false;

            if (mImgView_VideoFrame != null) {
                mImgView_VideoFrame.setImageBitmap(null);
                if (mBgBitmap != null && !mBgBitmap.isRecycled()) {
                    mBgBitmap.recycle();
                    mBgBitmap = null;
                    Log.d(TAG, "Bitmap recycle");
                }
            }
        }
    }

    public boolean isSurfaceReady() {
        return mSurfaceReady;
    }

    public boolean isPrepared(){
        return mIsPrepared;
    }

    public boolean getShouldPlay(){
        return mShouldPlay;
    }

    public boolean isPlaying() {
        boolean playing = false;
        if (mPlayer != null) {
            playing = mPlayer.isPlaying();
        }
        return playing;
    }

    //[[ START : It fits on the screen
    private void calLayoutWidthHeight() {
        if (mVideoFileName != null) {
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(mResourceUtil.getContentFilePath(mVideoFileName));

            // It fits on the screen
            final float videoWidth = Float.parseFloat(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            final float videoHeight = Float.parseFloat(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

            Log.d(TAG, "Video Size [" + videoWidth + "x" + videoHeight + "]");

            if (mScaleToVideoSize) {
                mLayout_width = ViewGroup.LayoutParams.MATCH_PARENT;
                mLayout_height = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                float videoAspectRatio = videoWidth / videoHeight;

                final int tv_width = this.getWidth();
                final int tv_height = this.getHeight();
                float tvAspectRatio = (float) tv_width / (float) tv_height;
                //Log.d(TAG, "View Size [" + tv_width + "x" + tv_height + "]");

                if (videoAspectRatio > tvAspectRatio) {
                    mLayout_width = tv_width;
                    mLayout_height = (int) ((float) tv_width / videoAspectRatio);
                } else {
                    mLayout_width = (int) (videoAspectRatio * (float) tv_height);
                    mLayout_height = tv_height;
                }
            }

            Log.d(TAG, "TV Size [" + mLayout_width + "x" + mLayout_height + "]");
        }
    }
    //]] END

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.i(TAG, "onPrepared()");

        mIsPrepared = true;

        //[[ START : update the video timer
        if (mVideoTimer != null) {
            mVideoTimer.setRegisterMediaPlayer(mp);
        }
        //]] END

        final int video_width = mp.getVideoWidth();
        final int video_height = mp.getVideoHeight();

        // It fits on the screen
        ViewGroup.LayoutParams lp = mTV.getLayoutParams();
        lp.width = mLayout_width;
        lp.height = mLayout_height;
        mTV.setLayoutParams(lp);

        if (mListener != null) {
            mListener.onVideoPrepared(this, video_width, video_height);
        }

        if (mShouldPlay || mIsAutoPlay) {
            try {
                play();
            } catch (Exception e) {
                Log.e(TAG, e + "problem playing video");
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mVideoTimer != null) {
            mVideoTimer.stop();
            mVideoTimer.makeFinalTime();
        }

        if(mOnCompletionListener != null){
            mOnCompletionListener.onCompletion(mp);
        }

        if(mShouldLoop && mIsPrepared && !mIsPaused){

            //method changed to overcome strange non-looping behavior
            mShouldPlay = true;

            try {
                preparePlayer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Removes the runnable for our breakpoint from the message queue
     */
    private void removeVideoBreakpoint() {
        Log.i("none", "Callback Removed");
        mHandler.removeCallbacks(mRunnableVideoBreak);
    }

    private void removeChapterEndedRunnable() {
        mHandler.removeCallbacks(mRunnableChapterEnded);
    }

    /**
     * Finds the index for the track info that matches the given track type
     *
     * @param mediaTrackType
     * @param trackInfo
     * @return
     */
    private int findTrackIndexFor(int mediaTrackType, MediaPlayer.TrackInfo[] trackInfo) {
        int index = -1;
        for (int i = 0; i < trackInfo.length; i++) {
            if (trackInfo[i].getTrackType() == mediaTrackType) {
                return i;
            }
        }
        return index;
    }

    public Chapter getChapter(int index) {
        return mChapters.get(index);
    }

    public Chapter getCurrentChapter() {
        Chapter ret = null;

        final Integer i = getCurrentChapterIndex();
        if (i != null && i >= 0) {
            ret = mChapters.get(i);
        }

        return ret;
    }

    public Integer getCurrentChapterIndex() {
        if (mPlayer != null || mChapters != null) {
            int pos = mPlayer.getCurrentPosition();
            for (int i = 0; i < mChapters.size(); i++) {
                if (mChapters.get(i).getChapterStart() == pos) {
                    return i;
                } else if (mChapters.get(i).getChapterStart() < pos) {
                    if ((i + 1) >= mChapters.size()) {
                        return i;
                    }
                } else {
                    return i - 1;
                }
            }

            // another the search method
            //int mid = mChapters.size() / 2;
            //return getChapIndexSearch(mChapters, mid, pos);
        }
        return null;
    }

    private Integer getChapIndexSearch(List<Chapter> list, int index, int pos) {
        if (list != null) {
            if (list.get(index).getChapterStart() == pos) {
                return index;
            } else if (list.get(index).getChapterStart() < pos) {
                if ((index + 1) >= list.size()) {
                    return index;
                }

                if (list.get(index + 1).getChapterStart() < pos) {
                    return index;
                } else {
                    return getChapIndexSearch(list, index + 1, pos);
                }

            } else {
                if ((index-1) < 0) {
                    return index - 1;
                }

                if (list.get(index - 1).getChapterStart() <= pos) {
                    return index -1;
                } else {
                    return getChapIndexSearch(list, index - 1 , pos);
                }
            }
        } else {
            return null;
        }
    }

    public int getChapterStart(int chapterIndex) {
        return mChapters.get(chapterIndex).getChapterStart();
    }

    public void seekToChapter(int chapterIndex) {
        seekToChapter(mChapters.get(chapterIndex));
    }

    public void seekToChapter(Chapter chapter) {
        if (mPlayer != null) {
            mPlayer.seekTo(chapter.getChapterStart());
            mHandler.removeCallbacks(mRunnableVideoBreak);
            // TODO j.k make sure chapter ended is called.
            int i = 0;
            for (; i < mChapters.size(); i++) {
                if (mChapters.get(i) == chapter)
                    break;
            }
            mNextChapter = i;
            setChapterListener(mNextChapter);
        }
    }

    public void seekToMS(int ms) {
        if (mPlayer != null && mIsPrepared) {
            mPlayer.seekTo(ms);
        }
    }

    @Override
    public void onChaptersParsed(List<ExtendedChapter> chapters) {
        this.mChapters = chapters;
        if (mListener != null) {
            mListener.onChaptersLoaded(mChapters);
        }
        setChapterListener(mNextChapter);
    }

    public void setChapterListener(int chapterIndex) {
        if (!isPlaying() || mChapters == null || mChapters.isEmpty()) {
            //Log.e(TAG, "setChapterListener - return");
            return;
        }

        mHandler.removeCallbacks(mRunnableVideoBreak);

        int currentTime = 0;
        int nextChapterStart = 0;
        int msDelay = 0;
        Chapter chapter;

        if (mPlayer != null) {
            currentTime = mPlayer.getCurrentPosition();
        }

        chapter = mChapters.get(chapterIndex);
        nextChapterStart = chapter.getChapterStart();
        msDelay = nextChapterStart - currentTime;
        Log.i(TAG, "Delay is " + msDelay);
        mHandler.postDelayed(mRunnableVideoBreak, msDelay);
    }

    private void notifyChapterEnded() {
        if (!isPlaying() || mChapters == null || mChapters.isEmpty()) {
            return;
        }

        mHandler.removeCallbacks(mRunnableChapterEnded);

        int currentTime = 0;
        int msDelay = Integer.MAX_VALUE;

        if (mPlayer != null) {
            currentTime = mPlayer.getCurrentPosition();
        }

        int count = mChapters.size();
        for (int i = 0; i < count; i++) {
            ExtendedChapter chapter = mChapters.get(i);
            if (chapter != null && chapter.isStarted() && !chapter.isEnded()) {
                Chapter.ActionInfo actionInfo = chapter.getAction();

                // Issue: user can go back to old Chapter and chapter.ended is not called
                // Hack: Call ended() if start time is none future
                int chapterStart = chapter.getChapterStart();
                if (chapterStart > currentTime) {
                    if (mVideoBreakPointListener != null) {
                        mVideoBreakPointListener.onChapterEnded(VideoTextureView.this, i, actionInfo);
                        chapter.setIsEnded(true);
                    }
                } else {
                    int chapterEnd = chapter.getChapterEnd();
                    int delay = chapterEnd - currentTime;

                    if (delay <= 0) {
                        if (mVideoBreakPointListener != null) {
                            mVideoBreakPointListener.onChapterEnded(VideoTextureView.this, i, actionInfo);
                            chapter.setIsEnded(true);
                        }
                    } else if (msDelay > delay) {
                        msDelay = delay;
                    }
                }
            }
        }

        if (msDelay != Integer.MAX_VALUE) {
            Log.i(TAG, "Delay is " + msDelay);
            mHandler.postDelayed(mRunnableChapterEnded, msDelay);
        }
    }

    private void updateChapterEndedRunnable() {
        // notifyChapterEnded() will update it.
        notifyChapterEnded();
    }


    /**
     * Loads the background (same the thumbnail)
     */
    private void loadBackgroundImage(boolean show){
        if(hasVideoResourceFile(mVideoFrameFileName) == true && mImgView_VideoFrame != null) {
            if (!show) {
                mImgView_VideoFrame.setVisibility(View.INVISIBLE);
            }
            else {
                File file = new File(mResourceUtil.getContentFilePath(mVideoFrameFileName));
                Date lastFileDate = new Date(file.lastModified());

                if (!mLastFrameFileDate.equals(lastFileDate)) {
                    // save the modify date of frame file
                    mLastFrameFileDate = lastFileDate;

                    mBgBitmap = BitmapFactory.decodeFile(mResourceUtil.getContentFilePath(mVideoFrameFileName));

                    if (mBgBitmap != null) {
                        Log.i(TAG, "The video frame file loaded : " + mVideoFrameFileName);

                        int width, height;
                        if (mScaleToVideoSize) {
                            width = ViewGroup.LayoutParams.MATCH_PARENT;
                            height = ViewGroup.LayoutParams.MATCH_PARENT;
                            mImgView_VideoFrame.setScaleType(ImageView.ScaleType.FIT_XY);
                        } else {
                            width = (int) (mLayout_width * mSizeMultiplier);
                            height = (int) (mLayout_height * mSizeMultiplier);
                            mImgView_VideoFrame.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            Log.d(TAG, "loadBackgroundImage [" + width + "x" + height + "]");
                        }


                        mImgView_VideoFrame.setLayoutParams(new LayoutParams(width, height, Gravity.CENTER));
                        mImgView_VideoFrame.setImageBitmap(mBgBitmap);

                    }
                }
                mImgView_VideoFrame.setVisibility(View.VISIBLE);
            }
        }
    }

    public int getTotalChapters()
    {
        return mChapters.size();
    }

    public boolean hasChapters()
    {
        return mHasChapters;
    }

    private void loadChapterFile() {
        if (hasVideoResourceFile(mChapterFileName) == false) {
            if (mChapters == null) {
                mChapters = new ArrayList<ExtendedChapter>();
            }
            mHasChapters = false;
        } else {
            mChapterParseTask.setListener(this).execute(mResourceUtil.getContentFilePath(mChapterFileName));
            mHasChapters = true;
        }
    }

    private void loadSubTitleFile() {
        if (hasVideoResourceFile(mSubTitleFileName) == true) {
            mSubtitleMimeType = MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP;
            mSubtitleUri = Uri.parse(mResourceUtil.getContentFilePath(mSubTitleFileName));
        }
    }

    public boolean hasVideoResourceFile(String filename) {
        if (filename == null) {
            return false;
        }

        if (mResourceUtil.isMissingContentFile(filename)) {
            return false;
        }

        return true;
    }


    public ResourceUtil getmResourceUtil() {
        return mResourceUtil;
    }
}

