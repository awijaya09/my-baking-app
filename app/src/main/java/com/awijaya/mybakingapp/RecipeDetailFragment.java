package com.awijaya.mybakingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.awijaya.mybakingapp.Model.Ingredient;
import com.awijaya.mybakingapp.Model.Recipe;
import com.awijaya.mybakingapp.Model.Step;
import com.awijaya.mybakingapp.Networking.RetrofitClient;
import com.awijaya.mybakingapp.Networking.RetrofitInterface;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awijaya on 9/18/17.
 */

public class RecipeDetailFragment extends Fragment {

    private static final String TAG = "Recipe Fragment";
    private int curIndex = 0;

    @BindView(R.id.list_view_recipe_steps)
    ListView mListViewSteps;

    @BindView(R.id.text_view_steps_title)
    TextView mStepTitle;

    @BindView(R.id.btn_previous_steps)
    Button mPrevBtn;

    @BindView(R.id.btn_next_steps)
    Button mNextBtn;

    private Recipe mRecipe;
    private RecipeDetailListViewAdapter mAdapter;
    private ArrayList<Step> mSteps;

    public RecipeDetailFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        mAdapter = new RecipeDetailListViewAdapter(getContext(), mRecipe, curIndex);
        mListViewSteps.setAdapter(mAdapter);
        mSteps = mRecipe.recipeSteps;

        updateStepsTitle(mSteps.get(curIndex));

        if(curIndex == 0){
            mPrevBtn.setEnabled(false);
        }
        setNextBtnClick();
        return rootView;
    }

    private void setNextBtnClick(){
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curIndex++;
                Uri mediaUri = Uri.parse(mSteps.get(curIndex).stepVideoURL);
                mAdapter.playNextVideo(mediaUri);

                updateStepsTitle(mSteps.get(curIndex));
            }
        });
    }

    private void updateStepsTitle(Step newStep){
        mStepTitle.setText(newStep.stepShortDescription);
    }

    public void setRecipeItem(Recipe recipe){
        this.mRecipe = recipe;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter.releasePlayer();
    }
}
