package com.awijaya.mybakingapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by awijaya on 9/18/17.
 */

public class Step {


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
}
