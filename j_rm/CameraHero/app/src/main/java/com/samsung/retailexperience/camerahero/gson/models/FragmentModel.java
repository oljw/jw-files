package com.samsung.retailexperience.camerahero.gson.models;

import android.util.Log;

/**
 * Created by icanmobile on 1/12/16.
 */
public class FragmentModel<T> extends ResourceModel {
    private String layout;
    private String drawerId;
    private String background;
    private String actionBackKey;
    private String pivotX;
    private String pivotY;
    private T fragment;

    public FragmentModel() {
        this.layout         = null;
        this.drawerId       = null;
        this.background     = null;
        this.actionBackKey  = null;
        this.pivotX         = null;
        this.pivotY         = null;
        this.fragment       = null;
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


    public String getDrawerId() {
        return this.drawerId;
    }
    public int getDrawerResId() {
        if (this.drawerId != null)
            return getResId(this.drawerId);
        return 0;
    }
    public void setDrawerId(String drawerId) {
        this.drawerId = drawerId;
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


    public String getActionBackKey() {
        return this.actionBackKey;
    }
    public void setActionBackKey(String actionBackKey) {
        this.actionBackKey = actionBackKey;
    }


    public String getPivotX() {
        return this.pivotX;
    }
    public int getPivotXResId() {
        if (this.pivotX != null)
            return getResId(this.pivotX);
        return 0;
    }
    public void setPivotX(String pivotX) {
        this.pivotX = pivotX;
    }


    public String getPivotY() {
        return this.pivotY;
    }
    public int getPivotYResId() {
        if (this.pivotY != null)
            return getResId(this.pivotY);
        return 0;
    }
    public void setPivotY(String pivotY) {
        this.pivotY = pivotY;
    }


    public T getFragment() {
        return this.fragment;
    }
    public void setFragment(T fragment) {
        this.fragment = fragment;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.background != null)
            appendString(builder, "background = " + this.background);
        if (this.fragment != null) {
            appendString(builder, "fragment [");
            appendString(builder, this.fragment.toString());
            appendString(builder, "]");
        }
        return builder.toString();
    }

    private void appendString(StringBuilder builder, String value) {
        builder.append(value + System.getProperty("line.separator"));
    }

    public void print() {
        Log.d(FragmentModel.class.getName(), toString());
    }
}
