package com.developer.jw.ecosystemcopy.ui.view.circlemenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.developer.jw.ecosystemcopy.R;

import java.util.ArrayList;

/**
 * Created by icanmobile on 3/8/16.
 */
public class CircleMenuView extends RelativeLayout {
    private static final String TAG = CircleMenuView.class.getSimpleName();

    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private ImageView mCircle = null;
    private ImageView mPhone = null;
    private ArrayList<CircleMenuItemView> mItems = new ArrayList<CircleMenuItemView>();
    private CircleMenuViewListener mListener = null;
    private Paint mPaint = null;
    private boolean mInitAnimationEnd = false;

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
        mInitAnimationEnd = false;
        mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.view_circle_menu, this, true);

        mCircle = (ImageView) findViewById(R.id.circle);
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

        itemView = (CircleMenuItemView) findViewById(R.id.item_tablet);
        itemView.setTag("4");
        mItems.add(itemView);

        itemView = (CircleMenuItemView) findViewById(R.id.item_smart_watch);
        itemView.setTag("5");
        mItems.add(itemView);

        // fade out circle items.
        setFadeCircleItems(0f);


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF0077C8);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(8);

        this.setWillNotDraw(false);
        invalidate();
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
        if (mInitAnimationEnd == false) return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                cancelMessages();

                mSelectedItem = isTouchedItem(event.getRawX(), event.getRawY());
                if (mSelectedItem > -1) {
                    mSelected = true;
                    invalidate();
                    mHandler.sendEmptyMessageDelayed(MSG_ICON_SELECTED, ICON_SELECTED_TIME);
                }
                else {
                    mHandler.sendEmptyMessage(MSG_TURN_ON_ALL_ICONS);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mSelected && mSelectedItem != isTouchedItem(event.getRawX(), event.getRawY())) {
                    cancelMessages();
                    mSelectedItem = -1;
                    mSelected = false;
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mSelected && mSelectedItem != -1) {
                    cancelMessages();
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
        else if (isTurnOnAddIcons()) {
            for(int i=0; i<=mSelectedItem; i++) {
                turnOnIconRes(i);
                changePhoneRes(i);
                drawLine(canvas, i);
            }
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

        if (index > -1 && index < mItems.size()) {
            mItems.get(index).onIconPressed(true);
        }
        else {
            resetIcons();
        }
    }
    private void turnOnIconRes(int index) {
        if (index <= -1 || index >= mItems.size()) return;
        mItems.get(index).onIconPressed(true);
    }
    private void turnOffIconRes(int index) {
        if (index <= -1 || index >= mItems.size()) return;
        mItems.get(index).onIconPressed(false);
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
        if (index <= -1 || index >= mItems.size()) return null;

        mItems.get(index).getLocationOnScreen(locOnScreen);
        itemCenter.x = Math.round(locOnScreen[0] + (mItems.get(index).getMeasuredWidth()/2));
        itemCenter.y = Math.round(locOnScreen[1] + (mItems.get(index).getMeasuredHeight()/2)) - view_y;

//        Log.d(TAG, "##### itemCenter.x = " + itemCenter.x + ", itemCenter.y = " + itemCenter.y);
        return itemCenter;
    }

    private void drawLine(Canvas canvas, int index) {
        if (index <= -1 || index >= mItems.size()) return;
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

    private boolean mIsTurnOnAddIcons = false;
    private boolean isTurnOnAddIcons() {
        return this.mIsTurnOnAddIcons;
    }
    private void setTurnOnAddIcons(boolean isTurnOnAddIcons) {
        this.mIsTurnOnAddIcons = isTurnOnAddIcons;
    }


    private int TURN_OFF_ALL_ICONS_TIME = 1000;
    private int TURN_ON_ADD_ICONS_TIME  = 50;
    private int TURN_OFF_ADD_ICONS_TIME = 500;
    private int ICON_SELECTED_TIME      = 200;
    private static final int MSG_TURN_ON_ALL_ICONS  = 1;
    private static final int MSG_TURN_OFF_ALL_ICONS = 2;
    private static final int MSG_TURN_ON_ADD_ICONS  = 10;
    private static final int MSG_TURN_OFF_ADD_ICONS = 11;
    private static final int MSG_ICON_SELECTED      = 100;
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MSG_TURN_ON_ALL_ICONS:
                    {
                        setTurnOnAllIcons(true);
                        invalidate();
                        sendEmptyMessageDelayed(MSG_TURN_OFF_ALL_ICONS, TURN_OFF_ALL_ICONS_TIME);
                    }
                    break;

                case MSG_TURN_OFF_ALL_ICONS:
                    {
                        setTurnOnAllIcons(false);
                        mSelectedItem = -1;
                        invalidate();
                    }
                    break;

                case MSG_TURN_ON_ADD_ICONS:
                    {
                        setTurnOnAddIcons(true);
                        mSelectedItem++;

                        if (mSelectedItem < mItems.size()) {
                            invalidate();
                            sendEmptyMessageDelayed(MSG_TURN_ON_ADD_ICONS, TURN_ON_ADD_ICONS_TIME);
                        }
                        else {
                            sendEmptyMessageDelayed(MSG_TURN_OFF_ADD_ICONS, TURN_OFF_ADD_ICONS_TIME);
                        }
                    }
                    break;

                case MSG_TURN_OFF_ADD_ICONS:
                    {
                        setTurnOnAddIcons(false);
                        mSelectedItem = -1;
                        invalidate();
                    }
                    break;

                case MSG_ICON_SELECTED:
                    {
                        mHandler.sendEmptyMessage(MSG_TURN_OFF_ALL_ICONS);
                        if (mListener != null) mListener.onClickItem(mSelectedItem);
                    }
                    break;
            }
        }
    };

    private void cancelMessages() {
        setTurnOnAllIcons(false);
        setTurnOnAddIcons(false);
        mHandler.removeCallbacksAndMessages(null);
    }



    public void setFadeCircleItems(float alpha) {
        mCircle.setAlpha(alpha);
        for(int i=0; i<mItems.size(); i++) {
            mItems.get(i).setAlpha(alpha);
        }
    }

    public void fadeInCircleItems() {
        AnimatorSet anims = new AnimatorSet();

        ObjectAnimator anim1 = getFadeInAnim(mCircle, 0);
        ObjectAnimator anim2 = getFadeInAnim(mItems.get(0), 1);
        ObjectAnimator anim3 = getFadeInAnim(mItems.get(1), 2);
        ObjectAnimator anim4 = getFadeInAnim(mItems.get(2), 3);
        ObjectAnimator anim5 = getFadeInAnim(mItems.get(3), 4);
        ObjectAnimator anim6 = getFadeInAnim(mItems.get(4), 5);
        ObjectAnimator anim7 = getFadeInAnim(mItems.get(5), 6);

        anims.playTogether(anim1, anim2, anim3, anim4, anim5, anim6, anim7);
        anims.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // end of init animation
                mInitAnimationEnd = true;
                mHandler.sendEmptyMessageDelayed(MSG_TURN_ON_ADD_ICONS, TURN_ON_ADD_ICONS_TIME);
            }
        });
        anims.setStartDelay(FADE_IN_TIME);
        anims.start();
    }

    private static final int FADE_IN_TIME = 100;
    private static final int LATENCY_BETWEEN_ANIM = 50;
    private ObjectAnimator getFadeInAnim(final View view, int delayCount) {
        ObjectAnimator animAlpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        animAlpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
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

    private ObjectAnimator getFadeOutAnim(final View view, int delayCount) {
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
}
