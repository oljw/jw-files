package com.samsung.retailexperience.retailhero.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by MONSTER on 1/20/2016.
 */
public abstract class CustomizedGalleryAdapter extends BaseAdapter {

    @Override
    public final View getView(int i, View reusableView, ViewGroup viewGroup) {
        View wrappedView = null;
        CustomizedGalleryItemWrapper coverFlowItem;

        if (reusableView != null) {
            coverFlowItem = (CustomizedGalleryItemWrapper) reusableView;
            wrappedView = coverFlowItem.getChildAt(0);
            coverFlowItem.removeAllViews();
        } else {
            coverFlowItem = new CustomizedGalleryItemWrapper(viewGroup.getContext());
        }

        wrappedView = this.getCoverFlowItem(i, wrappedView, viewGroup);

        if (wrappedView == null) {
            throw new NullPointerException("getCoverFlowItem() was expected to return a view, but null was returned.");
        }

        coverFlowItem.addView(wrappedView);
        coverFlowItem.setLayoutParams(wrappedView.getLayoutParams());

        return coverFlowItem;
    }

    // =============================================================================
    // Abstract methods
    // =============================================================================
    public abstract View getCoverFlowItem(int position, View reusableView, ViewGroup parent);

}