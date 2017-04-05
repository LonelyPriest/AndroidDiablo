package com.diablo.dt.diablo.model.stock;

import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.MatchGood;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/4.
 */

public class EntryStock {
    private Integer goodId;
    private Integer orderId;

    private String styleNumber;
    private String brand;
    private String type;

    private Integer brandId;
    private Integer firmId;
    private Integer typeId;
    private Integer sex;
    private Integer year;
    private Integer season;
    private Integer free;

    private Float orgPrice;
    private Float tagPrice;
    private Float pkgPrice;
    private Float price3;
    private Float price4;
    private Float price5;
    private Float discount;

    private String sGroup;
    private String path;
    private Integer alarmDay;

    private Integer total;

    private Integer state;

    private List<DiabloColor> colors;
    private List<String> orderSizes;
    private List<EntryStockAmount> amounts;

    public EntryStock() {
        orderId = 0;
        total = 0;
        amounts = new ArrayList<>();
    }

    public EntryStock(MatchGood good) {
        init(good);
        amounts = new ArrayList<>();
        orderId = 0;
        total = 0;
    }

    public void init(MatchGood good) {
        goodId      = good.getId();
        styleNumber = good.getStyleNumber();
        brand       = good.getBrand();
        type        = good.getType();

        brandId = good.getBrandId();
        firmId  = good.getFirmId();
        typeId  = good.getFirmId();

        sex    = good.getSex();
        year   = good.getYear();
        season = good.getSeason();
        free   = good.getFree();

        orgPrice = good.getOrgPrice();
        tagPrice = good.getTagPrice();
        pkgPrice = good.getPkgPrice();
        price3   = good.getPrice3();
        price4   = good.getPrice4();
        price5   = good.getPrice5();
        discount = good.getDiscount();

        sGroup   = good.getsGroup();
        path     = good.getPath();
        alarmDay = good.getAlarmDay();

        colors = new ArrayList<>();
        for (String colorId: good.getColor().split(DiabloEnum.SIZE_SEPARATOR)) {
            colors.add(Profile.instance().getColor(DiabloUtils.instance().toInteger(colorId)));
        }
        orderSizes = Profile.instance().genSortedSizeNamesByGroups(sGroup);

        this.state = StockUtils.STARTING_STOCK;
    }

    public Integer getGoodId() {
        return goodId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getStyleNumber() {
        return styleNumber;
    }

    public void setStyleNumber(String styleNumber) {
        this.styleNumber = styleNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getFirmId() {
        return firmId;
    }

    public void setFirmId(Integer firmId) {
        this.firmId = firmId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
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

    public String getsGroup() {
        return sGroup;
    }

    public void setsGroup(String sGroup) {
        this.sGroup = sGroup;
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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public List<EntryStockAmount> getAmounts() {
        return amounts;
    }

    public void setAmounts(List<EntryStockAmount> amounts) {
        this.amounts = amounts;
    }

    public void clearAmounts() {
        this.amounts.clear();
    }

    public void addAmount(EntryStockAmount amount) {
        if (null != this.amounts) {
            this.amounts.add(amount);
        }
    }

    public EntryStockAmount getAmount(Integer colorId, String size) {
        EntryStockAmount amount = null;
        for (EntryStockAmount a: amounts) {
            if (a.getColorId().equals(colorId) && a.getSize().equals(size)) {
                amount = a;
                break;
            }
        }

        return amount;
    }

    public List<DiabloColor> getColors() {
        return colors;
    }

    public List<String> getOrderSizes() {
        return orderSizes;
    }

    public Float calcStockPrice() {
        return orgPrice * total;
    }

    public boolean isSame(EntryStock stock) {
        return stock.getStyleNumber().equals(styleNumber) && stock.getBrandId().equals(brandId);
    }

    public String getName() {
        return this.styleNumber + "/" + this.brand + "/" + this.type;
    }
}
