package com.samsung.retailexperience.retailecosystem.util;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.samsung.retailexperience.retailecosystem.R;
import com.samsung.retailexperience.retailecosystem.ui.view.circlemenu.CircleMenuItemView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by icanmobile on 3/16/16.
 */
public class PageTransformer {
    private static final String TAG = PageTransformer.class.getSimpleName();

    ArrayList<ChildView> mChildViews = new ArrayList<ChildView>();
    ArrayList<ChildView> mListItemViews = new ArrayList<ChildView>();

    public void run(Context context, View view, boolean enter, AppConst.TransactionDir dir) {
        if (mChildViews.size() == 0)
            getChildViews(view, mChildViews);

        if (enter) {
            switch (dir) {
                case TRANSACTION_DIR_FORWARD:
                {
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
                                    for(int c=0; c<mListItemViews.size(); c++) {
                                        mListItemViews.get(c).getView().setTranslationX(deltaX * mListItemViews.get(c).getDeltaX());
                                    }
                                }
                                else {
                                    if (view.getId() == R.id.item_tablet_and_pc || view.getId() == R.id.item_smart_watch)
                                        view.setTranslationX(deltaX * mChildViews.get(i).getDeltaX() + 2.0f);
                                    else
                                     view.setTranslationX(deltaX * mChildViews.get(i).getDeltaX());
                                }
                            }
                        }
                    });
                    translateX.setDuration(context.getResources().getInteger(R.integer.animTime) * 2);
                    translateX.start();
                }
                break;
                case TRANSACTION_DIR_BACKWARD:
                {
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
                                    for(int c=0; c<mListItemViews.size(); c++) {
                                        mListItemViews.get(c).getView().setTranslationX(deltaX * mListItemViews.get(c).getDeltaX());
                                    }
                                }
                                else {
                                    if (view.getId() == R.id.item_smart_tv || view.getId() == R.id.item_samsung_pay)
                                        view.setTranslationX(deltaX * mChildViews.get(i).getDeltaX() + 2.0f);
                                    else
                                        view.setTranslationX(deltaX * mChildViews.get(i).getDeltaX());
                                }
                            }
                        }
                    });
                    translateX.setDuration(context.getResources().getInteger(R.integer.animTime) * 2);
                    translateX.start();
                }
                break;
            }
        }
    }

    Random rand = new Random();
    final float minX = 0.0f;
    final float maxX = 1.0f;
    float getRandomValue() {
        return rand.nextFloat() * (maxX - minX) + minX;
    }

    private void getChildViews(View view, ArrayList<ChildView> childViews) {
        if (!(view instanceof ViewGroup))
            return;

        if (view instanceof CircleMenuItemView || view.getId() == R.id.back_button)
            return;

        ViewGroup viewGroup = (ViewGroup) view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
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
