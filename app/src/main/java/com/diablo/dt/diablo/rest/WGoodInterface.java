package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.entity.DiabloBrand;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.DiabloColorKind;
import com.diablo.dt.diablo.entity.DiabloSizeGroup;
import com.diablo.dt.diablo.entity.DiabloType;
import com.diablo.dt.diablo.entity.MatchGood;
import com.diablo.dt.diablo.request.MatchGoodRequest;
import com.diablo.dt.diablo.request.inventory.GoodDetailRequest;
import com.diablo.dt.diablo.request.inventory.InventoryNewRequest;
import com.diablo.dt.diablo.response.inventory.AddFirmResponse;
import com.diablo.dt.diablo.response.inventory.GoodDetailResponse;
import com.diablo.dt.diablo.response.inventory.InventoryNewResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by buxianhui on 17/3/14.
 */

public interface WGoodInterface {
    @GET("list_w_color")
    Call<List<DiabloColor>> listColor(@Header("cookie") String token);

    @GET("list_color_type")
    Call<List<DiabloColorKind>> listColorKind(@Header("cookie") String token);

    @GET("list_w_size")
    Call<List<DiabloSizeGroup>> listSizeGroup(@Header("cookie") String token);

    @GET("list_brand")
    Call<List<DiabloBrand>> listBrand(@Header("cookie") String token);

    @GET("list_type")
    Call<List<DiabloType>> listType(@Header("cookie") String token);

    @POST("match_all_w_good")
    Call<List<MatchGood>> matchAllGood(@Header("cookie") String token, @Body MatchGoodRequest request);

    @POST("new_w_good")
    Call<InventoryNewResponse> addGood(@Header("cookie") String token, @Body InventoryNewRequest request);

    @POST("new_w_brand")
    Call<AddFirmResponse> addBrand(@Header("cookie") String token, @Body DiabloBrand brand);

    @POST("new_w_type")
    Call<AddFirmResponse> addGoodType(@Header("cookie") String token, @Body DiabloType goodType);

    @POST("filter_w_good")
    Call<GoodDetailResponse> filterGood(@Header("cookie") String token, @Body GoodDetailRequest request);
}
