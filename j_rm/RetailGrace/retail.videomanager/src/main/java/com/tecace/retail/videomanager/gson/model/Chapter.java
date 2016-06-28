package com.tecace.retail.videomanager.gson.model;

import java.io.Serializable;

/**
 * Created by smheo on 9/29/2015.
 */
public class Chapter {
    // TODO members should be private.  however, gson doesn't work.
    private final int chapterStart;
    private final int chapterEnd;

    private final String chapterAction;
    private final String chapterActionMessage;
    private final InteractionOverlay interactionOverlay;

    private boolean mIsStarted;
    private boolean mIsEnded;

    public Chapter(int chapterStart,
                   int chapterEnd,
                   String chapterAction,
                   String chapterActionMessage,
                   InteractionOverlay interactionOverlay) {
        this.chapterStart = chapterStart;
        this.chapterEnd = chapterEnd;

        this.chapterAction = chapterAction;
        this.chapterActionMessage = chapterActionMessage;
        this.interactionOverlay = interactionOverlay;
    }

    public void startedChapter() {
        mIsStarted = true;
        mIsEnded = false;
    }

    public void reset() {
        mIsStarted = false;
        mIsEnded = false;
    }

    public int getChapterStart() {
        return this.chapterStart;
    }

    public int getChapterEnd() {
        return this.chapterEnd;
    }


    public ChapterAction getAction() {
        return ChapterAction.valueOf(chapterAction);
    }

    public String getActionMessage() {
        return chapterActionMessage;
    }

    public InteractionOverlay getInteractionOverlay() {
        return interactionOverlay;
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
        return String.format("start: %d, end: %d, started: %b, ended: %b, action: %s, actionMessage: %s, overlay: %s",
                chapterStart, chapterEnd, mIsStarted, mIsEnded, chapterAction, chapterActionMessage, interactionOverlay);
    }

    public enum ChapterAction {
        Super(false), Disclaimer(false), Custom(false), Tap(true),
        Swipe_Left(true), Swipe_Up(true), Swipe_Right(true), Swipe_Down(true), Unknown(false);

        public final boolean isUserInteractionable;
        ChapterAction(boolean isUserInteractionable) {
            this.isUserInteractionable = isUserInteractionable;
        }
    }
}