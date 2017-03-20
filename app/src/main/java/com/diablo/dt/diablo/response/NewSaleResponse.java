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
    private String style_number;
    @SerializedName("order_id")
    private Integer orderId;
    @SerializedName("cbalance")
    private Float currentBalance;
    @SerializedName("lbalance")
    private Float lastBalance;

    @SerializedName("pinfo")
    private List<PrinterResponse> pinfos;

    public NewSaleResponse() {

    }

    private class PrinterResponse {
        @SerializedName("device")
        private Integer device;
        @SerializedName("ecode")
        private Integer ecode;

        private PrinterResponse() {

        }
    }
}
