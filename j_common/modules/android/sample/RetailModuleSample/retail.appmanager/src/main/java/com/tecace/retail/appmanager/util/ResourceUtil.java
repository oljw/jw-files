package com.tecace.retail.appmanager.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tecace.retail.appmanager.ui.activity.MissingContentActivity;

import java.io.File;
import java.util.List;

/**
 * Created by smheo on 9/29/2015.
 */
public class ResourceUtil {
    private final String TAG = ResourceUtil.class.getSimpleName();

    private static ResourceUtil sInstance = null;
    public static ResourceUtil getInstance() {
        if (sInstance == null)
            sInstance = new ResourceUtil();
        return sInstance;
    }

    public int getResId(Context context, String data) {
        int delimPos = data.indexOf('/');
        if (delimPos == -1 ) {
            return 0;
        }
        String[] vals = new String[2];
        vals[0] = data.substring(1, delimPos);
        vals[1] = data.substring(delimPos + 1);
        return context.getResources().getIdentifier(vals[1], vals[0], context.getPackageName());
    }

    public String getContentFilePath(Context context, String fileName){
        final String dir = context.getExternalFilesDir(null).toString();
        return String.format("%s/%s", dir, fileName);
    }

    public boolean isMissingContentFile(Context context, String filename) {
        if (filename == null) {
            return true;
        }

        File current = new File(getContentFilePath(context, filename));
        boolean exists = !(current.exists());

        if (exists) {
            Log.e(TAG, "Missing Content : " + current.getAbsolutePath());
            startMissingContentActivity(context, filename);
        }

        return exists;
    }

    public boolean isMissingFile(Context context, String fullpath) {
        File current = new File(fullpath);
        boolean exists = !(current.exists());
        if (exists) {
            Log.e(TAG, "Missing file : " + current.getAbsolutePath());
        }

        return exists;
    }

    public boolean isMissingFileList(Context context, List<String> filelist) {
        if (filelist == null ||filelist.isEmpty()) {
            Log.e(TAG, "The file list is empty");
            return true;
        }

        for (String filename : filelist) {
            if (isMissingFile(context, getContentFilePath(context, filename))) {
                return true;
            }
        }

        return false;
    }

    //icanmobile (+) test
    private void startMissingContentActivity(Context context, String filename) {
        try {
            Intent intent = new Intent(context, MissingContentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setAction(RetailAppManagerConst.ACTION_MISSING_FILE);
            intent.putExtra(RetailAppManagerConst.WHAT_MISSING_FILE, filename);
            context.startActivity(intent);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

