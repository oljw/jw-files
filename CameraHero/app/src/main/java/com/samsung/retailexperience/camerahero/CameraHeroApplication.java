package com.samsung.retailexperience.camerahero;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by icanmobile on 1/14/16.
 */
public class CameraHeroApplication extends Application {
    private static final String TAG = CameraHeroApplication.class.getSimpleName();

    private static Context mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    public static Resources getAppResources() {
        return mContext.getResources();
    }
}
