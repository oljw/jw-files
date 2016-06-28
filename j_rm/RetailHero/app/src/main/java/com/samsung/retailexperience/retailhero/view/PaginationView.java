package com.samsung.retailexperience.retailhero.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.RetailHeroApplication;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by icanmobile on 1/19/16.
 */
public class PaginationView<T> extends FrameLayout {
    private static final String TAG = PaginationView.class.getSimpleName();

    private Context mContext = null;
    private GestureDetector mGestureDetector = null;
    private ArrayList<PaginationViewItem<T>> mPages = new ArrayList<PaginationViewItem<T>>();
    private int mIndex = 0;

    public PaginationView(Context context) {
        super(context);
        init(context);
    }

    public PaginationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PaginationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mGestureDetector = new GestureDetector(mContext, new simpleGestureListener());
    }

    @Override
    protected void onFinishInflate () {
        super.onFinishInflate();
    }

    public ArrayList<PaginationViewItem<T>> getPages() {
        return mPages;
    }

    public void setPages(ArrayList<PaginationViewItem<T>> pages, final String uiState) {
        if (pages == null) return;
        mPages = pages;
        setPageIndex(mPages.size() - 1);

        View view = mPages.get(getPageIndex(uiState)).getFragment().getView();
        if (view != null) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    if (mListener != null)
                        mListener.onPaginationReady(uiState);
                }
            });
        }
    }


    private PaginationViewListener mListener = null;
    public interface PaginationViewListener {
        void onPaginationReady(String uiState);
        void onChangedPage(BaseFragment fragment, String uiState, String drawerId, int pageIndex);
    }
    public void setListener(PaginationViewListener listener) {
        mListener = listener;
    }

    private View getPageView(int index) {
        if (mPages == null || mPages.size() < 0) return null;
        return mPages.get(index).getFragment().getView();
    }

    private boolean mFreeze = false;
    private boolean getFreeze() {
        return mFreeze;
    }
    public void setFreeze(boolean freeze) {
        mFreeze = freeze;
    }

    private float mDownX = 0f;
    private boolean mCheckDir = false;
    private boolean mMoveLeft = true;
    private float mThreshold = 20f;
    private int mAnimTime = getResources().getInteger(R.integer.animTime);
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (marginXAnimator != null && marginXAnimator.isRunning()) return false;
        if (getFreeze()) return false;

        if (event.getPointerCount() > 1) return false;

        mGestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getRawX();
                mMoveLeft = true;   //<--- left out
                mCheckDir = false;

                //disable all of the child controls in page for moving fragment.
                setEnableChildsInPage(mPages.get(getPageIndex()).getFragment().getView(), true);
                break;

            case MotionEvent.ACTION_MOVE:
                if (!mCheckDir) {
                    if (event.getRawX() - mDownX > mThreshold) {
                        mMoveLeft = false;
                        mCheckDir = true;
                        //disable all of the child controls in page for moving fragment.
                        setEnableChildsInPage(mPages.get(getPageIndex()).getFragment().getView(), false);
                    }

                    if (event.getRawX() - mDownX < -1 * mThreshold) {
                        mMoveLeft = true;
                        mCheckDir = true;
                        //disable all of the child controls in page for moving fragment.
                        setEnableChildsInPage(mPages.get(getPageIndex()).getFragment().getView(), false);
                    }

                    if (Math.abs(event.getRawX() - mDownX) <= mThreshold)
                        return super.dispatchTouchEvent(event);
                }

                if (mMoveLeft)
                    movingLeft(Math.round(event.getRawX() - mDownX));
                else
                    movingRight(Math.round(event.getRawX() - mDownX));

//                handler.removeCallbacksAndMessages(null);
//                handler.sendMessageDelayed(handler.obtainMessage(MSG_ACTION_MOVE, Math.round(event.getRawX() - mDownX)), 200);
                break;

            case MotionEvent.ACTION_UP:
//                handler.removeCallbacksAndMessages(null);
                if (mMoveLeft)
                    movedLeft(Math.round(event.getRawX() - mDownX));
                else
                    movedRight(Math.round(event.getRawX() - mDownX));
                break;
        }
        return super.dispatchTouchEvent(event);
    }

//    private static final int MSG_ACTION_MOVE = 1;
//    private Handler handler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_ACTION_MOVE: {
//                    if (msg.obj == null) return;
//                    if (mMoveLeft)
//                        movedLeft(Math.round((int) msg.obj));
//                    else
//                        movedRight(Math.round((int) msg.obj));
//                }
//                break;
//            }
//        }
//    };

    private void movingDirection(boolean bMoveLeft, float downX) {
        if (bMoveLeft) {
            if (getPageIndex() == 0) {  //last page
                for (int p = mPages.size() - 1; p >= 0; p--) {
                    View view = mPages.get(p).getFragment().getView();
                    setPageMarginX(view, 0);
                }
                if (mListener != null)
                    mListener.onChangedPage(mPages.get(mPages.size() - 1).getFragment(),
                            mPages.get(mPages.size() - 1).getUIState(),
                            mPages.get(mPages.size() - 1).getModel().getDrawerId(),
                            (mPages.size() - 1));

                setPageIndex(mPages.size() - 1);
            }
        } else {
            if (getPageIndex() == 0 || getPageIndex() == 4) {
                for (int p = mPages.size() - 1; p >= 1; p--) {
                    View view = mPages.get(p).getFragment().getView();
                    setPageMarginX(view, getResources().getInteger(R.integer.animXOffsetMinus));
                }
                if (mListener != null)
                        mListener.onChangedPage(mPages.get(0).getFragment(),
                                mPages.get(0).getUIState(),
                                mPages.get(0).getModel().getDrawerId(),
                                0);
                setPageIndex(0);
            }
        }
    }

    int lastDiffX = 0;
    private void movingLeft(int diffX) {
        if (getPageIndex() == 0) return;
        if (diffX > 0) return;
        setPageMarginX(getPageView(getPageIndex()), diffX);
        lastDiffX = diffX;
    }

    private void movedLeft(int diffX) {
        if (getPageIndex() == 0) return;
        if (diffX > 0) diffX = lastDiffX;
        lastDiffX = 0;
        pageMarginXAnimation(getPageView(getPageIndex()), diffX);

    }

    private void movingRight(int diffX) {
        if (getPageIndex() == mPages.size() - 1) return;
        if (diffX < 0) return;
        setPageMarginX(getPageView(getPrevPageIndex()), (getResources().getInteger(R.integer.animXOffsetMinus) + diffX));
    }

    private void movedRight(int diffX) {
        if (getPageIndex() == mPages.size() - 1) return;
        pageMarginXAnimation(getPageView(getPrevPageIndex()), (getResources().getInteger(R.integer.animXOffsetMinus) + diffX));
    }

    private int getPageMarginX(View view) {
        if (view == null) return 0;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return params.leftMargin;
    }

    private void setPageMarginX(View view, int moveX) {
        if (view == null) return;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.setMargins(moveX, 0, (-1 * moveX), 0);
        view.requestLayout();

        if (mMoveLeft) {
            setScaleXY(getPageView(getNextPageIndex()), true, moveX);
        } else {
            setScaleXY(getPageView(getPageIndex()), false, moveX);
        }
    }

    private ValueAnimator marginXAnimator = null;
    private boolean mReverse = true;
    private int mTargetValue = 0;

    private void pageMarginXAnimation(final View view, float diffX) {
        Log.d(TAG, "##### pageMarginXAnimation)+");

        if (view == null) return;

        if (marginXAnimator != null && marginXAnimator.isRunning())
            marginXAnimator.cancel();

        int valueFrom = Math.round(diffX);
        int valueTo = 0;
        mReverse = true;

        if (mMoveLeft) {
            valueTo = 0;
            if (Math.abs(valueFrom) >= Math.round(getResources().getInteger(R.integer.animXOffset) / 4)) {
                valueTo = getResources().getInteger(R.integer.animXOffsetMinus);
                mReverse = false;
            }
        } else {
            valueTo = getResources().getInteger(R.integer.animXOffsetMinus);
            if (Math.abs(valueTo - valueFrom) >= Math.round(getResources().getInteger(R.integer.animXOffset) / 4)) {
                valueTo = 0;
                mReverse = false;
            }
        }
        mTargetValue = valueTo;

        marginXAnimator = ValueAnimator.ofInt(valueFrom, valueTo);
        marginXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setPageMarginX(view, (Integer) valueAnimator.getAnimatedValue());
            }
        });
        marginXAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mReverse) return;
                if (mListener != null) {
                    int index = 0;
                    if (mMoveLeft)  //<-- left out
                        index = getNextPageIndex();
                    else    //-->   //left in
                        index = getPrevPageIndex();
                    setPageMarginX(view, mTargetValue);

                    //change index
                    setPageIndex(index);
                    mListener.onChangedPage(mPages.get(getPageIndex()).getFragment(),
                            mPages.get(getPageIndex()).getUIState(),
                            mPages.get(getPageIndex()).getModel().getDrawerId(),
                            getPageIndex());
                }
            }
        });
        marginXAnimator.setInterpolator(new DecelerateInterpolator());
        marginXAnimator.setDuration(mAnimTime);
        marginXAnimator.start();
    }

    public void setScaleXY(View view, boolean bMoveLeft, float diffX) {
        if (view == null) return;
        view.setPivotX(RetailHeroApplication.getContext().getResources().getInteger(R.integer.animXOffset) / 2);
        view.setPivotY(RetailHeroApplication.getContext().getResources().getInteger(R.integer.animYOffset) / 2);

        if (bMoveLeft) {
            float startValue = getResources().getInteger(R.integer.animXOffset) / 10f;
            float endValue = getResources().getInteger(R.integer.animXOffset) / 2f;

            if (Math.abs(diffX) < startValue) {
                view.setScaleX(0.8f);
                view.setScaleY(0.8f);
            } else if (Math.abs(diffX) > startValue && Math.abs(diffX) < endValue) {
                float xy = 0.8f + ((1f - 0.8f) / (endValue - startValue)) * (Math.abs(diffX) - startValue);
                if (xy < 0.8) xy = 0.8f;
                if (xy > 1f) xy = 1f;
                view.setScaleX(xy);
                view.setScaleY(xy);
            } else if (Math.abs(diffX) > startValue) {
                view.setScaleX(1f);
                view.setScaleY(1f);
            }
        } else {
            float endValue = getResources().getInteger(R.integer.animXOffset) / 10f;
            float startValue = getResources().getInteger(R.integer.animXOffset) / 2f;

            if (Math.abs(diffX) > startValue) {
                view.setScaleX(1f);
                view.setScaleY(1f);
            } else if (Math.abs(diffX) < startValue && Math.abs(diffX) > endValue) {
                float xy = 1f - ((1f - 0.8f) / (endValue - startValue)) * (Math.abs(diffX) - startValue);
                if (xy < 0.8) xy = 0.8f;
                if (xy > 1f) xy = 1f;
                view.setScaleX(xy);
                view.setScaleY(xy);
            } else if (Math.abs(diffX) < endValue) {
                view.setScaleX(0.8f);
                view.setScaleY(0.8f);
            }
        }
    }

    private int getPageIndex() {
        return mIndex;
    }

    private void setPageIndex(int index) {
        mIndex = index;
    }

    //4 -> 3 -> 2 -> 1 -> 0
    private int getNextPageIndex() {
        int index = getPageIndex();
        index--;
        if (index < 0) index = mPages.size() - 1;
        return index;
    }

    //0 -> 1 -> 2 -> 3 -> 4
    private int getPrevPageIndex() {
        int index = getPageIndex();
        index++;
        if (index >= mPages.size()) index = 0;
        return index;
    }

    public int getPageIndex(String uiState) {
        for (int p = 0; p < mPages.size(); p++) {
            if (mPages.get(p).getUIState().equals(uiState))
                return p;
        }
        return mPages.size() - 1;
    }

    public void setPage(String uiState) {
        //reset each page left margin
        for (int p = 0; p < mPages.size(); p++) {
            setPageMarginX(mPages.get(p).getFragment().getView(), 0);
            setScaleXY(mPages.get(p).getFragment().getView(), true, 0);
        }

        int pageIndex = getPageIndex(uiState);
        for (int p = mPages.size() - 1; p > pageIndex; p--) {
            setPageMarginX(mPages.get(p).getFragment().getView(), getResources().getInteger(R.integer.animXOffsetMinus));
            setScaleXY(mPages.get(p).getFragment().getView(), true, getResources().getInteger(R.integer.animXOffsetMinus));
        }
        setScaleXY(mPages.get(pageIndex).getFragment().getView(), true, getResources().getInteger(R.integer.animXOffsetMinus));

        setPageIndex(pageIndex);
    }

    public PaginationViewItem<T> getPage(String uiState) {
        int pageIndex = getPageIndex(uiState);
        return mPages.get(pageIndex);
    }

    public PaginationViewItem<T> getPage() {
        return mPages.get(getPageIndex());
    }

    private void setEnableChildsInPage(View view, boolean isEnable) {
        if (!(view instanceof ViewGroup))
            return;

        ViewGroup viewGroup = (ViewGroup) view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            child.setEnabled(isEnable);
            setEnableChildsInPage(child, isEnable);
        }
    }

    private class simpleGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            String velocity = "onFling: \n" + e1.toString() + "\n" + e2.toString() + "\n"
//                    + "velocityX= " + String.valueOf(velocityX) + "\n"
//                    + "velocityY= " + String.valueOf(velocityY) + "\n";

            int value = Math.round(Math.abs(velocityX));
            if (value == 0) {
                mAnimTime = RetailHeroApplication.getContext().getResources().getInteger(R.integer.animTime);
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            mAnimTime = 1000000/value;
            mAnimTime = Math.max(mAnimTime, 50);
            mAnimTime = Math.min(mAnimTime, RetailHeroApplication.getContext().getResources().getInteger(R.integer.animTime));
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}