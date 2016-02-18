package com.samsung.retailexperience.retailhero.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.samsung.retailexperience.retailhero.RetailHeroApplication;

/**
 * Created by icanmobile on 1/19/16.
 */
public abstract class OverlayView extends RelativeLayout {
    private static final String TAG = OverlayView.class.getSimpleName();

    protected WindowManager.LayoutParams mLayoutParams = null;

    protected Context mContext = null;
    private int mLayoutResId = 0;
    private GestureDetector mGestureDetector = null;
    private boolean mIsMoving = false;

    public OverlayView(Context context, int layoutResId, boolean isMoving) {
        super(context);
        mContext = context;
        mLayoutResId = layoutResId;
        mGestureDetector = new GestureDetector(mContext, new GestureListener());
        mIsMoving = isMoving;
    }

    private void inflateView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(mLayoutResId, this);
        onInflateView();
    }

    private void setupLayoutParams() {
        mLayoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        mLayoutParams.gravity = getLayoutGravity();

        onSetupLayoutParams(mLayoutParams);
    }

    public int getLayoutGravity() {
        if (onLayoutGravity() != -1)
            return onLayoutGravity();
        return Gravity.CENTER;
    }

    public boolean isVisible() {
        // Override this method to control when the Overlay is visible without
        // destroying it.
        return true;
    }

    public void refreshLayout() {
        // Call this to force the updating of the view's layout.
        if (isVisible()) {
            removeAllViews();
            inflateView();
            onSetupLayoutParams(mLayoutParams);
            ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(this, mLayoutParams);
            refresh();
        }
    }

    protected void addView() {
        setupLayoutParams();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).addView(this, mLayoutParams);
        super.setVisibility(View.GONE);
    }

    protected void load() {
        inflateView();
        addView();
        refresh();
    }

    protected void unload() {
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).removeView(this);
        removeAllViews();
    }

    protected void reload() {
        unload();
        load();
    }

    public void destory() {
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).removeView(this);
    }

    public void refresh() {
        // Call this to update the contents of the Overlay.
        if (!isVisible()) {
            setVisibility(View.GONE);
        } else {
//            setVisibility(View.VISIBLE);
            refreshViews();
        }
    }

    protected void hide() {
        // Set visibility, but bypass onVisibilityToChange()
        super.setVisibility(View.GONE);
    }

    protected void show() {
        // Set visibility, but bypass onVisibilityToChange()
        super.setVisibility(View.VISIBLE);
    }

    public int getResId(String data) {
        Context context = RetailHeroApplication.getContext();
        int delimPos = data.indexOf('/');
        if (delimPos == -1 ) {
            return 0;
        }
        String[] vals = new String[2];
        vals[0] = data.substring(1, delimPos);
        vals[1] = data.substring(delimPos + 1);
        return context.getResources().getIdentifier(vals[1], vals[0], context.getPackageName());
    }

    protected abstract void onInflateView();
    protected abstract void onSetupLayoutParams(WindowManager.LayoutParams layoutParams);
    protected abstract int onLayoutGravity();
    protected abstract void refreshViews();
    protected abstract boolean onDoubleTapped(MotionEvent e);
    protected abstract boolean onSingleTapped(MotionEvent e);

    protected static final int FADE_IN_TIME = 200;
    protected static final int LATENCY_BETWEEN_ANIM = 300;

    protected ObjectAnimator getAlphaAnim(final View view, int delayCount) {
        ObjectAnimator animAlpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        animAlpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                Log.d(TAG, "##### onAnimationStart : view.setVisibility(View.VISIBLE)");
                setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animAlpha.setDuration(FADE_IN_TIME);
        animAlpha.setStartDelay(LATENCY_BETWEEN_ANIM * delayCount);

        return animAlpha;
    }

    protected ObjectAnimator getFadeOutAnim(final View view, int delayCount) {
        ObjectAnimator animAlpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        animAlpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animAlpha.setDuration(FADE_IN_TIME);
        animAlpha.setStartDelay(LATENCY_BETWEEN_ANIM * delayCount);

        return animAlpha;
    }


    private int initialX = 0;
    private int initialY = 0;
    private float initialTouchX = 0f;
    private float initialTouchY = 0f;
    private WindowManager.LayoutParams lparams;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        if (!mIsMoving) return true;
        lparams = mLayoutParams;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lparams = mLayoutParams;
                initialX = lparams.x;
                initialY = lparams.y;
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                lparams.x = initialX + (int) (event.getRawX() - initialTouchX);
                lparams.y = initialY + (int) (event.getRawY() - initialTouchY);
                ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(this, lparams);
                break;
        }
        return true;
    }

    private class GestureListener extends
            GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            onDoubleTapped(e);
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            onSingleTapped(e);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }
    }
}
