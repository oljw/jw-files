package com.samsung.retailexperience.retailhero.util;

/**
 * Created by icanmobile on 1/12/16.
 */
public class AppConsts {

    // model name
    public static final String HERO_B2B = "hero_b2b";
    public static final String HERO_B2C = "hero_b2c";
    public static final String HERO2_B2B = "hero2_b2b";
    public static final String HERO2_B2C = "hero2_b2c";

    // zip asset files (download from cms server)
    public static final String[] ZIP_ASSETS_LIST = { "frame.zip", "chapter.zip", "image.zip"};

    // bundle key for fragment args
    public static final String ARG_FRAGMENT_MODEL   = "ARG_FRAGMENT_MODEL";
    public static final String ARG_JSON_MODEL       = "ARG_JSON_MODEL";
    public static final String ARG_LAST_UISTATE     = "ARG_LAST_UISTATE";

    public static final int TIMEOUT_SIX_SECOND = 6000;

    // fragment transactions
    public enum TransactionDir {
        TRANSACTION_DIR_NONE,
        TRANSACTION_DIR_FORWARD,
        TRANSACTION_DIR_BACKWARD
    };

    // service start arguments
    public static final String ARG_START_SERVICE = "ARG_START_SERVICE";
    public enum ArgStartService {
        MAIN_ACTIVITY,
        SYSTEM_REBOOT
    }

    // UpdateUIReceiver
    public static final String ACTION_FOREGROUND_DETECTED = "com.samsung.retailexperience.retailhero.receiver.action.FOREGROUND_DETECTED";
    public static final String ARG_DETECTED_FOREGROUND = "ARG_DETECTED_FOREGROUND";
    public static final String ACTION_CHANGE_FRAGMENT = "com.samsung.retailexperience.retailhero.receiver.action.CHANGE_FRAGMENT";
    public static final String ARG_NEXT_FRAGMENT = "ARG_NEXT_FRAGMENT";

    // intent value of the missing activity
    public static final String ACTION_MISSING_FILE = "com.samsung.retail.retailhero.missingfile";
    public static final String WHAT_MISSING_FILE = "whatMissingFile";

    public enum DeviceModel {
        GALAXY_S4,
        GALAXY_S5,
        GALAXY_S6,
        GALAXY_S6_EDGE,
        GALAXY_S6_EDGE_PLUS,
        GALAXY_S7,
        GALAXY_S7_EDGE,
        GALAXY_NOTE3,
        GALAXY_NOTE4,
        GALAXY_NOTE5,
        GALAXY_NOTE_EDGE
    };
}