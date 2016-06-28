package com.samsung.retailexperience.retailhero.view;

import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseFragment;

/**
 * Created by icanmobile on 1/21/16.
 */
public class PaginationViewItem <T> {
    private static final String TAG = PaginationViewItem.class.getSimpleName();

    private BaseFragment fragment;
    private FragmentModel<T> model;
    private String uiState;

    public PaginationViewItem(BaseFragment fragment, FragmentModel<T> model, String uiState) {
        this.fragment = fragment;
        this.model = model;
        this.uiState = uiState;
    }

    public BaseFragment getFragment() {
        return this.fragment;
    }
    public void setFragment(BaseFragment fragment) {
        this.fragment = fragment;
    }

    public FragmentModel<T> getModel() {
        return this.model;
    }
    public void setModel(FragmentModel<T> model) {
        this.model = model;
    }

    public String getUIState() {
        return this.uiState;
    }
    public void setUiState(String uiState) {
        this.uiState = uiState;
    }
}
