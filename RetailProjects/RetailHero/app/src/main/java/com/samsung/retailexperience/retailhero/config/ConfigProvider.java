package com.samsung.retailexperience.retailhero.config;

import com.samsung.retailexperience.retailhero.config.environment.IEnvironments;
import com.samsung.retailexperience.retailhero.config.setting.ISettings;
import com.samsung.retailexperience.retailhero.util.ResourceUtil;

/**
 * Created by smheo on 9/29/2015.
 */
public interface ConfigProvider {
    public IEnvironments getEnvironmentConfig();
    public ISettings getSettingsManager();
    public ResourceUtil getResourceUtil();
}
