package com.diablo.dt.diablo.request.sale;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/3/14.
 */

public class LastSaleRequest {
    @SerializedName("style_number")
    private String styleNumber;
    @SerializedName("brand")
    private Integer brand;
    @SerializedName("shop")
    private Integer shop;
    @SerializedName("retailer")
    private Integer retailer;
    @SerializedName("r_pgood")
    private Integer RPGood;

    public LastSaleRequest(String styleNumber, Integer brand, Integer shop, Integer retailer) {
        this.styleNumber = styleNumber;
        this.brand = brand;
        this.shop = shop;
        this.retailer = retailer;
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

    public Integer getRetailer() {
        return retailer;
    }

    public void setRetailer(Integer retailer) {
        this.retailer = retailer;
    }

    public Integer getRPGood() {
        return RPGood;
    }

    public void setRPGood(Integer RPGood) {
        this.RPGood = RPGood;
    }
}
