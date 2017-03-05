package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/2/24.
 */

public class AuthenRight {
    @SerializedName("action")
    private String action;
    @SerializedName("id")
    private Integer rightId;
    @SerializedName("name")
    private String name;
    @SerializedName("parent")
    private String parent;

    public AuthenRight(){

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
