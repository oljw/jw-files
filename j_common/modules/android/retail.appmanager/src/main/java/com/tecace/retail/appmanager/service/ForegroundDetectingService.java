package com.tecace.retail.appmanager.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.tecace.retail.appmanager.util.RetailAppManagerConst;

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
            /**
             * Issue: Software Keyboard suggestion pop up (while long press) doesn't have any packagename nor class name.
             *   Therefore, it should filtered out (JW)
             * Solution: do not check if those packageName or className is null
             */
            if (event.getPackageName() == null || event.getClassName() == null) {
                Log.d(TAG, "onAccessibilityEvent package name or class name is null");
                return;
            }
            ComponentName componentName = new ComponentName(
                    event.getPackageName().toString(),
                    event.getClassName().toString()
            );

            ActivityInfo activityInfo = tryGetActivity(componentName);
            boolean isActivity = activityInfo != null;
            if (isActivity) {
//                Toast.makeText(ForegroundDetectingService.this, componentName.flattenToShortString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RetailAppManagerConst.ACTION_FOREGROUND_DETECTED);
                intent.putExtra(RetailAppManagerConst.ARG_DETECTED_FOREGROUND, componentName.flattenToShortString());
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
