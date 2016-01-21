package com.samsung.retailexperience.retailhero.util;

import android.content.Context;

import com.google.gson.Gson;
import com.samsung.retailexperience.retailhero.RetailHeroApplication;
import com.samsung.retailexperience.retailhero.gson.models.MenuModel;

/**
 * Created by icanmobile on 1/15/16.
 */
public class ModelUtil {
    private static final String TAG = ModelUtil.class.getSimpleName();

    public static MenuModel getMenuModel(String title, String json) {
        Context context = RetailHeroApplication.getContext();
        String data = AssetUtil.GetTextFromAsset(context, json);

        Gson gson = new Gson();
        MenuModel menuModel = gson.fromJson(data, MenuModel.class);
        menuModel.setTitle(title);
        return menuModel;
    }
}
