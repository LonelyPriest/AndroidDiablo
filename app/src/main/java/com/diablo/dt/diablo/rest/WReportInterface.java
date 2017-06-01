package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.request.report.DailyReportRequest;
import com.diablo.dt.diablo.response.report.DailyReportResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by buxianhui on 17/5/31.
 */

public interface WReportInterface {
    @POST("daily_wreport/{type}")
    Call<DailyReportResponse> dailyReportOfRealTime(
        @Header("cookie") String token,
        @Path("type") String type,
        @Body DailyReportRequest request);
}
