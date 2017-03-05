package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.entity.BaseSetting;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by buxianhui on 17/3/6.
 */

public interface BaseSettingInterface {
    @GET("list_base_setting")
    Call<List<BaseSetting>> listBaseSetting(@Header("cookie") String token);
}
