package com.tecace.retail.appmanager.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.tecace.retail.appmanager.R;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by icanmobile on 4/6/16.
 */
public class CircularReveal {
    private static final String TAG = CircularReveal.class.getSimpleName();

    private static CircularReveal sInstance = null;
    public static CircularReveal getInstance() {
        if (sInstance == null)
            sInstance = new CircularReveal();
        return sInstance;
    }

    private int cx = 0;
    private int cy = 0;

    public void setPivots(int cx, int cy) {
        this.cx = cx;
        this.cy = cy;
    }
    public int getPivotX() {
        return cx;
    }
    public int getPivotY() {
        return cy;
    }

    public Animator getReveal(final Context context, final View view, int cx, int cy, final int fromColorResId, final int toColorResId) {
        setPivots(cx, cy);

        int radius = getEnclosingCircleRadius(view, getPivotX() , getPivotY());
        Animator anim = ViewAnimationUtils.createCircularReveal(view, getPivotX(), getPivotY(), 0, radius);
        anim.setDuration(context.getResources().getInteger(R.integer.animTime));
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
                view.setBackgroundResource(fromColorResId);
                runColorAnim(context, view, fromColorResId, toColorResId);
            }
        });
        return anim;
    }

    public void runReveal(final Context context, final View view, int cx, int cy, final int fromColorResId, final int toColorResId) {
        setPivots(cx, cy);

        int radius = getEnclosingCircleRadius(view, getPivotX() , getPivotY());
        Animator anim = ViewAnimationUtils.createCircularReveal(view, getPivotX(), getPivotY(), 0, radius);
        anim.setDuration(context.getResources().getInteger(R.integer.animTime));
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
                view.setBackgroundResource(fromColorResId);
                runColorAnim(context, view, fromColorResId, toColorResId);
            }
        });
        anim.start();
    }

    public Animator getUnreveal(final Context context, final View view, final int fromColorResId, final int toColorResId) {
        int radius = getEnclosingCircleRadius(view, getPivotX(), getPivotY());
        final Animator anim = ViewAnimationUtils.createCircularReveal(view, getPivotX(), getPivotY(), radius, 0);
        anim.setDuration(context.getResources().getInteger(R.integer.animTime));
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                runColorAnim(context, view, fromColorResId, toColorResId);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                view.setVisibility(View.GONE);
            }
        });
        return anim;
    }

    public void runUnreveal(final Context context, final View view, final int fromColorResId, final int toColorResId) {
        int radius = getEnclosingCircleRadius(view, getPivotX(), getPivotY());
        Animator anim = ViewAnimationUtils.createCircularReveal(view, getPivotX(), getPivotY(), radius, 0);
        anim.setDuration(context.getResources().getInteger(R.integer.animTime));
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                runColorAnim(context, view, fromColorResId, toColorResId);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                view.setVisibility(View.GONE);
            }
        });
        anim.start();
    }

    private int getEnclosingCircleRadius(View v, int cx, int cy) {
        int realCenterX = cx + v.getLeft();
        int realCenterY = cy + v.getTop();
        int distanceTopLeft = (int) Math.hypot(realCenterX - v.getLeft(), realCenterY - v.getTop());
        int distanceTopRight = (int) Math.hypot(v.getRight() - realCenterX, realCenterY - v.getTop());
        int distanceBottomLeft = (int) Math.hypot(realCenterX - v.getLeft(), v.getBottom() - realCenterY);
        int distanceBottomRight = (int) Math.hypot(v.getRight() - realCenterX, v.getBottom() - realCenterY);

        Integer[] distances = new Integer[]{distanceTopLeft, distanceTopRight, distanceBottomLeft,
                distanceBottomRight};
        return Collections.max(Arrays.asList(distances));
    }

    public void runColorAnim(Context context, final View view, int color1Id, int color2Id) {
        int colorFrom = context.getResources().getColor(color1Id);
        int colorTo = context.getResources().getColor(color2Id);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(context.getResources().getInteger(R.integer.animTime)); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }
}
