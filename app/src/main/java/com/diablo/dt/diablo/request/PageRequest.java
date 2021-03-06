package com.diablo.dt.diablo.request;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.utils.DiabloEnum;

/**
 * Created by buxianhui on 17/2/23.
 */

public class PageRequest {
    @SerializedName("math")
    private String mMatch = "and";
    @SerializedName("page")
    private Integer mPage;
    @SerializedName("count")
    private Integer mCount;

    public PageRequest(){
        mPage = DiabloEnum.DEFAULT_PAGE;
        mCount = DiabloEnum.DEFAULT_ITEMS_PER_PAGE;
    }

    public PageRequest(Integer page, Integer count){
        this.mPage = page;
        this.mCount = count;
    }

    public String getMatch() {
        return mMatch;
    }

    public void setMatch(String match) {
        this.mMatch = match;
    }

    public Integer getPage() {
        return mPage;
    }

    public void setPage(Integer page) {
        this.mPage = page;
    }

    public Integer getCount() {
        return mCount;
    }

    public void setCount(Integer count) {
        this.mCount = count;
    }

    public Integer getPageStartIndex(){
        return (mPage - 1) * mCount + 1;
    }

}
