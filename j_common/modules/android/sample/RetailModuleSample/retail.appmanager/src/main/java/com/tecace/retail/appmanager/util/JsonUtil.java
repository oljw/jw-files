package com.tecace.retail.appmanager.util;

import android.content.Context;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by icanmobile on 3/1/16.
 */
public class JsonUtil {
    private static final String TAG = JsonUtil.class.getSimpleName();

    private static JsonUtil sInstance = null;
    public static JsonUtil getInstance() {
        if (sInstance == null)
            sInstance = new JsonUtil();
        return sInstance;
    }

    public <T> T loadJsonModel(Context context, String json, Class<T> classOfT) {
        try {
            String data = AssetUtil.getInstance().GetTextFromAsset(context, json);
            Gson gson = new Gson();
            return gson.fromJson(data, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T loadJsonModel(Context context, String json, Type type) {
        try {
            String data = AssetUtil.getInstance().GetTextFromAsset(context, json);
            Gson gson = new Gson();
            return gson.fromJson(data, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
