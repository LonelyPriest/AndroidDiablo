package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.utils.DiabloEnum;

/**
 * Created by buxianhui on 17/4/10.
 */

public class DiabloColorKind {
    @SerializedName("id")
    private Integer kindId;
    @SerializedName("name")
    private String kindName;

    public DiabloColorKind() {
        kindId = DiabloEnum.INVALID_INDEX;
        kindName = DiabloEnum.EMPTY_STRING;
    }

    public Integer getId() {
        return kindId;
    }

    public String getName() {
        return kindName;
    }
}
