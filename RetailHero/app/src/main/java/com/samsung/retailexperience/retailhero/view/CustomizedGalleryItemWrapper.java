package com.samsung.retailexperience.retailhero.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by MONSTER on 1/20/2016.
 */
public class CustomizedGalleryItemWrapper extends ViewGroup {

    // =============================================================================
    // Private members
    // =============================================================================

    /**
     * This paint is used to draw the wrapped view including any filters.
     */
    private Paint paint;

    /**
     * This is a cache holding the wrapped view's visual representation.
     */
    private Bitmap wrappedViewBitmap;

    /**
     * This canvas is used to let the wrapped view draw it's content.
     */
    private Canvas wrappedViewDrawingCanvas;

    public CustomizedGalleryItemWrapper(Context context) {
        super(context);
        this.init();
    }

    public CustomizedGalleryItemWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public CustomizedGalleryItemWrapper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init();
    }

    private void init() {
        this.paint = new Paint();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.remeasureChildren();

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int measuredWidth = this.getMeasuredWidth();
            int measuredHeight = this.getMeasuredHeight();

            if (this.wrappedViewBitmap == null || this.wrappedViewBitmap.getWidth() != measuredWidth
                    || this.wrappedViewBitmap.getHeight() != measuredHeight) {
                this.wrappedViewBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
                this.wrappedViewDrawingCanvas = new Canvas(this.wrappedViewBitmap);
            }

            View child = getChildAt(0);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            int childLeft = (measuredWidth - childWidth) / 2;
            int childRight = measuredWidth - childLeft;
            child.layout(childLeft, 0, childRight, childHeight);
        }
    }

    // =============================================================================
    // Methods
    // =============================================================================

    private void remeasureChildren() {
        View child = this.getChildAt(0);

        if (child != null) {
            int heightSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.AT_MOST);
            int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.AT_MOST);
            this.getChildAt(0).measure(widthSpec, heightSpec);
        }
    }

}