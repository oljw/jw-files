package com.samsung.retailexperience.retailhero.ui.fragment.camera_app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samsung.retailexperience.retailhero.R;


/**
 * Created by icanmobile on 1/14/16.
 */
public class BottomGalleryBarFragment extends Fragment {
    private static final String TAG = TopMenuBarFragment.class.getSimpleName();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    )
    {
        View rootView = inflater.inflate( R.layout.gallery_bottom_menubar, container, false );
        return rootView;
    }
}
