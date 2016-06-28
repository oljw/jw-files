package com.samsung.retailexperience.retailgrace.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.samsung.retailexperience.retailgrace.R;
import com.samsung.retailexperience.retailgrace.gson.model.ArgsModel;
import com.samsung.retailexperience.retailgrace.ui.fragment.MenuFragment;
import com.tecace.app.manager.analytics.FragmentChangeCause;
import com.tecace.app.manager.gson.model.ArgumentsModel;
import com.tecace.app.manager.gson.model.FragmentModel;
import com.tecace.app.manager.ui.activity.BaseActivity;
import com.tecace.app.manager.util.AppManagerConst;
import com.tecace.retail.analyticsmanager.AnalyticsManager;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    protected static Class[] NO_INTERACTION_DETECT_CLASS = {
            MenuFragment.class
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setNoInteractionDetectClass(NO_INTERACTION_DETECT_CLASS);

        changeFragment(mDefaultFragment,
                AppManagerConst.TransactionDir.TRANSACTION_DIR_NONE,
                FragmentChangeCause.START_APP);
    }

    @Override
    public void onNewIntent(Intent intent)
    {
        Log.d(TAG, "##### onNewIntent)+");

        super.onNewIntent(intent);
        setIntent(intent);

        setNoInteractionDetectClass(NO_INTERACTION_DETECT_CLASS);

        AnalyticsManager.getInstance().setScreenName("NATIVE");

        changeFragment(mDefaultFragment,
                AppManagerConst.TransactionDir.TRANSACTION_DIR_NONE,
                FragmentChangeCause.START_APP);
    }

    @Override
    public ArgumentsModel getArgsModel(FragmentModel fragmentModel) {
        if (fragmentModel == null) return null;
        return new ArgsModel(fragmentModel.getMe(),fragmentModel.getSibling());
    }

    @Override
    public String getFragmentName(FragmentModel prevModel, FragmentModel newModel) {
        return null;
    }

    @Override
    public void serviceConnected() {
        //we know that service is connected
    }
}
