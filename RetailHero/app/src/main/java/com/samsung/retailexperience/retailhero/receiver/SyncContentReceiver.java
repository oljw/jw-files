package com.samsung.retailexperience.retailhero.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SyncContentReceiver extends BroadcastReceiver {

    private static final String TAG = SyncContentReceiver.class.getName();

    private static final String ACTION_SYNC_STARTED = "com.samsung.retailcloud.content.sync.STARTED";
    private static final String ACTION_SYNC_ABORTED = "com.samsung.retailcloud.content.sync.ABORTED";
    private static final String ACTION_SYNC_COMPLETED = "com.samsung.retailcloud.content.sync.COMPLETED";

    private static final String ACTION_NOTIFY_PARTNER = "com.samsung.retailcloud.content.UPDATE";
    private static final String EXTRA_FILENAME = "filename";
    private static final String EXTRA_DELETED = "deleted";

    public SyncContentReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case ACTION_SYNC_STARTED :
                //Log.i(TAG, "SYNC STARTED");
                break;
            case ACTION_SYNC_ABORTED :
                Log.e(TAG, "SYNC ABORTED");
                break;
            case ACTION_SYNC_COMPLETED :
                //Log.i(TAG, "SYNC COMPLETED");
                break;
            case ACTION_NOTIFY_PARTNER :
                String filename = intent.getStringExtra(EXTRA_FILENAME);
                boolean isDeleted = intent.getBooleanExtra(EXTRA_DELETED, false);

                Log.i(TAG, filename + ":" + isDeleted);
                break;
        }
    }
}
