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

    public List<DiabloEntryStock> getEntryStocks() {
        return entryStocks;
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

        // use to reject
        @SerializedName("fdiscount")
        private Float fdiscount;
        @SerializedName("fprice")
        private Float fprice;
        @SerializedName("amounts")
        private List<DiabloEntryStockAmount> entryStockRejectAmounts;

        // use to update
        @SerializedName("operation")
        private String operation;
        @SerializedName("changed_amount")
        private List<DiabloEntryStockAmount> changedAmounts;

        public String getStyleNumber() {
            return styleNumber;
        }

        public Integer getBrandId() {
            return brandId;
        }

        public Integer getFirmId() {
            return firmId;
        }

        public Integer getTypeId() {
            return typeId;
        }

        public Integer getSex() {
            return sex;
        }

        public Integer getYear() {
            return year;
        }

        public Integer getSeason() {
            return season;
        }

        public String getsGroup() {
            return sGroup;
        }

        public Integer getFree() {
            return free;
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

        public Integer getAlarmDay() {
            return alarmDay;
        }

        public Integer getTotal() {
            return total;
        }

        public String getPath() {
            return path;
        }

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

        /**
         * use to reject only
         */
        public void setFdiscount(Float fdiscount) {
            this.fdiscount = fdiscount;
        }

        public void setFprice(Float fprice) {
            this.fprice = fprice;
        }

        public void setEntryStockRejectAmounts(List<DiabloEntryStockAmount> entryStockRejectAmounts) {
            this.entryStockRejectAmounts = entryStockRejectAmounts;
        }

        /**
         * use to update only
         */
        public void setOperation(String operation) {
            this.operation = operation;
        }

        public void setChangedAmounts(List<DiabloEntryStockAmount> changedAmounts) {
            this.changedAmounts = changedAmounts;
        }
    }

    public static class DiabloEntryStockAmount {
        @SerializedName("cid")
        private Integer colorId;
        @SerializedName("size")
        private String size;
        @SerializedName("count")
        private Integer count;

        // use to reject only
        @SerializedName("reject_count")
        private Integer rejectCount;

        // use to update only
        @SerializedName("operation")
        private String operation;

        public DiabloEntryStockAmount() {

        }

        public void setColorId(Integer colorId) {
            this.colorId = colorId;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public void setRejectCount(Integer rejectCount) {
            this.rejectCount = rejectCount;
        }

        public void setOperation(String operation) {
            this.operation = operation;
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

        // use to update only
        @SerializedName("id")
        private Integer RsnId;
        @SerializedName("rsn")
        private String rsn;
        @SerializedName("old_firm")
        private Integer oldFirm;
        @SerializedName("old_balance")
        private Float oldBalance;
        @SerializedName("old_verify_pay")
        private Float oldVerificate;
        @SerializedName("old_should_pay")
        private Float oldShouldPay;
        @SerializedName("old_has_pay")
        private Float oldHasPay;
        @SerializedName("old_datetime")
        private String oldDatetime;

        public DiabloStockCalc() {

        }

        public void setFirmId(Integer firmId) {
            this.firmId = firmId;
        }

        public Integer getFirmId() {
            return firmId;
        }

        public void setShopId(Integer shopId) {
            this.shopId = shopId;
        }

        public Integer getShopId() {
            return shopId;
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

        public String getEmployeeId() {
            return employeeId;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getComment() {
            return comment;
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

        public Float getCash() {
            return cash;
        }

        public void setCard(Float card) {
            this.card = card;
        }

        public Float getCard() {
            return card;
        }

        public void setWire(Float wire) {
            this.wire = wire;
        }

        public Float getWire() {
            return wire;
        }

        public void setVerificate(Float verificate) {
            this.verificate = verificate;
        }

        public Float getVerificate() {
            return verificate;
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

        public void setRsnId(Integer RsnId) {
            this.RsnId = RsnId;
        }

        public void setRsn(String rsn) {
            this.rsn = rsn;
        }

        public void setOldFirm(Integer oldFirm) {
            this.oldFirm = oldFirm;
        }

        public void setOldBalance(Float oldBalance) {
            this.oldBalance = oldBalance;
        }

        public void setOldVerificate(Float oldVerificate) {
            this.oldVerificate = oldVerificate;
        }

        public void setOldShouldPay(Float oldShouldPay) {
            this.oldShouldPay = oldShouldPay;
        }

        public void setOldHasPay(Float oldHasPay) {
            this.oldHasPay = oldHasPay;
        }

        public void setOldDatetime(String oldDatetime) {
            this.oldDatetime = oldDatetime;
        }
    }
}
