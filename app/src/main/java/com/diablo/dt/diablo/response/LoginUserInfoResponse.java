package com.diablo.dt.diablo.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by buxianhui on 17/2/23.
 */

public class LoginUserInfoResponse extends response{
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
    List<Right> rights;
    @SerializedName("shop")
    List<Shop> shops;

    public class Right {
        @SerializedName("action")
        String action;
        @SerializedName("id")
        Integer rightId;
        @SerializedName("name")
        String name;
        @SerializedName("parent")
        String parent;

        public Right(){

        }

        public String getAction() {
            return this.action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Integer getRightId() {
            return this.rightId;
        }

        public void setRightId(Integer rightId) {
            this.rightId = rightId;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParent() {
            return this.parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }
    }

    public class Shop {
        @SerializedName("func_id")
        Integer funcId;
        @SerializedName("name")
        String name;
        @SerializedName("repo_id")
        Integer repo;
        @SerializedName("shop_id")
        Integer shop;
        @SerializedName("type")
        Integer type;

        public Shop(){

        }

        public Integer getFuncId() {
            return this.funcId;
        }

        public String getName() {
            return this.name;
        }

        public Integer getRepo() {
            return this.repo;
        }

        public Integer getShop() {
            return this.shop;
        }

        public Integer getType() {
            return this.type;
        }
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


    public List<Right> getRights() {
        return this.rights;
    }

    public List<Shop> getShops(){
        return this.shops;
    }
}
