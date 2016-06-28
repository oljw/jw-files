package com.samsung.retailexperience.retailtmo.video.gson.models;

/**
 * Created by jaekim on 3/4/16.
 */
public class ExtendedChapter extends Chapter {
    private boolean mIsStarted;
    private boolean mIsEnded;

    public ExtendedChapter(int chapterStart, int chapterEnd, String chapterAction, String chapterActionExtra) {
        super(chapterStart, chapterEnd, chapterAction, chapterActionExtra);
    }

    public void startedChapter() {
        mIsStarted = true;
        mIsEnded = false;
    }

    public void setIsEnded(boolean isEnded) {
        mIsEnded = isEnded;
    }

    public boolean isStarted() {
        return mIsStarted;
    }

    public boolean isEnded() {
        return mIsEnded;
    }

    @Override
    public String toString() {
        return String.format("%s, started: %b, ended: %b",
                super.toString(), mIsStarted, mIsEnded);
    }
}
