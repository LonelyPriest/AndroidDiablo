package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.response.inventory.AddFirmResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by buxianhui on 17/4/3.
 */

public interface FirmInterface {
    @GET("list_firm")
    Call<List<Firm>> listFirm(@Header("cookie") String token);

    @POST("new_firm")
    Call<AddFirmResponse> addFirm(@Header("cookie") String token, @Body Firm firm);
}
