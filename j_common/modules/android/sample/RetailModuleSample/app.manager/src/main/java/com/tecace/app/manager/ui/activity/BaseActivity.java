package com.tecace.app.manager.ui.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.tecace.app.manager.RetailAppApplication;
import com.tecace.app.manager.analytics.FragmentChangeCause;
import com.tecace.app.manager.gson.model.ArgumentsModel;
import com.tecace.app.manager.gson.model.FragmentModel;
import com.tecace.app.manager.ui.fragment.BaseFragment;
import com.tecace.app.manager.util.AppManagerConst;
import com.tecace.retail.analyticsmanager.AnalyticsManager;
import com.tecace.retail.appmanager.ui.activity.RetailActivity;
import com.tecace.retail.appmanager.util.JsonUtil;
import com.tecace.retail.appmanager.util.PreferenceUtil;
import com.tecace.retail.appmanager.util.TimerHandler;
import com.tecace.retail.appmanager.util.TopExceptionHandler;
import com.tecace.retail.appmanager.util.RetailAppManagerConst;

import java.util.ArrayList;

/**
 * Created by icanmobile on 3/1/16.
 */

public abstract class BaseActivity extends RetailActivity
    implements TopExceptionHandler.TopExceptionListener {
    private static final String TAG = BaseActivity.class.getSimpleName();

    protected String mDefaultFragment = "models/attractor.json";
    protected BaseFragment mFragment = null;
    protected FragmentModel mFragmentModel = null;

    protected TimerHandler mUserInterActionTimer = null;
    protected boolean mIsTimerout = false;
    protected static final int NO_INTERACTION_TIMEOUT = 30000;
    protected static Class[] NO_INTERACTION_DETECT_CLASS = null;
    protected void setNoInteractionDetectClass(Class[] classes) {
        NO_INTERACTION_DETECT_CLASS = classes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this, this));
        setUserInterActionTimer();
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
        RetailAppApplication.initCMSContents();

        AnalyticsManager.getInstance().resumeApp(mFragmentModel != null ? getString(mFragmentModel.getNameResId()) : null);

        // for no interaction
        if (mUserInterActionTimer != null) {
            //Log.d(TAG, "@@@ : restart a timer for the no interaction detection ");
            mIsTimerout = false;
            mUserInterActionTimer.setTimeout(NO_INTERACTION_TIMEOUT);
            mUserInterActionTimer.start();
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "##### onPause)+ ");

        AnalyticsManager.getInstance().pauseApp();

        // for no interaction : stop a timer
        if (mUserInterActionTimer != null) {
            mUserInterActionTimer.stop();
        }

        super.onPause();
    }

    @Override
    public void onUserInteraction() {
        // for no interaction
        if (mFragment != null && mIsTimerout == false) {
            Log.d(TAG, "@@@ User Interaction");
            mUserInterActionTimer.setTimeout(NO_INTERACTION_TIMEOUT);
            mUserInterActionTimer.start();
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
    public void onUnCaughtException() {
        Log.d(TAG, "##### onUnCaughtException)+ ");
        AnalyticsManager.getInstance().forcePauseAnalytics();
    }

    private void setUserInterActionTimer() {
        Log.d(TAG, "##### setUserInterActionTimer)+ ");
        if (mUserInterActionTimer == null) {
            mUserInterActionTimer = new TimerHandler();
            mUserInterActionTimer.setTimeout(NO_INTERACTION_TIMEOUT);
            mUserInterActionTimer.setOnTimeoutListener(new TimerHandler.OnTimeoutListener() {
                @Override
                public void onTimeout() {
                    Log.d(TAG, "##### setOnTimeoutListener : onTimeout)+ ");
                    if (mFragment != null) {
                        Log.d(TAG, "##### Timeout :" + mFragment.getClass().getSimpleName());

                        Log.d(TAG, "##### Timeout : NO_INTERACTION_DETECT_CLASS.length = " + NO_INTERACTION_DETECT_CLASS.length);

                        if (NO_INTERACTION_DETECT_CLASS == null || NO_INTERACTION_DETECT_CLASS.length == 0) return;

                        // searching the no interaction class
                        for (Class objClass : NO_INTERACTION_DETECT_CLASS) {
                            if (objClass.equals(mFragment.getClass())) {
                                Log.d(TAG, "@@@ user interaction didn't occur for " + NO_INTERACTION_TIMEOUT + "ms");
                                    mFragment.changeFragment(mDefaultFragment,
                                            AppManagerConst.TransactionDir.TRANSACTION_DIR_NONE,
                                            FragmentChangeCause.TIME_OUT);
                            }
                        }
                        mIsTimerout = true;
                    }
                }
            });
        }
    }

    /*
 * Control Commands one by one
 */
    private boolean mRunningCommand = false;
    private final int FINISH_COMMAND_MSG = 1;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FINISH_COMMAND_MSG:
                {
                    AppManagerConst.TransactionDir dir = (AppManagerConst.TransactionDir) msg.obj;
                    if (dir == null) break;
                    if (dir == AppManagerConst.TransactionDir.TRANSACTION_DIR_ADD)
                        removeFragments();

                    mRunningCommand = false;
                }
                break;
            }
        }
    };

    public void changeFragment(String json, AppManagerConst.TransactionDir dir, FragmentChangeCause cause) {
        if (json.equals("UNDER_CONSTRUCTION")) {
            Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
            return;
        }

        // no exit app by back key
        if (json.equals("NONE") && PreferenceUtil.getInstance().getBoolean(this, RetailAppManagerConst.PREFERENCE_APP_SELF_FINISH)) {
            selfFinish();
            return;
        }


        if (mRunningCommand) return;
        mRunningCommand = true;

        if (mUserInterActionTimer != null) {
            mIsTimerout = false;
            mUserInterActionTimer.start();
        }

        changeFragment(json, dir, cause, false);

        mHandler.sendMessageDelayed(mHandler.obtainMessage(FINISH_COMMAND_MSG, dir), getResources().getInteger(com.tecace.app.manager.R.integer.animTime) + 100);
    }

    public void changeFragment(String json, AppManagerConst.TransactionDir dir, FragmentChangeCause cause, boolean runningCommand) {
        Log.d(TAG, "##### changeFragment)+ json = " + json);

        FragmentModel fragmentModel = JsonUtil.getInstance().loadJsonModel(this, json, FragmentModel.class);

        Log.d(TAG, "##### changeFragment : fragmentModel = " + fragmentModel);
        if (fragmentModel == null) return;

        BaseFragment prevFragment = mFragment;

        try {
            Class<?> fragmentClass = Class.forName(fragmentModel.getClassName());
            mFragment = (BaseFragment) fragmentClass.newInstance();

            Bundle args = new Bundle();
            args.putSerializable(AppManagerConst.ARGUMENTS_MODEL,
                    new ArgumentsModel(json, dir.name(),
                            (mFragmentModel !=null ? mFragmentModel.getSibling() : null)));
            mFragment.setArguments(args);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (mFragmentModel == null) mFragmentModel = fragmentModel;
        if (mFragment != null) {
            switch(dir) {
                case TRANSACTION_DIR_NONE:
                {
                    getFragmentManager().beginTransaction()
                            .replace(com.tecace.app.manager.R.id.fragmentContainer, mFragment)
                            .commitAllowingStateLoss();
                }
                break;
                case TRANSACTION_DIR_FORWARD:
                {
                    // custom animation start from exit to enter
                    // setCustomAnimations(enter, exit)
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(fragmentModel.getForwardEnterAnimResId(), mFragmentModel.getForwardExitAnimResId())
                            .replace(com.tecace.app.manager.R.id.fragmentContainer, mFragment)
                            .commitAllowingStateLoss();
                }
                break;
                case TRANSACTION_DIR_BACKWARD:
                {
                    // custom animation start from exit to enter
                    // setCustomAnimations(enter, exit)
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(fragmentModel.getBackwardEnterAnimResId(), mFragmentModel.getBackwardExitAnimResId())
                            .replace(com.tecace.app.manager.R.id.fragmentContainer, mFragment)
                            .commitAllowingStateLoss();
                }
                break;
                case TRANSACTION_DIR_ADD: {
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(fragmentModel.getAddEnterAnimResId(), FragmentTransaction.TRANSIT_NONE)
                            .add(com.tecace.app.manager.R.id.fragmentContainer, mFragment)
                            .commitAllowingStateLoss();
                    insertFragment(prevFragment);
                }
                break;
            }
        }

        AnalyticsManager.getInstance().notifyScreenChanged(cause.name(), getFragmentName(mFragmentModel, fragmentModel));

        mFragmentModel = fragmentModel;
    }

    protected ArrayList<BaseFragment> mFragments = new ArrayList<BaseFragment>();
    public void insertFragment(BaseFragment fragment) {
        if (fragment == null) return;
        mFragments.add(fragment);
    }
    public void removeFragments() {
        for(int f=0; f<mFragments.size(); f++) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(mFragments.get(f));
            ft.commitAllowingStateLoss();
            mFragments.remove(f);
        }
    }

    public abstract String getFragmentName(FragmentModel prevModel, FragmentModel newModel);
//    public String getFragmentName(FragmentModel prevModel, FragmentModel newModel) {
//        String name = null;
//
//        if (newModel.getNameResId() == 0) return null;
//        name = getString(newModel.getNameResId());
//
//        // change fragment name from "LEGAL" to sibling fragment name.
////        if (name.equals(getString(R.string.legal))) {
////            if (prevModel.getSibling().equals("models/gear_vr_info.json"))
////                return getString(R.string.gear_vr_legal);
////            else if ( prevModel.getSibling().equals("models/smart_tv_info.json"))
////                return getString(R.string.smart_tv_legal);
////            else if (prevModel.getSibling().equals("models/samsung_pay_info.json"))
////                return getString(R.string.samsung_pay_legal);
////            else if (prevModel.getSibling().equals("models/smart_things_info.json"))
////                return getString(R.string.smart_things_legal);
////            else if (prevModel.getSibling().equals("models/tablet_info.json"))
////                return getString(R.string.tablet_legal);
////            else if (prevModel.getSibling().equals("models/smart_watch_info.json"))
////                return getString(R.string.smart_watch_legal);
////        }
//
//        return name;
//    }
}
