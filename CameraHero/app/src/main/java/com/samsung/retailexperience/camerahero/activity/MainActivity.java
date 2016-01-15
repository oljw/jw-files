package com.samsung.retailexperience.camerahero.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.samsung.retailexperience.camerahero.R;
import com.samsung.retailexperience.camerahero.fragment.CameraFragment;
import com.samsung.retailexperience.camerahero.util.AppConsts;

public class MainActivity extends BaseActivity {

    private Context mContext;
    private AppConsts.UIState mUIState = AppConsts.UIState.UI_STATE_NONE;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeFragment(AppConsts.UIState.UI_STATE_CAMERA, AppConsts.TransactionDir.TRANSACTION_DIR_NONE);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void changeFragment(AppConsts.UIState newState, AppConsts.TransactionDir dir) {
        if (mUIState == newState) return;

        getResources().getString(R.string.app_name);

        switch (newState) {
            case UI_STATE_CAMERA:
                mFragment = CameraFragment.newInstance("models/camera.json");
                break;

            case UI_STATE_GALLERY:
                break;
        }

        if (mFragment != null) {
            switch (dir) {
                case TRANSACTION_DIR_NONE:
                    getFragmentManager().beginTransaction()
                            .add(R.id.fragmentContainer, mFragment)
                            .commit();
                    break;
                case TRANSACTION_DIR_FORWARD:
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.animator.right_in_with_alpha, R.animator.left_out_with_alpha)
                            .replace(R.id.fragmentContainer, mFragment)
                            .commit();
                    break;
                case TRANSACTION_DIR_BACKWARD:
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.animator.left_in_with_alpha, R.animator.right_out_with_alpha)
                            .replace(R.id.fragmentContainer, mFragment)
                            .commit();
                    break;
            }
        }



        mUIState = newState;
    }

}
