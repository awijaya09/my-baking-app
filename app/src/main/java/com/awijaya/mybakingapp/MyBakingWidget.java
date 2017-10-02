package com.awijaya.mybakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
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

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_baking_widget);
        Intent svcIntent = new Intent(context, WidgetService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.listview_widget, svcIntent);
        return remoteViews;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Intent serviceIntent = new Intent(context, RemoteFetchService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            context.startService(serviceIntent);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals(DATA_FETCHED)) {
            Log.d(TAG, "onReceive: The widget update is invoked");
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
            CharSequence widgetText = context.getString(R.string.appwidget_text);
            remoteViews.setTextViewText(R.id.text_view_title, widgetText);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

}

