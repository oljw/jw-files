package com.samsung.retailexperience.retailtmo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samsung.retailexperience.retailtmo.analytics.FragmentChangeCause;
import com.samsung.retailexperience.retailtmo.animator.PageTransformer;
import com.samsung.retailexperience.retailtmo.gson.models.ArgumentsModel;
import com.samsung.retailexperience.retailtmo.util.AppConst;

/**
 * Created by JW on 2016-06-02.
 */
public class CameraFragment extends BaseCameraFragment {
    private static final String TAG = CameraFragment.class.getSimpleName();

    boolean mTransitionEnter = false;
    private String mCameraIdNum = FRONT_FACING_CAMERA_ID;


    @Override
    public void onFragmentCreated(ArgumentsModel args) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(View view) {

    }

    @Override
    public void onPageTransitionStart(boolean enter, int nextAnim, AppConst.TransactionDir dir) {
        mTransitionEnter = enter;
        getView().addOnLayoutChangeListener(layoutChangeListener);
    }

    @Override
    public void onPageTransitionEnd(boolean enter, int nextAnim, AppConst.TransactionDir dir) {

    }

    private View.OnLayoutChangeListener layoutChangeListener = new View.OnLayoutChangeListener(){
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                   int oldRight, int oldBottom) {
            v.removeOnLayoutChangeListener(this);
            PageTransformer.getInstance().run(getAppContext(), getView(), mTransitionEnter, getTransactionDir());
        }
    };

    @Override
    public void onPageTransitionCancel(boolean enter, int nextAnim, AppConst.TransactionDir dir) {

    }

    @Override
    public void onBackPressed() {
        if (getFragmentModel() == null || getFragmentModel().getActionBackKey() == null) return;
        changeFragment(getFragmentModel().getActionBackKey(),
                AppConst.TransactionDir.TRANSACTION_DIR_BACKWARD, FragmentChangeCause.BACK_PRESSED);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mCameraPreviewLayout != null) {
            mCameraPreviewLayout.startBackgroundThread();
            mCameraPreviewLayout.openCamera(mCameraIdNum);
        }
    }
}
