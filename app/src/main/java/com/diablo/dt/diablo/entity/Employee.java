package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/2/24.
 */

public class Employee {
    @SerializedName("id")
    private Integer Id;
    @SerializedName("name")
    private String name;
    @SerializedName("number")
    private String number;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("sex")
    private Integer sex;
    @SerializedName("address")
    private String address;
    @SerializedName("entry")
    private String entry;
    @SerializedName("position")
    private Integer position;


    public Integer getId() {
        return this.Id;
    }

    public String getName() {
        return this.name;
    }

    public String getNumber() {
        return this.number;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Integer getSex() {
        return this.sex;
    }

    public String getAddress() {
        return this.address;
    }

    public String getEntry() {
        return this.entry;
    }

    public Integer getPosition() {
        return this.position;
    }



    public Employee() {

    }
}
