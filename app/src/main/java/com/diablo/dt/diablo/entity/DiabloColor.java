package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/3/14.
 */

public class DiabloColor {
    @SerializedName("id")
    private Integer ColorId;
    @SerializedName("name")
    private String name;
    @SerializedName("tid")
    private Integer typeId;
    @SerializedName("type")
    private String typeName;
    @SerializedName("remark")
    private String remark;

    public DiabloColor(){};

    public Integer getColorId() {
        return ColorId;
    }

    public String getName() {
        return name;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getRemark() {
        return remark;
    }
}
