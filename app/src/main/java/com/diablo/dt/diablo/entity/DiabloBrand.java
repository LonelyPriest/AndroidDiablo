package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/3/28.
 */

public class DiabloBrand {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("supplier_id")
    private String supplierId;
    @SerializedName("supplier")
    private String firm;

    public DiabloBrand() {

    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getFirm() {
        return firm;
    }
}
