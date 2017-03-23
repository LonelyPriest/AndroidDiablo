package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.entity.BaseSetting;
import com.diablo.dt.diablo.request.LogoutRequest;
import com.diablo.dt.diablo.response.Response;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by buxianhui on 17/3/6.
 */

public interface BaseSettingInterface {
    @GET("list_base_setting")
    Call<List<BaseSetting>> listBaseSetting(@Header("cookie") String token);

    @POST("destroy_login_user")
    Call<Response> logout(@Header("cookie") String token, @Body LogoutRequest request);
}
