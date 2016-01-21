package com.samsung.retailexperience.retailhero.widget;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.ui.activity.BaseActivity;
import com.samsung.retailexperience.retailhero.util.AppConst;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MONSTER on 1/19/2016.
 */
public class WidgetProviderConstants {
    static int FEATURE_COUNT = 5;
    static List<Integer> featureDescriptions = new ArrayList<>(FEATURE_COUNT);
    static {
        featureDescriptions.add(R.drawable.widget_item_desc_1);
        featureDescriptions.add(R.drawable.widget_item_desc_1);
        featureDescriptions.add(R.drawable.widget_item_desc_1);
        featureDescriptions.add(R.drawable.widget_item_desc_1);
        featureDescriptions.add(R.drawable.widget_item_desc_1);
    }

    static List<Class<?>> featureExplorers = new ArrayList<>(FEATURE_COUNT);
    static {
        featureExplorers.add(BaseActivity.class);
        featureExplorers.add(BaseActivity.class);
        featureExplorers.add(BaseActivity.class);
        featureExplorers.add(BaseActivity.class);
        featureExplorers.add(BaseActivity.class);
    }

    // TODO Make seperate
    static List<String> featureExplorerFragments = new ArrayList<>(FEATURE_COUNT);
    static {
        featureExplorerFragments.add(AppConst.UIState.UI_STATE_WHATS_NEW_MAIN.name());
        featureExplorerFragments.add(AppConst.UIState.UI_STATE_WHATS_NEW_MAIN.name());
        featureExplorerFragments.add(AppConst.UIState.UI_STATE_EXCLUSIVES_MAIN.name());
        featureExplorerFragments.add(AppConst.UIState.UI_STATE_WHATS_NEW_MAIN.name());
        featureExplorerFragments.add(AppConst.UIState.UI_STATE_WHATS_NEW_MAIN.name());
    }
}
