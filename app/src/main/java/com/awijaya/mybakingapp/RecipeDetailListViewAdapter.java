package com.awijaya.mybakingapp;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.awijaya.mybakingapp.Model.Ingredient;
import com.awijaya.mybakingapp.Model.Recipe;
import com.awijaya.mybakingapp.Model.Step;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awijaya on 9/18/17.
 */

public class RecipeDetailListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Step> mSteps;
    private ArrayList<Ingredient> mIngredient = new ArrayList<Ingredient>();
    private Recipe mRecipe;

    public RecipeDetailListViewAdapter(Context context, Recipe recipe){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRecipe = recipe;
        mIngredient = recipe.recipeIngredients;
    }


    @Override
    public int getCount() {
        if( mIngredient != null) {
            return mIngredient.size()+2;
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (i > 1){
            return mIngredient.get(i-2);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position > 1) {
            return 3;
        } else {
            return position;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (position == 0) {
            VideoViewHolder videoViewHolder;

            if (view != null){
                videoViewHolder = (VideoViewHolder) view.getTag();
            } else {
                view = inflater.inflate(R.layout.recipe_detail_video, viewGroup, false);
                videoViewHolder = new VideoViewHolder(view);
                view.setTag(videoViewHolder);
            }

            return view;

        } else if (position == 1) {
            StepsViewHolder stepsViewHolder;

            if(view != null) {
                stepsViewHolder = (StepsViewHolder) view.getTag();
            } else {
                view = inflater.inflate(R.layout.recipe_detail_steps, viewGroup, false);
                stepsViewHolder = new StepsViewHolder(view);
                view.setTag(stepsViewHolder);
            }
            Step stepItem = mRecipe.recipeSteps.get(0);
            stepsViewHolder.mStepsTitle.setText(stepItem.stepShortDescription);
            stepsViewHolder.mStepsDesc.setText(stepItem.stepDescription);

            return view;

        } else {
            IngredientsViewHolder ingredientsViewHolder;

            if(view != null) {
                ingredientsViewHolder = (IngredientsViewHolder) view.getTag();
            } else {
                view = inflater.inflate(R.layout.recipe_detail_ingredients, viewGroup, false);
                ingredientsViewHolder = new IngredientsViewHolder(view);
                view.setTag(ingredientsViewHolder);
            }
            Ingredient ingredientItem = mRecipe.recipeIngredients.get(position-2);
            ingredientsViewHolder.mIngredientText.setText(ingredientItem.ingredientQuantity + " " + ingredientItem.ingredientMeasure + " " + ingredientItem.ingredientName);

            return view;
        }


    }

    static class VideoViewHolder {
        @BindView(R.id.simple_exoplayer)
        SimpleExoPlayerView mSimpleExoPlayer;

        public VideoViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    static class StepsViewHolder {
        @BindView(R.id.text_view_steps_title)
        TextView mStepsTitle;

        @BindView(R.id.text_view_steps_desc)
        TextView mStepsDesc;

        @BindView(R.id.btn_previous_steps)
        Button mPrevStepBtn;

        @BindView(R.id.btn_next_steps)
        Button mNextStepBtn;

        public StepsViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    static class IngredientsViewHolder {
        @BindView(R.id.text_view_ingredient_item)
        TextView mIngredientText;

        public IngredientsViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
