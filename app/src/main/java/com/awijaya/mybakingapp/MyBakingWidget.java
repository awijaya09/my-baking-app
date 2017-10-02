package com.awijaya.mybakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.awijaya.mybakingapp.Widget.RemoteFetchService;
import com.awijaya.mybakingapp.Widget.WidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class MyBakingWidget extends AppWidgetProvider {

    public static final String DATA_FETCHED = "com.awijaya.mybakingapp.DATA_FETCHED";
    private static final String TAG = "Widget Update";

    private void updateWidgetListView(Context context, int appWidgetId, AppWidgetManager appWidgetManager) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_baking_widget);
        Intent svcIntent = new Intent(context, WidgetService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.listview_widget, svcIntent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateWidgetListView(context, appWidgetId, appWidgetManager);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            Log.d(TAG, "onReceive: The widget update is invoked");
            AppWidgetManager mAppWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName mComponentName = new ComponentName(context, getClass());
            mAppWidgetManager.notifyAppWidgetViewDataChanged(mAppWidgetManager.getAppWidgetIds(mComponentName), R.id.listview_widget);
        }
    }

}

