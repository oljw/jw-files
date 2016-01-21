package com.samsung.retailexperience.retailhero.view;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.util.AppConsts;

/**
 * Created by icanmobile on 1/19/16.
 */
public class TriggerMessageView extends OverlayView {
    private static final String TAG = TriggerMessageView.class.getSimpleName();

    public static TriggerMessageView sInstance = null;
    private String mMessage = null;
    private String mTriggerUIState = null;
    private TextView mMessageView = null;

    public TriggerMessageView(Context context, String message, String triggerUIState) {
        super(context, R.layout.trigger_message, false);
        mMessage = message;
        mTriggerUIState = triggerUIState;
        load();
    }

    public String getMessage() {
        return mMessage;
    }
    public void setMessage(String message) {
        mMessage = message;
    }


    public String getTriggerUIState() {
        return mTriggerUIState;
    }
    public void setTriggerUIState(String triggerUIState) {
        mTriggerUIState = triggerUIState;
    }


    @Override
    protected void onInflateView() {
        mMessageView = (TextView) this.findViewById(R.id.trigger_message);
    }

    @Override
    protected void onSetupLayoutParams(WindowManager.LayoutParams layoutParams) {
        layoutParams.x = getResources().getInteger(R.integer.triggerMessageLocationX);
        layoutParams.y = getResources().getInteger(R.integer.triggerMessageLocationY);
        layoutParams.width = getResources().getInteger(R.integer.triggerMessageWidth);
        layoutParams.height = getResources().getInteger(R.integer.triggerMessageHeight);
    }

    @Override
    protected int onLayoutGravity() {
        return Gravity.TOP | Gravity.LEFT;
    }

    @Override
    protected void refreshViews() {
        mMessageView.setText(mMessage);
    }

    @Override
    protected boolean onSingleTapped(MotionEvent e) {
        Intent intent = new Intent(AppConsts.ACTION_CHANGE_FRAGMENT);
        intent.putExtra(AppConsts.ARG_NEXT_FRAGMENT, mTriggerUIState);
        mContext.sendBroadcast(intent);
        return false;
    }
}
