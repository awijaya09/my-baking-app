package com.awijaya.mybakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awijaya on 9/18/17.
 */

public class Recipe implements Parcelable {

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

    protected Recipe(Parcel in) {
        recipeId = in.readInt();
        recipeName = in.readString();
        recipeServings = in.readInt();
        recipeImage = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(recipeId);
        parcel.writeString(recipeName);
        parcel.writeInt(recipeServings);
        parcel.writeString(recipeImage);
    }
}

