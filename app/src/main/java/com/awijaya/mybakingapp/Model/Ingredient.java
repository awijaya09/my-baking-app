package com.awijaya.mybakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by awijaya on 9/18/17.
 */

public class Ingredient implements Parcelable{

        @SerializedName("quantity")
        public double ingredientQuantity;

        @SerializedName("measure")
        public String ingredientMeasure;

        @SerializedName("ingredient")
        public String ingredientName;

        protected Ingredient(Parcel in) {
                ingredientQuantity = in.readDouble();
                ingredientMeasure = in.readString();
                ingredientName = in.readString();
        }

        public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
                @Override
                public Ingredient createFromParcel(Parcel in) {
                        return new Ingredient(in);
                }

                @Override
                public Ingredient[] newArray(int size) {
                        return new Ingredient[size];
                }
        };

        @Override
        public int describeContents() {
                return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
                parcel.writeDouble(ingredientQuantity);
                parcel.writeString(ingredientMeasure);
                parcel.writeString(ingredientName);
        }
}
