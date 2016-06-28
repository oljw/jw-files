package com.samsung.retailexperience.retailhero.ui.fragment.camera_app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.samsung.retailexperience.retailhero.R;


/**
 * Created by JW on 1/14/16.
 */
public class BottomMenuBarFragment extends Fragment {
    private static final String TAG = BottomMenuBarFragment.class.getSimpleName();

    protected View mView = null;

    public interface BottomMenuBarListener {

    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState)
    {
        mView = inflater.inflate( R.layout.camera_bottom_menubar, container, false );

        return mView;
    }

}
