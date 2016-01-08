package com.jaewoolee.cameradummy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class CameraActivity extends Activity {

    private static Camera mCamera;
    private static CameraPreview mPreview;

    static final int MEDIA_TYPE_IMAGE = 1;

    static Context context;

    /** setting last image taken **/
    static ImageView myImage;
    static File imgFile = null;
    int duration = Toast.LENGTH_LONG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_preview);

        mCamera = getCameraInstance();

        context = getApplicationContext();
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        myImage = (ImageView) findViewById(R.id.lastPic);
        /** if last image is clicked it will open in an image viewer app **/
        myImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                if(imgFile == null){
                    CharSequence text = "This is only an icon, take a photo";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else{
                    intent.setDataAndType(Uri.parse("file://" + imgFile.getAbsolutePath()), "image/*");
                    startActivity(intent);
                }
                return false;
            }
        });

        /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public void takePhoto(View v){
        mCamera.takePicture(null, null, mPicture);
    }

    private PictureCallback mPicture = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

            if (pictureFile == null){
                Log.d("ERROR", "Error creating media file, check storage permissions:" );
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

            } catch (FileNotFoundException e) {
                Log.d("ERROR", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("ERROR", "Error accessing file: " + e.getMessage());
            }
            setImage();

            mCamera.startPreview();
        }
    };

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){

        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if(!isSDPresent)
        {
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, "card not mounted", duration);
            toast.show();

            Log.d("ERROR", "Card not mounted");
        }
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath() + "/cameraSpeed/");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){

                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
            imgFile = mediaFile;
        } else {
            return null;
        }

        return mediaFile;
    }

    public static void setImage(){
        if(imgFile !=null){
            if(imgFile.exists()){
                Bitmap myBitmap = decodeSampleImage(imgFile, 100, 100); // prevents memory out of memory exception
                myImage.setImageBitmap(myBitmap);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera == null){
            mCamera = getCameraInstance();

            context = getApplicationContext();
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.release();
            mCamera = null;

        }
    }

    public static Bitmap decodeSampleImage(File f, int width, int height) {
        try {
            System.gc(); // First of all free some memory 	        // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o); 	        // The new size we want to scale to 	                            final int requiredWidth = width;
            final int requiredHeight = height; 	        // Find the scale value (as a power of 2)
            int sampleScaleSize = 1;
            while (o.outWidth / sampleScaleSize / 2 >= requiredWidth && o.outHeight / sampleScaleSize / 2 >= requiredHeight)
                sampleScaleSize *= 2;

            // Decode with inSampleSize

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = sampleScaleSize;

            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (Exception e) {
            //  Log.d(TAG, e.getMessage()); // We don't want the application to just throw an exception
        }

        return null;
    }
}
