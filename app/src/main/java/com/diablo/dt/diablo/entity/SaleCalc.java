package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/3/12.
 */

public class SaleCalc {
    @SerializedName("retailer")
    private Integer retailer;
    @SerializedName("shop")
    private Integer shop;

    @SerializedName("datetime")
    private String datetime;
    @SerializedName("employee")
    private String employee;
    @SerializedName("comment")
    private String comment;

    @SerializedName("balance")
    private Float balance;
    @SerializedName("cash")
    private Float cash;
    @SerializedName("card")
    private Float card;
    @SerializedName("wire")
    private Float wire;
    @SerializedName("verificate")
    private Float verificate;
    @SerializedName("should_pay")
    private Float shouldPay;
    @SerializedName("has_pay")
    private Float hasPay;
    @SerializedName("accBalance")
    private Float accBalance;

    @SerializedName("total")
    private Integer total;
    @SerializedName("sell_total")
    private Integer sellTotal;
    @SerializedName("reject_total")
    private Integer rejectTotal;

    public SaleCalc(){
        this.retailer = -1;

        cash = 0f;
        card = 0f;
        wire = 0f;

        verificate = 0f;
    };

    public Integer getRetailer() {
        return retailer;
    }

    public void setRetailer(Integer retailer) {
        this.retailer = retailer;
    }

    public Integer getShop() {
        return shop;
    }

    public void setShop(Integer shop) {
        this.shop = shop;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Float getCash() {
        return cash;
    }

    public void setCash(Float cash) {
        this.cash = cash;
    }

    public Float getCard() {
        return card;
    }

    public void setCard(Float card) {
        this.card = card;
    }

    public Float getWire() {
        return wire;
    }

    public void setWire(Float wire) {
        this.wire = wire;
    }

    public Float getVerificate() {
        return verificate;
    }

    public void setVerificate(Float verificate) {
        this.verificate = verificate;
    }

    public Float getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(Float shouldPay) {
        this.shouldPay = shouldPay;
    }

    public Float getHasPay() {
        return hasPay;
    }

//    public void setHasPay(Float pay) {
//        this.hasPay = pay;
//    }

    public void resetHasPay(){
        this.hasPay = cash + card + wire;
    }

    public Float getAccBalance() {
        return accBalance;
    }

    public void setAccBalance(Float accBalance) {
        this.accBalance = accBalance;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSellTotal() {
        return sellTotal;
    }

    public void setSellTotal(Integer sellTotal) {
        this.sellTotal = sellTotal;
    }

    public Integer getRejectTotal() {
        return rejectTotal;
    }

    public void setRejectTotal(Integer rejectTotal) {
        this.rejectTotal = rejectTotal;
    }
}
