package com.samsung.retailexperience.retailecosystem.video.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samsung.retailexperience.retailecosystem.video.util.RefreshHandler;

/**
 * Created by smheo on 9/29/2015.
 */
public class VideoTimerView extends TextView implements RefreshHandler.OnRefreshListener {

    RefreshHandler timer = new RefreshHandler();
    long startTime = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    //[[ START : update the video timer
    private MediaPlayer mPlayer;
    private int mMediaGetDuration = 0;

    public void setRegisterMediaPlayer(MediaPlayer player) {
        if (player != null) {
            this.mPlayer = player;
            mMediaGetDuration = mPlayer.getDuration();
        }
    }
    //]] END

    public VideoTimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public VideoTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideoTimerView(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        timer.setOnRefreshListener(this);
        timer.setDelay(100);
        setTextColor(Color.RED);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setBackgroundColor(Color.WHITE);
    }

    public void start() {
        if (timer != null) {
            timer.start();
            startTime = SystemClock.uptimeMillis();
        }
    }
    public void stop(){
        if (timer != null){
            timer.stop();
        }
    }
    public void pause(){
        timeSwapBuff = updatedTime;
        timer.stop();
    }

    public void makeFinalTime() {
        int totalTime = mMediaGetDuration;
        int milliseconds = totalTime % 1000;
        int secs = totalTime / 1000;
        secs = secs % 60;
        int mins = secs / 60;
        mins = mins % 60;
        int hours = secs / 3600;
        secs = secs % 60;

        setText(String.format("Time: %02d:%02d:%02d,%03d\nMS: %d", hours, mins, secs, milliseconds, totalTime));
    }

    @Override
    public void onRefresh() {
        // update the video timer
        if (mPlayer != null) {
            int totalTime = mMediaGetDuration;
            int milliseconds = totalTime % 1000;
            int secs = totalTime / 1000;
            secs = secs % 60;
            int mins = secs / 60;
            mins = mins % 60;
            int hours = secs / 3600;
            secs = secs % 60;

            setText(String.format("Time: %02d:%02d:%02d,%03d\nMS: %d", hours, mins, secs, milliseconds, mPlayer.getCurrentPosition()));
        }
    }
}
