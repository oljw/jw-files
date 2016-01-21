package com.samsung.retailexperience.retailhero.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

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
            setVisibility(View.VISIBLE);
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

    protected abstract void onInflateView();
    protected abstract void onSetupLayoutParams(WindowManager.LayoutParams layoutParams);
    protected abstract int onLayoutGravity();
    protected abstract void refreshViews();
    protected abstract boolean onSingleTapped(MotionEvent e);


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
            return false;
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
