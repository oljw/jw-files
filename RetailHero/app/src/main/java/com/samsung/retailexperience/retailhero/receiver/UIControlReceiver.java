package com.samsung.retailexperience.retailhero.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.samsung.retailexperience.retailhero.service.HeroService;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;

/**
 * Created by icanmobile on 1/18/16.
 */
public class UIControlReceiver extends BroadcastReceiver {
    private static final String TAG = UIControlReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(AppConsts.ACTION_FOREGROUND_DETECTED)) {
            String appName = intent.getExtras().getString(AppConsts.ARG_DETECTED_FOREGROUND);
            Log.d(TAG, "##### ACTION_FOREGROUND_DETECTED : " + appName);

            if (getService() == null) return;
            getService().setForegroundApp(appName);
        }

        else if (intent.getAction().equals(AppConsts.ACTION_CHANGE_FRAGMENT)) {
            if (intent.getExtras().getString(AppConsts.ARG_NEXT_FRAGMENT) != null &&
                    intent.getExtras().getString(AppConsts.ARG_NEXT_FRAGMENT).length() > 0) {
                AppConst.UIState newState = AppConst.UIState.valueOf(intent.getExtras().getString(AppConsts.ARG_NEXT_FRAGMENT));
                Log.d(TAG, "##### ACTION_CHANGE_FRAGMENT : " + newState.name());

                if (getService() == null) return;
                getService().changeFragment(newState);
            }
        }
    }

    private static HeroService sInstance = null;
    public static void setService(HeroService instance) {
        sInstance = instance;
    }
    private HeroService getService() {
        return sInstance;
    }
}
