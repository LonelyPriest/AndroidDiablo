package com.diablo.dt.diablo.request.stock;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.request.PageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/3.
 */

public class StockDetailRequest extends PageRequest {
    @SerializedName("fields")
    private Condition mCondition;

//    public StockDetailRequest(){
//        super();
//    }
//
//    public StockDetailRequest(Integer currentPage){
//        super(currentPage, DiabloEnum.DEFAULT_ITEMS_PER_PAGE);
//    }

    public StockDetailRequest(Integer currentPage, Integer itemsPerPage){
        super(currentPage, itemsPerPage);
        mCondition = new Condition();
    }

//    public Condition getCondition() {
//        return mCondition;
//    }
//
//    public void setCondition(Condition condition) {
//        this.mCondition = condition;
//    }

    public void setStartTime(String startTime) {
        mCondition.setStartTime(startTime);
    }

    public void setEndTime(String endTime) {
        mCondition.setEndTime(endTime);
    }

    public void addShop(Integer shop) {
        mCondition.addShop(shop);
    }

    public void addFirm(Integer firm) {
        mCondition.addFirm(firm);
    }

    public void addStockType(Integer stockType) {
        mCondition.addStockType(stockType);
    }

    public List<Integer> getShops() {
        return mCondition.getShops();
    }

    public void setShops(List<Integer> shops) {
        mCondition.setShops(shops);
    }

    public void trim() {
        mCondition.trim();
    }

    private static class Condition{
        @SerializedName("start_time")
        private String mStartTime;
        @SerializedName("end_time")
        private String mEndTime;

        @SerializedName("firm")
        private List<Integer> mFirms;
        @SerializedName("purchaser_type")
        private List<Integer> mStockTypes;
        @SerializedName("shop")
        private List<Integer> mShops;


        public Condition(){
            mFirms = new ArrayList<>();
            mShops = new ArrayList<>();
            mStockTypes = new ArrayList<>();
        }

        public void setStartTime(String startTime) {
            this.mStartTime = startTime;
        }

        public void setEndTime(String endTime) {
            this.mEndTime = endTime;
        }


        private void addFirm(Integer firm) {
            if (!mFirms.contains(firm)) {
                mFirms.add(firm);
            }
        }

        private void addShop(Integer shop) {
            if (!mShops.contains(shop)) {
                this.mShops.add(shop);
            }
        }

        private void addStockType(Integer stockType) {
            if (!mStockTypes.contains(stockType)) {
                this.mStockTypes.add(stockType);
            }
        }


        private void setShops(List<Integer> shops) {
            this.mShops = shops;
        }

        private List<Integer> getShops() {
            return mShops;
        }

        private void trim() {
            if (0 == mFirms.size())
                mFirms = null;
            if (0 == mShops.size())
                mShops = null;
            if (0 == mStockTypes.size())
                mStockTypes = null;
        }
    }
}
