package com.tecace.app.manager.gson.model;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by icanmobile on 3/1/16.
 */
public class VideoModel extends BaseModel implements Serializable {
    private String title = null;
    private String orientation = null;
    private String videoFile = null;
    private String frameFile = null;
    private String chapterFile = null;
    private String chapterLayout = null;
    private String subTitleFile = null;
    private String looped = null;
    private String autoPlay = null;
    private String action = null;
    private String actionClick = null;

    public VideoModel() {
        this(null, null, null, null, null, null, null, null, null, null, null);
    }
    public VideoModel(String title,
                      String orientation,
                      String videoFile,
                      String frameFile,
                      String chapterFile,
                      String chapterLayout,
                      String subTitleFile,
                      String looped,
                      String autoPlay,
                      String action,
                      String actionClick) {
        this.title          = title;
        this.orientation    = orientation;
        this.videoFile      = videoFile;
        this.frameFile      = frameFile;
        this.chapterFile    = chapterFile;
        this.chapterLayout  = chapterLayout;
        this.subTitleFile   = subTitleFile;
        this.looped         = looped;
        this.autoPlay       = autoPlay;
        this.action         = action;
        this.actionClick    = actionClick;
    }

    public String getTitle() {
        return this.title;
    }
    public int getTitleResId() {
        if (this.title != null)
            return getResId(this.title);
        return 0;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrientation() {
        return this.orientation;
    }
    public int getOrientationResId() {
        if (this.orientation != null)
            return getResId(this.orientation);
        return 0;
    }
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getVideoFile() {
        return this.videoFile;
    }
    public int getVideoFileResId() {
        if (this.videoFile != null)
            return getResId(this.videoFile);
        return 0;
    }
    public void setVideoFile(String videoFile) {
        this.videoFile = videoFile;
    }

    public String getFrameFile() {
        return this.frameFile;
    }
    public void setFrameFile(String frameFile) {
        this.frameFile = frameFile;
    }

    public String getChapterFile() {
        return this.chapterFile;
    }
    public void setChapterFile(String chapterFile) {
        this.chapterFile = chapterFile;
    }

    public String getChapterLayout() {
        return this.chapterLayout;
    }
    public int getChapterLayoutResId() {
        if (this.chapterLayout != null)
            return getResId(this.chapterLayout);
        return 0;
    }
    public void setChapterLayout(int chapterLayout) {
        this.chapterLayout = String.valueOf(chapterLayout);
    }

    public String getSubTitleFile() {
        return this.subTitleFile;
    }
    public void setSubTitleFile(String subTitleFile) {
        this.subTitleFile = subTitleFile;
    }


    public boolean getLooped() {
        if (this.looped == null) return false;
        return Boolean.valueOf(this.looped);
    }
    public void setLooped(Boolean looped) {
        this.looped = Boolean.toString(looped);
    }


    public boolean getAutoPlay() {
        if (this.autoPlay == null) return false;
        return Boolean.valueOf(this.autoPlay);
    }
    public void setAutoPlay(Boolean autoPlay) {
        this.autoPlay = Boolean.toString(autoPlay);
    }


    public String getAction() {
        return this.action;
    }
    public void setAction(String action) {
        this.action = action;
    }

    public String getActionClick() {
        return this.actionClick;
    }
    public void setActionClick(String actionClick) {
        this.actionClick = actionClick;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.title != null)
            appendString(builder, "title = " + this.title);
        if (this.videoFile != null)
            appendString(builder, "videoFile = " + this.videoFile);
        if (this.frameFile != null)
            appendString(builder, "frameFile = " + this.frameFile);
        if (this.chapterFile != null)
            appendString(builder, "chapterFile = " + this.chapterFile);
        if (this.subTitleFile != null)
            appendString(builder, "subTitleFile = " + this.subTitleFile);
        if (this.action != null)
            appendString(builder, "action = " + this.action);
        if (this.actionClick != null)
            appendString(builder, "actionClick = " + this.actionClick);

        return builder.toString();
    }

    @Override
    public void print() {
        Log.d(VideoModel.class.getName(), toString());
    }
}