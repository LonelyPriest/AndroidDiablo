package com.diablo.dt.diablo.response.good;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.Response;

/**
 * Created by buxianhui on 17/4/9.
 */

public class InventoryNewResponse extends Response {
    @SerializedName("brand")
    private Integer brandId;
    @SerializedName("type")
    private Integer typeID;

    public Integer getBrandId() {
        return brandId;
    }

    public Integer getTypeID() {
        return typeID;
    }
}
