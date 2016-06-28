package com.samsung.retailexperience.retailtmo.analytics;

import android.app.Application;
import android.content.Context;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by icanmobile on 4/5/16.
 */
public class Analyst {
    private static final String TAG = Analyst.class.getSimpleName();

    private static Analyst sInstance = null;
    public static Analyst getInstance() {
        if (sInstance == null)
            sInstance = new Analyst();
        return sInstance;
    }

    private Context mContext = null;
    protected AnalyticsManager mManager = null;
    private AnalyticsManager getManager() {
        return mManager;
    }

    private Context getContext() {
        return mContext;
    }

    public void init(Application application) {
        mContext = application.getApplicationContext();
        mManager = new AnalyticsManager(application);
    }

    public void deInit() {
        mManager = null;
        sInstance = null;
    }


    private static final Set<String> IGNORE_SESSION_FRAGLIST =
            Sets.newHashSet("ATTRACTOR");

    private String mLatestName = null;
    public void resume(String name) {
        if (name == null) return;
        if (IGNORE_SESSION_FRAGLIST.contains(name)) return;
        getManager().resumeActivity(name);
    }

    public void pause(String name) {
        if (name == null) return;
        if (IGNORE_SESSION_FRAGLIST.contains(name)) return;
        getManager().pauseActivity();
        mLatestName = null;
    }

    public void sendFragmentChange(String fragName, FragmentChangeCause cause) {
        boolean isIgnoredAnalytics = IGNORE_SESSION_FRAGLIST.contains(fragName);
        if (!getManager().isSessionActive() && !isIgnoredAnalytics) {
            getManager().resumeSession();
            mLatestName = fragName;
        }

        // TODO think about going out of the retail app mode
        if (!isIgnoredAnalytics) {
            getManager().fragmentChanged(fragName, cause);
        }

        if (getManager().isSessionActive() && isIgnoredAnalytics) {
            if (mManager.isFragmentAvailable()) {
                mManager.sendFragChangeActionEvent(fragName, cause);
            }
            pause(mLatestName);
        }
    }

    public void sendUserActionEvent(AnalyticsInteractionActionType type, String extra) {
        mManager.sendAnalyticsActionEvent(AnalyticsManager.AnalyticsInteractionType.USER_INTERACTION, type, extra);
    }

    public void sendChapterActionEvent(AnalyticsInteractionActionType type, String extra) {
        mManager.sendAnalyticsActionEvent(AnalyticsManager.AnalyticsInteractionType.CHAPTER_INTERACTION, type, extra);

    }

}
