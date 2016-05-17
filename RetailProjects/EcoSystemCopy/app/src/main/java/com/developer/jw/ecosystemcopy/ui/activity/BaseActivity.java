package com.developer.jw.ecosystemcopy.ui.activity;

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

import com.developer.jw.ecosystemcopy.R;
import com.developer.jw.ecosystemcopy.gson.models.FragmentModel;
import com.developer.jw.ecosystemcopy.ui.fragment.BaseFragment;
import com.developer.jw.ecosystemcopy.ui.fragment.DecisionFragment;
import com.developer.jw.ecosystemcopy.ui.fragment.LegalFragment;
import com.developer.jw.ecosystemcopy.ui.fragment.ListFragment;
import com.developer.jw.ecosystemcopy.ui.fragment.MenuFragment;
import com.developer.jw.ecosystemcopy.util.AppConst;
import com.developer.jw.ecosystemcopy.util.TopExceptionHandler;
import com.developer.jw.ecosystemcopy.video.config.ConfigProvider;
import com.developer.jw.ecosystemcopy.video.config.environment.Environments;
import com.developer.jw.ecosystemcopy.video.config.environment.IEnvironments;
import com.developer.jw.ecosystemcopy.video.config.setting.ISettings;
import com.developer.jw.ecosystemcopy.video.ui.activity.MissingContentActivity;
import com.developer.jw.ecosystemcopy.video.util.ResourceUtil;
import com.developer.jw.ecosystemcopy.video.util.TimerHandler;

/**
 * Created by JW on 2016-04-21.
 */
public class BaseActivity extends Activity implements ResourceUtil.MissingResourceListener{
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
    protected boolean mIsTimeOut = false;
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
    public void onStart() {
        Log.d(TAG, "#### onStart called");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "#### onStop called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "#### onDestroy called");

        screenStayOn(false);

        /* For no interaction
         * clear timer for if there's no interaction
         */
        if (mUserInterActionTimer != null) {
            mUserInterActionTimer.stop();
            mUserInterActionTimer.setOnTimeoutListener(null);
            mUserInterActionTimer = null;
        }

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "#### onResume called");

        //For no interaction : stop the timer
        if (mUserInterActionTimer != null) {
            mIsTimeOut = false;
            mUserInterActionTimer.setTimeout(NO_INTERACTION_TIMEOUT);
            mUserInterActionTimer.start();
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "#### onPause called");

        //for no interaction : stop the timer
        if (mUserInterActionTimer != null) {
            mUserInterActionTimer.stop();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed called");
        super.onBackPressed();
    }

    @Override
    public void onUserInteraction() {
        //for no interaction
        if (mFragment != null) {
            if (!mIsTimeOut) {
                Log.d(TAG, "#### User Interaction");
                mUserInterActionTimer.setTimeout(NO_INTERACTION_TIMEOUT);
                mUserInterActionTimer.start();
            }
        }
        super.onUserInteraction();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mFragment != null) {
                mFragment.onBackPressed();
                return true;
            }
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
    }

    /*
     * Activity Environment
     */
    private void getGlobalVariables() {
        gEnvMgr = ((ConfigProvider)getApplication()).getEnvironmentConfig();
        gSetMgr = ((ConfigProvider)getApplication()).getSettingsManager();
        gResMgr = ((ConfigProvider)getApplication()).getResourceUtil();

//        Log.i(TAG, "Activity Target : " + gEnvMgr.getStringValue(Environments.FLAVOR));
    }

    private void setMissingContentListener() {
        if (gResMgr != null) {
            gResMgr.setMissingResourceListener(this);
        }
    }

    @Override
    public void onMissingResource(String missingfile) {
        Log.d(TAG, "onMissingResources: " + missingfile);
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
                Log.d(TAG, "#### setOnTimeoutListener");
                if (mFragment != null) {
                    Log.d(TAG, "#### Timeout :" + mFragment.getClass().getSimpleName());

                    //Searching for the no interaction class
                    for (Class objClass : NO_INTERACTION_DETECT_CLASS) {
                        if (objClass.equals(mFragment.getClass())) {
                            Log.d(TAG, "#### user interaction didn't occur for " + NO_INTERACTION_TIMEOUT + "ms");
                            mFragment.changeFragment(mDefaultFragment,
                                    AppConst.TransactionDir.TRANSACTION_DIR_NONE);
                        }
                    }
                    mIsTimeOut = true;
                }
            }
            });
        }
    }
}
