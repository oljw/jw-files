package com.samsung.retailexperience.retailtmo.gson.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by icanmobile on 3/22/16.
 */
public class ListModel extends BaseModel implements Serializable {
    public String background;
    public String fabColor;
    public String fabImage;
    public ArrayList<ListItemModel> listItems = new ArrayList<ListItemModel>();

    public ListModel() {
        this(null, null, null, null);
    }
    public ListModel(String background,
                     String fabColor,
                     String fabImage,
                     ArrayList<ListItemModel> listItems) {
        this.background         = background;
        this.fabColor           = fabColor;
        this.fabImage           = fabImage;
        this.listItems          = listItems;
    }

    // background resource id
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


    // FAB (floating action button) color
    public String getFabColor() {
        return this.fabColor;
    }
    public int getFabColorResId() {
        if (this.fabColor != null)
            return getResId(this.fabColor);
        return 0;
    }
    public void setFabColor(String fabColor) {
        this.fabColor = fabColor;
    }

    // FAB (floating action button) image
    public String getFabImage() {
        return this.fabImage;
    }
    public int getFabImageResId() {
        if (this.fabImage != null)
            return getResId(this.fabImage);
        return 0;
    }
    public void setFabImage(String fabImage) {
        this.fabImage = fabImage;
    }

    // List items
    public ArrayList<ListItemModel> getListItems() {
        return this.listItems;
    }
    public void setListItems(ArrayList<ListItemModel> listItems) {
        this.listItems = listItems;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.background != null)
            appendString(builder, "background = " + this.background);
        if (this.fabColor != null)
            appendString(builder, "fabColor = " + this.fabColor);
        if (this.fabImage != null)
            appendString(builder, "fabImage = " + this.fabImage);
        if (this.listItems != null) {
            for (ListItemModel item : this.listItems) {
                appendString(builder, "[");
                appendString(builder, this.listItems.toString());
                appendString(builder, "]");
            }
        }
        return builder.toString();
    }

    @Override
    public void print() {
        Log.d(ListModel.class.getName(), toString());
    }
}
