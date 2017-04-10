package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Stock;
import com.diablo.dt.diablo.request.MatchStockRequest;
import com.diablo.dt.diablo.request.stock.InventoryDetailRequest;
import com.diablo.dt.diablo.request.stock.NewStockRequest;
import com.diablo.dt.diablo.request.stock.StockDetailRequest;
import com.diablo.dt.diablo.request.stock.StockNoteRequest;
import com.diablo.dt.diablo.request.sale.StockRequest;
import com.diablo.dt.diablo.response.good.InventoryDetailResponse;
import com.diablo.dt.diablo.response.stock.GetStockNewResponse;
import com.diablo.dt.diablo.response.stock.NewStockResponse;
import com.diablo.dt.diablo.response.stock.StockDetailResponse;
import com.diablo.dt.diablo.response.stock.StockNoteResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by buxianhui on 17/3/6.
 */

public interface StockInterface {
    @POST("match_all_w_inventory")
    Call<List<MatchStock>> matchAllStock(
            @Header("cookie") String token, @Body MatchStockRequest request);

    @POST("list_w_inventory")
    Call <List<Stock>> getStock(@Header("cookie") String token, @Body StockRequest request);

    @POST("filter_w_inventory_new")
    Call<StockDetailResponse> filterStockNew(@Header("cookie") String token, @Body StockDetailRequest request);

    @POST("filter_w_inventory_new_rsn_group")
    Call<StockNoteResponse> filterStockNote(@Header("cookie") String token, @Body StockNoteRequest request);

    // inventory
    @POST("filter_w_inventory_group")
    Call<InventoryDetailResponse> filterInventory(@Header("cookie") String token,
                                                  @Body InventoryDetailRequest request);

    @POST("new_w_inventory")
    Call<NewStockResponse> addStock(@Header("cookie") String token, @Body NewStockRequest request);

    @POST("reject_w_inventory")
    Call<NewStockResponse> rejectStock(@Header("cookie") String token, @Body NewStockRequest request);

    @POST("update_w_inventory")
    Call<NewStockResponse> updateStock(@Header("cookie") String token, @Body NewStockRequest request);

    @GET("get_w_inventory_new_info/{id}")
    Call<GetStockNewResponse> getStockNewInfo(@Header("cookie") String token, @Path("id") String rsn);
}
