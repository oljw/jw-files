package com.tecace.retail.appmanager.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tecace.retail.appmanager.R;
import com.tecace.retail.appmanager.util.RetailAppManagerConst;
import com.tecace.retail.appmanager.util.FuncUtil;
import com.tecace.retail.appmanager.util.PreferenceUtil;
import com.tecace.retail.appmanager.util.ResourceUtil;
import com.tecace.retail.appmanager.util.TimerHandler;

/**
 * Created by smheo on 9/29/2015.
 */
public class MissingContentActivity extends Activity {
    private static final String TAG = MissingContentActivity.class.getSimpleName();

    private Context mContext;
    private String mMissingFile;
    private TimerHandler mTimer;
    private static final int DEFAULT_TIME_TO_CHECK_CONTENTS = 5000;

    private Context getContext() {
        return this.mContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "##### onCreate)+ ");
        mContext = this;
        setWindowFeatures();
        screenStayOn(true);
        setContentView(R.layout.activity_missing_content);

        // set App title
        ((TextView) findViewById(R.id.titleTv))
                .setText(PreferenceUtil.getInstance().getString(mContext, RetailAppManagerConst.PREFERENCE_APP_TITLE));

        // get activity name
        String action = getIntent().getAction();
        if (action != null) {
            if (getIntent().getAction().equals(RetailAppManagerConst.ACTION_MISSING_FILE)) {
                mMissingFile = getIntent().getStringExtra(RetailAppManagerConst.WHAT_MISSING_FILE);
            }

            TextView missingTextView = (TextView) findViewById(R.id.filenameTv);
            if(missingTextView != null && mMissingFile != null) {
                missingTextView.setText(mMissingFile);
            }

            //Log.d(TAG, "Missing Activity Name : " + mContentKey);

            //[[ set timer
            mTimer = new TimerHandler();
            if (mTimer != null) {
                mTimer.setOnTimeoutListener(new TimerHandler.OnTimeoutListener() {
                    @Override
                    public void onTimeout() {
                        Log.d(TAG, "##### onTimeout)+ ");
                        if (mMissingFile == null) {
                            Log.e(TAG, "resource is null");
                            return;
                        }

                        if (ResourceUtil.getInstance().isMissingFile(mContext, ResourceUtil.getInstance().getContentFilePath(mContext, mMissingFile))) {
                            if (mTimer != null) {
                                startSearchTimer();
                                Log.d(TAG, mMissingFile + " : checking again the presence of the missing contents");
                            }
                        } else {
                            Log.d(TAG, mMissingFile + " are confirmed presence");

                            // go attractor activity
                            goToMainActivity(0, R.animator.identity_animation, true);
                        }
                    }
                });
            }
            //]] END set timer
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "##### onResume)+ ");

        if (mTimer != null) {
            startSearchTimer();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "##### onPause)+ ");
        if (mTimer != null) {
            mTimer.stop();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "##### onDestroy)+ ");
        if (mTimer != null) {
            mTimer.setOnTimeoutListener(null);
            mTimer = null;
        }
        super.onDestroy();
    }

    protected void setWindowFeatures() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void screenStayOn(boolean on) {
        if (on) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.screenBrightness = 1.0f;
            getWindow().setAttributes(lp);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_FULLSCREEN |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // go to the attractor activity
            goToMainActivity(0, R.animator.identity_animation, true);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void startSearchTimer() {
        if (mTimer != null) {
            int setTime = DEFAULT_TIME_TO_CHECK_CONTENTS;
            mTimer.start(setTime);
        }
    }

    private void goToMainActivity(int enterAnim, int exitAnim, boolean finish) {
        FuncUtil.getInstance().startApp(getContext(),
                PreferenceUtil.getInstance().getString(getContext(), RetailAppManagerConst.PREFERENCE_APP_PACKAGE),
                PreferenceUtil.getInstance().getString(getContext(), RetailAppManagerConst.PREFERENCE_APP_CLASS));
        if (finish) finish();
    }
}
