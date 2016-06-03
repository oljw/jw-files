package jw.developer.com.camera2project;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.common.collect.Sets;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.samsung.retailexperience.retailecosystem.analytics.Utils;
import com.samsung.retailexperience.retailecosystem.receiver.ScreenOffReceiver;
import com.samsung.retailexperience.retailecosystem.util.AppConst;
import com.tecace.retail.analyticsmanager.AnalyticsManager;
import com.tecace.retail.appmanager.config.environment.EnvironmentManager;
import com.tecace.retail.appmanager.config.setting.SettingsManager;
import com.tecace.retail.appmanager.util.FileUtil;
import com.tecace.retail.appmanager.util.FontTypeface;
import com.tecace.retail.appmanager.util.ResourceUtil;

import java.io.File;
import java.util.Set;

/**
 * Created by icanmobile on 3/1/16.
 */
//public class RetailEcoSystemApplication extends Application implements ConfigProvider {
public class RetailEcoSystemApplication extends Application {
    private static final String TAG = RetailEcoSystemApplication.class.getSimpleName();

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

        // unzip files: for frame, bg, chapter directors
        initCMSContents();

        //make a directory for app
        makeAppDirectory();

        initImageLoader(mContext);

        initTypeface();

        //init Analyst
        initAnalytics();
    }

    public static Context getContext() {
        return mContext;
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

    /*
     * CMS contents
     */
    public static void initCMSContents() {
        FileUtil.getInstance().startUnzipFiles(getContext(), AppConst.ZIP_ASSETS_LIST);
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

    /*
     * Analytics
     */
    public void initAnalytics() {
        String deviceId = Utils.getInstance().getDeviceId(mContext);

//        String amazonAppId = getString(R.string.amazon_app_id); //Amazon Mobile Analytics App ID
//        String amazonCognitoId = getString(R.string.amazon_cognito_id);//Amazon Cognito Identity Pool ID
        String amazonAppId = getString(R.string.amazon_test_app_id); //Amazon Mobile Analytics App ID for test
        String amazonCognitoId = getString(R.string.amazon_test_cognito_id);//Amazon Cognito Identity Pool ID for test

        AnalyticsManager.getInstance().setAppInfo(getContext(), deviceId, amazonAppId, amazonCognitoId);

        Set<String> IGNORE_SESSION_FRAGLIST = Sets.newHashSet("ATTRACTOR");
        AnalyticsManager.getInstance().setIgnoreScreens(IGNORE_SESSION_FRAGLIST);
    }
}
