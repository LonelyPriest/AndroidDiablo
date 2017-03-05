package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/3/6.
 */

public class BaseSetting {
    @SerializedName("id")
    private Integer id;
    @SerializedName("cname")
    private String cname;
    @SerializedName("ename")
    private String ename;
    @SerializedName("value")
    private String value;
    @SerializedName("shop")
    private Integer shop;
    @SerializedName("type")
    private Integer type;
    @SerializedName("remark")
    private String remark;
    @SerializedName("entry_date")
    private String date;

    public Integer getId() {
        return id;
    }

    public String getCName() {
        return cname;
    }

    public String getEName() {
        return ename;
    }

    public String getValue() {
        return value;
    }

    public Integer getShop() {
        return shop;
    }

    public Integer getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }

    public String getDate() {
        return date;
    }
}
