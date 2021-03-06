package com.diablo.dt.diablo.model.sale;

import com.google.gson.Gson;

import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.MatchStock;
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
    private Integer alarmDy;

    private Integer stockExist;

    private Float orgPrice;
    private Float tagPrice;
    private Float pkgPrice;
    private Float price3;
    private Float price4;
    private Float price5;
    private Float discount;
    private Float finalPrice;

    private String  path;
    private Integer second;

    private List<String> orderSizes;
    private String  sizeGroup;
    private List<DiabloColor> colors;
    private Integer free;
    private String  comment;

    private Integer saleTotal;

    private Integer selectedPrice;

    private Integer state;

    private String operation;

    private List<SaleStockAmount> amounts;

    synchronized public void addAmount(SaleStockAmount amount){
        amounts.add(new Gson().fromJson(new Gson().toJson(amount),  SaleStockAmount.class));
    }

    synchronized public void clearAmounts(){
        amounts.clear();
    }

    public void setAmounts(List<SaleStockAmount> amounts) {
        this.amounts.clear();
        for (SaleStockAmount a: amounts){
            this.amounts.add(a);
        }
    }

    public List<SaleStockAmount> getAmounts(){
        return amounts;
    }

    public SaleStockAmount getAmount(Integer color, String size) {
        SaleStockAmount amount = null;
        for (SaleStockAmount a: amounts) {
            if (a.getColorId().equals(color) && a.getSize().equals(size)) {
                amount = a;
                break;
            }
        }

        return amount;
    }

    public Integer calcExistStock(){
        Integer total = 0;
        for (SaleStockAmount a: amounts){
            total += a.getStock();
        }

        return total;
    }


    public SaleStock() {
        this.orderId = 0;
        amounts = new ArrayList<>();
    }

    public SaleStock(SaleStock stock) {
        this.orderId = stock.getOrderId();
        this.styleNumber = stock.getStyleNumber();
        this.brand = stock.getBrand();
        this.type = stock.getType();

        this.brandId = stock.getBrandId();
        this.typeId = stock.getTypeId();
        this.firmId = stock.getFirmId();

        this.sex = stock.getSex();
        this.season = stock.getSeason();
        this.year = stock.getYear();
        this.alarmDy = stock.getAlarmDy();

        this.stockExist = stock.getStockExist();

        this.orgPrice = stock.getOrgPrice();
        this.tagPrice = stock.getTagPrice();
        this.pkgPrice = stock.getPkgPrice();
        this.price3 = stock.getPrice3();
        this.price4 = stock.getPrice4();
        this.price5 = stock.getPrice5();
        this.discount = stock.getDiscount();
        this.finalPrice = stock.getFinalPrice();

        this.path = stock.getPath();
        this.second = stock.getSecond();

        this.orderSizes = null;
        this.sizeGroup  = stock.getSizeGroup();
        this.colors     = null;
        this.free       = stock.getFree();
        this.comment    = stock.getComment();

        this.saleTotal  = stock.getSaleTotal();
        this.selectedPrice = stock.getSelectedPrice();
        this.state  = stock.getState();

        this.amounts = new ArrayList<>();
        for (SaleStockAmount a: stock.getAmounts()) {
            SaleStockAmount amount = new SaleStockAmount(a.getColorId(), a.getSize());
            amount.setSellCount(a.getSellCount());
            amounts.add(amount);
        }
    }

    public SaleStock(MatchStock stock, Integer selectedPrice) {
        this.orderId = 0;
        init(stock, selectedPrice);
        amounts = new ArrayList<>();
    }

    public void init(MatchStock stock, Integer selectPrice) {
        this.styleNumber = stock.getStyleNumber();
        this.brand = stock.getBrand();
        this.type = stock.getType();

        this.brandId = stock.getBrandId();
        this.typeId = stock.getTypeId();
        this.firmId = stock.getFirmId();

        this.sex = stock.getSex();
        this.season = stock.getSeason();
        this.year = stock.getYear();
        this.alarmDy = stock.getAlarmDay();

        this.orgPrice = stock.getOrgPrice();
        this.tagPrice = stock.getTagPrice();
        this.pkgPrice = stock.getPkgPrice();
        this.price3 = stock.getPrice3();
        this.price4 = stock.getPrice4();
        this.price5 = stock.getPrice5();
        this.discount = stock.getDiscount();

        this.free = stock.getFree();
        this.sizeGroup = stock.getSizeGroup();
        this.path = stock.getPath();
        this.comment = stock.getComment();

        this.second     = DiabloEnum.DIABLO_FALSE;
        this.stockExist = 0;
        this.selectedPrice = selectPrice;
        this.state = DiabloEnum.STARTING_SALE;
    }

    public void setStyleNumber(String styleNumber){
        this.styleNumber = styleNumber;
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

//    public void setBrand(Integer brandId){
//        this.brandId = brandId;
//    }
//
//    public void setType(Integer typeId) {
//        this.typeId = typeId;
//    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setSelectedPrice(Integer selectedPrice) {
        this.selectedPrice = selectedPrice;
    }

    public Integer getSelectedPrice() {
        return selectedPrice;
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

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Integer getFirmId() {
        return firmId;
    }

//    public void setSex(Integer sex) {
//        this.sex = sex;
//    }

    public Integer getSex() {
        return sex;
    }

    public Integer getSeason() {
        return season;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getAlarmDy() {
        return alarmDy;
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

//    public void setSelectedPrice(Integer selectedPrice) {
//        this.selectedPrice = selectedPrice;
//    }

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

    public List<DiabloColor> getColors() {
        return colors;
    }

    public void setColors(List<DiabloColor> colors) {
        this.colors = colors;
    }

    public List<String> getOrderSizes() {
        return orderSizes;
    }

    public void setOrderSizes(List<String> sizes) {
        this.orderSizes = sizes;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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

        // setFinalPrice(price);
        // return getFinalPrice();
        return price;
    }


    public Float getSalePrice(){
        if ( null == saleTotal ) {
            return  0f;
        }
        return DiabloUtils.getInstance().priceWithDiscount(finalPrice, discount) * saleTotal;
    }

    public boolean isSame(SaleStock stock) {
        return stock.getStyleNumber().equals(styleNumber) && stock.getBrandId().equals(brandId);
    }

    public String getName() {
        return this.styleNumber + "/" + this.brand + "/" + this.type;
    }
}