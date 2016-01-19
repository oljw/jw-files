package com.samsung.retailexperience.camerahero.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;

import com.samsung.retailexperience.camerahero.R;

/**
 * Created by Jaewoo on 2016-01-18.
 */
public class GalleryFragment extends Fragment{
    private static final String TAG = GalleryFragment.class.getSimpleName();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    )
    {
        View rootView = inflater.inflate( R.layout.fragment_gallery, container, false );
        return rootView;
    }

}
