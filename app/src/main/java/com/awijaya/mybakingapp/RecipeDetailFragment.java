package com.awijaya.mybakingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.awijaya.mybakingapp.Model.Ingredient;
import com.awijaya.mybakingapp.Model.Recipe;
import com.awijaya.mybakingapp.Model.Step;
import com.awijaya.mybakingapp.Networking.RetrofitClient;
import com.awijaya.mybakingapp.Networking.RetrofitInterface;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awijaya on 9/18/17.
 */

public class RecipeDetailFragment extends Fragment {

    private static final String TAG = "Recipe Fragment";
    private static final String RECIPE_KEY = "recipe";
    public static final String INDEX_KEY = "curIndex";
    public static final String ING_KEY = "ingKey";
    public static final String PLAYER_POS = "playerPos";
    private int curIndex = 0;

    @BindView(R.id.rv_detail_list)
    RecyclerView mRecyclerViewDetail;

    @BindView(R.id.text_view_steps_title)
    TextView mStepTitle;

    @BindView(R.id.btn_previous_steps)
    Button mPrevBtn;

    @BindView(R.id.btn_next_steps)
    Button mNextBtn;

    private Recipe mRecipe;
    private LinearLayoutManager mAdapter;
    private RecipeDetailListAdapter recipeAdapter;
    private ArrayList<Step> mSteps;
    private ArrayList<Ingredient> mIngredients;
    private long curPlayerPos = 0l;

    public RecipeDetailFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
            curIndex = savedInstanceState.getInt(INDEX_KEY);
            curPlayerPos = savedInstanceState.getLong(PLAYER_POS);
        }

        mAdapter = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recipeAdapter = new RecipeDetailListAdapter(getContext(), mRecipe);
        recipeAdapter.setPlayerCurrentPost(curPlayerPos);
        mRecyclerViewDetail.setAdapter(recipeAdapter);
        mRecyclerViewDetail.setLayoutManager(mAdapter);
        mRecyclerViewDetail.setHasFixedSize(true);

        mSteps = mRecipe.recipeSteps;
        mIngredients = mRecipe.recipeIngredients;

        updateStepsTitle(mSteps.get(curIndex));

        if(curIndex == 0){
            mPrevBtn.setEnabled(false);
        }
        setNextBtnClick();
        setPrevBtnClick();
        return rootView;
    }

    private void setNextBtnClick(){
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curIndex++;
                Uri mediaUri = Uri.parse(mSteps.get(curIndex).stepVideoURL);
                recipeAdapter.playNextVideo(mediaUri);

                updateStepsTitle(mSteps.get(curIndex));
            }
        });
    }

    private void setPrevBtnClick() {
        mPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curIndex > 0) {
                    curIndex--;
                    Uri mediaUri = Uri.parse(mSteps.get(curIndex).stepVideoURL);
                    recipeAdapter.playNextVideo(mediaUri);

                    updateStepsTitle(mSteps.get(curIndex));
                }
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE_KEY, mRecipe);
        outState.putInt(INDEX_KEY, curIndex);
        outState.putParcelableArrayList(ING_KEY, mIngredients);
        outState.putLong(PLAYER_POS, recipeAdapter.getPlayerCurrentPost());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recipeAdapter.releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        recipeAdapter.releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        recipeAdapter.initializeMedia();
    }
}
