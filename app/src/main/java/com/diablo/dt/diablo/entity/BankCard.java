package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 18/8/9.
 */

public class BankCard {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("no")
    private String number;
    @SerializedName("bank")
    private String bankName;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getBankName() {
        return bankName;
    }


}
