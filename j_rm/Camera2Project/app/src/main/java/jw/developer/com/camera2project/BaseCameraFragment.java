package jw.developer.com.camera2project;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jw.developer.com.camera2project.util.AppConst;

/**
 * Created by JW on 2016-05-24.
 */
public abstract class BaseCameraFragment extends BaseFragment {

    private CameraPreviewLayout mCameraPreviewLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_camera_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCameraPreviewLayout = (CameraPreviewLayout) view.findViewById(R.id.camera_preview);
        mCameraPreviewLayout.startBackgroundThread();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCameraPreviewLayout != null)
            mCameraPreviewLayout.closeCamera();
            mCameraPreviewLayout.stopBackgroundThread();

//        if (mOrientationListener != null) {
//            mOrientationListener.disable();
//            }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
