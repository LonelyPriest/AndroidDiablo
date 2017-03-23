package com.diablo.dt.diablo.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/3/23.
 */

public class AddRetailerResponse extends Response {
    @SerializedName("id")
    private Integer insertId;

    public Integer getInsertId() {
        return insertId;
    }
}
