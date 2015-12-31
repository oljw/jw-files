package com.jaewoolee.photogridviewctl.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class displayInfoEx {
	private final static String TAG="mgDisplayInfo";
	
	public static int _display_width;
	public static int _display_height;
	
	public static displayInfoEx getDisplaySize(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		
		displayInfoEx displayInfo = new displayInfoEx();
		displayInfo._display_width = metrics.widthPixels;
		displayInfo._display_height = metrics.heightPixels;
		return displayInfo; 
	}
	
	public static displayInfoEx getDisplaySize() {
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		
		displayInfoEx displayInfo = new displayInfoEx();
		displayInfo._display_width = metrics.widthPixels;
		displayInfo._display_height = metrics.heightPixels;
		return displayInfo;
	}
}
