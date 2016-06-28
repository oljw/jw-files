package com.tecace.app.manager.analytics;

/**
 * Created by jaekim on 3/29/16.
 */
public enum FragmentChangeCause {
//    NEW_ACTIVITY("NewActivity"),
//    ON_ACTIVITY_RESUME("OnActivityResume"),
//    ON_ACTIVITY_PAUSE("OnActivityPause"),
//    WIDGET("Widget"),
    START_APP("StartApp"),
    BACK_PRESSED("BackPressed"),
    TIME_OUT("TimeOut"),
    VIDEO_COMPLETED("VideoCompleted"),
//    GEAR_S2("GearS2"),
//    BLUETOOTH_CONNECTION("BluetoothConnection"),
    TAP("Tap"),
//    SWIPE("Swipe"),
//    COACH_MARK("CoachMark"),
    TESTING("Testing", false);

    String mCause;
    boolean mSendAnalytics;
    // Default: send analytics.
    FragmentChangeCause(String cause) {
        this(cause, true);
    }
    FragmentChangeCause(String cause, boolean sendAnalytics) {
        this.mCause = cause;
        this.mSendAnalytics = sendAnalytics;
    }

    public String getCause() {
        return mCause;
    }
    public boolean sendAnalytics() {
        return mSendAnalytics;
    }
}
