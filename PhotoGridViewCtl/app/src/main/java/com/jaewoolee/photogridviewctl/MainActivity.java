package com.jaewoolee.photogridviewctl;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.jaewoolee.photogridviewctl.photogridview.photoGridView;
import com.jaewoolee.photogridviewctl.utils.displayInfoEx;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getName();

    private int mDisplayW = 0;
    private int mDisplayH = 0;
    private photoGridView mPhotoGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate executed");

        super.onCreate(savedInstanceState);

        getDisplaySize();
        keepScreenOn();
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart executed");
        super.onStart();
        //add codes
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop executed");

        super.onStop();
        //add codes
    }

    @Override
    protected void onDestroy() {
        //add codes
        Log.d(TAG, "onDestroy executed");

        //destroy activity
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume executed");

        super.onResume();
    }

    protected void onPause() {
        Log.d(TAG, "onPause executed");

        super.onPause();
        //add codes
    }

    public void onBackPressed() {
        //add codes
        Log.d(TAG, "onBack executed");


        super.onBackPressed();
    }

    private void getDisplaySize() {
        Log.d(TAG, "getDisplaySize executed");

        displayInfoEx displayInfo = displayInfoEx.getDisplaySize(this);
        mDisplayW = displayInfo._display_width;
        mDisplayH = displayInfo._display_height;
    }

    private void keepScreenOn() {
        Log.d(TAG, "KeepScreenOn executed");

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = 1.0f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void init() {
        Log.d(TAG, "init() executed");

        mPhotoGridView = (photoGridView)findViewById(R.id.photo_grid_view);
        mPhotoGridView.init("/", mDisplayW);
    }

    public static int getMaxSelectedPhotos() {
        Log.d(TAG, "getMaxSelectedphotos executed");

        return 10;
    }

    public static void refreshSelectedPhotoCount(int count) {
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
