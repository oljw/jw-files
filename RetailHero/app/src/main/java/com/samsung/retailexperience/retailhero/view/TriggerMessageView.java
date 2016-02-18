package com.samsung.retailexperience.retailhero.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.util.AppConsts;

/**
 * Created by icanmobile on 1/19/16.
 */
public class TriggerMessageView extends OverlayView {
    private static final String TAG = TriggerMessageView.class.getSimpleName();

    public static TriggerMessageView sInstance = null;
    private RelativeLayout mMessageLayout = null;
    private String mMessageResId = null;
    private String mTriggerUIState = null;
    private ImageView mMessageView = null;

    public TriggerMessageView(Context context, String messageResId, String triggerUIState) {
        super(context, R.layout.trigger_message, false);
        mMessageResId = messageResId;
        mTriggerUIState = triggerUIState;
        load();
    }

    public String getMessage() {
        return mMessageResId;
    }
    public void setMessage(String messageResId) {
        mMessageResId = messageResId;
    }


    public String getTriggerUIState() {
        return mTriggerUIState;
    }
    public void setTriggerUIState(String triggerUIState) {
        mTriggerUIState = triggerUIState;
    }


    @Override
    protected void onInflateView() {
        mMessageLayout = (RelativeLayout) this.findViewById(R.id.trigger_message_layout);
        mMessageView = (ImageView) this.findViewById(R.id.trigger_message);
    }

    @Override
    protected void onSetupLayoutParams(WindowManager.LayoutParams layoutParams) {
//        layoutParams.x = getResources().getInteger(R.integer.triggerMessageLocationX);
//        layoutParams.y = getResources().getInteger(R.integer.triggerMessageLocationY);
        layoutParams.width = getResources().getInteger(R.integer.triggerMessageWidth);
        layoutParams.height = getResources().getInteger(R.integer.triggerMessageHeight);
    }

    @Override
    protected int onLayoutGravity() {
        return Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
    }

    @Override
    protected void refreshViews() {
        isSentBroadcast = false;
        mMessageView.setImageResource(getResId(mMessageResId));
        prepareAutoFadeoutHandler();
        getMessageAnimation().start();
    }

    protected boolean isSentBroadcast = false;
    @Override
    protected boolean onDoubleTapped(MotionEvent e) {
        if (isSentBroadcast) return false;
        isSentBroadcast = true;

        Intent intent = new Intent(AppConsts.ACTION_CHANGE_FRAGMENT);
        intent.putExtra(AppConsts.ARG_NEXT_FRAGMENT, mTriggerUIState);
        mContext.sendBroadcast(intent);
        return false;
    }

    @Override
    protected boolean onSingleTapped(MotionEvent e) {
        if (isSentBroadcast) return false;
        isSentBroadcast = true;

        Intent intent = new Intent(AppConsts.ACTION_CHANGE_FRAGMENT);
        intent.putExtra(AppConsts.ARG_NEXT_FRAGMENT, mTriggerUIState);
        mContext.sendBroadcast(intent);
        return false;
    }


    protected static final int AUTO_FADE_OUT_TIME = 5000;
    private Handler mAutoFadeOutHandler;
    private Runnable mAutoFadeOutRunnable;

    private AnimatorSet animReturned = null;
    public void prepareAutoFadeoutHandler() {
        mAutoFadeOutHandler = new Handler();
        mAutoFadeOutRunnable = new Runnable() {
            @Override
            public void run() {
                mAutoFadeOutHandler.removeCallbacks(mAutoFadeOutRunnable);
                getAutoFadeOutAnim().start();
            }
        };
    }
    public AnimatorSet getMessageAnimation() {
        animReturned = new AnimatorSet();

        ObjectAnimator anim1 = getAlphaAnim(mMessageLayout, 1);

        animReturned.play(anim1);

        mAutoFadeOutHandler.postDelayed(mAutoFadeOutRunnable, AUTO_FADE_OUT_TIME);
        return animReturned;
    }
    public AnimatorSet getAutoFadeOutAnim() {
        animReturned = new AnimatorSet();

        // Decision Page
        ObjectAnimator anim1 = getFadeOutAnim(mMessageLayout, 0);

        animReturned.play(anim1);
        animReturned.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                hide();
            }
        });
        return animReturned;
    }
}
