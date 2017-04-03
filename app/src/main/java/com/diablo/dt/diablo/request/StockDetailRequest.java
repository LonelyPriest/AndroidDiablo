package com.diablo.dt.diablo.request;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.utils.DiabloEnum;

/**
 * Created by buxianhui on 17/4/3.
 */

public class StockDetailRequest extends PageRequest {
    @SerializedName("fields")
    private Condition mCondition;

    public StockDetailRequest(){
        super();
    }

    public StockDetailRequest(Integer currentPage){
        super(currentPage, DiabloEnum.DEFAULT_ITEMS_PER_PAGE);
    }

    public StockDetailRequest(Integer currentPage, Integer itemsPerPage){
        super(currentPage, itemsPerPage);
    }

    public Condition getCondition() {
        return mCondition;
    }

    public void setCondition(Condition condition) {
        this.mCondition = condition;
    }

    public static class Condition{
        @SerializedName("start_time")
        String mStartTime;
        @SerializedName("end_time")
        String mEndTime;

        public Condition(){

        }

        public String getStartTime() {
            return mStartTime;
        }

        public void setStartTime(String startTime) {
            this.mStartTime = startTime;
        }

        public String getEndTime() {
            return mEndTime;
        }

        public void setEndTime(String endTime) {
            this.mEndTime = endTime;
        }
    }
}
