package com.diablo.dt.diablo.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by buxianhui on 17/2/23.
 */

public class SaleDetailResponse extends response{
    @SerializedName("total")
    private Integer mTotal;

    @SerializedName("t_amount")
    private Integer mAmount;

    @SerializedName("t_card")
    private Float mCard;

    @SerializedName("t_cash")
    private Float mCash;

    @SerializedName("t_epay")
    private Float mEPay;

    @SerializedName("t_hpay")
    private Float mHPay;

    @SerializedName("t_spay")
    private Float mSPay;

    @SerializedName("t_verificate")
    private Float mVerificate;

    @SerializedName("t_wire")
    private Float mWire;

    @SerializedName("data")
    List<SaleDetail> mSaleDetail;

    public class SaleDetail{
        @SerializedName("id")
        Integer id;
        @SerializedName("order_id")
        Integer orderId;
        @SerializedName("rsn")
        String rsn;

        @SerializedName("balance")
        Float balance;
        @SerializedName("card")
        Float card;
        @SerializedName("cash")
        Float cash;
        @SerializedName("e_pay")
        Float epay;
        @SerializedName("has_pay")
        Float hasPay;
        @SerializedName("should_pay")
        Float shouldPay;
        @SerializedName("verificate")
        Float verificate;
        @SerializedName("wire")
        Float wire;

        @SerializedName("shop_id")
        Integer shop;
        @SerializedName("employee_id")
        String employee;
        @SerializedName("retailer_id")
        String retailer;

        @SerializedName("total")
        String total;
        @SerializedName("type")
        String type;
        @SerializedName("state")
        String state;
        @SerializedName("entry_date")
        String entryDate;

        SaleDetail(){

        }

        public Integer getId() {
            return this.id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getOrderId() {
            return this.orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public String getRsn() {
            return this.rsn;
        }

        public void setRsn(String rsn) {
            this.rsn = rsn;
        }

        public Float getBalance() {
            return this.balance;
        }

        public void setBalance(Float balance) {
            this.balance = balance;
        }

        public Float getCard() {
            return this.card;
        }

        public void setCard(Float card) {
            this.card = card;
        }

        public Float getCash() {
            return this.cash;
        }

        public void setCash(Float cash) {
            this.cash = cash;
        }

        public Float getEpay() {
            return this.epay;
        }

        public void setEpay(Float epay) {
            this.epay = epay;
        }

        public Float getHasPay() {
            return this.hasPay;
        }

        public void setHasPay(Float hasPay) {
            this.hasPay = hasPay;
        }

        public Float getShouldPay() {
            return this.shouldPay;
        }

        public void setShouldPay(Float shouldPay) {
            this.shouldPay = shouldPay;
        }

        public Float getVerificate() {
            return this.verificate;
        }

        public void setVerificate(Float verificate) {
            this.verificate = verificate;
        }

        public Float getWire() {
            return this.wire;
        }

        public void setWire(Float wire) {
            this.wire = wire;
        }

        public Integer getShop() {
            return this.shop;
        }

        public void setShop(Integer shop) {
            this.shop = shop;
        }

        public String getEmployee() {
            return this.employee;
        }

        public void setEmployee(String employee) {
            this.employee = employee;
        }

        public String getRetailer() {
            return this.retailer;
        }

        public void setRetailer(String retailer) {
            this.retailer = retailer;
        }

        public String getTotal() {
            return this.total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getState() {
            return this.state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getEntryDate() {
            return this.entryDate;
        }

        public void setEntryDate(String entryDate) {
            this.entryDate = entryDate;
        }
    }

    public SaleDetailResponse(){
        super();
    }

    public Integer getTotal() {
        return mTotal;
    }

    public void setTotal(Integer mTotal) {
        this.mTotal = mTotal;
    }

    public Integer getAmount() {
        return mAmount;
    }

    public void setAmount(Integer mAmount) {
        this.mAmount = mAmount;
    }

    public Float getCard() {
        return mCard;
    }

    public void setCard(Float mCard) {
        this.mCard = mCard;
    }

    public Float getCash() {
        return mCash;
    }

    public void setCash(Float mCash) {
        this.mCash = mCash;
    }

    public Float getEPay() {
        return mEPay;
    }

    public void setEPay(Float mEPay) {
        this.mEPay = mEPay;
    }

    public Float getHPay() {
        return mHPay;
    }

    public void setHPay(Float mHPay) {
        this.mHPay = mHPay;
    }

    public Float getSPay() {
        return mSPay;
    }

    public void setSPay(Float spay) {
        this.mSPay = spay;
    }

    public Float getVerificate() {
        return mVerificate;
    }

    public void setVerificate(Float verificate) {
        this.mVerificate = verificate;
    }

    public Float getWire() {
        return mWire;
    }

    public void setWire(Float wire) {
        this.mWire = wire;
    }

    public List<SaleDetail> getSaleDetail() {
        return mSaleDetail;
    }

    public void setSaleDetail(List<SaleDetail> saleDetail) {
        this.mSaleDetail = saleDetail;
    }
}
