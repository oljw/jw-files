package com.samsung.retailexperience.retailhero.gson.models;

import android.util.Log;

/**
 * Created by icanmobile on 2/2/16.
 */
public class DeviceItemModel extends ResourceModel {
    public String layout;
    public String deviceImage;
    public String deviceName;
    public String isSelected;
    public String tag;

    public DeviceItemModel() {
        this.layout = null;
        this.deviceImage = null;
        this.deviceName = null;
        this.isSelected = null;
        this.tag = null;
    }

    public String getLayout() {
        return this.layout;
    }
    public int getLayoutResId() {
        if (this.layout != null)
            return getResId(this.layout);
        return 0;
    }
    public void setLayout(String layout) {
        this.layout = layout;
    }


    public String getDeviceImage() {
        return this.deviceImage;
    }
    public int getDeviceImageResId() {
        if (this.deviceImage != null)
            return getResId(this.deviceImage);
        return 0;
    }
    public void setDeviceImage(String deviceImage) {
        this.deviceImage = deviceImage;
    }

    public String getDeviceName() {
        return this.deviceName;
    }
    public int getDeviceNameResId() {
        if (this.deviceName != null)
            return getResId(this.deviceName);
        return 0;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isSelected() {
        if (this.isSelected != null)
            return true;
        return false;
    }
    public void setSelected(boolean isSelected) {
        if (isSelected)
            this.isSelected = "true";
        else
            this.isSelected = null;
    }

    public String getTag() {
        return this.tag;
    }
    public int getTagResId() {
        if (this.tag != null)
            return getResId(this.tag);
        return 0;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }


    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.layout != null)
            appendString(builder, "layout = " + this.layout);
        if (this.deviceImage != null)
            appendString(builder, "deviceImage = " + this.deviceImage);
        if (this.deviceName != null)
            appendString(builder, "deviceName = " + this.deviceName);
        if (this.isSelected != null)
            appendString(builder, "isSelected = " + this.isSelected);
        if (this.tag != null)
            appendString(builder, "tag = " + this.tag);
        return builder.toString();
    }

    private void appendString(StringBuilder builder, String value) {
        builder.append(value + System.getProperty("line.separator"));
    }

    public void print() {
        Log.d(DeviceItemModel.class.getName(), toString());
    }

}
