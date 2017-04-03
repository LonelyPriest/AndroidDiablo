package com.diablo.dt.diablo.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/4/3.
 */

public class MatchGoodRequest {
    @SerializedName("firm")
    private Integer firm;
    @SerializedName("start_time")
    private String startTime;

    public MatchGoodRequest(Integer firm, String startTime) {
        this.firm = firm;
        this.startTime = startTime;
    }

    public MatchGoodRequest(String startTime) {
        this.firm = null;
        this.startTime = startTime;
    }

    public Integer getFirm() {
        return firm;
    }

    public void setFirm(Integer firm) {
        this.firm = firm;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
