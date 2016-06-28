package com.samsung.retailexperience.retailecosystem.video.config;

import com.samsung.retailexperience.retailecosystem.video.config.environment.IEnvironments;
import com.samsung.retailexperience.retailecosystem.video.config.setting.ISettings;
import com.samsung.retailexperience.retailecosystem.video.util.ResourceUtil;

/**
 * Created by smheo on 9/29/2015.
 */
public interface ConfigProvider {
    public IEnvironments getEnvironmentConfig();
    public ISettings getSettingsManager();
    public ResourceUtil getResourceUtil();
}

