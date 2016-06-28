package com.tecace.app.manager.util;

/**
 * Created by icanmobile on 3/1/16.
 */
public class AppManagerConst {
    public enum TransactionDir {
        TRANSACTION_DIR_NONE,
        TRANSACTION_DIR_FORWARD,
        TRANSACTION_DIR_BACKWARD,
        TRANSACTION_DIR_ADD
    };

    public static final String ARGUMENTS_MODEL = "ARGUMENTS_MODEL";
    public static final String ARG_FRAGMENT_MODEL = "ARG_FRAGMENT_MODEL";
    public static final String ARG_FRAGMENT_TRANSACTION_DIR = "ARG_FRAGMENT_TRANSACTION_DIR";
    public static final String ARG_LEGAL_MENU_ITEM_ACTION = "ARG_LEGAL_MENU_ITEM_ACTION";

    public static final int TIMEOUT_SIX_SECOND = 6000;

}
