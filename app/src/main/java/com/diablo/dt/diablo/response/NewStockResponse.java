package com.diablo.dt.diablo.response;

import com.google.gson.annotations.SerializedName;

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
