package com.samsung.retailexperience.retailhero.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.samsung.retailexperience.retailhero.R;

/**
 * Created by jaekim on 2/5/16.
 */
public class FlingAnimationHorizontal  extends FlingAnimation {

    private static final int NUM_OF_ICONS = 3;
    private View[] mChildViews = new View[NUM_OF_ICONS];
    private static int[] DURATION = new int[NUM_OF_ICONS];
    static {
        DURATION[0] = ANIMATION_DURATION;
        DURATION[1] = ANIMATION_DURATION;
        DURATION[2] = ANIMATION_DURATION + ANIMATION_DURATION_EXTENTION;
    }

    public FlingAnimationHorizontal(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FlingAnimationHorizontal(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlingAnimationHorizontal(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mChildViews[0] = findViewById(R.id.horizontal_fling_one);
        mChildViews[1] = findViewById(R.id.horizontal_fling_two);
        mChildViews[2] = findViewById(R.id.horizontal_fling_three);

        mIsViewPopulated = true;
    }

    @Override
    int getNumIcons() {
        return NUM_OF_ICONS;
    }

    @Override
    View getFlingIconView(int index) {
        return mChildViews[index];
    }

    @Override
    int getFlingIconDuration(int index) {
        return DURATION[index];
    }

    @Override
    void hideAllIcons() {
        for (View child : mChildViews) {
            child.setVisibility(View.INVISIBLE);
        }
    }
}