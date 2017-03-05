package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/2/24.
 */

public class Retailer {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("address")
    private String address;
    @SerializedName("balance")
    private Float balance;
    @SerializedName("cid")
    private Integer city;
    @SerializedName("pid")
    private Integer province;
    @SerializedName("entry_date")
    private String entryDate;
    @SerializedName("merchant")
    private Integer merchant;

    public Retailer(){

    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getBalance() {
        return this.balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Integer getCity() {
        return this.city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getProvince() {
        return this.province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public String getEntryDate() {
        return this.entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public Integer getMerchant() {
        return this.merchant;
    }

    public void setMerchant(Integer merchant) {
        this.merchant = merchant;
    }
}
