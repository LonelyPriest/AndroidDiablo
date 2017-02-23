package com.diablo.dt.diablo.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/2/22.
 */

public class LoginResponse extends response{
    @SerializedName("token")
    private String token;

    LoginResponse(){
        super();
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String mToken) {
        this.token = token;
    }
}
