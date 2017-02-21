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
    @POST("filter_wsale_new")
    Call<List<String>> filterWsaleNew(@Field("star_time") String start,
                                      @Field("end_time") String end);
}
