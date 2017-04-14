package com.diablo.dt.diablo.request.sale;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.request.PageRequest;

import java.util.ArrayList;
import java.util.List;

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

    public void addRetailer(Integer retailer) {
        mCondition.addRetailer(retailer);
    }

    public void removeRetailer(Integer retailer) {
        mCondition.removeRetailer(retailer);
    }

    public void addShop(Integer shop) {
        mCondition.addShop(shop);
    }

    public void removeShop(Integer shop) {
        mCondition.removeShop(shop);
    }

    public void addRSN(String rsn) {
        mCondition.addRSN(rsn);
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
        private List<Integer> mRetailers;
        @SerializedName("shop")
        private List<Integer> mShops;
        @SerializedName("rsn")
        private List<String> mRSNs;

        public Condition(){
            mRetailers = new ArrayList<>();
            mShops = new ArrayList<>();
            mRSNs = new ArrayList<>();
        }

        public void setStartTime(String startTime) {
            this.mStartTime = startTime;
        }

        public void setEndTime(String endTime) {
            this.mEndTime = endTime;
        }

        private void addRetailer(Integer retailer) {
            if (null != mRetailers) {
                if (!mRetailers.contains(retailer)) {
                    this.mRetailers.add(retailer);
                }
            }
        }

        private void removeRetailer(Integer retailer) {
            if (null != mRetailers) {
                mRetailers.remove(retailer);
            }
        }

        private void addShop(Integer shop) {
            if (null != mShops) {
                if (!mShops.contains(shop)) {
                    this.mShops.add(shop);
                }
            }
        }

        private void removeShop(Integer shop) {
            if (null != mShops) {
                mShops.remove(shop);
            }
        }

        private void addRSN(String rsn) {
            if (null != mRSNs) {
                if (!mRSNs.contains(rsn)) {
                    this.mRSNs.add(rsn);
                }
            }
        }
    }
}
