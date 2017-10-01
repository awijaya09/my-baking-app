package com.awijaya.mybakingapp.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.awijaya.mybakingapp.Model.Ingredient;
import com.awijaya.mybakingapp.R;

import java.util.ArrayList;

/**
 * Created by awijaya on 10/1/17.
 */

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<Ingredient> mIngredients = new ArrayList<Ingredient>();
    private Context mContext = null;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent) {
        this.mContext = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem();
    }

    private void populateListItem(){

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        final RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(), R.layout.list_view_ingredient);
        Ingredient sIngredient = mIngredients.get(i);
        remoteView.setTextViewText(R.id.text_view_ingredient, sIngredient.ingredientName);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
