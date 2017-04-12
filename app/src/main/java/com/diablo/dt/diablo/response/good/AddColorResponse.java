package com.diablo.dt.diablo.response.good;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.Response;

/**
 * Created by buxianhui on 17/4/12.
 */

public class AddColorResponse extends Response {
    @SerializedName("id")
    private Integer insertId;

    public Integer getInsertId() {
        return insertId;
    }
}
