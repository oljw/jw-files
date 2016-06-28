package com.samsung.retailexperience.retailtmo.ui.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.samsung.retailexperience.retailtmo.R;
import com.samsung.retailexperience.retailtmo.analytics.FragmentChangeCause;
import com.samsung.retailexperience.retailtmo.animator.PageTransformer;
import com.samsung.retailexperience.retailtmo.gson.models.ArgumentsModel;
import com.samsung.retailexperience.retailtmo.util.AppConst;

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
        //JW
//        mBackBtn = (FrameLayout) mView.findViewById(R.id.back_button);
//        if (mBackBtn != null) {
//            mBackBtn.setOnClickListener(backBtnClick);
//        }
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
                AppConst.TransactionDir.TRANSACTION_DIR_BACKWARD, FragmentChangeCause.BACK_PRESSED);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String action = (String)view.getTag();
        if (action == null) return;
        changeFragment(action, AppConst.TransactionDir.TRANSACTION_DIR_FORWARD, FragmentChangeCause.TAP);
    }

}
