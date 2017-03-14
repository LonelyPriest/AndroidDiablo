package com.diablo.dt.diablo.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/3/14.
 */

public class StockRequest {
    @SerializedName("style_number")
    private String styleNumber;
    @SerializedName("brand")
    private Integer brand;
    @SerializedName("shop")
    private Integer shop;
    @SerializedName("qtype")
    private Integer queryType;

    public StockRequest(String styleNumber, Integer brand, Integer shop, Integer queryType) {
        this.styleNumber = styleNumber;
        this.brand = brand;
        this.shop = shop;
        this.queryType = queryType;
    }

    public String getStyleNumber() {
        return styleNumber;
    }

    public void setStyleNumber(String styleNumber) {
        this.styleNumber = styleNumber;
    }

    public Integer getBrand() {
        return brand;
    }

    public void setBrand(Integer brand) {
        this.brand = brand;
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
}
