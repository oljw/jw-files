package com.tecace.app.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.common.collect.Sets;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tecace.app.manager.analytics.Utils;
import com.tecace.retail.analyticsmanager.AnalyticsManager;
import com.tecace.retail.appmanager.RetailApplication;

import java.util.Set;

/**
 * Created by icanmobile on 6/2/16.
 */
public abstract class RetailAppApplication extends RetailApplication {
    private static final String TAG = RetailAppApplication.class.getSimpleName();

    protected abstract String getAnalyticsAppId();
    protected abstract String getAnalyticsCognitoId();

    private static Context mContext = null;
    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        //init ImageLoader
        initImageLoader();

        initAnalytics(getAnalyticsAppId(), getAnalyticsCognitoId());
    }

    public static Context getContext() {
        return mContext;
    }

    /*
     * Universal Image Loader
     */
    public static void initImageLoader() {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getContext());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    private static DisplayImageOptions options = null;
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
     * Analytics
     */
    public void initAnalytics(String amazonAppId, String amazonCognitoId) {
        String deviceId = Utils.getInstance().getDeviceId(getContext());

        AnalyticsManager.getInstance().setAppInfo(getContext(), deviceId, amazonAppId, amazonCognitoId);

        Set<String> IGNORE_SESSION_FRAGLIST = Sets.newHashSet("ATTRACTOR");
        AnalyticsManager.getInstance().setIgnoreScreens(IGNORE_SESSION_FRAGLIST);
    }
}
