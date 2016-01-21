package com.samsung.retailexperience.retailhero.gson.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by icanmobile on 1/12/16.
 */
public class MenuModel extends ResourceModel implements Serializable {
    public String title;
    public ArrayList<MenuItemModel> menuItems = new ArrayList<MenuItemModel>();

    public MenuModel() {
        this.title = null;
        this.menuItems.clear();
    }
    public MenuModel(String title,
                     ArrayList<MenuItemModel> menuItems) {
        this.title = null;
        this.menuItems = menuItems;
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

    public ArrayList<MenuItemModel> getMenuItems() {
        return this.menuItems;
    }
    public void setMenuItems(ArrayList<MenuItemModel> menuItems) {
        this.menuItems = menuItems;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.title != null)
            appendString(builder, "title = " + this.title);
        if (this.menuItems != null) {
            for (MenuItemModel item : this.menuItems) {
                appendString(builder, "[");
                appendString(builder, this.menuItems.toString());
                appendString(builder, "]");
            }
        }
        return builder.toString();
    }

    private void appendString(StringBuilder builder, String value) {
        builder.append(value + System.getProperty("line.separator"));
    }

    public void print() {
        Log.d(MenuModel.class.getName(), toString());
    }
}
