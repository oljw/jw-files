package com.samsung.retailexperience.retailecosystem.video.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.List;

/**
 * Created by smheo on 9/29/2015.
 */
public class ResourceUtil {
    private final String TAG = ResourceUtil.class.getSimpleName();

    public interface MissingResourceListener {
        void onMissingResource(String missingfile);
    }

    private Context context;
    private MissingResourceListener mMissingListener;


    public ResourceUtil(Context context){
        this.context = context;
    }

    public void setMissingResourceListener(MissingResourceListener listener) {
        this.mMissingListener = listener;
    }

    public String getContentFilePath(String fileName){
        final String dir = context.getExternalFilesDir(null).toString();
        return String.format("%s/%s", dir, fileName);
    }

    public boolean isMissingContentFile(String filename) {
        if (filename == null) {
            return true;
        }
        File current = new File(getContentFilePath(filename));
        boolean exists = !(current.exists());
        if (exists) {
            Log.e(TAG, "Missing Content : " + current.getAbsolutePath());
            if (mMissingListener != null) {
                mMissingListener.onMissingResource(filename);
            }
        }

        return exists;
    }

    public boolean isMissingFile(String fullpath) {
        File current = new File(fullpath);
        boolean exists = !(current.exists());
        if (exists) {
            Log.e(TAG, "Missing file : " + current.getAbsolutePath());
        }

        return exists;
    }

    public boolean isMissingFileList(List<String> filelist) {
        if (filelist == null ||filelist.isEmpty()) {
            Log.e(TAG, "The file list is empty");
            return true;
        }

        for (String filename : filelist) {
            if (isMissingFile(getContentFilePath(filename))) {
                return true;
            }
        }

        return false;
    }
}

