package com.diablo.dt.diablo.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/3/23.
 */

public class LogoutRequest {
    @SerializedName("operation")
    private String operation;

    public LogoutRequest(String operation) {
        this.operation = operation;
    }
}
