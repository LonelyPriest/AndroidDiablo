package com.diablo.dt.diablo.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by buxianhui on 17/3/20.
 */

public class NewSaleResponse extends Response {
    @SerializedName("rsn")
    private String rsn;

    @SerializedName("pcode")
    private Integer pcode;

    @SerializedName("style_number")
    private String styleNumber;
    @SerializedName("order_id")
    private Integer orderId;
    @SerializedName("cbalance")
    private Float currentBalance;
    @SerializedName("lbalance")
    private Float lastBalance;

    @SerializedName("pinfo")
    private List<printResponse> pinfos;

    public NewSaleResponse() {

    }

    public static class printResponse {
        @SerializedName("device")
        private Integer device;
        @SerializedName("ecode")
        private Integer ecode;

        private printResponse() {

        }

        public Integer getDevice() {
            return device;
        }

        public Integer getEcode() {
            return ecode;
        }
    }

    public String getRsn() {
        return rsn;
    }

    public Integer getPcode() {
        return pcode;
    }

    public String getStyleNumber() {
        return styleNumber;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Float getCurrentBalance() {
        return currentBalance;
    }

    public Float getLastBalance() {
        return lastBalance;
    }

    public List<printResponse> getPinfos() {
        return pinfos;
    }
}
