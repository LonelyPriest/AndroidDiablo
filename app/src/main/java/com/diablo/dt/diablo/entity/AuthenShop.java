package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/2/24.
 */

public class AuthenShop {
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

    public AuthenShop(){

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
