package com.samsung.retailexperience.retailhero.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;

import com.samsung.retailexperience.retailhero.util.AppConsts;

/**
 * Created by icanmobile on 1/18/16.
 */
public class ForegroundDetectingService extends AccessibilityService {
    private static final String TAG = ForegroundDetectingService.class.getSimpleName();

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        //Configure these here for compatibility with API 13 and below.
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;

        if (Build.VERSION.SDK_INT >= 16)
            //Just in case this helps
            config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

        setServiceInfo(config);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            ComponentName componentName = new ComponentName(
                    event.getPackageName().toString(),
                    event.getClassName().toString()
            );

            ActivityInfo activityInfo = tryGetActivity(componentName);
            boolean isActivity = activityInfo != null;
            if (isActivity) {
//                Toast.makeText(ForegroundDetectingService.this, componentName.flattenToShortString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AppConsts.ACTION_FOREGROUND_DETECTED);
                intent.putExtra(AppConsts.ARG_DETECTED_FOREGROUND, componentName.flattenToShortString());
                sendBroadcast(intent);
            }
        }
    }

    private ActivityInfo tryGetActivity(ComponentName componentName) {
        try {
            return getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public void onInterrupt() {}

}
