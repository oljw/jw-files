package com.samsung.retailexperience.retailtmo.gson.models;

import android.util.Log;

/**
 * Created by icanmobile on 3/1/16.
 */
public class MenuItemModel extends BaseModel {
    public String layout;
    public String title;
    public String subTitle;
    public String background;
    public String action;

    public MenuItemModel() {
        this(null, null, null, null, null);
    }
    public MenuItemModel(String layout,
                         String title,
                         String subTitle,
                         String background,
                         String action) {
        this.layout     = layout;
        this.title      = title;
        this.subTitle   = subTitle;
        this.background = background;
        this.action     = action;
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


    public String getBackground() {
        return this.background;
    }
    public int getBackgroundResId() {
        if (this.background != null)
            return getResId(this.background);
        return 0;
    }
    public void setBackground(String background) {
        this.background = background;
    }


    public String getAction() {
        return this.action;
    }
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.layout != null)
            appendString(builder, "layout = " + this.layout);
        if (this.title != null)
            appendString(builder, "title = " + this.title);
        if (this.subTitle != null)
            appendString(builder, "subTitle = " + this.subTitle);
        if (this.background != null)
            appendString(builder, "background = " + this.background);
        if (this.action != null)
            appendString(builder, "action = " + this.action);
        return builder.toString();
    }

    @Override
    public void print() {
        Log.d(MenuItemModel.class.getName(), toString());
    }
}
