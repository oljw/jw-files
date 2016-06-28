package com.tecace.retail.appmanager.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.tecace.retail.appmanager.iRetailAppService;
import com.tecace.retail.appmanager.iRetailAppServiceCallback;
import com.tecace.retail.appmanager.util.RetailAppManagerConst;

/**
 * Created by icanmobile on 5/31/16.
 */
public abstract class RetailActivity extends Activity {
    private static final String TAG = RetailActivity.class.getSimpleName();

    protected int mDisplayW = 0;
    protected int mDisplayH = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setWindowFeatures();
        getDisplaySize();
        screenStayOn(true);

        startHeroService();
        bindHeroService();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    protected void setWindowFeatures() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void screenStayOn(boolean on) {
        if (on) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.screenBrightness = 1.0f;
            getWindow().setAttributes(lp);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_FULLSCREEN |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    protected void getDisplaySize() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        mDisplayW = metrics.widthPixels;
        mDisplayH = metrics.heightPixels;
    }

    public void selfFinish() {
        moveTaskToBack(true);
        finish();
    }


    /*
     * service bind function
     */
    private static final String RETAIL_APP_SERVICE = "com.tecace.retail.appmanager.RETAIL_APP_SERVICE";
    private iRetailAppService mService = null;
    private boolean mIsBound = false;

    public iRetailAppService getService() {
        return mService;
    }

    private void startHeroService() {
        Intent serviceIntent = new Intent(RETAIL_APP_SERVICE);
        ResolveInfo ri = getPackageManager().resolveService(serviceIntent, 0);
        serviceIntent.setPackage(ri.serviceInfo.packageName);

        Bundle bundle = new Bundle();
        bundle.putString(RetailAppManagerConst.ARG_START_SERVICE, RetailAppManagerConst.ArgStartService.SYSTEM_REBOOT.name());
        serviceIntent.putExtras(bundle);

        getApplicationContext().startService(serviceIntent);
    }
    private void stopHeroService() {
        Intent serviceIntent = new Intent(RETAIL_APP_SERVICE);
        ResolveInfo ri = getPackageManager().resolveService(serviceIntent, 0);
        serviceIntent.setPackage(ri.serviceInfo.packageName);
        getApplicationContext().stopService(serviceIntent);
    }
    private void bindHeroService() {
        if (mIsBound == false) {
            Intent serviceIntent = new Intent(RETAIL_APP_SERVICE);
            ResolveInfo ri = getPackageManager().resolveService(serviceIntent, 0);
            serviceIntent.setPackage(ri.serviceInfo.packageName);
            getApplicationContext().bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }
    private void unbindHeroService() {
        if (mIsBound) {
            if (mService != null) {
                try {
                    mService.unregisterCallback(mServiceCallback);
                    mService = null;
                    getApplicationContext().unbindService(mServiceConnection);
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
            mService = iRetailAppService.Stub.asInterface(service);
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

    private iRetailAppServiceCallback mServiceCallback = new iRetailAppServiceCallback() {
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
