package com.samsung.retailexperience.retailtmo.analytics;

import java.util.Map;

/**
 * Created by jaekim on 3/25/16.
 */
public interface IAnalytics {
    // Activity
    public void resumeSession();
    public void pauseSession();

//    /**
//     *
//     * @param fragment
//     * @param name A fragment can be shared, but each feature should have a specific name
//     */
//    public void resumeFragment(Fragment fragment, String name);
//    public void pauseFragment(Fragment fragment);
//    public void pauseFragment(Fragment fragment, Map<String, String> attributes);

    public void sendScreenStateEvent(String currentScreen, double duration);

    public void sendAttributeEvent(String eventName, String screenName, Map<String, String> attributes);

    public void sendMetricEvent(String eventName, String screenName, Map<String, Double> metrics);

    public void sendEvent(String eventName, String screenName, Map<String, String> attributes, Map<String, Double> metrics);
}
