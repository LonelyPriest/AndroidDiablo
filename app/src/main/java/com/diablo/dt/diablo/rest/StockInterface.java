package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.request.MatchStockRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by buxianhui on 17/3/6.
 */

public interface StockInterface {
    @POST("match_all_w_inventory")
    Call<List<MatchStock>> matchAllStock(
            @Header("cookie") String token, @Body MatchStockRequest request);
}
