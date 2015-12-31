package com.jaewoolee.photogridviewctl.photogridview;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.jaewoolee.photogridviewctl.R;
import com.jaewoolee.photogridviewctl.utils.photoInfoEx;
import com.jaewoolee.photogridviewctl.MainActivity;

import java.io.File;
import java.util.ArrayList;

public class photoGridAdapter extends CursorAdapter {
private final static String TAG="PhotoGridAdapter";

	private Context mContext;
	private DisplayMetrics metrics; 
	private GridView.LayoutParams mParam;
	private int mImageColumnIndex;
	private int mDataColumnIndex;
	private imageWorker mImageWorker;
	
	public static ArrayList<photoInfoEx> mSelectedPhotoList;
	
	public photoGridAdapter(Context context, Cursor c) {
		super(context, c);
		
		mContext = context;
		
		int size = context.getResources().getDimensionPixelSize(R.dimen.selection_grid_column_width);
		
		mParam = new GridView.LayoutParams(size, size);
		mImageColumnIndex = c.getColumnIndex(Images.Media._ID);
		mDataColumnIndex = c.getColumnIndexOrThrow(Images.Media.DATA);
		//mThumbDataColumnIndex = c.getColumnIndexOrThrow(Images.Thumbnails.DATA);
		mImageWorker = new imageWorker(context, 0, 0, false);
		setSelectedPhotoList(new ArrayList<photoInfoEx>());
		
		metrics = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);

	}
	
	public photoGridAdapter(Context context, Cursor c, int size) {
		super(context, c);
		
		mContext = context;
		
		mParam = new GridView.LayoutParams(size, size);
		mImageColumnIndex = c.getColumnIndex(Images.Media._ID);
		mDataColumnIndex = c.getColumnIndexOrThrow(Images.Media.DATA);
		//mThumbDataColumnIndex = c.getColumnIndexOrThrow(Images.Thumbnails.DATA);
		mImageWorker = new imageWorker(context, 0, 0, false);
		setSelectedPhotoList(new ArrayList<photoInfoEx>());
		
		metrics = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		final imageViewHolder holder = (imageViewHolder) view.getTag();
		final String path = cursor.getString(mDataColumnIndex);
        
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "onClick)+");

				Log.d(TAG, "bindView uses photoInfoEx.getPhotoInfoFromUri method executed");

				photoInfoEx tempInfo = photoInfoEx.getPhotoInfoFromUri(com.jaewoolee.photogridviewctl.photogridview.photoGridView.getActivityContext(), Uri.fromFile(new File(path)));
				
//				Log.d(TAG, "###################################" );
//				Log.d(TAG, "photoInfo.mId = " + tempInfo.mId );
//				Log.d(TAG, "photoInfo.mFilePath = " + tempInfo.mFilePath );
//				Log.d(TAG, "photoInfo.mThumbnailPath = " + tempInfo.mThumbnailPath );
//				Log.d(TAG, "photoInfo.mTitle = " + tempInfo.mTitle );
//				Log.d(TAG, "photoInfo.mFileName = " + tempInfo.mFileName );
//				Log.d(TAG, "photoInfo.mFileAdded = " + tempInfo.mFileAdded );
//				Log.d(TAG, "photoInfo.mFileAdded_original = " + tempInfo.mFileAdded_original );
//				Log.d(TAG, "photoInfo.mFileModified = " + tempInfo.mFileModified );
//				Log.d(TAG, "photoInfo.mFileSize = " + tempInfo.mFileSize );
//				Log.d(TAG, "photoInfo.mMimeType = " + tempInfo.mMimeType );
//				Log.d(TAG, "photoInfo.mDescription = " + tempInfo.mDescription );
//				Log.d(TAG, "photoInfo.mPicasa_id = " + tempInfo.mPicasa_id );
//				Log.d(TAG, "photoInfo.mIsPrivate = " + tempInfo.mIsPrivate );
//				Log.d(TAG, "photoInfo.mLatitude = " + tempInfo.mLatitude);
//				Log.d(TAG, "photoInfo.mLongitude = " + tempInfo.mLongitude );
//				Log.d(TAG, "photoInfo.mDateTaken = " + tempInfo.mDateTaken );
//				Log.d(TAG, "photoInfo.mOrientation = " + tempInfo.mOrientation );
				
				
				//SelectedPhotoItem tempItem = new SelectedPhotoItem(tempInfo);
				
				if( isContainInSelectedList(path) == -1 ) {
					//Log.d(TAG, "########## PhotoGridAdapter : view.setOnClickListener");
					
					if(getSelectedPhotos() >= MainActivity.getMaxSelectedPhotos() )
						return;
					
//					Drawable d = holder.ivImage.getDrawable();
//					d.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
//					holder.ivImage.setImageDrawable(d);
//					holder.ivImage.invalidate();
					
					mImageWorker.loadBitmap(holder.id, holder.ivImage, path);
					Drawable d = holder.ivImage.getDrawable();
					d.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
					
					Drawable[] layers = new Drawable[2];
					layers[0] = d;
					layers[1] = com.jaewoolee.photogridviewctl.photogridview.photoGridView.getActivityContext().getResources().getDrawable(R.drawable.check_white_150);
					LayerDrawable layerDrawable = new LayerDrawable(layers);
					holder.ivImage.setImageDrawable(layerDrawable);
					holder.ivImage.invalidate();
					
					getSelectedPhotoList().add(tempInfo);
					
					MainActivity.refreshSelectedPhotoCount( getSelectedPhotos() );
				}
				else
				{
//					Drawable d = holder.ivImage.getDrawable();
//					d.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
//					holder.ivImage.setImageDrawable(d);
//					holder.ivImage.invalidate();

					mImageWorker.loadBitmap(holder.id, holder.ivImage, path);
					Drawable d = holder.ivImage.getDrawable();
					d.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
					holder.ivImage.setImageDrawable(d);
					holder.ivImage.invalidate();
					
					removeFromSelectedList(path);
					
					MainActivity.refreshSelectedPhotoCount( getSelectedPhotos() );
				}
			}
		});
		
		//set image
		try{
			holder.id = cursor.getInt(mImageColumnIndex);		
			holder.ivImage.setScaleType(ScaleType.CENTER_CROP);
			mImageWorker.loadBitmap(holder.id, holder.ivImage, path);
			
			if( isContainInSelectedList(path) != -1 ) {
//				Log.d(TAG, "########## PhotoGridAdapter : Already Selected !!");
				
//				Drawable d = holder.ivImage.getDrawable();
//				d.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
//				holder.ivImage.setImageDrawable(d);
//				holder.ivImage.invalidate();
				
				Drawable d = holder.ivImage.getDrawable();
				d.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
				
				Drawable[] layers = new Drawable[2];
				layers[0] = d;
				layers[1] = com.jaewoolee.photogridviewctl.photogridview.photoGridView.getActivityContext().getResources().getDrawable(R.drawable.check_white_150);
				LayerDrawable layerDrawable = new LayerDrawable(layers);
				holder.ivImage.setImageDrawable(layerDrawable);
				holder.ivImage.invalidate();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView convertView = new ImageView(context);
		imageViewHolder holder = new imageViewHolder();
		
		holder.ivImage = (ImageView)convertView;
		holder.ivImage.setLayoutParams(mParam);
		convertView.setTag(holder);		
		
		return convertView;
	}

	@Override
	public void changeCursor(Cursor cursor) {
		//clear memcache
		mImageWorker.clearMemCache();
		mImageColumnIndex = cursor.getColumnIndex(Images.Media._ID);
		mDataColumnIndex = cursor.getColumnIndexOrThrow(Images.Media.DATA);
		
		super.changeCursor(cursor);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return super.getCount();
	}

	@Override
	public Cursor getCursor() {
		// TODO Auto-generated method stub
		return super.getCursor();
	}
	
	public String getImageFilePath(int position){
		Cursor c = getCursor();
		String filePath = "";
		if(c != null && !c.isClosed()){
		
			filePath = c.getString(mDataColumnIndex);
		}		
		
		return filePath;
	}
	
	public int isContainInSelectedList(String filePath){
		String photoPath = "";
		
		if(getSelectedPhotoList() != null && getSelectedPhotoList().size() > 0){
			for(int i =0; i < getSelectedPhotoList().size(); i++){
				photoPath = getSelectedPhotoList().get(i).mFilePath;
				if(photoPath != null && filePath.compareTo(photoPath) == 0){
					return i;
				}
			}
		}
		return -1;
	}

	public static ArrayList<photoInfoEx> getSelectedPhotoList() {
		return mSelectedPhotoList;
	}

	public void setSelectedPhotoList(ArrayList<photoInfoEx> mSelectedPhotoList) {
		this.mSelectedPhotoList = mSelectedPhotoList;
	}
	
	public static int getSelectedPhotos() {
		return mSelectedPhotoList.size();
	}

	public static void resetSelectedPhotoList() {
		mSelectedPhotoList.clear();
	}
	
	public void removeFromSelectedList(String filePath){
		String photoPath = "";
		
		if(getSelectedPhotoList() != null && getSelectedPhotoList().size() > 0){
			for(int i =0; i < getSelectedPhotoList().size(); i++){
				photoPath = getSelectedPhotoList().get(i).mFilePath;
				if(photoPath != null && filePath.compareTo(photoPath) == 0){
					getSelectedPhotoList().remove(i);
				}
			}
		}
	}

	
//	public void setMode(int mode){
//		mMode = mode;
//	}
	
	public void setPauseImageWork(boolean pauseWork){
		if(mImageWorker != null){
			mImageWorker.setPauseWork(pauseWork);
		}
	}
	
	public void setExitTasksEarlyImageWork(boolean exitTasksEarly){
		if(mImageWorker != null){
			mImageWorker.setExitTasksEarly(exitTasksEarly);
		}
	}
	
	public int scaleObject(int dp) {
		return (int) (dp * metrics.density );
	}
}
