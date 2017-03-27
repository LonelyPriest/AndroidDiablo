package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.response.AddRetailerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by buxianhui on 17/2/24.
 */

public interface RetailerInterface {
    @GET("list_w_retailer")
    Call<List<Retailer>> listRetailer(@Header("cookie") String token);

    @POST("new_w_retailer")
    Call<AddRetailerResponse> addRetailer(@Header("cookie") String token, @Body Retailer retailer);

    @POST("match_w_retailer")
    Call<List<Retailer>> matchRetailer(@Header("cookie") String token, @Body String match);

    @GET("get_w_retailer/{retailerId}")
    Call<Retailer> getRetailer(@Header("cookie") String token, @Path("retailerId") Integer retailerId);
}
