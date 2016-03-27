package com.samsung.retailexperience.retailecosystem.gson.models;

import android.util.Log;

/**
 * Created by icanmobile on 3/22/16.
 */
public class ListItemModel extends BaseModel {
    public String layout;
    public String layoutHeight;
    public String layoutColor;
    public String image;
    public String text;
    public String textFont;
    public String textSize;
    public String textColor;

    public ListItemModel() {
        this(null, null, null, null, null, null, null, null);
    }
    public ListItemModel(String layout,
                         String layoutHeight,
                         String layoutColor,
                         String image,
                         String text,
                         String textFont,
                         String textSize,
                         String textColor) {
        this.layout             = layout;
        this.layoutHeight       = layoutHeight;
        this.layoutColor        = layoutColor;
        this.image              = image;
        this.text              = text;
        this.textFont          = textFont;
        this.textSize          = textSize;
        this.textColor         = textColor;
    }

    // layout
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

    // layout height
    public String getLayoutHeight() {
        return this.layoutHeight;
    }
    public int getLayoutHeightResId() {
        if (this.layoutHeight != null)
            return getResId(this.layoutHeight);
        return 0;
    }
    public void setLayoutHeight(String layoutHeight) {
        this.layoutHeight = layoutHeight;
    }

    // layout color
    public String getLayoutColor() {
        return this.layoutColor;
    }
    public int getLayoutColorResId() {
        if (this.layoutColor != null)
            return getResId(this.layoutColor);
        return 0;
    }
    public void setLayoutColor(String layoutColor) {
        this.layoutColor = layoutColor;
    }


    // image
    public String getImage() {
        return this.image;
    }
    public int getImageResId() {
        if (this.image != null)
            return getResId(this.image);
        return 0;
    }
    public void setImage(String image) {
        this.image = image;
    }



    // Text
    public String getText() {
        return this.text;
    }
    public int getTextResId() {
        if (this.text != null)
            return getResId(this.text);
        return 0;
    }
    public void setText(String text) {
        this.text = text;
    }

    //  Text Font
    public String getTextFont() {
        return this.textFont;
    }
    public void setTextFont(String textFont) {
        this.textFont = textFont;
    }

    // Text Size
    public String getTextSize() {
        return this.textSize;
    }
    public int getTextSizeResId() {
        if (this.textSize != null)
            return getResId(this.textSize);
        return 0;
    }
    public void setTextSize(String textSize) {
        this.textSize = textSize;
    }

    //  Text Color
    public String getTextColor() {
        return this.textColor;
    }
    public int getTextColorResId() {
        if (this.textColor != null)
            return getResId(this.textColor);
        return 0;
    }
    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }



    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (this.layout != null)
            appendString(builder, "layout = " + this.layout);
        if (this.layoutHeight != null)
            appendString(builder, "layoutHeight = " + this.layoutHeight);
        if (this.layoutColor != null)
            appendString(builder, "layoutColor = " + this.layoutColor);
        if (this.image != null)
            appendString(builder, "image = " + this.image);
        if (this.text != null)
            appendString(builder, "text = " + this.text);
        if (this.textSize != null)
            appendString(builder, "textSize = " + this.textSize);
        if (this.textColor != null)
            appendString(builder, "textColor = " + this.textColor);
        if (this.textFont != null)
            appendString(builder, "textFont = " + this.textFont);

        return builder.toString();
    }

    @Override
    public void print() {
        Log.d(ListItemModel.class.getName(), toString());
    }
}
