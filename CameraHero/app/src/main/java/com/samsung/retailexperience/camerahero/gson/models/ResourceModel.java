package com.samsung.retailexperience.camerahero.gson.models;

import android.content.Context;

import com.samsung.retailexperience.camerahero.CameraHeroApplication;

/**
 * Created by icanmobile on 1/12/16.
 */
public class ResourceModel {
    public int getResId(String data) {
        Context context = CameraHeroApplication.getContext();
        int delimPos = data.indexOf('/');
        String[] vals = new String[2];
        vals[0] = data.substring(1, delimPos);
        vals[1] = data.substring(delimPos + 1);
        return context.getResources().getIdentifier(vals[1], vals[0], context.getPackageName());
    }
}
