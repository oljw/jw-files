package com.tecace.retail.analyticsmanager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by icanmobile on 5/6/16.
 */
public class AnalyticsManager {
    private static final String TAG = AnalyticsManager.class.getSimpleName();

    private static AnalyticsManager sInstance = null;
    public static AnalyticsManager getInstance() {
        if (sInstance == null)
            sInstance = new AnalyticsManager();
        return sInstance;
    }


    private boolean mNoAnalytics = false;    //for debug mode

    private Context mContext = null;
    private Context getContext() {
        return mContext;
    }

    private String mDeviceId = null;
    public void setAppInfo(Context context, String deviceId, String amazonAppId, String amazonCognitoId) {
        if (mNoAnalytics) return;

        mContext = context;
        mDeviceId = deviceId;
        AmazonAnalytics.getInstance().setAppInfo(context, amazonAppId, amazonCognitoId);
    }

    static final Map<String, Double> EMPTY_METRICS_MAP = Collections.emptyMap();
    private String mScreenName;
    private long mScreenStartTime = 0;

    // session state
    private boolean mSessionActive = false;
    private boolean isSessionActive() {
        return mSessionActive;
    }
    private void setSessionActive(boolean sessionActive) {
        mSessionActive = sessionActive;
    }


    // app resume event
    public void resumeApp(String screenName) {
        if (mNoAnalytics) return;

        if (getIgnoreScreens() != null && getIgnoreScreens().contains(screenName)) {
            mScreenName = screenName;
            return;
        }

        if (screenName != null && mScreenName != null && mScreenName.equals(screenName)) {
            resumeAnalyticsDelayed(RetailAnalyticsManagerConst.RESUME_ANALYTICS_DELAY_TIME);
        }
        mScreenName = screenName;
    }
    // app pause event
    public void pauseApp() {
        if (mNoAnalytics) return;

        mHandler.removeCallbacksAndMessages(null);
        pauseAnalytics();
    }

    public void setScreenName(String screenName) {
        if (mNoAnalytics) return;

        if (screenName == null) return;
        mScreenName = screenName;
    }
    private String getScreenName() {
        return mScreenName;
    }

    // session ignore fragments
    private Set<String> mIgnoreScreens = null;
    public void setIgnoreScreens(Set<String> ignoreScreens) {
        mIgnoreScreens = ignoreScreens;
    }
    private Set<String> getIgnoreScreens() {
        return mIgnoreScreens;
    }

    // resume
    private void resumeAnalytics() {
        try {
            if (isSessionActive()) return;
            AmazonAnalytics.getInstance().resumeSession();
            mSessionActive = true;
        } catch (Exception e) {
            Log.e(TAG, "##### resumeAnalytics failed.");
        }
    }

    private void pauseAnalytics() {
        try {
            if (!isSessionActive()) return;

            AmazonAnalytics.getInstance().pauseSession();
            mScreenStartTime = 0;
            mSessionActive = false;
        } catch(Exception e) {
            Log.e(TAG, "##### pauseAnalytics failed.");
        }
    }

    public void forcePauseAnalytics() {
        if (mNoAnalytics) return;

        try {
            AmazonAnalytics.getInstance().pauseSession();
            mScreenStartTime = 0;
            mSessionActive = false;
        } catch(Exception e) {
            Log.e(TAG, "##### pauseAnalytics failed.");
        }
    }

    private void resumeAnalyticsDelayed(int delayTime) {
        mHandler.sendEmptyMessageDelayed(MSG_RESUME_ANALYTICS, delayTime);
    }
    private final int MSG_RESUME_ANALYTICS = 1;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RESUME_ANALYTICS:
                    resumeAnalytics();
                    break;
            }
        }
    };



    /*
     * Notify Events
     */
    // notify screen changed
    public void notifyScreenChanged(String actionType, String toScreen) {
        if (mNoAnalytics) return;

        // ignore session for the fragments
        if (getIgnoreScreens() != null && getIgnoreScreens().contains(toScreen)) {
            Map<String, String> attributes = new LinkedHashMap<>(4);
            attributes.put(RetailAnalyticsManagerConst.EVENT_TYPE, actionType);
            attributes.put(RetailAnalyticsManagerConst.CURRENT_SCREEN, mScreenName);
            attributes.put(RetailAnalyticsManagerConst.TO_SCREEN, toScreen);
            attributes.put(RetailAnalyticsManagerConst.DEVICE_ID, mDeviceId);

            long currentTimeMillis = System.currentTimeMillis();
            long duration = currentTimeMillis - mScreenStartTime;
            // shouldn't be less than 0.
            duration = Math.max(duration, 0l);
            Map<String, Double> metrics = new LinkedHashMap<>(1);
            metrics.put(RetailAnalyticsManagerConst.SCREEN_DURATION, (double) duration);
            sendEvent(RetailAnalyticsManagerConst.SCREEN_EVENT, attributes, metrics);

            pauseAnalytics();
            mScreenName = toScreen;
            mScreenStartTime = 0;
            return;
        }

        // resume session
        if (!isSessionActive()) resumeAnalytics();

        Map<String, String> attributes = new LinkedHashMap<>(4);
        attributes.put(RetailAnalyticsManagerConst.EVENT_TYPE, actionType);
        attributes.put(RetailAnalyticsManagerConst.CURRENT_SCREEN, mScreenName);
        attributes.put(RetailAnalyticsManagerConst.TO_SCREEN, toScreen);
        attributes.put(RetailAnalyticsManagerConst.DEVICE_ID, mDeviceId);


        long currentTimeMillis = System.currentTimeMillis();
        if (mScreenStartTime == 0)
            sendEvent(RetailAnalyticsManagerConst.SCREEN_EVENT, attributes, EMPTY_METRICS_MAP);
        else {
            long duration = currentTimeMillis - mScreenStartTime;
            // shouldn't be less than 0.
            duration = Math.max(duration, 0l);
            Map<String, Double> metrics = new LinkedHashMap<>(1);
            metrics.put(RetailAnalyticsManagerConst.SCREEN_DURATION, (double) duration);
            sendEvent(RetailAnalyticsManagerConst.SCREEN_EVENT, attributes, metrics);
        }

        mScreenName = toScreen;
        mScreenStartTime = currentTimeMillis;
    }


    // notify capter event
    public void notifyChapterEvent(String actionType, String extra) {
        if (mNoAnalytics) return;

        if (!isSessionActive()) resumeAnalytics();
        notifyEvent(AnalyticsInteractionType.CHAPTER_INTERACTION, actionType, extra);
    }
    // notify user event
    public void notifyUserEvent(String actionType, String extra) {
        if (mNoAnalytics) return;

        if (!isSessionActive()) resumeAnalytics();
        notifyEvent(AnalyticsInteractionType.USER_INTERACTION, actionType, extra);
    }
    // notify event
    private void notifyEvent(AnalyticsInteractionType interactionEventType,
                             String actionType, String extra) {
        Map<String, String> attributes = new LinkedHashMap<>(4);
        attributes.put(RetailAnalyticsManagerConst.EVENT_TYPE, actionType);
        attributes.put(RetailAnalyticsManagerConst.CURRENT_SCREEN, mScreenName);
        attributes.put(RetailAnalyticsManagerConst.EXTRA, extra);
        attributes.put(RetailAnalyticsManagerConst.DEVICE_ID, mDeviceId);

        sendEvent(interactionEventType.mAnalyticsType, attributes, EMPTY_METRICS_MAP);
    }
    // send event to Amazon Analytics
    private void sendEvent(String eventName, Map<String, String> attributes, Map<String, Double> metrics) {
        try {
            AmazonAnalytics.getInstance().sendEvent(eventName, attributes, metrics);
        } catch (Exception e) {
            Log.e(TAG, "##### sendAnalyticsActionEvent failed.");
        }
    }
}
