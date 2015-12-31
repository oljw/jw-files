package com.jaewoolee.photogridviewctl.utils;

import android.database.Cursor;

public class utilsEx {

	public static void safeReleaseCursor( Cursor cursor ) {
		if( cursor != null && !cursor.isClosed() ) {
			cursor.close();
		}
		cursor = null;
	}
}

