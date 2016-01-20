package com.samsung.retailexperience.camerahero.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.samsung.retailexperience.camerahero.fragment.BaseFragment;
import com.samsung.retailexperience.camerahero.fragment.GalleryFragment;

/**
 * Created by icanmobile on 1/14/16.
 */
public class BaseActivity extends Activity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    protected BaseFragment mFragment = null;
    protected GalleryFragment mGFragment = null;
    public int mDisplayW;
    public int mDisplayH;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setWindowFeatures();
        getDisplaySize();
        keepScreenOn();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "##### onStart)+ ");

        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "##### onStop)+ ");

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "##### onDestroy)+ ");

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "##### onResume)+ ");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "##### onPause)+ ");
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "##### onBackPressed)+ ");

        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mFragment != null)
                mFragment.onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void selfFinish() {
        Log.d(TAG, "##### selfFinish)+ ");
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /*
     * Activity Environment
     */
    public void setWindowFeatures() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void keepScreenOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = 1.0f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void getDisplaySize() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        mDisplayW = metrics.widthPixels;
        mDisplayH = metrics.heightPixels;

//        Log.d(TAG, "##### mDisplayW = " + mDisplayW);
//        Log.d(TAG, "##### mDisplayH = " + mDisplayH);
    }
}
