package com.diablo.dt.diablo.response.retailer;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.Response;

/**
 * Created by buxianhui on 17/4/21.
 */

public class UpdateRetailerResponse extends Response {
    @SerializedName("cid")
    private Integer province;

    public Integer getProvince() {
        return province;
    }
}
