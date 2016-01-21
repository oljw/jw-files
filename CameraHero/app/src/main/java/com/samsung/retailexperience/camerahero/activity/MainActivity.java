package com.samsung.retailexperience.camerahero.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.samsung.retailexperience.camerahero.R;
import com.samsung.retailexperience.camerahero.fragment.CameraFragment;
import com.samsung.retailexperience.camerahero.fragment.GalleryFragment;
import com.samsung.retailexperience.camerahero.util.AppConsts;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;
    private Camera mCamera;
    private AppConsts.UIState mUIState = AppConsts.UIState.UI_STATE_NONE;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "##### MainActivity onCreate called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeFragment(AppConsts.UIState.UI_STATE_CAMERA, AppConsts.TransactionDir.TRANSACTION_DIR_NONE);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "##### MainActivity onDestroy called");

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "##### MainActivity onBackPressed called");

        super.onBackPressed();
    }

    public void changeFragment(AppConsts.UIState newState, AppConsts.TransactionDir dir) {
        Log.d(TAG, "##### MainActivity changeFragment called");

        if (mUIState == newState) return;

        getResources().getString(R.string.app_name);

        switch (newState) {
            case UI_STATE_CAMERA:
                mFragment = CameraFragment.newInstance("models/camera.json");
                break;

            case UI_STATE_GALLERY:
                mGFragment = GalleryFragment.newInstance("models/gallery.json");
                break;
        }

        if (mFragment != null) {
            switch (dir) {
                case TRANSACTION_DIR_NONE:
                    getFragmentManager().beginTransaction()
                            .add(R.id.fragmentContainer, mFragment)
                            .addToBackStack(null)
                            .commit();
                    break;
                case TRANSACTION_DIR_FORWARD:
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.animator.right_in_with_alpha, R.animator.left_out_with_alpha)
                            .replace(R.id.fragmentContainer, mGFragment)
                            .addToBackStack(null)
                            .commit();
                    break;
                case TRANSACTION_DIR_BACKWARD:
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.animator.left_in_with_alpha, R.animator.right_out_with_alpha)
                            .replace(R.id.fragmentContainer, mFragment)
                            .addToBackStack(null)
                            .commit();
                    break;
            }
        }
        mUIState = newState;
    }

}
