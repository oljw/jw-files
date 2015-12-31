package com.jaewoolee.photogridviewctl.photogridview;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;

import com.jaewoolee.photogridviewctl.utils.utilsEx;

import java.util.Date;

public class photoInfo {
	private final static String TAG="PhotoInfo";

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
	
	public static photoInfo getPhotoInfoFromUri(Uri contentUri) {
		Cursor cursor = null;
		if (contentUri.getScheme().compareTo("file") == 0) {
			String filePath = contentUri.getPath();
			filePath = DatabaseUtils.sqlEscapeString(filePath);
			cursor = photoGridView.getActivityContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data = " + filePath, null,null);
		} else {
			cursor = photoGridView.getActivityContext().getContentResolver().query(contentUri, null, null, null, null);
		}
		
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				photoInfo info = new photoInfo();
				
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
				Cursor thumbnail_cursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(photoGridView.getActivityContext().getContentResolver(), info.mId, MediaStore.Images.Thumbnails.MINI_KIND, thumb_proj);
				thumbnail_cursor.moveToFirst();
				info.mThumbnailPath = thumbnail_cursor.getString(thumbnail_cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
				
				utilsEx.safeReleaseCursor(cursor);
				utilsEx.safeReleaseCursor(thumbnail_cursor);
				return info;
			}
		}
		
		Log.v(TAG, "getPhotoInfoFromUri(), can't find file from this uri: " + contentUri);
		
		return null;
	}
}

