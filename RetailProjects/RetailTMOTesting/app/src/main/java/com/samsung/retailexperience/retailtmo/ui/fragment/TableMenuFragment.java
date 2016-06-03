package com.samsung.retailexperience.retailtmo.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;
import com.samsung.retailexperience.retailtmo.R;
import com.samsung.retailexperience.retailtmo.analytics.FragmentChangeCause;
import com.samsung.retailexperience.retailtmo.gson.models.ArgumentsModel;
import com.samsung.retailexperience.retailtmo.gson.models.FragmentModel;
import com.samsung.retailexperience.retailtmo.gson.models.MenuItemModel;
import com.samsung.retailexperience.retailtmo.gson.models.MenuModel;
import com.samsung.retailexperience.retailtmo.util.AppConst;
import com.samsung.retailexperience.retailtmo.util.FileUtil;
import com.samsung.retailexperience.retailtmo.util.JsonUtil;

import java.util.ArrayList;

/**
 * Created by jaekim on 5/17/16.
 */
public abstract class TableMenuFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = TableMenuFragment.class.getSimpleName();

    private FragmentModel<MenuModel> mFragmentModel = null;
    private AppConst.TransactionDir mTransactionDir = null;
    private ArrayList<MenuItemModel> mMenuItems = null;

    private BitmapDrawable drawable;
    private Bitmap bitmap;

    public TableMenuFragment() {

    }

    abstract public void onFragmentCreated(ArgumentsModel args);
    abstract public void onViewCreated(View view);
    protected FragmentModel<MenuModel> getFragmentModel() {
        return mFragmentModel;
    }
    protected AppConst.TransactionDir getTransactionDir() {
        return this.mTransactionDir;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ArgumentsModel args = (ArgumentsModel) getArguments().getSerializable(AppConst.ARGUMENTS_MODEL);
            mFragmentModel = JsonUtil.loadJsonModel(getActivity().getApplicationContext(),
                    args.getFragmentJson(), new TypeToken<FragmentModel<MenuModel>>() {}.getType());
            mTransactionDir = (AppConst.TransactionDir) AppConst.TransactionDir.valueOf(args.getTransitionDir());
            onFragmentCreated(args);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(getFragmentModel().getLayoutResId(), container, false);

        // TODO set background
        if (getFragmentModel().getBackgroundResId() > 0)
            mView.setBackgroundResource(getFragmentModel().getBackgroundResId());

        // set view pivot X,Y
        if (getFragmentModel().getPivotXValue() != 0)
            mView.setPivotX(getResources().getInteger(getFragmentModel().getPivotXValue()));
        if (getFragmentModel().getPivotYValue() != 0)
            mView.setPivotY(getResources().getInteger(getFragmentModel().getPivotYValue()));

        TableLayout tableLayout = (TableLayout)mView.findViewById(R.id.menu_layout);
        if (tableLayout != null) {
            mMenuItems = getFragmentModel().getFragment().getMenuItems();
            Context context = inflater.getContext();
            // TODO
            int rowCount = 2;
            int colCount = 3;
            for (int row = 0; row < rowCount; row++) {
                /* Create a new row to be added. */
                TableRow tr = new TableRow(context);
                TableRow.LayoutParams tableRowLayoutParams =
                        new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT);
                tr.setLayoutParams(tableRowLayoutParams);
                for (int col = 0; col < colCount; col++) {
                    MenuItemModel item = mMenuItems.get((rowCount + 1) * row + col);

                    Button button = new Button(context);
                    button.setText(item.getTitle());
                    Log.e(TAG, "item title: " + item.getTitle());

                    // TODO Release bitmap resource
                    bitmap = FileUtil.getBitmapResource(context, item.getBackground());
                    drawable = new BitmapDrawable(context.getResources(), bitmap);
                    button.setBackground(drawable);

                    TableRow.LayoutParams rowItemLayoutParams =
                            new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT);
                    button.setLayoutParams(rowItemLayoutParams);
                    button.setTag(item.getAction());
                    button.setOnClickListener(this);
                    tr.addView(button);
                }
                tableLayout.addView(tr);
            }
        }

        onViewCreated(mView);

        return mView;
    }

    @Override
    public void onPause() {
        super.onPause();

        //JW
        if (bitmap != null) {
            Log.d(TAG, "bitmap recycled");
            bitmap.recycle();
        }
    }

    @Override
    public void onClick(View v) {
        String actionTag = (String)v.getTag();
        if (!Strings.isNullOrEmpty(actionTag)) {
            changeFragment(actionTag, AppConst.TransactionDir.TRANSACTION_DIR_FORWARD, FragmentChangeCause.TAP);
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentModel() == null || getFragmentModel().getActionBackKey() == null) return;
        changeFragment(getFragmentModel().getActionBackKey(),
                AppConst.TransactionDir.TRANSACTION_DIR_BACKWARD, FragmentChangeCause.BACK_PRESSED);
    }
}
