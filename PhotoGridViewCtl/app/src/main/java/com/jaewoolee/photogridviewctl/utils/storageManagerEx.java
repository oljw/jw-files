package com.jaewoolee.photogridviewctl.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class storageManagerEx {
	private final static String TAG="storageManagerEx";
	private static String appDirectory = "";
	
	public static String createAppDirectory(String appDir) {
		String sdDir = getExternalStorageRoot();
		if( sdDir == Environment.MEDIA_UNMOUNTED )
			return appDirectory;
		
		appDirectory = sdDir + "/" + appDir;
		createDirectory( appDirectory );
		return appDirectory;
	}
	
	private static String getExternalStorageRoot(){
		String sdDir;
		String ext = Environment.getExternalStorageState();
		if( ext.equals(Environment.MEDIA_MOUNTED) ){
			sdDir = Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		else {
			sdDir = Environment.MEDIA_UNMOUNTED;
		}
		return sdDir;
	}
	
	private static boolean createDirectory(String dirPath) {
		File dir = new File(dirPath);
		if( !dir.exists() ) {
			if( !dir.mkdir() ) {
				Log.e(TAG, "mkdir failed !! ");
				return false;
			}
		}
		return true;
	}
	
	public static boolean isFileExist(String filePath) {
		File f = new File( filePath );
		if( !f.exists() )
			return false;
		
		return true;
	}
}

