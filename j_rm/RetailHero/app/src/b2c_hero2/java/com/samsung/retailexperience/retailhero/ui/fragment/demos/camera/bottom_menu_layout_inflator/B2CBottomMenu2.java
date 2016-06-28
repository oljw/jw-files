package com.samsung.retailexperience.retailhero.ui.fragment.demos.camera.bottom_menu_layout_inflator;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samsung.retailexperience.retailhero.R;

/**
 * Created by JW on 2/2/2016.
 */
public class B2CBottomMenu2 extends Fragment{
    private static final String TAG = B2CBottomMenu2.class.getSimpleName();

    protected View mView = null;

    public interface BottomMenuListener {

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mView = inflater.inflate( R.layout.hero2b2c_bottom_menu, container, false );

        return mView;
    }

}
