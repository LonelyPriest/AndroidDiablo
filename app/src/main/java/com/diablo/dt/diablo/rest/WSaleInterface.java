package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.request.LastSaleRequest;
import com.diablo.dt.diablo.request.NewSaleRequest;
import com.diablo.dt.diablo.request.SaleDetailRequest;
import com.diablo.dt.diablo.response.LastSaleResponse;
import com.diablo.dt.diablo.response.NewSaleResponse;
import com.diablo.dt.diablo.response.PrintResponse;
import com.diablo.dt.diablo.response.SaleDetailResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by buxianhui on 17/2/21.
 */

public interface WSaleInterface {
    @POST("filter_w_sale_new")
    Call<SaleDetailResponse> filterWSaleNew(
            @Header("cookie") String token, @Body SaleDetailRequest request);

    @POST("get_last_sale")
    Call<List<LastSaleResponse>> getLastSale(@Header("cookie") String token, @Body LastSaleRequest request);

    @POST("new_w_sale")
    Call<NewSaleResponse> startSale(@Header("cookie") String token, @Body NewSaleRequest request);

    @POST("reject_w_sale")
    Call<NewSaleResponse> startReject(@Header("cookie") String token, @Body NewSaleRequest request);

    @POST("print_w_sale")
    Call<PrintResponse> startPrint(@Header("cookie") String token, @Body NewSaleRequest.DiabloRSN rsn);
}
