package com.awijaya.mybakingapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awijaya on 9/18/17.
 */

public class Recipe {

    @SerializedName("id")
    public int recipeId;

    @SerializedName("name")
    public String recipeName;

    @SerializedName("servings")
    public int recipeServings;

    @SerializedName("image")
    public String recipeImage;

    @SerializedName("ingredients")
    public ArrayList<Ingredient> recipeIngredients;

    @SerializedName("steps")
    public ArrayList<Step> recipeSteps;
}

