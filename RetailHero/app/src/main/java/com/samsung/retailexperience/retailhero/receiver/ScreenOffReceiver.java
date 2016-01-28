package com.samsung.retailexperience.retailhero.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.samsung.retailexperience.retailhero.ui.activity.MainActivity;


public class ScreenOffReceiver extends BroadcastReceiver {
    private static final String TAG = ScreenOffReceiver.class.getSimpleName();
    public ScreenOffReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {

            Log.i(TAG, "### We have detected that the screen is turned off");

            wakeUpDevice(context);
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(i);
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
}
