package com.developer.jw.ecosystemcopy.ui.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.developer.jw.ecosystemcopy.R;
import com.developer.jw.ecosystemcopy.gson.models.ArgumentsModel;
import com.developer.jw.ecosystemcopy.gson.models.FragmentModel;
import com.developer.jw.ecosystemcopy.ui.fragment.BaseFragment;
import com.developer.jw.ecosystemcopy.util.AppConst;
import com.developer.jw.ecosystemcopy.util.JsonUtil;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    //TODO LockerView not implemented here.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeFragment(mDefaultFragment,
                AppConst.TransactionDir.TRANSACTION_DIR_NONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d(TAG, "#### onNewIntent");

        super.onNewIntent(intent);
        setIntent(intent);

        changeFragment(mDefaultFragment,
                AppConst.TransactionDir.TRANSACTION_DIR_NONE);
    }

    /*
     * Control Commands one by One
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

    public void changeFragment(String json, AppConst.TransactionDir dir) {
        //TODO Skipped code about locker & cause parameter skipped
        if (mRunningCommand) return;
        mRunningCommand = true;

        if (mUserInterActionTimer != null) {
            mIsTimeOut = false;
            mUserInterActionTimer.start();
        }

        changeFragment(json, dir, false);

        mHandler.sendMessageDelayed(mHandler.obtainMessage(FINISH_COMMAND_MSG, dir), getResources().getInteger(R.integer.animTime) + 100);
    }

    public void changeFragment(String json, AppConst.TransactionDir dir, boolean runningCommand) {
        //TODO Skipped code about locker & cause parameter skipped
        FragmentModel fragmentModel = JsonUtil.loadJsonModel(this, json, FragmentModel.class);
        if (fragmentModel == null) return;

        BaseFragment prevFragment = mFragment;

        try {
            Class<?> fragmentClass = Class.forName(fragmentModel.getClassName());
            mFragment = (BaseFragment) fragmentClass.newInstance();

            Bundle args = new Bundle();
            args.putSerializable(AppConst.ARGUMENTS_MODEL,
                    new ArgumentsModel(json, dir.name(),
                            (mFragmentModel != null ? mFragmentModel.getSibling() : null)));
            mFragment.setArguments(args);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (mFragmentModel == null) mFragmentModel = fragmentModel;
        if (mFragment != null) {
            switch(dir) {
                case TRANSACTION_DIR_NONE: {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, mFragment)
                            .commitAllowingStateLoss();
                }
                break;

                case TRANSACTION_DIR_FORWARD: {
                    /*custom animation start from exit to enter
                    * setCustomAnimations(enter, exit)
                    * */
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(fragmentModel.getForwardEnterAnimResId(), mFragmentModel.getForwardExitAnimResId())
                            .replace(R.id.fragmentContainer, mFragment)
                            .commitAllowingStateLoss();
                }
                break;

                case TRANSACTION_DIR_BACKWARD: {
                    /*custom animation start from exit to enter
                    * setCustomAnimations(enter, exit)
                    * */
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
        mFragmentModel = fragmentModel;
    }

    protected ArrayList<BaseFragment> mFragments = new ArrayList<BaseFragment>();
    public void insertFragment(BaseFragment fragment) {
        if (fragment == null) return;
        mFragments.add(fragment);
    }

    public void removeFragments() {
        for(int f = 0; f < mFragments.size(); f++) {
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

    /*TODO Locker is not implemented */
}
