package jw.developer.com.camera2project;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by JW on 2016-05-24.
 */
public class CameraFragment extends Fragment {

//    private CameraView mCameraView;
    private CameraPreviewLayout mCameraPreviewLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_camera_test, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mCameraView = (CameraView) view.findViewById(R.id.camera_view_testing);
        mCameraPreviewLayout = (CameraPreviewLayout) view.findViewById(R.id.camera_view_testing);
        mCameraPreviewLayout.startBackgroundThread();

    }

    public static CameraFragment newInstance() {
        return new CameraFragment();
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
