package com.jaewoolee.photogridviewctl.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;

import java.io.Serializable;
import java.util.Date;

public class photoInfoEx implements Serializable {
	private final static String TAG="mgPhotoInfo";
	
	public long mId;
	public String mFilePath;
	public String mTitle;
	public String mFileName;
	public String mFileAdded;
	public long mFileAdded_original;
	public String mFileModified;
	public long mFileSize;
	public String mMimeType;
	public String mDescription;
	public String mPicasa_id;
	public int mIsPrivate;
	public double mLatitude;
	public double mLongitude;
	public long mDateTaken;
	public int mOrientation;
	public String mThumbnailPath;
	
	public static photoInfoEx getPhotoInfoFromUri(Context context, Uri contentUri) {

		Cursor cursor = null;
		if (contentUri.getScheme().compareTo("file") == 0) {
			String filePath = contentUri.getPath();
			filePath = DatabaseUtils.sqlEscapeString(filePath);
			cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data = " + filePath, null,null);
		} else {
			cursor = context.getContentResolver().query(contentUri, null, null, null, null);
		}
		
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				photoInfoEx info = new photoInfoEx();
				
				info.mId = cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media._ID));
				info.mFilePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
				info.mTitle = cursor.getString(cursor.getColumnIndexOrThrow(Images.Media.TITLE));
				info.mFileName = cursor.getString(cursor.getColumnIndexOrThrow(Images.Media.DISPLAY_NAME));
				
				long added = cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media.DATE_ADDED));
				Date addedDate = new Date(added*1000);
				info.mFileAdded = addedDate.toLocaleString();
				info.mFileAdded_original = added;

				long modified = cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media.DATE_MODIFIED));
				Date modifiedDate = new Date(modified*1000);
				info.mFileModified = modifiedDate.toLocaleString();
				
				info.mFileSize = cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media.SIZE));
				info.mMimeType = cursor.getString(cursor.getColumnIndexOrThrow(Images.Media.MIME_TYPE));
				info.mDescription = cursor.getString(cursor.getColumnIndexOrThrow(Images.Media.DESCRIPTION)); 
				info.mPicasa_id = cursor.getString(cursor.getColumnIndexOrThrow(Images.Media.PICASA_ID)); 
				info.mIsPrivate = cursor.getInt(cursor.getColumnIndexOrThrow(Images.Media.IS_PRIVATE));
				info.mLatitude = cursor.getDouble(cursor.getColumnIndexOrThrow(Images.Media.LATITUDE));
				info.mLongitude = cursor.getDouble(cursor.getColumnIndexOrThrow(Images.Media.LONGITUDE));
				info.mDateTaken = cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media.DATE_TAKEN));
				info.mOrientation = cursor.getInt(cursor.getColumnIndexOrThrow(Images.Media.ORIENTATION));
				
				String[] thumb_proj = { MediaStore.Images.Thumbnails.DATA };
				Cursor thumbnail_cursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(context.getContentResolver(), info.mId, MediaStore.Images.Thumbnails.MINI_KIND, thumb_proj);
				if( thumbnail_cursor != null && thumbnail_cursor.getCount() > 0 )
				{
					thumbnail_cursor.moveToFirst();
					info.mThumbnailPath = thumbnail_cursor.getString(thumbnail_cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
				}
				com.jaewoolee.photogridviewctl.utils.utilsEx.safeReleaseCursor(cursor);
				com.jaewoolee.photogridviewctl.utils.utilsEx.safeReleaseCursor(thumbnail_cursor);
				
				return info;
			}
		}
		
		Log.v(TAG, "getPhotoInfoFromUri(), can't find file from this uri: " + contentUri);
		
		return null;
	}
	
	public static photoInfoEx getPhotoInfoFromMediaStore(Context context, Cursor cursor, int position){
		photoInfoEx info = new photoInfoEx();
		
		if (cursor != null && cursor.moveToPosition(position)) {
			try{

				info.mId = cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media._ID));
				info.mFilePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
				info.mTitle = cursor.getString(cursor.getColumnIndexOrThrow(Images.Media.TITLE));
				info.mFileName = cursor.getString(cursor.getColumnIndexOrThrow(Images.Media.DISPLAY_NAME));
				
				long added = cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media.DATE_ADDED));
				Date addedDate = new Date(added*1000);
				info.mFileAdded = addedDate.toLocaleString();
				info.mFileAdded_original = added;

				long modified = cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media.DATE_MODIFIED));
				Date modifiedDate = new Date(modified*1000);
				info.mFileModified = modifiedDate.toLocaleString();
				
				info.mFileSize = cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media.SIZE));
				info.mMimeType = cursor.getString(cursor.getColumnIndexOrThrow(Images.Media.MIME_TYPE));
				info.mDescription = cursor.getString(cursor.getColumnIndexOrThrow(Images.Media.DESCRIPTION)); 
				info.mPicasa_id = cursor.getString(cursor.getColumnIndexOrThrow(Images.Media.PICASA_ID)); 
				info.mIsPrivate = cursor.getInt(cursor.getColumnIndexOrThrow(Images.Media.IS_PRIVATE));
				info.mLatitude = cursor.getDouble(cursor.getColumnIndexOrThrow(Images.Media.LATITUDE));
				info.mLongitude = cursor.getDouble(cursor.getColumnIndexOrThrow(Images.Media.LONGITUDE));
				info.mDateTaken = cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media.DATE_TAKEN));
				info.mOrientation = cursor.getInt(cursor.getColumnIndexOrThrow(Images.Media.ORIENTATION));
				
				String[] thumb_proj = { MediaStore.Images.Thumbnails.DATA };
				Cursor thumbnail_cursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(context.getContentResolver(), info.mId, MediaStore.Images.Thumbnails.MINI_KIND, thumb_proj);
				thumbnail_cursor.moveToFirst();
				info.mThumbnailPath = thumbnail_cursor.getString(thumbnail_cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));


				com.jaewoolee.photogridviewctl.utils.utilsEx.safeReleaseCursor(cursor);
				com.jaewoolee.photogridviewctl.utils.utilsEx.safeReleaseCursor(thumbnail_cursor);
				return info;
			}catch(Exception e){
				e.printStackTrace();
				return null;				
			}
		}
		
		return null;
	}
}
