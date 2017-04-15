package com.diablo.dt.diablo.request.sale;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.request.PageRequest;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/3/28.
 */

public class SaleNoteRequest extends PageRequest {
    @SerializedName("fields")
    private Condition mCondition;

//    public SaleNoteRequest(){
//        super();
//    }
//
//    public SaleNoteRequest(Integer currentPage){
//        super(currentPage, DiabloEnum.DEFAULT_ITEMS_PER_PAGE);
//    }

    public SaleNoteRequest(Integer currentPage, Integer itemsPerPage){
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

    public void addRetailer(Integer retailer) {
        mCondition.addRetailer(retailer);
    }

    public void addShop(Integer shop) {
        mCondition.addShop(shop);
    }

    public void addEmployee(String employee) {
        mCondition.addEmployee(employee);
    }

    public void addRSN(String rsn) {
        mCondition.addRSN(rsn);
    }

    public void addSellType(Integer sellType) {
        mCondition.addSellType(sellType);
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

        @SerializedName("retailer")
        private List<Integer> mRetailers;
        @SerializedName("shop")
        private List<Integer> mShops;
        @SerializedName("employ")
        private List<String> mEmployees;
        @SerializedName("sell_type")
        private List<Integer> mSellTypes;
        @SerializedName("rsn")
        private List<String> mRSNs;

        public Condition(){
            mStyleNumbers = new ArrayList<>();
            mBrands = new ArrayList<>();
            mGoodTypes = new ArrayList<>();
            mFirms = new ArrayList<>();
            mYears = new ArrayList<>();

            mRetailers = new ArrayList<>();
            mShops = new ArrayList<>();
            mEmployees = new ArrayList<>();
            mSellTypes = new ArrayList<>();

            mRSNs = new ArrayList<>();
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

        private void addRetailer(Integer retailer) {
            if (!mRetailers.contains(retailer)) {
                this.mRetailers.add(retailer);
            }
        }

        private void addShop(Integer shop) {
            if (!mShops.contains(shop)) {
                this.mShops.add(shop);
            }
        }

        private void addEmployee(String employee) {
            if (!mEmployees.contains(employee)) {
                this.mEmployees.add(employee);
            }
        }

        private void addRSN(String rsn) {
            if (!mRSNs.contains(rsn)) {
                this.mRSNs.add(rsn);
            }
        }

        private void addSellType(Integer sellType) {
            if (!mSellTypes.contains(sellType)) {
                this.mSellTypes.add(sellType);
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
            if (0 == mRetailers.size())
                mRetailers = null;
            if (0 == mShops.size())
                mShops = null;
            if (0 == mEmployees.size())
                mEmployees = null;
            if (0 == mSellTypes.size())
                mSellTypes = null;
            if (0 == mRSNs.size())
                mRSNs = null;
        }

//        public String getStartTime() {
//            return mStartTime;
//        }
//
//        public String getEndTime() {
//            return mEndTime;
//        }
//
//        public List<String> getStyleNumbers() {
//            return mStyleNumbers;
//        }
//
//        public List<Integer> getBrands() {
//            return mBrands;
//        }
//
//        public List<Integer> getGoodTypes() {
//            return mGoodTypes;
//        }
//
//        public List<Integer> getFirms() {
//            return mFirms;
//        }
//
//        public List<Integer> getYears() {
//            return mYears;
//        }
//
//        public List<Integer> getRetailers() {
//            return mRetailers;
//        }
//

//
//        public List<String> getEmployees() {
//            return mEmployees;
//        }
//
//        public List<Integer> getSellTypes() {
//            return mSellTypes;
//        }
//
//        public List<String> getRSNs() {
//            return mRSNs;
//        }
    }
}
