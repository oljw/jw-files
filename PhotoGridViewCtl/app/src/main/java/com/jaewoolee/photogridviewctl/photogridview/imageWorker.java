package com.jaewoolee.photogridviewctl.photogridview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.provider.MediaStore.Images;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.ImageView;

import com.jaewoolee.photogridviewctl.R;

import java.lang.ref.WeakReference;

public class imageWorker {
private final static String TAG="ImageWorker";
	
    private static final int FADE_IN_TIME = 200;

    private LruCache<String, Bitmap> mMemoryCache;
    private Bitmap mLoadingBitmap;
    private Context mContext;
    protected boolean mPauseWork = false;
    private boolean mExitTasksEarly = false;
    private final Object mPauseWorkLock = new Object();
    
    private boolean mHighQualityImage = false;
    
    private int mImageWidth = 0;
	private int mImageHeight = 0;
    
    //width and height is for high quality image.
    public imageWorker(Context context, int width, int height, boolean isHighQuality) {
    	mHighQualityImage = isHighQuality;
    	mImageWidth = width;
    	mImageHeight = height;
        mContext = context;
        
        mLoadingBitmap = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.loading_rect)).getBitmap();
        //mLoadingBitmap = null;
        
        //cache
  		// Get max available VM memory, exceeding this amount will throw an
  	    // OutOfMemory exception. Stored in kilobytes as LruCache takes an
  	    // int in its constructor.
  	    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
  	    // Use 1/8th of the available memory for this memory cache.
  	    final int cacheSize = maxMemory / 8;

  	    //ICANMOBILE (-)
  	    /*
  	    mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
  	        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
			@Override
  	        protected int sizeOf(String key, Bitmap bitmap) {
  	            // The cache size will be measured in kilobytes rather than
  	            // number of items.
  	        	if(Build.VERSION.SDK_INT <Build.VERSION_CODES.HONEYCOMB_MR1){
  	        		return bitmap.getRowBytes() * bitmap.getHeight()/1024;
  	        	}else{
  	        		return bitmap.getByteCount()/1024;
  	        	}
  	        }
  	    };
  	    */
  	    mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
  	        protected int sizeOf(String key, Bitmap bitmap) {
  	            // The cache size will be measured in kilobytes rather than
  	            // number of items.
  	        	return bitmap.getRowBytes() * bitmap.getHeight()/1024;
  	        }
  	    };
    }

    public void loadBitmap(int resId, ImageView imageView, String filePath){
		final String imageKey = String.valueOf(resId);

	    final Bitmap bitmap = getBitmapFromMemCache(imageKey);
	    if (bitmap != null) {
	        imageView.setImageBitmap(bitmap);
	    }
	    else if (cancelPotentialWork(resId, imageView)) {
	    	final BitmapWorkerTask task = new BitmapWorkerTask(imageView, filePath);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(mContext.getResources(), mLoadingBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            
            try{
            	task.executeOnExecutor(asyncTask.DUAL_THREAD_EXECUTOR, resId);
            }catch(Exception e){
            	task.cancel(true);
            	e.printStackTrace();	
            }
	    }
	}
	
	 static class AsyncDrawable extends BitmapDrawable {
	        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;
	
	        public AsyncDrawable(Resources res, Bitmap bitmap,
	                BitmapWorkerTask bitmapWorkerTask) {
	            super(res, bitmap);
	            bitmapWorkerTaskReference =
	                new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
	        }
	
	        public BitmapWorkerTask getBitmapWorkerTask() {
	            return bitmapWorkerTaskReference.get();
	        }
	    }
	
	    public static boolean cancelPotentialWork(int data, ImageView imageView) {
	        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
	
	        if (bitmapWorkerTask != null) {
	            final int bitmapData = bitmapWorkerTask.data;
	            if (bitmapData != data) {
	                // Cancel previous task
	                bitmapWorkerTask.cancel(true);
	            } else {
	                // The same work is already in progress
	                return false;
	            }
	        }
	        // No task associated with the ImageView, or an existing task was cancelled
	        return true;
	    }
	
	    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
	       if (imageView != null) {
	           final Drawable drawable = imageView.getDrawable();
	           if (drawable instanceof AsyncDrawable) {
	               final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
	               return asyncDrawable.getBitmapWorkerTask();
	           }
	        }
	        return null;
	    }
	
	    class BitmapWorkerTask extends asyncTask<Integer,  Void, Bitmap> {
	        private final WeakReference<ImageView> imageViewReference;
	        private int data = 0;
	        private String mFilePath="";

	        public BitmapWorkerTask(ImageView imageView, String filePath) {
	            // Use a WeakReference to ensure the ImageView can be garbage collected
	            imageViewReference = new WeakReference<ImageView>(imageView);
	            mFilePath = filePath;
	        }

	        // Decode image in background.
	        @Override
	        protected Bitmap doInBackground(Integer... params) {
	            data = params[0];
	            Bitmap bm = null;
	            
	            //check task cancel policy
	            checkCancelPolicy();
	            
	            // Wait here if work is paused and the task is not cancelled
	            synchronized (mPauseWorkLock) {
	                while (mPauseWork && !isCancelled()) {
	                    try {
	                        mPauseWorkLock.wait();
	                    } catch (InterruptedException e) {}
	                }
	            }
	            
	            
	            if(!isCancelled() && !mExitTasksEarly){
	            	try{
	            		if(mHighQualityImage && !TextUtils.isEmpty(mFilePath)){
			            	bm= com.jaewoolee.photogridviewctl.photogridview.utilBitmap.decodeSampledBitmapFromFile(mFilePath, mImageWidth, mImageHeight);
	            		}else{
			            	bm= Images.Thumbnails.getThumbnail(mContext.getContentResolver(), data, Images.Thumbnails.MICRO_KIND, null);
	            		}
		            	bm = com.jaewoolee.photogridviewctl.photogridview.utilBitmap.GetExifOrientation(mFilePath, bm);
		            }catch(Exception e){
			            e.printStackTrace();
		            }
		            
		            addBitmapToMemoryCache(String.valueOf(params[0]), bm);	            
		            
	            	if(bm == null && mFilePath != null && com.jaewoolee.photogridviewctl.photogridview.utilCommon.isExistFile(mFilePath)){
	            		bm = ((BitmapDrawable)mContext.getResources().getDrawable(R.drawable.loading_rect)).getBitmap();
					}
	            }
	            return bm;
	        }

	        // Once complete, see if ImageView is still around and set bitmap.
	        @Override
	        protected void onPostExecute(Bitmap bitmap) {	        	
	        	if(isCancelled() && mExitTasksEarly){
	        		bitmap = null;
	        	}
	        	
	        	if (imageViewReference != null && bitmap != null) {
	                final ImageView imageView = imageViewReference.get();
	                final BitmapWorkerTask bitmapWorkerTask =
	                        getBitmapWorkerTask(imageView);
	                if (this == bitmapWorkerTask && imageView != null) {
	                    setImageDrawable(imageView, bitmap);
	                }
	            }
	        }

			@Override
			protected void onCancelled(Bitmap result) {
				super.onCancelled(result);
	            synchronized (mPauseWorkLock) {
	                mPauseWorkLock.notifyAll();
	            }
			}
			
			private void checkCancelPolicy(){
				// cancel task, if reference view is null or isShown is false.
	            if (imageViewReference != null){
	            	if(imageViewReference.get() != null){	//ImageView is. 	            	
	            			if(!imageViewReference.get().isShown() && mPauseWork){ // isShow is false. view is invisible. just cancel. View will update using new task later.
	            				cancel(true);
//	            				Log.d("NOW", "NOW, doInBackground, isShow is false task cancel data = " + data);
		            		}
	            	}
	            	else{
	            		//imageView is gone. So it doesn't need task.
	            		cancel(true);
//	            		Log.d("NOW", "NOW, imageView is gone. doInBackground, task cancel data = " + data);
	            	}
	            }else{
	            	//imageViewReference is null. So it doesn't need task. 
	            	cancel(true);
//	            	Log.d("NOW", "NOW, doInBackground, imageViewReference is null task cancel data = " + data);
	            }
			}
	    }
	
	    /**
	     * Called when the processing is complete and the final drawable should be 
	     * set on the ImageView.
	     *
	     * @param imageView
	     * @param drawable
	     */
	    private void setImageDrawable(ImageView imageView, Bitmap bitmap) {
	    	BitmapDrawable bd = new BitmapDrawable(bitmap);
	    	
            // Transition drawable with a transparent drawable and the final drawable
            final TransitionDrawable td =
                    new TransitionDrawable(new Drawable[] {
                            new ColorDrawable(android.R.color.transparent),
                            bd
                    });
//            // Set background to loading bitmap
//            imageView.setBackgroundDrawable(new BitmapDrawable(mLoadingBitmap));

            imageView.setImageDrawable(td);
            td.startTransition(FADE_IN_TIME);
	    }
	
	    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	        if (getBitmapFromMemCache(key) == null && bitmap != null) {
	            mMemoryCache.put(key, bitmap);
	        }
	    }

	    public Bitmap getBitmapFromMemCache(String key) {
	        return mMemoryCache.get(key);
	    }
	    
	    public void clearMemCache(){
	    	mMemoryCache.evictAll();
	    }
	    
	    /**
	     * Pause any ongoing background work. This can be used as a temporary
	     * measure to improve performance. For example background work could
	     * be paused when a ListView or GridView is being scrolled using a
	     * {@link android.widget.AbsListView.OnScrollListener} to keep
	     * scrolling smooth.
	     * <p>
	     * If work is paused, be sure setPauseWork(false) is called again
	     * before your fragment or activity is destroyed (for example during
	     * {@link android.app.Activity#onPause()}), or there is a risk the
	     * background thread will never finish.
	     */
	    public void setPauseWork(boolean pauseWork) {
	        synchronized (mPauseWorkLock) {
	            mPauseWork = pauseWork;
	            if (!mPauseWork) {
	                mPauseWorkLock.notifyAll();
	            }
	        }
	    }
	    
	    public void setExitTasksEarly(boolean exitTasksEarly) {
	        mExitTasksEarly = exitTasksEarly;
	        setPauseWork(false);
	    }
}