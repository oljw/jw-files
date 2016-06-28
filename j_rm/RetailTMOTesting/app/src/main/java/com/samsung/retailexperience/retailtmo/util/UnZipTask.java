package com.samsung.retailexperience.retailtmo.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;

/**
 * Created by smheo on 1/28/2016.
 */
public class UnZipTask extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = UnZipTask.class.getSimpleName();

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            Log.d(TAG, "@@@ doInBackground+) " + params[0]);
            if (params[0] == null) return false;

            String zipFilePath = params[0];
            FileUtil fileUtil = new FileUtil();

            if (fileUtil == null) return false;
            if (fileUtil.unzipToFolder(zipFilePath)) {
                File deletingZip = new File(zipFilePath);
                Log.d(TAG, "@@@ starting delete zipfile");
                if (deletingZip != null) {
                    Log.d(TAG, "@@@ I have a zipfile");
                    if (deletingZip.exists()) {
                        Log.d(TAG, "@@@ deleting zip file");
                        deletingZip.delete();
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        Log.d(TAG, "@@@@@ Finished unzip");
    }
}