package com.samsung.retailexperience.retailecosystem.video.gson.models;

import android.util.Log;

/**
 * Created by smheo on 9/29/2015.
 */
public class Chapter {
    private static final String TAG = Chapter.class.getSimpleName();

    private final int chapterStart;
    private final int chapterEnd;
    private final String chapterAction;
    private final String chapterActionExtra;
    private ActionInfo actionInfo;

    public Chapter(int chapterStart, int chapterEnd, String chapterAction, String chapterActionExtra) {
        this.chapterStart = chapterStart;
        this.chapterEnd = chapterEnd;
        this.chapterAction = chapterAction;
        this.chapterActionExtra = chapterActionExtra;
    }

    public void init() {
        this.actionInfo = new ActionInfo(chapterAction, chapterActionExtra);
    }

    public int getChapterStart() {
        return this.chapterStart;
    }

    public int getChapterEnd() {
        return this.chapterEnd;
    }

    public ActionInfo getAction() {
        return actionInfo;
    }

    @Override
    public String toString() {
        return String.format("start: %d, end: %d, action:%s",
                getChapterStart(), getChapterEnd(), getAction());
    }

    public enum ACTION {
        Super, Disclaimer, Unknown
    }

    public static class ActionInfo {
        ACTION action;
        final String extra;

        public ActionInfo(String action, String extra) {
            this.extra = extra;
            try {
                this.action = ACTION.valueOf(action);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "FIXIT. Unknown action type: " + action);
                this.action = ACTION.Unknown;
            }
        }

        public ACTION getAction() {
            return action;
        }

        public String getExtra() {
            return extra;
        }

        @Override
        public String toString() {
            return String.format("Action:%s, extra:%s", action.toString(), extra);
        }
    }
}

