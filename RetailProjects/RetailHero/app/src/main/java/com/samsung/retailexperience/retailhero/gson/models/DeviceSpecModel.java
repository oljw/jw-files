package com.samsung.retailexperience.retailhero.gson.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by icanmobile on 2/2/16.
 */
public class DeviceSpecModel extends ResourceModel implements Serializable {

    public String action;
    public ArrayList<DeviceSpecItemModel> deviceSpecItems = new ArrayList<DeviceSpecItemModel>();
    public String closeAction;

    public DeviceSpecModel() {
        this.action = null;
        deviceSpecItems.clear();
    }

    public DeviceSpecModel(String action,
                           ArrayList<DeviceSpecItemModel> deviceSpecItems) {
        this(action, deviceSpecItems, null);
    }
    public DeviceSpecModel(String action,
                           ArrayList<DeviceSpecItemModel> deviceSpecItems,
                           String closeAction) {
        this.action = action;
        this.deviceSpecItems = deviceSpecItems;
        this.closeAction = closeAction;
    }

    public String getAction() {
        return this.action;
    }
    public void setAction(String action) {
        this.action = action;
    }

    public ArrayList<DeviceSpecItemModel> getDeviceSpecItems() {
        return this.deviceSpecItems;
    }
    public void setDeviceSpecItems(ArrayList<DeviceSpecItemModel> deviceSpecItems) {
        this.deviceSpecItems = deviceSpecItems;
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
        if (this.deviceSpecItems != null)
            appendString(builder, "deviceSpecItems = " + this.deviceSpecItems);
        return builder.toString();
    }

    private void appendString(StringBuilder builder, String value) {
        builder.append(value + System.getProperty("line.separator"));
    }

    public void print() {
        Log.d(DeviceSpecModel.class.getName(), toString());
    }
}
