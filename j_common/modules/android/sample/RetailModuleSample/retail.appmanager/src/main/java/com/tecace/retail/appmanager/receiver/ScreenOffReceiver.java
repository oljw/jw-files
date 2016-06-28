package com.tecace.retail.appmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tecace.retail.appmanager.util.RetailAppManagerConst;
import com.tecace.retail.appmanager.util.FuncUtil;
import com.tecace.retail.appmanager.util.PreferenceUtil;


public class ScreenOffReceiver extends BroadcastReceiver {
    private static final String TAG = ScreenOffReceiver.class.getSimpleName();
    public ScreenOffReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "##### onReceive)+ ");
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            Log.d(TAG, "##### onReceive : Intent.ACTION_SCREEN_OFF!!");
            if (!FuncUtil.getInstance().isScreenOn(context)) {
                FuncUtil.getInstance().wakeUpDevice(context);
                FuncUtil.getInstance().startApp(context,
                        PreferenceUtil.getInstance().getString(context, RetailAppManagerConst.PREFERENCE_APP_PACKAGE),
                        PreferenceUtil.getInstance().getString(context, RetailAppManagerConst.PREFERENCE_APP_CLASS));
            }
        }
    }
}
