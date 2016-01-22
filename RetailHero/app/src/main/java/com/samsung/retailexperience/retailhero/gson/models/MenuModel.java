package com.samsung.retailexperience.retailhero.gson.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by icanmobile on 1/12/16.
 */
public class MenuModel extends ResourceModel implements Serializable {
    public String title;
    public String subTitle;
    public ArrayList<MenuItemModel> menuItems = new ArrayList<MenuItemModel>();
    public String videoTitle;
    public String videoSubTitle;
    public String videoAction;

    public MenuModel() {
        this.title = null;
        this.subTitle = null;
        this.menuItems.clear();
        this.videoTitle = null;
        this.videoSubTitle = null;
    }
    public MenuModel(String title, String subTitle,
                     ArrayList<MenuItemModel> menuItems,
                     String videoTitle, String videoSubTitle, String videoAction) {
        this.title = title;
        this.subTitle = subTitle;
        this.menuItems = menuItems;
        this.videoTitle = videoTitle;
        this.videoSubTitle = videoSubTitle;
        this.videoAction = videoAction;
    }

    public String getTitle() {
        return this.title;
    }
    public int getTitleResId() {
        if (this.title != null)
            return getResId(this.title);
        return 0;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return this.subTitle;
    }
    public int getSubTitleResId() {
        if (this.subTitle != null)
            return getResId(this.subTitle);
        return 0;
    }
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public ArrayList<MenuItemModel> getMenuItems() {
        return this.menuItems;
    }
    public void setMenuItems(ArrayList<MenuItemModel> menuItems) {
        this.menuItems = menuItems;
    }

    public String getVideoTitle() {
        return this.videoTitle;
    }
    public int getVideoTitleResId() {
        if (this.videoTitle != null)
            return getResId(this.videoTitle);
        return 0;
    }
    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoSubTitle() {
        return this.videoSubTitle;
    }
    public int getVideoSubTitleResId() {
        if (this.videoSubTitle != null)
            return getResId(this.videoSubTitle);
        return 0;
    }
    public void setVideoSubTitle(String videoSubTitle) {
        this.videoSubTitle = videoSubTitle;
    }

    public String getVideoAction() {
        return this.videoAction;
    }
    public void setVideoAction(String videoAction) {
        this.videoAction = videoAction;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.title != null)
            appendString(builder, "title = " + this.title);

        if (this.subTitle != null)
            appendString(builder, "subTitle = " + this.subTitle);

        if (this.menuItems != null) {
            for (MenuItemModel item : this.menuItems) {
                appendString(builder, "[");
                appendString(builder, this.menuItems.toString());
                appendString(builder, "]");
            }
        }

        if (this.videoTitle != null)
            appendString(builder, "videoTilte = " + this.videoTitle);

        if (this.videoSubTitle != null)
            appendString(builder, "videoSubTitle = " + this.videoSubTitle);

        return builder.toString();
    }

    private void appendString(StringBuilder builder, String value) {
        builder.append(value + System.getProperty("line.separator"));
    }

    public void print() {
        Log.d(MenuModel.class.getName(), toString());
    }
}
