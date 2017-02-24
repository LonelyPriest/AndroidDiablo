package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/2/24.
 */

public class Retailer {
    @SerializedName("id")
    Integer id;
    @SerializedName("name")
    String name;
    @SerializedName("mobile")
    String mobile;
    @SerializedName("address")
    String address;
    @SerializedName("balance")
    Float balance;
    @SerializedName("cid")
    Integer city;
    @SerializedName("pid")
    Integer province;
    @SerializedName("entry_date")
    String entryDate;
    @SerializedName("merchant")
    Integer merchant;

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
