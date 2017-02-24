package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.entity.Retailer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by buxianhui on 17/2/24.
 */

public interface RetailerInterface {
    @GET("list_w_retailer")
    Call<List<Retailer>> listRetailer(@Header("cookie") String token);
}
