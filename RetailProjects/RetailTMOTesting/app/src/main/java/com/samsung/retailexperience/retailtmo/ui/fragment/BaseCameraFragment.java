package com.samsung.retailexperience.retailtmo.ui.fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.samsung.retailexperience.retailtmo.R;
import com.samsung.retailexperience.retailtmo.gson.models.ArgumentsModel;
import com.samsung.retailexperience.retailtmo.gson.models.FragmentModel;
import com.samsung.retailexperience.retailtmo.gson.models.ListModel;
import com.samsung.retailexperience.retailtmo.gson.models.MenuModel;
import com.samsung.retailexperience.retailtmo.ui.activity.MainActivity;
import com.samsung.retailexperience.retailtmo.ui.view.CameraPreviewLayout;
import com.samsung.retailexperience.retailtmo.util.AppConst;
import com.samsung.retailexperience.retailtmo.util.JsonUtil;

/**
 * Created by JW on 2016-05-24.
 */
public abstract class BaseCameraFragment extends BaseFragment {
    public static final String TAG = BaseCameraFragment.class.getSimpleName();

    protected static final String FRONT_FACING_CAMERA_ID = "1";
    protected static final String BACK_FACING_CAMERA_ID = "0";

    protected static final boolean MANUAL_FOCUS_ENABLED = true;
    protected static final boolean MANUAL_FOCUS_DISABLED = false;

    protected CameraPreviewLayout mCameraPreviewLayout = new CameraPreviewLayout(MainActivity.myActivity,
            CameraPreviewLayout.mCameraIdNum, CameraPreviewLayout.mEnableManualFocus);

    private FragmentModel<MenuModel> mFragmentModel;
    private AppConst.TransactionDir mTransactionDir;

    private Button mCaptureBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ArgumentsModel args = (ArgumentsModel) getArguments().getSerializable(AppConst.ARGUMENTS_MODEL);
            mFragmentModel = JsonUtil.loadJsonModel(getActivity().getApplicationContext(),
                    args.getFragmentJson(), new TypeToken<FragmentModel<ListModel>>() {}.getType());
            mTransactionDir = (AppConst.TransactionDir) AppConst.TransactionDir.valueOf(args.getTransitionDir());
            onFragmentCreated(args);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(getFragmentModel().getLayoutResId(), container, false); //layout_camera_fragment used

        // set background
        if (getFragmentModel().getBackgroundResId() > 0)
            mView.setBackgroundResource(mFragmentModel.getBackgroundResId());

        // set view pivot X,Y
        if (getFragmentModel().getPivotXValue() != 0)
            mView.setPivotX(getResources().getInteger(getFragmentModel().getPivotXValue()));
        if (getFragmentModel().getPivotYValue() != 0)
            mView.setPivotY(getResources().getInteger(getFragmentModel().getPivotYValue()));

        mCameraPreviewLayout = (CameraPreviewLayout) mView.findViewById(R.id.camera_preview);

        mCaptureBtn = (Button) mView.findViewById(R.id.capture_btn);
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "##### mCaptureBtn Clicked");
                mCameraPreviewLayout.takePicture();
                CameraPreviewLayout.mOutputImage.setVisibility(View.VISIBLE);
            }
        });
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onPause() {
        if (mCameraPreviewLayout != null) {
            mCameraPreviewLayout.closeCamera();
            mCameraPreviewLayout.stopBackgroundThread();

            if (mCameraPreviewLayout.mOutputImage != null &&
                    mCameraPreviewLayout.mOutputImage.getDrawable() != null) {
                BitmapDrawable b = (BitmapDrawable) mCameraPreviewLayout.mOutputImage.getDrawable();
                if (b != null && b.getBitmap() != null) {
                    b.getBitmap().recycle();
                    Log.d(TAG, "##### mOutputImage bitmap recycled");
                }
                mCameraPreviewLayout.mOutputImage.setImageBitmap(null);
            }
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mCameraPreviewLayout != null) {
            mCameraPreviewLayout.startBackgroundThread();
            mCameraPreviewLayout.openCamera(CameraPreviewLayout.mCameraIdNum);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy()  {
        super.onDestroy();
    }

    abstract public void onFragmentCreated(ArgumentsModel args);
    abstract public void onViewCreated(View view);

    protected FragmentModel<MenuModel> getFragmentModel() {
        return mFragmentModel;
    }
    protected AppConst.TransactionDir getTransactionDir() {
        return this.mTransactionDir;
    }

    abstract public void onPageTransitionStart(boolean enter, int nextAnim, AppConst.TransactionDir dir);
    abstract public void onPageTransitionEnd(boolean enter, int nextAnim, AppConst.TransactionDir dir);
    abstract public void onPageTransitionCancel(boolean enter, int nextAnim, AppConst.TransactionDir dir);

    /**
     * Camera Demo Controllers.
     */

    protected void initializeCameraDemo(String cameraId, boolean enableManualFocus) {
        this.mCameraPreviewLayout.mCameraIdNum = cameraId;
        this.mCameraPreviewLayout.mEnableManualFocus = enableManualFocus;
    }

    protected void turnCameraOn() {
        if (mCameraPreviewLayout != null) {
            mCameraPreviewLayout.startBackgroundThread();
            mCameraPreviewLayout.openCamera(CameraPreviewLayout.mCameraIdNum);
        }
    }

    protected void turnCameraOff() {
        if (mCameraPreviewLayout != null) {
            mCameraPreviewLayout.closeCamera();
            mCameraPreviewLayout.stopBackgroundThread();

            if (mCameraPreviewLayout.mOutputImage != null &&
                    mCameraPreviewLayout.mOutputImage.getDrawable() != null) {
                BitmapDrawable b = (BitmapDrawable) mCameraPreviewLayout.mOutputImage.getDrawable();
                if (b != null && b.getBitmap() != null) {
                    b.getBitmap().recycle();
                    Log.d(TAG, "##### mOutputImage bitmap recycled");
                }
                mCameraPreviewLayout.mOutputImage.setImageBitmap(null);
            }
        }
    }

}
