package com.tecace.app.manager.gson.model;

import com.tecace.retail.appmanager.util.ResourceUtil;

import java.io.Serializable;

/**
 * Created by icanmobile on 3/1/16.
 */
public abstract class BaseModel implements Serializable {
    public int getResId(String data) {
        return ResourceUtil.getResId(data);
    }

    public abstract String toString();
    public void appendString(StringBuilder builder, String value) {
        builder.append(value + System.getProperty("line.separator"));
    }
    public abstract void print();
}
