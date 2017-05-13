package com.diablo.dt.diablo.response.sale;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.Response;

/**
 * Created by buxianhui on 17/5/13.
 */

public class SalePrintContentResponse extends Response {
    @SerializedName("content")
    private String content;

//    public SalePrintContentResponse() {
//        this.content = DiabloEnum.EMPTY_STRING;
//    }

    public String getContent() {
        return content;
    }
}
