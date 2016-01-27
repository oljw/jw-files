package com.samsung.retailexperience.retailhero.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.samsung.retailexperience.retailhero.R;

/**
 * Created by jaekim on 1/23/16.
 */
public class MockedEdge extends FrameLayout implements View.OnTouchListener, View.OnClickListener {
    private static final String TAG = MockedEdge.class.getSimpleName();

    private static final float SWIPING_DRAWER_THRESHOLD_X = 1000f;
    private static final float GESTURE_LEFT_RIGHT_THRESHOLD = 20f;
    // if the last gesture happened in last 300 milli second, it is effective gesture
    private static final long GESTURE_EFFECTIVE_TIME_THRESHOLD = 300l;

    private static final float MINIMUM_DISTANCE_TO_PERFORM_ANIMATION = 10f;

    public enum ViewStatus {
        MAIN_VIEW, DRAWER_SWIPING, DRAWER_ANIMATION, DRAWER_VIEW
    }
    public enum GestureLeftRight {
        LEFT, RIGHT, NONE
    }
    public enum DrawerItem {
        CALENDAR, PHONE, EMAIL, CAMERA
    }

    public enum EdgeMode {
        APPS("Apps Edge", R.drawable.edge_apps_selection_indi),
        TASKS("Tasks Edge", R.drawable.edge_tasks_selection_indi);

        final String title;
        final int indicatorResourceId;

        EdgeMode(String title, int resourceId) {
            this.title = title;
            this.indicatorResourceId = resourceId;
        }

    }

    private View mSwipeToOpen;
    private View mDrawerView;
    private View mDrawerView2;
    private float mTouchStartedX;
    private float mTouchLastX;
    private ViewStatus mViewStatus;
    private long mSwipedTime;
    private GestureLeftRight mGestureLeftRight = GestureLeftRight.NONE;
//    private View mDrawerKnob;
    private View mTapEmail;
    private View mTapCalendar;
    private View mSuperOverlay;
    private TextView mEdgeModeTitle;
    private ImageView mEdgeModeIndicator;
    private View mEdgeModeBgContainer;

    private EdgeDrawerEventsListener mEdgeDrawerEventsListener;


    public MockedEdge(Context context) {
        super(context);
    }
    public MockedEdge(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MockedEdge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public MockedEdge(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mSwipeToOpen = findViewById(R.id.edge_swipe_to_open);
        mDrawerView = findViewById(R.id.edge_drawer);
        mDrawerView2 = findViewById(R.id.edge_drawer2);
        //mDrawerKnob = findViewById(R.id.edge_knob);
        mTapEmail = findViewById(R.id.edge_tap_email);
        mTapCalendar = findViewById(R.id.edge_tap_calendar);
        mSuperOverlay = findViewById(R.id.edge_drawer_highlighter_overlay);
        findViewById(R.id.edge_email_tapable).setOnClickListener(this);
        findViewById(R.id.edge_calendar_tapable).setOnClickListener(this);

        mEdgeModeTitle = (TextView) findViewById(R.id.edge_mode_title);
        mEdgeModeIndicator = (ImageView) findViewById(R.id.edge_mode_selection_indi);
        mEdgeModeBgContainer = findViewById(R.id.edge_mode_bg);

        this.setOnTouchListener(this);

        setViewStatus(ViewStatus.MAIN_VIEW);
    }

    private void setViewStatus(ViewStatus viewStatus) {
        switch (viewStatus) {
            case DRAWER_ANIMATION:
                break;
            case DRAWER_SWIPING:
                // initialize the position of drawer to the edge of root view
                mDrawerView.setX(this.getRight());
                mDrawerView.setVisibility(VISIBLE);
//                mDrawerKnob.setVisibility(INVISIBLE);

                // TODO search mSwipeToOpen references
                mSwipeToOpen.setVisibility(GONE);
                break;
            case DRAWER_VIEW:
                mSwipeToOpen.setVisibility(GONE);
                break;
            case MAIN_VIEW:
                mSwipeToOpen.setVisibility(VISIBLE);
                // INVISIBLE: I need a width of this, so I can place it correctly on the screen
                mDrawerView.setVisibility(INVISIBLE);
                mDrawerView2.setVisibility(INVISIBLE);
                mTapEmail.setVisibility(INVISIBLE);
                mTapCalendar.setVisibility(INVISIBLE);
                mSuperOverlay.setVisibility(GONE);

                mEdgeModeBgContainer.setAlpha(0f);
                break;
        }
        mViewStatus = viewStatus;
    }

    public void setEdgeDrawerEventsListener(EdgeDrawerEventsListener edgeDrawerEventsListener) {
        mEdgeDrawerEventsListener = edgeDrawerEventsListener;
    }

    public void forceSwipingAnimation() {
        performEdgeDrawerAnimation(true, true);
    }


    public void removeMainScreen() {
        mSwipeToOpen.setVisibility(GONE);
    }

    public void showTapAnEmail() {
        mSuperOverlay.setVisibility(VISIBLE);
        mTapEmail.setVisibility(VISIBLE);

//        View email = mDrawerView.findViewById(R.id.edge_email_image);
//        float emailPosY = mDrawerView.getY() + email.getY();    // recursively.  Need y positions of parent and email icon
//        float emailMarginRight = mDrawerView.getRight() - email.getRight();
//        float screenWidth = getResources().getDimensionPixelSize(R.dimen.display_width_in_pixel);
//        Log.e(TAG, "screenWidth: " + screenWidth + ", emailMarginRight: " + emailMarginRight + ", mTapEmail.getWidth(): " + mTapEmail.getWidth());
//        float tapEmailX = screenWidth - emailMarginRight - mTapEmail.getWidth();
//
//        mTapEmail.setY(emailPosY);
//        mTapEmail.setX(tapEmailX);

//        View targetView = mDrawerView.findViewById(R.id.edge_email_image);
        //matchPositionByTopAndRight(mTapEmail, targetView, mDrawerView);
        placeTappableViewAsDrawerItem(mTapEmail, 3);
    }

    public void showTapCalendar() {
        // two different drawer.  show the second one
        mSuperOverlay.setVisibility(VISIBLE);
        mTapCalendar.setVisibility(VISIBLE);


        mTapEmail.setVisibility(GONE);
        mSwipeToOpen.setVisibility(GONE);
        mDrawerView.setVisibility(GONE);

        mDrawerView2.setVisibility(VISIBLE);
        View targetView = mDrawerView.findViewById(R.id.edge_calendar_image2);
//        matchPositionByTopAndRight(mTapCalendar, targetView, mDrawerView2);

        placeTappableViewAsDrawerItem(mTapCalendar, 0);
    }

//    private void matchPositionByTopAndRight(View thisView, View targetView, View targetParentView) {
//        // Hack.  it should be recursively get Y position, but target and parent view's y are important.
//        float targetPosY = targetParentView.getY() + targetView.getY();
//        float targetMarginRight = targetParentView.getRight() - targetView.getRight();
//        float screenWidth = getResources().getDimensionPixelSize(R.dimen.display_width_in_pixel);
//        float targetPosX = screenWidth - targetMarginRight - thisView.getWidth();
//
//        thisView.setY(targetPosY);
//        thisView.setX(targetPosX);
//    }

    private void placeTappableViewAsDrawerItem(View tappableView, int drawerItemIndex) {
        float screenWidth = getResources().getDimensionPixelSize(R.dimen.display_width_in_pixel);
        float drawerWidth = getResources().getDimensionPixelSize(R.dimen.edge_drawer_width);
        float iconSize = getResources().getDimensionPixelSize(R.dimen.edge_drawer_icon_size);
        float iconRightEdge = screenWidth - drawerWidth + iconSize;
        float tappableViewX = iconRightEdge - tappableView.getWidth();

        float drawerTop = getResources().getDimensionPixelSize(R.dimen.edge_drawer_start_y);
        float textHeight = getResources().getDimensionPixelSize(R.dimen.edge_drawer_title_with_space);
        float tappableViewY = drawerTop + (iconSize + textHeight) * drawerItemIndex;

        tappableView.setX(tappableViewX);
        tappableView.setY(tappableViewY);
    }

    public void hideTapAnEmail() {
        mSuperOverlay.setVisibility(GONE);
        mTapEmail.setVisibility(INVISIBLE);

    }

    private void updateDrawerViewPosition(float touchX) {
        float offSet = mTouchStartedX - touchX;
        float newX = this.getRight() - offSet;
        float drawerOpenedPosX = this.getRight() - mDrawerView.getWidth();
        newX = Math.max(drawerOpenedPosX, newX);
        mDrawerView.setX(newX);

//        float newAlpha = getAlphaForEdgeModeBg();
        float newAlpha = offSet / mDrawerView.getWidth();
        newAlpha = Math.max(newAlpha, 0f);
        newAlpha = Math.min(newAlpha, 1f);
        mEdgeModeBgContainer.setAlpha(newAlpha);

        float movedX = Math.abs(touchX - mTouchLastX);
        if (movedX > GESTURE_LEFT_RIGHT_THRESHOLD) {
            mSwipedTime = System.currentTimeMillis();
            mGestureLeftRight = touchX > mTouchLastX ? GestureLeftRight.RIGHT : GestureLeftRight.LEFT;
        }
    }

//    private float getAlphaForEdgeModeBg() {
//        float offSet = this.getRight() - mEdgeModeBgContainer.getX();
//        float newAlpha = offSet / mDrawerView.getWidth();
//        newAlpha = Math.max(newAlpha, 0f);
//        newAlpha = Math.min(newAlpha, 1f);
//        return newAlpha;
//    }

    private void wrapUpSwiping() {
        if (mSwipedTime + GESTURE_EFFECTIVE_TIME_THRESHOLD > System.currentTimeMillis()) {
            switch (mGestureLeftRight) {
                case LEFT:
                    performEdgeDrawerAnimation(true, false);
                    return;
                case RIGHT:
                    performEdgeDrawerAnimation(false, false);
                    return;
                default:
                    Log.e(TAG, "ERROR: Gesture is not defined");
                    break;
            }
        }

        float drawerOpenedPosX = this.getRight() - mDrawerView.getWidth();
        boolean moveLeft = mDrawerView.getX() < drawerOpenedPosX + mDrawerView.getWidth() * 0.66f;
        performEdgeDrawerAnimation(moveLeft, false);
    }

    private void changeToDrawerOpenedMode() {
        setViewStatus(ViewStatus.DRAWER_VIEW);

        if (mEdgeDrawerEventsListener != null) {
            mEdgeDrawerEventsListener.edgeDrawerIsOpened();
        }
    }

    private void changeToDrawerClosedMode() {
        setViewStatus(ViewStatus.MAIN_VIEW);
    }

    private Animator.AnimatorListener createDrawerAnimationListener(final boolean isOpened) {
        Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // no additional work required.  ignore it.
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isOpened) {
                    changeToDrawerOpenedMode();
                } else {
                    changeToDrawerClosedMode();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (isOpened) {
                    changeToDrawerOpenedMode();
                } else {
                    changeToDrawerClosedMode();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // it shouldn't happen
            }
        };

        return animatorListener;
    }


    private void performEdgeDrawerAnimation(boolean moveLeft, boolean force) {
        if (force && mViewStatus != ViewStatus.DRAWER_SWIPING) {
            setUpEdgeBg(EdgeMode.APPS);

            // user is not interacting.  animate from right side
            mDrawerView.setX(this.getRight());
            mDrawerView.setVisibility(VISIBLE);
//            mDrawerKnob.setVisibility(INVISIBLE);
            mSwipeToOpen.setVisibility(INVISIBLE);
        }
        float drawerOpenedPosX = this.getRight() - mDrawerView.getWidth();
        if (mDrawerView.getX() - drawerOpenedPosX  < MINIMUM_DISTANCE_TO_PERFORM_ANIMATION) {
            mDrawerView.setX(drawerOpenedPosX);
            changeToDrawerOpenedMode();
            return;
        }

        // TODO if it is close animation, and there isn't much space to animate, don't animate (if !force)


        AnimatorSet animatorSet;
        if (moveLeft) {
            // perform Open Animation
            animatorSet = getDrawerAnimationSet(drawerOpenedPosX, 1f, createDrawerAnimationListener(true));
            if (mEdgeDrawerEventsListener != null) {
                mEdgeDrawerEventsListener.swipeLeftAnimationStarted();
            }
        } else {
            // perform Close Animation
            animatorSet = getDrawerAnimationSet(this.getRight(), 0f, createDrawerAnimationListener(false));
        }

        setViewStatus(ViewStatus.DRAWER_ANIMATION);

        animatorSet.start();
    }

    private AnimatorSet getDrawerAnimationSet(float targetPosX, float targetBgAlpha, Animator.AnimatorListener animatorListener) {
        ObjectAnimator animX = ObjectAnimator.ofFloat(mDrawerView, "x", targetPosX);
        AnimatorSet animSet = new AnimatorSet();
        long milliSeconds = Math.abs((long) ((mDrawerView.getX() - targetPosX) / 0.5f));
        animSet.setDuration(milliSeconds);
        animSet.addListener(animatorListener);

        ObjectAnimator bgAlpha = ObjectAnimator.ofFloat(mEdgeModeBgContainer, "alpha",
                mEdgeModeBgContainer.getAlpha(), targetBgAlpha);


        animSet.play(animX).with(bgAlpha);

        return animSet;
    }

    public void showTaskMode() {
        setUpEdgeBg(EdgeMode.TASKS);
    }

    private void setUpEdgeBg(EdgeMode mode) {
        mEdgeModeTitle.setText(mode.title);
        mEdgeModeIndicator.setImageResource(mode.indicatorResourceId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edge_email_tapable:
                if (mEdgeDrawerEventsListener != null) {
                    mEdgeDrawerEventsListener.emailTapped();
                }
                hideTapAnEmail();
                break;
            case R.id.edge_calendar_tapable:
                if (mEdgeDrawerEventsListener != null) {
                    mEdgeDrawerEventsListener.calendarTapped();
                }
                // TODO remove unnecessary views?  Only Video is left, ignore now
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartedX = event.getX();
                mTouchLastX = mTouchStartedX;
                if (mViewStatus == ViewStatus.MAIN_VIEW && mTouchStartedX > SWIPING_DRAWER_THRESHOLD_X) {
                    setUpEdgeBg(EdgeMode.APPS);
                    setViewStatus(ViewStatus.DRAWER_SWIPING);

                    if (mEdgeDrawerEventsListener != null) {
                        mEdgeDrawerEventsListener.swipeLeftTouchStarted();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mViewStatus == ViewStatus.DRAWER_SWIPING) {
                    updateDrawerViewPosition(event.getX());
                }
                mTouchLastX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                if (mViewStatus == ViewStatus.DRAWER_SWIPING) {
                    wrapUpSwiping();
                }
                break;
        }
        return true;
    }

    public interface EdgeDrawerEventsListener {
        void swipeLeftTouchStarted();
        void swipeLeftAnimationStarted();
        void edgeDrawerIsOpened();
        void emailTapped();
        void calendarTapped();
    }

}
