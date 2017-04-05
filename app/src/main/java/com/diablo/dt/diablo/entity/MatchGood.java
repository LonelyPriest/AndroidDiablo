package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/4/3.
 */

public class MatchGood {
    @SerializedName("id")
    private Integer id;
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
    @SerializedName("firm_id")
    private Integer firmId;
    @SerializedName("sex")
    private Integer sex;
    @SerializedName("color")
    private String color;
    @SerializedName("year")
    private Integer year;
    @SerializedName("season")
    private Integer season;
    @SerializedName("size")
    private String size;
    @SerializedName("s_group")
    private String sGroup;
    @SerializedName("free")
    private Integer free;

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
    @SerializedName("entry_date")
    private String datetime;

    public MatchGood() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStyleNumber() {
        return styleNumber;
    }

    public void setStyleNumber(String styleNumber) {
        this.styleNumber = styleNumber;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFirmId() {
        return firmId;
    }

    public void setFirmId(Integer firmId) {
        this.firmId = firmId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getsGroup() {
        return sGroup;
    }

    public void setsGroup(String sGroup) {
        this.sGroup = sGroup;
    }

    public Integer getFree() {
        return free;
    }

    public void setFree(Integer free) {
        this.free = free;
    }

    public Float getOrgPrice() {
        return orgPrice;
    }

    public void setOrgPrice(Float orgPrice) {
        this.orgPrice = orgPrice;
    }

    public Float getTagPrice() {
        return tagPrice;
    }

    public void setTagPrice(Float tagPrice) {
        this.tagPrice = tagPrice;
    }

    public Float getPkgPrice() {
        return pkgPrice;
    }

    public void setPkgPrice(Float pkgPrice) {
        this.pkgPrice = pkgPrice;
    }

    public Float getPrice3() {
        return price3;
    }

    public void setPrice3(Float price3) {
        this.price3 = price3;
    }

    public Float getPrice4() {
        return price4;
    }

    public void setPrice4(Float price4) {
        this.price4 = price4;
    }

    public Float getPrice5() {
        return price5;
    }

    public void setPrice5(Float price5) {
        this.price5 = price5;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getAlarmDay() {
        return alarmDay;
    }

    public void setAlarmDay(Integer alarmDay) {
        this.alarmDay = alarmDay;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getName() {
        return this.styleNumber + "/" + this.brand + "/" + this.type;
    }
}
