package com.awijaya.mybakingapp.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.awijaya.mybakingapp.MainActivity;
import com.awijaya.mybakingapp.Model.Ingredient;
import com.awijaya.mybakingapp.Model.Recipe;
import com.awijaya.mybakingapp.Networking.SharedNetworking;
import com.awijaya.mybakingapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by awijaya on 10/1/17.
 */

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = "ListProvider";
    private Collection<Ingredient> mIngredients;
    //private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Recipe tRecipe;
    private Context mContext = null;

    public ListProvider(Context context) {
        this.mContext = context;
    }


    @Override
    public void onCreate() {
        mIngredients = new ArrayList<Ingredient>();
    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences preferences = mContext.getSharedPreferences("com.awijaya.data", Context.MODE_PRIVATE);
        String json = preferences.getString("saved_recipe", "");
        if (!json.equals("")) {
            Gson gson = new Gson();
            Type collectionType = new TypeToken<Collection<Ingredient>>() {
            }.getType();
            mIngredients = gson.fromJson(json, collectionType);
        }
    }

    @Override
    public void onDestroy() {
        mIngredients.clear();
    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount: Ingredient Size = " + mIngredients.size());
        return (mIngredients == null) ? 0 : mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (mIngredients != null) {
            final RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.list_view_ingredient);

            Ingredient sIngredient = (Ingredient) mIngredients.toArray()[i];
            remoteView.setTextViewText(R.id.text_view_ingredient, sIngredient.ingredientName);

            return remoteView;
        }
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
