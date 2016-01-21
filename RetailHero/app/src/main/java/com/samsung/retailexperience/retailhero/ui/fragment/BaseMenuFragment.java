package com.samsung.retailexperience.retailhero.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.MenuItemModel;
import com.samsung.retailexperience.retailhero.gson.models.MenuModel;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.AssetUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by icanmobile on 1/12/16.
 */
public abstract class BaseMenuFragment extends BaseFragment
        implements AdapterView.OnItemClickListener {
    private static final String TAG = BaseMenuFragment.class.getSimpleName();

    protected View mView = null;
    protected String mJsonModel = null;
    protected FragmentModel<MenuModel> mFragmentModel = null;
    protected ImageView mDrawer = null;
    protected ListView mListView = null;
    protected ArrayList<MenuItemModel> mMenuItems = null;

    public BaseMenuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFragmentModel = (FragmentModel<MenuModel>) getArguments().getSerializable(AppConsts.ARG_FRAGMENT_MODEL);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(mFragmentModel.getLayoutResId(), container, false);

        //set background color
        if (mFragmentModel.getBackgroundResId() > 0)
            mView.setBackgroundResource(mFragmentModel.getBackgroundResId());

        // drawer icon
        mDrawer = (ImageView) mView.findViewById(R.id.drawer);
        if (mDrawer != null) {
            if (mFragmentModel.getDrawerResId() == 0) {
                mDrawer.setVisibility(View.INVISIBLE);
            }
            else {
                mDrawer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickDrawerBtn();
                    }
                });
            }
        }

        // set title
        if (mFragmentModel.getFragment().getTitleResId() > 0 &&  mView.findViewById(R.id.title) != null)
            ((TextView) mView.findViewById(R.id.title)).setText(getString(mFragmentModel.getFragment().getTitleResId()));

        // set listview
        mListView = (ListView) mView.findViewById(R.id.list);
        if (mListView != null) {
            if (mFragmentModel.getFragment().getMenuItems().size() > 0) {
                mMenuItems = mFragmentModel.getFragment().getMenuItems();
            }
            else {
                mMenuItems = onMenuItemsRequested();
                if (mMenuItems == null)
                    mMenuItems = new ArrayList<MenuItemModel>();
            }
            mListView.setAdapter(new MenuAdapter());
            mListView.setOnItemClickListener(this);
        }

        onViewCreated(mView);
        return mView;
    }

    abstract public void onViewCreated(View view);
    abstract public ArrayList<MenuItemModel> onMenuItemsRequested();

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
        if (mFragmentModel != null)
            setDrawer(mFragmentModel.getDrawerResId());
        else
            setDrawer(0);
    }

    protected FragmentModel<MenuModel> loadJsonFragmentModel() {
        Context context = getActivity().getApplicationContext();
        String data = AssetUtil.GetTextFromAsset(context, mJsonModel);

        Gson gson = new Gson();
        Type fragmentType = new TypeToken<FragmentModel<MenuModel>>() {}.getType();
        return gson.fromJson(data, fragmentType);
    }

    protected FragmentModel<MenuModel> getFragmentModel() {
        return mFragmentModel;
    }

    private class MenuAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mMenuItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mMenuItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Context context = getActivity().getApplicationContext();
            MenuItemModel item = mMenuItems.get(position);

            View menuItemView = LayoutInflater.from(context).inflate(item.getLayoutResId(), mListView, false);

            if ((ImageView)menuItemView.findViewById(R.id.menuItemIcon) != null) {
                if (item.getIconResId() > 0)
                    ((ImageView) menuItemView.findViewById(R.id.menuItemIcon)).setImageResource(item.getIconResId());
                else    //if icon is not exist, we use padding value of icon for menuItemTitle (ex. Class)
                    menuItemView.findViewById(R.id.menuItemIcon).setVisibility(View.GONE);
            }

            if ((TextView) menuItemView.findViewById(R.id.menuItemTitle) != null) {
                if (item.getTitleResId() > 0) {
                    ((TextView) menuItemView.findViewById(R.id.menuItemTitle)).setText(item.getTitleResId());
                    if (item.getIconResId() > 0) {
                        ViewGroup.MarginLayoutParams p =
                                (ViewGroup.MarginLayoutParams) ((TextView) menuItemView.findViewById(R.id.menuItemTitle)).getLayoutParams();
                        p.leftMargin = (int) getResources().getDimension(R.dimen.menu_item_component_spacing);
                        ((TextView) menuItemView.findViewById(R.id.menuItemTitle)).requestLayout();
                    }
                } else
                    menuItemView.findViewById(R.id.menuItemTitle).setVisibility(View.GONE);
            }

            if (item.getAction() != null)
                menuItemView.setTag(item.getAction());

            return menuItemView;
        }
    };
}