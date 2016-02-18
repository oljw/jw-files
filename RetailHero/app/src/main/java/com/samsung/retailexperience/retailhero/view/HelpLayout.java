package com.samsung.retailexperience.retailhero.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.samsung.retailexperience.retailhero.R;

/**
 * Created by icanmobile on 1/31/16.
 */
public class HelpLayout extends RelativeLayout {
    private static final String TAG = HelpLayout.class.getSimpleName();

    private static final int FADE_IN_TIME = 200;
    private static final int LATENCY_BETWEEN_ANIM = 300;
    private static final int AUTO_FADE_OUT_TIME = 3000;

    private Handler mAutoFadeOutHandler;
    private Runnable mAutoFadeOutRunnable;

    private View mSelf = null;
    private AnimatorSet animReturned = null;

    private FrameLayout mHelpMainMenuTap = null;
    private FrameLayout mHelpMainMenuSwipe = null;


    public HelpLayout(Context context) {
        super(context);
        init(context);
    }

    public HelpLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HelpLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        RelativeLayout parent = (RelativeLayout) inflate(getContext(), R.layout.help_layout, this);
        mSelf = parent;

        parent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (animReturned != null) {
                    animReturned.cancel();
                    animReturned = null;
                }

                hideAllHelpMessage();
                mAutoFadeOutHandler.removeCallbacks(mAutoFadeOutRunnable);
                view.setVisibility(View.GONE); // hide help view
//                actionGoingToAttractorLoop();
                return true;
            }
        });

        if (!isInEditMode())
            initHelpMessage(parent);

        prepareAutoFadeoutHandler();
    }

    private void initHelpMessage(final RelativeLayout parent) {
        mHelpMainMenuSwipe = (FrameLayout) parent.findViewById(R.id.help_mainmenu_swipe);
        mHelpMainMenuTap = (FrameLayout) parent.findViewById(R.id.help_mainmenu_tap);
    }

    public AnimatorSet getMainMenuHelp() {
        animReturned = new AnimatorSet();

        ObjectAnimator anim1 = getAlphaAnim(mHelpMainMenuSwipe, 0);
        ObjectAnimator anim2 = getAlphaAnim(mHelpMainMenuTap, 0);


        animReturned.playTogether(anim1, anim2);
        mAutoFadeOutHandler.postDelayed(mAutoFadeOutRunnable, AUTO_FADE_OUT_TIME);
        return animReturned;
    }

    public void prepareAutoFadeoutHandler() {
        mAutoFadeOutHandler = new Handler();
        mAutoFadeOutRunnable = new Runnable() {
            @Override
            public void run() {
                mAutoFadeOutHandler.removeCallbacks(mAutoFadeOutRunnable);
                getAutoFadeOutAnim().start();
                mSelf.setVisibility(View.GONE);

            }
        };
    }

    public AnimatorSet getAutoFadeOutAnim() {
        animReturned = new AnimatorSet();

        // Decision Page
        ObjectAnimator anim1 = getFadeOutAnim(mHelpMainMenuSwipe, 0);
        ObjectAnimator anim2 = getFadeOutAnim(mHelpMainMenuTap, 0);

        animReturned.playTogether(anim1, anim2);
        animReturned.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
//                actionGoingToAttractorLoop();
            }
        });
        return animReturned;
    }

    public boolean isAnimatorRunning() {
        if (animReturned == null) return false;
        return animReturned.isRunning();
    }

    public void stop() {
        Log.d(TAG, "##### stop !!!!");
        if (mAutoFadeOutHandler == null || mSelf ==  null) return;
        hideAllHelpMessage();
        mAutoFadeOutHandler.removeCallbacks(mAutoFadeOutRunnable);
        mSelf.setVisibility(View.GONE);
    }

    private ObjectAnimator getAlphaAnim(final View view, int delayCount) {
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

    private void hideAllHelpMessage() {
        mHelpMainMenuSwipe.setVisibility(View.GONE);
        mHelpMainMenuTap.setVisibility(View.GONE);
    }

//    private void actionGoingToAttractorLoop() {
//        ((MainActivity) mSelf.getContext()).startAutoResetHandler();
//    }
}