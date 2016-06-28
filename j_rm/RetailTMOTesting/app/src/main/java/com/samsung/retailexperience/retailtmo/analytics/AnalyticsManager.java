package com.samsung.retailexperience.retailtmo.analytics;

import android.app.Application;
import android.util.Log;

import com.google.common.base.Strings;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jaekim on 3/29/16.
 */
public class AnalyticsManager {
    private static final String TAG = AnalyticsManager.class.getSimpleName();

    // Event Names
    private static final String SCREEN_CHANGED = "screen_changed";
    public static final String WATCH_FACE_DURATION = "watch_face";

    // Attributes Names
    private static final String EVENT_TYPE = "event_type";
    private static final String TO_SCREEN = "to_screen";
    private static final String EXTRA = "extra";

    // Metric Names
    private static final String DURATION = "duration";

    private final IAnalytics mAnalytics;

    private static final Map<String, String> EMPTY_MAP = Collections.emptyMap();

    private String mFragmentName;
    private long mCurrentScreenLoadTime;

    private boolean mIsSessionActive;

    public AnalyticsManager(Application application) {
        mAnalytics = new AmazonAnalytics(application);
    }

    public void fragmentChanged(String newFragName, FragmentChangeCause cause) {
        Log.d(TAG, "fragmentChanged newFragName " + newFragName + ", cause: " + cause);

        String analyticsFragName = Utils.convertToAnalyticsTitle(newFragName);

        if (cause.sendAnalytics()) {
            fragmentChanged(analyticsFragName, cause.getCause());
            resumeFragment(analyticsFragName);
        }
    }

    public boolean isFragmentAvailable() {
        return !Strings.isNullOrEmpty(mFragmentName);
    }

    public void sendFragChangeActionEvent(String newFragName, FragmentChangeCause cause) {
        if (cause.sendAnalytics()) {
            String analyticsFragName = Utils.convertToAnalyticsTitle(newFragName);
            sendFragChangeActionEvent(mFragmentName, analyticsFragName, cause.getCause());
        }
    }

    public boolean isSessionActive() {
        return mIsSessionActive;
    }

    public void resumeSession() {
        if (mAnalytics != null && !mIsSessionActive) {
            mAnalytics.resumeSession();
        }
        mIsSessionActive = true;
    }

    public void resumeActivity(String currentFragName) {
        resumeSession();

        String analyticsFragName = Utils.convertToAnalyticsTitle(currentFragName);

//        sendFragChangeActionEvent(analyticsFragName, "", FragmentChangeCause.ON_ACTIVITY_RESUME.getCause());

        resumeFragment(analyticsFragName);
    }

    public void pauseSession() {
        if (mAnalytics != null && mIsSessionActive) {
            mAnalytics.pauseSession();
        }
        mIsSessionActive = false;
    }

    public void pauseActivity() {
        // just send fragment session time
        sendScreenState("NONE");

        pauseSession();
    }

    //    public void resumeFragment(String fragName) {
//        if (!Strings.isNullOrEmpty(mFragmentName)) {
//            if (Objects.equals(mFragmentName, fragName)) {
//                // it is the same screen.
//                return;
//            }
//            // else send the previous screen info
//            sendScreenState(EMPTY_MAP);
//        }
//        mCurrentScreenLoadTime = System.currentTimeMillis();
//        mFragmentName = fragName;
//
//    }
//
//    public void pauseFragment() {
//        if (!Strings.isNullOrEmpty(mFragmentName)) {
//            sendScreenState(EMPTY_MAP);
//        }
//    }
//
    private void fragmentChanged(String target, String cause) {
        // TODO bad saving current name.  update it.
        String currentScreen = mFragmentName;
        sendScreenState(target);

        sendFragChangeActionEvent(currentScreen, target, cause);
    }

    private void resumeFragment(String fragName) {
        mCurrentScreenLoadTime = System.currentTimeMillis();
        mFragmentName = fragName;
    }


    private void sendScreenState(String newScreen) {
        if (Strings.isNullOrEmpty(mFragmentName)) {
            return;
        }

        if (mAnalytics != null) {
            double duration = (double) (System.currentTimeMillis() - mCurrentScreenLoadTime);
            mAnalytics.sendScreenStateEvent(mFragmentName, duration);
        }

        mFragmentName = null;
    }

    private void sendFragChangeActionEvent(String currentScreen, String targetScreen, String cause) {
        if (mAnalytics != null) {
            Map<String, String> attributes = new LinkedHashMap<>(2);
            attributes.put(EVENT_TYPE, cause);
            attributes.put(TO_SCREEN, targetScreen);

            mAnalytics.sendAttributeEvent(SCREEN_CHANGED, currentScreen, attributes);
        }
    }

    public void sendAnalyticsActionEvent(AnalyticsInteractionType analyticsType,
                                         AnalyticsInteractionActionType type, String extra) {
        // mIsSessionActive is added here
        // issue: Hamburger menu is opened and home key to exit and reenter. then close hamburger is called
        if (mAnalytics != null && mIsSessionActive) {

            Map<String, String> attributes = new LinkedHashMap<>(2);
            attributes.put(EVENT_TYPE, type.getActionType());
            attributes.put(EXTRA, extra);

            mAnalytics.sendAttributeEvent(analyticsType.mAnalyticsType, mFragmentName, attributes);
        }
    }

//    public void sendAnalyticsDurationEvent(String eventName, String screenName, long duration) {
//        if (mAnalytics != null) {
//            String safeScreenName = Utils.convertToAnalyticsTitle(screenName);
//            Map<String, String> attributes = new LinkedHashMap<>(1);
//            attributes.put(SCREEN_NAME, safeScreenName);
//
//            Map<String, Double> metrics = new LinkedHashMap<>(1);
//            metrics.put(DURATION, Double.valueOf((double)duration));
//
//            mAnalytics.sendMetricEvent(eventName, mFragmentName, metrics);
//        }
//    }

    public void sendAnalyticsDurationEvent(String eventName, String fragmentName, String extra, long duration) {
        if (mAnalytics != null) {
            String safeScreenName = Utils.convertToAnalyticsTitle(fragmentName);
            Map<String, String> attributes = new LinkedHashMap<>(1);
            attributes.put(EXTRA, extra);

            Map<String, Double> metrics = new LinkedHashMap<>(1);
            metrics.put(DURATION, Double.valueOf((double) duration));

            // Bad~ mFragmentName should be used, but onPause() is called after changeFragment().
            // So, mFragmentName is updated before sending an anlytics
            mAnalytics.sendEvent(eventName, safeScreenName, attributes, metrics);
        }
    }

    public enum AnalyticsInteractionType {
        CHAPTER_INTERACTION("chapter_interaction"), USER_INTERACTION("user_interaction");

        String mAnalyticsType;
        AnalyticsInteractionType(String type) {
            mAnalyticsType = type;
        }
    }
}
