package com.diablo.dt.diablo.request.report;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.request.PageRequest;

/**
 * Created by buxianhui on 17/6/3.
 */

public class DailyReportRequest extends PageRequest {
    @SerializedName("fields")
    private Condition mCondition;

    public DailyReportRequest(Integer currentPage, Integer itemsPerPage){
        super(currentPage, itemsPerPage);
        mCondition = new Condition();
    }

    public void setStartTime(String startTime) {
        mCondition.setStartTime(startTime);
    }

    public void setEndTime(String endTime) {
        mCondition.setEndTime(endTime);
    }

    public static class Condition{
        @SerializedName("start_time")
        private String mStartTime;
        @SerializedName("end_time")
        private String mEndTime;


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
    }
}
