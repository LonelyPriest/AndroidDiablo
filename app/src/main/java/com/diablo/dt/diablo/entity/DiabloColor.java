package com.diablo.dt.diablo.entity;

import com.diablo.dt.diablo.utils.DiabloEnum;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by buxianhui on 17/3/14.
 */

public class DiabloColor {
    @SerializedName("id")
    private Integer colorId;
    @SerializedName("name")
    private String name;
    @SerializedName("tid")
    private Integer typeId;
    @SerializedName("type")
    private String typeName;
    @SerializedName("remark")
    private String remark;

    public DiabloColor(){
        this.colorId = DiabloEnum.DIABLO_FREE_COLOR;
    };

    public DiabloColor(Integer colorId){
        this.colorId = colorId;
    }

    public Integer getColorId() {
        return colorId;
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

    public boolean includeIn(List<DiabloColor> colors){
        boolean include = false;
        for (int i = 0; i < colors.size(); i++) {
            if (colors.get(i).getColorId().equals(colorId)){
                include = true;
                break;
            }
        }
        return include;
    }
}
