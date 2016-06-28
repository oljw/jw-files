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
    static int FEATURE_COUNT = 1;
    static List<Integer> featureDescriptions = new ArrayList<>(FEATURE_COUNT);
    static {
        featureDescriptions.add(R.drawable.widget_whats_new);
    }

    static List<Class<?>> featureExplorers = new ArrayList<>(FEATURE_COUNT);
    static {
        featureExplorers.add(BaseActivity.class);
    }

    static List<String> featureExplorerFragments = new ArrayList<>(FEATURE_COUNT);
    static {
        featureExplorerFragments.add(AppConst.UIState.UI_STATE_WHATS_NEW_MAIN.name());
    }
}
