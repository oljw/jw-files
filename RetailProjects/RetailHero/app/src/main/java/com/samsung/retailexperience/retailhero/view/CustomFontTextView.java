package com.samsung.retailexperience.retailhero.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.samsung.retailexperience.retailhero.R;

/**
 * Created by smheo on 9/29/2015.
 */
public class CustomFontTextView extends TextView {

    //private static final String TAG = CustomFontTextView.class.getCanonicalName();

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
        if (!isInEditMode()) {
            TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView, defStyle, 0);
            String customFont = a.getString(R.styleable.CustomFontTextView_customFont);
            setCustomFont(ctx, customFont);
            a.recycle();
        }
    }

    public void setCustomFont(Context ctx, String asset) {
        if (asset != null) {
            try {
                Typeface tf = Typeface.createFromAsset(ctx.getAssets(), asset);
                setTypeface(tf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
