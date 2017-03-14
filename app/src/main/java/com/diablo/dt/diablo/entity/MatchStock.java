package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/3/7.
 */

public class MatchStock {
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

    public String getName() {
        return this.styleNumber + "/" + this.brand + "/" + this.type;
    }
}
