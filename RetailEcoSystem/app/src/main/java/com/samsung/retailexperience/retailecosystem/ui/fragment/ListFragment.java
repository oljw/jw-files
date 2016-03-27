package com.samsung.retailexperience.retailecosystem.ui.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.samsung.retailexperience.retailecosystem.util.AppConst;
import com.samsung.retailexperience.retailecosystem.util.PageTransformer;

/**
 * Created by icanmobile on 3/22/16.
 */
public class ListFragment extends BaseListFragment {
    private static final String TAG = ListFragment.class.getSimpleName();

    @Override
    public void onFragmentCreated() {

    }

    @Override
    public void onViewCreated(View view) {

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
        if (getFragmentModel() == null || getFragmentModel().getActionBackKey() == null) return;
        changeFragment(getFragmentModel().getActionBackKey(),
                AppConst.TransactionDir.TRANSACTION_DIR_BACKWARD);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
    }
}
