package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by buxianhui on 17/2/22.
 */

public interface WLoginInterfacae {
    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> login(@Field("user_name") String name,
                              @Field("user_password") String password,
                              @Field("tablet") Integer tablet);
}
