package com.diablo.dt.diablo.response;

import com.diablo.dt.diablo.entity.AuthenRight;
import com.diablo.dt.diablo.entity.AuthenShop;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by buxianhui on 17/2/23.
 */

public class LoginUserInfoResponse extends Response{
    @SerializedName("login_employee")
    Integer loginEmployee;
    @SerializedName("login_firm")
    Integer loginFirm;
    @SerializedName("login_retailer")
    Integer loginRetailer;
    @SerializedName("login_shop")
    Integer loginShop;
    @SerializedName("type")
    Integer loginType;

    @SerializedName("right")
    List<AuthenRight> rights;
    @SerializedName("shop")
    List<AuthenShop> shops;

    public LoginUserInfoResponse(){
        super();
    }

    public Integer getLoginEmployee() {
        return this.loginEmployee;
    }

    public void setLoginEmployee(Integer loginEmployee) {
        this.loginEmployee = loginEmployee;
    }

    public Integer getLoginFirm() {
        return this.loginFirm;
    }

    public void setLoginFirm(Integer loginFirm) {
        this.loginFirm = loginFirm;
    }

    public Integer getLoginRetailer() {
        return this.loginRetailer;
    }

    public void setLoginRetailer(Integer loginRetailer) {
        this.loginRetailer = loginRetailer;
    }

    public Integer getLoginShop() {
        return this.loginShop;
    }

    public void setLoginShop(Integer loginShop) {
        this.loginShop = loginShop;
    }

    public Integer getLoginType() {
        return this.loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }


    public List<AuthenRight> getRights() {
        return this.rights;
    }

    public List<AuthenShop> getShops(){
        return this.shops;
    }
}
