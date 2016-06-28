package com.tecace.retail.appmanager.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tecace.retail.appmanager.RetailApplication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by icanmobile on 3/1/16.
 */
public class AssetUtil {
    private static final String TAG = AssetUtil.class.getSimpleName();

    private static final String DEF_TYPE_DRAWABLE = "drawable";

    private static AssetUtil sInstance = null;
    public static AssetUtil getInstance() {
        if (sInstance == null)
            sInstance = new AssetUtil();
        return sInstance;
    }

    public static String GetTextFromAsset(Context context, String filename) {
        StringBuilder ret = new StringBuilder();

        try {
            InputStream is = context.getAssets().open(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader f = new BufferedReader(inputStreamReader);
            String line = f.readLine();
            while (line != null) {
                ret.append(line);
                line = f.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret.toString();
    }

    public static Bitmap getBitmap(String imageName) {
        Context context = RetailApplication.getContext();

        int dotPos = imageName.indexOf('.');

        String imageNameNoExtension;
        if (dotPos >= 0) {
            imageNameNoExtension = imageName.substring(0, dotPos);
        } else {
            imageNameNoExtension = imageName;
        }
        int resourceId = ResourceUtil.getResId(imageNameNoExtension, DEF_TYPE_DRAWABLE);

        if (resourceId == 0) {
            return null;
        }

        return BitmapFactory.decodeResource(context.getResources(), resourceId);
    }
}
