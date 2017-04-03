package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.entity.Firm;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by buxianhui on 17/4/3.
 */

public interface FirmInterface {
    @GET("list_firm")
    Call<List<Firm>> listFirm(@Header("cookie") String token);
}
