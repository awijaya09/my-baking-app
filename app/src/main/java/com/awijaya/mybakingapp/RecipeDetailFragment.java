package com.awijaya.mybakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.awijaya.mybakingapp.Model.Recipe;
import com.awijaya.mybakingapp.Networking.RetrofitClient;
import com.awijaya.mybakingapp.Networking.RetrofitInterface;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awijaya on 9/18/17.
 */

public class RecipeDetailFragment extends Fragment {

    @BindView(R.id.list_view_recipe_steps)
    ListView mListViewSteps;

    private Recipe mRecipe;

    public RecipeDetailFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        if(getArguments() != null){
            mRecipe = this.getArguments().getBundle(MainActivity.RECIPE_BUNDLE_KEY).getParcelable(MainActivity.RECIPE_BUNDLE_KEY);
        }

        setupDetailView();
        return rootView;
    }

    private void setupDetailView(){
        RecipeDetailListViewAdapter adapter = new RecipeDetailListViewAdapter(getContext(), mRecipe);
        mListViewSteps.setAdapter(adapter);

    }
}
