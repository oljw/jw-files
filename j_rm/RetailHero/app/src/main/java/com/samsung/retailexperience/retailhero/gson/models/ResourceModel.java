package com.samsung.retailexperience.retailhero.gson.models;

import android.content.Context;

import com.samsung.retailexperience.retailhero.RetailHeroApplication;

import java.io.Serializable;

/**
 * Created by icanmobile on 1/12/16.
 */
public class ResourceModel implements Serializable {
    public int getResId(String data) {
        Context context = RetailHeroApplication.getContext();
        int delimPos = data.indexOf('/');
        if (delimPos == -1 ) {
            return 0;
        }
        String[] vals = new String[2];
        vals[0] = data.substring(1, delimPos);
        vals[1] = data.substring(delimPos + 1);
        return context.getResources().getIdentifier(vals[1], vals[0], context.getPackageName());
    }
}
