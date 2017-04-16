package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/3/7.
 */

/**
 * used at match
 */
public class MatchStock extends DiabloEntity{
    @SerializedName("style_number")
    private String styleNumber;
    @SerializedName("brand_id")
    private Integer brandId;
    @SerializedName("brand")
    private String brand;
    @SerializedName("type_id")
    private Integer typeId;
    @SerializedName("type")
    private String type;
    @SerializedName("sex")
    private Integer sex;
    @SerializedName("season")
    private Integer season;
    @SerializedName("firm_id")
    private Integer firmId;
    @SerializedName("s_group")
    private String sGroup;
    @SerializedName("free")
    private Integer free;
    @SerializedName("year")
    private Integer year;
    @SerializedName("org_price")
    private Float orgPrice;
    @SerializedName("tag_price")
    private Float tagPrice;
    @SerializedName("pkg_price")
    private Float pkgPrice;
    @SerializedName("price3")
    private Float price3;
    @SerializedName("price4")
    private Float price4;
    @SerializedName("price5")
    private Float price5;
    @SerializedName("discount")
    private Float discount;
    @SerializedName("path")
    private String path;
    @SerializedName("alarm_day")
    private Integer alarmDay;

    public MatchStock() {

    }

    public void setStyleNumber(String styleNumber) {
        this.styleNumber = styleNumber;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public void setFirmId(Integer firmId) {
        this.firmId = firmId;
    }

    public String getsGroup() {
        return sGroup;
    }

    public void setsGroup(String sGroup) {
        this.sGroup = sGroup;
    }

    public void setFree(Integer free) {
        this.free = free;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setOrgPrice(Float orgPrice) {
        this.orgPrice = orgPrice;
    }

    public void setTagPrice(Float tagPrice) {
        this.tagPrice = tagPrice;
    }

    public void setPkgPrice(Float pkgPrice) {
        this.pkgPrice = pkgPrice;
    }

    public void setPrice3(Float price3) {
        this.price3 = price3;
    }

    public void setPrice4(Float price4) {
        this.price4 = price4;
    }

    public void setPrice5(Float price5) {
        this.price5 = price5;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAlarmDay(Integer alarmDay) {
        this.alarmDay = alarmDay;
    }

    public String getStyleNumber() {
        return styleNumber;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public String getBrand() {
        return brand;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public String getType() {
        return type;
    }

    public Integer getSex() {
        return sex;
    }

    public Integer getSeason() {
        return season;
    }

    public Integer getFirmId() {
        return firmId;
    }

    public String getSizeGroup() {
        return sGroup;
    }

    public Integer getFree() {
        return free;
    }

    public Integer getYear() {
        return year;
    }

    public Float getOrgPrice() {
        return orgPrice;
    }

    public Float getTagPrice() {
        return tagPrice;
    }

    public Float getPkgPrice() {
        return pkgPrice;
    }

    public Float getPrice3() {
        return price3;
    }

    public Float getPrice4() {
        return price4;
    }

    public Float getPrice5() {
        return price5;
    }

    public Float getDiscount() {
        return discount;
    }

    public String getPath() {
        return path;
    }

    public Integer getAlarmDay() {
        return alarmDay;
    }

    @Override
    public String getName() {
        return this.styleNumber + "/" + this.brand + "/" + this.type;
    }

    @Override
    public String getViewName() {
        return this.styleNumber;
    }
}
