package com.samsung.retailexperience.retailhero.config.setting;

/**
 * Created by smheo on 9/29/2015.
 */
public interface ISettings {
    public String getStringSetting(String key, String defaultValue);
    public void setStringSetting(String key, String value);

    public boolean getBooleanSetting(String key, boolean defaultValue);
    public void setBooleanSetting(String key, boolean value);
}
