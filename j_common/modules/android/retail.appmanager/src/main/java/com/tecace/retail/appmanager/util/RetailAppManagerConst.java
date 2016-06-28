package com.tecace.retail.appmanager.util;

/**
 * Created by icanmobile on 3/1/16.
 */
public class RetailAppManagerConst {
    private static final String TAG = RetailAppManagerConst.class.getSimpleName();

    public static final String[] ZIP_ASSETS_LIST = { "frame.zip", "chapter.zip", "image.zip"};

    // intent value of the missing activity
    public static final String ACTION_MISSING_FILE = "ACTION_MISSING_FILE";
    public static final String WHAT_MISSING_FILE = "WHAT_MISSING_FILE";

    // RetailAppService
    public static final String ARG_START_SERVICE = "ARG_START_SERVICE";
    public enum ArgStartService {
        MAIN_ACTIVITY,
        SYSTEM_REBOOT
    }

    // ForegroundDetectingService
    public static final String ACTION_FOREGROUND_DETECTED = "com.tecace.retail.appmanager.receiver.action.FOREGROUND_DETECTED";
    public static final String ARG_DETECTED_FOREGROUND = "ARG_DETECTED_FOREGROUND";

    // shared preference keys
    public static final String PREFERENCE_APP_PACKAGE = "PREFERENCE_APP_PACKAGE";
    public static final String PREFERENCE_APP_CLASS = "PREFERENCE_APP_CLASS";
    public static final String PREFERENCE_APP_TITLE = "PREFERENCE_APP_TITLE";
    public static final String PREFERENCE_APP_SELF_FINISH = "PREFERENCE_APP_SELF_FINISH";
}
