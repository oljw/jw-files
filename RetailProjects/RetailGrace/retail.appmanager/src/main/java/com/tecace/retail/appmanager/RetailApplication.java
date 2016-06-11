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
public class RetailApplication extends Application {
    private static final String TAG = RetailApplication.class.getSimpleName();

    private static Context mContext = null;
    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

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
    protected void setAppInfo(String pkg, String cls, String title, boolean selfFinish) {
        PreferenceUtil.getInstance().setString(getContext(), RetailAppManagerConst.PREFERENCE_APP_PACKAGE, pkg);
        PreferenceUtil.getInstance().setString(getContext(), RetailAppManagerConst.PREFERENCE_APP_CLASS, cls);
        PreferenceUtil.getInstance().setString(getContext(), RetailAppManagerConst.PREFERENCE_APP_TITLE, title);
        PreferenceUtil.getInstance().setBoolean(getContext(), RetailAppManagerConst.PREFERENCE_APP_SELF_FINISH, selfFinish);
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
