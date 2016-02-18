package com.samsung.retailexperience.retailhero.view;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.SpinnerAdapter;

/**
 * Created by MONSTER on 1/20/2016.
 */
public class CustomizedGallery extends Gallery {

    private static final float SCALEDOWN_GRAVITY_CENTER = 0.5f;

    private float mTouchXWhileNonScrollable;
    private float mTouchYWhileNonScrollable;

    private float unselectedAlpha = 0.8f;

    /**
     * Factor (0-1) that defines how much the unselected children should be scaled down. 1 means no scaledown.
     */
    private float unselectedScale = 0.9f;

    /**
     * TODO This may disable item.invalidate(); call
     * Distance in pixels between the transformation effects (alpha, rotation, zoom) are applied.
     */
    private int actionDistance;

    private GalleryTapListener listener;
    private GalleryTouchListener mTouchListener;

    private boolean mIsScrollable = true;
    private boolean mTouchStartedWhileNonScrollable;

    /**
     * Saturation factor (0-1) of items that reach the outer effects distance.
     */
    private float unselectedSaturation;

    public CustomizedGallery(Context context) {
        super(context);
    }

    public CustomizedGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomizedGallery(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setGalleryTapListener(GalleryTapListener listener) {
        this.listener = listener;
    }

    public void setGalleryTouchListener(GalleryTouchListener listener) {
        this.mTouchListener = listener;
    }

    public void setScrollable(boolean scrollable) {
        mIsScrollable = scrollable;
        if (!mIsScrollable) {
            mTouchStartedWhileNonScrollable = false;
        }
    }

    /**
     * Use this to provide a {@link CustomizedGalleryAdapter} to the coverflow. This method will throw an
     * {@link ClassCastException} if the passed adapter does not subclass {@link CustomizedGalleryAdapter}.
     *
     * @param adapter
     */
    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        if (!(adapter instanceof CustomizedGalleryAdapter)) {
            throw new ClassCastException(getClass().getSimpleName() + " only works in conjunction with a "
                    + CustomizedGalleryAdapter.class.getSimpleName());
        }

        super.setAdapter(adapter);
    }


    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        // We can cast here because CustomizedGalleryAdapter only creates wrappers.
        CustomizedGalleryItemWrapper item = (CustomizedGalleryItemWrapper) child;

        // TODO remove item.invalidate() function
        // Since Jelly Bean childs won't get invalidated automatically, needs to be added for the smooth coverflow
        // animation
        // if (android.os.Build.VERSION.SDK_INT >= 16) {
        item.invalidate();
        // }

        final int coverFlowWidth = this.getWidth();
        final int coverFlowCenter = coverFlowWidth / 2;
        final int childWidth = item.getWidth();
        final int childHeight = item.getHeight();
        final int childCenter = item.getLeft() + childWidth / 2;

        float effectsAmount = 0f;

        if (childCenter != coverFlowCenter) {
            final int actionDistance = (int) ((coverFlowWidth + childWidth) / 2.0f);

            // Calculate the abstract amount for all effects.
            effectsAmount = Math.min(1.0f, Math.max(-1.0f, (1.0f / actionDistance) * (childCenter - coverFlowCenter)));
        }

        // Clear previous transformations and set transformation type (matrix + alpha).
        t.clear();
        t.setTransformationType(Transformation.TYPE_BOTH);

        // // Alpha
        if (this.unselectedAlpha != 1) {
            final float alphaAmount = (this.unselectedAlpha - 1) * Math.abs(effectsAmount) + 1;
            t.setAlpha(alphaAmount);
        }


        final Matrix imageMatrix = t.getMatrix();
        //
        // Zoom.
        if (this.unselectedScale != 1) {
            final float zoomAmount = (this.unselectedScale - 1) * Math.abs(effectsAmount) + 1;
            // Calculate the scale anchor (y anchor can be altered)
            final float translateX = childWidth / 2.0f;
            final float translateY = childHeight * SCALEDOWN_GRAVITY_CENTER;
            imageMatrix.preTranslate(-translateX, -translateY);
            imageMatrix.postScale(zoomAmount, zoomAmount);
            imageMatrix.postTranslate(translateX, translateY);
        }

        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // return super.onSingleTapUp(e);

        if (listener != null) {
            listener.onGalleyTap();
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // super.onLongPress(e);
        return;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        int fakeKeyboardKey = velocityX > 0 ? KeyEvent.KEYCODE_DPAD_LEFT : KeyEvent.KEYCODE_DPAD_RIGHT;
        onKeyDown(fakeKeyboardKey, null);
        return true;
    }

    private void notifyTouchListener(MotionEvent event) {
        if (mTouchListener != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchListener.onTouchStarted();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mTouchListener.onTouchMoved();
                    break;
                case MotionEvent.ACTION_UP :
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    mTouchListener.onTouchEnd();
                    break;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        notifyTouchListener(event);

        if (mIsScrollable) {
            return super.onTouchEvent(event);
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchStartedWhileNonScrollable = true;
                    mTouchXWhileNonScrollable = event.getX();
                    mTouchYWhileNonScrollable = event.getY();
                break;
                case MotionEvent.ACTION_UP:
                    if (mTouchStartedWhileNonScrollable) {
                        if (listener != null) {
                            float offsetX = mTouchXWhileNonScrollable - event.getX();
                            float offsetY = mTouchYWhileNonScrollable - event.getY();
                            float squaredOffsetDistance = offsetX * offsetX + offsetY * offsetY;
                            if (squaredOffsetDistance < 400f) {
                                listener.onGalleyTap();
                            }
                        }
                        mTouchStartedWhileNonScrollable = false;
                    }
                    // TODO FIX it.  Need this code (When user is scrolling and gallery scroll is disabled, gallery is stuck)
                    // it should return true instead of super.onTouchEvent(event)!!!!!!!!
                    return super.onTouchEvent(event);
            }
            return true;
        }

    }

    public interface GalleryTapListener {
        void onGalleyTap();
    }

    public interface GalleryTouchListener {
        void onTouchStarted();
        void onTouchMoved();
        void onTouchEnd();
    }

}
