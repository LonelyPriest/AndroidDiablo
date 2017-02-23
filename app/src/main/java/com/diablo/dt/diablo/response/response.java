package com.diablo.dt.diablo.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/2/23.
 */

public class response {
    @SerializedName("ecode")
    private Integer code;
    @SerializedName("einfo")
    private String error;

    public response(){

    };

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
