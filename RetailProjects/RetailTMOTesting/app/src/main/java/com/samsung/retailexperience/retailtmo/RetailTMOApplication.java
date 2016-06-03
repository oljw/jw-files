package com.samsung.retailexperience.retailtmo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.samsung.retailexperience.retailtmo.analytics.Analyst;
import com.samsung.retailexperience.retailtmo.receiver.ScreenOffReceiver;
import com.samsung.retailexperience.retailtmo.util.AppConst;
import com.samsung.retailexperience.retailtmo.util.FileUtil;
import com.samsung.retailexperience.retailtmo.util.FontTypeface;
import com.samsung.retailexperience.retailtmo.video.config.ConfigProvider;
import com.samsung.retailexperience.retailtmo.video.config.environment.EnvironmentManager;
import com.samsung.retailexperience.retailtmo.video.config.environment.Environments;
import com.samsung.retailexperience.retailtmo.video.config.environment.IEnvironments;
import com.samsung.retailexperience.retailtmo.video.config.setting.ISettings;
import com.samsung.retailexperience.retailtmo.video.config.setting.SettingsManager;
import com.samsung.retailexperience.retailtmo.video.util.ResourceUtil;

import java.io.File;

/**
 * Created by icanmobile on 3/1/16.
 */
public class RetailTMOApplication extends Application implements ConfigProvider {
    private static final String TAG =  RetailTMOApplication.class.getSimpleName();

    private static Context mContext = null;
    private EnvironmentManager em = null;
    private SettingsManager sm = null;
    private ResourceUtil rs = null;
    private static DisplayImageOptions options = null;
    private FontTypeface mFontTypeface = null;

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
        fileUtil.startUnzipFiles(this, AppConst.ZIP_ASSETS_LIST);

        Log.i(TAG, "App Target : " + em.getStringValue(Environments.FLAVOR));

        //make a directory for app
        makeAppDirectory();

        initImageLoader(mContext);

        initTypeface();

        //init Analyst
        Analyst.getInstance().init(this);
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
        Log.d(TAG, "##### registerScreenOffReceiver)+ ");
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

    /*
     * Universal Image Loader
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public static DisplayImageOptions getDisplayImageOption() {
        if (null == options) {
            BitmapFactory.Options resizeOptions = new BitmapFactory.Options();
            resizeOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            resizeOptions.inDither = true;
            resizeOptions.inSampleSize = 1;
            options = new DisplayImageOptions.Builder()
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .cacheOnDisk(true)
                    .cacheInMemory(false)
                    .considerExifParams(true)
                    .decodingOptions(resizeOptions)
                    .build();
        }
        return options;
    }

    /*
     * Font typeface
     */
    public void initTypeface() {
        FontTypeface.getInstance().createFonts(getContext());
    }
}
