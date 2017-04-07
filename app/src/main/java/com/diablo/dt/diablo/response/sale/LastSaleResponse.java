package com.diablo.dt.diablo.response.sale;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/3/14.
 */

public class LastSaleResponse {
    @SerializedName("rsn")
    private String rsn;
    @SerializedName("id")
    private Integer rid;
    @SerializedName("style_number")
    private String styleNumber;
    @SerializedName("sell_type")
    private String sellType;
    @SerializedName("fdiscount")
    private Float discount;
    @SerializedName("fprice")
    private Float price;

    public LastSaleResponse() {

    }

    public String getRsn() {
        return rsn;
    }

    public Integer getRid() {
        return rid;
    }

    public String getStyleNumber() {
        return styleNumber;
    }

    public String getSellType() {
        return sellType;
    }

    public Float getDiscount() {
        return discount;
    }

    public Float getPrice() {
        return price;
    }
}
