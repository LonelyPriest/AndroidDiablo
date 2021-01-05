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

//    private static final OkHttpClient client = new OkHttpClient.Builder().
//        connectTimeout(30, TimeUnit.SECONDS).
//        readTimeout(30, TimeUnit.SECONDS).
//        writeTimeout(30, TimeUnit.SECONDS).build();


    private StockClient(){

    }

    public static Retrofit getClient() {
        String baseUrl = Profile.instance().getServer() + mUrl;
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
//                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }

    public static void resetClient() {
        retrofit = null;
    }
}
