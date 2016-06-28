package com.samsung.retailexperience.retailgrace;

import android.content.Context;

import com.samsung.retailexperience.retailgrace.util.AppConst;
import com.tecace.app.manager.RetailAppApplication;

/**
 * Created by icanmobile on 5/31/16.
 */
public class RetailGraceApplication extends RetailAppApplication {
    private static final String TAG = RetailGraceApplication.class.getSimpleName();

    private static Context mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }


    @Override
    protected String getAnalyticsAppId() {
        return getString(R.string.amazon_test_app_id);
    }

    @Override
    protected String getAnalyticsCognitoId() {
        return getString(R.string.amazon_test_cognito_id);
    }

    @Override
    protected String getAppPackage() {
        return AppConst.APP_PACKAGE;
    }

    @Override
    protected String getAppClass() {
        return AppConst.APP_CLASS;
    }

    @Override
    protected String getAppTitle() {
        return AppConst.APP_TITLE;
    }

    @Override
    protected Boolean getAppSelfFinish() {
        return AppConst.APP_SELF_FINISH;
    }
}
