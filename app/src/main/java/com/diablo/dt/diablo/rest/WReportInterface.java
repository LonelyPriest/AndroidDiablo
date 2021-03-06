package com.diablo.dt.diablo.rest;

import com.diablo.dt.diablo.request.report.DailyReportRealRequest;
import com.diablo.dt.diablo.request.report.DailyReportRequest;
import com.diablo.dt.diablo.request.report.MonthReportRequest;
import com.diablo.dt.diablo.response.Response;
import com.diablo.dt.diablo.response.report.DailyReportRealResponse;
import com.diablo.dt.diablo.response.report.DailyReportResponse;
import com.diablo.dt.diablo.response.report.DailyReportSaleDetailResponse;
import com.diablo.dt.diablo.response.report.MonthReportResponse;

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
    Call<DailyReportRealResponse> dailyReportOfRealTime(
        @Header("cookie") String token,
        @Path("type") String type,
        @Body DailyReportRealRequest request);

    @POST("daily_wreport/{type}")
    Call<DailyReportSaleDetailResponse> dailyReportSaleDetailOfRealTime(
        @Header("cookie") String token,
        @Path("type") String type,
        @Body DailyReportRealRequest request);

    @POST("h_daily_wreport")
    Call<DailyReportResponse> filterDailyReport(@Header("cookie") String token, @Body DailyReportRequest request);

    @POST("h_month_wreport")
    Call<MonthReportResponse> filterMonthReport(@Header("cookie") String token, @Body MonthReportRequest request);

    @POST("syn_daily_report")
    Call<Response> synDailyReport(@Header("cookie") String token, @Body DailyReportRequest.Condition request);
}
