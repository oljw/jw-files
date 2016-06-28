package com.samsung.retailexperience.retailgrace.ui.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.tecace.app.manager.analytics.FragmentChangeCause;
import com.tecace.app.manager.gson.model.ArgumentsModel;
import com.tecace.app.manager.ui.fragment.BaseListFragment;
import com.tecace.app.manager.util.AppManagerConst;
import com.tecace.retail.appmanager.animator.PageTransformer;

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
    public void onPageTransitionStart(boolean enter, int nextAnim, AppManagerConst.TransactionDir dir) {
        mTransitionEnter = enter;
        getView().addOnLayoutChangeListener(layoutChangeListener);
    }

    @Override
    public void onPageTransitionEnd(boolean enter, int nextAnim, AppManagerConst.TransactionDir dir) {

    }

    @Override
    public void onPageTransitionCancel(boolean enter, int nextAnim, AppManagerConst.TransactionDir dir) {

    }

    private View.OnLayoutChangeListener layoutChangeListener = new View.OnLayoutChangeListener(){
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                   int oldRight, int oldBottom) {
            v.removeOnLayoutChangeListener(this);
            PageTransformer.getInstance().run(getAppContext(), getView(), mTransitionEnter, getTransactionDirBoolean(), mNoAnimChildViewIDs);
        }
    };

    @Override
    public void onBackPressed() {
        if (getFragmentModel() == null || getFragmentModel().getActionBackKey() == null) return;
        changeFragment(getFragmentModel().getActionBackKey(),
                AppManagerConst.TransactionDir.TRANSACTION_DIR_BACKWARD, FragmentChangeCause.BACK_PRESSED);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
    }
}
