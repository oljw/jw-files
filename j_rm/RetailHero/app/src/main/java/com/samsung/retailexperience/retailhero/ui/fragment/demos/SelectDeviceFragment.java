package com.samsung.retailexperience.retailhero.ui.fragment.demos;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.gson.models.DeviceItemModel;
import com.samsung.retailexperience.retailhero.gson.models.DeviceModel;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.view.CustomGridView;

import java.util.ArrayList;

/**
 * Created by icanmobile on 2/2/16.
 */
public class SelectDeviceFragment extends BaseFragment
        implements AdapterView.OnItemClickListener {
    private static final String TAG = SelectDeviceFragment.class.getSimpleName();

    protected View mView = null;
    private FragmentModel<DeviceModel> mFragmentModel = null;
    private CustomGridView mGridView = null;
    private ArrayList<DeviceItemModel> mDevices = null;

    public static SelectDeviceFragment newInstance(FragmentModel<DeviceModel> fragmentModel) {
        SelectDeviceFragment fragment = new SelectDeviceFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    public SelectDeviceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mFragmentModel = (FragmentModel<DeviceModel>) getArguments().getSerializable(AppConsts.ARG_FRAGMENT_MODEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(mFragmentModel.getLayoutResId(), container, false);

        //set background color
        if (mFragmentModel.getBackgroundResId() > 0)
            mView.setBackgroundResource(mFragmentModel.getBackgroundResId());

        //set drawer lock/unlock
        setDrawer(mFragmentModel.getDrawerId());



        if ((FrameLayout) mView.findViewById(R.id.back_button) != null) {
            ((FrameLayout) mView.findViewById(R.id.back_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mFragmentModel != null) {
                        changeFragment(AppConst.UIState.valueOf(mFragmentModel.getActionBackKey()),
                                AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
                    }
                }
            });
        }

        if ((FrameLayout) mView.findViewById(R.id.close_button) != null) {
            ((FrameLayout) mView.findViewById(R.id.close_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mFragmentModel != null) {
                        Log.d(TAG, "##### mFragmentModel.getFragment().getCloseAction() = " + mFragmentModel.getFragment().getCloseAction());
                        changeFragment(AppConst.UIState.valueOf(mFragmentModel.getFragment().getCloseAction()),
                                AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
                    }
                }
            });
        }

        mGridView = (CustomGridView) mView.findViewById(R.id.gridview);
        if (mGridView != null) {
            if (mFragmentModel.getFragment().getDeviceItems().size() > 0) {
                mDevices = mFragmentModel.getFragment().getDeviceItems();
                if (mDevices != null) {
                    mGridView.setAdapter(new selectDeviceAdapter());
                    mGridView.setOnItemClickListener(this);
                }
            }
        }

        return mView;
    }

    @Override
    public void onBackPressed() {
        if (mFragmentModel != null && mFragmentModel.getActionBackKey() != null) {
            changeFragment(AppConst.UIState.valueOf(mFragmentModel.getActionBackKey()),
                    AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedDevice((String)view.getTag());
        if (mFragmentModel != null)
            changeFragment(AppConst.UIState.valueOf(mFragmentModel.getFragment().getAction()),
                    AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
    }


    private class selectDeviceAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDevices.size();
        }

        @Override
        public Object getItem(int position) {
            return mDevices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Context context = getActivity().getApplicationContext();
            DeviceItemModel item = mDevices.get(position);

            View view = LayoutInflater.from(context).inflate(item.getLayoutResId(), mGridView, false);

//            //design debugging
//            switch (position) {
//                case 0:
//                    menuItemView.setBackgroundColor(0x77FF0000);
//                    break;
//
//                case 1:
//                    menuItemView.setBackgroundColor(0x7700FF00);
//                    break;
//
//                case 2:
//                    menuItemView.setBackgroundColor(0x770000FF);
//                    break;
//
//                case 3:
//                    menuItemView.setBackgroundColor(0x77777777);
//                    break;
//            }


            //device image
            if ((ImageView) view.findViewById(R.id.device_img) != null) {
                if (item.getDeviceImageResId() > 0)
                    ((ImageView) view.findViewById(R.id.device_img)).setImageResource(item.getDeviceImageResId());
            }

            //device select
            if ((ImageView) view.findViewById(R.id.device_select) != null && item.isSelected()) {
                ((ImageView) view.findViewById(R.id.device_select)).setVisibility(View.VISIBLE);
            }

            //device name
            if ((ImageView) view.findViewById(R.id.device_name) != null && item.getDeviceNameResId() > 0) {
                if (item.getDeviceNameResId() > 0)
                    ((ImageView) view.findViewById(R.id.device_name)).setImageResource(item.getDeviceNameResId());
            }

            if (item.getTagResId() > 0)
                view.setTag(getString(item.getTagResId()));

            return view;
        }
    };
}
