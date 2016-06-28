package com.tecace.retail.appmanager;

import android.app.Application;
import android.content.Context;

import com.tecace.retail.appmanager.util.RetailAppManagerConst;
import com.tecace.retail.appmanager.util.FileUtil;
import com.tecace.retail.appmanager.util.FontTypeface;
import com.tecace.retail.appmanager.util.PreferenceUtil;

import java.io.File;

/**
 * Created by icanmobile on 5/31/16.
 */
public abstract class RetailApplication extends Application {
    private static final String TAG = RetailApplication.class.getSimpleName();

    protected abstract String getAppPackage();
    protected abstract String getAppClass();
    protected abstract String getAppTitle();
    protected abstract Boolean getAppSelfFinish();

    private static Context mContext = null;
    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        setAppInfo();

        // unzip files: for frame, bg, chapter directors
        initCMSContents();

        // make application directory
        makeAppDirectory();

        // init typeface
        initTypeface();
    }

    public static Context getContext() {
        return mContext;
    }

    // set MainActivity Information
    private void setAppInfo() {
        PreferenceUtil.getInstance().setString(getContext(), RetailAppManagerConst.PREFERENCE_APP_PACKAGE, getAppPackage());
        PreferenceUtil.getInstance().setString(getContext(), RetailAppManagerConst.PREFERENCE_APP_CLASS, getAppClass());
        PreferenceUtil.getInstance().setString(getContext(), RetailAppManagerConst.PREFERENCE_APP_TITLE, getAppTitle());
        PreferenceUtil.getInstance().setBoolean(getContext(), RetailAppManagerConst.PREFERENCE_APP_SELF_FINISH, getAppSelfFinish());
    }


    /*
     * CMS contents
     */
    public static void initCMSContents() {
        FileUtil.getInstance().startUnzipFiles(getContext(), RetailAppManagerConst.ZIP_ASSETS_LIST);
    }

    /**
     *  we need make app directory because this directory are going to make the RM Host App
     */
    private void makeAppDirectory() {
        String extDir = this.getExternalFilesDir(null).getAbsolutePath();
        File dir  = new File(extDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /*
     * Font typeface
     */
    public void initTypeface() {
        FontTypeface.getInstance().createFonts(getContext());
    }
}
