package com.samsung.retailexperience.retailtmo.analytics;

import com.google.common.base.Strings;

/**
 * Created by icanmobile on 4/5/16.
 */
public class Utils {
    public static String convertToAnalyticsTitle(String name) {
        if (Strings.isNullOrEmpty(name)) {
            return name;
        }
        return name.replaceAll("[^A-Za-z0-9]", "_");
    }
}
