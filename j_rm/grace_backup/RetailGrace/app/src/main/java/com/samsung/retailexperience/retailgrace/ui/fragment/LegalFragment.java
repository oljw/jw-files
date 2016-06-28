package com.samsung.retailexperience.retailgrace.ui.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.samsung.retailexperience.retailgrace.R;
import com.samsung.retailexperience.retailgrace.gson.model.ArgsModel;
import com.tecace.app.manager.analytics.FragmentChangeCause;
import com.tecace.app.manager.gson.model.ArgumentsModel;
import com.tecace.app.manager.ui.fragment.BaseMenuFragment;
import com.tecace.app.manager.util.AppManagerConst;
import com.tecace.retail.appmanager.animator.PageTransformer;

/**
 * Created by icanmobile on 6/14/16.
 */
public class LegalFragment extends BaseMenuFragment {
    private static final String TAG = LegalFragment.class.getSimpleName();

    private String mLegalAction = null;
    private TextView mLegalMessage = null;
    boolean mTransitionEnter = false;

    @Override
    public void onFragmentCreated(ArgumentsModel args) {
        if (getFragmentModel().getFragment().getMenuItems() == null) return;

        ArgsModel argsModel = (ArgsModel) args;
        if (argsModel.getLegalGoToMainDemoAction() != null)
            getFragmentModel().getFragment().getMenuItems().get(0).setAction(((ArgsModel) args).getLegalGoToMainDemoAction());
        if (argsModel.getLegalGoToSubDemoAction() != null)
            getFragmentModel().getFragment().getMenuItems().get(1).setAction(((ArgsModel) args).getLegalGoToSubDemoAction());
        else
            getFragmentModel().getFragment().getMenuItems().remove(1);
    }

    @Override
    public void onViewCreated(View view) {
        mLegalMessage = (TextView) view.findViewById(R.id.message);
        if (mLegalMessage != null) {
            mLegalMessage.setText(getLegalMessage(getLegalAction()));
        }
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String action = (String)view.getTag();
        if (action == null) return;
        changeFragment(action, AppManagerConst.TransactionDir.TRANSACTION_DIR_FORWARD, FragmentChangeCause.TAP);
    }

    private String getLegalAction() {
        return this.mLegalAction;
    }
    private void setLegalAction(String legalAction) {
        this.mLegalAction = legalAction;
    }

    private String getLegalMessage(String legalAction) {
//        if (legalAction.equals("models/gear_vr_info.json"))
//            return getString(R.string.gear_vr_legal_desc);
//        else if ( legalAction.equals("models/smart_tv_info.json"))
//            return getString(R.string.smart_tv_legal_desc);
//        else if (legalAction.equals("models/samsung_pay_info.json"))
//            return getString(R.string.samsung_pay_legal_desc);
//        else if (legalAction.equals("models/smart_things_info.json"))
//            return getString(R.string.smart_things_legal_desc);
//        else if (legalAction.equals("models/tablet_info.json"))
//            return getString(R.string.tablet_legal_desc);
//        else if (legalAction.equals("models/smart_watch_info.json"))
//            return getString(R.string.smart_watch_legal_desc);

        return getString(R.string.legal_desc);
    }
}
