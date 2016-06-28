package com.tecace.app.manager.analytics;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.google.common.base.Strings;

import java.lang.reflect.Method;

/**
 * Created by icanmobile on 5/6/16.
 */
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

    static final String UNKNOWN_DEVICE_ID = "unknown";

    private static Utils sInstance = null;
    public static Utils getInstance() {
        if (sInstance == null)
            sInstance = new Utils();
        return sInstance;
    }

    /**
     * If the DeviceId (or IMEI) is empty, get ril serial #
     * @param context
     * @return
     */
    public String getDeviceIdOrSerialNumber(Context context) {
        String deviceId = getDeviceId(context);
        if (Strings.isNullOrEmpty(deviceId)) {
            deviceId = getManufacturerSerialNumber();
        }
        return deviceId;
    }

//    public String getDeviceId(Application application) {
//        TelephonyManager telephonyManager = (TelephonyManager)application.getSystemService(Context.TELEPHONY_SERVICE);
//        return telephonyManager.getDeviceId();
//    }
    public String getDeviceId(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getDeviceId();
        } catch (Exception e) {
            return null;
        }
    }

    public String getManufacturerSerialNumber() {
        String serial = UNKNOWN_DEVICE_ID;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serial = (String) get.invoke(c, "ril.serialnumber", UNKNOWN_DEVICE_ID);
        } catch (Exception ignored) {}
        return serial;
    }

    // TODO JW don't have to replace underscore ('_')
    public String convertToAnalyticsTitle(String name) {
        if (Strings.isNullOrEmpty(name)) {
            return name;
        }
        return name.replaceAll("[^A-Za-z0-9]", "_");
    }
}
