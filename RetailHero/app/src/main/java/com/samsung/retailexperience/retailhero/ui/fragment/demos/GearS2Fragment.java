package com.samsung.retailexperience.retailhero.ui.fragment.demos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.gson.models.BaseModel;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;

/**
 * Created by smheo on 1/15/2016.
 */
public class GearS2Fragment extends BaseFragment {

    private static final String TAG = GearS2Fragment.class.getSimpleName();

    protected View mView = null;
    private FragmentModel<BaseModel> mFragmentModel = null;

    public static GearS2Fragment newInstance(FragmentModel<BaseModel> fragmentModel) {
        GearS2Fragment fragment = new GearS2Fragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
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
        mView = inflater.inflate(mFragmentModel.getLayoutResId(), container, false);

        if (mView != null) {
            mView.findViewById(R.id.skip_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // end page
                    changeEndDemoFragment(AppConst.UIState.valueOf(mFragmentModel.getActionBackKey()),
                            AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
                }
            });
        }

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        if (mFragmentModel != null) {
            changeFragment(AppConst.UIState.valueOf(mFragmentModel.getActionBackKey()),
                    AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
        }
    }

}
