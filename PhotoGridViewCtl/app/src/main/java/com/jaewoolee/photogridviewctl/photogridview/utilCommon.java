package com.jaewoolee.photogridviewctl.photogridview;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager.BadTokenException;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class utilCommon {
	public final static String CLASS_TAG = "UtilCommon";

	public final static String[] GALLERY_PROJECTION = new String[] { Images.Media.DATA, Images.Media._ID,
																	 Images.Media.DISPLAY_NAME, Images.Media.DATE_ADDED, Images.Media.DATE_MODIFIED };
	
	public static boolean isGB() {
		return Build.VERSION.SDK_INT <= 10 ? true : false;
	}
	
	public static String getFileDateInfo(Context context, String filePath){
		int flags = 0;
		flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
		flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
		flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;
		flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
    	   
    	String madenDateTime = android.text.format.DateUtils.formatDateTime(context, new Date().getTime(), flags);
    	
    	try {
			ExifInterface exit = new ExifInterface(filePath);
			madenDateTime = exit.getAttribute(ExifInterface.TAG_DATETIME);
			Log.d(CLASS_TAG, "filePath = "+filePath+" madenDate = " +madenDateTime);
			if(madenDateTime == null){
				madenDateTime = android.text.format.DateUtils.formatDateTime(context, new Date().getTime(), flags);
			}else{
				SimpleDateFormat dateParser = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss"); 
				Date d = null;
				try {
					d = dateParser.parse(madenDateTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 	
				if(d != null){

					madenDateTime = android.text.format.DateUtils.formatDateTime(context, d.getTime(), flags);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return android.text.format.DateUtils.formatDateTime(context, new Date().getTime(), flags);
		}
	
		return madenDateTime;
	}
	
	public static String getTimeStamp(Context context, long milisec){
		String timeStamp = "";
		
		try{			
			SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", context.getResources().getConfiguration().locale);			
			timeStamp = dateParser.format(milisec);
		}catch(Exception e){
			return String.valueOf(milisec);
		}
//		Log.d(CLASS_TAG, "dateFormat = " + timeStamp);
		return timeStamp;
	}
	
	public static boolean isExistFile(String path){
        if(TextUtils.isEmpty(path)) return false;
        boolean isFile = false;            
        File f = new File(path);
        
        if(f != null){
           isFile = f.exists();
        }
        return isFile;
    }
	
	public static boolean fileCheckAndDelete(String filepath, boolean delFile) {
        File file = new File(filepath);
        if(!isExistFile(filepath)) {
            return false;
        }

        if(delFile) {
            file.delete();
        }
        
        return true;        
    }
	
	public static String capitalize(String s){
		return !s.equals("")&& s != null ? s.substring(0, 1).toUpperCase(Locale.getDefault()) +s.substring(1).toLowerCase(Locale.getDefault()):s;
	}
	
	public static String getFileName(Uri _uri) {
		
		String ret = null;
		if (_uri != null) {
			
			if (_uri.toString().startsWith("file://")) {
				//String[] tmp = _uri.toString().split("/");
				// ret = tmp[tmp.length-1];
				Date date = new Date(System.currentTimeMillis());
				
				ret = DateFormat.getDateInstance().format(date).replace(" ", "");
				ret = ret.replace(".", "");
				ret = "Photo_" + ret;
				ret = ret + date.getHours()+ date.getMinutes()+ date.getSeconds();
				
			} else {
				
				ret = utilBitmap.getFileName(photoGridView.getActivityContext(), _uri);
			}

			if (ret != null) {
				if (ret.endsWith(".jpg") 
						|| ret.endsWith(".jpg") 
						|| ret.endsWith(".bmp") 
						|| ret.endsWith(".gif")) {
					ret = utilCommon.safeSubString(ret, 0, ret.length()-4);
				} else if (ret.endsWith(".jpeg")) {
					ret = utilCommon.safeSubString(ret, 0, ret.length()-5);
				}
			}
		}
		
		Log.i(CLASS_TAG, "ret = " + ret);
		
		return ret;
	}
	
	public static int minusIntegerToZero(int value){
		return value>0?value:0;
	}
	
    public static String stripExtension(String str) {
        if(str == null) {
        	return null; 
        }
        
        int pos = str.lastIndexOf(".");
        
        return utilCommon.safeSubString(str, 0, pos);
    }
    
	public static void bitmapArrayRelease(Bitmap[] bitmap){
		if(bitmap == null){
			return;
		}
		
		for(int i = 0; i<bitmap.length; i++){
			utilBitmap.safeRecycle(bitmap[i]);
			bitmap[i]=null;
		}
		
		bitmap = null;
	}
	
	public static void bitmapArrayReleaseDim2(Bitmap[][] bitmap){
		if(bitmap == null){
			return;
		}
		
		for(int i = 0; i<bitmap.length; i++){
			if(null==bitmap[i]) {
				continue;
			}
			
			for(int j = 0; j<bitmap[i].length; j++){
				utilBitmap.safeRecycle(bitmap[i][j]);
				bitmap[i][j]=null;
			}
		}
		
		bitmap = null;
	}
	
	public static String removeMp3Extension(String name){
		 if(!TextUtils.isEmpty(name) && name.endsWith(".mp3")){
			 //remove .mp3
			 return name = utilCommon.safeSubString(name, 0, name.length() - ".mp3".length());
		 }
		 
		 return name;
		
	}
	
	public static boolean isLoadedLibrary(String name){
		boolean isLoaded = false;
    	try {
            Set<String> libs = new HashSet<String>();
            String mapsFile = "/proc/" + android.os.Process.myPid() + "/maps";
            BufferedReader reader = new BufferedReader(new FileReader(mapsFile));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith(".so")) {
                    int n = line.lastIndexOf(" ");
                    libs.add(line.substring(n + 1));
                }
            }
            reader.close();
            
            for (String lib : libs) {
               if(lib.contains(name)){
            	   isLoaded = true;
               }
            }
        } catch (FileNotFoundException e) {
            // Do some error handling...
        	return isLoaded;
        } catch (IOException e) {
            // Do some error handling...
        	return isLoaded;
        } catch (Exception e){
        	return isLoaded;
        }
    	return isLoaded;
	}
	
	public static void safeDialogShow(Activity activity, Dialog dialog) {
        // parameter
        if(null==activity || null==dialog) {
            Log.d(CLASS_TAG, "safeDialogShow(), activity or dialog is null");
            return;
        }
        // checking - available activity
        if(activity.isFinishing()) {
            Log.d(CLASS_TAG, "safeDialogShow(), activity is isFinishing() true");
            return;
        }
        // show
        try {
            // TRY TO SHOW
            dialog.show();
        } catch (BadTokenException bte) {
            // 20120125, jychoi, Sometimes it is occurred because invalid window.
            Log.e(CLASS_TAG, "safeDialogShow(), BadTokenException: " + bte.getMessage(), bte);
        } catch (Exception e) {
            Log.e(CLASS_TAG, "safeDialogShow(), Exception: " + e.getMessage(), e);
        }
    }
	
	public static void safeeDialogDismiss(Activity activity, Dialog dialog) {
		// parameter
        if(null==activity || null==dialog) {
            Log.d(CLASS_TAG, "safeeDialogDismiss(), activity or dialog is null");
            return;
        }
        // checking - available activity
        if(activity.isFinishing()) {
            Log.d(CLASS_TAG, "safeeDialogDismiss(), activity is isFinishing() true");
            return;
        }
    
        // checking is showing
        if(false == dialog.isShowing()) {
        	return;
        }
        // dismiss
        try {
            dialog.dismiss();
        } catch (Exception e) {
            Log.e(CLASS_TAG, "safeeDialogDismiss(), Exception: " + e.getMessage(), e);
        }
    }
	
	  public static String getSaveFileName(String path, String filename) {
		  	// getting only name without extension
		  	if(null == filename) {
		  		return null;
		  	}
			
			// checking exist
			File targetFile = new File(path, filename + utilBitmap.EXT_JPEG);
			if(false == targetFile.exists()) {
				return filename;
			}
			
			try {
				// find '(' and ')'
				int posLeft = filename.lastIndexOf('(');
				int posRigth = filename.lastIndexOf(')');
				
				// CASE OF do not have "(?)"
				if(posLeft==-1 || posRigth==-1) {
					// loop for create name
					for(int i=1; i<Long.MAX_VALUE; i++) {
						String potfix = "(" + i + ")";
						File tempFile = new File(path, filename + potfix + utilBitmap.EXT_JPEG);
						if(tempFile.exists()) {
							continue;
						}
						filename = filename + potfix;
						return filename;
					}
					
					filename = filename + "_";
					return filename;
				} 
				
				// CASE OF has "(?)"
				else {	
					// getting value
					String value = utilCommon.safeSubString(filename, posLeft+1, posRigth);
					long numericVal = Long.valueOf(value);
					// return
					String front = utilCommon.safeSubString(filename, 0, posLeft+1);
					String near = utilCommon.safeSubString(filename, posRigth, filename.length());
					
					// loop for create name
					for(int i=1; i<Long.MAX_VALUE; i++) {
						String tempFileName = front + (numericVal+i) + near;
						File tempFile = new File(path, tempFileName + utilBitmap.EXT_JPEG);
						if(tempFile.exists()) {
							continue;
						}
						filename = tempFileName;
						return filename;
					}
					
					filename = filename + "_";
					return filename;
				}
			} catch (Exception e) {
				for(int i=1; i<Long.MAX_VALUE; i++) {
					String potfix = "(" + i + ")";
					File tempFile = new File(path, filename + potfix);
					if(tempFile.exists()) {
						continue;
					}
					filename = filename + potfix;
					return filename;
				}
			}
			
			return filename + "_";
		}
	  
	  public static String getMimeType(String path) {
		  String extention =  path.substring(path.lastIndexOf(".") );
		  String mimeTypeMap =MimeTypeMap.getFileExtensionFromUrl(extention);
		  String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimeTypeMap);
		    return mimeType;
		}
	  
	  public static int convertDpToPixels(Context context, float input) {
          // Get the screen's density scale
          final float scale = context.getResources().getDisplayMetrics().density;
          // Convert the dps to pixels, based on density scale
          return (int) (input * scale + 0.5f);
	 }
	  
	  public static void safeReleaseCursor(Cursor cursor){
		  if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}		
			cursor = null;		  
	  }
	  
	public static PackageInfo getPackageInfo(Context context){
		PackageManager packageManager = context.getPackageManager();
		if (null == packageManager) {
			Log.d(CLASS_TAG, "printVersionInfo(), PackageManager is null");
			return null;
		}

		PackageInfo packageInfo = null;
		try {
			packageInfo = packageManager.getPackageInfo(photoGridView.PACKAGE_NAME,
					PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			Log.d(CLASS_TAG, "getPackageInfo(), Package name not found. So, UI will try to install engine service.");
		} catch (Exception e) {
			Log.d(CLASS_TAG, "getPackageInfo(), Package name not found with unknown exception. So, UI will try to install engine service.");
			e.printStackTrace();
		}

		if (null == packageInfo) {
			Log.d(CLASS_TAG, "getPackageInfo(), packageInfo is null");
			return null;
		}
		
		return  packageInfo;
	}
	
	public static void saveCapturedImage(Context context, String filePath) throws Exception{
		// checking param
		if(null==context || TextUtils.isEmpty(filePath)) {
			return;
		}
		
		ContentValues values = new ContentValues();
		
		// file info
		File file = new File(filePath);
		String fName = file.getName();
		values.put(Images.Media.TITLE, fName);
		values.put(Images.Media.DISPLAY_NAME, fName);
		values.put(Images.Media.DATA, filePath);
		values.put(Images.Media.MIME_TYPE, "image/jpg");
		// file size
		long fileSize = file.length();
		values.put(Images.Media.SIZE, fileSize);
		// orientation
		int orientationDegree = utilBitmap.GetExifOrientationDegree(filePath);
		values.put(Images.Media.ORIENTATION, orientationDegree);
		// date
		values.put(Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000); // dvide 1000 to sec
		values.put(Images.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000); // dvide 1000 to sec
		
		// insert media
		ContentResolver cr = context.getContentResolver();
		Uri PhotoUri = cr.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
		Log.v(CLASS_TAG, "captureImage = " + PhotoUri);
	}
	
	//file move or copy method
	public static void fileMoveOrCopy(String inFileName, String outFileName, int action) throws Exception {
	    //file move using NIO
		FileInputStream inputStream = new FileInputStream(inFileName);
	    FileOutputStream outputStream = new FileOutputStream(outFileName);
		
	    FileChannel fcin = inputStream.getChannel();
	    FileChannel fcout = outputStream.getChannel();
	    
	    long size = fcin.size();
	    
	    fcin.transferTo(0, size, fcout);
	    //close stream
	    fcout.close();
	    fcin.close();
	    outputStream.close();
	    inputStream.close();

	    //delete file after copy
	    utilCommon.fileCheckAndDelete(inFileName, true);
	 }
	
	public static int getMediaStoreID(String filePath) throws Exception{
		int id = -1;
		Cursor cursor = null;
		if(!utilCommon.isExistFile(filePath)){
			return id;
		}
		
		filePath = DatabaseUtils.sqlEscapeString(filePath);
		cursor = photoGridView.getActivityContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, utilCommon.GALLERY_PROJECTION, "_data = " + filePath , null,null);
		
		if(cursor != null){
			if (cursor.moveToFirst()) {
				// id
				id = cursor.getInt(cursor.getColumnIndexOrThrow(Images.Media._ID));
				
				utilCommon.safeReleaseCursor(cursor);
			}
		}
		return id;
	}
	
	public static boolean isDirectoryEmpty(String filePath){
		boolean isEmpty = true;
		if(filePath == null){
			return false;
		}
		
		File directory = new File(filePath);
		
		if (directory.isDirectory()) {
		    String[] files = directory.list();
		    if (files != null && files.length > 0) {
		       isEmpty = false;
		    }
		}	
		
		return isEmpty;
	}
	
	public static String getFolderPathWithoutSlash(String path) {
		if(null == path) {
			return null;
		}
		
		char lastChar = path.charAt(path.length() - 1);
		if('/' != lastChar) {
			return path;
		}
		
		path = utilCommon.safeSubString(path, 0, path.length() - 1);
		return path;
	}
	
	public static String safeSubString(String text, int start, int end){
		if(TextUtils.isEmpty(text)){
			return text;
		}
		
		if(start < 0 || start > end || end > text.length()){
			return text;
		}
		
		if(end != -1){
			try{
				text = text.substring(start, end);
			}catch(StringIndexOutOfBoundsException e){
				e.printStackTrace();
			}
		}
		
		return text;
	}
}
