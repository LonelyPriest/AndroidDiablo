package com.diablo.dt.diablo.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/5.
 */

public class NewStockRequest {
    @SerializedName("inventory")
    private List<DiabloEntryStock> entryStocks;
    @SerializedName("base")
    private DiabloStockCalc stockCalc;

    public NewStockRequest() {
        entryStocks = new ArrayList<>();
    }

    public void addEntryStock(DiabloEntryStock stock) {
        if (null != entryStocks) {
            entryStocks.add(stock);
        }
    }

    public void setStockCalc(DiabloStockCalc stockCalc) {
        this.stockCalc = stockCalc;
    }

    public static class DiabloEntryStock {
        @SerializedName("good")
        private Integer goodId;
        @SerializedName("style_number")
        private String styleNumber;
        @SerializedName("brand")
        private Integer brandId;
        @SerializedName("firm")
        private Integer firmId;
        @SerializedName("type")
        private Integer typeId;
        @SerializedName("sex")
        private Integer sex;
        @SerializedName("year")
        private Integer year;
        @SerializedName("season")
        private Integer season;
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
        @SerializedName("p3")
        private Float price3;
        @SerializedName("p4")
        private Float price4;
        @SerializedName("p5")
        private Float price5;
        @SerializedName("discount")
        private Float discount;

        @SerializedName("alarm_day")
        private Integer alarmDay;
        @SerializedName("total")
        private Integer total;

        @SerializedName("path")
        private String path;

        @SerializedName("amount")
        private List<DiabloEntryStockAmount> entryStockAmounts;

        public void setGoodId(Integer goodId) {
            this.goodId = goodId;
        }

        public void setStyleNumber(String styleNumber) {
            this.styleNumber = styleNumber;
        }

        public void setBrandId(Integer brandId) {
            this.brandId = brandId;
        }

        public void setFirmId(Integer firmId) {
            this.firmId = firmId;
        }

        public void setTypeId(Integer typeId) {
            this.typeId = typeId;
        }

        public void setSex(Integer sex) {
            this.sex = sex;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public void setSeason(Integer season) {
            this.season = season;
        }

        public void setsGroup(String sGroup) {
            this.sGroup = sGroup;
        }

        public void setFree(Integer free) {
            this.free = free;
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

        public void setAlarmDay(Integer alarmDay) {
            this.alarmDay = alarmDay;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public void setEntryStockAmounts(List<DiabloEntryStockAmount> entryStockAmounts) {
            this.entryStockAmounts = entryStockAmounts;
        }
    }

    public static class DiabloEntryStockAmount {
        @SerializedName("cid")
        private Integer colorId;
        @SerializedName("size")
        private String size;
        @SerializedName("count")
        private Integer count;

        public void setColorId(Integer colorId) {
            this.colorId = colorId;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }

    public static class DiabloStockCalc {
        @SerializedName("firm")
        private Integer firmId;
        @SerializedName("shop")
        private Integer shopId;
        @SerializedName("date")
        private String date;

        @SerializedName("datetime")
        private String datetime;
        @SerializedName("employee")
        private String employeeId;
        @SerializedName("comment")
        private String comment;

        @SerializedName("total")
        private Integer total;
        @SerializedName("balance")
        private Float balance;
        @SerializedName("cash")
        private Float cash;
        @SerializedName("card")
        private Float card;
        @SerializedName("wire")
        private Float wire;
        @SerializedName("verificate")
        private Float verificate;
        @SerializedName("should_pay")
        private Float shouldPay;
        @SerializedName("has_pay")
        private Float hasPay;
        @SerializedName("e_pay_type")
        private Integer extraCostType;
        @SerializedName("e_pay")
        private Float extraCost;

        public void setFirmId(Integer firmId) {
            this.firmId = firmId;
        }

        public void setShopId(Integer shopId) {
            this.shopId = shopId;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public void setBalance(Float balance) {
            this.balance = balance;
        }

        public void setCash(Float cash) {
            this.cash = cash;
        }

        public void setCard(Float card) {
            this.card = card;
        }

        public void setWire(Float wire) {
            this.wire = wire;
        }

        public void setVerificate(Float verificate) {
            this.verificate = verificate;
        }

        public void setShouldPay(Float shouldPay) {
            this.shouldPay = shouldPay;
        }

        public void setHasPay(Float hasPay) {
            this.hasPay = hasPay;
        }

        public void setExtraCostType(Integer extraCostType) {
            this.extraCostType = extraCostType;
        }

        public void setExtraCost(Float extraCost) {
            this.extraCost = extraCost;
        }
    }
}
