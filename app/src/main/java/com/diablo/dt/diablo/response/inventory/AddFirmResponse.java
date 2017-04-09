package com.diablo.dt.diablo.response.inventory;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.Response;

/**
 * Created by buxianhui on 17/4/9.
 */

public class AddFirmResponse extends Response {
    @SerializedName("id")
    private Integer insertId;

    public AddFirmResponse() {
        super();
    }

    public Integer getInsertId() {
        return insertId;
    }
}
