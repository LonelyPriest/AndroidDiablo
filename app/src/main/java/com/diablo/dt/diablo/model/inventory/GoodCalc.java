package com.diablo.dt.diablo.model.inventory;

import com.diablo.dt.diablo.entity.DiabloBrand;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.DiabloSizeGroup;
import com.diablo.dt.diablo.entity.DiabloType;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.utils.DiabloEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/8.
 */

public class GoodCalc {
    private String      mStyleNumber;
    private DiabloBrand mBrand;
    private DiabloType  mGoodType;
    private Firm        mFirm;

    private Integer mSex;
    private Integer mYear;
    private Integer mSeason;

    private Float mOrgPrice;
    private Float mPkgPrice;
    private Float mTagPrice;
    private Float mPrice3;
    private Float mPrice4;
    private Float mPrice5;
    private Float mDiscount;

    private Integer mAlarmDay;
    private String  mPath;

    private Integer mFree;

    public GoodCalc() {
        mOrgPrice = 0f;
        mPkgPrice = 0f;
        mTagPrice = 0f;

        mPrice3 = 0f;
        mPrice4 = 0f;
        mPrice5 = 0f;

        mDiscount = 100f;
        mAlarmDay = 7;

        mFree = DiabloEnum.DIABLO_FREE;

        mColors = new ArrayList<>();
        mSizeGroups = new ArrayList<>();
    }

    private List<DiabloColor> mColors;
    private List<DiabloSizeGroup> mSizeGroups;

    public String getStyleNumber() {
        return mStyleNumber;
    }

    public void setStyleNumber(String styleNumber) {
        this.mStyleNumber = styleNumber;
    }

    public DiabloBrand getBrand() {
        return mBrand;
    }

    public void setBrand(DiabloBrand brand) {
        this.mBrand = brand;
    }

    public DiabloType getGoodType() {
        return mGoodType;
    }

    public void setGoodType(DiabloType goodType) {
        this.mGoodType = goodType;
    }

    public Firm getFirm() {
        return mFirm;
    }

    public void setFirm(Firm firm) {
        this.mFirm = firm;
    }

    public Integer getSex() {
        return mSex;
    }

    public void setSex(Integer sex) {
        this.mSex = sex;
    }

    public Integer getYear() {
        return mYear;
    }

    public void setYear(Integer year) {
        this.mYear = year;
    }

    public Integer getSeason() {
        return mSeason;
    }

    public void setSeason(Integer season) {
        this.mSeason = season;
    }

    public Float getOrgPrice() {
        return mOrgPrice;
    }

    public void setOrgPrice(Float orgPrice) {
        this.mOrgPrice = orgPrice;
    }

    public Float getPkgPrice() {
        return mPkgPrice;
    }

    public void setPkgPrice(Float pkgPrice) {
        this.mPkgPrice = pkgPrice;
    }

    public Float getTagPrice() {
        return mTagPrice;
    }

    public void setTagPrice(Float tagPrice) {
        this.mTagPrice = tagPrice;
    }

    public List<DiabloColor> getColors() {
        return mColors;
    }

    public void addColor(DiabloColor color) {
        this.mColors.add(color);
    }

    public List<DiabloSizeGroup> getSizeGroups() {
        return mSizeGroups;
    }

    public void addSizeGroup(DiabloSizeGroup sizeGroup) {
        this.mSizeGroups.add(sizeGroup);
    }


    public Float getPrice3() {
        return mPrice3;
    }

    public void setPrice3(Float price3) {
        this.mPrice3 = price3;
    }

    public Float getPrice4() {
        return mPrice4;
    }

    public void setPrice4(Float price4) {
        this.mPrice4 = price4;
    }

    public Float getPrice5() {
        return mPrice5;
    }

    public void setPrice5(Float price5) {
        this.mPrice5 = price5;
    }

    public Float getDiscount() {
        return mDiscount;
    }

    public void setDiscount(Float discount) {
        this.mDiscount = discount;
    }

    public Integer getAlarmDay() {
        return mAlarmDay;
    }

    public void setAlarmDay(Integer alarmDay) {
        this.mAlarmDay = alarmDay;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    public Integer getFree() {
        return mFree;
    }

    public void setFree(Integer free) {
        this.mFree = free;
    }
}
