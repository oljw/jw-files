package com.samsung.retailexperience.retailhero.util;

import android.content.Context;

import com.google.gson.Gson;
import com.samsung.retailexperience.retailhero.RetailHeroApplication;
import com.samsung.retailexperience.retailhero.gson.models.DeviceModel;
import com.samsung.retailexperience.retailhero.gson.models.DeviceSpecModel;
import com.samsung.retailexperience.retailhero.gson.models.MenuModel;

/**
 * Created by icanmobile on 1/15/16.
 */
public class ModelUtil {
    private static final String TAG = ModelUtil.class.getSimpleName();

    public static MenuModel getMenuModel(String title,
                                         String subTitle,
                                         String json,
                                         String videoTitle,
                                         String videoSubTitle,
                                         String videoAction) {
        Context context = RetailHeroApplication.getContext();
        String data = AssetUtil.GetTextFromAsset(context, json);

        Gson gson = new Gson();
        MenuModel menuModel = gson.fromJson(data, MenuModel.class);
        menuModel.setTitle(title);
        menuModel.setSubTitle(subTitle);
        menuModel.setVideoTitle(videoTitle);
        menuModel.setVideoSubTitle(videoSubTitle);
        menuModel.setVideoAction(videoAction);
        return menuModel;
    }

    public static MenuModel getMenuModel(String title,
                                         String subTitle,
                                         String json,
                                         String videoTitle,
                                         String videoSubTitle,
                                         String videoBackground,
                                         String videoAction,
                                         String dots,
                                         String dot) {
        Context context = RetailHeroApplication.getContext();
        String data = AssetUtil.GetTextFromAsset(context, json);

        Gson gson = new Gson();
        MenuModel menuModel = gson.fromJson(data, MenuModel.class);
        menuModel.setTitle(title);
        menuModel.setSubTitle(subTitle);
        menuModel.setVideoTitle(videoTitle);
        menuModel.setVideoSubTitle(videoSubTitle);
        menuModel.setVideoBackground(videoBackground);
        menuModel.setVideoAction(videoAction);
        menuModel.setDots(dots);
        menuModel.setDot(dot);
        return menuModel;
    }

    public static DeviceSpecModel getDeviceSpecModel(String action, String json) {
        Context context = RetailHeroApplication.getContext();
        String data = AssetUtil.GetTextFromAsset(context, json);

        Gson gson = new Gson();
        DeviceSpecModel deviceSpecModel = gson.fromJson(data, DeviceSpecModel.class);
        deviceSpecModel.setAction(action);
        return deviceSpecModel;
    }

    public static DeviceSpecModel getDeviceSpecModel(String action, String deviceJson, String comparedDeviceJson) {
        Context context = RetailHeroApplication.getContext();
        String deviceData = AssetUtil.GetTextFromAsset(context, deviceJson);
        String comparedDeviceData = AssetUtil.GetTextFromAsset(context, comparedDeviceJson);

        Gson gson = new Gson();
        DeviceSpecModel deviceSpecModel = gson.fromJson(deviceData, DeviceSpecModel.class);
        deviceSpecModel.setAction(action);

        DeviceSpecModel comparedDeviceSpecModel = gson.fromJson(comparedDeviceData, DeviceSpecModel.class);
        if (comparedDeviceSpecModel == null) return null;

        for(int i=0; i< comparedDeviceSpecModel.getDeviceSpecItems().size(); i++) {
            if (comparedDeviceSpecModel.getDeviceSpecItems().get(i).getDeviceImage() != null)
                deviceSpecModel.getDeviceSpecItems().get(i).setComparedDeviceImage(comparedDeviceSpecModel.getDeviceSpecItems().get(i).getDeviceImage());
            if (comparedDeviceSpecModel.getDeviceSpecItems().get(i).getDeviceName() != null)
                deviceSpecModel.getDeviceSpecItems().get(i).setComparedDeviceName(comparedDeviceSpecModel.getDeviceSpecItems().get(i).getDeviceName());
            if (comparedDeviceSpecModel.getDeviceSpecItems().get(i).getDeviceSpec() != null)
                deviceSpecModel.getDeviceSpecItems().get(i).setComparedDeviceSpec(comparedDeviceSpecModel.getDeviceSpecItems().get(i).getDeviceSpec());
            if (comparedDeviceSpecModel.getDeviceSpecItems().get(i).getColors() != null)
                deviceSpecModel.getDeviceSpecItems().get(i).setComparedColors(comparedDeviceSpecModel.getDeviceSpecItems().get(i).getColors());
        }
        return deviceSpecModel;
    }

    public static DeviceModel getDeviceModel(String action, String json) {
        Context context = RetailHeroApplication.getContext();
        String data = AssetUtil.GetTextFromAsset(context, json);

        Gson gson = new Gson();
        DeviceModel deviceModel = gson.fromJson(data, DeviceModel.class);
        deviceModel.setAction(action);
        return deviceModel;
    }
}
