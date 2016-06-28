package com.samsung.retailexperience.retailtmo.video.config.setting;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by smheo on 9/29/2015.
 */
public class SettingsManager implements ISettings {

    private static final String SHARED_PREFERENCE_NAME = "com.samsung.retailexperience.retailtmo.default";
    protected final SharedPreferences prefs;

    public SettingsManager(Context context){
        prefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public SettingsManager(SharedPreferences prefs){
        this.prefs = prefs;
    }

    @Override
    public String getStringSetting(String key, String defaultValue){
        return prefs.getString(key, defaultValue);
    }

    @Override
    public boolean getBooleanSetting(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }

    @Override
    public void setStringSetting(String key, String value){
        prefs.edit().putString(key, value).commit();
    }
    @Override
    public void setBooleanSetting(String key, boolean value){
        prefs.edit().putBoolean(key, value).commit();
    }
}
