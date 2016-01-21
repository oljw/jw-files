package com.samsung.retailexperience.retailhero.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.RetailHeroApplication;
import com.samsung.retailexperience.retailhero.config.ConfigProvider;
import com.samsung.retailexperience.retailhero.config.environment.Environments;
import com.samsung.retailexperience.retailhero.config.environment.IEnvironments;
import com.samsung.retailexperience.retailhero.config.setting.ISettings;
import com.samsung.retailexperience.retailhero.iHeroService;
import com.samsung.retailexperience.retailhero.iHeroServiceCallback;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseFragment;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.ResourceUtil;
import com.samsung.retailexperience.retailhero.util.TopExceptionHandler;

import java.util.ArrayList;

/**
 * Created by icanmobile on 1/12/16.
 */
public abstract class BaseActivity extends Activity implements ResourceUtil.MissingResourceListener {
    private static final String TAG = BaseActivity.class.getSimpleName();

    protected IEnvironments gEnvMgr;
    protected ISettings gSetMgr;
    protected ResourceUtil gResMgr;

    protected BaseFragment mFragment = null;
    protected ArrayList<BaseFragment> mFragments = new ArrayList<BaseFragment>();
    protected Handler mFragmentsHandler = null;
    protected Runnable mFragmentsRunnable = null;

    public int mDisplayW;
    public int mDisplayH;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(null));

        getGlobalVariables();
        setWindowFeatures();
        getDisplaySize();
        //keepScreenOn();
        screenStayOn(true);
        setMissingContentListener();


        mFragmentsHandler = new Handler();
        mFragmentsRunnable = new Runnable() {
            @Override
            public void run() {
                removeFragments();
            }
        };

        if (isOverlayGranted()) {
            startHeroService();
            bindHeroService();
        }
        else {
            requestOverlayPermission();
        }
    }

    public void removeFragments() {
        Log.d(TAG, "##### removeFragments)+ mFragments = " + mFragments.size());
        for (int i=0; i<mFragments.size(); i++) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(mFragments.get(i));
            ft.commit();

            Log.d(TAG, "##### removeFragments : removed fragment = " + mFragments.get(i));
        }
        mFragments.clear();
        mFragments.add(mFragment);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "##### onStart)+ ");

        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "##### onStop)+ ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "##### onDestroy)+ ");

        screenStayOn(false);
        unbindHeroService();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "##### onResume)+ ");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "##### onPause)+ ");
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "##### onBackPressed)+ ");

        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mFragment != null)
                mFragment.onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onMissingResource(String missingfile) {
        //String activityName = getClass().getSimpleName();

        Log.d(TAG, "onMissingResource : " + missingfile);

        /**
         *  go to a missing page (activity? or fragment?)
         */
        Intent intent = new Intent(this, MissingContentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(AppConsts.ACTION_MISSING_FILE);
        intent.putExtra(AppConsts.WHAT_MISSING_FILE, missingfile);
        startActivity(intent);
        overridePendingTransition(0, R.anim.identity_animation);
        finish();
    }

    public void selfFinish() {
        Log.d(TAG, "##### selfFinish)+ ");
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    /*
     * Activity Environment
     */
    private void getGlobalVariables() {
        gEnvMgr = ((ConfigProvider)getApplication()).getEnvironmentConfig();
        gSetMgr = ((ConfigProvider)getApplication()).getSettingsManager();
        gResMgr = ((ConfigProvider)getApplication()).getResourceUtil();

        Log.i(TAG, "Activity Target : " + gEnvMgr.getStringValue(Environments.FLAVOR));
    }

    public void setWindowFeatures() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void screenStayOn(boolean on) {
        if (on) {
            //WindowManager.LayoutParams lp = getWindow().getAttributes();
            //lp.screenBrightness = 1.0f;
            //getWindow().setAttributes(lp);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_FULLSCREEN |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public void getDisplaySize() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        mDisplayW = metrics.widthPixels;
        mDisplayH = metrics.heightPixels;

//        Log.d(TAG, "##### mDisplayW = " + mDisplayW);
//        Log.d(TAG, "##### mDisplayH = " + mDisplayH);
    }

    private void setMissingContentListener() {
        if (gResMgr != null) {
            gResMgr.setMissingResourceListener(this);
        }
    }

    //http://developer.android.com/intl/ko/reference/android/provider/Settings.html#ACTION_MANAGE_OVERLAY_PERMISSION
    //Unable to add window android.view.ViewRoot$W@44da9bc0 -- permission denied for this window type
    private int REQUEST_OVERLAY_PERMISSION = 100;
    public boolean isOverlayGranted() {
        if (Settings.canDrawOverlays(this))
            return true;
        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (Settings.canDrawOverlays(this)) {
                // continue here - permission was granted
                startHeroService();
                bindHeroService();
            }
        }
    }

    /*
     * service bind function
     */
    private static final String HERO_SERVICE = "com.samsung.retailexperience.retailhero.HERO_SERVICE";
    private iHeroService mService = null;
    private boolean mIsBound = false;

    public iHeroService getService() {
        return mService;
    }

    private void startHeroService() {
        Intent serviceIntent = new Intent(HERO_SERVICE);
        ResolveInfo ri = getPackageManager().resolveService(serviceIntent, 0);
        serviceIntent.setPackage(ri.serviceInfo.packageName);

        Bundle bundle = new Bundle();
        bundle.putString(AppConsts.ARG_START_SERVICE, AppConsts.ArgStartService.SYSTEM_REBOOT.name());
        serviceIntent.putExtras(bundle);

        RetailHeroApplication.getContext().startService(serviceIntent);
    }
    private void stopHeroService() {
        Intent serviceIntent = new Intent(HERO_SERVICE);
        ResolveInfo ri = getPackageManager().resolveService(serviceIntent, 0);
        serviceIntent.setPackage(ri.serviceInfo.packageName);
        RetailHeroApplication.getContext().stopService(serviceIntent);
    }
    private void bindHeroService() {
        if (mIsBound == false) {
            Intent serviceIntent = new Intent(HERO_SERVICE);
            ResolveInfo ri = getPackageManager().resolveService(serviceIntent, 0);
            serviceIntent.setPackage(ri.serviceInfo.packageName);
            RetailHeroApplication.getContext().bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }
    private void unbindHeroService() {
        if (mIsBound) {
            if (mService != null) {
                try {
                    mService.unregisterCallback(mServiceCallback);
                    mService = null;
                    RetailHeroApplication.getContext().unbindService(mServiceConnection);
                } catch (RemoteException e) {
                    e.printStackTrace();;
                }
            }
            mIsBound = false;
        }
    }
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mService = iHeroService.Stub.asInterface(service);
            try {
                mService.registerCallback(mServiceCallback);
                mIsBound = true;
                serviceConnected();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
            mIsBound = false;
        }
    };

    private iHeroServiceCallback mServiceCallback = new iHeroServiceCallback() {
        //sample function
        @Override
        public void responseServiceFunc() throws RemoteException {

        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };

    public abstract void serviceConnected();
}
