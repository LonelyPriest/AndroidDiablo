package com.diablo.dt.diablo.response.report;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.Response;

import java.util.List;

/**
 * Created by buxianhui on 17/6/4.
 */

public class MonthReportResponse extends Response {
    @SerializedName("data")
    private List<MonthDetail> monthDetails;
    @SerializedName("stock")
    private List<StockOfDay> dayStocks;

    public List<MonthDetail> getMonthDetails() {
        return monthDetails;
    }

    public List<StockOfDay> getDayStocks() {
        return dayStocks;
    }

    public MonthDetail getDetail(Integer shop) {
        MonthDetail d = null;
        for (MonthDetail m: monthDetails) {
            if (m.shop.equals(shop)) {
                d = m;
                break;
            }
        }
        return d;
    }

    public StockOfDay getStock(Integer shop) {
        StockOfDay d = null;
        for (StockOfDay s: dayStocks) {
            if (s.shop.equals(shop)) {
                d = s;
                break;
            }
        }
        return d;
    }

    /**
     * statistic
     */
    public static class MonthDetail {
        @SerializedName("shop_id")
        private Integer shop;

        @SerializedName("sell")
        private Integer sell;
        @SerializedName("sell_cost")
        private Float sellCost;
        @SerializedName("should_pay")
        private Float shouldPay;
        @SerializedName("has_pay")
        private Float hasPay;

        @SerializedName("cash")
        private Float cash;
        @SerializedName("card")
        private Float card;
        @SerializedName("wire")
        private Float wire;
        @SerializedName("veri")
        private Float verificate;

        @SerializedName("stock_in")
        private Integer stockIn;
        @SerializedName("stock_out")
        private Integer stockOut;
        @SerializedName("stock_in_cost")
        private Float stockInCost;
        @SerializedName("stock_out_cost")
        private Float stockOutCost;

        @SerializedName("t_stock_in")
        private Integer tStockIn;
        @SerializedName("t_stock_out")
        private Integer tStockOut;
        @SerializedName("t_stock_in_cost")
        private Float tStockInCost;
        @SerializedName("t_stock_out_cost")
        private Float tStockOutCost;

        @SerializedName("stock_fix")
        private Integer stockFix;
        @SerializedName("stock_fix_cost")
        private Float stockFixCost;

        public MonthDetail() {
            shop = -1;

            sell = 0;
            sellCost = 0f;
            shouldPay = 0f;
            hasPay = 0f;

            cash = 0f;
            card = 0f;
            wire = 0f;
            verificate = 0f;

            stockIn = 0;
            stockOut = 0;
            stockInCost = 0f;
            stockOutCost = 0f;

            tStockIn = 0;
            tStockOut = 0;
            tStockInCost = 0f;
            tStockOutCost = 0f;

            stockFix = 0;
            stockFixCost = 0f;
        }

        public Integer getShop() {
            return shop;
        }

        public Integer getSell() {
            return sell;
        }

        public Float getSellCost() {
            return sellCost;
        }

        public Float getShouldPay() {
            return shouldPay;
        }

        public Float getHasPay() {
            return hasPay;
        }

        public Float getCash() {
            return cash;
        }

        public Float getCard() {
            return card;
        }

        public Float getWire() {
            return wire;
        }

        public Float getVerificate() {
            return verificate;
        }

        public Integer getStockIn() {
            return stockIn;
        }

        public Integer getStockOut() {
            return stockOut;
        }

        public Float getStockInCost() {
            return stockInCost;
        }

        public Float getStockOutCost() {
            return stockOutCost;
        }

        public Integer gettStockIn() {
            return tStockIn;
        }

        public Integer gettStockOut() {
            return tStockOut;
        }

        public Float gettStockInCost() {
            return tStockInCost;
        }

        public Float gettStockOutCost() {
            return tStockOutCost;
        }

        public Integer getStockFix() {
            return stockFix;
        }

        public Float getStockFixCost() {
            return stockFixCost;
        }
    }

    public static class StockOfDay {
        @SerializedName("shop_id")
        private Integer shop;
        @SerializedName("stockc")
        private Integer stockCalc;
        @SerializedName("stock_cost")
        private Float stockCost;

        public StockOfDay () {
            shop = -1;
            stockCalc = 0;
            stockCost = 0f;
        }

        public Integer getShop() {
            return shop;
        }

        public Integer getStockCalc() {
            return stockCalc;
        }

        public Float getStockCost() {
            return stockCost;
        }
    }
}
