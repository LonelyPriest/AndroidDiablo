package com.diablo.dt.diablo.request.sale;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.request.PageRequest;

/**
 * Created by buxianhui on 17/2/23.
 */

public class SaleDetailRequest extends PageRequest {
    @SerializedName("fields")
    private Condition mCondition;

//    public SaleDetailRequest(){
//        super();
//        mCondition = new Condition();
//    }
//
//    public SaleDetailRequest(Integer currentPage){
//        super(currentPage, DiabloEnum.DEFAULT_ITEMS_PER_PAGE);
//        mCondition = new Condition();
//    }

    public SaleDetailRequest(Integer currentPage, Integer itemsPerPage){
        super(currentPage, itemsPerPage);
        mCondition = new Condition();
    }

    public void setStartTime(String startTime) {
        mCondition.setStartTime(startTime);
    }

    public void setEndTime(String endTime) {
        mCondition.setEndTime(endTime);
    }

    public void setRetailer(Integer retailer) {
        mCondition.setRetailer(retailer);
    }

//    public Condition getCondition() {
//        return mCondition;
//    }
//
//    public void setCondition(Condition condition) {
//        this.mCondition = condition;
//    }

    private static class Condition{
        @SerializedName("start_time")
        private String mStartTime;
        @SerializedName("end_time")
        private String mEndTime;
        @SerializedName("retailer")
        private Integer mRetailer;

        public Condition(){

        }

        public void setStartTime(String startTime) {
            this.mStartTime = startTime;
        }

        public void setEndTime(String endTime) {
            this.mEndTime = endTime;
        }

        private void setRetailer(Integer retailer) {
            this.mRetailer = retailer;
        }
    }
}
