package com.diablo.dt.diablo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/3/20.
 */

public class DiabloPrintAttrs {
    @SerializedName("im_printer")
    private Integer immediatelyPrint;
    @SerializedName("retailer_id")
    private Integer retailerId;
    @SerializedName("retailer")
    private String retailerName;
    @SerializedName("shop")
    private String  shop;
    @SerializedName("employ")
    private String employee;

    public DiabloPrintAttrs(Integer immediatelyPrint, Integer retailerId, String retailerName, String shop, String employee) {
        this.immediatelyPrint = immediatelyPrint;
        this.retailerId = retailerId;
        this.retailerName = retailerName;
        this.shop = shop;
        this.employee = employee;
    }

    public void setImmediatelyPrint(Integer immediatelyPrint) {
        this.immediatelyPrint = immediatelyPrint;
    }

    public void setRetailerId(Integer retailerId) {
        this.retailerId = retailerId;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }
}

