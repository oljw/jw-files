package com.samsung.retailexperience.retailecosystem.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.samsung.retailexperience.retailecosystem.R;
import com.samsung.retailexperience.retailecosystem.gson.models.FragmentModel;
import com.samsung.retailexperience.retailecosystem.gson.models.ListItemModel;
import com.samsung.retailexperience.retailecosystem.gson.models.ListModel;
import com.samsung.retailexperience.retailecosystem.ui.view.CustomFontTextView;
import com.samsung.retailexperience.retailecosystem.ui.view.CustomListView;
import com.samsung.retailexperience.retailecosystem.util.AppConst;
import com.samsung.retailexperience.retailecosystem.util.JsonUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by icanmobile on 3/14/16.
 */
public abstract class BaseListFragment extends BaseFragment
        implements AdapterView.OnItemClickListener {
    private static final String TAG = BaseListFragment.class.getSimpleName();

    private FragmentModel<ListModel> mFragmentModel = null;
    private AppConst.TransactionDir mTransactionDir = null;

    private CustomListView mListView = null;
    private ListAdapter mAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String json = getArguments().getString(AppConst.ARG_FRAGMENT_MODEL);
            Type fragmentType = new TypeToken<FragmentModel<ListModel>>() {}.getType();
            mFragmentModel = JsonUtil.loadJsonModel(getActivity().getApplicationContext(), json, fragmentType);
            mTransactionDir = (AppConst.TransactionDir) getArguments().getSerializable(AppConst.ARG_FRAGMENT_TRANSACTION_DIR);
        }
        onFragmentCreated();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(getFragmentModel().getLayoutResId(), container, false);

        mListView = (CustomListView) mView.findViewById(R.id.list);
        if (mListView != null) {
            mAdapter = new ListAdapter(getAppContext());
            mAdapter.setData(getFragmentModel().getFragment().getListItems());
            mListView.setAdapter(mAdapter);
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {
                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                    if (mListView.computeVerticalScrollOffset() == 0) {
                    }
                    else {
                    }
                }
            });
        }
        return mView;
    }

    @Override
    public Animator onCreateAnimator(int transit, final boolean enter, int nextAnim) {
        Animator animator = null;

//        Log.d(TAG, "##### enter = " + enter + ", nextAnim = " + nextAnim);
        if (nextAnim == 0) return null;

        animator = AnimatorInflater.loadAnimator(getActivity(), nextAnim);
        if (animator != null) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    onPageTransitionCancel(enter, getTransactionDir());
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    onPageTransitionEnd(enter, getTransactionDir());
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    onPageTransitionStart(enter, getTransactionDir());
                }
            });
        }

        return animator;
    }

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

    abstract public void onFragmentCreated();
    abstract public void onViewCreated(View view);

    protected FragmentModel<ListModel> getFragmentModel() {
        return mFragmentModel;
    }
    protected AppConst.TransactionDir getTransactionDir() {
        return this.mTransactionDir;
    }

    abstract public void onPageTransitionStart(boolean enter, AppConst.TransactionDir dir);
    abstract public void onPageTransitionEnd(boolean enter, AppConst.TransactionDir dir);
    abstract public void onPageTransitionCancel(boolean enter, AppConst.TransactionDir dir);



    /*
     * List Adapter
     */
    private static class ImageHolder {
        ImageView image;
    }
    private static class TextHolder {
        CustomFontTextView text;
    }

    private class ListAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private DisplayImageOptions options;
        private ArrayList<ListItemModel> items;

        public ListAdapter (Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);

            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .build();
        }
        public void setData(ArrayList<ListItemModel> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return this.items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (items.get(position).getLayout().equals("@layout/list_item_close"))
                return 0;
            if (items.get(position).getLayout().equals("@layout/list_item_image_center"))
                return 1;
            if (items.get(position).getLayout().equals("@layout/list_item_image_right"))
                return 2;
            if (items.get(position).getLayout().equals("@layout/list_item_text"))
                return 3;
            if (items.get(position).getLayout().equals("@layout/list_item_margin"))
                return 4;
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 5;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ListItemModel item = this.items.get(position);
            switch (getItemViewType(position)) {
                case 0:
                    {
                        ImageHolder holder = null;
                        if (view == null) {
                            holder = new ImageHolder();
                            view = inflater.inflate(item.getLayoutResId(), viewGroup, false);
                            holder.image = (ImageView) view.findViewById(R.id.image);
                            view.setTag(holder);
                        }
                        else {
                            holder = (ImageHolder) view.getTag();
                        }

                        //  layout color
                        view.setBackgroundResource(item.getLayoutColorResId());
                    }
                    break;
                case 1:
                    {
                        ImageHolder holder = null;
                        if (view == null) {
                            holder = new ImageHolder();
                            view = inflater.inflate(item.getLayoutResId(), viewGroup, false);
                            holder.image = (ImageView) view.findViewById(R.id.image);
                            view.setTag(holder);
                        }
                        else {
                            holder = (ImageHolder) view.getTag();
                        }

                        //  layout color
                        view.setBackgroundResource(item.getLayoutColorResId());

                        // image
                        ImageLoader.getInstance().displayImage("drawable://" + item.getImageResId(), holder.image, options);
                    }
                    break;
                case 2:
                    {
                        ImageHolder holder = null;
                        if (view == null) {
                            holder = new ImageHolder();
                            view = inflater.inflate(item.getLayoutResId(), viewGroup, false);
                            holder.image = (ImageView) view.findViewById(R.id.image);
                            view.setTag(holder);
                        }
                        else {
                            holder = (ImageHolder) view.getTag();
                        }

                        //  layout color
                        view.setBackgroundResource(item.getLayoutColorResId());

                        // image
                        ImageLoader.getInstance().displayImage("drawable://" + item.getImageResId(), holder.image, options);
                    }
                    break;
                case 3:
                    {
                        TextHolder holder = null;
                        if (view == null) {
                            holder = new TextHolder();
                            view = inflater.inflate(item.getLayoutResId(), viewGroup, false);
                            holder.text = (CustomFontTextView) view.findViewById(R.id.text);
                            view.setTag(holder);
                        }
                        else {
                            holder = (TextHolder) view.getTag();
                        }

                        // layout color
                        view.setBackgroundResource(item.getLayoutColorResId());


                        // text
                        holder.text.setCustomFont(item.getTextFont());
                        holder.text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getInteger(item.getTextSizeResId()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            holder.text.setTextColor(getResources().getColor(item.getTextColorResId(), getAppContext().getTheme()));
                        else
                            holder.text.setTextColor(getResources().getColor(item.getTextColorResId()));
                        holder.text.setText(getString(item.getTextResId()));
                    }
                    break;
                case 4:
                    {
                        if (view == null)
                            view = inflater.inflate(item.getLayoutResId(), viewGroup, false);

                        // layout color
                        view.setBackgroundResource(item.getLayoutColorResId());

                        // layout height for margin
                        view.setMinimumHeight(getResources().getInteger(item.getLayoutHeightResId()));
                    }
                    break;
            }
            return view;
        }
    }
}