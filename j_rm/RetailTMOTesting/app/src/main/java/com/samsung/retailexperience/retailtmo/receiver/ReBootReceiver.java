package com.samsung.retailexperience.retailtmo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.samsung.retailexperience.retailtmo.ui.activity.MainActivity;


/**
 * Created by smheo on 9/29/2015.
 */
public class ReBootReceiver extends BroadcastReceiver {
    public ReBootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            startApp(context);
        }
    }

    private void startApp(Context context) {
        Intent sendIntent = new Intent(context.getApplicationContext(), MainActivity.class);
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sendIntent);
    }
}
