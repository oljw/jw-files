package com.tecace.retail.appmanager.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by JW on 2016-04-05.
 */
public class FontTypeface {
    private static final String TAG = FontTypeface.class.getSimpleName();

    private static FontTypeface sInstance = null;
    public static FontTypeface getInstance() {
        if (sInstance == null)
            sInstance = new FontTypeface();
        return sInstance;
    }

    public void createFonts(Context context) {
        createFontBold(context);
        createFontOne(context);
    }

    public Typeface getFont(String asset) {
        if (asset.equals(SAMSUNG_SHARP_SANS_BOLD)) return mFontBold;
        if (asset.equals(SAMSUNG_ONE_400)) return mFontOne;
        return null;
    }

    public String SAMSUNG_SHARP_SANS_BOLD = "fonts/SamsungSharpSans-Bold.ttf";
    public Typeface mFontBold = null;
    public void createFontBold(Context context) {
        if (mFontBold != null) return;
        mFontBold = Typeface.createFromAsset(context.getAssets(), SAMSUNG_SHARP_SANS_BOLD);
    }
    public Typeface getFontBold() {
        return mFontBold;
    }

    public String SAMSUNG_ONE_400 = "fonts/SamsungOne-400_v1.0.ttf" ;
    public Typeface mFontOne = null;
    public void createFontOne(Context context) {
        if (mFontOne != null) return;
        mFontOne = Typeface.createFromAsset(context.getAssets(), SAMSUNG_ONE_400);
    }
    public Typeface getFontOne() {
        return mFontOne;
    }
}
