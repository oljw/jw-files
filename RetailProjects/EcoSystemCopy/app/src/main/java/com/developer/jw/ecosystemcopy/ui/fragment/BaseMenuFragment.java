package com.developer.jw.ecosystemcopy.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developer.jw.ecosystemcopy.R;
import com.developer.jw.ecosystemcopy.animator.CircularReveal;
import com.developer.jw.ecosystemcopy.gson.models.ArgumentsModel;
import com.developer.jw.ecosystemcopy.gson.models.FragmentModel;
import com.developer.jw.ecosystemcopy.gson.models.MenuItemModel;
import com.developer.jw.ecosystemcopy.gson.models.MenuModel;
import com.developer.jw.ecosystemcopy.util.AppConst;
import com.developer.jw.ecosystemcopy.util.JsonUtil;
import com.google.common.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by JW on 2016-04-21.
 */
public abstract class BaseMenuFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    private static final String TAG = BaseMenuFragment.class.getSimpleName();

    private FragmentModel<MenuModel> mFragmentModel = null;
    private AppConst.TransactionDir mTransactionDir = null;
    private ImageView mImageBG = null;
    private TextView mTitle1 = null;
    private TextView mTitle2 = null;
    private TextView mSubTitle = null;
    private ListView mListView = null;
    private ArrayList<MenuItemModel> mMenuItems = null;

    public BaseMenuFragment() {

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

        // set background
        if (getFragmentModel().getBackgroundResId() > 0)
            mView.setBackgroundResource(getFragmentModel().getBackgroundResId());

        // set view pivot X,Y
        if (getFragmentModel().getPivotXValue() != 0)
            mView.setPivotX(getResources().getInteger(getFragmentModel().getPivotXValue()));
        if (getFragmentModel().getPivotYValue() != 0)
            mView.setPivotY(getResources().getInteger(getFragmentModel().getPivotYValue()));

        mImageBG = (ImageView) mView.findViewById(R.id.image_bg);
        if (mImageBG != null) {
            if (getFragmentModel().getImageBG() != null) {
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .considerExifParams(true)
                        .build();
                ImageLoader.getInstance().displayImage("drawable://" + getFragmentModel().getImageBGResId(), mImageBG, options);

                if (getFragmentModel().getImageBGAlignResId() > 0) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mImageBG.getLayoutParams();
                    layoutParams.addRule(getResources().getInteger(getFragmentModel().getImageBGAlignResId()));
                    mImageBG.setLayoutParams(layoutParams);
                }
                if (getFragmentModel().getImageBGMargin() != null) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mImageBG.getLayoutParams();
                    int leftMargin = getResources().getInteger(getFragmentModel().getImageBGMarginLeftResId());
                    int topMargin = getResources().getInteger(getFragmentModel().getImageBGMarginTopResId());
                    int rightMargin = getResources().getInteger(getFragmentModel().getImageBGMarginRightResId());
                    int bottomMargin = getResources().getInteger(getFragmentModel().getImageBGMarginBottomResId());
                    params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                }
            }
            else {
                mImageBG.setVisibility(View.GONE);
            }
        }

        // Title 1 - small
        mTitle1 = (TextView) mView.findViewById(R.id.title1);
        if (mTitle1 != null) {
            if (getFragmentModel().getFragment().getTitle() != null &&
                    getFragmentModel().getFragment().getTitle().size() > 0)
                mTitle1.setText(getString(getFragmentModel().getFragment().getTitleResId(0)));
        }

        // Title 2 - large
        mTitle2 = (TextView) mView.findViewById(R.id.title2);
        if (mTitle2 != null) {
            if (getFragmentModel().getFragment().getTitle() != null &&
                    getFragmentModel().getFragment().getTitle().size() > 1)
                mTitle2.setText(getString(getFragmentModel().getFragment().getTitleResId(1)));
        }

        // Subtitle
        mSubTitle = (TextView) mView.findViewById(R.id.subtitle);
        if (mSubTitle != null) {
            if (getFragmentModel().getFragment().getSubTitle() != null &&
                    getFragmentModel().getFragment().getSubTitle().size() > 0)
                mSubTitle.setText(getString(getFragmentModel().getFragment().getSubTitleResId(0)));
        }

        // Menu ListView
        mListView = (ListView) mView.findViewById(R.id.list);
        if (mListView != null) {
            mMenuItems = getFragmentModel().getFragment().getMenuItems();
            mListView.setAdapter(new MenuAdapter());
            mListView.setOnItemClickListener(this);
        }

        onViewCreated(mView);

        return mView;
    }


    @Override
    public Animator onCreateAnimator(int transit, final boolean enter, final int nextAnim) {
        Animator animator = null;

//        Log.d(TAG, "##### enter = " + enter + ", nextAnim = " + nextAnim);
        if (nextAnim == 0) return null;

        animator = getCustomAnimator(enter, nextAnim);
        if (animator == null)
            animator = AnimatorInflater.loadAnimator(getActivity(), nextAnim);

        if (animator != null) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    onPageTransitionCancel(enter, nextAnim, getTransactionDir());
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    onPageTransitionEnd(enter, nextAnim, getTransactionDir());
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    onPageTransitionStart(enter, nextAnim, getTransactionDir());
                }
            });
        }
        return animator;
    }

    private Animator getCustomAnimator(boolean enter, int nextAnim) {
        switch (nextAnim) {
            case R.animator.coded_circle_reveal:
                getView().addOnLayoutChangeListener(layoutChangeListener);
                return AnimatorInflater.loadAnimator(getActivity(), R.animator.none);
            case R.animator.coded_circle_unreveal:
                return CircularReveal.getInstance().getUnreveal(getAppContext(), getView(), R.color.white, R.color.samsung_blue);
        }
        return null;
    }

    private View.OnLayoutChangeListener layoutChangeListener = new View.OnLayoutChangeListener(){
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                   int oldRight, int oldBottom) {
            v.removeOnLayoutChangeListener(this);

            int cx = getResources().getInteger(getFragmentModel().getPivotXValue());
            int cy = getResources().getInteger(getFragmentModel().getPivotYValue());
            CircularReveal.getInstance().runReveal(getAppContext(), v, cx, cy, R.color.samsung_blue, R.color.white);
        }
    };



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy()  {
        super.onDestroy();
    }

    abstract public void onFragmentCreated(ArgumentsModel args);
    abstract public void onViewCreated(View view);

    protected FragmentModel<MenuModel> getFragmentModel() {
        return mFragmentModel;
    }
    protected AppConst.TransactionDir getTransactionDir() {
        return this.mTransactionDir;
    }

    abstract public void onPageTransitionStart(boolean enter, int nextAnim, AppConst.TransactionDir dir);
    abstract public void onPageTransitionEnd(boolean enter, int nextAnim, AppConst.TransactionDir dir);
    abstract public void onPageTransitionCancel(boolean enter, int nextAnim, AppConst.TransactionDir dir);



    private class MenuAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mMenuItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mMenuItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Context context = getActivity().getApplicationContext();
            MenuItemModel item = mMenuItems.get(position);

            final View itemView = LayoutInflater.from(context).inflate(item.getLayoutResId(), mListView, false);

            if ((TextView) itemView.findViewById(R.id.title) != null) {
                ((TextView) itemView.findViewById(R.id.title)).setText(getString(item.getTitleResId()));
            }

            if ((ImageView) itemView.findViewById(R.id.icon) != null) {
                ((ImageView) itemView.findViewById(R.id.icon)).setImageResource(item.getIconResId());
            }

            if (item.getAction() != null)
                itemView.setTag(item.getAction());

            return itemView;
        }
    };
}
