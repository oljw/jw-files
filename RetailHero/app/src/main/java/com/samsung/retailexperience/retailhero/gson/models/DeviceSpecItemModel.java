package com.samsung.retailexperience.retailhero.gson.models;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by icanmobile on 2/2/16.
 */
public class DeviceSpecItemModel extends ResourceModel {
    public String layout;
    public String deviceImage;
    public String deviceName;
    public String deviceNameLarge;
    public String deviceSpecItem;
    public String deviceSpec;
    public String comparedDeviceImage;
    public String comparedDeviceName;
    public String comparedDeviceSpec;
    public String action;
    public ArrayList<String> colors = new ArrayList<String>();
    public ArrayList<String> comparedColors = new ArrayList<String>();

    public DeviceSpecItemModel() {
        this.layout = null;
        this.deviceImage = null;
        this.deviceName = null;
        this.deviceNameLarge = null;
        this.deviceSpecItem = null;
        this.deviceSpec = null;
        this.comparedDeviceImage = null;
        this.comparedDeviceName = null;
        this.comparedDeviceSpec = null;
        this.colors.clear();
        this.comparedColors.clear();
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


    public String getDeviceNameLarge() {
        return this.deviceNameLarge;
    }
    public int getDeviceNameLargeResId() {
        if (this.deviceNameLarge != null)
            return getResId(this.deviceNameLarge);
        return 0;
    }
    public void setDeviceNameLarge(String deviceNameLarge) {
        this.deviceNameLarge = deviceNameLarge;
    }


    public String getDeviceSpecItem() {
        return this.deviceSpecItem;
    }
    public int getDeviceSpecItemResId() {
        if (this.deviceSpecItem != null)
            return getResId(this.deviceSpecItem);
        return 0;
    }
    public void setDeviceSpecItem(String deviceSpecItem) {
        this.deviceSpecItem = deviceSpecItem;
    }

    public String getDeviceSpec() {
        return this.deviceSpec;
    }
    public int getDeviceSpecResId() {
        if (this.deviceSpec != null)
            return getResId(this.deviceSpec);
        return 0;
    }


    public String getComparedDeviceImage() {
        return this.comparedDeviceImage;
    }
    public int getComparedDeviceImageResId() {
        if (this.comparedDeviceImage != null)
            return getResId(this.comparedDeviceImage);
        return 0;
    }
    public void setComparedDeviceImage(String comparedDeviceImage) {
        this.comparedDeviceImage = comparedDeviceImage;
    }

    public String getComparedDeviceName() {
        return this.comparedDeviceName;
    }
    public int getComparedDeviceNameResId() {
        if (this.comparedDeviceName != null)
            return getResId(this.comparedDeviceName);
        return 0;
    }
    public void setComparedDeviceName(String comparedDeviceName) {
        this.comparedDeviceName = comparedDeviceName;
    }


    public String getComparedDeviceSpec() {
        return this.comparedDeviceSpec;
    }
    public int getComparedDeviceSpecResId() {
        if (this.comparedDeviceSpec != null)
            return getResId(this.comparedDeviceSpec);
        return 0;
    }
    public void setComparedDeviceSpec(String comparedDeviceSpec) {
        this.comparedDeviceSpec = comparedDeviceSpec;
    }


    public String getAction() {
        return this.action;
    }
    public void setAction(String action) {
        this.action = action;
    }

    public ArrayList<String> getColors() {
        return this.colors;
    }
    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }
    public int getColorResId(int index) {
        if (this.colors.size() <= index) return 0;
        if (this.colors.get(index) != null)
            return getResId(this.colors.get(index));
        return 0;
    }

    public ArrayList<String> getComparedColors() {
        return this.comparedColors;
    }
    public void setComparedColors(ArrayList<String> comparedColors) {
        this.comparedColors = comparedColors;
    }
    public int getComparedColorResId(int index) {
        if (this.comparedColors.size() <= index) return 0;
        if (this.comparedColors.get(index) != null)
            return getResId(this.comparedColors.get(index));
        return 0;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.layout != null)
            appendString(builder, "layout = " + this.layout);
        if (this.deviceImage != null)
            appendString(builder, "deviceImage = " + this.deviceImage);
        if (this.deviceName != null)
            appendString(builder, "deviceName = " + this.deviceName);
        if (this.deviceSpecItem != null)
            appendString(builder, "deviceSpecItem = " + this.deviceSpecItem);
        if (this.deviceSpec != null)
            appendString(builder, "deviceSpec = " + this.deviceSpec);
        if (this.comparedDeviceImage != null)
            appendString(builder, "comparedDeviceImage = " + this.comparedDeviceImage);
        if (this.comparedDeviceName != null)
            appendString(builder, "comparedDeviceName = " + this.comparedDeviceName);
        if (this.comparedDeviceSpec != null)
            appendString(builder, "comparedDeviceSpec = " + this.comparedDeviceSpec);
        if (this.action != null)
            appendString(builder, "action = " + this.action);
        return builder.toString();
    }

    private void appendString(StringBuilder builder, String value) {
        builder.append(value + System.getProperty("line.separator"));
    }

    public void print() {
        Log.d(DeviceSpecItemModel.class.getName(), toString());
    }

}
