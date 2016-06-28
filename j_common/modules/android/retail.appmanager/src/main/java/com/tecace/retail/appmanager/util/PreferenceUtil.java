package com.tecace.retail.appmanager.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by icanmobile on 6/3/16.
 */
public class PreferenceUtil {
    private static final String TAG = PreferenceUtil.class.getSimpleName();

    public static final String SHARED_PREFERENCE_NAME = "com.tecace.retail.appmanager.default";

    private static PreferenceUtil sInstance = null;
    public static PreferenceUtil getInstance() {
        if (sInstance == null)
            sInstance = new PreferenceUtil();
        return sInstance;
    }

    public int getInt(Context context, String key) {
        if( context == null ) return 0;
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return pref.getInt(key, 0);
    }
    public void setInt(Context context, String key, int value) {
        if( context == null ) return;
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public boolean getBoolean(Context context, String key) {
        if( context == null ) return false;
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return pref.getBoolean(key, false);
    }
    public void setBoolean(Context context, String key, boolean value) {
        if( context == null ) return;
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public String getString(Context context, String key) {
        if( context == null ) return null;
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return pref.getString(key, "");
    }
    public void setString(Context context, String key, String value) {
        if( context == null ) return;
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(key, value);
        edit.commit();
    }
}
