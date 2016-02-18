package com.samsung.retailexperience.retailhero.gson.models;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by icanmobile on 1/12/16.
 */
public class VideoModel extends ResourceModel implements Serializable {
    private String title = null;
    private String videoFile = null;
    private String frameFile = null;
    private String chapterFile = null;
    private String subTitleFile = null;
    private String action = null;

    public VideoModel() {
        this(null, null, null, null, null, null);
    }
    public VideoModel(String title,
                      String videoFile,
                      String frameFile,
                      String chapterFile,
                      String subTitleFile,
                      String action) {
        this.title      = title;
        this.videoFile  = videoFile;
        this.frameFile = frameFile;
        this.chapterFile = chapterFile;
        this.subTitleFile = subTitleFile;
        this.action     = action;
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

    public String getSubTitleFile() {
        return this.subTitleFile;
    }
    public void setSubTitleFile(String subTitleFile) {
        this.subTitleFile = subTitleFile;
    }

    public String getAction() {
        return this.action;
    }
    public void setAction(String action) {
        this.action = action;
    }

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

        return builder.toString();
    }

    private void appendString(StringBuilder builder, String value) {
        builder.append(value + System.getProperty("line.separator"));
    }

    public void print() {
        Log.d(VideoModel.class.getName(), toString());
    }
}