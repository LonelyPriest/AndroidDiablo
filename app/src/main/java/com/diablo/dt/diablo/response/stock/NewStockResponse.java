package com.diablo.dt.diablo.response.stock;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.Response;

/**
 * Created by buxianhui on 17/4/5.
 */

public class NewStockResponse extends Response {
    @SerializedName("rsn")
    private String rsn;

    public String getRsn() {
        return rsn;
    }
}
