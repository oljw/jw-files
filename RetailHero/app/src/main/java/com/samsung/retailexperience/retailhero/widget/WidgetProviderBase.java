package com.samsung.retailexperience.retailhero.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;

import java.util.Objects;

/**
 * Created by MONSTER on 1/19/2016.
 */
public abstract class WidgetProviderBase extends AppWidgetProvider {
    private static final String EXPLORER_CLICKED = "com.samsung.retailexperience.retailhero.widget.WidgetProviderBase.ExplorerClicked";
    private static final String LAUNCH_CLASS = "launchClass";
    private static final String LAUNCH_FRAGMENT_NAME = "launchFragmentName";

//    public static final String ACTION_CHANGE_FRAGMENT = "com.samsung.retailexperience.retailhero.receiver.action.CHANGE_FRAGMENT";
//    public static final String ARG_NEXT_FRAGMENT = "ARG_NEXT_FRAGMENT";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.homewidget_base);

            updateItemDescription(remoteViews);
            updateExplorerIntent(context, remoteViews);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private void updateItemDescription(RemoteViews remoteViews) {
        int itemDescription = getItemDescription();
        remoteViews.setImageViewResource(R.id.item_description, itemDescription);
    }

    private void updateExplorerIntent(Context context, RemoteViews remoteViews) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(EXPLORER_CLICKED);
        // WIDGET_NEW_ACTIVITY
//        Class<?> cls = getExplorerClass();
//        intent.putExtra(LAUNCH_CLASS, cls);
        String fragmentName = getFragmentName();
        intent.putExtra(LAUNCH_FRAGMENT_NAME, fragmentName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // explorer button is too small, make all widget clickable
        //remoteViews.setOnClickPendingIntent(R.id.item_explorer, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.widget_root, pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(EXPLORER_CLICKED, intent.getAction())) {
            String fragmentName = intent.getStringExtra(LAUNCH_FRAGMENT_NAME);

            Intent i = new Intent(AppConsts.ACTION_CHANGE_FRAGMENT);
            i.putExtra(AppConsts.ARG_NEXT_FRAGMENT, fragmentName);
            context.sendBroadcast(i);

            // WIDGET_NEW_ACTIVITY
//            Class<?> cls = (Class<?>)intent.getSerializableExtra(LAUNCH_CLASS);
//            Intent i = new Intent(context, cls);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
        } else {
            super.onReceive(context, intent);
        }
    }

    abstract int getItemDescription();
    abstract Class<?> getExplorerClass();
    abstract String getFragmentName();
}
