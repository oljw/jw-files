package com.samsung.retailexperience.retailtmo.analytics;

import android.app.Application;
import android.util.Log;

import com.amazonaws.mobileconnectors.amazonmobileanalytics.AnalyticsEvent;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.InitializationException;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.MobileAnalyticsManager;
import com.google.common.base.Strings;
import com.samsung.retailexperience.retailtmo.R;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jaekim on 3/25/16.
 */
public class AmazonAnalytics implements IAnalytics {
    private static final String TAG = AmazonAnalytics.class.getSimpleName();

    private static final String SCREEN_STATE = "screen_state";
    private static final String CURRENT_SCREEN = "current_screen";
    private static final String SCREEN_DURATION = "duration";

    private MobileAnalyticsManager mAnalytics = null;

    private static final Map<String, String> EMPTY_ATTR_MAP = Collections.emptyMap();
    private static final Map<String, Double> EMPTY_METRICS_MAP = Collections.emptyMap();

    // TODO remove it
    private static final boolean THROW_EXCEPTION_WHEN_MISSING_INFO = false;

    public AmazonAnalytics(Application application) {
//        try {
//            mAnalytics = MobileAnalyticsManager.getOrCreateInstance(
//                    application.getApplicationContext(),
//                    application.getString(R.string.amazon_app_id), //Amazon Mobile Analytics App ID
//                    application.getString(R.string.amazon_cognito_id) //Amazon Cognito Identity Pool ID
//            );
//        } catch(InitializationException ex) {
//            Log.e(TAG, "Failed to initialize Amazon Mobile Analytics", ex);
//        }
    }

    @Override
    public void resumeSession() {
        if (mAnalytics == null) return;
        mAnalytics.getSessionClient().resumeSession();
    }

    @Override
    public void pauseSession() {
        if (mAnalytics == null) return;
        mAnalytics.getSessionClient().pauseSession();
        mAnalytics.getEventClient().submitEvents();
    }

    @Override
    public void sendScreenStateEvent(String currentScreen, double duration) {
        if (THROW_EXCEPTION_WHEN_MISSING_INFO) {
            if (Strings.isNullOrEmpty(currentScreen) || duration < 5.0) {
                throw new AssertionError("sendScreenStateEvent " + currentScreen + ", duration: " + duration);
            }
        }

        Map<String, Double> metrics = new LinkedHashMap<>(1);
        metrics.put(SCREEN_DURATION, Double.valueOf((double) duration));

        sendEvent(SCREEN_STATE, currentScreen, EMPTY_ATTR_MAP, metrics);
    }

    @Override
    public void sendAttributeEvent(String eventName, String screenName, Map<String, String> attributes) {
        sendEvent(eventName, screenName, attributes, EMPTY_METRICS_MAP);
    }

    @Override
    public void sendMetricEvent(String eventName, String screenName, Map<String, Double> metrics) {
        if (THROW_EXCEPTION_WHEN_MISSING_INFO) {
            if (Strings.isNullOrEmpty(eventName) || Strings.isNullOrEmpty(screenName) || metrics.size() == 0) {
                throw new AssertionError("sendMetricEvent eventName: " + eventName + ", screenName: " + screenName + ", metrics: " + metrics);
            }
        }

        sendEvent(eventName, screenName, EMPTY_ATTR_MAP, metrics);
    }

    @Override
    public void sendEvent(String eventName, String screenName, Map<String, String> attributes, Map<String, Double> metrics) {
        if (mAnalytics == null) return;
//        Log.d(TAG, "##### sendEvent : " + screenName + " : " + eventName);

        AnalyticsEvent event = getDefaultActionEvent(eventName, screenName);
        appendAttribute(event, attributes);
        appendMetrics(event, metrics);
        sendEvent(event);
    }

    private void appendAttribute(AnalyticsEvent event, Map<String, String> attributes) {
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            event.addAttribute(entry.getKey(), entry.getValue());
        }
    }

    private void appendMetrics(AnalyticsEvent event, Map<String, Double> metrics) {
        for (Map.Entry<String, Double> entry : metrics.entrySet()) {
            event.addMetric(entry.getKey(), entry.getValue());
        }
    }

    private void sendEvent(AnalyticsEvent event) {
        if (mAnalytics == null) return;
        mAnalytics.getEventClient().recordEvent(event);
    }

    private AnalyticsEvent getDefaultActionEvent(String eventName, String screenName) {
        if (mAnalytics == null) return null;
        AnalyticsEvent event = mAnalytics.getEventClient().createEvent(eventName);
        if (!Strings.isNullOrEmpty(screenName)) {
            event.addAttribute(CURRENT_SCREEN, screenName);
        }
        return event;
    }

}
