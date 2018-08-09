package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/2/24.
 */

public class DiabloShop extends DiabloEntity{
    @SerializedName("func_id")
    private Integer funcId;
    @SerializedName("name")
    private String name;
    @SerializedName("repo_id")
    private Integer repo;
    @SerializedName("shop_id")
    private Integer shop;
    @SerializedName("type")
    private Integer type;
    @SerializedName("address")
    private String address;

    public DiabloShop(){

    }

    public Integer getFuncId() {
        return this.funcId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getViewName() {
        return name;
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

    public String getAddress() {
        return this.address;
    }
}
