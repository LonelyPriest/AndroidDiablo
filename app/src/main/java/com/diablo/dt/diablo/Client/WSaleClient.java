package com.diablo.dt.diablo.Client;

import com.diablo.dt.diablo.entity.MainProfile;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by buxianhui on 17/2/21.
 */

public class WSaleClient {
    private static Retrofit retrofit;
    private static final  String mUrl = "wsale/";

    private WSaleClient (){

    }

    public static Retrofit getClient() {
        String baseUrl = MainProfile.getInstance().getServer() + mUrl;
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
