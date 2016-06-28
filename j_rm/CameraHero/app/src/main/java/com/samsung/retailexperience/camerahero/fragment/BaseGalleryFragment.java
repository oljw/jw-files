package com.samsung.retailexperience.camerahero.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samsung.retailexperience.camerahero.R;
import com.samsung.retailexperience.camerahero.gson.models.CameraModel;
import com.samsung.retailexperience.camerahero.gson.models.FragmentModel;
import com.samsung.retailexperience.camerahero.util.AppConsts;

import java.lang.reflect.Type;

/**
 * Created by Jaewoo on 1/19/16.
 */
public abstract class BaseGalleryFragment extends BaseFragment {
    private static final String TAG = BaseCameraFragment.class.getSimpleName();

    protected View gView = null;
    protected String mJsonModel = null;
    protected FragmentModel<CameraModel> mFragmentModel = null;

    public BaseGalleryFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "##### BaseGallery onCreate Called");

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mJsonModel = getArguments().getString(AppConsts.ARG_JSON_MODEL);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(TAG, "##### BaseGallery onCreateView Called");

        mFragmentModel = loadFragmentModel();
        gView = inflater.inflate(R.layout.fragment_gallery, container, false);

        onViewCreated(gView);
        return gView;
    }

    abstract public void onViewCreated(View view);

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy()  {
        super.onDestroy();
    }

    protected FragmentModel<CameraModel> loadFragmentModel() {
        Context context = getActivity().getApplicationContext();
        String data = GetTextFromAsset(context, mJsonModel);

        Gson gson = new Gson();
        Type fragmentType = new TypeToken<FragmentModel<CameraModel>>() {}.getType();
        return gson.fromJson(data, fragmentType);
    }

    protected FragmentModel<CameraModel> getFragmentModel() {
        return mFragmentModel;
    }
}