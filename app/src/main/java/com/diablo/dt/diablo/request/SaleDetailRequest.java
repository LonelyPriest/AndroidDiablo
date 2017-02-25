package com.diablo.dt.diablo.request;

import com.diablo.dt.diablo.entity.DiabloEnum;
import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/2/23.
 */

public class SaleDetailRequest extends PageRequest {
    @SerializedName("fields")
    private Condition mCondition;

    public SaleDetailRequest(){
        super();
    }

    public SaleDetailRequest(Integer currentPage){
        super(currentPage, DiabloEnum.DEFAULT_ITEMS_PER_PAGE);
    }

    public SaleDetailRequest(Integer currentPage, Integer itemsPerPage){
        super(currentPage, itemsPerPage);
    }

    public Condition getCondtion() {
        return mCondition;
    }

    public void setCondtion(Condition condition) {
        this.mCondition = condition;
    }

    public class Condition{
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
