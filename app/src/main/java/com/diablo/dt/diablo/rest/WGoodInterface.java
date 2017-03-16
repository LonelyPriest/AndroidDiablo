package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.DiabloSizeGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by buxianhui on 17/3/14.
 */

public interface WGoodInterface {
    @GET("list_w_color")
    Call<List<DiabloColor>> listColor(@Header("cookie") String token);

    @GET("list_w_size")
    Call<List<DiabloSizeGroup>> listSizeGroup(@Header("cookie") String token);
}