package com.awijaya.mybakingapp;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
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

    private static final String TAG = "Recipe Detail Adapter";

    public RecipeDetailListViewAdapter(Context context, Recipe recipe){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRecipe = recipe;
        mIngredient = recipe.recipeIngredients;

    }


    @Override
    public int getCount() {
            return mIngredient.size()+2;
    }

    @Override
    public Object getItem(int i) {
            return mIngredient.get(i-2);
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
        if (position == 0) {
            return 0;
        } else if (position == 1){
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (position == 0) {
            VideoViewHolder videoViewHolder;

            if (view != null) {
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

            return view;

        }
            else {
            IngredientsViewHolder ingredientsViewHolder;

            if(view != null) {
                ingredientsViewHolder = (IngredientsViewHolder) view.getTag();
            } else {
                view = inflater.inflate(R.layout.recipe_detail_ingredients, viewGroup, false);
                ingredientsViewHolder = new IngredientsViewHolder(view);
                view.setTag(ingredientsViewHolder);
            }
            Ingredient ingredientItem = mRecipe.recipeIngredients.get(position-2);
            ingredientsViewHolder.mIngQty.setText(String.valueOf(ingredientItem.ingredientQuantity));
            ingredientsViewHolder.mIngMeasure.setText(ingredientItem.ingredientMeasure);
            ingredientsViewHolder.mIngName.setText(ingredientItem.ingredientName);

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
        @BindView(R.id.text_view_ingredients_title)
        TextView mIngTitle;

        public StepsViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    static class IngredientsViewHolder {
        @BindView(R.id.text_view_ingredient_item_qty)
        TextView mIngQty;

        @BindView(R.id.text_view_ingredient_item_measure)
        TextView mIngMeasure;

        @BindView(R.id.text_view_ingredient_item_name)
        TextView mIngName;

        public IngredientsViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
