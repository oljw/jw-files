package com.samsung.retailexperience.retailhero.gson.models;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by icanmobile on 1/12/16.
 */
public class FragmentModel<T> extends ResourceModel implements Serializable {
    private String layout;
    private String actionBackKey;
    private T fragment;
    private String background;
    private String drawerId;
    private String pivotX;
    private String pivotY;

    public FragmentModel() {
        this(null);
    }
    public FragmentModel( String layout) {
        this(layout, null);
    }
    public FragmentModel( String layout,
                          String actionBackKey) {
        this(layout, actionBackKey, null);
    }
    public FragmentModel( String layout,
                          String actionBackKey,
                          T fragment) {
        this(layout, actionBackKey, fragment, null);
    }
    public FragmentModel( String layout,
                          String actionBackKey,
                          T fragment,
                          String background) {
        this(layout, actionBackKey, fragment, background, null);
    }
    public FragmentModel( String layout,
                          String actionBackKey,
                          T fragment,
                          String background,
                          String drawerId) {
        this(layout, actionBackKey, fragment, background, drawerId, null, null);
    }
    public FragmentModel( String layout,
                          String actionBackKey,
                          T fragment,
                          String background,
                          String drawerId,
                          String pivotX,
                          String pivotY) {
        this.layout         = layout;
        this.actionBackKey  = actionBackKey;
        this.fragment       = fragment;
        this.background     = background;
        this.drawerId       = drawerId;
        this.pivotX         = pivotX;
        this.pivotY         = pivotY;
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



    public String getActionBackKey() {
        return this.actionBackKey;
    }
    public void setActionBackKey(String actionBackKey) {
        this.actionBackKey = actionBackKey;
    }



    public T getFragment() {
        return this.fragment;
    }
    public void setFragment(T fragment) {
        this.fragment = fragment;
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



    public String getDrawerId() {
        return this.drawerId;
    }
    public void setDrawerId(String drawerId) {
        this.drawerId = drawerId;
    }



    public String getPivotX() {
        return this.pivotX;
    }
    public void setPivotX(String pivotX) {
        this.pivotX = pivotX;
    }
    public int getPivotXValue() {
        return Integer.parseInt(this.pivotX);
    }
    public void setPivotXValue(int pivotX) {
        this.pivotX = String.valueOf(pivotX);
    }
    public String getPivotY() {
        return this.pivotY;
    }
    public void setPivotY(String pivotY) {
        this.pivotY = pivotY;
    }
    public int getPivotYValue() {
        return Integer.parseInt(this.pivotY);
    }
    public void setPivotYValue(int pivotY) {
        this.pivotY = String.valueOf(pivotY);
    }
    public void setPivot(int x, int y) {
        this.pivotX = String.valueOf(x);
        this.pivotY = String.valueOf(y);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.layout != null)
            appendString(builder, "layout = " + this.layout);
        if (this.actionBackKey != null)
            appendString(builder, "actionBackKey = " + this.actionBackKey);
        if (this.fragment != null) {
            appendString(builder, "fragment [");
            appendString(builder, this.fragment.toString());
            appendString(builder, "]");
        }
        if (this.background != null)
            appendString(builder, "background = " + this.background);
        if (this.drawerId != null)
            appendString(builder, "drawerId = " + this.drawerId);
        if (this.pivotX != null)
            appendString(builder, "pivotX = " + this.pivotX);
        if (this.pivotY != null)
            appendString(builder, "pivotY = " + this.pivotY);

        return builder.toString();
    }

    private void appendString(StringBuilder builder, String value) {
        builder.append(value + System.getProperty("line.separator"));
    }

    public void print() {
        Log.d(FragmentModel.class.getName(), toString());
    }
}
