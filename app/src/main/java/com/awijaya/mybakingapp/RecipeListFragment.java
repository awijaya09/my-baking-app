package com.awijaya.mybakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.awijaya.mybakingapp.Model.Ingredient;
import com.awijaya.mybakingapp.Model.Recipe;
import com.awijaya.mybakingapp.Model.Step;
import com.awijaya.mybakingapp.Networking.RetrofitClient;
import com.awijaya.mybakingapp.Networking.RetrofitInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by awijaya on 9/17/17.
 */

public class RecipeListFragment extends Fragment {

    @BindView(R.id.list_view_main)
    ListView mListView;

    @BindView(R.id.progress_bar_mainlist_fragment)
    ProgressBar mProgressBar;

    private RetrofitInterface mInterface;
    private static final String TAG = "Retrofit Callback";
    private ArrayList<Recipe> mDataSources = new ArrayList<Recipe>() ;
    private boolean isDownloading = true;

    public RecipeListFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, rootView);

        if (isDownloading) {
            mProgressBar.animate();
        }

        mInterface = RetrofitClient.getClient().create(RetrofitInterface.class);
        downloadRcipeList();

        return rootView;
    }

    private void downloadRcipeList(){
        Call<ArrayList<Recipe>> call = mInterface.getRecipeList();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                Log.d(TAG, "onResponse: Getting data from JSON");

                ArrayList<Recipe> recipeList = response.body();
                for (Recipe recipeItem: recipeList){
                    mDataSources.add(recipeItem);
                    Log.d(TAG, "onResponse: The recipe results: "+ recipeItem.recipeName + "Recipe ID: " + recipeItem.recipeId);
                    ArrayList<Ingredient> ingredients = recipeItem.recipeIngredients;
                    ArrayList<Step> steps = recipeItem.recipeSteps;
                    Log.d(TAG, "onResponse: Number of ingredients: " + ingredients.size() + "Number of Steps: " + steps.size());
                }

                HomeListViewAdapter adapter = new HomeListViewAdapter(getContext(), mDataSources);
                mListView.setAdapter(adapter);

                isDownloading = false;
                mProgressBar.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.e(TAG, "onFailure: Unable to get data from JSON" + t.getMessage());
                call.cancel();
            }
        });
    }
}
