package com.samsung.retailexperience.retailhero.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.samsung.retailexperience.retailhero.service.HeroService;
import com.samsung.retailexperience.retailhero.ui.activity.MainActivity;
import com.samsung.retailexperience.retailhero.util.AppConsts;


/**
 * Created by smheo on 9/29/2015.
 */
public class ReBootReceiver extends BroadcastReceiver {
    public ReBootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            startService(context);
            startApp(context);
        }
    }

    private void startService(Context context) {
        Intent serviceIntent = new Intent(context, HeroService.class);
        Bundle bundle = new Bundle();
        bundle.putString(AppConsts.ARG_START_SERVICE, AppConsts.ArgStartService.SYSTEM_REBOOT.name());
        serviceIntent.putExtras(bundle);
        context.startService(serviceIntent);
    }

    private void startApp(Context context) {
        Intent sendIntent = new Intent(context.getApplicationContext(), MainActivity.class);
//        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sendIntent);
    }
}
