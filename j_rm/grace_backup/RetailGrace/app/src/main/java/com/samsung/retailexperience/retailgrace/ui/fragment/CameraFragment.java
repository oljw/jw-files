package com.samsung.retailexperience.retailgrace.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.samsung.retailexperience.retailgrace.R;
import com.tecace.app.manager.analytics.FragmentChangeCause;
import com.tecace.app.manager.ext.ui.view.compareview.CompareOverlayView;
import com.tecace.app.manager.gson.model.ArgumentsModel;
import com.tecace.app.manager.ui.fragment.BaseCameraFragment;
import com.tecace.app.manager.util.AppManagerConst;
import com.tecace.retail.appmanager.animator.PageTransformer;
import com.tecace.retail.videomanager.annotation.OnChapter;
import com.tecace.retail.videomanager.annotation.OnChapterEnded;
import com.tecace.retail.videomanager.gson.model.Chapter;


/**
 * Created by JW on 2016-06-02.
 */
public class CameraFragment extends BaseCameraFragment {
    private static final String TAG = CameraFragment.class.getSimpleName();

    boolean mTransitionEnter = false;

    /*** GRACE ***/
    private CompareOverlayView mCompareOverlayView;

    @Override
    public void onFragmentCreated(ArgumentsModel args) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //NORMAL
        initializeDemo(BACK_FACING_CAMERA_ID, MANUAL_FOCUS_ENABLED, IRIS_DISABLED, NORMAL_SCREEN_SIZE);

        //IRIS
//        initializeDemo(FRONT_FACING_CAMERA_ID, MANUAL_FOCUS_DISABLED, IRIS_ENABLED, IRIS_UI_SIZE);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view) {
        mCompareOverlayView = (CompareOverlayView) view.findViewById(R.id.compare_view);
    }

    @Override
    public void onPlayVideo() {
        if (isLandscape() == false) return;
        playVideo(mVideoView);
    }

    @Override
    public void onPageTransitionStart(boolean enter, int nextAnim, AppManagerConst.TransactionDir dir) {
        mTransitionEnter = enter;
        getView().addOnLayoutChangeListener(layoutChangeListener);
    }

    @Override
    public void onPageTransitionEnd(boolean enter, int nextAnim, AppManagerConst.TransactionDir dir) {

    }

    private View.OnLayoutChangeListener layoutChangeListener = new View.OnLayoutChangeListener(){
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                   int oldRight, int oldBottom) {
            v.removeOnLayoutChangeListener(this);
            PageTransformer.getInstance().run(getAppContext(), getView(), mTransitionEnter, getTransactionDirBoolean(), mNoAnimChildViewIDs);
        }
    };

    @Override
    public void onPageTransitionCancel(boolean enter, int nextAnim, AppManagerConst.TransactionDir dir) {

    }

    @Override
    public void onBackPressed() {
        if (getFragmentModel() == null || getFragmentModel().getActionBackKey() == null) return;
        changeFragment(getFragmentModel().getActionBackKey(),
                AppManagerConst.TransactionDir.TRANSACTION_DIR_BACKWARD, FragmentChangeCause.BACK_PRESSED);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @OnChapter(chapterIndex = 2)
    public void onChaper_2(Chapter action) {
        Log.i(TAG, "Chapter 2");
        mCurrentIndex = 2;
        mCompareOverlayView.setVisibility(View.VISIBLE);
    }

    @OnChapterEnded(chapterIndex = 2)
    public void onChaperEnded_2(Chapter actionInfo) {
        Log.i(TAG, "Chapter Ended 2");
        mCompareOverlayView.setVisibility(View.GONE);
    }

    @OnChapter(chapterIndex = 4)
    public void onChaper_4(Chapter action) {
        Log.i(TAG, "Chapter 4");
        mCurrentIndex = 4;
        turnCameraOn();
    }

    @OnChapterEnded(chapterIndex = 4)
    public void onChaperEnded_4(Chapter actionInfo) {
        Log.i(TAG, "Chapter Ended 4");

    }

    @OnChapter(chapterIndex = 5)
    public void onChaper_5(Chapter action) {
        Log.i(TAG, "Chapter 5");
        mCurrentIndex = 5;
        if (!mCaptureBtn.isClickable()) {
            displayCapturedImage();
        }
    }

    @OnChapterEnded(chapterIndex = 5)
    public void onChaperEnded_5(Chapter actionInfo) {
        Log.i(TAG, "Chapter Ended 5");
        turnCameraOff();
        goBackToVideo();
    }
}
