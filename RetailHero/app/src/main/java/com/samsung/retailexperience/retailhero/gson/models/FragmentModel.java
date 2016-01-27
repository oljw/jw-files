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

    public FragmentModel() {
        this(null, null, null, null, null);
    }
    public FragmentModel( String layout) {
        this(layout, null, null, null, null);
    }
    public FragmentModel( String layout,
                          String actionBackKey) {
        this(layout, actionBackKey, null, null, null);
    }
    public FragmentModel( String layout,
                          String actionBackKey,
                          T fragment) {
        this(layout, actionBackKey, fragment, null, null);
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
        this.layout         = layout;
        this.actionBackKey  = actionBackKey;
        this.fragment       = fragment;
        this.background     = background;
        this.drawerId       = drawerId;
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

        return builder.toString();
    }

    private void appendString(StringBuilder builder, String value) {
        builder.append(value + System.getProperty("line.separator"));
    }

    public void print() {
        Log.d(FragmentModel.class.getName(), toString());
    }
}
