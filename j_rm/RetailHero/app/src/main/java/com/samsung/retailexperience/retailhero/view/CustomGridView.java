package com.samsung.retailexperience.retailhero.view;

import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by icanmobile on 2/2/16.
 */
public class CustomGridView extends GridView {
    public CustomGridView(Context context) {
        super(context);
    }

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int computeVerticalScrollOffset() {
        return super.computeVerticalScrollOffset();
    }

    @Override
    public int computeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    private OnNoItemPressListener listener;
    public interface OnNoItemPressListener
    {
        public void onNoItemPress();
        public void onNoItemLongPress();
    }

    public void setOnNoItemPressListener(OnNoItemPressListener listener)
    {
        this.listener = listener;
    }

    private PointF downPoint = new PointF(0,0);
    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        // The pointToPosition() method returns -1 if the touch event
        // occurs outside of a child View.
        // Change the MotionEvent action as needed. Here we use ACTION_DOWN
        // as a simple, naive indication of a click.
        if (pointToPosition((int) event.getX(), (int) event.getY()) == -1) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                downPoint.x = event.getRawX();
                downPoint.y = event.getRawY();
                handler.sendEmptyMessageDelayed(MSG_LONG_PRESS, 1000);
            }
            else if(event.getAction() == MotionEvent.ACTION_MOVE) {
                if (Math.abs(downPoint.x - event.getRawX()) > 30 ||
                        Math.abs(downPoint.y - event.getRawY()) > 30)
                    handler.removeCallbacksAndMessages(null);
            }
            else if(event.getAction() == MotionEvent.ACTION_UP) {
                handler.removeCallbacksAndMessages(null);
                if (listener != null)
                    listener.onNoItemPress();
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private static final int MSG_LONG_PRESS        = 1;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LONG_PRESS:
                {
                    if (listener != null)
                        listener.onNoItemLongPress();
                }
                break;
            }
        }
    };
}

