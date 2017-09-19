package com.awijaya.mybakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by awijaya on 9/18/17.
 */

public class Step implements Parcelable{


    @SerializedName("id")
    public int stepId;

    @SerializedName("shortDescription")
    public String stepShortDescription;

    @SerializedName("description")
    public String stepDescription;

    @SerializedName("videoURL")
    public String stepVideoURL;

    @SerializedName("thumbnailURL")
    public String stepThumbnailURL;

    protected Step(Parcel in) {
        stepId = in.readInt();
        stepShortDescription = in.readString();
        stepDescription = in.readString();
        stepVideoURL = in.readString();
        stepThumbnailURL = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(stepId);
        parcel.writeString(stepShortDescription);
        parcel.writeString(stepDescription);
        parcel.writeString(stepVideoURL);
        parcel.writeString(stepThumbnailURL);
    }
}
