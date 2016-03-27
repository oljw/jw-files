package com.samsung.retailexperience.retailecosystem.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.samsung.retailexperience.retailecosystem.R;
import com.samsung.retailexperience.retailecosystem.gson.models.FragmentModel;
import com.samsung.retailexperience.retailecosystem.ui.fragment.BaseFragment;
import com.samsung.retailexperience.retailecosystem.ui.fragment.DecisionFragment;
import com.samsung.retailexperience.retailecosystem.ui.fragment.LegalFragment;
import com.samsung.retailexperience.retailecosystem.ui.fragment.ListFragment;
import com.samsung.retailexperience.retailecosystem.ui.fragment.MenuFragment;
import com.samsung.retailexperience.retailecosystem.util.AppConst;
import com.samsung.retailexperience.retailecosystem.util.TopExceptionHandler;
import com.samsung.retailexperience.retailecosystem.video.config.ConfigProvider;
import com.samsung.retailexperience.retailecosystem.video.config.environment.Environments;
import com.samsung.retailexperience.retailecosystem.video.config.environment.IEnvironments;
import com.samsung.retailexperience.retailecosystem.video.config.setting.ISettings;
import com.samsung.retailexperience.retailecosystem.video.ui.activity.MissingContentActivity;
import com.samsung.retailexperience.retailecosystem.video.util.ResourceUtil;
import com.samsung.retailexperience.retailecosystem.video.util.TimerHandler;

/**
 * Created by icanmobile on 3/1/16.
 */
public class BaseActivity extends Activity implements ResourceUtil.MissingResourceListener {
    private static final String TAG = BaseActivity.class.getSimpleName();

    protected int mDisplayW = 0;
    protected int mDisplayH = 0;

    protected IEnvironments gEnvMgr;
    protected ISettings gSetMgr;
    protected ResourceUtil gResMgr;

    protected String mDefaultFragment = "models/attractor.json";
    protected BaseFragment mFragment = null;
    protected FragmentModel mFragmentModel = null;

    protected TimerHandler mUserInterActionTimer = null;
    protected boolean mIsTimerout = false;
    protected static final int NO_INTERACTION_TIMEOUT = 30000;
    protected static final Class[] NO_INTERACTION_DETECT_CLASS = {
            DecisionFragment.class,
            LegalFragment.class,
            MenuFragment.class,
            ListFragment.class
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(null));

        getGlobalVariables();
        setWindowFeatures();
        getDisplaySize();
        screenStayOn(true);
        setMissingContentListener();
        setUserInterActionTimer();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "##### onStart)+ ");

        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "##### onStop)+ ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "##### onDestroy)+ ");

        // for no interaction
        // clear timer for the no interaction
        if (mUserInterActionTimer != null) {
            mUserInterActionTimer.stop();
            mUserInterActionTimer.setOnTimeoutListener(null);
            mUserInterActionTimer = null;
        }

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "##### onResume)+ ");

        // for no interaction
        if (mUserInterActionTimer != null) {
            //Log.d(TAG, "@@@ : restart a timer for the no interaction detection ");
            mIsTimerout = false;
            mUserInterActionTimer.start();
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "##### onPause)+ ");

        // for no interaction : stop a timer
        if (mUserInterActionTimer != null) {
            //Log.d(TAG, "@@@ : stop timer");
            mUserInterActionTimer.stop();
        }

        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "##### onBackPressed)+ ");
        super.onBackPressed();
    }

    @Override
    public void onUserInteraction() {
        // for no interaction
        if (mFragment != null) {
            if (mIsTimerout == false) {
                Log.d(TAG, "@@@ User Interaction");
                mUserInterActionTimer.start();
            }
        }
        super.onUserInteraction();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mFragment != null)
                mFragment.onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

    protected void getDisplaySize() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        mDisplayW = metrics.widthPixels;
        mDisplayH = metrics.heightPixels;
    }

    public void selfFinish() {
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    /*
     * Activity Environment
     */
    private void getGlobalVariables() {
        gEnvMgr = ((ConfigProvider)getApplication()).getEnvironmentConfig();
        gSetMgr = ((ConfigProvider)getApplication()).getSettingsManager();
        gResMgr = ((ConfigProvider)getApplication()).getResourceUtil();

        Log.i(TAG, "Activity Target : " + gEnvMgr.getStringValue(Environments.FLAVOR));
    }

    private void setMissingContentListener() {
        if (gResMgr != null) {
            gResMgr.setMissingResourceListener(this);
        }
    }

    @Override
    public void onMissingResource(String missingfile) {
        Log.d(TAG, "onMissingResource : " + missingfile);

        /**
         *  go to a missing page (activity? or fragment?)
         */
        Intent intent = new Intent(this, MissingContentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(AppConst.ACTION_MISSING_FILE);
        intent.putExtra(AppConst.WHAT_MISSING_FILE, missingfile);
        startActivity(intent);
        overridePendingTransition(0, R.animator.identity_animation);
        finish();

    }

    private void setUserInterActionTimer() {
        if (mUserInterActionTimer == null) {
            mUserInterActionTimer = new TimerHandler();
            mUserInterActionTimer.setTimeout(NO_INTERACTION_TIMEOUT);
            mUserInterActionTimer.setOnTimeoutListener(new TimerHandler.OnTimeoutListener() {
                @Override
                public void onTimeout() {
                    Log.d(TAG, "##### setOnTimeoutListener)+");
                    if (mFragment != null) {
                        Log.d(TAG, "@@@ Timeout :" + mFragment.getClass().getSimpleName());

                        // searching the no interaction class
                        for (Class objClass : NO_INTERACTION_DETECT_CLASS) {
                            if (objClass.equals(mFragment.getClass())) {
                                Log.d(TAG, "@@@ user interaction didn't occur for " + NO_INTERACTION_TIMEOUT + "ms");
                                    mFragment.changeFragment(mDefaultFragment,
                                            AppConst.TransactionDir.TRANSACTION_DIR_NONE);
                            }
                        }
                        mIsTimerout = true;
                    }
                }
            });
        }
    }
}
