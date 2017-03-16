package com.diablo.dt.diablo.entity;

import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/3/14.
 */

public class SaleStock {
    private Integer orderId;
    private String  styleNumber;
    private String  brand;
    private String  type;

    private Integer brandId;
    private Integer typeId;
    private Integer firmId;

    private Integer sex;
    private Integer season;
    private Integer year;

    private Integer stockExist;
    private Integer selectedPrice;

    private Float tagPrice;
    private Float pkgPrice;
    private Float price3;
    private Float price4;
    private Float price5;
    private Float discount;

    private Integer free;
    private String  sizeGroup;
    private String  path;


    private Float   finalPrice;
    private Integer second;
    private Integer saleTotal;
    private Integer state;
    private String  comment;


    private List<SaleStockAmount> amounts;

    public void addAmount(SaleStockAmount amount){
        amounts.add(amount);
    }

    public void setAmounts(List<SaleStockAmount> amounts) {
        amounts.clear();
        for (SaleStockAmount a: amounts){
            this.amounts.add(a);
        }
    }

    public List<SaleStockAmount> getAmounts(){
        return amounts;
    }

    public SaleStock(MatchStock stock, Integer selectedPrice) {
        this.orderId = 0;
        this.styleNumber = stock.getStyleNumber();
        this.brand = stock.getBrand();
        this.type = stock.getType();

        this.brandId = stock.getBrandId();
        this.typeId = stock.getTypeId();
        this.firmId = stock.getFirmId();

        this.sex = stock.getSex();
        this.season = stock.getSeason();
        this.year = stock.getYear();

        this.tagPrice = stock.getTagPrice();
        this.pkgPrice = stock.getPkgPrice();
        this.price3 = stock.getPrice3();
        this.price4 = stock.getPrice4();
        this.price5 = stock.getPrice5();
        this.discount = stock.getDiscount();

        this.free = stock.getFree();
        this.sizeGroup = stock.getSizeGroup();
        this.path = stock.getPath();

        this.second     = DiabloEnum.DIABLO_FALSE;
        this.stockExist = 0;
        this.selectedPrice = selectedPrice;
        this.state = DiabloEnum.STARTING_SALE;

        amounts = new ArrayList<>();
    }

    public String getStyleNumber() {
        return styleNumber;
    }

    public String getBrand() {
        return brand;
    }

    public String getType() {
        return type;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public Integer getSelectedPrice() {
        return selectedPrice;
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

    public Integer getFirmId() {
        return firmId;
    }

    public Integer getSex() {
        return sex;
    }

    public Integer getSeason() {
        return season;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getFree() {
        return free;
    }

    public String getSizeGroup() {
        return sizeGroup;
    }

    public String getPath() {
        return path;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getStockExist() {
        return stockExist;
    }

    public void setStockExist(Integer stockExist) {
        this.stockExist = stockExist;
    }

    public Integer getSaleTotal() {
        return saleTotal;
    }

    public void setSaleTotal(Integer saleTotal) {
        this.saleTotal = saleTotal;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setSelectedPrice(Integer selectedPrice) {
        this.selectedPrice = selectedPrice;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }


    public void  setFinalPrice(Float price){
        this.finalPrice = price;
    }

    public Float getFinalPrice(){
        return this.finalPrice;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public Float getValidPrice(){
        Float price;
        switch (selectedPrice){
            case 1:
                price = getTagPrice();
                break;
            case 2:
                price = getPkgPrice();
                break;
            case 3:
                price = getPrice3();
                break;
            case 4:
                price = getPrice4();
                break;
            case 5:
                price = getPrice5();
                break;
            default:
                price = getTagPrice();
                break;
        }

        setFinalPrice(price);
        return getFinalPrice();
    }


    public Float getSalePrice(){
        return DiabloUtils.getInstance().priceWithDiscount(finalPrice, discount) * saleTotal;
    }
}