package com.diablo.dt.diablo.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by buxianhui on 17/2/21.
 */

public interface WSaleInterface {
    @FormUrlEncoded
    @POST("login")
    Call<List<String>> filterWsaleNew(@Field("user_name") String name,
                                      @Field("user_password") String password);
}
