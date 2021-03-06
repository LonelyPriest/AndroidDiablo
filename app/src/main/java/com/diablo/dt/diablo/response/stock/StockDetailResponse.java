package com.diablo.dt.diablo.response.stock;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/3.
 */

public class StockDetailResponse extends Response {
    @SerializedName("total")
    private Integer mTotal;

    @SerializedName("t_amount")
    private Integer mAmount;

    @SerializedName("t_card")
    private Float mCard;

    @SerializedName("t_cash")
    private Float mCash;

    @SerializedName("t_hpay")
    private Float mHPay;

    @SerializedName("t_spay")
    private Float mSPay;

    @SerializedName("t_verificate")
    private Float mVerificate;

    @SerializedName("t_wire")
    private Float mWire;

    @SerializedName("data")
    private List<StockDetail> mStockDetails;

    public StockDetailResponse() {
        super();
        mTotal = 0;
        mAmount = 0;
        mCard = 0f;
        mHPay = 0f;
        mSPay = 0f;
        mVerificate = 0f;
        mWire = 0f;

        mStockDetails = new ArrayList<>();
    }

    public static class StockDetail{
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
        @SerializedName("e_pay_type")
        Integer ePayType;
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
        @SerializedName("firm_id")
        Integer firm;

        @SerializedName("total")
        Integer total;
        @SerializedName("type")
        Integer type;
        // checked, unchecked
        @SerializedName("state")
        Integer state;
        @SerializedName("entry_date")
        String entryDate;
        @SerializedName("comment")
        String comment;

        StockDetail(){

        }

        public Integer getId() {
            return this.id;
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

        public Integer getEPayType() {
            return ePayType;
        }

        public Float getEPay() {
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

        public Integer getFirm() {
            return this.firm;
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

        public String getComment() {
            return comment;
        }
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

    public List<StockDetail> getSaleDetail() {
        return mStockDetails;
    }
}
