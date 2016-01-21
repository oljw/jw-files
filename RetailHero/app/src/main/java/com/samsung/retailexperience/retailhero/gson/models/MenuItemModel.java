package com.samsung.retailexperience.retailhero.gson.models;

import android.util.Log;

/**
 * Created by icanmobile on 1/12/16.
 */
public class MenuItemModel extends ResourceModel {
    public String layout;
    public String title;
    public String icon;
    public String tag;
    public String action;

    public MenuItemModel() {
        this(null, null, null, null, null);
    }
    public MenuItemModel(String layout) {
        this(layout, null, null, null, null);
    }
    public MenuItemModel(String layout,
                         String title,
                         String tag) {
        this(layout, title, tag, null, null);
    }
    public MenuItemModel(String layout,
                         String title,
                         String tag,
                         String action) {
        this(layout, title, tag, action, null);
    }
    public MenuItemModel(String layout,
                         String title,
                         String tag,
                         String action,
                         String icon) {
        this.layout = layout;
        this.title  = title;
        this.icon   = icon;
        this.tag    = tag;
        this.action = action;
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



    public String getIcon() {
        return this.icon;
    }
    public int getIconResId() {
        if (this.icon != null)
            return getResId(this.icon);
        return 0;
    }
    public void setIcon(String icon) {
        this.icon = icon;
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




    public String getTag() {
        return this.tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }




    public String getAction() {
        return this.action;
    }
    public void setAction(String action) {
        this.action = action;
    }




    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.title != null)
            appendString(builder, "title = " + this.title);
        if (this.layout != null)
            appendString(builder, "layout = " + this.layout);
        if (this.tag != null)
            appendString(builder, "tag = " + this.tag);
        if (this.action != null)
            appendString(builder, "action = " + this.action);
        return builder.toString();
    }

    private void appendString(StringBuilder builder, String value) {
        builder.append(value + System.getProperty("line.separator"));
    }

    public void print() {
        Log.d(MenuItemModel.class.getName(), toString());
    }
}
