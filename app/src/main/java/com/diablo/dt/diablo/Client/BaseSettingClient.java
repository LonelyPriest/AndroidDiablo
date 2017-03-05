package com.diablo.dt.diablo.Client;

import com.diablo.dt.diablo.entity.Profie;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by buxianhui on 17/3/6.
 */

public class BaseSettingClient {
    private static Retrofit retrofit;
    private static final  String mUrl = "wbase/";

    private BaseSettingClient(){

    }

    public static Retrofit getClient() {
        String baseUrl = Profie.getInstance().getServer() + mUrl;
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
