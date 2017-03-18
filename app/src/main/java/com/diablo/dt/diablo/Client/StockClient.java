package com.diablo.dt.diablo.client;

import com.diablo.dt.diablo.entity.Profile;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by buxianhui on 17/3/7.
 */

public class StockClient {
    private static Retrofit retrofit;
    private static final  String mUrl = "purchaser/";

    private StockClient(){

    }

    public static Retrofit getClient() {
        String baseUrl = Profile.instance().getServer() + mUrl;
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
