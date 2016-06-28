package com.samsung.retailexperience.retailecosystem.util;

/**
 * Created by icanmobile on 3/1/16.
 */
public class AppConst {
    private static final String TAG = AppConst.class.getSimpleName();

    public enum TransactionDir {
        TRANSACTION_DIR_NONE,
        TRANSACTION_DIR_FORWARD,
        TRANSACTION_DIR_BACKWARD
    };

    public static final String ARG_FRAGMENT_MODEL = "ARG_FRAGMENT_MODEL";
    public static final String ARG_FRAGMENT_TRANSACTION_DIR = "ARG_FRAGMENT_TRANSACTION_DIR";
    public static final String ARG_LEGAL_MENU_ITEM_ACTION = "ARG_LEGAL_MENU_ITEM_ACTION";

    public static final String[] ZIP_ASSETS_LIST = { "frame.zip", "chapter.zip", "image.zip"};

    public static final int TIMEOUT_SIX_SECOND = 6000;

    // intent value of the missing activity
    public static final String ACTION_MISSING_FILE = "com.samsung.retailexperience.retailecosystem.missingfile";
    public static final String WHAT_MISSING_FILE = "whatMissingFile";
}
