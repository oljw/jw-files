package com.developer.jw.ecosystemcopy.gson.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by icanmobile on 3/1/16.
 */
public class MenuModel extends BaseModel implements Serializable {
    public ArrayList<String> title = new ArrayList<String>();
    public ArrayList<String> subTitle = new ArrayList<String>();
    public ArrayList<MenuItemModel> menuItems = new ArrayList<MenuItemModel>();

    public MenuModel() {
        this(null, null, null);
    }
    public MenuModel(ArrayList<String> title,
                     ArrayList<String> subTitle,
                     ArrayList<MenuItemModel> menuItems) {
        this.title      = title;
        this.subTitle   = subTitle;
        this.menuItems  = menuItems;
    }


    public ArrayList<String> getTitle() {
        return this.title;
    }
    public int getTitleResId(int index) {
        if (this.title.size() <= index) return 0;
        if (this.title.get(index) != null)
            return getResId(this.title.get(index));
        return 0;
    }
    public void setTitle(ArrayList<String> title) {
        this.title = title;
    }
    public void setTitle(String title) {
        this.title.add(title);
    }


    public ArrayList<String> getSubTitle() {
        return this.subTitle;
    }
    public int getSubTitleResId(int index) {
        if (this.subTitle.size() <= index) return 0;
        if (this.subTitle != null)
            return getResId(this.subTitle.get(index));
        return 0;
    }
    public void setSubTitle(ArrayList<String> subTitle) {
        this.subTitle = subTitle;
    }
    public void setSubTitle(String subTitle) {
        this.subTitle.add(subTitle);
    }


    public ArrayList<MenuItemModel> getMenuItems() {
        return this.menuItems;
    }
    public void setMenuItems(ArrayList<MenuItemModel> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.title != null) {
            for (int t=0; t<this.title.size(); t++)
                appendString(builder, "title[" + t + "] = " + this.title.get(t));
        }
        if (this.subTitle != null) {
            for (int s=0; s<this.subTitle.size(); s++)
                appendString(builder, "subTitle[" + s + "] = " + this.subTitle.get(s));
        }
        if (this.menuItems != null) {
            for (MenuItemModel item : this.menuItems) {
                appendString(builder, "[");
                appendString(builder, this.menuItems.toString());
                appendString(builder, "]");
            }
        }
        return builder.toString();
    }

    @Override
    public void print() {
        Log.d(MenuModel.class.getName(), toString());
    }
}
