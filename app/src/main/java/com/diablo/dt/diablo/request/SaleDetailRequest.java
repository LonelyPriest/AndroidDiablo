package com.diablo.dt.diablo.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/2/23.
 */

public class SaleDetailRequest extends PageRequest {
    @SerializedName("fields")
    Condtion mCondtion;

    public SaleDetailRequest(){
        super();
        mCondtion = new Condtion();
    }

    public Condtion getCondtion() {
        return mCondtion;
    }

    public void setCondtion(Condtion condtion) {
        this.mCondtion = condtion;
    }

    public class Condtion{
        @SerializedName("start_time")
        String mStartTime;
        @SerializedName("end_time")
        String mEndTime;

        public Condtion(){

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
