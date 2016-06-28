package com.tecace.retail.analyticsmanager;

/**
 * Created by icanmobile on 5/6/16.
 */
public enum AnalyticsInteractionType {
    CHAPTER_INTERACTION("chapter_interaction"), USER_INTERACTION("user_interaction");

    String mAnalyticsType;
    AnalyticsInteractionType(String type) {
        mAnalyticsType = type;
    }
}