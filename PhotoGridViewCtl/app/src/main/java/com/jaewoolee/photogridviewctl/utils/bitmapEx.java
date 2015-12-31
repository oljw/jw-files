package com.jaewoolee.photogridviewctl.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class bitmapEx {
private final static String TAG="bitmapEx";
	
	public static Bitmap rawDataToBitmap(Bitmap bitmap, int[] raw, int w, int h)
	{
		Log.d( TAG, "w = " + w + ", h = " + h );

		if( raw == null || w <= 0 || h <= 0 ) {
			Log.d( TAG, "RETURN NULL => w = " + w + ", h = " + h );
			return null;
		}
		
		if( bitmap == null || w != bitmap.getWidth() || h != bitmap.getHeight() || bitmap.getConfig() != Bitmap.Config.ARGB_8888 )
		{
			Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			bmp.setPixels(raw, 0, w, 0, 0, w, h);
			return bmp;
		}
		else
		{
			bitmap.setPixels(raw, 0, w, 0, 0, w, h);
			return bitmap;
		}
	}
	
	public static Bitmap loadImage(String imagePath ) {
		Log.d(TAG, "loadImage method used");

		Bitmap bmp = loadJPEG(imagePath);
		if (bmp == null)
			return null;

		com.jaewoolee.photogridviewctl.utils.jpegExifEx exif = com.jaewoolee.photogridviewctl.utils.jpegExifEx.getJPEGExif(imagePath);
		if (exif.getOrientation() == ORIENTATION_ROTATE_90) {
			bmp = rotateBitmap(bmp, 90);
		} else if (exif.getOrientation() == ORIENTATION_ROTATE_180) {
			bmp = rotateBitmap(bmp, 180);
		} else if (exif.getOrientation() == ORIENTATION_ROTATE_270) {
			bmp = rotateBitmap(bmp, 270);
		}
		return bmp;
	}
	
	//ICANMOBILE (+) 20140331 for Android Version 4.4.2 Kitkat
	public static Bitmap loadImage(String imagePath, boolean bInputShareable )
	{
		Bitmap bmp = loadJPEG( imagePath, bInputShareable );
		if( bmp == null )
			return null;
		
		com.jaewoolee.photogridviewctl.utils.jpegExifEx exif = com.jaewoolee.photogridviewctl.utils.jpegExifEx.getJPEGExif(imagePath);
		if( exif.getOrientation() == ORIENTATION_ROTATE_90 ){
			bmp = rotateBitmap( bmp, 90);
		}
		else if( exif.getOrientation() == ORIENTATION_ROTATE_180 ){
			bmp = rotateBitmap( bmp, 180);
		}
		else if( exif.getOrientation() == ORIENTATION_ROTATE_270 ){
			bmp = rotateBitmap( bmp, 270);
		}
		return bmp;
	}
	//END ICANMOBILE
	
	public static Bitmap loadImage(String imagePath, int sampleSize){
		Bitmap bmp = loadJPEG(imagePath, sampleSize);
		if( bmp == null )
			return null;
		
		com.jaewoolee.photogridviewctl.utils.jpegExifEx exif = com.jaewoolee.photogridviewctl.utils.jpegExifEx.getJPEGExif(imagePath);
		if( exif.getOrientation() == ORIENTATION_ROTATE_90 ){
			if( bmp.getWidth() > bmp.getHeight() )
				bmp = rotateBitmap( bmp, 90);
		}
		else if( exif.getOrientation() == ORIENTATION_ROTATE_180 ){
			bmp = rotateBitmap( bmp, 180);
		}
		else if( exif.getOrientation() == ORIENTATION_ROTATE_270 ){
			if( bmp.getWidth() > bmp.getHeight() )
				bmp = rotateBitmap( bmp, 270);
		}
		
		return bmp;
	}
	
	public static Bitmap loadImage(String imagePath, int maxW, int maxH, int orientation  ){
		Bitmap bmp = null;
		
		com.jaewoolee.photogridviewctl.utils.jpegExifEx exif = com.jaewoolee.photogridviewctl.utils.jpegExifEx.getJPEGExif(imagePath);
		
		int photoW = exif._width;
		int photoH = exif._height;
		
		Log.d(TAG, "photoW = " + photoW + ", photoH = " + photoH );
		Log.d(TAG, "exif.getOrientation = " + exif.getOrientation() );
		Log.d(TAG, "orientation = " + orientation );

		if( photoW == 0 || photoH == 0 ) {
			bmp = loadJPEG(imagePath);
			if( bmp == null )
				return null;
			
			photoW = bmp.getWidth();
			photoH = bmp.getHeight();
		}

		if( photoW > maxW || photoH > maxH ) {
			int sampleSize = 1;
			while( photoW/sampleSize > maxW || photoH/sampleSize > maxH ) {
				sampleSize++;
			}
			
			bmp = loadJPEG(imagePath, --sampleSize );
			if( bmp == null )
				return null;
			
			if( orientation == ORIENTATION_ROTATE_90 ){
				if( bmp.getWidth() > bmp.getHeight() )
					bmp = rotateBitmap( bmp, 90);
			}
			else if( orientation == ORIENTATION_ROTATE_180 ){
					bmp = rotateBitmap( bmp, 180);
			}
			else if( orientation == ORIENTATION_ROTATE_270 ){
				if( bmp.getWidth() > bmp.getHeight() )
					bmp = rotateBitmap( bmp, 270);
			}
			
//			if( exif.getOrientation() == ORIENTATION_ROTATE_90 ){
//				if( bmp.getWidth() > bmp.getHeight() )
//					bmp = rotateBitmap( bmp, 90);
//			}
//			else if( exif.getOrientation() == ORIENTATION_ROTATE_180 ){
//					bmp = rotateBitmap( bmp, 180);
//			}
//			else if( exif.getOrientation() == ORIENTATION_ROTATE_270 ){
//				if( bmp.getWidth() > bmp.getHeight() )
//					bmp = rotateBitmap( bmp, 270);
//			}
			
		}
		else {
			bmp = loadJPEG(imagePath);
			if( bmp == null )
				return null;
			
			if( orientation == ORIENTATION_ROTATE_90 ){
				if( bmp.getWidth() > bmp.getHeight() )
					bmp = rotateBitmap( bmp, 90);
			}
			else if( orientation == ORIENTATION_ROTATE_180 ){
				bmp = rotateBitmap( bmp, 180);
			}
			else if( orientation == ORIENTATION_ROTATE_270 ){
				if( bmp.getWidth() > bmp.getHeight() )
					bmp = rotateBitmap( bmp, 270);
			}
			
//			if( exif.getOrientation() == ORIENTATION_ROTATE_90 ){
//				if( bmp.getWidth() > bmp.getHeight() )
//					bmp = rotateBitmap( bmp, 90);
//			}
//			else if( exif.getOrientation() == ORIENTATION_ROTATE_180 ){
//				bmp = rotateBitmap( bmp, 180);
//			}
//			else if( exif.getOrientation() == ORIENTATION_ROTATE_270 ){
//				if( bmp.getWidth() > bmp.getHeight() )
//					bmp = rotateBitmap( bmp, 270);
//			}
		}
		
		float bmp_ratio_w = 1.0f;
		float bmp_ratio_h = 1.0f;
		if( bmp.getWidth() > bmp.getHeight() )
			bmp_ratio_w = (float)bmp.getWidth()/(float)bmp.getHeight();
		else
			bmp_ratio_h = (float)bmp.getHeight()/(float)bmp.getWidth();
		
		int resize_w = 0;
		int resize_h = 0;
		if( bmp_ratio_w > bmp_ratio_h ) {
			if( bmp.getWidth() > maxW ) {
				resize_w = maxW;
				int distance = bmp.getWidth() - maxW;
				resize_h = bmp.getHeight() - Math.round( distance / bmp_ratio_w );
				
				if( resize_h > maxH ) {
					distance = resize_h - maxH;
					resize_h = maxH;
					resize_w -= Math.round( distance / bmp_ratio_h );
				}
			}
			else {
				resize_w = maxW;
				int distance = maxW - bmp.getWidth();
				resize_h = bmp.getHeight() + Math.round( distance / bmp_ratio_w );
				
				if( resize_h > maxH ) {
					distance = resize_h - maxH;
					resize_h = maxH;
					resize_w -= Math.round( distance / bmp_ratio_h );
				}
			}
		}
		else {
			if( bmp.getHeight() > maxH ) {
				resize_h = maxH;
				int distance = bmp.getHeight() - maxH;
				resize_w = bmp.getWidth() - Math.round( distance / bmp_ratio_h );
				
				if( resize_w > maxW ) {
					distance = resize_w - maxW;
					resize_w = maxW;
					resize_h -= Math.round( distance / bmp_ratio_w );
				}
			}
			else {
				resize_h = maxH;
				int distance = maxH - bmp.getHeight();
				resize_w = bmp.getWidth() + Math.round( distance / bmp_ratio_h );
				
				if( resize_w > maxW ) {
					distance = resize_w - maxW;
					resize_w = maxW;
					resize_h -= Math.round( distance / bmp_ratio_w );
				}
			}
		}
		
		bmp = bitmapEx.resizeBitmap(bmp, resize_w, resize_h );
		if( bmp == null )
			return null;

		return bmp;
	}
	
	public static Bitmap loadImage(String imagePath, float resizeScale)
	{
		Bitmap bmp = loadJPEG(imagePath);
		
		if( bmp == null )
			return null;
	
		bmp = resizeBitmap(bmp, (int)(bmp.getWidth() * resizeScale), (int)(bmp.getHeight() * resizeScale) );
	
		com.jaewoolee.photogridviewctl.utils.jpegExifEx exif = com.jaewoolee.photogridviewctl.utils.jpegExifEx.getJPEGExif(imagePath);
		if( exif.getOrientation() == ORIENTATION_ROTATE_90 ){
			bmp = rotateBitmap( bmp, 90);
		}
		else if( exif.getOrientation() == ORIENTATION_ROTATE_180 ){
			bmp = rotateBitmap( bmp, 180);
		}
		else if( exif.getOrientation() == ORIENTATION_ROTATE_270 ){
			bmp = rotateBitmap( bmp, 270);
		}
		return bmp;
	}
	
	public static Bitmap loadJPEG(String jpegPath) {
		Bitmap bm = null;
	    BitmapFactory.Options bfOptions=new BitmapFactory.Options();
	    bfOptions.inDither=true;                     //Disable Dithering mode
	    bfOptions.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
	    bfOptions.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
	    bfOptions.inTempStorage=new byte[32 * 1024]; 


	    File file=new File(jpegPath);
	    FileInputStream fs=null;
	    try {
	        fs = new FileInputStream(file);
	    } catch (FileNotFoundException e) {
	        //TODO do something intelligent
	        e.printStackTrace();
	    }

	    try {
	        if(fs!=null) bm=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
	    } catch (IOException e) {
	        //TODO do something intelligent
	        e.printStackTrace();
	    } finally{ 
	        if(fs!=null) {
	            try {
	                fs.close();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	    }
	    
	    return bm;
	}
	
	//ICANMOBILE (+) 20140331 for Android Version 4.4.2 Kitkat
	public static Bitmap loadJPEG(String jpegPath, boolean bInputShareable) {
		Bitmap bm = null;
	    BitmapFactory.Options bfOptions=new BitmapFactory.Options();
	    bfOptions.inDither=true;                     //Disable Dithering mode
	    bfOptions.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
	    bfOptions.inInputShareable=bInputShareable;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
	    bfOptions.inTempStorage=new byte[32 * 1024]; 


	    File file=new File(jpegPath);
	    FileInputStream fs=null;
	    try {
	        fs = new FileInputStream(file);
	    } catch (FileNotFoundException e) {
	        //TODO do something intelligent
	        e.printStackTrace();
	    }

	    try {
	        if(fs!=null) bm=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
	    } catch (IOException e) {
	        //TODO do something intelligent
	        e.printStackTrace();
	    } finally{ 
	        if(fs!=null) {
	            try {
	                fs.close();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	    }
	    
	    return bm;
	}
	//END ICANMOBILE
	
	public static Bitmap loadJPEG(String jpegPath, int sampleSize) {
		Bitmap bm = null;
	    BitmapFactory.Options bfOptions=new BitmapFactory.Options();
	    bfOptions.inDither=true;                     //Disable Dithering mode
	    bfOptions.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
	    bfOptions.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
	    bfOptions.inTempStorage=new byte[32 * 1024]; 
	    bfOptions.inSampleSize=sampleSize;

	    File file=new File(jpegPath);
	    FileInputStream fs=null;
	    try {
	        fs = new FileInputStream(file);
	    } catch (FileNotFoundException e) {
	        //TODO do something intelligent
	        e.printStackTrace();
	    }

	    try {
	        if(fs!=null) bm=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
	    } catch (IOException e) {
	        //TODO do something intelligent
	        e.printStackTrace();
	    } finally{ 
	        if(fs!=null) {
	            try {
	                fs.close();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	    }
	    
	    return bm;
	}
	
	
	static Bitmap mRotatedBmp = null;
	public static Bitmap rotateBitmap(Bitmap bmp, float rot){        
        Matrix matrix = new Matrix();
        matrix.postRotate(rot);
        mRotatedBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        
        if( bmp != null ){
        	bmp.recycle();
        	bmp = null;
		}
        System.gc();
        
        return mRotatedBmp;
	}
	
	static Bitmap resizedBmp = null;
	public static Bitmap resizeBitmap(Bitmap bmp, int resizeWidth, int resizeHeight){
		Log.d(TAG, "resizeWidth = " + resizeWidth + " resizeHeight = " + resizeHeight);
		resizedBmp = Bitmap.createScaledBitmap(bmp, resizeWidth, resizeHeight, false).copy(Config.ARGB_8888, true);
		
		
        if( bmp != null ){
        	bmp.recycle();
        	bmp = null;
		}
        System.gc();
        
		return resizedBmp;
	}
	
	public static Bitmap centerCropBitmap(Bitmap srcBmp ) {
		Bitmap dstBmp;
		if (srcBmp.getWidth() >= srcBmp.getHeight()){

			  dstBmp = Bitmap.createBitmap(
			     srcBmp, 
			     srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
			     0,
			     srcBmp.getHeight(), 
			     srcBmp.getHeight()
			     );

			}else{

			  dstBmp = Bitmap.createBitmap(
			     srcBmp,
			     0, 
			     srcBmp.getHeight()/2 - srcBmp.getWidth()/2,
			     srcBmp.getWidth(),
			     srcBmp.getWidth() 
			     );
			}
		
		return dstBmp;
	}
	
	public static boolean deleteFile(String filepath) {
		
		boolean ret = false;
		File file = new File(filepath);
		if(file.exists())
			ret = file.delete();

		return ret;
	}
	
		
	public static final int ORIENTATION_UNDEFINED = 0;
    public static final int ORIENTATION_NORMAL = 1;
    public static final int ORIENTATION_FLIP_HORIZONTAL = 2;  // left right reversed mirror
    public static final int ORIENTATION_ROTATE_180 = 3;
    public static final int ORIENTATION_FLIP_VERTICAL = 4;  // upside down mirror
    public static final int ORIENTATION_TRANSPOSE = 5;  // flipped about top-left <--> bottom-right axis
    public static final int ORIENTATION_ROTATE_90 = 6;  // rotate 90 cw to right it
    public static final int ORIENTATION_TRANSVERSE = 7;  // flipped about top-right <--> bottom-left axis
    public static final int ORIENTATION_ROTATE_270 = 8;  // rotate 270 to right it
}

