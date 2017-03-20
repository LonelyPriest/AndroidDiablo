package com.diablo.dt.diablo.model;

/**
 * Created by buxianhui on 17/3/12.
 */

public class SaleCalc {
    private Integer retailer;
    private Integer shop;

    private String datetime;
    private String employee;
    private String comment;

    private Float balance;
    private Float cash;
    private Float card;
    private Float wire;
    private Float verificate;
    private Float shouldPay;
    private Float hasPay;
    private Float accBalance;

    private Integer extraCostType;
    private Float extraCost;

    private Integer total;
    private Integer sellTotal;
    private Integer rejectTotal;

    public SaleCalc(){
        retailer = -1;

        cash = 0f;
        card = 0f;
        wire = 0f;
        extraCost  = 0f;
        verificate = 0f;
        shouldPay  = 0f;
        hasPay     = 0f;
    }

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

    public Integer getExtraCostType() {
        return extraCostType;
    }

    public void setExtraCostType(Integer extraCostType) {
        this.extraCostType = extraCostType;
    }

    public Float getExtraCost() {
        return extraCost;
    }

    public void setExtraCost(Float extraCost) {
        this.extraCost = extraCost;
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

    public void calcHasPay(){
        hasPay = cash + card + wire;
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

    public void resetCash() {
        cash = 0f;
    }

    public Float calcAccBalance(){
        accBalance = balance + shouldPay + extraCost - verificate - hasPay;
        return accBalance;
    }
}