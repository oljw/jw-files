package com.samsung.retailexperience.retailhero.util;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by smheo on 9/29/2015.
 */
public class ScreenInfo {

    private int WidthPixels = 0;
    private int HeightPixels = 0;
    private int Dpi = 0;
    private int RotationInfo = 0;

    public ScreenInfo(Context context) {
        init(context);
    }

    public void init(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);

                //((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        this.WidthPixels = metrics.widthPixels;
        this.HeightPixels = metrics.heightPixels;
        this.Dpi = metrics.densityDpi;
        this.RotationInfo = display.getRotation();
    }

    public int getScreenWidth() {
        return this.WidthPixels;
    }

    public int getScreenHeight() {
        return this.HeightPixels;
    }

    public int getScreenDpi() {
        return this.Dpi;
    }

    public int getRotationInfo() {
        return this.RotationInfo;
    }

    public static int getScreenWidth(Context context) {
        Point wsize = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getSize(wsize);
        return wsize.x;
        /*
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
         */
    }

    public static int getScreenHeight(Context context) {
        Point wsize = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getSize(wsize);
        return wsize.y;
        /*
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
         */
    }

    public static int getScreenRotation(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getRotation();
    }


    // Pixel --> DP
    public static int getPxToDp(Context context, int pixel) {
        float dp;

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        dp = pixel / (metrics.densityDpi / 160f);
        return (int) dp;
    }

    // DP --> Pixel
    public static int getDpToPx(Context context, float dp) {
        float px;

        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return (int) px;
    }
}
