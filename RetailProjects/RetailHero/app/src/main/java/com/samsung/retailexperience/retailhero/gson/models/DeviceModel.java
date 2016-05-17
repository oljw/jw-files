package com.samsung.retailexperience.retailhero.gson.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by icanmobile on 2/2/16.
 */
public class DeviceModel extends ResourceModel implements Serializable {
    public String action;
    public ArrayList<DeviceItemModel> devices = new ArrayList<DeviceItemModel>();
    public String closeAction;

    public DeviceModel() {
        this.action = null;
        devices.clear();
        this.closeAction = null;
    }

    public DeviceModel(String action,
                       ArrayList<DeviceItemModel> deviceSpecItems) {
        this(action, deviceSpecItems, null);
    }
    public DeviceModel(String action,
                       ArrayList<DeviceItemModel> deviceSpecItems,
                       String closeAction) {
        this.action = action;
        this.devices = deviceSpecItems;
        this.closeAction = closeAction;
    }

    public String getAction() {
        return this.action;
    }
    public void setAction(String action) {
        this.action = action;
    }


    public ArrayList<DeviceItemModel> getDeviceItems() {
        return this.devices;
    }
    public void setDeviceItems(ArrayList<DeviceItemModel> devices) {
        this.devices = devices;
    }


    public String getCloseAction() {
        return this.closeAction;
    }
    public void setCloseAction(String closeAction) {
        this.closeAction = closeAction;
    }


    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.action != null)
            appendString(builder, "action = " + this.action);
        if (this.devices != null)
            appendString(builder, "devices = " + this.devices);
        return builder.toString();
    }

    private void appendString(StringBuilder builder, String value) {
        builder.append(value + System.getProperty("line.separator"));
    }

    public void print() {
        Log.d(DeviceModel.class.getName(), toString());
    }
}
