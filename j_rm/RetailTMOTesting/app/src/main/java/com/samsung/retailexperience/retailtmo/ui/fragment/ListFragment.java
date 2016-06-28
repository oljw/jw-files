package com.samsung.retailexperience.retailtmo.ui.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.samsung.retailexperience.retailtmo.analytics.FragmentChangeCause;
import com.samsung.retailexperience.retailtmo.animator.PageTransformer;
import com.samsung.retailexperience.retailtmo.gson.models.ArgumentsModel;
import com.samsung.retailexperience.retailtmo.util.AppConst;

/**
 * Created by icanmobile on 3/22/16.
 */
public class ListFragment extends BaseListFragment {
    private static final String TAG = ListFragment.class.getSimpleName();

    boolean mTransitionEnter = false;

    @Override
    public void onFragmentCreated(ArgumentsModel args) {

    }

    @Override
    public void onViewCreated(View view) {

    }

    @Override
    public void onPageTransitionStart(boolean enter, int nextAnim, AppConst.TransactionDir dir) {
        mTransitionEnter = enter;
        getView().addOnLayoutChangeListener(layoutChangeListener);
    }

    @Override
    public void onPageTransitionEnd(boolean enter, int nextAnim, AppConst.TransactionDir dir) {

    }

    @Override
    public void onPageTransitionCancel(boolean enter, int nextAnim, AppConst.TransactionDir dir) {

    }

    private View.OnLayoutChangeListener layoutChangeListener = new View.OnLayoutChangeListener(){
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                   int oldRight, int oldBottom) {
            v.removeOnLayoutChangeListener(this);
            PageTransformer.getInstance().run(getAppContext(), getView(), mTransitionEnter, getTransactionDir());
        }
    };

    @Override
    public void onBackPressed() {
        if (getFragmentModel() == null || getFragmentModel().getActionBackKey() == null) return;
        changeFragment(getFragmentModel().getActionBackKey(),
                AppConst.TransactionDir.TRANSACTION_DIR_BACKWARD, FragmentChangeCause.BACK_PRESSED);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
    }
}
