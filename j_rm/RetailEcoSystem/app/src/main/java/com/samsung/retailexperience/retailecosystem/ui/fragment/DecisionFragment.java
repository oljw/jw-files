package com.samsung.retailexperience.retailecosystem.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.samsung.retailexperience.retailecosystem.R;
import com.samsung.retailexperience.retailecosystem.ui.view.circlemenu.CircleMenuView;
import com.samsung.retailexperience.retailecosystem.util.AppConst;
import com.samsung.retailexperience.retailecosystem.util.PageTransformer;

/**
 * Created by icanmobile on 3/3/16.
 */
public class DecisionFragment extends BaseMenuFragment {
    private static final String TAG = DecisionFragment.class.getSimpleName();

    private CircleMenuView mCircleMenuView = null;

    @Override
    public void onFragmentCreated() {
    }

    @Override
    public void onViewCreated(View view) {
        mCircleMenuView = (CircleMenuView) view.findViewById(R.id.circle_menu_view);
        mCircleMenuView.setListener(circleMenuViewListener);
    }

    @Override
    public void onPageTransitionStart(boolean enter, AppConst.TransactionDir dir) {
        new PageTransformer().run(getAppContext(), getView(), enter, dir);
    }

    @Override
    public void onPageTransitionEnd(boolean enter, AppConst.TransactionDir dir) {

    }

    @Override
    public void onPageTransitionCancel(boolean enter, AppConst.TransactionDir dir) {

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "##### onBackPressed)+ " + getFragmentModel().getActionBackKey());
        if (getFragmentModel() == null || getFragmentModel().getActionBackKey() == null) return;


        Log.d(TAG, "##### onBackPressed : call changeFragment()");
        changeFragment(getFragmentModel().getActionBackKey(),
                AppConst.TransactionDir.TRANSACTION_DIR_BACKWARD);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private CircleMenuView.CircleMenuViewListener circleMenuViewListener = new CircleMenuView.CircleMenuViewListener() {
        @Override
        public void onClickItem(int index) {
            if (getFragmentModel() == null ||
                getFragmentModel().getFragment().getMenuItems() == null ||
                getFragmentModel().getFragment().getMenuItems().get(index) == null)
                return;
            if (index < 0 || index >= getFragmentModel().getFragment().getMenuItems().size()) return;

            changeFragment(getFragmentModel().getFragment().getMenuItems().get(index).getAction(),
                    AppConst.TransactionDir.TRANSACTION_DIR_FORWARD);
        }
    };
}
