package com.tecace.app.manager.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tecace.app.manager.R;
import com.tecace.app.manager.analytics.AnalyticsInteractionActionType;
import com.tecace.app.manager.analytics.FragmentChangeCause;
import com.tecace.app.manager.gson.model.ArgumentsModel;
import com.tecace.app.manager.gson.model.FragmentModel;
import com.tecace.app.manager.gson.model.ListItemModel;
import com.tecace.app.manager.gson.model.ListModel;
import com.tecace.app.manager.util.AppManagerConst;
import com.tecace.retail.analyticsmanager.AnalyticsManager;
import com.tecace.retail.appmanager.ui.view.CustomFontTextView;
import com.tecace.retail.appmanager.ui.view.CustomListView;
import com.tecace.retail.appmanager.util.JsonUtil;

import java.util.ArrayList;

/**
 * Created by icanmobile on 3/14/16.
 */
public abstract class BaseListFragment extends BaseFragment
        implements AdapterView.OnItemClickListener {
    private static final String TAG = BaseListFragment.class.getSimpleName();

    private FragmentModel<ListModel> mFragmentModel = null;

    private CustomListView mListView = null;
    private ListAdapter mAdapter = null;
    private ImageView mCloseBtn = null;
    private FloatingActionButton mFab = null;

    public enum CTL_POSITION {
        CTL_POSITION_DOWN,
        CTL_POSITION_UP
    };
    private FloatingActionButton mFabArrowDown = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ArgumentsModel args = (ArgumentsModel) getArguments().getSerializable(AppManagerConst.ARGUMENTS_MODEL);
            mFragmentModel = JsonUtil.getInstance().loadJsonModel(getActivity().getApplicationContext(),
                    args.getFragmentJson(), new TypeToken<FragmentModel<ListModel>>() {}.getType());
            mTransactionDir = (AppManagerConst.TransactionDir) AppManagerConst.TransactionDir.valueOf(args.getTransitionDir());
            onFragmentCreated(args);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(getFragmentModel().getLayoutResId(), container, false);

        // Floating Action Button
        mFab = (FloatingActionButton)mView.findViewById(R.id.fab);
        if (mFab != null) {
            if (getFragmentModel().getFragment().getFabColorResId() > 0)
                mFab.setBackgroundTintList(getResources().getColorStateList(getFragmentModel().getFragment().getFabColorResId()));
            if (getFragmentModel().getFragment().getFabImageResId() > 0)
                mFab.setImageResource(getFragmentModel().getFragment().getFabImageResId());
            mFab.setOnClickListener(closeBtnClick);
        }

        mFabArrowDown = (FloatingActionButton)mView.findViewById(R.id.fab_arrow_down);
        if (mFabArrowDown != null) {
            mFabArrowDown.setTag(CTL_POSITION.CTL_POSITION_DOWN.name());
            mFabArrowDown.setOnClickListener(arrowDownClick);
        }

        mListView = (CustomListView) mView.findViewById(R.id.list);
        if (mListView != null) {
            if (getFragmentModel().getFragment().getBackgroundResId() > 0)
                mListView.setBackgroundResource(getFragmentModel().getFragment().getBackgroundResId());

            mAdapter = new ListAdapter(getAppContext());
            mAdapter.setData(getFragmentModel().getFragment().getListItems());
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(this);
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                    if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                        AnalyticsManager.getInstance().notifyUserEvent(AnalyticsInteractionActionType.SWIPE.getActionType(),
                                AnalyticsInteractionActionType.Extra.SCROLL);
                    }
                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                    int offsetY = mListView.computeVerticalScrollOffset();
                    int extentY = mListView.computeVerticalScrollExtent();
                    int rangeY = mListView.computeVerticalScrollRange();

                    if( offsetY + extentY == rangeY ) {
                        rotateView(mFabArrowDown, 0f, 180f);
                        mFabArrowDown.setTag(CTL_POSITION.CTL_POSITION_UP.name());
                    }
                    else {
                        if (mFabArrowDown.getTag().equals(CTL_POSITION.CTL_POSITION_UP.name())) {
                            rotateView(mFabArrowDown, 180f, 360f);
                            mFabArrowDown.setTag(CTL_POSITION.CTL_POSITION_DOWN.name());
                        }
                    }
                }
            });
        }
        return mView;
    }

    protected View.OnClickListener closeBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (getFragmentModel() == null || getFragmentModel().getActionBackKey() == null) return;
            changeFragment(getFragmentModel().getActionBackKey(),
                    AppManagerConst.TransactionDir.TRANSACTION_DIR_BACKWARD, FragmentChangeCause.TAP);
        }
    };

    protected View.OnClickListener arrowDownClick = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            AnalyticsManager.getInstance().notifyUserEvent(AnalyticsInteractionActionType.TAP.getActionType(), AnalyticsInteractionActionType.Extra.SCROLL);

            if (view.getTag().equals(CTL_POSITION.CTL_POSITION_UP.name())) {
                mListView.smoothScrollToPositionFromTop(0, 0);
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListView.setSelection(0);
                    }
                }, 100);
            }
            else {
                int tagIndex = mListView.getLastVisiblePosition();
                for (int i = mListView.getLastVisiblePosition(); i >= mListView.getFirstVisiblePosition(); i--) {
                    ListItemModel item = (ListItemModel) mAdapter.getItem(i);
                    if (item.getTextSizeResId() > 0) {
                        int textSize = getResources().getInteger(item.getTextSizeResId());
                        if (textSize >= getResources().getInteger(R.integer.fragment_list_scroll_tag_value)) {
                            tagIndex = i - 1;   //find margin list item above tag list item
                            break;
                        }
                    }
                }

                if (tagIndex - 1 == mListView.getFirstVisiblePosition()) {
                    mListView.smoothScrollBy(getResources().getInteger(R.integer.animYOffset), 500);
                }
                else {
                    View child = mListView.getChildAt(tagIndex - mListView.getFirstVisiblePosition());
                    if (child != null)
                        mListView.smoothScrollBy((int) child.getY(), 500);
                }
            }
        }
    };

    protected void rotateView(View view, float start, float end) {
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(view, "rotation", start, end);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setDuration(100);
        rotateAnimator.start();
    }

    @Override
    public Animator onCreateAnimator(int transit, final boolean enter, final int nextAnim) {
        Animator animator = null;

//        Log.d(TAG, "##### enter = " + enter + ", nextAnim = " + nextAnim);
        if (nextAnim == 0) return null;

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

    protected FragmentModel<ListModel> getFragmentModel() {
        return mFragmentModel;
    }

    abstract public void onPageTransitionStart(boolean enter, int nextAnim, AppManagerConst.TransactionDir dir);
    abstract public void onPageTransitionEnd(boolean enter, int nextAnim, AppManagerConst.TransactionDir dir);
    abstract public void onPageTransitionCancel(boolean enter, int nextAnim, AppManagerConst.TransactionDir dir);



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
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public int getItemViewType(int position) {
//            if (items.get(position).getLayout().equals("@layout/list_item_close"))
//                return 0;
            if (items.get(position).getLayout().equals("@layout/list_item_image_center"))
                return 0;
            if (items.get(position).getLayout().equals("@layout/list_item_image_right"))
                return 1;
            if (items.get(position).getLayout().equals("@layout/list_item_text"))
                return 2;
            if (items.get(position).getLayout().equals("@layout/list_item_text_bold"))
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
//                case 0:
//                    {
//                        ImageHolder holder = null;
//                        if (view == null) {
//                            holder = new ImageHolder();
//                            view = inflater.inflate(item.getLayoutResId(), viewGroup, false);
//                            holder.image = (ImageView) view.findViewById(R.id.image);
//                            view.setTag(holder);
//                        }
//                        else {
//                            holder = (ImageHolder) view.getTag();
//                        }
//
//                        //  layout color
//                        view.setBackgroundResource(item.getLayoutColorResId());
//
//                        // image
//                        if (item.getImageResId() > 0)
//                            ImageLoader.getInstance().displayImage("drawable://" + item.getImageResId(), holder.image, options);
//
//                        mCloseBtn = holder.image;
//                        mCloseBtn.setOnClickListener(closeBtnClick);
//                    }
//                    break;
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

                        // image
                        ImageLoader.getInstance().displayImage("drawable://" + item.getImageResId(), holder.image, options);


//                        new ImageHolderAsyncTask(view, holder, item).execute();
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


//                        new ImageHolderAsyncTask(view, holder, item).execute();
                    }
                    break;
                case 2:
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
//                        holder.text.setCustomFont(item.getTextFont());
                        holder.text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getInteger(item.getTextSizeResId()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            holder.text.setTextColor(getResources().getColor(item.getTextColorResId(), getAppContext().getTheme()));
                        else
                            holder.text.setTextColor(getResources().getColor(item.getTextColorResId()));
                        holder.text.setText(getString(item.getTextResId()));


//                        new TextHolderAsyncTask(view, holder, item).execute();
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
    //                        holder.text.setCustomFont(item.getTextFont());
                        holder.text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getInteger(item.getTextSizeResId()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            holder.text.setTextColor(getResources().getColor(item.getTextColorResId(), getAppContext().getTheme()));
                        else
                            holder.text.setTextColor(getResources().getColor(item.getTextColorResId()));
                        holder.text.setText(getString(item.getTextResId()));


    //                        new TextHolderAsyncTask(view, holder, item).execute();
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


    /*
    private class ImageHolderAsyncTask extends AsyncTask<Void, Void, Bitmap> {
        private WeakReference<View> view;
        private WeakReference<ImageHolder> holder;
        private WeakReference<ListItemModel> item;

        public ImageHolderAsyncTask (View view, ImageHolder holder, ListItemModel item) {
            this.view = new WeakReference<View>(view);
            this.holder = new WeakReference<ImageHolder>(holder);
            this.item = new WeakReference<ListItemModel>(item);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            return ImageLoader.getInstance().loadImageSync("drawable://" + item.get().getImageResId());
        }

        protected void onPostExecute(Bitmap bmp) {
            this.view.get().setBackgroundResource(item.get().getLayoutColorResId());
            holder.get().image.setImageBitmap(bmp);
        }
    }



    private class TextHolderAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<View> view;
        private WeakReference<TextHolder> holder;
        private WeakReference<ListItemModel> item;

        public TextHolderAsyncTask(View view, TextHolder holder, ListItemModel item) {
            this.view = new WeakReference<View>(view);
            this.holder = new WeakReference<TextHolder>(holder);
            this.item = new WeakReference<ListItemModel>(item);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return true;
        }

        protected void onPostExecute(Boolean test) {
            view.get().setBackgroundResource(item.get().getLayoutColorResId());

            holder.get().text.setCustomFont(item.get().getTextFont());
            holder.get().text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getInteger(item.get().getTextSizeResId()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                holder.get().text.setTextColor(getResources().getColor(item.get().getTextColorResId(), getAppContext().getTheme()));
            else
                holder.get().text.setTextColor(getResources().getColor(item.get().getTextColorResId()));
            holder.get().text.setText(getString(item.get().getTextResId()));
        }
    }
    */
}