package com.awijaya.mybakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.awijaya.mybakingapp.Model.Ingredient;
import com.awijaya.mybakingapp.Model.Recipe;
import com.awijaya.mybakingapp.Model.Step;
import com.awijaya.mybakingapp.Networking.RetrofitClient;
import com.awijaya.mybakingapp.Networking.RetrofitInterface;
import com.awijaya.mybakingapp.Networking.SharedNetworking;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by awijaya on 9/17/17.
 */

public class RecipeListFragment extends Fragment implements HomeListAdapter.HomeListAdapterOnClickHandler {

    @BindView(R.id.rv_main_list)
    RecyclerView mRecyclerView;

    private RetrofitInterface mInterface;
    private static final String TAG = "Retrofit Callback";
    private static final String RECIPE_LIST_KEY = "recipeList";
    private ArrayList<Recipe> mDataSources = new ArrayList<Recipe>();
    private boolean isDownloading = true;
    private LinearLayoutManager mLinearManager;

    OnItemClickListener mCallback;

    @Override
    public void onItemClick(Recipe item) {
        mCallback.onItemSelected(item);
    }

    public interface OnItemClickListener {
        void onItemSelected(Recipe recipe);
    }

    public RecipeListFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnItemClickListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: error " + e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            mDataSources = savedInstanceState.getParcelableArrayList(RECIPE_LIST_KEY);
            isDownloading = false;
            setCallBackForEachRecipe();
            showRecipeList();
        }

        setCallBackForEachRecipe();
        showRecipeList();
        return rootView;
    }
    public void setRecipeList(ArrayList<Recipe> recipeList){
        this.mDataSources = recipeList;
    }

    public ArrayList<Recipe> getRecipeList(){
        return this.mDataSources;
    }

    public void setCallBackForEachRecipe(){
        HomeListAdapter mAdapter = new HomeListAdapter(mDataSources, this);
        mLinearManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLinearManager);
        mRecyclerView.setHasFixedSize(true);
    }

    public void showRecipeList(){
        isDownloading = false;
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(RECIPE_LIST_KEY, mDataSources);
    }
}
