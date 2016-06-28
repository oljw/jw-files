package com.tecace.retail.appmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tecace.retail.appmanager.util.RetailAppManagerConst;

/**
 * Created by icanmobile on 6/2/16.
 */
public class ForegroundDetectingReceiver extends BroadcastReceiver {
    private static final String TAG = ForegroundDetectingReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(RetailAppManagerConst.ACTION_FOREGROUND_DETECTED)) {
            String appName = intent.getExtras().getString(RetailAppManagerConst.ARG_DETECTED_FOREGROUND);
            Log.d(TAG, "##### ACTION_FOREGROUND_DETECTED : " + appName);

            // Call a function of RetailAppService Instance
//            if (RetailAppService.getInstance() == null) return;
//            RetailAppService.getInstance().setForegroundApp(appName);
        }
    }
}
