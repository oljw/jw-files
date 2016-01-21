package com.samsung.retailexperience.retailhero.ui.fragment.demos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;

/**
 * Created by smheo on 1/15/2016.
 */
public class CompareDeviceFragment extends BaseFragment {

    private static final String TAG = CompareDeviceFragment.class.getSimpleName();

    public CompareDeviceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compare_device, container, false);
        return view;
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

    @Override
    public void onSetDrawer() {
        setDrawer(0);
    }

    @Override
    public void onBackPressed() {
        changeFragment(AppConst.UIState.UI_STATE_DECISION,
                AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
    }
}
