package com.samsung.retailexperience.retailmodulesample.ui.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.samsung.retailexperience.retailmodulesample.R;
import com.tecace.app.manager.analytics.FragmentChangeCause;
import com.tecace.app.manager.gson.model.ArgumentsModel;
import com.tecace.app.manager.ui.fragment.BaseMenuFragment;
import com.tecace.app.manager.util.AppManagerConst;
import com.tecace.retail.appmanager.animator.PageTransformer;

/**
 * Created by icanmobile on 3/3/16.
 */
public class MenuFragment extends BaseMenuFragment {
    private static final String TAG = MenuFragment.class.getSimpleName();

    private FrameLayout mBackBtn = null;
    boolean mTransitionEnter = false;

    @Override
    public void onFragmentCreated(ArgumentsModel args) {
    }

    @Override
    public void onViewCreated(View view) {
        // Back Button
        mBackBtn = (FrameLayout) mView.findViewById(R.id.back_button);
        if (mBackBtn != null) {
            mBackBtn.setOnClickListener(backBtnClick);
        }
    }

    @Override
    public void onPageTransitionStart(boolean enter, int nextAnim, AppManagerConst.TransactionDir dir) {
        mTransitionEnter = enter;
        if (!isCustomAnimator(nextAnim))
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

    protected View.OnClickListener backBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            goBack();
        }
    };

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        if (getFragmentModel() == null || getFragmentModel().getActionBackKey() == null) return;
        changeFragment(getFragmentModel().getActionBackKey(),
                AppManagerConst.TransactionDir.TRANSACTION_DIR_BACKWARD, FragmentChangeCause.BACK_PRESSED);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String action = (String)view.getTag();
        if (action == null) return;
        changeFragment(action, AppManagerConst.TransactionDir.TRANSACTION_DIR_FORWARD, FragmentChangeCause.TAP);
    }

}
