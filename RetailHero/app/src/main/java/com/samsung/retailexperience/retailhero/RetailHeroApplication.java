package com.samsung.retailexperience.retailhero;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.samsung.retailexperience.retailhero.config.ConfigProvider;
import com.samsung.retailexperience.retailhero.config.environment.EnvironmentManager;
import com.samsung.retailexperience.retailhero.config.environment.Environments;
import com.samsung.retailexperience.retailhero.config.environment.IEnvironments;
import com.samsung.retailexperience.retailhero.config.setting.ISettings;
import com.samsung.retailexperience.retailhero.config.setting.SettingsManager;
import com.samsung.retailexperience.retailhero.receiver.ScreenOffReceiver;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.FileUtil;
import com.samsung.retailexperience.retailhero.util.ResourceUtil;

import java.io.File;

/**
 * Created by icanmobile on 1/12/16.
 */
public class RetailHeroApplication extends Application implements ConfigProvider {
    public static final String TAG = RetailHeroApplication.class.getSimpleName();

    private static Context mContext = null;
    private EnvironmentManager em = null;
    private SettingsManager sm = null;
    private ResourceUtil rs = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        registerScreenOffReceiver();

        em = new EnvironmentManager(this);
        sm = new SettingsManager(this);
        rs = new ResourceUtil(this);

        // unzip files: for frame, bg, chapter directors
        FileUtil fileUtil = new FileUtil();
        fileUtil.startUnzipFiles(this, AppConsts.ZIP_ASSETS_LIST);

        Log.i(TAG, "App Target : " + em.getStringValue(Environments.FLAVOR));

        //make a directory for app
        makeAppDirectory();
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public IEnvironments getEnvironmentConfig() {
        return em;
    }

    @Override
    public ISettings getSettingsManager() {
        return sm;
    }

    @Override
    public ResourceUtil getResourceUtil() {
        return rs;
    }

    /**
     *  Register a receiver for wake up the app when the screen turned off.
     */
    private void registerScreenOffReceiver() {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        ScreenOffReceiver screenOffReceiver = new ScreenOffReceiver();
        registerReceiver(screenOffReceiver, filter);
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

}
