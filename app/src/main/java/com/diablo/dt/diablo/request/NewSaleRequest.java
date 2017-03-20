package com.diablo.dt.diablo.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/3/20.
 */

public class NewSaleRequest {

    @SerializedName("inventory")
    private List<DiabloSaleStock> stocks;
    @SerializedName("base")
    private DiabloSaleCalc saleCalc;
    @SerializedName("print")
    private DiabloPrintAttr printAttr;

    public NewSaleRequest() {
        stocks = new ArrayList<>();
    }

    public void addStock(DiabloSaleStock stock) {
        stocks.add(stock);
    }

    public void setSaleCalc(DiabloSaleCalc calc) {
        this.saleCalc = calc;
    }

    public void setPrintAttr(DiabloPrintAttr attr) {
        this.printAttr = attr;
    }

    public static class DiabloSaleStock {
        @SerializedName("order_id")
        private Integer orderId;
        @SerializedName("style_number")
        private String  styleNumber;
        @SerializedName("brand_name")
        private String  brand;
        @SerializedName("brand")
        private Integer brandId;
        @SerializedName("type")
        private Integer typeId;
        @SerializedName("type_name")
        private String  type;



        @SerializedName("firm")
        private Integer firmId;
        @SerializedName("sex")
        private Integer sex;
        @SerializedName("season")
        private Integer season;
        @SerializedName("year")
        private Integer year;

        @SerializedName("sell_total")
        private Integer saleTotal;
        @SerializedName("fdiscount")
        private Float fdiscount;
        @SerializedName("fprice")
        private Float   fprice;

        @SerializedName("path")
        private String  path;
        @SerializedName("second")
        private Integer second;

        @SerializedName("sizes")
        private List<String> orderSizes;
        @SerializedName("s_group")
        private String  sizeGroup;
        @SerializedName("colors")
        private List<DiabloSaleColor> colors;
        @SerializedName("free")
        private Integer free;
        @SerializedName("comment")
        private String  comment;

        @SerializedName("sell_style")
        private Integer sellTye;
        @SerializedName("amounts")
        private List<DiabloSaleStockAmount> amounts;

        public DiabloSaleStock() {
            // orderSizes = new ArrayList<>();
            // colors = new ArrayList<>();
            amounts = new ArrayList<>();
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public void setStyleNumber(String styleNumber) {
            this.styleNumber = styleNumber;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setBrandId(Integer brandId) {
            this.brandId = brandId;
        }

        public void setTypeId(Integer typeId) {
            this.typeId = typeId;
        }

        public void setFirmId(Integer firmId) {
            this.firmId = firmId;
        }

        public void setSex(Integer sex) {
            this.sex = sex;
        }

        public void setSeason(Integer season) {
            this.season = season;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public void setFdiscount(Float fdiscount) {
            this.fdiscount = fdiscount;
        }

        public void setFprice(Float fprice) {
            this.fprice = fprice;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public void setSecond(Integer second) {
            this.second = second;
        }

        public void setOrderSizes(List<String> orderSizes) {
            this.orderSizes = orderSizes;
        }

        public void setSizeGroup(String sizeGroup) {
            this.sizeGroup = sizeGroup;
        }

        public void setColors(List<DiabloSaleColor> colors) {
            this.colors = colors;
        }

        public void setFree(Integer free) {
            this.free = free;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public void setSaleTotal(Integer saleTotal) {
            this.saleTotal = saleTotal;
        }

        public void setSellTye(Integer sellTye) {
            this.sellTye = sellTye;
        }

        public void addAmount(DiabloSaleStockAmount amount) {
            this.amounts.add(amount);
        }
    }

    public static class DiabloSaleStockAmount {
        @SerializedName("cid")
        private Integer colorId;
        @SerializedName("size")
        private String  size;
        @SerializedName("sell_count")
        private Integer sellCount;

        public DiabloSaleStockAmount() {

        }

        public void setColorId(Integer colorId) {
            this.colorId = colorId;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public void setSellCount(Integer sellCount) {
            this.sellCount = sellCount;
        }
    }

    public static class DiabloSaleColor {
        @SerializedName("cid")
        private Integer colorId;
        @SerializedName("cname")
        private String colorName;

        public DiabloSaleColor() {

        }

        public void setColorId(Integer colorId) {
            this.colorId = colorId;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }
    }

    public static class DiabloSaleCalc {
        @SerializedName("retailer")
        private Integer retailer;
        @SerializedName("shop")
        private Integer shop;

        @SerializedName("datetime")
        private String datetime;
        @SerializedName("employee")
        private String employee;
        @SerializedName("comment")
        private String comment;

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
        @SerializedName("sys_customer")
        private boolean isSysRetailer;
        @SerializedName("accBalance")
        private Float accBalance;

        @SerializedName("e_pay_type")
        private Integer extraCostType;
        @SerializedName("e_pay")
        private Float extraCost;

        @SerializedName("total")
        private Integer total;
        @SerializedName("sell_total")
        private Integer sellTotal;
        @SerializedName("reject_total")
        private Integer rejectTotal;

        public void setRetailer(Integer retailer) {
            this.retailer = retailer;
        }

        public void setShop(Integer shop) {
            this.shop = shop;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public void setEmployee(String employee) {
            this.employee = employee;
        }

        public void setComment(String comment) {
            this.comment = comment;
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

        public void setSysRetailer(boolean sysRetailer) {
            isSysRetailer = sysRetailer;
        }

        public void setAccBalance(Float accBalance) {
            this.accBalance = accBalance;
        }

        public void setExtraCostType(Integer extraCostType) {
            this.extraCostType = extraCostType;
        }

        public void setExtraCost(Float extraCost) {
            this.extraCost = extraCost;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public void setSellTotal(Integer sellTotal) {
            this.sellTotal = sellTotal;
        }

        public void setRejectTotal(Integer rejectTotal) {
            this.rejectTotal = rejectTotal;
        }
    }

    public static class DiabloPrintAttr {
        @SerializedName("im_printer")
        private Integer immediatelyPrint;
        @SerializedName("retailer_id")
        private Integer retailerId;
        @SerializedName("retailer")
        private String retailerName;
        @SerializedName("shop")
        private String  shop;
        @SerializedName("employ")
        private String employee;

        public DiabloPrintAttr() {

        }

        public void setImmediatelyPrint(Integer immediatelyPrint) {
            this.immediatelyPrint = immediatelyPrint;
        }

        public void setRetailerId(Integer retailerId) {
            this.retailerId = retailerId;
        }

        public void setRetailerName(String retailerName) {
            this.retailerName = retailerName;
        }

        public void setShop(String shop) {
            this.shop = shop;
        }

        public void setEmployee(String employee) {
            this.employee = employee;
        }
    }
}
