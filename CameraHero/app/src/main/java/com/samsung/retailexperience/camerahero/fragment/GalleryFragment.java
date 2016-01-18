package com.samsung.retailexperience.camerahero.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.samsung.retailexperience.camerahero.R;

/**
 * Created by Jaewoo on 2016-01-18.
 */
public class GalleryFragment extends BaseFragment{
    private static final String TAG = "GalleryFragment";

    protected View mView = null;
    private Button mGalleryView = null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState){
        mView = inflater.inflate( R.layout.fragment_gallery, container, false );

        mGalleryView = (Button) mView.findViewById(R.id.gallery_view);

        return mView;

//        return inflater.inflate(R.layout.fragment_gallery, container, false);

    }


    @Override
    public void onBackPressed() {

    }
}
