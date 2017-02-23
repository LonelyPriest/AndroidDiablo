package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.response.LoginUserInfoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by buxianhui on 17/2/23.
 */

public interface RightInterface {
    @GET("get_login_user_info")
    Call<LoginUserInfoResponse> getLoginUserInfo(@Header("cookie") String token);
}
