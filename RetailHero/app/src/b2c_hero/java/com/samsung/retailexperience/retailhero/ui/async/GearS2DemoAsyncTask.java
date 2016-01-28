package com.samsung.retailexperience.retailhero.ui.async;

import android.app.FragmentManager;
import android.os.AsyncTask;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.gson.models.BaseModel;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.GearS2Fragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.view.PaginationView;
import com.samsung.retailexperience.retailhero.view.PaginationViewItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by icanmobile on 1/27/16.
 */
public class GearS2DemoAsyncTask extends AsyncTask<String, Void, ArrayList<PaginationViewItem<BaseModel>> > {
    private WeakReference<PaginationView<BaseModel>> parentReference;
    private WeakReference<FragmentManager> fragmentManagerReference;
    private WeakReference<PaginationView.PaginationViewListener> listenerReference;
    private String uiState = null;

    public GearS2DemoAsyncTask(PaginationView<BaseModel> parent, FragmentManager fragmentManager, PaginationView.PaginationViewListener listener) {
        parentReference = new WeakReference<PaginationView<BaseModel>>(parent);
        fragmentManagerReference = new WeakReference<FragmentManager>(fragmentManager);
        listenerReference = new WeakReference<PaginationView.PaginationViewListener>(listener);
    }

    @Override
    protected ArrayList<PaginationViewItem<BaseModel>> doInBackground(String... params) {
        if (fragmentManagerReference.get() == null) return null;

        uiState = params[0];
        ArrayList<PaginationViewItem<BaseModel>> pages = new ArrayList<PaginationViewItem<BaseModel>>();
        {
            FragmentModel<BaseModel> fragmentModel = new FragmentModel<BaseModel>();
            fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
            fragmentModel.setBackground("@drawable/gear_s2_demo_5_bg");
            fragmentModel.setFragment(new BaseModel());
            fragmentModel.setLayout("@layout/fragment_gear_stwo");
            BaseFragment fragment = GearS2Fragment.newInstance(fragmentModel);

            fragmentManagerReference.get().beginTransaction()
                    .add(R.id.gearS2DemoPaginationContainer, fragment)
                    .commit();

            pages.add(new PaginationViewItem<BaseModel>(fragment,
                    fragmentModel,
                    AppConst.UIState.UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_5.name()));
        }

        {
            FragmentModel<BaseModel> fragmentModel = new FragmentModel<BaseModel>();
            fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
            fragmentModel.setBackground("@drawable/gear_s2_demo_4_bg");
            fragmentModel.setFragment(new BaseModel());
            fragmentModel.setLayout("@layout/fragment_gear_stwo");
            BaseFragment fragment = GearS2Fragment.newInstance(fragmentModel);

            fragmentManagerReference.get().beginTransaction()
                    .add(R.id.gearS2DemoPaginationContainer, fragment)
                    .commit();

            pages.add(new PaginationViewItem<BaseModel>(fragment,
                    fragmentModel,
                    AppConst.UIState.UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_4.name()));
        }

        {
            FragmentModel<BaseModel> fragmentModel = new FragmentModel<BaseModel>();
            fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
            fragmentModel.setBackground("@drawable/gear_s2_demo_3_bg");
            fragmentModel.setFragment(new BaseModel());
            fragmentModel.setLayout("@layout/fragment_gear_stwo");
            BaseFragment fragment = GearS2Fragment.newInstance(fragmentModel);

            fragmentManagerReference.get().beginTransaction()
                    .add(R.id.gearS2DemoPaginationContainer, fragment)
                    .commit();

            pages.add(new PaginationViewItem<BaseModel>(fragment,
                    fragmentModel,
                    AppConst.UIState.UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_3.name()));
        }

        {
            FragmentModel<BaseModel> fragmentModel = new FragmentModel<BaseModel>();
            fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
            fragmentModel.setBackground("@drawable/gear_s2_demo_2_bg");
            fragmentModel.setFragment(new BaseModel());
            fragmentModel.setLayout("@layout/fragment_gear_stwo");
            BaseFragment fragment = GearS2Fragment.newInstance(fragmentModel);

            fragmentManagerReference.get().beginTransaction()
                    .add(R.id.gearS2DemoPaginationContainer, fragment)
                    .commit();

            pages.add(new PaginationViewItem<BaseModel>(fragment,
                    fragmentModel,
                    AppConst.UIState.UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_2.name()));
        }

        {
            FragmentModel<BaseModel> fragmentModel = new FragmentModel<BaseModel>();
            fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
            fragmentModel.setBackground("@drawable/gear_s2_demo_1_bg");
            fragmentModel.setFragment(new BaseModel());
            fragmentModel.setLayout("@layout/fragment_gear_stwo");
            BaseFragment fragment = GearS2Fragment.newInstance(fragmentModel);

            fragmentManagerReference.get().beginTransaction()
                    .add(R.id.gearS2DemoPaginationContainer, fragment)
                    .commit();

            pages.add(new PaginationViewItem<BaseModel>(fragment,
                    fragmentModel,
                    AppConst.UIState.UI_STATE_EXCLUSIVE_DEMO_GEAR_S2.name()));
        }
        return pages;
    }

    protected void onPostExecute(ArrayList<PaginationViewItem<BaseModel>> pages) {
        if (parentReference != null && listenerReference != null && pages != null) {
            final PaginationView<BaseModel> parent = parentReference.get();
            parent.setPages(pages, uiState);
            parent.setListener(listenerReference.get());
        }
    }
}
