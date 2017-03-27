package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.request.LastSaleRequest;
import com.diablo.dt.diablo.request.NewSaleRequest;
import com.diablo.dt.diablo.request.SaleDetailRequest;
import com.diablo.dt.diablo.request.SaleNoteRequest;
import com.diablo.dt.diablo.response.GetSaleNewResponse;
import com.diablo.dt.diablo.response.LastSaleResponse;
import com.diablo.dt.diablo.response.NewSaleResponse;
import com.diablo.dt.diablo.response.PrintResponse;
import com.diablo.dt.diablo.response.SaleDetailResponse;
import com.diablo.dt.diablo.response.SaleNoteResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by buxianhui on 17/2/21.
 */

public interface WSaleInterface {
    @POST("filter_w_sale_new")
    Call<SaleDetailResponse> filterSaleNew(@Header("cookie") String token, @Body SaleDetailRequest request);

    @POST("filter_w_sale_rsn_group")
    Call<SaleNoteResponse> filterSaleNote(@Header("cookie") String token, @Body SaleNoteRequest request);

    @POST("get_last_sale")
    Call<List<LastSaleResponse>> getLastSale(@Header("cookie") String token, @Body LastSaleRequest request);

    @POST("new_w_sale")
    Call<NewSaleResponse> startSale(@Header("cookie") String token, @Body NewSaleRequest request);

    @POST("reject_w_sale")
    Call<NewSaleResponse> startReject(@Header("cookie") String token, @Body NewSaleRequest request);

    @POST("update_w_sale")
    Call<com.diablo.dt.diablo.response.Response> updateSale(@Header("cookie") String token, @Body NewSaleRequest request);

    @POST("print_w_sale")
    Call<PrintResponse> startPrint(@Header("cookie") String token, @Body NewSaleRequest.DiabloRSN rsn);

    @GET("get_w_sale_new/{id}")
    Call<GetSaleNewResponse> getSale(@Header("cookie") String token, @Path("id") String rsn);
}
