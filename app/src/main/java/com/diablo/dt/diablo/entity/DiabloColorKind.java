package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.utils.DiabloEnum;

/**
 * Created by buxianhui on 17/4/10.
 */

public class DiabloColorKind extends DiabloEntity{
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

    @Override
    public String getName() {
        return kindName;
    }

    @Override
    public String getViewName() {
        return kindName;
    }
}
