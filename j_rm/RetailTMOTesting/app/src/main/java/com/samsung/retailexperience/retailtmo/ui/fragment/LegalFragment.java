package com.samsung.retailexperience.retailtmo.ui.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.samsung.retailexperience.retailtmo.R;
import com.samsung.retailexperience.retailtmo.analytics.FragmentChangeCause;
import com.samsung.retailexperience.retailtmo.animator.PageTransformer;
import com.samsung.retailexperience.retailtmo.gson.models.ArgumentsModel;
import com.samsung.retailexperience.retailtmo.util.AppConst;

/**
 * Created by icanmobile on 3/8/16.
 */
public class LegalFragment extends BaseMenuFragment {
    private static final String TAG = LegalFragment.class.getSimpleName();

    private String mLegalAction = null;
    private TextView mLegalMessage = null;
    boolean mTransitionEnter = false;

    @Override
    public void onFragmentCreated(ArgumentsModel args) {
        if (args.getLegalMenuItemAction() != null) {
            setLegalAction(args.getLegalMenuItemAction());

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
        //JW COMMENTED OUT
//        mLegalMessage = (TextView) view.findViewById(R.id.message);
//        if (mLegalMessage != null) {
//            mLegalMessage.setText(getLegalMessage(getLegalAction()));
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


    @Override
    public void onBackPressed() {
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

    private String getLegalAction() {
        return this.mLegalAction;
    }
    private void setLegalAction(String legalAction) {
        this.mLegalAction = legalAction;
    }

    //JW COMMENTED OUT
//    private String getLegalMessage(String legalAction) {
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
//
//        return getString(R.string.legal_desc);
//    }
}
