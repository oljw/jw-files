package com.tecace.retail.appmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tecace.retail.appmanager.util.RetailAppManagerConst;
import com.tecace.retail.appmanager.util.FuncUtil;
import com.tecace.retail.appmanager.util.PreferenceUtil;


/**
 * Created by smheo on 9/29/2015.
 */
public class ReBootReceiver extends BroadcastReceiver {
    public ReBootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            FuncUtil.getInstance().startApp(context,
                    PreferenceUtil.getInstance().getString(context, RetailAppManagerConst.PREFERENCE_APP_PACKAGE),
                    PreferenceUtil.getInstance().getString(context, RetailAppManagerConst.PREFERENCE_APP_CLASS));
        }
    }
}
