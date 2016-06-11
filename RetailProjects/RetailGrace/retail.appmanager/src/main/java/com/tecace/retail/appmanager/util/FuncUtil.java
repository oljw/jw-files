package com.tecace.retail.appmanager.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by icanmobile on 6/3/16.
 */
public class FuncUtil {
    private static final String TAG = FuncUtil.class.getSimpleName();

    private static FuncUtil sInstance = null;
    public static FuncUtil getInstance() {
        if (sInstance == null)
            sInstance = new FuncUtil();
        return sInstance;
    }

    public void wakeUpDevice(Context context) {
        Log.d(TAG, "##### wakeUpDevice)+ ");
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wakeup");

        if (wakeLock.isHeld()) {
            // release old wake lock
            wakeLock.release();
        }

        // create a new wake lock
        wakeLock.acquire();


        // elease again
        wakeLock.release();
    }

    public void startApp(Context context, String pkg, String cls) {
        Log.d(TAG, "##### startApp)+ ");
        if (pkg == null || pkg.length() == 0 || cls == null || cls.length() == 0 ) return;

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName(pkg, cls));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public boolean isScreenOn(Context context) {
        Log.d(TAG, "##### isScreenOn)+ ");
        // checking screen on/off state
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean bIsScreenOn = false;
        if (android.os.Build.VERSION.SDK_INT < 20) {
            bIsScreenOn = pm.isScreenOn();
        } else {
            bIsScreenOn = pm.isInteractive();
        }

        return bIsScreenOn;
    }
}
