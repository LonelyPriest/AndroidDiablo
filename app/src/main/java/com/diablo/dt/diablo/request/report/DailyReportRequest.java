package com.diablo.dt.diablo.request.report;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by buxianhui on 17/5/31.
 */

public class DailyReportRequest {
    @SerializedName("condition")
    private Condition condition;

    public DailyReportRequest(String startTime, String endTime, List<Integer> shops) {
        condition = new Condition(startTime, endTime, shops);
    }

    public void setStartTime(String startTime) {
        condition.setStartTime(startTime);
    }

    public void setEndTime(String endTime) {
        condition.setEndTime(endTime);
    }

    public void setShops(List<Integer> shops) {
        condition.setShops(shops);
    }

    private static class Condition {
        @SerializedName("start_time")
        private String startTime;
        @SerializedName("end_time")
        private String endTime;
        @SerializedName("shop")
        private List<Integer> shops;

        private Condition(String startTime, String endTime, List<Integer> shops) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.shops = shops;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public void setShops(List<Integer> shops) {
            this.shops = shops;
        }
    }
}
