package com.diablo.dt.diablo.response;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.sale.NewSaleResponse;

import java.util.List;

/**
 * Created by buxianhui on 17/3/20.
 */

public class PrintResponse extends Response {
    @SerializedName("pcode")
    private Integer pcode;

    @SerializedName("pinfo")
    private List<NewSaleResponse.printResponse> pinfos;

    public PrintResponse() {

    }

    public Integer getPCode() {
        return pcode;
    }

    public List<NewSaleResponse.printResponse> getPInfos() {
        return pinfos;
    }
}
