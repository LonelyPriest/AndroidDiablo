package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.request.sale.LastSaleRequest;
import com.diablo.dt.diablo.request.sale.NewSaleRequest;
import com.diablo.dt.diablo.request.sale.SaleDetailRequest;
import com.diablo.dt.diablo.request.sale.SaleNoteDetailRequest;
import com.diablo.dt.diablo.request.sale.SaleNoteRequest;
import com.diablo.dt.diablo.response.PrintResponse;
import com.diablo.dt.diablo.response.sale.GetSaleNewResponse;
import com.diablo.dt.diablo.response.sale.LastSaleResponse;
import com.diablo.dt.diablo.response.sale.NewSaleResponse;
import com.diablo.dt.diablo.response.sale.SaleDetailResponse;
import com.diablo.dt.diablo.response.sale.SaleNoteDetailResponse;
import com.diablo.dt.diablo.response.sale.SaleNoteResponse;
import com.diablo.dt.diablo.response.sale.SalePrintContentResponse;

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

    @POST("get_w_sale_print_content")
    Call<SalePrintContentResponse> getPrintContent(@Header("cookie") String token, @Body NewSaleRequest.DiabloRSN rsn);

    @GET("get_w_sale_new/{id}")
    Call<GetSaleNewResponse> getSale(@Header("cookie") String token, @Path("id") String rsn);

    @GET("get_w_sale_note/{rsn}")
    Call<SaleNoteResponse> getSaleNote(@Header("cookie") String token, @Path("rsn") String rsn);

    @POST("w_sale_rsn_detail")
    Call<SaleNoteDetailResponse> getSaleNoteDetail(@Header("cookie") String token, @Body SaleNoteDetailRequest request);
}
