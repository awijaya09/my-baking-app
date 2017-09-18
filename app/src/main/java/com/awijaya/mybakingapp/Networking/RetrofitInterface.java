package com.awijaya.mybakingapp.Networking;

import com.awijaya.mybakingapp.Model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by awijaya on 9/18/17.
 */

public interface RetrofitInterface {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<Recipe>> getRecipeList();
}
