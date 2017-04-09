package com.diablo.dt.diablo.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/2/24.
 */

public class Response {
    @SerializedName("ecode")
    private Integer code;
    @SerializedName("einfo")
    private String error;

    public Response(){

    }

    public Integer getCode() {
        return code;
    }

    public String getError() {
        return error;
    }
}
