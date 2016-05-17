package com.samsung.retailexperience.retailhero.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by icanmobile on 1/15/16.
 */
public class AssetUtil {
    private static final String TAG = AssetUtil.class.getSimpleName();

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

}
