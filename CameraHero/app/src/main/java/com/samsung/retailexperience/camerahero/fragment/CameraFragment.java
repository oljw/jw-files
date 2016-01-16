package com.samsung.retailexperience.camerahero.fragment;

import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.samsung.retailexperience.camerahero.R;
import com.samsung.retailexperience.camerahero.activity.MainActivity;
import com.samsung.retailexperience.camerahero.util.AppConsts;
import com.samsung.retailexperience.camerahero.view.CameraSurfaceView;

/**
 * Created by icanmobile on 1/14/16.
 */
public class CameraFragment extends BaseCameraFragment
    implements BottomMenuBarFragment.BottomMenuBarListener,
    CameraSurfaceView.CameraSurfaceListener {
    private static final String TAG = CameraFragment.class.getSimpleName();

    public static CameraFragment newInstance(String fragmentModel) {
        Log.d(TAG, "##### CameraFragment newInstance Called");

        CameraFragment fragment = new CameraFragment();

        Bundle args = new Bundle();
        args.putString(AppConsts.ARG_JSON_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    private TopMenuBarFragment mTopMenuBar = null;
    private BottomMenuBarFragment mBottomMenuBar = null;
    private FrameLayout mPreview = null;
    private Camera mCamera = null;
    private CameraSurfaceView mCameraSurface = null;

    @Override
    public void onViewCreated(View view) {
        Log.d(TAG, "##### CameraFragment onViewCreated Called");
//
//        mTopMenuBar = (TopMenuBarFragment) getChildFragmentManager().findFragmentById(R.id.top_fragment);
//        mBottomMenuBar = (BottomMenuBarFragment) getChildFragmentManager().findFragmentById(R.id.bottom_fragment);
//        mBottomMenuBar.setListener(this);

        Log.d(TAG, "GetCameraInstance ##############");
        mCamera = getCameraInstance();
        Log.d(TAG, "GotCameraInstance #########");

        mPreview = (FrameLayout) view.findViewById(R.id.camera_preview);
//        mPreview.setOnTouchListener(mPreveiwTouchListener);

        mCameraSurface = new CameraSurfaceView((MainActivity)getActivity(), mCamera);
        mCameraSurface.setListener(this);
        mPreview.addView(mCameraSurface);

    }

    public static Camera getCameraInstance(){
        Log.d(TAG, "getCameraInstance called");

        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
            Log.d(TAG, "##### Camera Opened");
        }
        catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e(TAG, "Camera is not available or in use or does not exist: " + e.getMessage());
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onBackPressed() {
        changeFragment(AppConsts.UIState.valueOf(getFragmentModel().getActionBackKey()),
                AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
    }

    @Override
    public void onStillClicked() {
        Toast.makeText((MainActivity)getActivity(), "Still Clicked !!!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSwitchClicked() {

    }

    @Override
    public void onVideoClicked() {

    }

    View.OnTouchListener mPreveiwTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    };

    @Override
    public void onDummy() {

    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    public void onResume() {
        Log.d(TAG, "onResume called");

        super.onResume();
//        mCamera = Camera.open(findBackFacingCamera());
//        mPicture = getPictureCallback();
//        mCameraSurface.refreshCamera(mCamera);
    }

//    private void setMenuBar() {
//        if (mTopMenuBar.isHidden()) {
//            getFragmentManager().beginTransaction().setCustomAnimations(
//                    R.animator.left_out, R.animator.left_in
//            ).show( mTopMenuBar ).setCustomAnimations(
//                    R.animator.right_out, R.animator.right_in
//            ).show( mBottomMenuBar ).commit();
//        }
//        else {
//            getFragmentManager().beginTransaction().setCustomAnimations(
//                    R.animator.left_in, R.animator.left_out
//            ).hide( mTopMenuBar ).setCustomAnimations(
//                    R.animator.right_in, R.animator.right_out
//            ).hide( mBottomMenuBar ).commit();
//        }
//    }
}
