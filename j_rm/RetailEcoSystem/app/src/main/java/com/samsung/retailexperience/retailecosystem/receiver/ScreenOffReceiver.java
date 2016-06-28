package com.samsung.retailexperience.retailecosystem.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.samsung.retailexperience.retailecosystem.ui.activity.MainActivity;


public class ScreenOffReceiver extends BroadcastReceiver {
    private static final String TAG = ScreenOffReceiver.class.getSimpleName();
    public ScreenOffReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "##### onReceive)+ ");
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            Log.d(TAG, "##### onReceive : Intent.ACTION_SCREEN_OFF!!");
            boolean bIsScreenOn;
            if (android.os.Build.VERSION.SDK_INT < 20) {
                bIsScreenOn = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isScreenOn();
            } else {
                bIsScreenOn = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isInteractive();
            }

            //Log.i(TAG, "@@@@@ ScreenOffReceiver - Current screen status : " + bIsScreenOn);

            if (bIsScreenOn == false) {
                wakeUpDevice(context);
                startApp(context);

            }
        }
    }

    private void wakeUpDevice(Context context) {
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

    private void startApp(Context context) {
        Intent sendIntent = new Intent(context.getApplicationContext(), MainActivity.class);
//        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sendIntent);
    }
}
