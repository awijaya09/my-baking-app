package com.awijaya.mybakingapp.Networking;

import android.util.Log;

import com.awijaya.mybakingapp.Model.Ingredient;
import com.awijaya.mybakingapp.Model.Recipe;
import com.awijaya.mybakingapp.Model.Step;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by awijaya on 9/30/17.
 */

public class SharedNetworking {

    private static RetrofitInterface mInterface;
    private static final String TAG = "Shared Networking";

    public static ArrayList<Recipe> downloadRcipeList(Callback<ArrayList<Recipe>> callback) {

        final ArrayList<Recipe> mDataSources = new ArrayList<Recipe>();
        mInterface = RetrofitClient.getClient().create(RetrofitInterface.class);
        retrofit2.Call<ArrayList<Recipe>> call = mInterface.getRecipeList();
        call.enqueue(callback);

        return mDataSources;

    }



}
