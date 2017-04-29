package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buxianhui on 17/3/14.
 */

/**
 * stock use at sale, when choose a good styleNumber, will query the stocks information from server
 */
public class Stock extends DiabloEntity{
    @SerializedName("style_number")
    private String styleNumber;
    @SerializedName("brand_id")
    private Integer brandId;
    @SerializedName("type_id")
    private Integer typeId;
    @SerializedName("sex")
    private Integer sex;
    @SerializedName("season")
    private Integer season;
    // total, color and size not include
    @SerializedName("total")
    private Integer totalExist;

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

    @SerializedName("color_id")
    private Integer colorId;
    @SerializedName("size")
    private String size;
    @SerializedName("amount")
    private Integer exist;

    @Override
    public String getName() {
        return styleNumber;
    }

    @Override
    public String getViewName() {
        return styleNumber;
    }

    public String getStyleNumber() {
        return styleNumber;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public Integer getSex() {
        return sex;
    }

    public Integer getSeason() {
        return season;
    }

    public Integer getTotalExist() {
        return totalExist;
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

    public Integer getColorId() {
        return colorId;
    }

    public String getSize() {
        return size;
    }

    public Integer getExist() {
        return exist;
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
}
