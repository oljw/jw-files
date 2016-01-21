package com.samsung.retailexperience.retailhero.gson.models;

/**
 * Created by smheo on 9/29/2015.
 */
public class Chapter {
    private final int chapterStart;
    private final int chapterEnd;

    public Chapter(int chapterStart, int chapterEnd) {
        this.chapterStart = chapterStart;
        this.chapterEnd = chapterEnd;
    }

    public int getChapterStart() {
        return this.chapterStart;
    }

    public int getChapterEnd() {
        return this.chapterEnd;
    }
}
