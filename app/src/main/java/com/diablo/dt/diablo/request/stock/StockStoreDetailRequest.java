package com.diablo.dt.diablo.request.stock;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.request.PageRequest;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/7.
 */

public class StockStoreDetailRequest extends PageRequest {
    @SerializedName("fields")
    private Condition mCondition;

    public StockStoreDetailRequest(Integer currentPage, Integer itemsPerPage){
        super(currentPage, itemsPerPage);
        mCondition = new Condition();
    }
    
    public void setStartTime(String startTime) {
        mCondition.setStartTime(startTime);
    }

    public void setEndTime(String endTime) {
        mCondition.setEndTime(endTime);
    }

    public void addStyleNumber(String styleNumber) {
        mCondition.addStyleNumber(styleNumber);
    }

    public void addBrand(Integer brand) {
        mCondition.addBrand(brand);
    }

    public void addGoodType(Integer type) {
        mCondition.addGoodType(type);
    }

    public void addFirm(Integer firm) {
        mCondition.addFirm(firm);
    }

    public void addYear(String year) {
        mCondition.addYear(DiabloUtils.instance().toInteger(year));
    }

    public void addShop(Integer shop) {
        mCondition.addShop(shop);
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

        @SerializedName("style_number")
        private List<String> mStyleNumbers;
        @SerializedName("brand")
        private List<Integer> mBrands;
        @SerializedName("type")
        private List<Integer> mGoodTypes;
        @SerializedName("firm")
        private List<Integer> mFirms;
        @SerializedName("year")
        private List<Integer> mYears;

        @SerializedName("shop")
        private List<Integer> mShops;

        private Condition(){
            mStyleNumbers = new ArrayList<>();
            mBrands = new ArrayList<>();
            mGoodTypes = new ArrayList<>();
            mFirms = new ArrayList<>();
            mYears = new ArrayList<>();

            mShops = new ArrayList<>();
        }

        private void setStartTime(String startTime) {
            this.mStartTime = startTime;
        }

        private void setEndTime(String endTime) {
            this.mEndTime = endTime;
        }

        private void addStyleNumber(String styleNumber) {
            if (!mStyleNumbers.contains(styleNumber)) {
                mStyleNumbers.add(styleNumber);
            }
        }

        private void addBrand(Integer brand) {
            if (!mBrands.contains(brand)) {
                mBrands.add(brand);
            }
        }

        private void addGoodType(Integer type) {
            if (!mGoodTypes.contains(type)) {
                mGoodTypes.add(type);
            }
        }

        private void addFirm(Integer firm) {
            if (!mFirms.contains(firm)) {
                mFirms.add(firm);
            }
        }

        private void addYear(Integer year) {
            if (!mYears.contains(year)) {
                mYears.add(year);
            }
        }

        private void addShop(Integer shop) {
            if (!mShops.contains(shop)) {
                this.mShops.add(shop);
            }
        }

        private void setShops(List<Integer> shops) {
            this.mShops = shops;
        }

        private List<Integer> getShops() {
            return mShops;
        }

        private void trim() {
            if (0 == mStyleNumbers.size())
                mStyleNumbers = null;
            if (0 == mBrands.size())
                mBrands = null;
            if (0 == mGoodTypes.size())
                mGoodTypes = null;
            if (0 == mFirms.size())
                mFirms = null;
            if (0 == mYears.size())
                mYears = null;
            if (0 == mShops.size())
                mShops = null;
        }
    }
}
