package com.developer.jw.ecosystemcopy.video.config;


import com.developer.jw.ecosystemcopy.video.config.environment.IEnvironments;
import com.developer.jw.ecosystemcopy.video.config.setting.ISettings;
import com.developer.jw.ecosystemcopy.video.util.ResourceUtil;

/**
 * Created by smheo on 9/29/2015.
 */
public interface ConfigProvider {
    public IEnvironments getEnvironmentConfig();
    public ISettings getSettingsManager();
    public ResourceUtil getResourceUtil();
}

