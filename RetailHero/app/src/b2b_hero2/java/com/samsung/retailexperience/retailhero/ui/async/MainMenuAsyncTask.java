package com.samsung.retailexperience.retailhero.ui.async;

import android.app.FragmentManager;
import android.os.AsyncTask;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.MenuModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.MenuFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.ModelUtil;
import com.samsung.retailexperience.retailhero.view.PaginationView;
import com.samsung.retailexperience.retailhero.view.PaginationViewItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by icanmobile on 1/27/16.
 */
public class MainMenuAsyncTask extends AsyncTask<String, Void, ArrayList<PaginationViewItem<MenuModel>> > {
    private WeakReference<PaginationView<MenuModel>> parentReference;
    private WeakReference<FragmentManager> fragmentManagerReference;
    private WeakReference<PaginationView.PaginationViewListener> listenerReference;
    private String uiState = null;

    public MainMenuAsyncTask(PaginationView<MenuModel> parent, FragmentManager fragmentManager, PaginationView.PaginationViewListener listener) {
        parentReference = new WeakReference<PaginationView<MenuModel>>(parent);
        fragmentManagerReference = new WeakReference<FragmentManager>(fragmentManager);
        listenerReference = new WeakReference<PaginationView.PaginationViewListener>(listener);
    }


    @Override
    protected ArrayList<PaginationViewItem<MenuModel>> doInBackground(String... params) {
        if (fragmentManagerReference.get() == null) return null;

        uiState = params[0];
        ArrayList<PaginationViewItem<MenuModel>> pages = new ArrayList<PaginationViewItem<MenuModel>>();
        {
            FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
            fragmentModel.setActionBackKey("UI_STATE_DECISION");
            fragmentModel.setBackground("@color/white");
            fragmentModel.setDrawerId(AppConst.UIState.UI_STATE_NEW_TO_ANDROID_MAIN.name());
            fragmentModel.setFragment(ModelUtil.getMenuModel(
                    "@string/new_to_android_demo_title",                //title
                    "@string/new_to_android_demo_subtitle",
                    "models/new_to_android_menu.json", //menu for ListView
                    null, null, null, null,
                    "5", "5"));

            fragmentModel.setLayout("@layout/fragment_menu");
            BaseFragment fragment = MenuFragment.newInstance(fragmentModel);

            fragmentManagerReference.get().beginTransaction()
                    .add(R.id.paginationContainer, fragment)
                    .commit();

            pages.add(new PaginationViewItem<MenuModel>(fragment,
                    fragmentModel,
                    AppConst.UIState.UI_STATE_NEW_TO_ANDROID_MAIN.name()));
        }

        {
            FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
            fragmentModel.setActionBackKey("UI_STATE_DECISION");
            fragmentModel.setBackground("@color/white");
            fragmentModel.setDrawerId(AppConst.UIState.UI_STATE_EXCLUSIVES_MAIN.name());
            fragmentModel.setFragment(ModelUtil.getMenuModel(
                    "@string/ss_exclusives_demo_title",                //title
                    "@string/ss_exclusives_demo_subtitle",
                    "models/samsung_exclusives.json",                  //menu for ListView
                    "@string/ss_exclusives_demo_video_title",
                    "@string/ss_exclusives_demo_video_subtitle",
                    "@drawable/matts_story_background",
                    "UI_STATE_EXCLUSIVES_DEMO_VIDEO",
                    "5", "4"));

            fragmentModel.setLayout("@layout/fragment_menu");
            BaseFragment fragment = MenuFragment.newInstance(fragmentModel);

            fragmentManagerReference.get().beginTransaction()
                    .add(R.id.paginationContainer, fragment)
                    .commit();

            pages.add(new PaginationViewItem<MenuModel>(fragment,
                    fragmentModel,
                    AppConst.UIState.UI_STATE_EXCLUSIVES_MAIN.name()));
        }

        {
            FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
            fragmentModel.setActionBackKey("UI_STATE_DECISION");
            fragmentModel.setBackground("@color/white");
            fragmentModel.setDrawerId(AppConst.UIState.UI_STATE_DESIGN_MAIN.name());
            fragmentModel.setFragment(ModelUtil.getMenuModel(
                    "@string/design_demo_title",                //title
                    "@string/design_demo_subtitle",
                    "models/design_menu.json",                  //menu for ListView
                    "@string/design_demo_video_title",
                    "@string/design_demo_video_subtitle",
                    "@drawable/matts_story_background",
                    "UI_STATE_DESIGN_DEMO_VIDEO",
                    "5", "3"));

            fragmentModel.setLayout("@layout/fragment_menu");
            BaseFragment fragment = MenuFragment.newInstance(fragmentModel);

            fragmentManagerReference.get().beginTransaction()
                    .add(R.id.paginationContainer, fragment)
                    .commit();

            pages.add(new PaginationViewItem<MenuModel>(fragment,
                    fragmentModel,
                    AppConst.UIState.UI_STATE_DESIGN_MAIN.name()));
        }

        {
            FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
            fragmentModel.setActionBackKey("UI_STATE_DECISION");
            fragmentModel.setBackground("@color/white");
            fragmentModel.setDrawerId(AppConst.UIState.UI_STATE_PRODUCTIVITY_MAIN.name());
            fragmentModel.setFragment(ModelUtil.getMenuModel(
                    "@string/productivity_demo_title",                //title
                    "@string/productivity_demo_subtitle",
                    "models/productivity_menu.json",                  //menu for ListView
                    "@string/productivity_demo_video_title",
                    "@string/productivity_demo_video_subtitle",
                    "@drawable/matts_story_background",
                    "UI_STATE_PRODUCTIVITY_DEMO_VIDEO",
                    "5", "2"));

            fragmentModel.setLayout("@layout/fragment_menu");
            BaseFragment fragment = MenuFragment.newInstance(fragmentModel);

            fragmentManagerReference.get().beginTransaction()
                    .add(R.id.paginationContainer, fragment)
                    .commit();

            pages.add(new PaginationViewItem<MenuModel>(fragment,
                    fragmentModel,
                    AppConst.UIState.UI_STATE_PRODUCTIVITY_MAIN.name()));
        }

        {
            FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
            fragmentModel.setActionBackKey("UI_STATE_DECISION");
            fragmentModel.setBackground("@color/white");
            fragmentModel.setDrawerId(AppConst.UIState.UI_STATE_WHATS_NEW_MAIN.name());
            fragmentModel.setFragment(ModelUtil.getMenuModel(
                    "@string/whats_new_demo_title",                //title
                    "@string/whats_new_demo_subtitle",
                    "models/whats_new_menu.json", //menu for ListView
                    null, null, null, null,
                    "5", "1"));

            fragmentModel.setLayout("@layout/fragment_menu");
            BaseFragment fragment = MenuFragment.newInstance(fragmentModel);

            fragmentManagerReference.get().beginTransaction()
                    .add(R.id.paginationContainer, fragment)
                    .commit();

            pages.add(new PaginationViewItem<MenuModel>(fragment,
                    fragmentModel,
                    AppConst.UIState.UI_STATE_WHATS_NEW_MAIN.name()));
        }
        return pages;
    }

    protected void onPostExecute(ArrayList<PaginationViewItem<MenuModel>> pages) {
        if (parentReference != null && listenerReference != null && pages != null) {
            final PaginationView<MenuModel> parent = parentReference.get();
            parent.setPages(pages, uiState);
            parent.setListener(listenerReference.get());
        }
    }
}
