package com.diablo.dt.diablo.model.stock;

import com.diablo.dt.diablo.utils.DiabloEnum;

/**
 * Created by buxianhui on 17/4/3.
 */

public class StockCalc {
    private Integer firm;
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

    private Integer stockType;

    public StockCalc(){
        stockType = DiabloEnum.STOCK_IN;
        init();
    }

    public StockCalc(Integer stockType) {
        this.stockType = stockType;
        init();
    }

    public StockCalc(StockCalc calc) {
        this.firm = calc.getFirm();
        this.shop = calc.getShop();

        this.datetime = calc.getDatetime();
        this.employee = calc.getEmployee();
        this.comment = calc.getComment();

        this.balance = calc.getBalance();
        this.cash = calc.getCash();
        this.card = calc.getCard();
        this.wire = calc.getWire();
        this.verificate = calc.getVerificate();
        this.shouldPay = calc.getShouldPay();
        this.hasPay = calc.getHasPay();
        this.accBalance = calc.getAccBalance();

        this.extraCostType = calc.getExtraCostType();
        this.extraCost = calc.getExtraCost();

        this.total = calc.getTotal();

        this.stockType = calc.getStockType();
    }

    private void init() {
        firm = -1;

        cash = 0f;
        card = 0f;
        wire = 0f;
        extraCost  = 0f;
        verificate = 0f;
        shouldPay  = 0f;
        hasPay     = 0f;
    }

    public Integer getFirm() {
        return firm;
    }

    public void setFirm(Integer firm) {
        this.firm = firm;
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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getStockType () {
        return stockType;
    };

    public void resetCash() {
        cash = 0f;
    }

    public Float calcAccBalance(){
        if (DiabloEnum.STOCK_IN.equals(stockType)) {
            accBalance = balance + shouldPay + extraCost - verificate - hasPay;
        } else if (DiabloEnum.STOCK_OUT.equals(stockType)) {
            accBalance = balance - shouldPay - extraCost - verificate;
        }

        return accBalance;
    }
}
