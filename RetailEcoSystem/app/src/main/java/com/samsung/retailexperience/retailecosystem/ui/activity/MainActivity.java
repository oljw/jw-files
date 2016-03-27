package com.samsung.retailexperience.retailecosystem.ui.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.samsung.retailexperience.retailecosystem.R;
import com.samsung.retailexperience.retailecosystem.gson.models.FragmentModel;
import com.samsung.retailexperience.retailecosystem.ui.fragment.BaseFragment;
import com.samsung.retailexperience.retailecosystem.util.AppConst;
import com.samsung.retailexperience.retailecosystem.util.JsonUtil;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragment(mDefaultFragment, AppConst.TransactionDir.TRANSACTION_DIR_NONE);
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
        changeFragment(mDefaultFragment, AppConst.TransactionDir.TRANSACTION_DIR_NONE);
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
//                    if (dir == AppConst.TransactionDir.TRANSACTION_DIR_BACKWARD)
//                        removeFragments();

                    mRunningCommand = false;
                }
                break;
            }
        }
    };

    public void changeFragment(String json, AppConst.TransactionDir dir) {
        if (json.equals("UNDER_CONSTRUCTION")) {
            Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mRunningCommand) return;
        mRunningCommand = true;

        if (mUserInterActionTimer != null) {
            mIsTimerout = false;
            mUserInterActionTimer.start();
        }

        if (json.equals("NONE")) {
            selfFinish();
            return;
        }

        changeFragment(json, dir, false);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(FINISH_COMMAND_MSG, dir), 300+100);
    }

    public void changeFragment(String json, AppConst.TransactionDir dir, boolean runningCommand) {
        FragmentModel fragmentModel = JsonUtil.loadJsonModel(this, json, FragmentModel.class);
        if (fragmentModel == null) return;

        BaseFragment prevFragment = mFragment;

        try {
            Class<?> fragmentClass = Class.forName(fragmentModel.getClassName());
            mFragment = (BaseFragment) fragmentClass.newInstance();

            Bundle args = new Bundle();
            args.putString(AppConst.ARG_FRAGMENT_MODEL, json);
            args.putSerializable(AppConst.ARG_FRAGMENT_TRANSACTION_DIR, dir );
            if (fragmentModel.getMe().equals("models/legal.json"))
                args.putString(AppConst.ARG_LEGAL_MENU_ITEM_ACTION, mFragmentModel.getSibling());
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
                case TRANSACTION_DIR_FORWARD: {
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
//                            .add(R.id.fragmentContainer, mFragment)
                            .replace(R.id.fragmentContainer, mFragment)
                            .commitAllowingStateLoss();
//                    insertFragment(prevFragment);
                }
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
        for(int f=0; f<mFragments.size(); f++) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(mFragments.get(f));
            ft.commitAllowingStateLoss();
            mFragments.remove(f);
        }
    }
}
