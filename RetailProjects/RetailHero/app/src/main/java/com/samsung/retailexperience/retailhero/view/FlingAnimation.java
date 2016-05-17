package com.samsung.retailexperience.retailhero.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.samsung.retailexperience.retailhero.util.TimerHandler;
import com.samsung.retailexperience.retailhero.R;

/**
 * Created by jaekim on 2/5/16.
 */
public abstract class FlingAnimation extends LinearLayout
        implements TimerHandler.OnTimeoutListener {

    protected static int ANIMATION_DURATION = 400;
    // when animation is done, stay as it for 200 ms
    protected static int ANIMATION_DURATION_EXTENTION = 300;

    private int mIconIndex;
    private TimerHandler mTimerHandler;

    // TODO replace with native method.
    protected boolean mIsViewPopulated = false;

    public FlingAnimation(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FlingAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlingAnimation(Context context) {
        super(context);
    }

    abstract int getNumIcons();
    abstract View getFlingIconView(int index);
    abstract int getFlingIconDuration(int index);
    abstract void hideAllIcons();

    // need?
    public void destroyAnimation() {
        stopAnimation();

        mTimerHandler = null;
    }

    // if we have timer, use existing one.  Otherwise, create a new one.
    public void initializeAnimation() {
        if (mTimerHandler != null) {
            stopAnimation();
        }
        mTimerHandler = new TimerHandler();
        mTimerHandler.setOnTimeoutListener(this);
    }

    private void stopAnimation() {
        if (mTimerHandler != null) {
            mTimerHandler.stop();
        }
    }

    private void resetAnimation() {
        if (mTimerHandler != null) {
            mTimerHandler.stop();
        } else {
            initializeAnimation();
        }

        mIconIndex = 0;     // reset index
        animateFlingIcons();
    }

    private void animateFlingIcons() {
        int numIcons = getNumIcons();
        int iconIndex = mIconIndex % numIcons;
        if (iconIndex == 0) {
            hideAllIcons();
        }

        // retrun a view in reverse order
        int reverseIndex = numIcons - 1 - iconIndex;
        View icon = getFlingIconView(reverseIndex);
        icon.setVisibility(View.VISIBLE);

        int duration = getFlingIconDuration(iconIndex);
        mTimerHandler.start(duration);

        mIconIndex++;
    }

    @Override
    public void onTimeout() {
        animateFlingIcons();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        // it is no longer visible.  need onAttachedToWindow? test it.
        if (mTimerHandler != null) {
            mTimerHandler.stop();
            mTimerHandler = null;
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (!mIsViewPopulated) {
            return;
        }

        if (visibility != View.VISIBLE) {
            stopAnimation();
        }

        switch (changedView.getId()) {
            case R.id.horizontal_fling_anim_container:
            case R.id.vertical_fling_anim_container:
                // if it is container, enable or disable animate
                if (visibility == View.VISIBLE) {
                    resetAnimation();
                }
                break;
        }
    }

}

