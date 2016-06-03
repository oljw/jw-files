package com.samsung.retailexperience.retailtmo.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by icanmobile on 3/1/16.
 */
public class JsonUtil {
    private static final String TAG = JsonUtil.class.getSimpleName();
// J.K
//    public static <T> T loadJsonModel(Context context, String json, Class<T> classOfT) {
//        String data = AssetUtil.GetTextFromAsset(context, json);
//        Gson gson = new Gson();
//        return gson.fromJson(data, classOfT);
//    }
//
//    public static <T> T loadJsonModel(Context context, String json, Type type) {
//        String data = AssetUtil.GetTextFromAsset(context, json);
//        Gson gson = new Gson();
//        return gson.fromJson(data, type);
//    }

    public static <T> T loadJsonResource(Context context, String json, Class<T> classOfT) {
        String data = FileUtil.getTextFromResource(context, json);
        Gson gson = new Gson();
        return gson.fromJson(data, classOfT);
    }

    public static <T> T loadJsonModel(Context context, String json, Type type) {
        Log.e(TAG, "loadJsonModel json: " + json);
        String data = FileUtil.getTextFromResource(context, json);
        Gson gson = new Gson();
        return gson.fromJson(data, type);
    }

}
