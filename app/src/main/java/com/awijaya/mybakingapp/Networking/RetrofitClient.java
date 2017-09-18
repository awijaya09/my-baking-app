package com.awijaya.mybakingapp.Networking;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by awijaya on 9/18/17.
 */

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static final String CLIENT_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";


    public static Retrofit getClient(){


        retrofit = new Retrofit.Builder()
                .baseUrl(CLIENT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;

    }



}
