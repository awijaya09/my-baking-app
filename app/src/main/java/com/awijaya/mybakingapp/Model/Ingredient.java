package com.awijaya.mybakingapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by awijaya on 9/18/17.
 */

public class Ingredient {

        @SerializedName("quantity")
        public double ingredientQuantity;

        @SerializedName("measure")
        public String ingredientMeasure;

        @SerializedName("ingredient")
        public String ingredientName;

}
