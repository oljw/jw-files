package com.samsung.retailexperience.retailhero.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.RetailHeroApplication;

/**
 * Created by icanmobile on 1/22/16.
 */
public class AnimationUtil {
    private static final String TAG = AnimationUtil.class.getSimpleName();

    /*
     * Margin X Animation
     */
    public static int getMarginX(View view) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return params.leftMargin;
    }
    public static void setMarginX(View view, int l) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.setMargins(l, 0, -l, 0);
        view.requestLayout();
    }

    private static ValueAnimator marginXAnimator = null;
    public static void marginXAnimation(final View view, boolean leftOut, final AnimatorListenerAdapter listener) {
        if (marginXAnimator != null && marginXAnimator.isRunning())
            marginXAnimator.cancel();

        int valueFrom = 0;
        int valueTo = 0;

        if (leftOut) {  // 0 ~ -1440
            valueFrom = 0;
            valueTo = RetailHeroApplication.getContext().getResources().getInteger(R.integer.animXOffsetMinus);
        }
        else {  // -1440 ~ 0
            valueFrom = RetailHeroApplication.getContext().getResources().getInteger(R.integer.animXOffsetMinus);
            valueTo = 0;
        }

        marginXAnimator = ValueAnimator.ofInt(valueFrom, valueTo);
        marginXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setMarginX(view, (Integer) valueAnimator.getAnimatedValue());
            }
        });

        if (listener != null) {
            marginXAnimator.addListener(listener);
        }
        else {
            marginXAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            });
        }
        marginXAnimator.setInterpolator(new DecelerateInterpolator());
        marginXAnimator.setDuration(RetailHeroApplication.getContext().getResources().getInteger(R.integer.animTime));
        marginXAnimator.start();
    }


    /*
     * Sliding and Pop up/down
     */
    private static ValueAnimator slidingAnimator = null;
    public static void slidingAnimation(final View view, boolean up, final AnimatorListenerAdapter listener) {
        if (up)
            slidingAnimator = ObjectAnimator.ofPropertyValuesHolder(view,
                    PropertyValuesHolder.ofFloat("yFraction", 1.f),
                    PropertyValuesHolder.ofFloat("yFraction", 0f));
        else
            slidingAnimator = ObjectAnimator.ofPropertyValuesHolder(view,
                    PropertyValuesHolder.ofFloat("yFraction", 0f),
                    PropertyValuesHolder.ofFloat("yFraction", 1f));

        if (listener != null) {
            slidingAnimator.addListener(listener);
        }
        else {
            slidingAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            });
        }
        slidingAnimator.setInterpolator(new DecelerateInterpolator());
        slidingAnimator.setDuration(RetailHeroApplication.getContext().getResources().getInteger(R.integer.animTime));
        slidingAnimator.start();
    }

//    public void setPopAnimation(View view, boolean up, float yFraction) {
//        view.setPivotX(RetailHeroApplication.getContext().getResources().getInteger(R.integer.animXOffset) / 2);
//        view.setPivotY(RetailHeroApplication.getContext().getResources().getInteger(R.integer.animYOffset) / 2);
//
//        if (up) {
//            float startValue = getResources().getInteger(R.integer.animXOffset) / 10f;
//            float endValue = getResources().getInteger(R.integer.animXOffset) / 2f;
//
//            if (Math.abs(diff) < startValue) {
//                view.setScaleX(0.8f);
//                view.setScaleY(0.8f);
//            } else if (Math.abs(diff) > startValue && Math.abs(diff) < endValue) {
//                float xy = 0.8f + ((1f - 0.8f) / (endValue - startValue)) * (Math.abs(diff) - startValue);
//                if (xy < 0.8) xy = 0.8f;
//                if (xy > 1f) xy = 1f;
//                view.setScaleX(xy);
//                view.setScaleY(xy);
//            } else if (Math.abs(diff) > startValue) {
//                view.setScaleX(1f);
//                view.setScaleY(1f);
//            }
//        } else {
//            float endValue = getResources().getInteger(R.integer.animXOffset) / 10f;
//            float startValue = getResources().getInteger(R.integer.animXOffset) / 2f;
//
//            if (Math.abs(diff) > startValue) {
//                view.setScaleX(1f);
//                view.setScaleY(1f);
//            } else if (Math.abs(diff) < startValue && Math.abs(diff) > endValue) {
//                float xy = 1f - ((1f - 0.8f) / (endValue - startValue)) * (Math.abs(diff) - startValue);
//                if (xy < 0.8) xy = 0.8f;
//                if (xy > 1f) xy = 1f;
//                view.setScaleX(xy);
//                view.setScaleY(xy);
//            } else if (Math.abs(diff) < endValue) {
//                view.setScaleX(0.8f);
//                view.setScaleY(0.8f);
//            }
//        }
//    }



    /*
     * Scale XY Animation
     */
    public static void setScaleXY(View view, float xy) {
        view.setPivotX(0);
        view.setPivotY(RetailHeroApplication.getContext().getResources().getInteger(R.integer.animYOffset));
        view.setScaleX(xy);
        view.setScaleY(xy);
    }
    private static ValueAnimator scaleXYAnimator = null;
    public static void scaleXYAnimation(final View view, boolean down, final AnimatorListenerAdapter listener) {
        if (scaleXYAnimator != null && scaleXYAnimator.isRunning())
            scaleXYAnimator.cancel();

        float valueFrom = 0;
        float valueTo = 0;

        if (down) {
            valueFrom = 1.0f;
            valueTo = 0f;
            setScaleXY(view, 1.0f);
        }
        else {
            valueFrom = 0f;
            valueTo = 1.0f;
            setScaleXY(view, 0f);
        }

        scaleXYAnimator = ValueAnimator.ofFloat(valueFrom, valueTo);
        scaleXYAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setScaleXY(view, (float) valueAnimator.getAnimatedValue());
            }
        });

        if (listener != null) {
            scaleXYAnimator.addListener(listener);
        }
        else {
            scaleXYAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            });
        }
        scaleXYAnimator.setInterpolator(new DecelerateInterpolator());
        scaleXYAnimator.setDuration(RetailHeroApplication.getContext().getResources().getInteger(R.integer.animTime));
        scaleXYAnimator.start();
    }

    private static ObjectAnimator scaleAnimator = null;
    public static void scaleAnimation(final View view, boolean down, final AnimatorListenerAdapter listener) {
        if (scaleAnimator != null && scaleAnimator.isRunning())
            scaleAnimator.cancel();

        if (down) {
            setScaleXY(view, 1.0f);
            scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(view,
                    PropertyValuesHolder.ofFloat("scaleX", 0f),
                    PropertyValuesHolder.ofFloat("scaleY", 0f));
        }
        else {
            setScaleXY(view, 0f);
            scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(view,
                    PropertyValuesHolder.ofFloat("scaleX", 1.0f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.0f));
        }

        if (listener != null) {
            scaleAnimator.addListener(listener);
        }
        else {
            scaleAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            });
        }
        scaleAnimator.setInterpolator(new DecelerateInterpolator());
        scaleAnimator.setDuration(RetailHeroApplication.getContext().getResources().getInteger(R.integer.animTime));
        scaleAnimator.start();
    }
}
