package com.samsung.retailexperience.retailhero.widget;

/**
 * Created by MONSTER on 1/19/2016.
 */
public class WidgetProvider3 extends WidgetProviderBase {
    private static final int FEATURE_INDEX = 2;

    @Override
    int getItemDescription() {
        return WidgetProviderConstants.featureDescriptions.get(FEATURE_INDEX);
    }

    @Override
    Class<?> getExplorerClass() {
        return WidgetProviderConstants.featureExplorers.get(FEATURE_INDEX);
    }

    @Override
    String getFragmentName() {
        return WidgetProviderConstants.featureExplorerFragments.get(FEATURE_INDEX);
    }

}
