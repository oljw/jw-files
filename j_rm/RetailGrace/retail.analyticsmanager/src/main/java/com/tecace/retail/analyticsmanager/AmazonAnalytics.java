package com.tecace.retail.analyticsmanager;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobileconnectors.amazonmobileanalytics.AnalyticsEvent;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.MobileAnalyticsManager;

import java.util.Map;

/**
 * Created by icanmobile on 5/6/16.
 */
public class AmazonAnalytics {
    private static final String TAG = AmazonAnalytics.class.getSimpleName();

    private static AmazonAnalytics sInstance = null;
    public static AmazonAnalytics getInstance() {
        if (sInstance == null)
            sInstance = new AmazonAnalytics();
        return sInstance;
    }

    private Context mContext = null;
    private Context getContext() {
        return mContext;
    }

    private String mAmazonAppId = null;
    private String mAmazonCognitoId = null;
    public void setAppInfo(Context context, String amazonAppId, String amazonCognitoId) {
        Log.d(TAG, "##### [AmazonAnalytics] amazonAppId = " + amazonAppId + ", amazonCognitoId = " + amazonCognitoId + " $$$$$$$$$");
        mContext = context;
        mAmazonAppId = amazonAppId;
        mAmazonCognitoId = amazonCognitoId;
    }

    private String getAmazonAppId() {
        return mAmazonAppId;
    }
    private String getAmazonCognitoId() {
        return mAmazonCognitoId;
    }

    private MobileAnalyticsManager mAnalytics = null;
    public MobileAnalyticsManager getAnalytics() {
        try {
            if (mAnalytics == null) {
                if (getContext() == null || getAmazonAppId() == null || getAmazonCognitoId() == null) return null;
                mAnalytics = MobileAnalyticsManager.getOrCreateInstance(
                        getContext(),
                        getAmazonAppId(),
                        getAmazonCognitoId()
                );
            }
        } catch(Exception e) {
            Log.e(TAG, "##### [AmazonAnalytics] getAnalytics failed. $$$$$$$$$");
        }
        return mAnalytics;
    }

    public void resumeSession() {
        try {
            Log.d(TAG, "##### [AmazonAnalytics] resumeSession $$$$$$$$$");
            getAnalytics().getSessionClient().resumeSession();
            getAnalytics().getEventClient().submitEvents();
        } catch (Exception e) {
            Log.e(TAG, "##### [AmazonAnalytics] resumeSession failed. $$$$$$$$$");
        }
    }

    public void pauseSession() {
        try {
            Log.d(TAG, "##### [AmazonAnalytics] pauseSession $$$$$$$$$");
            getAnalytics().getSessionClient().pauseSession();
            getAnalytics().getEventClient().submitEvents();
        } catch (Exception e) {
            Log.e(TAG, "##### [AmazonAnalytics] pauseSession failed. $$$$$$$$$");
        }
    }

    public void sendEvent(String eventName, Map<String, String> attributes, Map<String, Double> metrics) {
        try {
            AnalyticsEvent event = getAnalytics().getEventClient().createEvent(eventName);
            appendAttribute(event, attributes);
            appendMetrics(event, metrics);
            sendEvent(event);
        } catch (Exception e) {
            Log.e(TAG, "##### [AmazonAnalytics] sendEvent failed. $$$$$$$$$");
        }
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
        Log.d(TAG, "##### [AmazonAnalytics] sendEvent)+ $$$$$$$$$");
        Log.d(TAG, "##### [AmazonAnalytics] Type: " + event.getEventType());
        Log.d(TAG, "##### [AmazonAnalytics] Attributes: " + event.getAllAttributes());
        Log.d(TAG, "##### [AmazonAnalytics] Metrics: " + event.getAllMetrics());
        Log.d(TAG, "##### [AmazonAnalytics] sendEvent)- $$$$$$$$$");

        try {
            getAnalytics().getEventClient().recordEvent(event);
            getAnalytics().getEventClient().submitEvents();
        } catch (Exception e) {
            Log.e(TAG, "##### [AmazonAnalytics] sendEvent failed. $$$$$$$$$");
        }
    }
}
