package com.tecace.app.manager.analytics;

/**
 * Created by jaekim on 3/29/16.
 */
public enum AnalyticsInteractionActionType {
    TAP("Tap"), ZOOM("Zoom"), SWIPE("Swipe"), BACK_PRESSED("BackPressed"), FACE_LOOPED("FaceLooped"),
    TIME_OUT("TimeOut"), ROTATE_DEVICE("RotateDevice");

    String mAction;
    AnalyticsInteractionActionType(String action) {
        this.mAction = action;
    }

    public String getActionType() {
        return mAction;
    }

    public static class Extra {
//        public static final String MANUAL_FOCUS = "manual_focus";
//        public static final String CAPTURE = "take_picture";
//        public static final String IMAGE = "image";
//        public static final String TAP_IMAGE = "tap_image";
//        public static final String ZOOM_IN = "zoom_in";
//        public static final String ZOOM_COUNT = "zoom_count_";
//        public static final String SHORTCUT = "shortcut";
//        public static final String BLUE_TAB = "blue_tab";
//        public static final String MORE_FEEDS = "more_feeds";
//        public static final String CALENDAR = "calendar";
//        public static final String EMAIL = "email";
//        public static final String DOWNLOAD = "download";
//        public static final String PAYMENT = "payment";
//        public static final String TAP_PAYMENT = "tap_payment";
//        public static final String CARDS = "cards";
//        public static final String SWIPE_CARDS_COUNT = "swipe_cards_count_";
//        public static final String SWIPE_CARDS = "swipe_cards";
//        public static final String LAUNCH = "launch";
//        public static final String SCAN_NOW = "scan_now";
//        public static final String HELP_LAYOUT = "help_view";
//        public static final String SPEC = "spec";
        public static final String ROTATE = "rotate";
//        public static final String APP_ICON = "app_icon";
//        public static final String WATCH = "watch";
//        public static final String OPEN_MENU = "open_hamburger_menu";
//        public static final String CLOSE_MENU = "close_hamburger_menu";
//        public static final String BATTERY_ICON = "battery_icon";
        public static final String SCROLL = "scroll";
    }
}
