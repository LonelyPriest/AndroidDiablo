package com.diablo.dt.diablo.model.good;

import com.diablo.dt.diablo.entity.DiabloBrand;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.DiabloSizeGroup;
import com.diablo.dt.diablo.entity.DiabloType;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.entity.MatchGood;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

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

    public GoodCalc(MatchGood good) {
        mStyleNumber = good.getStyleNumber();

        mBrand = new DiabloBrand(Profile.instance().getBrand(good.getBrandId()));
        mGoodType = new DiabloType(Profile.instance().getDiabloType(good.getTypeId()));
        mFirm = new Firm(Profile.instance().getFirm(good.getFirmId()));

        mSex = good.getSex();
        mYear = good.getYear();
        mSeason = good.getSeason();

        mOrgPrice = good.getOrgPrice();
        mPkgPrice = good.getPkgPrice();
        mTagPrice = good.getTagPrice();
        mPrice3 = good.getPrice3();
        mPrice4 = good.getPrice4();
        mPrice5 = good.getPrice5();
        mDiscount = good.getDiscount();

        mAlarmDay = good.getAlarmDay();
        mPath = good.getPath();
        mFree = good.getFree();

        mColors = DiabloUtils.instance().stringColorToArray(good.getColor());
//        mColors = new ArrayList<>();
//        for (String colorId: good.getColor().split(DiabloEnum.SIZE_SEPARATOR)) {
//            DiabloColor color = Profile.instance().getColor(DiabloUtils.instance().toInteger(colorId));
//            if (null != color) {
//                mColors.add(color);
//            }
//        }

        mSizeGroups = DiabloUtils.instance().stringSizeGroupToArray(good.getsGroup());
//        mSizeGroups = new ArrayList<>();
//        for (String groupId: good.getsGroup().split(DiabloEnum.SIZE_SEPARATOR)) {
//            DiabloSizeGroup group = Profile.instance().getSizeGroup(DiabloUtils.instance().toInteger(groupId));
//            if (null != group) {
//                mSizeGroups.add(group);
//            }
//        }
    }

    public GoodCalc(GoodCalc calc) {
        mStyleNumber = calc.getStyleNumber();
        mBrand = new DiabloBrand(calc.getBrand());
        mGoodType = new DiabloType(calc.getGoodType());
        mFirm = new Firm(calc.getFirm());

        mSex = calc.getSex();
        mYear = calc.getYear();
        mSeason = calc.getSeason();

        mOrgPrice = calc.getOrgPrice();
        mPkgPrice = calc.getPkgPrice();
        mTagPrice = calc.getTagPrice();
        mPrice3 = calc.getPrice3();
        mPrice4 = calc.getPrice4();
        mPrice5 = calc.getPrice5();
        mDiscount = calc.getDiscount();

        mAlarmDay = calc.getAlarmDay();
        mPath = calc.getPath();
        mFree = calc.getFree();

        mColors = new ArrayList<>(calc.getColors());
        mSizeGroups = new ArrayList<>(calc.getSizeGroups());
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

    public void clearColor() {
        mColors.clear();
    }

    public void addColor(DiabloColor color) {
        if (null == getColor(color.getColorId())) {
            this.mColors.add(color);
        }
    }

    public void removeColor(DiabloColor color) {
        DiabloColor removed = getColor(color.getColorId());
        if (null != removed) {
            this.mColors.remove(removed);
        }
    }

    public void clearSizeGroups() {
        mSizeGroups.clear();
    }

    public List<DiabloSizeGroup> getSizeGroups() {
        return mSizeGroups;
    }

    public void addSizeGroup(DiabloSizeGroup sizeGroup) {
        if (null == getSizeGroup(sizeGroup.getGroupId())) {
            this.mSizeGroups.add(sizeGroup);
        }
    }

    public void removeSizeGroup(DiabloSizeGroup sizeGroup) {
        DiabloSizeGroup removed = getSizeGroup(sizeGroup.getGroupId());
        if (null != removed) {
            this.mSizeGroups.remove(removed);
        }
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

    public DiabloColor getColor(Integer colorId) {
        DiabloColor found = null;
        for (DiabloColor color: mColors) {
            if(color.getColorId().equals(colorId)) {
                found = color;
            }
        }

        return found;
    }

    public String getStringColors() {
        List<String> colorNames = new ArrayList<>();
        for(DiabloColor color: mColors) {
            if (null != color.getName()) {
                colorNames.add(color.getName());
            }
        }
        return android.text.TextUtils.join(DiabloEnum.SIZE_SEPARATOR, colorNames);
    }

    public String getStringColorIds() {
        List<Integer> ids = new ArrayList<>();
        for(DiabloColor color: mColors) {
            if (!DiabloEnum.DIABLO_FREE_COLOR.equals(color.getColorId())) {
                ids.add(color.getColorId());
            }
        }
        return android.text.TextUtils.join(DiabloEnum.SIZE_SEPARATOR, ids);
    }

    public DiabloSizeGroup getSizeGroup(Integer groupId) {
        DiabloSizeGroup found = null;
        for (DiabloSizeGroup g: mSizeGroups) {
            if (g.getGroupId().equals(groupId)) {
                found = g;
            }
        }

        return found;
    }

    public String getStringSizeGroups() {
        String ids = getStringSizeGroupIds();
        return android.text.TextUtils.join(DiabloEnum.SIZE_SEPARATOR,
            Profile.instance().genSortedSizeNamesByGroups(ids));
    }

    public String getStringSizeGroupIds() {
        List<Integer> ids = new ArrayList<>();
        for (DiabloSizeGroup g: mSizeGroups) {
            if (0 != g.getGroupId()) {
                ids.add(g.getGroupId());
            }
        }

        return android.text.TextUtils.join(DiabloEnum.SIZE_SEPARATOR, ids);
    }
}
