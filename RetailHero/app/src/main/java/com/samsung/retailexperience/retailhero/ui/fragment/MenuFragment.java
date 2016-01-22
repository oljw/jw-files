package com.samsung.retailexperience.retailhero.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.MenuItemModel;
import com.samsung.retailexperience.retailhero.gson.models.MenuModel;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;

import java.util.ArrayList;

/**
 * Created by icanmobile on 1/12/16.
 */
public class MenuFragment extends BaseMenuFragment {
    private static final String TAG = MenuFragment.class.getSimpleName();

    public static MenuFragment newInstance(FragmentModel<MenuModel> fragmentModel) {
        MenuFragment fragment = new MenuFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        if (view != null) {
            View videoButton = view.findViewById(R.id.menu_video_button);
            if (videoButton != null) {
                videoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strVideoAction = mFragmentModel.getFragment().getVideoAction();
                        if (strVideoAction == null) {
                            return;
                        }
                        changeFragment(AppConst.UIState.valueOf(strVideoAction), AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
                    }
                });

                TextView videoTitle = (TextView) view.findViewById(R.id.videoMenuTitle);
                if (videoTitle != null && mFragmentModel.getFragment().getVideoTitleResId() > 0) {
                    videoTitle.setText(mFragmentModel.getFragment().getVideoTitleResId());
                }

                TextView videoSubTitle = (TextView) view.findViewById(R.id.videoMenuSubTitle);
                if (videoSubTitle != null && mFragmentModel.getFragment().getVideoSubTitleResId() > 0) {
                    videoSubTitle.setText(mFragmentModel.getFragment().getVideoSubTitleResId());
                }
            }
        }
    }

    @Override
    public ArrayList<MenuItemModel> onMenuItemsRequested() {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String action = (String)view.getTag();
        if (action == null) return;
        changeFragment(AppConst.UIState.valueOf(action), AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentModel().getActionBackKey() != null) {
            changeFragment(AppConst.UIState.valueOf(getFragmentModel().getActionBackKey()),
                    AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
        }
    }
}
