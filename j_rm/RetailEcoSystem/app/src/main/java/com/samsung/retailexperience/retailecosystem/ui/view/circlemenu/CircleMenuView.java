package com.samsung.retailexperience.retailecosystem.ui.view.circlemenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.samsung.retailexperience.retailecosystem.R;

import java.util.ArrayList;

/**
 * Created by icanmobile on 3/8/16.
 */
public class CircleMenuView extends RelativeLayout {
    private static final String TAG = CircleMenuView.class.getSimpleName();

    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private ImageView mCircle = null;
//    private ImageView mSmallCircle = null;
    private ImageView mPhone = null;
    private ArrayList<CircleMenuItemView> mItems = new ArrayList<CircleMenuItemView>();
    private CircleMenuViewListener mListener = null;
    private Paint mPaint = null;

    public CircleMenuView(Context context) {
        super(context);
        init(context, null);
    }

    public CircleMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.view_circle_menu, this, true);

        mCircle = (ImageView) findViewById(R.id.circle);
//        mSmallCircle = (ImageView)findViewById(R.id.small_circle);
        mPhone = (ImageView) findViewById(R.id.phone);

        CircleMenuItemView itemView = (CircleMenuItemView) findViewById(R.id.item_gear_vr);
        itemView.setTag("0");
        mItems.add(itemView);

        itemView = (CircleMenuItemView) findViewById(R.id.item_smart_tv);
        itemView.setTag("1");
        mItems.add(itemView);

        itemView = (CircleMenuItemView) findViewById(R.id.item_samsung_pay);
        itemView.setTag("2");
        mItems.add(itemView);

        itemView = (CircleMenuItemView) findViewById(R.id.item_smart_things);
        itemView.setTag("3");
        mItems.add(itemView);

        itemView = (CircleMenuItemView) findViewById(R.id.item_tablet_and_pc);
        itemView.setTag("4");
        mItems.add(itemView);

        itemView = (CircleMenuItemView) findViewById(R.id.item_smart_watch);
        itemView.setTag("5");
        mItems.add(itemView);


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF0077C8);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(8);

        this.setWillNotDraw(false);
        invalidate();

        postDelayed(new Runnable() {
            @Override
            public void run() {
                cancelMessages();
                mHandler.sendEmptyMessage(MSG_TURN_ON_ALL_ICONS);
//                mHandler.sendEmptyMessage(MSG_TURN_ON_CIRCLE_MODE_ALL_ICONS);
            }
        }, 800);

    }

    public interface CircleMenuViewListener {
        public void onClickItem(int index);
    }
    public void setListener(CircleMenuViewListener listener) {
        mListener = listener;
    }


    private int mSelectedItem = -1;
    int[] locOnScreen = new int[2];
    private int isTouchedItem(float x, float y) {
        int index = -1;
        for (CircleMenuItemView item : mItems) {
            item.getLocationOnScreen(locOnScreen);
            if ((x > locOnScreen[0] && x < (locOnScreen[0] + item.getMeasuredWidth())) &&
                (y > locOnScreen[1] && y < (locOnScreen[1] + item.getMeasuredHeight())))
                index = Integer.parseInt((String) item.getTag());
        }
        return index;
    }

    private boolean mSelected = false;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getPointerCount() > 1) return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                cancelMessages();

                mSelectedItem = isTouchedItem(event.getRawX(), event.getRawY());
                if (mSelectedItem > -1) {
                    mSelected = true;
                    invalidate();
                }
                else {
                    mHandler.sendEmptyMessage(MSG_TURN_ON_BLINK_MODE_ALL_ICONS);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mSelected && mSelectedItem != isTouchedItem(event.getRawX(), event.getRawY())) {
                    mSelectedItem = -1;
                    mSelected = false;
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mSelected && mSelectedItem != -1) {
                    if (mListener != null) mListener.onClickItem(mSelectedItem);
                }
                else {
                    mSelectedItem = -1;
                    mSelected = false;
                    invalidate();
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isTurnOnAllIcons()) {
            changePhoneRes(0);
            for(int i=0; i<mItems.size(); i++) {
                changeIconRes(i, false);
                drawLine(canvas, i);
            }
        }
        else if (isCircleMode()) {
            changeIconRes(mSelectedItem, true);
            changePhoneRes(0);
            drawLine(canvas, mSelectedItem);
        }
        else {
            changeIconRes(mSelectedItem, true);
            changePhoneRes(mSelectedItem);
            drawLine(canvas, mSelectedItem);
        }
    }

    private void changeIconRes(int index, boolean touched) {
        if (touched)
            resetIcons();

        if (index > -1) {
            mItems.get(index).onIconPressed(true);
        }
        else {
            resetIcons();
        }
    }

    private void resetIcons() {
        for(int i=0; i<mItems.size(); i++)
            mItems.get(i).onIconPressed(false);
    }

    private void changePhoneRes(int index) {
        if (index > -1) {
            mPhone.setImageResource(R.drawable.phone_icon_blue);
        }
        else {
            mPhone.setImageResource(R.drawable.phone_icon);
        }
    }

    int view_y = 0;
    Point viewCenter = new Point(0, 0);
    Point itemCenter = new Point(0, 0);
    private Point getViewCenterPos() {
        getLocationOnScreen(locOnScreen);
        view_y = locOnScreen[1];
        viewCenter.x = Math.round(locOnScreen[0] + (getMeasuredWidth()/2));
        viewCenter.y = Math.round(locOnScreen[1] + (getMeasuredHeight()/2)) - view_y;
        return viewCenter;
    }
    private Point getItemCenterPos(int index) {
        mItems.get(index).getLocationOnScreen(locOnScreen);
        itemCenter.x = Math.round(locOnScreen[0] + (mItems.get(index).getMeasuredWidth()/2));
        itemCenter.y = Math.round(locOnScreen[1] + (mItems.get(index).getMeasuredHeight()/2)) - view_y;
        return itemCenter;
    }

    private void drawLine(Canvas canvas, int index) {
        if (index == -1) return;
        Point viewCenter = getViewCenterPos();
        Point itemCenter = getItemCenterPos(index);
        canvas.drawLine(viewCenter.x, viewCenter.y, itemCenter.x, itemCenter.y, mPaint);
    }

    private boolean mIsTurnOnAllIcons = false;
    private boolean isTurnOnAllIcons() {
        return this.mIsTurnOnAllIcons;
    }
    private void setTurnOnAllIcons(boolean isTurnOnAllIcons) {
        this.mIsTurnOnAllIcons = isTurnOnAllIcons;
    }

    private boolean mIsCircleMode = false;
    private boolean isCircleMode() {
        return mIsCircleMode;
    }
    private void setCircleMode(boolean isCircleMode) {
        this.mIsCircleMode = isCircleMode;
    }

    private int mCount = 0;
    private boolean mTurned = false;
    private int MAX_BLINK_COUNT = 2;
    private int TURN_ON_TIME = 1000;
    private int BLINK_MODE_TIME = 150;
    private int CIRCLE_MODE_TIME = 100;
    private static final int MSG_TURN_ON_ALL_ICONS = 1;
    private static final int MSG_TURN_OFF_ALL_ICONS = 2;
    private static final int MSG_TURN_ON_BLINK_MODE_ALL_ICONS = 3;
    private static final int MSG_TURN_OFF_BLINK_MODE_ALL_ICONS = 4;
    private static final int MSG_TURN_ON_CIRCLE_MODE_ALL_ICONS = 5;
    private static final int MSG_TURN_OFF_CIRCLE_MODE_ALL_ICONS = 6;
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MSG_TURN_ON_ALL_ICONS:
                    {
                        if (isTurnOnAllIcons()) return;
                        setTurnOnAllIcons(true);
                        invalidate();
                        sendEmptyMessageDelayed(MSG_TURN_OFF_ALL_ICONS, TURN_ON_TIME);
                    }
                    break;

                case MSG_TURN_OFF_ALL_ICONS:
                    {
                        if (!isTurnOnAllIcons()) return;
                        setTurnOnAllIcons(false);
                        mSelectedItem = -1;
                        invalidate();
                    }
                    break;

                case MSG_TURN_ON_BLINK_MODE_ALL_ICONS:
                    {
                        if (isTurnOnAllIcons()) return;
                        setTurnOnAllIcons(true);
                        invalidate();
                        sendEmptyMessageDelayed(MSG_TURN_OFF_BLINK_MODE_ALL_ICONS, BLINK_MODE_TIME);
                    }
                    break;

                case MSG_TURN_OFF_BLINK_MODE_ALL_ICONS:
                    {
                        if (!isTurnOnAllIcons()) return;
                        setTurnOnAllIcons(false);
                        mSelectedItem = -1;
                        invalidate();

                        mCount++;
                        if (mCount < MAX_BLINK_COUNT)
                            sendEmptyMessageDelayed(MSG_TURN_ON_BLINK_MODE_ALL_ICONS, BLINK_MODE_TIME);
                        else
                            mCount = 0;
                    }
                    break;

                case MSG_TURN_ON_CIRCLE_MODE_ALL_ICONS:
                    {
                        setCircleMode(true);
                        mSelectedItem = mCount++;
                        invalidate();
                        sendEmptyMessageDelayed(MSG_TURN_OFF_CIRCLE_MODE_ALL_ICONS, CIRCLE_MODE_TIME);
                    }
                    break;
                case MSG_TURN_OFF_CIRCLE_MODE_ALL_ICONS:
                    {
                        //option 1
                        if (!mTurned) {
                            if (mCount >= mItems.size()) {
                                mTurned = true;
                                mCount = 0;
                            }
                            mSelectedItem = -1;
                            invalidate();
                            sendEmptyMessage(MSG_TURN_ON_CIRCLE_MODE_ALL_ICONS);
                        }
                        else {
                            mCount = 0;
                            mTurned = false;
                            setCircleMode(false);
                            sendEmptyMessage(MSG_TURN_ON_ALL_ICONS);
                        }

//                        //option 2
//                        if (mCount < mItems.size()) {
//                            mSelectedItem = -1;
//                            invalidate();
//                            sendEmptyMessage(MSG_TURN_ON_CIRCLE_MODE_ALL_ICONS);
//                        }
//                        else {
//                            mCount = 0;
//                            setCircleMode(false);
//                            sendEmptyMessage(MSG_TURN_ON_ALL_ICONS);
//                        }
                    }
                    break;
            }
        }
    };

    private void cancelMessages() {
        setTurnOnAllIcons(false);
        setCircleMode(false);
        mHandler.removeCallbacksAndMessages(null);
    }
}
