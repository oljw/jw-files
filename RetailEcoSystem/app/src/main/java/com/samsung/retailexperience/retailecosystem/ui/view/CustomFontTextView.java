package com.samsung.retailexperience.retailecosystem.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.samsung.retailexperience.retailecosystem.R;

/**
 * Created by icanmobile on 3/1/16.
 */
public class CustomFontTextView extends TextView {
    private static final String TAG = CustomFontTextView.class.getSimpleName();
    private Context context;

    public CustomFontTextView(Context context) {
        super(context);
        init(context, null, R.attr.customFontStyle);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.customFontStyle);
        init(context, attrs, R.attr.customFontStyle);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs,defStyle);
    }

    private void init(Context ctx, AttributeSet attrs, int defStyle) {
        this.context = ctx;
        if (!isInEditMode()) {
            TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView, defStyle, 0);
            String customFont = a.getString(R.styleable.CustomFontTextView_customFont);
            setCustomFont(customFont);
            a.recycle();
        }
    }

    public void setCustomFont(String asset) {
        if (asset != null) {
            try {
                Typeface tf = Typeface.createFromAsset(this.context.getAssets(), asset);
                setTypeface(tf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setCustomFont(Context context, String asset) {
        if (asset != null) {
            try {
                Typeface tf = Typeface.createFromAsset(context.getAssets(), asset);
                setTypeface(tf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
