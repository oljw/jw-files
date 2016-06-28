package com.samsung.retailexperience.retailecosystem.gson.models;

import android.content.Context;

import com.samsung.retailexperience.retailecosystem.RetailEcoSystemApplication;

import java.io.Serializable;

/**
 * Created by icanmobile on 3/1/16.
 */
public abstract class BaseModel implements Serializable {
    public int getResId(String data) {
        Context context = RetailEcoSystemApplication.getContext();
        int delimPos = data.indexOf('/');
        if (delimPos == -1 ) {
            return 0;
        }
        String[] vals = new String[2];
        vals[0] = data.substring(1, delimPos);
        vals[1] = data.substring(delimPos + 1);
        return context.getResources().getIdentifier(vals[1], vals[0], context.getPackageName());
    }

    public abstract String toString();
    public void appendString(StringBuilder builder, String value) {
        builder.append(value + System.getProperty("line.separator"));
    }
    public abstract void print();
}
