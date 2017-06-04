package com.diablo.dt.diablo.request.report;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by buxianhui on 17/6/4.
 */

public class MonthReportRequest {

    @SerializedName("condition")
    private Condition mCondition;

    public MonthReportRequest(){
        mCondition = new Condition();
    }

    public void setStartTime(String startTime) {
        mCondition.setStartTime(startTime);
    }

    public void setEndTime(String endTime) {
        mCondition.setEndTime(endTime);
    }

    public void setShops(List<Integer> shops) {
        mCondition.setShops(shops);
    }

    public static class Condition{
        @SerializedName("start_time")
        private String mStartTime;
        @SerializedName("end_time")
        private String mEndTime;
        @SerializedName("shop")
        private List<Integer> shops;

        public Condition(){

        }

        public Condition(String startTime, String endTime) {
            this.mStartTime = startTime;
            this.mEndTime = endTime;
        }

        public void setStartTime(String startTime) {
            this.mStartTime = startTime;
        }

        public void setEndTime(String endTime) {
            this.mEndTime = endTime;
        }

        public void setShops(List<Integer> shops) {
            this.shops = shops;
        }
    }
}
