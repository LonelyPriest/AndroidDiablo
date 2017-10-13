package com.diablo.dt.diablo.request.stock;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/10/13.
 */

public class StockNoteDetailRequest {
    @SerializedName("rsn")
    private String rsn;
    @SerializedName("style_number")
    private String styleNumber;
    @SerializedName("brand")
    private Integer brand;

    public StockNoteDetailRequest(String rsn, String styleNumber, Integer brand) {
        this.rsn = rsn;
        this.styleNumber = styleNumber;
        this.brand = brand;
    }

    public void setRsn(String rsn) {
        this.rsn = rsn;
    }

    public void setStyleNumber(String styleNumber) {
        this.styleNumber = styleNumber;
    }

    public void setBrand(Integer brand) {
        this.brand = brand;
    }
}
