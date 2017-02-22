package com.diablo.dt.diablo.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/2/22.
 */

public class LoginResponse {
    @SerializedName("ecode")
    private Integer mCode;
    @SerializedName("einfo")
    private String mError;
    @SerializedName("token")
    private String mToken;

    public Integer getCode() {
        return mCode;
    }

    public void setCode(Integer mCode) {
        this.mCode = mCode;
    }

    public String getError() {
        return mError;
    }

    public void setError(String mInfo) {
        this.mError = mInfo;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }
}
