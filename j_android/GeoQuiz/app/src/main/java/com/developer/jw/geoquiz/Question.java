package com.developer.jw.geoquiz;

/**
 * Created by JW on 2016-04-12.
 */
public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mCheated;

    public Question(int textResId, boolean answerTrue, boolean cheated){
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mCheated = cheated;
    }

    public boolean isCheated() {
        return mCheated;
    }

    public void setCheated(boolean cheated) {
        mCheated = cheated;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
