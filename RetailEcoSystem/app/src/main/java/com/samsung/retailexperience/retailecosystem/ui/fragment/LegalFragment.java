package com.samsung.retailexperience.retailecosystem.ui.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.samsung.retailexperience.retailecosystem.R;
import com.samsung.retailexperience.retailecosystem.util.AppConst;
import com.samsung.retailexperience.retailecosystem.util.PageTransformer;

/**
 * Created by icanmobile on 3/8/16.
 */
public class LegalFragment extends BaseMenuFragment {
    private static final String TAG = LegalFragment.class.getSimpleName();

    private String mLegalAction = null;
    private TextView mLegalMessage = null;
    @Override
    public void onFragmentCreated() {
        if (getArguments() != null) {
            setLegalAction(getArguments().getString(AppConst.ARG_LEGAL_MENU_ITEM_ACTION));
            if (getLegalAction()!=  null) {
                if (getFragmentModel().getFragment().getMenuItems() == null ||
                    getFragmentModel().getFragment().getMenuItems().size() < 2) return;
                getFragmentModel().getFragment().getMenuItems().get(0).setAction(getLegalAction());
            }
            else {
                getFragmentModel().getFragment().getMenuItems().remove(0);
            }
        }
    }

    @Override
    public void onViewCreated(View view) {
        mLegalMessage = (TextView) view.findViewById(R.id.legal_message);
        if (mLegalMessage != null) {
            mLegalMessage.setText(getLegalMessage(getLegalAction()));
        }
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String action = (String)view.getTag();
        if (action == null) return;
        changeFragment(action, AppConst.TransactionDir.TRANSACTION_DIR_FORWARD);
    }

    private String getLegalAction() {
        return this.mLegalAction;
    }
    private void setLegalAction(String legalAction) {
        this.mLegalAction = legalAction;
    }

    private String getLegalMessage(String legalAction) {
        if (legalAction.equals("models/gear_vr_info.json"))
            return getString(R.string.gear_vr_legal_desc);
        else if ( legalAction.equals("models/smart_tv_info.json"))
            return getString(R.string.smart_tv_legal_desc);
        else if (legalAction.equals("models/samsung_pay_info.json"))
            return getString(R.string.samsung_pay_legal_desc);
        else if (legalAction.equals("models/smart_things_info.json"))
            return getString(R.string.smart_things_legal_desc);
        else if (legalAction.equals("models/tablet_info.json"))
            return getString(R.string.tablet_legal_desc);
        else if (legalAction.equals("models/smart_watch_info.json"))
            return getString(R.string.smart_watch_legal_desc);

        return getString(R.string.legal);
    }
}
