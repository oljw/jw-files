package com.samsung.retailexperience.retailtmo.ui.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.samsung.retailexperience.retailtmo.R;
import com.samsung.retailexperience.retailtmo.analytics.Analyst;
import com.samsung.retailexperience.retailtmo.analytics.FragmentChangeCause;
import com.samsung.retailexperience.retailtmo.animator.CircularReveal;
import com.samsung.retailexperience.retailtmo.gson.models.ArgumentsModel;
import com.samsung.retailexperience.retailtmo.gson.models.FragmentModel;
import com.samsung.retailexperience.retailtmo.ui.fragment.BaseFragment;
import com.samsung.retailexperience.retailtmo.ui.view.LockerView;
import com.samsung.retailexperience.retailtmo.util.AppConst;
import com.samsung.retailexperience.retailtmo.util.JsonUtil;
import com.samsung.retailexperience.retailtmo.video.config.ConfigProvider;

import java.util.ArrayList;

public class MainActivity extends BaseActivity
    implements LockerView.LockerViewListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private LockerView mLockerView = null;



    //JW
//    public static MainActivity myActivity;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



//        myActivity = this;


        setContentView(R.layout.activity_main);

        // LockerView for exit application
        mLockerView = (LockerView) findViewById(R.id.locker);
        mLockerView.setListener(this);

        changeFragment(mDefaultFragment,
                AppConst.TransactionDir.TRANSACTION_DIR_NONE,
                FragmentChangeCause.START_APP);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "##### onDestroy)+ ");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onNewIntent(Intent intent)
    {
        Log.d(TAG, "##### onNewIntent)+");

        super.onNewIntent(intent);
        setIntent(intent);

        changeFragment(mDefaultFragment,
                AppConst.TransactionDir.TRANSACTION_DIR_NONE,
                FragmentChangeCause.START_APP);
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
                    AppConst.TransactionDir dir = (AppConst.TransactionDir) msg.obj;
                    if (dir == null) break;
                    if (dir == AppConst.TransactionDir.TRANSACTION_DIR_ADD)
                        removeFragments();

                    mRunningCommand = false;
                }
                break;
            }
        }
    };

    public void changeFragment(String json, AppConst.TransactionDir dir, FragmentChangeCause cause) {
//        if (json.equals("UNDER_CONSTRUCTION")) {
//            Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
//            return;
//        }

        // no exit app by back key
        if (json.equals("NONE")) return;


        if (mRunningCommand) return;
        mRunningCommand = true;

        if (mUserInterActionTimer != null) {
            mIsTimerout = false;
            mUserInterActionTimer.start();
        }

        changeFragment(json, dir, cause, false);

        mHandler.sendMessageDelayed(mHandler.obtainMessage(FINISH_COMMAND_MSG, dir), getResources().getInteger(R.integer.animTime) + 100);
    }

    public void changeFragment(String json, AppConst.TransactionDir dir, FragmentChangeCause cause, boolean runningCommand) {

        //CHECK FOR MISSING JSON FILES - JW
        Log.d(TAG, "#### json file name: " + json);
        if (((ConfigProvider)getApplicationContext()).getResourceUtil().isMissingContentFile(json)) {
            return;
        }

        FragmentModel fragmentModel = JsonUtil.loadJsonModel(this, json, FragmentModel.class);
        if (fragmentModel == null) return;

        BaseFragment prevFragment = mFragment;

        try {
            Class<?> fragmentClass = Class.forName(fragmentModel.getClassName());
            mFragment = (BaseFragment) fragmentClass.newInstance();

            Bundle args = new Bundle();
            args.putSerializable(AppConst.ARGUMENTS_MODEL,
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
                                .replace(R.id.fragmentContainer, mFragment)
                                .commitAllowingStateLoss();
                    }
                    break;
                case TRANSACTION_DIR_FORWARD:
                    {
                        // custom animation start from exit to enter
                        // setCustomAnimations(enter, exit)
                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(fragmentModel.getForwardEnterAnimResId(), mFragmentModel.getForwardExitAnimResId())
                                .replace(R.id.fragmentContainer, mFragment)
                                .commitAllowingStateLoss();
                    }
                    break;
                case TRANSACTION_DIR_BACKWARD:
                    {
                        // custom animation start from exit to enter
                        // setCustomAnimations(enter, exit)
                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(fragmentModel.getBackwardEnterAnimResId(), mFragmentModel.getBackwardExitAnimResId())
                                .replace(R.id.fragmentContainer, mFragment)
                                .commitAllowingStateLoss();
                    }
                    break;
                case TRANSACTION_DIR_ADD: {
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(fragmentModel.getAddEnterAnimResId(), FragmentTransaction.TRANSIT_NONE)
                            .add(R.id.fragmentContainer, mFragment)
                            .commitAllowingStateLoss();
                    insertFragment(prevFragment);
                }
                break;
            }
        }

        Analyst.getInstance().sendFragmentChange(getFragmentName(mFragmentModel, fragmentModel), cause);

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

    public String getFragmentName(FragmentModel prevModel, FragmentModel newModel) {
        String name = null;

        if (newModel.getNameResId() == 0) return null;
        name = getString(newModel.getNameResId());

        // change fragment name from "LEGAL" to sibling fragment name.
        if (name.equals(getString(R.string.legal))) {
            if (prevModel.getSibling().equals("models/gear_vr_info.json"))
                return getString(R.string.gear_vr_legal);
            else if ( prevModel.getSibling().equals("models/smart_tv_info.json"))
                return getString(R.string.smart_tv_legal);
            else if (prevModel.getSibling().equals("models/samsung_pay_info.json"))
                return getString(R.string.samsung_pay_legal);
            else if (prevModel.getSibling().equals("models/smart_things_info.json"))
                return getString(R.string.smart_things_legal);
            else if (prevModel.getSibling().equals("models/tablet_info.json"))
                return getString(R.string.tablet_legal);
            else if (prevModel.getSibling().equals("models/smart_watch_info.json"))
                return getString(R.string.smart_watch_legal);
        }

        return name;
    }


    /*
     * Locker
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mLockerView != null && mLockerView.isShown() == false)
            mLockerView.setKeyDown(keyCode);

        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mLockerView != null && mLockerView.isShown() == false) {
            mLockerView.setKeyUp(keyCode);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void showLocker() {
        if (mLockerView.isShown()) return;
        mLockerView.show();
        CircularReveal.getInstance().runReveal(this, mLockerView, mDisplayW / 2, mDisplayH / 2, R.color.samsung_blue, R.color.samsung_blue_transparent);
    }
    public void hideLocker() {
        if (mLockerView.isShown() == false) return;
        mLockerView.hide();
        CircularReveal.getInstance().runUnreveal(this, mLockerView, R.color.samsung_blue, R.color.samsung_blue_transparent);
    }

    @Override
    public void onLockerShow() {
        showLocker();
    }

    @Override
    public void onLockerMatch(boolean bMatched) {
        hideLocker();
        if (bMatched) {
            selfFinish();
        }
        else {
            Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLockerHide() {
        hideLocker();
    }
}
