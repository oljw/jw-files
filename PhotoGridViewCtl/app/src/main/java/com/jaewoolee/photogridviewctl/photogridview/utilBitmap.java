package com.jaewoolee.photogridviewctl.photogridview;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class utilBitmap {
private static final String CLASS_TAG = "UtilBitmap";
	
	public static final String EXT_JPEG = ".jpg";
	public static final String MIME_JPEG = "image/jpg";
	
	public static final int EDIT_TYPE_CCW = 0;
	public static final int EDIT_TYPE_CW = 1;			
	public static final int EDIT_TYPE_HFLIP = 2;
	public static final int EDIT_TYPE_VFLIP = 3;
	public static final int EDIT_TYPE_NONE_FLIP = 4;
	public static final int EDIT_TYPE_HV_FLIP = 5;
	public static final int EDIT_TYPE_COUNT = EDIT_TYPE_VFLIP + 1;
	
	public static final int BITMAP_MAX_WIDTH = 1280;
	
	public native static int saveJpeg(Bitmap bitmap, String filename);
	
	public static boolean safeRecycle(Bitmap bmp) {
		boolean isSuccess = true;
		
		try {
			if(bmp!=null && !bmp.isRecycled()){
				bmp.recycle();
			}
		} catch(Exception exception) {
			isSuccess = false;
			Log.e(CLASS_TAG, "safeRecycle(), Exception: " + exception);
		}
		
		return isSuccess;
	}
	
	public static Uri getFile(String filepath){
		Uri uri = null;
		
		File file = new File(filepath);
		if(file.exists())
			uri = Uri.fromFile(file);
		else
			Log.e(CLASS_TAG, "file not exist? - " + filepath);
		
		return uri;
	}	
	
	public static boolean deleteFile(Context context, Uri uri) {
		
		boolean ret = false;
		if (context.getContentResolver().delete(uri, null, null) > 0)
			ret = true;

		return ret;
	}
	
	public static boolean deleteFile(String filepath) {
		
		boolean ret = false;
		File file = new File(filepath);
		if(file.exists())
			ret = file.delete();

		return ret;
	}
	
	public static boolean deleteFileViaContentResolver(String filepath) {		
		// delte file via content resolver
		filepath = DatabaseUtils.sqlEscapeString(filepath);
		String where = MediaStore.Images.Media.DATA + "="  + filepath;
		boolean ret = false;
		if (photoGridView.getActivityContext().getContentResolver().delete(Images.Media.EXTERNAL_CONTENT_URI, where, null) > 0) {
			ret = true;
		}

		return ret;
	}
	
	public static void deleteAllFilesInFolder(String folderPath) {
		File folder = new File(folderPath);
		if(false == folder.exists()) {
			return;
		}

    	 String[] files = folder.list();
    	 if (files == null) {
    		 return;
    	 }
    	 int cnt = 0;
    	 for(int i=0; i<files.length; i++) {
    		 String filename = files[i];
    		 
    		 // exclude ".nomedia"
    		 if(".nomedia".equalsIgnoreCase(filename)) {
    			 continue;
    		 }
    		 
        	 File targetFile = new File(folderPath, filename);
         	 if (targetFile.exists()) {
         		targetFile.delete();
         		cnt++;
         	 }
    	 }
    	 
    	 Log.v(CLASS_TAG, "deleteAllFilesInFolder(), deleted: " + cnt);
	}
	
	public static Bitmap getBitmap(Context context, String filepath) {
		int width = 1280;
		int height = 800;
		
		// First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(filepath, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, width, height);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(filepath, options);
	}
	
	private static Bitmap getResizedBitmap (String filepath, int sampleSize){
		
		Bitmap bitmap = null;
		
		try {
			FileInputStream fis;
			fis = new FileInputStream(filepath);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = sampleSize;
			//opts.inDither = true;
			opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
			bitmap = BitmapFactory.decodeFileDescriptor(fis.getFD(), null, opts);
			fis.close();
			fis = null;
			Log.i(CLASS_TAG, "getResizedBitmap");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			bitmap = getResizedBitmap (filepath, sampleSize*2);
			Log.i(CLASS_TAG, "getResizedBitmap OutOfMemoryError  getResizedBitmap again");
		} 
		
		return bitmap;

	}
	
	public static String getFilePath(Context context, Uri contentUri) 
	{  
		String [] proj={MediaStore.Images.Media.DATA};  
		Cursor cursor = context.getContentResolver().query( contentUri,
									proj, // Which columns to return  
									null, // WHERE clause; which rows to return (all rows)  
									null, // WHERE clause selection arguments (none)  
									null); // Order-by clause (ascending by name)
	
		if (cursor != null)
		{
			if (cursor.getCount() > 0)
			{
				int column_filepath = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA); 
				cursor.moveToFirst();
				
				return cursor.getString(column_filepath);
			}
		}
		Log.i(CLASS_TAG, "can't find file from this uri - " + contentUri);
		
		return photoGridView.PHOTOGALLERY_DIR + "/" + "tmp.jpg";
		//return PConst.EXT_PHOTOGRAM_TEMP + "/" + PConst.FILE_NAME_TEMP;
	}
	
	public static String getFileName(Context context, Uri contentUri) 
	{  
		String [] proj={MediaStore.Images.ImageColumns.DISPLAY_NAME};  
		Cursor cursor = context.getContentResolver().query( contentUri,
									proj, // Which columns to return  
									null, // WHERE clause; which rows to return (all rows)  
									null, // WHERE clause selection arguments (none)  
									null); // Order-by clause (ascending by name)
	
		if (cursor != null)
		{
			if (cursor.getCount() > 0)
			{
				int column_filename = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
				
				cursor.moveToFirst();
				
				return cursor.getString(column_filename);
			}
		}
	  
		return null;
	}
	
	public static Bitmap rotateBitmap(Context context, Bitmap _bitmap, int rotate , boolean bitmapRecycle) {
		
		int angle = rotate % 360;

		Log.i(CLASS_TAG, "angle " + angle);
		
		if(angle == 0)
		{
			Log.i(CLASS_TAG, "no need to make, angle is " + angle);
			return _bitmap;
		}
		Matrix mtx = new Matrix();
		mtx.postRotate(angle);

		Bitmap rotatedBMP = null;
		// Rotating Bitmap
		try {
			if (_bitmap != null)
				rotatedBMP = Bitmap.createBitmap(_bitmap, 0, 0, _bitmap.getWidth(), _bitmap.getHeight(), mtx, true);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			//Toast.makeText(context, "Image Too Large!!!", Toast.LENGTH_SHORT).show();
			return _bitmap;
		}	
		
		if(bitmapRecycle){
			utilBitmap.safeRecycle(_bitmap);
			_bitmap = null;
			System.gc();
		}
		
		return rotatedBMP;

		
	}
	
	public static Bitmap flipBitmap(Context context, Bitmap _bitmap, int flipType) {
		Matrix matrix = new Matrix();
		Log.d(CLASS_TAG, "flipType = " + flipType);
		if(flipType == EDIT_TYPE_VFLIP){
			matrix.preScale(1.0f, -1.0f); 
		}else if(flipType == EDIT_TYPE_HFLIP){
			matrix.preScale(-1.0f, 1.0f); 
		}else if(flipType == EDIT_TYPE_HV_FLIP){
			matrix.preScale(-1.0f, -1.0f);
		}else if(flipType == EDIT_TYPE_NONE_FLIP){
			return _bitmap;
		}
		
		
		Bitmap flipBitmap = null;
		try{
			if(_bitmap != null){
				flipBitmap = Bitmap.createBitmap(_bitmap, 0, 0, _bitmap.getWidth(), _bitmap.getHeight(), matrix, false);
			}
			
		}catch(OutOfMemoryError e){
			e.printStackTrace();
			return _bitmap;
		}catch(Exception e){
			e.printStackTrace();
			return _bitmap;
		}
		
		safeRecycle(_bitmap);
		_bitmap = null;
		System.gc();
		return flipBitmap;

		
	}

	public static Bitmap applyMtrix(Context context, Bitmap _bitmap, Matrix matrix) {
		if(null == matrix) {
			return _bitmap;
		}
	
		Bitmap flipBitmap = null;
		try{
			if(_bitmap != null){
				flipBitmap = Bitmap.createBitmap(_bitmap, 0, 0, _bitmap.getWidth(), _bitmap.getHeight(), matrix, false);
			}
			
		}catch(OutOfMemoryError e){
			e.printStackTrace();
			return _bitmap;
		}catch(Exception e){
			e.printStackTrace();
			return _bitmap;
		}
		
		if(false == _bitmap.isRecycled()) { 
		//	_bitmap.recycle();
		}
		
		_bitmap = null;
		System.gc();
		return flipBitmap;		
	}
	
	public static Bitmap convertViewToBitmap(View view, int width, int height){
		
		Bitmap bitmap = null;
		bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);
		
		return bitmap;
	}
	
	public static Uri saveBitmapAsImageContent(Bitmap bitmap, String filedir, String filename) throws Exception {
		File file = new File(filedir, filename + EXT_JPEG);
		
		// avoiding duplicated image data in the system media content DB 
		deleteFileViaContentResolver(file.getAbsolutePath());
		
		// setting values
		ContentValues values = new ContentValues();
		values.put(Images.Media.TITLE, file.getName());
		values.put(Images.Media.DISPLAY_NAME, file.getName());
		values.put(Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000); //dvide 1000 to sec
		values.put(Images.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000); //dvide 1000 to sec
		values.put(Images.Media.MIME_TYPE, MIME_JPEG);
		values.put(Images.Media.DATA, file.getAbsolutePath());

		// Save
		return saveFormatJPEG(values, bitmap, file);
	}
	
	private static Uri saveFormatJPEG(ContentValues values, Bitmap bitmap, File file) throws Exception{
		ContentResolver cr = photoGridView.getActivityContext().getContentResolver();
		Uri PhotoUri = cr.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
		
		if (0 == saveJpeg (bitmap, file.getAbsolutePath())) {// JNI jpeg save API is failed
			try {
				OutputStream outStream = cr.openOutputStream(PhotoUri);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream); // poor image quality
				outStream.flush();
				outStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//save file size
		long fileSize = new File(file.getAbsolutePath()).length();
		ContentValues valuesForFileSize = new ContentValues();
		
		valuesForFileSize.put(Images.Media.SIZE, fileSize);
		cr.update(PhotoUri, valuesForFileSize, null, null);

		return PhotoUri;
	}
	
	private static Uri saveFormatPNG(ContentValues values, Bitmap bitmap, File file) throws Exception {
		ContentResolver cr = photoGridView.getActivityContext().getContentResolver();
		Uri PhotoUri = cr.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
		
		//save file with png extension
		try {
			OutputStream outStream = cr.openOutputStream(PhotoUri);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream); // poor image quality
			outStream.flush();
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//save file size
		long fileSize = new File(file.getAbsolutePath()).length();
		ContentValues valuesForFileSize = new ContentValues();
		
		valuesForFileSize.put(Images.Media.SIZE, fileSize);
		cr.update(PhotoUri, valuesForFileSize, null, null);

		return PhotoUri;
	}
	
	public static String saveBitmapAsOnlyFile(Bitmap bitmap, String filedir, String filename) {
		boolean isSuccess = false;
		
		String fName = new String(filename);
		fName += ".jpg";
		
		if (0 == saveJpeg (bitmap, filedir + "/" + fName)) {// JNI jpeg save API is failed
			File copyFile = new File(filedir, fName);
		
			OutputStream outputStream = null;
			try {       
				copyFile.createNewFile();
			    outputStream = new FileOutputStream(copyFile);
			    isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream); // poor image quality
			} 
			catch (Exception e) {
				isSuccess = false;
				e.printStackTrace();
			} finally {
				try {
					outputStream.flush();
					outputStream.close();
					outputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
			if(isSuccess) {
				return copyFile.getAbsolutePath();
			} else {
				return null;
			}
		} 
		
		return filedir + "/" + fName;
	}
	
	public static int GetExifOrientationDegree(String filename){
		int degree = 0;
		
		int orientation_value = GetExifOrientationValue(filename);
		switch(orientation_value) {
		case ExifInterface.ORIENTATION_ROTATE_270:
			degree = 270;
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			degree = 180;
			break;
		case ExifInterface.ORIENTATION_ROTATE_90:
			degree = 90;
			break;
		}
		
		return degree;
	}
	
	public static int GetExifOrientationValue(String filename){
		int orientation = 0;
		ExifInterface exif;
		String tag = "";
    	try {
			exif = new ExifInterface(filename);
			tag = exif.getAttribute(ExifInterface.TAG_ORIENTATION);  
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		if(tag == null)
		{
			Log.i(CLASS_TAG, "no exif tag");
			return orientation;
		}
		
		try{
			orientation = Integer.valueOf(tag);
		}catch(NumberFormatException e)
		{
			Log.i(CLASS_TAG, "exif tag not a number");
		}
		
		return orientation;
	}
	
    public static Bitmap GetExifOrientation(String filename, Bitmap bitmap) {
    	// checking filename
    	if(null == filename) {
    		Log.i(CLASS_TAG, "GetExifOrientation(), file name is null");
    		return bitmap;
    	}
    	
    	// checking bitmap
    	if(null == bitmap) {
    		return null;
    	}
    	
    	ExifInterface exif;
		String tag = "";
    	try {
			exif = new ExifInterface(filename);
			tag = exif.getAttribute(ExifInterface.TAG_ORIENTATION);  
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		if(tag == null)
		{
			Log.i(CLASS_TAG, "no exif tag");
			return bitmap;
		}
		
		int orientation = 0;
		try{
			orientation = Integer.valueOf(tag);
		}catch(NumberFormatException e)
		{
			Log.i(CLASS_TAG, "exif tag not a number");
			return bitmap;
		}

		//Log.i(CLASS_TAG, "exif orientation = " + orientation);
		
		Matrix mtx = new Matrix();

		switch(orientation)
		{
		case ExifInterface.ORIENTATION_ROTATE_270:
			mtx.postRotate(270);
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			mtx.postRotate(180);
			break;
		case ExifInterface.ORIENTATION_ROTATE_90:
			mtx.postRotate(90);
			break;
		case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
			mtx.postScale(-1, 1);
			mtx.postTranslate(bitmap.getWidth(), 0);
			break;
		case ExifInterface.ORIENTATION_TRANSVERSE:
			mtx.postRotate(90);
			mtx.postScale(-1, 1);
			mtx.postTranslate(bitmap.getWidth(), 0); 			
			break;
		case ExifInterface.ORIENTATION_FLIP_VERTICAL:
			mtx.postRotate(180);
			mtx.postScale(-1, 1);
			mtx.postTranslate(bitmap.getWidth(), 0);
			break;
		case ExifInterface.ORIENTATION_TRANSPOSE:
			mtx.postRotate(270);
			mtx.postScale(-1, 1);
			mtx.postTranslate(bitmap.getWidth(), 0);
			break;
		default:
			//Log.i(CLASS_TAG, "exif tag ORIENTATION_UNDEFINED(0) or ORIENTATION_NORMAL(1), " + orientation);
			return bitmap;
		}

		Bitmap rotatedBMP = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);
		if( utilBitmap.safeRecycle(bitmap) ) {
			bitmap = null;
		}
		
		return rotatedBMP;
    }
    
	/**
     * Bitmap�� ratio�� ���缭 max�� ��ŭ resize�Ѵ�.
     * @param Bitmap �� 
     * @param max ���ϴ� ũ���� ��
     * reference: http://www.sjava.net/304
     */
    public static Bitmap resizeBitmap(Bitmap src, int max) {
        if(src == null)
            return null;
        
        int width = src.getWidth();
        int height = src.getHeight();
        float rate = 0.0f;
        
        if (width > height) {
            rate = max / (float) width;
            height = (int) (height * rate);
            width = max;
        } else {
            rate = max / (float) height;
            width = (int) (width * rate);
            height = max;
        }

        return Bitmap.createScaledBitmap(src, width, height, true);
    }
    
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float)height / (float)reqHeight);
			} else {
				inSampleSize = Math.round((float)width / (float)reqWidth);
			}
		}
		return inSampleSize;
	}
	
	public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(pathName, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(pathName, options);
	}

	public static Uri  insertFileViaContentResolver(String targetFilePath, photoInfo photoInfo) throws Exception{
		File targetFile = new File(targetFilePath);
		String fileDisplayName = targetFile.getName();
		String fileTitle = com.jaewoolee.photogridviewctl.photogridview.utilCommon.safeSubString(fileDisplayName, 0, fileDisplayName.lastIndexOf("."));
		
	    //insert to contentsResolver
	    ContentValues values = new ContentValues();		    
	    values.put(Images.Media.TITLE, fileTitle);
		values.put(Images.Media.DISPLAY_NAME, fileDisplayName);
		values.put(Images.Media.DATE_ADDED, photoInfo.mFileAdded_original); //dvide 1000 to sec
		values.put(Images.Media.DATE_MODIFIED, photoInfo.mFileModified); //dvide 1000 to sec
		values.put(Images.Media.SIZE, photoInfo.mFileSize);		
		values.put(Images.Media.MIME_TYPE, photoInfo.mMimeType);
		values.put(Images.Media.DESCRIPTION, photoInfo.mDescription);
		values.put(Images.Media.PICASA_ID, photoInfo.mPicasa_id);
		values.put(Images.Media.LATITUDE, photoInfo.mLatitude);
		values.put(Images.Media.LONGITUDE, photoInfo.mLongitude);
		values.put(Images.Media.DATE_TAKEN, photoInfo.mDateTaken);
		values.put(Images.Media.ORIENTATION, photoInfo.mOrientation);
		values.put(Images.Media.DATA, targetFilePath);
		
		// 2013.04.16 jychoi The isPrivate means hiding in the slideshow.
		// values.put(Images.Media.IS_PRIVATE, photoInfo.mIsPrivate);
	
		ContentResolver cr = photoGridView.getActivityContext().getContentResolver();
		Uri PhotoUri = cr.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
		
//		Log.d(CLASS_TAG, "insert photo info = "  + photoInfo.mTitle + photoInfo.mFileName+photoInfo.mFileAdded_original+photoInfo.mFileSize + photoInfo.mMimeType +
//				photoInfo.mDescription + photoInfo.mPicasa_id+photoInfo.mIsPrivate+photoInfo.mLatitude+photoInfo.mLongitude +photoInfo.mDateTaken+photoInfo.mOrientation);
		
		return PhotoUri;
	}
	
	public static String resizeAndSaveTempFile(Context context, String filePath) {
		// MAX_SIZE
		int MAX_SIZE = 1280;
		/*
		if(UtilCommon.isGB()) {
			MAX_SIZE = 800;
		}
		*/
		
		// getting bitmap
		Bitmap tempBitmap = utilBitmap.getBitmap(context, filePath);
		if (null == tempBitmap) {
			return null;
		}

		// orientation
		tempBitmap = utilBitmap.GetExifOrientation(filePath, tempBitmap);
		
		// resize
		Bitmap scaledBitmap = null;
		if (tempBitmap.getWidth() > MAX_SIZE || tempBitmap.getHeight() > MAX_SIZE) {
			scaledBitmap = utilBitmap.resizeBitmap(tempBitmap, MAX_SIZE);
		} else {
			scaledBitmap = tempBitmap;
		}
		
		// save bitmap
		String sacledFilePath = null;
		File tempFile = new File(filePath);
		String fileName = com.jaewoolee.photogridviewctl.photogridview.utilCommon.stripExtension(tempFile.getName());
		sacledFilePath = utilBitmap.saveBitmapAsOnlyFile(scaledBitmap, photoGridView.PHOTOGALLERY_DIR + "/tmp", fileName);

		// release
		utilBitmap.safeRecycle(tempBitmap);
		tempBitmap = null;
		utilBitmap.safeRecycle(scaledBitmap);
		scaledBitmap = null;
		System.gc();
		
		// return
		return sacledFilePath;
	}
}
