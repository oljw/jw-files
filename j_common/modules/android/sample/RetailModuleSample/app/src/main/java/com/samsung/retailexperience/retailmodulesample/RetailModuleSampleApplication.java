package com.samsung.retailexperience.retailmodulesample;

import android.content.Context;

import com.samsung.retailexperience.retailmodulesample.util.AppConst;
import com.tecace.app.manager.RetailAppApplication;

/**
 * Created by icanmobile on 6/3/16.
 */
public class RetailModuleSampleApplication extends RetailAppApplication {
    private static final String TAG = RetailModuleSampleApplication.class.getSimpleName();

    private static Context mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        setAppInfo(AppConst.APP_PACKAGE, AppConst.APP_CLASS, AppConst.APP_TITLE, AppConst.APP_SELF_FINISH,
                getString(R.string.amazon_test_app_id),     //Amazon Mobile Analytics App ID for test
                getString(R.string.amazon_test_cognito_id));//Amazon Cognito Identity Pool ID for test
//                getString(R.string.amazon_app_id),          //Amazon Mobile Analytics App ID
//                getString(R.string.amazon_cognito_id));     //Amazon Cognito Identity Pool ID
    }

    public static Context getContext() {
        return mContext;
    }
}
