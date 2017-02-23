package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.request.SaleDetailRequest;
import com.diablo.dt.diablo.response.SaleDetailResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by buxianhui on 17/2/21.
 */

public interface WSaleInterface {
    @POST("filter_w_sale_new")
    Call<SaleDetailResponse> filterWsaleNew(
            @Header("cookie") String token, @Body SaleDetailRequest request);
}
