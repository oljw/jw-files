package com.jaewoolee.photogridviewctl.photogridview;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView.ScaleType;

import java.io.File;


public class photoGridView extends GridView {
	private static final String TAG="PhotoGridView";
	
	public static String PHOTOGALLERY_DIR = "/mnt/sdcard/PhotoGallery";
	public static String PACKAGE_NAME = "com.magicglasssoft.photogallery";
	
	private static Context mContext;
	private photoGridAdapter mAdapter;
	private Cursor mCursor;
	
	//private String mPhotoFolder = "/mnt/sdcard/DCIM/Camera";
	private String mPhotoFolder = "/";
	private int mDisplayW = 0;
	private int mDisplayH = 0;
	
	public photoGridView(Context context) {
		super(context);
		mContext = context;
		//init();
		
	}
	
	public photoGridView( Context context, AttributeSet attrs ) {
		super( context, attrs );
		mContext = context;
		//init();
	}
	
	public static Context getActivityContext() {
		return mContext;
	}
	
	public void setPhotoFolder( String folder ) {
		mPhotoFolder = folder;
	}
	
	public void setDisplaySize(int width, int height) {
		mDisplayW = width;
		mDisplayH = height;
	}
	
	public void init()
	{
		Log.d(TAG, "########## PhotoGridView : init : mPhotoFolder = " + mPhotoFolder );
		
		int distance = 6;

		int size = (int)(Math.floor(mDisplayW/3) - distance);
		mCursor = getFolderCursorFromContentResolver(mPhotoFolder);
		//mAdapter = new PhotoGridAdapter(mContext, mCursor);
		mAdapter = new photoGridAdapter(mContext, mCursor, size);
		setAdapter( mAdapter );
		setVerticalSpacing(distance);
		
		
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				
				Log.d(TAG, "########## PhotoGridView : onItemClick");
				
				imageViewHolder holder = (imageViewHolder) v.getTag();
				holder.ivImage.setScaleType(ScaleType.FIT_CENTER);
				
				Bitmap thumb = MediaStore.Images.Thumbnails.getThumbnail(
                        mContext.getContentResolver(), holder.id,
                        MediaStore.Images.Thumbnails.MINI_KIND,
                        (BitmapFactory.Options) null );
				
				/*
				mImageView.setImageBitmap(thumb);
				mImageView.setScaleType(ScaleType.FIT_CENTER);
				*/
			}
		});
		
		OnScrollListener gridScroll = new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// Pause ImageWork to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                	mAdapter.setPauseImageWork(true);
                } else {
                	mAdapter.setPauseImageWork(false);
                }
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		};
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){ // < honeycomb, fix for scroll issue 
			setOnTouchListener(new fingerTracker(gridScroll));
		}else{
			setOnScrollListener(gridScroll);			
		}
	}

	public void init(String rootDir, int display_width)
	{
		Log.d(TAG, "########## PhotoGridView : init : mPhotoFolder = " + mPhotoFolder );
		
		mPhotoFolder = rootDir;
		mDisplayW = display_width;
		
		int distance = 4;

//		int size = (int)(Math.round(mDisplayW/3) - distance);
		int size = (int)(Math.round(mDisplayW/3) - ((distance*2)/3) );

		Log.d(TAG, "mDisplayW = " + mDisplayW );
		Log.d(TAG, "size = " + size );
		Log.d(TAG, "total size = " + (size*3) );
		
		
		setColumnWidth( size );
		setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		
		mCursor = getFolderCursorFromContentResolver(mPhotoFolder);
		//mAdapter = new PhotoGridAdapter(mContext, mCursor);
		mAdapter = new photoGridAdapter(mContext, mCursor, size);
		setAdapter( mAdapter );
		
		
		setHorizontalSpacing(distance);
	
		setVerticalSpacing(distance);
		
		
		
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				
				Log.d(TAG, "########## PhotoGridView : onItemClick");
				
				imageViewHolder holder = (imageViewHolder) v.getTag();
				holder.ivImage.setScaleType(ScaleType.FIT_CENTER);
				
				Bitmap thumb = MediaStore.Images.Thumbnails.getThumbnail(
                        mContext.getContentResolver(), holder.id,
                        MediaStore.Images.Thumbnails.MINI_KIND,
                        (BitmapFactory.Options) null );
				
				/*
				mImageView.setImageBitmap(thumb);
				mImageView.setScaleType(ScaleType.FIT_CENTER);
				*/
			}
		});
		
		OnScrollListener gridScroll = new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// Pause ImageWork to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                	mAdapter.setPauseImageWork(true);
                } else {
                	mAdapter.setPauseImageWork(false);
                }
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		};
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){ // < honeycomb, fix for scroll issue 
			setOnTouchListener(new fingerTracker(gridScroll));
		}else{
			setOnScrollListener(gridScroll);			
		}
	}

	
	public final static String[] FOLDER_PROJECTION = new String[] {  Images.Media._ID, Images.Media.DATA, Images.Media.DISPLAY_NAME,
		Images.Media.BUCKET_DISPLAY_NAME, Images.Media.BUCKET_ID, Images.Media.TITLE, Images.Media.DATE_ADDED, 
		Images.Media.DATE_MODIFIED, Images.Media.SIZE, Images.Media.MIME_TYPE, Images.Media.DESCRIPTION, 
		Images.Media.PICASA_ID, Images.Media.IS_PRIVATE, Images.Media.LATITUDE, Images.Media.LONGITUDE, 
		Images.Media.DATE_TAKEN, Images.Media.ORIENTATION};
	
	public Cursor getFolderCursorFromContentResolver(String folderPath) {
		char lastChar = folderPath.charAt(folderPath.length() - 1);
		if('/' == lastChar) {
			folderPath = com.jaewoolee.photogridviewctl.photogridview.utilCommon.safeSubString(folderPath, 0, folderPath.length() - 2);
		}
		
		Log.d(TAG, "########## folderPath = " + folderPath);
		
		Cursor cursor;
		if( folderPath.length() > 1 ) {
			long bucketId = getBucketIdFromFolderPath(folderPath);
			// getting cursor
			String selection = Images.Media.BUCKET_ID + "='" + bucketId + "'";
//			cursor = mContext.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI, FOLDER_PROJECTION, 
//					                                     selection, null, Images.Media.DATE_TAKEN + " DESC, " + Images.Media.DISPLAY_NAME + " DESC");
			cursor = mContext.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI, FOLDER_PROJECTION, 
                    selection, null, Images.Media.DATE_TAKEN + " DESC");
//			cursor = mContext.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI, FOLDER_PROJECTION, 
//                  selection, null, Images.Media.DISPLAY_NAME + " DESC");
		}
		else {
//			cursor = mContext.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI, FOLDER_PROJECTION, 
//                    									 null, null, Images.Media.DATE_TAKEN + " DESC, " + Images.Media.DISPLAY_NAME + " DESC");
			cursor = mContext.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI, FOLDER_PROJECTION, 
					 null, null, Images.Media.DATE_TAKEN + " DESC");
//			cursor = mContext.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI, FOLDER_PROJECTION, 
//					 null, null, Images.Media.DISPLAY_NAME + " DESC");
		}
		
		
		if(cursor != null){
			cursor.moveToFirst();
		}				
				
		return cursor;
	}
	
	public static int COL_INDEX_ID = 0;
	public static int COL_INDEX_DATA = 1;
	public static int COL_INDEX_DISPLAY_NAME = 2;
	public static int COL_INDEX_BUCKET_DISPLAY_NAME = 3;
	public static int COL_INDEX_BUCKET_ID = 4;
	
	public final static int INVALID_VALUE = -1;
	
	private long getBucketIdFromFolderPath(String folderPath) {
		Cursor galleryFolderCursor = getGalleryFolderCursor();
		if(null == galleryFolderCursor) {
			return INVALID_VALUE;
		}
		
		galleryFolderCursor.moveToFirst();
		while (false == galleryFolderCursor.isAfterLast()) {
			String pathAndAnme = galleryFolderCursor.getString(COL_INDEX_DATA);
			File file = new File(pathAndAnme);
			String onlyPath = file.getParent();
			if(onlyPath != null && folderPath != null && onlyPath.equalsIgnoreCase(folderPath)) {
				return galleryFolderCursor.getLong(COL_INDEX_BUCKET_ID);
			}
			
			galleryFolderCursor.moveToNext();
		}
		
		return INVALID_VALUE;
	}
	
	public Cursor getGalleryFolderCursor() {
		// "GROUP BY REFERENCE"
		// http://insidecoding.com/2012/02/08/some-sql-injection-in-android-how-to-use-group-by-and-case-when-you-are-not-allowed-to-do-so/
		String selection = Images.Media.BUCKET_ID + " IS NOT NULL) GROUP BY (" + Images.Media.BUCKET_ID;

		// query from content resolver
		Cursor cursor = mContext.getContentResolver().query(
				Images.Media.EXTERNAL_CONTENT_URI, FOLDER_PROJECTION, selection,
				null, Images.Media.BUCKET_DISPLAY_NAME + " ASC");
		
		return cursor;
	}
	
	public void refreshGridView() {
		mCursor = getFolderCursorFromContentResolver(mPhotoFolder);
		mAdapter.changeCursor(mCursor);
		mAdapter.resetSelectedPhotoList();
	}
}