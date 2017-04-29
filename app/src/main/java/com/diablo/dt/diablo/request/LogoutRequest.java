package com.diablo.dt.diablo.request;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.utils.DiabloEnum;

/**
 * Created by buxianhui on 17/3/23.
 */

public class LogoutRequest {
    @SerializedName("operation")
    private String operation;
    @SerializedName("tablet")
    private Integer tablet;

    public LogoutRequest(String operation) {
        this.operation = operation;
        this.tablet = DiabloEnum.TABLET;
    }
}
