package com.diablo.dt.diablo.view.good;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

/**
 * Created by buxianhui on 17/4/8.
 */

public class DiabloGoodCalcView {
    private View mStyleNumber;
    private View mBrand;
    private View mGoodType;
    private View mFirm;
    private View mSex;
    private View mYear;
    private View mSeason;

    private View mOrgPrice;
    private View mPkgPrice;
    private View mTagPrice;

    private View mColor;
    private View mSize;

    public DiabloGoodCalcView() {

    }

    public void setStyleNumberText(String styleNumber) {
        ((EditText)mStyleNumber).setText(styleNumber);
    }

    public void setBrandText(String brand) {
        ((AutoCompleteTextView)mBrand).setText(brand);
    }

    public void setGoodTypeText(String type) {
        ((AutoCompleteTextView)mGoodType).setText(type);
    }

    public void setFirmText(String firm) {
        ((AutoCompleteTextView)mFirm).setText(firm);
    }

    public void setOrgPriceText(Float orgPrice) {
        ((EditText)mOrgPrice).setText(DiabloUtils.instance().toString(orgPrice));
    }

    public void setPkgPriceText(Float pkgPrice) {
        ((EditText)mPkgPrice).setText(DiabloUtils.instance().toString(pkgPrice));
    }

    public void setTagPriceText(Float tagPrice) {
        ((EditText)mTagPrice).setText(DiabloUtils.instance().toString(tagPrice));
    }

    public void setColorText(String color) {
        ((EditText)mColor).setText(color);
    }

    public void setSizeText(String size) {
        ((EditText)mSize).setText(size);
    }

    public void reset() {
        if (null != mStyleNumber) {
            ((EditText)mStyleNumber).setText(DiabloEnum.EMPTY_STRING);
        }

        if (null != mBrand) {
            ((AutoCompleteTextView)mBrand).setText(DiabloEnum.EMPTY_STRING);
        }

        if (null != mGoodType) {
            ((AutoCompleteTextView)mGoodType).setText(DiabloEnum.EMPTY_STRING);
        }

        if (null != mFirm) {
            ((AutoCompleteTextView)mFirm).setText(DiabloEnum.EMPTY_STRING);
        }

        if (null != mOrgPrice) {
            ((EditText)mOrgPrice).setText(DiabloEnum.EMPTY_STRING);
        }

        if (null != mPkgPrice) {
            ((EditText)mPkgPrice).setText(DiabloEnum.EMPTY_STRING);
        }

        if (null != mTagPrice) {
            ((EditText)mTagPrice).setText(DiabloEnum.EMPTY_STRING);
        }

        if (null != mColor) {
            ((EditText)mColor).setText(DiabloEnum.EMPTY_STRING);
        }

        if (null != mSize) {
            ((EditText)mSize).setText(DiabloEnum.EMPTY_STRING);
        }
    }

    public View getStyleNumber() {
        return mStyleNumber;
    }

    public void setStyleNumber(View styleNumber) {
        this.mStyleNumber = styleNumber;
    }

    public View getBrand() {
        return mBrand;
    }

    public void setBrand(View brand) {
        this.mBrand = brand;
    }

    public View getGoodType() {
        return mGoodType;
    }

    public void setGoodType(View goodType) {
        this.mGoodType = goodType;
    }

    public View getFirm() {
        return mFirm;
    }

    public void setFirm(View firm) {
        this.mFirm = firm;
    }

    public View getSex() {
        return mSex;
    }

    public void setSex(View sex) {
        this.mSex = sex;
    }

    public View getYear() {
        return mYear;
    }

    public void setYear(View year) {
        this.mYear = year;
    }

    public View getSeason() {
        return mSeason;
    }

    public void setSeason(View season) {
        this.mSeason = season;
    }

    public View getOrgPrice() {
        return mOrgPrice;
    }

    public void setOrgPrice(View orgPrice) {
        this.mOrgPrice = orgPrice;
    }

    public String getOrgPriceStringValue() {
        return ((EditText)mOrgPrice).getText().toString().trim();
    }

    public View getPkgPrice() {
        return mPkgPrice;
    }

    public String getPkgPriceStringValue() {
        return ((EditText)mPkgPrice).getText().toString().trim();
    }

    public void setPkgPrice(View pkgPrice) {
        this.mPkgPrice = pkgPrice;
    }

    public View getTagPrice() {
        return mTagPrice;
    }

    public String getTagPriceStringValue() {
        return ((EditText)mTagPrice).getText().toString().trim();
    }

    public void setTagPrice(View tagPrice) {
        this.mTagPrice = tagPrice;
    }

    public View getColor() {
        return mColor;
    }

    public void setColor(View color) {
        this.mColor = color;
    }

    public View getSize() {
        return mSize;
    }

    public void setSize(View size) {
        this.mSize = size;
    }
}
