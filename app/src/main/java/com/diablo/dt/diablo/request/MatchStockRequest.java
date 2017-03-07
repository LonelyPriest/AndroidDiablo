package com.diablo.dt.diablo.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/3/7.
 */

public class MatchStockRequest {
    @SerializedName("shop")
    private Integer shop;
    @SerializedName("qtype")
    private Integer queryType;
    @SerializedName("start_time")
    private String startTime;

    public MatchStockRequest(String stime, Integer shop, Integer queryType){
        this.shop = shop;
        this.startTime = stime;
        this.queryType = queryType;
    }

    public Integer getShop() {
        return shop;
    }

    public void setShop(Integer shop) {
        this.shop = shop;
    }

    public Integer getQueryType() {
        return queryType;
    }

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
