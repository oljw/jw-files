package com.samsung.retailexperience.retailecosystem.gson.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by icanmobile on 3/22/16.
 */
public class ListModel extends BaseModel implements Serializable {
    public ArrayList<ListItemModel> listItems = new ArrayList<ListItemModel>();

    public ListModel() {
        this(null);
    }
    public ListModel(ArrayList<ListItemModel> listItems) {
        this.listItems = listItems;
    }

    public ArrayList<ListItemModel> getListItems() {
        return this.listItems;
    }
    public void setListItems(ArrayList<ListItemModel> listItems) {
        this.listItems = listItems;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
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
