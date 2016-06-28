package com.samsung.retailexperience.retailhero.ui.fragment.demos;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.gson.models.DeviceSpecItemModel;
import com.samsung.retailexperience.retailhero.gson.models.DeviceSpecModel;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;

import java.util.ArrayList;

/**
 * Created by smheo on 1/15/2016.
 */
public class DeviceSpecsFragment extends BaseFragment {

    private static final String TAG = DeviceSpecsFragment.class.getSimpleName();

    protected View mView = null;
    private FragmentModel<DeviceSpecModel> mFragmentModel = null;
    private LinearLayout mCompareDevice = null;
    private ListView mListView = null;
    protected ArrayList<DeviceSpecItemModel> mDeviceSpecItems = null;
    protected ArrayList<DeviceSpecItemModel> mComparedDeviceSpecItem = null;
    protected LinearLayout mColorLayout = null;
    protected LinearLayout mCompareColorLayout = null;

    public static DeviceSpecsFragment newInstance(FragmentModel<DeviceSpecModel> fragmentModel) {
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
            mFragmentModel = (FragmentModel<DeviceSpecModel>) getArguments().getSerializable(AppConsts.ARG_FRAGMENT_MODEL);
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

        mCompareDevice = (LinearLayout) mView.findViewById(R.id.compare_device_layout);
        if (mCompareDevice != null) {
            mCompareDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mFragmentModel != null) {
                        changeFragment(AppConst.UIState.valueOf(mFragmentModel.getFragment().getAction()),
                                AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
                    }
                }
            });
        }


        if ((FrameLayout) mView.findViewById(R.id.close_button) != null) {
            ((FrameLayout) mView.findViewById(R.id.close_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mFragmentModel != null) {
                        changeFragment(AppConst.UIState.valueOf(mFragmentModel.getFragment().getCloseAction()),
                                AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
                    }
                }
            });
        }


        mListView = (ListView) mView.findViewById(R.id.list);
        if (mListView != null) {
            if (mFragmentModel.getFragment().getDeviceSpecItems().size() > 0) {
                mDeviceSpecItems = mFragmentModel.getFragment().getDeviceSpecItems();
                if (mDeviceSpecItems != null)
                    mListView.setAdapter(new deviceSpecAdapter());
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




    static class ViewHolderSpecItem {
        TextView specItem;
        TextView spec;
        TextView comparedSpec;
    }

    static class ViewHolderColors {
        TextView specItem;
        LinearLayout colorsLayout;
        LinearLayout comparedColorsLayout;
    }

    static class ViewHolderCategory {
        ImageView deviceImage;
        ImageView deviceName;

    }

    static class ViewHolderCategories {
        ImageView deviceImage;
        ImageView deviceName;
        LinearLayout comparedDeviceCategory;
        ImageView comparedDeviceImage;
        ImageView comparedDeviceName;
    }


    private class deviceSpecAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDeviceSpecItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mDeviceSpecItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (mDeviceSpecItems.get(position).getLayout().equals("@layout/device_spec_item"))
                return 0;
            if (mDeviceSpecItems.get(position).getLayout().equals("@layout/device_spec_item_colors"))
                return 1;
            if (mDeviceSpecItems.get(position).getLayout().equals("@layout/device_spec_item_category"))
                return 2;
            if (mDeviceSpecItems.get(position).getLayout().equals("@layout/device_spec_item_categories"))
                return 3;
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 4;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DeviceSpecItemModel item = mDeviceSpecItems.get(position);
            int viewType = this.getItemViewType(position);
            switch (viewType) {
                case 0:
                    {
                        ViewHolderSpecItem holder = null;

                        if (convertView == null) {
                            holder = new ViewHolderSpecItem();
                            convertView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(item.getLayoutResId(), mListView, false);

                            holder.specItem = (TextView) convertView.findViewById(R.id.device_spec_item);
                            holder.spec = (TextView) convertView.findViewById(R.id.device_spec);
                            holder.comparedSpec = (TextView) convertView.findViewById(R.id.compared_device_spec);
                            convertView.setTag(holder);
                        }
                        else {
                            holder = (ViewHolderSpecItem) convertView.getTag();
                        }

                        if (item.getDeviceSpecItemResId() > 0) {
                            holder.specItem.setText(item.getDeviceSpecItemResId());
                        }
                        //device spec
                        if (item.getDeviceSpecResId() > 0) {
                            holder.spec.setText(item.getDeviceSpecResId());
                        }

                        //compared device spect
                        if (item.getComparedDeviceSpecResId() > 0)
                            holder.comparedSpec.setText(item.getComparedDeviceSpecResId());
                        else
                            holder.comparedSpec.setVisibility(View.GONE);
                    }
                    break;

                case 1:
                    {
                        ViewHolderColors holder = null;

                        if (convertView == null) {
                            holder = new ViewHolderColors();
                            convertView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(item.getLayoutResId(), mListView, false);

                            holder.specItem = (TextView) convertView.findViewById(R.id.device_spec_item);
                            holder.colorsLayout = (LinearLayout) convertView.findViewById(R.id.colors_layout);
                            holder.comparedColorsLayout = (LinearLayout) convertView.findViewById(R.id.compared_colors_layout);
                            convertView.setTag(holder);
                        }
                        else {
                            holder = (ViewHolderColors) convertView.getTag();
                            holder.colorsLayout.removeAllViews();
                            holder.comparedColorsLayout.removeAllViews();
                        }

                        if (item.getDeviceSpecItemResId() > 0) {
                            holder.specItem.setText(item.getDeviceSpecItemResId());
                        }

                        for (int i = 0; i < item.getColors().size(); i++) {
                            TextView colorCircle = new TextView(getActivity());
                            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            colorCircle.setLayoutParams(layoutParams);
                            colorCircle.setBackgroundResource(R.drawable.circular_shape);
                            holder.colorsLayout.addView(colorCircle);

                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) colorCircle.getLayoutParams();
                            params.setMargins(0, 0, 20, 0);

                            GradientDrawable bgShape = (GradientDrawable) colorCircle.getBackground();
                            bgShape.setColor(getResources().getColor(item.getColorResId(i)));
                        }


                        if (item.getComparedColors().size() > 0) {
                            for (int i = 0; i < item.getComparedColors().size(); i++) {
                                TextView colorCircle = new TextView(getActivity());
                                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                colorCircle.setLayoutParams(layoutParams);
                                colorCircle.setBackgroundResource(R.drawable.circular_shape);
                                holder.comparedColorsLayout.addView(colorCircle);

                                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) colorCircle.getLayoutParams();
                                params.setMargins(0, 0, 20, 0);

                                GradientDrawable bgShape = (GradientDrawable) colorCircle.getBackground();
                                bgShape.setColor(getResources().getColor(item.getComparedColorResId(i)));
                            }
                        } else {
                            holder.comparedColorsLayout.setVisibility(View.GONE);
                        }

                    }
                    break;

                case 2:
                    {
                        ViewHolderCategory holder = null;

                        if (convertView == null) {
                            holder = new ViewHolderCategory();
                            convertView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(item.getLayoutResId(), mListView, false);

                            holder.deviceImage = (ImageView) convertView.findViewById(R.id.device_img);
                            holder.deviceName = (ImageView) convertView.findViewById(R.id.device_name);
                            convertView.setTag(holder);
                        }
                        else {
                            holder = (ViewHolderCategory) convertView.getTag();
                        }

                        if (item.getDeviceImageResId() > 0)
                            holder.deviceImage.setImageResource(item.getDeviceImageResId());

                        if (item.getDeviceNameLargeResId() > 0) //using logo large
                            holder.deviceName.setImageResource(item.getDeviceNameLargeResId());
                    }
                    break;

                case 3:
                    {
                        ViewHolderCategories holder = null;

                        if (convertView == null) {
                            holder = new ViewHolderCategories();
                            convertView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(item.getLayoutResId(), mListView, false);

                            holder.deviceImage = (ImageView) convertView.findViewById(R.id.device_img);
                            holder.deviceName = (ImageView) convertView.findViewById(R.id.device_name);
                            holder.comparedDeviceCategory = (LinearLayout) convertView.findViewById(R.id.compared_device_cateogry);
                            holder.comparedDeviceImage = (ImageView) convertView.findViewById(R.id.compared_device_img);
                            holder.comparedDeviceName = (ImageView) convertView.findViewById(R.id.compared_device_name);
                            convertView.setTag(holder);
                        }
                        else {
                            holder = (ViewHolderCategories) convertView.getTag();
                        }

                        if (item.getDeviceImageResId() > 0)
                            holder.deviceImage.setImageResource(item.getDeviceImageResId());

                        if (item.getDeviceNameLargeResId() > 0) //using logo large
                            holder.deviceName.setImageResource(item.getDeviceNameLargeResId());

                        if (item.getComparedDeviceImageResId() > 0)
                            holder.comparedDeviceImage.setImageResource(item.getComparedDeviceImageResId());

                        if (item.getComparedDeviceNameResId() > 0)
                            holder.comparedDeviceName.setImageResource(item.getComparedDeviceNameResId());
                    }
                    break;

            }

            return convertView;
        }
    };
}
