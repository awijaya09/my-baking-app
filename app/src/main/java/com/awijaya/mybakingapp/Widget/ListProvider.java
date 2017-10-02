package com.awijaya.mybakingapp.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.awijaya.mybakingapp.Model.Ingredient;
import com.awijaya.mybakingapp.Model.Recipe;
import com.awijaya.mybakingapp.Networking.SharedNetworking;
import com.awijaya.mybakingapp.R;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by awijaya on 10/1/17.
 */

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = "List Provider caller";
    private ArrayList<Ingredient> mIngredients = new ArrayList<Ingredient>();
    private Context mContext = null;

    public ListProvider(Context context) {
        this.mContext = context;
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        SharedNetworking.downloadRcipeList(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                ArrayList<Recipe> mRecipe = new ArrayList<>();
                ArrayList<Recipe> recipeList = response.body();
                for (Recipe recipeItem : recipeList) {
                    mRecipe.add(recipeItem);
                }
                Random r = new Random();
                int recipeIndex = r.nextInt(mRecipe.size());
                mIngredients = mRecipe.get(recipeIndex).recipeIngredients;
                Log.d(TAG, "onResponse: ingredients recieved " + mIngredients.size());
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.e("ListProvider", "onFailure: Unable to get data from JSON" + t.getMessage());
                call.cancel();
            }
        });
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return (mIngredients == null) ? 0 : mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        final RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.list_view_ingredient);
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
