package com.tecace.retail.appmanager.ui.view.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by JW on 2016-06-21.
 *
 * This View is made to be used exclusively on Security Demo for Retail Grace.
 * Will be updated promptly when necessary.
 */
public class DrawingView extends View {

    private Paint mEyePaint;
    Rect rect1;
    Rect rect2;
    Rect rect3;

    public DrawingView(Context context) {
        super(context);
        initPaint();
    }
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }
    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }
    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
    }

    private void initPaint() {
        mEyePaint = new Paint();
        mEyePaint.setStrokeWidth(5f);
        mEyePaint.setStyle(Paint.Style.STROKE);
        mEyePaint.setColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rect1 != null ) {
            canvas.drawRect(rect1, mEyePaint);
        }
        if (rect2 != null) {
            canvas.drawRect(rect2, mEyePaint);
        }
        if (rect3 != null) {
            canvas.drawRect(rect3, mEyePaint);
        }
    }

    public void setRect(Rect r1) {
        this.rect1 = r1;
        this.invalidate();
    }

    public void setCircleRects(Rect r2, Rect r3) {
        this.rect2 = r2;
        this.rect3 = r3;
        this.invalidate();
    }
}
