package com.diablo.dt.diablo.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by buxianhui on 17/2/23.
 */

public class SaleDetailResponse extends Response{
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
        Integer retailer;

        @SerializedName("total")
        Integer total;
        @SerializedName("type")
        Integer type;
        @SerializedName("state")
        Integer state;
        @SerializedName("entry_date")
        String entryDate;

        SaleDetail(){

        }

        public Integer getId() {
            return this.id;
        }

        public Integer getOrderId() {
            return this.orderId;
        }

        public void setOrderId (Integer orderId){
            this.orderId = orderId;
        }

        public String getRsn() {
            return this.rsn;
        }

        public Float getBalance() {
            return this.balance;
        }

        public Float getCard() {
            return this.card;
        }

        public Float getCash() {
            return this.cash;
        }

        public Float getEpay() {
            return this.epay;
        }

        public Float getHasPay() {
            return this.hasPay;
        }

        public Float getShouldPay() {
            return this.shouldPay;
        }

        public Float getVerificate() {
            return this.verificate;
        }

        public Float getWire() {
            return this.wire;
        }

        public Integer getShop() {
            return this.shop;
        }

        public String getEmployee() {
            return this.employee;
        }

        public Integer getRetailer() {
            return this.retailer;
        }

        public Integer getTotal() {
            return this.total;
        }

        public Integer getType() {
            return this.type;
        }

        public Integer getState() {
            return this.state;
        }

        public String getEntryDate() {
            return this.entryDate;
        }
    }

    public SaleDetailResponse(){
        super();
    }

    public Integer getTotal() {
        return mTotal;
    }

    public Integer getAmount() {
        return mAmount;
    }

    public Float getCard() {
        return mCard;
    }

    public Float getCash() {
        return mCash;
    }

    public Float getEPay() {
        return mEPay;
    }

    public Float getHPay() {
        return mHPay;
    }

    public Float getSPay() {
        return mSPay;
    }

    public Float getVerificate() {
        return mVerificate;
    }

    public Float getWire() {
        return mWire;
    }

    public List<SaleDetail> getSaleDetail() {
        return mSaleDetail;
    }
}
