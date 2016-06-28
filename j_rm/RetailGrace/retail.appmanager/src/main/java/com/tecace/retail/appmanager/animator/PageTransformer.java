package com.tecace.retail.appmanager.animator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tecace.retail.appmanager.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by icanmobile on 3/16/16.
 */
public class PageTransformer {
    private static final String TAG = PageTransformer.class.getSimpleName();

    private static PageTransformer sInstance = null;
    public static PageTransformer getInstance() {
        if (sInstance == null)
            sInstance = new PageTransformer();
        return sInstance;
    }

    private ArrayList<ChildView> mChildViews = new ArrayList<ChildView>();
    private ArrayList<ChildView> mListItemViews = new ArrayList<ChildView>();
    private float mAcceleration = 1.5f;

    public float getAcceleration() {
        return mAcceleration;
    }
    public void setAcceleration(float acceleration) {
        mAcceleration = acceleration;
    }

    public void run(Context context, View view, boolean enter, boolean bForward, ArrayList<Integer> noChildViewIDs) {
        mChildViews.clear();
        mListItemViews.clear();
        getChildViews(view, mChildViews);

        mNoChildViewIDs.clear();
        if (noChildViewIDs != null)
            mNoChildViewIDs = noChildViewIDs;

        if (enter) {
            if (bForward) {
                final float sDeltaX = context.getResources().getInteger(R.integer.animXOffset);
                float eDeltaX = 0;

                ValueAnimator translateX = ValueAnimator.ofFloat(sDeltaX, eDeltaX);
                translateX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float deltaX = (float) animation.getAnimatedValue();
                        for (int i = 0; i < mChildViews.size(); i++) {
                            View view = mChildViews.get(i).getView();
                            if (view instanceof ListView && ((ListView) view).getChildCount() > 0) {
                                view.setTranslationX(0);

                                if (mListItemViews.size() == 0)
                                    getChildViews(view, mListItemViews);
                                for (int c = 0; c < mListItemViews.size(); c++) {
                                    mListItemViews.get(c).getView().setTranslationX(deltaX * mListItemViews.get(c).getDeltaX());
                                }
                            }
                            else {
                                view.setTranslationX(deltaX * mChildViews.get(i).getDeltaX());
                            }
                        }
                    }
                });
                translateX.setDuration(Math.round(context.getResources().getInteger(R.integer.animTime) * getAcceleration()));
                translateX.start();
            }
            else {
                final float sDeltaX = context.getResources().getInteger(R.integer.animXOffsetMinus);
                float eDeltaX = 0;

                ValueAnimator translateX = ValueAnimator.ofFloat(sDeltaX, eDeltaX);
                translateX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float deltaX = (float) animation.getAnimatedValue();
                        for (int i = 0; i < mChildViews.size(); i++) {
                            View view = mChildViews.get(i).getView();
                            if (view instanceof ListView && ((ListView) view).getChildCount() > 0) {
                                view.setTranslationX(0);

                                if (mListItemViews.size() == 0)
                                    getChildViews(view, mListItemViews);
                                for (int c = 0; c < mListItemViews.size(); c++) {
                                    mListItemViews.get(c).getView().setTranslationX(deltaX * mListItemViews.get(c).getDeltaX());
                                }
                            }
                            else {
                                view.setTranslationX(deltaX * mChildViews.get(i).getDeltaX());
                            }
                        }
                    }
                });
                translateX.setDuration(Math.round(context.getResources().getInteger(R.integer.animTime) * getAcceleration()));
                translateX.start();
            }
        }
    }

    ArrayList<Integer> mNoChildViewIDs = new ArrayList<Integer>();
    boolean isNoChildViewId(int viewId) {
        for(int i=0; i<mNoChildViewIDs.size(); i++) {
            int noChildViewId = mNoChildViewIDs.get(i);
            if (noChildViewId == viewId) return true;
        }
        return false;
    }

    Random rand = new Random();
    final float minX = 0.0f;
    final float maxX = 0.5f;
    float getRandomValue() {
        return rand.nextFloat() * (maxX - minX) + minX;
    }

    private void getChildViews(View view, ArrayList<ChildView> childViews) {
        if (!(view instanceof ViewGroup))
            return;

        if (isNoChildViewId(view.getId())) return;

        ViewGroup viewGroup = (ViewGroup) view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);

            //background image is too heavy for animation.
            if (isNoChildViewId(view.getId())) continue;

            childViews.add(new ChildView(child));
            getChildViews(child, childViews);
        }
    }


    private class ChildView {
        View view;
        float deltaX;

        public ChildView(View view) {
            this.view = view;
            this.deltaX = getRandomValue();
        }
        public ChildView(View view, float deltaX) {
            this.view = view;
            this.deltaX = deltaX;
        }

        public View getView() {
            return this.view;
        }
        public void setView(View view) {
            this.view = view;
        }

        public float getDeltaX() {
            return this.deltaX;
        }
        public void setDeltaX(float deltaX) {
            this.deltaX = deltaX;
        }
    }

}
