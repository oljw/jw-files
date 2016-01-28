package com.samsung.retailexperience.retailhero.ui.fragment.demos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samsung.retailexperience.retailhero.gson.models.BaseModel;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;

/**
 * Created by smheo on 1/15/2016.
 */
public class DeviceSpecsFragment extends BaseFragment {

    private static final String TAG = DeviceSpecsFragment.class.getSimpleName();

    protected View mView = null;
    private FragmentModel<BaseModel> mFragmentModel = null;

    public static DeviceSpecsFragment newInstance(FragmentModel<BaseModel> fragmentModel) {
        DeviceSpecsFragment fragment = new DeviceSpecsFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    public DeviceSpecsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mFragmentModel = (FragmentModel<BaseModel>) getArguments().getSerializable(AppConsts.ARG_FRAGMENT_MODEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "##### DeviceSpecsFragment : onCreateView : " + mFragmentModel.getLayout());

        mView = inflater.inflate(mFragmentModel.getLayoutResId(), container, false);

        //set background color
        if (mFragmentModel.getBackgroundResId() > 0)
            mView.setBackgroundResource(mFragmentModel.getBackgroundResId());

        //set drawer lock/unlock
        setDrawer(mFragmentModel.getDrawerId());

        return mView;
    }

    @Override
    public void onBackPressed() {
        if (mFragmentModel != null) {
            changeFragment(AppConst.UIState.valueOf(mFragmentModel.getActionBackKey()),
                    AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
        }
    }
}