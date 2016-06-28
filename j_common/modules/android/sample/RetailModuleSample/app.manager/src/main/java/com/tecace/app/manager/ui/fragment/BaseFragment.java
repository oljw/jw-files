package com.tecace.app.manager.ui.fragment;

import android.view.View;

import com.tecace.app.manager.R;
import com.tecace.app.manager.analytics.FragmentChangeCause;
import com.tecace.app.manager.ui.activity.BaseActivity;
import com.tecace.app.manager.util.AppManagerConst;
import com.tecace.retail.appmanager.ui.fragment.RetailFragment;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by icanmobile on 3/1/16.
 */
public abstract class BaseFragment extends RetailFragment {
    private static final String TAG = BaseFragment.class.getSimpleName();

    protected View mView = null;

    protected AppManagerConst.TransactionDir mTransactionDir = null;
    protected ArrayList<Integer> mNoAnimChildViewIDs = new ArrayList<Integer>(Arrays.asList(R.id.back_button));

    abstract public void onBackPressed();

    public void changeFragment(String json, AppManagerConst.TransactionDir dir, FragmentChangeCause cause) {
        try {
            ((BaseActivity) getActivity()).changeFragment(json, dir, cause);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public View getView() {
        return mView;


    }

    protected AppManagerConst.TransactionDir getTransactionDir() {
        return this.mTransactionDir;
    }
    protected boolean getTransactionDirBoolean() {
        switch (this.mTransactionDir) {
            case TRANSACTION_DIR_FORWARD:
                return true;
            case TRANSACTION_DIR_BACKWARD:
                return false;
        }
        return false;
    }
}
