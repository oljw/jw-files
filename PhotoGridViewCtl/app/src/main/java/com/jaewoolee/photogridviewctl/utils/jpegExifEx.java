package com.jaewoolee.photogridviewctl.utils;

import android.media.ExifInterface;

import java.io.IOException;

public class jpegExifEx {
	private final static String TAG="JPEGExifUtils";
	public int _width;
	public int _height;
	public int _orientation;
	public String _date_time;
	public String _exif_desc;
	public String _make;
	public String _model;
	public int _white_balance;
	public String _apeture;
	

	public static jpegExifEx getJPEGExif(String file) {
		jpegExifEx exif = new jpegExifEx();
		try {
			   ExifInterface exifInterface = new ExifInterface(file);
			   
			   String strWidth = "";
			   String strHeight = "";
			   String strOrientation = "";
			   String strDateTime = "";
			   String strWhiteBalance = "";
			   String strMake = "";
			   String strModel = "";
			   String strAperture = "";
			   
			   int width = 0;
			   int height = 0;
			   int orientation = 0;
			   String date_time = "";
			   int white_balance = 0;
			   
			   strWidth = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
			   strHeight = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
			   strOrientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
			   strDateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
			   
			   strMake = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
			   strModel = exifInterface.getAttribute(ExifInterface.TAG_MODEL);
			   strWhiteBalance = exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
			   strAperture = exifInterface.getAttribute(ExifInterface.TAG_APERTURE);
			   
			   if( strWidth != null && strWidth.length() > 0 )
			   {
				   width = Integer.parseInt(strWidth);
				   exif.setWidth(width);
			   }
			   
			   if( strHeight != null && strHeight.length() > 0 )
			   {
				   height = Integer.parseInt(strHeight);
				   exif.setHeight(height);
			   }
			   
			   if( strOrientation != null && strOrientation.length() > 0 )
			   {
				   orientation = Integer.parseInt(strOrientation);
				   exif.setOrientation(orientation);
			   }
			   
			   if( strDateTime != null && strDateTime.length() > 0 )
			   {
				   date_time = strDateTime;
				   exif.setDateTime(date_time);
			   }
			   
			   exif.setMake(strMake);
			   exif.setModel(strModel);
			   exif.setApeture(strAperture);
			   if( strWhiteBalance != null && strWhiteBalance.length() > 0 )
			   {
				   white_balance = Integer.parseInt(strWhiteBalance);
				   exif.setWhiteBalance(white_balance);
			   }
			   
//			   exif += "\nIMAGE_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
//			   exif += "\nIMAGE_WIDTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
//			   exif += "\n DATETIME: " + exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
//			   exif += "\n TAG_MAKE: " + exifInterface.getAttribute(ExifInterface.TAG_MAKE);
//			   exif += "\n TAG_MODEL: " + exifInterface.getAttribute(ExifInterface.TAG_MODEL);
//			   exif += "\n TAG_ORIENTATION: " + exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
//			   exif += "\n TAG_WHITE_BALANCE: " + exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
//			   exif += "\n TAG_FOCAL_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
//			   exif += "\n TAG_FLASH: " + exifInterface.getAttribute(ExifInterface.TAG_FLASH);
//			   exif += "\nGPS related:";
//			   
//			   float[] LatLong = new float[2];
//			   if(exifInterface.getLatLong(LatLong)){
//			    exif += "\n latitude= " + LatLong[0];
//			    exif += "\n longitude= " + LatLong[1];
//			   }
//			   else{
//			    exif += "Exif tags are not available!";
//			   }
			   
			   String exifDesc = "";
			   exifDesc += "\nIMAGE_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
			   exifDesc += "\nIMAGE_WIDTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
			   exifDesc += "\n DATETIME: " + exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
			   exifDesc += "\n TAG_MAKE: " + exifInterface.getAttribute(ExifInterface.TAG_MAKE);
			   exifDesc += "\n TAG_MODEL: " + exifInterface.getAttribute(ExifInterface.TAG_MODEL);
			   exifDesc += "\n TAG_ORIENTATION: " + exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
			   exifDesc += "\n TAG_WHITE_BALANCE: " + exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
			   exifDesc += "\n TAG_FOCAL_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
			   exifDesc += "\n TAG_FLASH: " + exifInterface.getAttribute(ExifInterface.TAG_FLASH);
			   exifDesc += "\n TAG_APERTURE: " + exifInterface.getAttribute(ExifInterface.TAG_APERTURE);
			   exifDesc += "\n TAG_EXPOSURE_TIME: " + exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
			   exifDesc += "\nGPS related:";
			   
			   float[] LatLong = new float[2];
			   if(exifInterface.getLatLong(LatLong)){
				   exifDesc += "\n latitude= " + LatLong[0];
				   exifDesc += "\n longitude= " + LatLong[1];
			   }
			   else{
				   exifDesc += "Exif tags are not available!";
			   }
			   
			   exif.setExifDescription( exifDesc );
			   
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		
		return exif;
	}
	
	public int getWidth() {
		return _width;
	}
	
	public void setWidth(int width) {
		_width = width;
	}
	
	public int getHeight() {
		return _height;
	}
	
	public void setHeight(int height) {
		_height = height;
	}
	
	public int getOrientation() {
		return _orientation;
	}
	
	public void setOrientation(int orientation) {
		_orientation = orientation;
	}
	
	public String getDateTime() {
		return _date_time;
	}
	
	public void setDateTime(String date_time) {
		_date_time = date_time;
	}
	
	public String getMake() {
		return _make;
	}
	
	public void setMake(String make) {
		_make = make;
	}
	
	public String getModel() {
		return _model;
	}
	
	public void setModel(String model) {
		_model = model;
	}
	
	public int getWhiteBalance() {
		return _white_balance;
	}
	
	public void setWhiteBalance(int white_balance) {
		_white_balance = white_balance;
	}
	
	public String getApeture() {
		return _apeture;
	}
	
	public void setApeture(String apeture) {
		_apeture = apeture;
	}
	
	public String getExifDescription() {
		return _exif_desc;
	}
	
	public void setExifDescription(String exif_desc ) {
		_exif_desc = exif_desc;
	}
	
	
}


