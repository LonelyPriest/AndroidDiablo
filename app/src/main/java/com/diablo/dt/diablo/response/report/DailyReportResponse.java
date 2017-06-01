package com.diablo.dt.diablo.response.report;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.Response;

import java.util.List;

/**
 * Created by buxianhui on 17/5/31.
 */

public class DailyReportResponse extends Response {
    @SerializedName("sale")
    private List<StockSale> stocksSale;
    @SerializedName("profit")
    private List<StockProfit> stockskProfit;

    @SerializedName("pin")
    private List<StockStore>  stocksIn;
    @SerializedName("pout")
    private List<StockStore>  stocksOut;

    @SerializedName("rstock")
    private List<StockStore>   stocksReal;
    @SerializedName("lstock")
    private List<StockStore>   stocksLast;

    public List<StockSale> getStocksSale() {
        return stocksSale;
    }

    public StockSale getStockSale(Integer shop) {
        StockSale f = null;
        for (StockSale s: stocksSale) {
            if (s.shop.equals(shop)) {
                f = s;
                break;
            }
        }
        return f;
    }

    public List<StockProfit> getStocksProfit() {
        return stockskProfit;
    }

    public StockProfit getStockProfit(Integer shop) {
        StockProfit f = null;
        for (StockProfit p: stockskProfit) {
            if (p.shop.equals(shop)) {
                f = p;
                break;
            }
        }
        return f;
    }

    public List<StockStore> getStocksIn() {
        return stocksIn;
    }

    public StockStore getStockIn(Integer shop) {
        StockStore f = null;
        for (StockStore ss: stocksIn) {
            if (ss.shop.equals(shop)) {
                f = ss;
                break;
            }
        }
        return f;
    }

    public List<StockStore> getStocksOut() {
        return stocksOut;
    }

    public StockStore getStockOut(Integer shop) {
        StockStore f = null;
        for (StockStore ss: stocksOut) {
            if (ss.shop.equals(shop)) {
                f = ss;
                break;
            }
        }
        return f;
    }

    public List<StockStore> getStocksReal() {
        return stocksReal;
    }

    public StockStore getStockReal(Integer shop) {
        StockStore f = null;
        for (StockStore ss: stocksReal) {
            if (ss.shop.equals(shop)) {
                f = ss;
                break;
            }
        }
        return f;
    }

    public List<StockStore> getStocksLast() {
        return stocksLast;
    }

    public StockStore getStockLast(Integer shop) {
        StockStore f = null;
        for (StockStore ss: stocksLast) {
            if (ss.shop.equals(shop)) {
                f = ss;
                break;
            }
        }
        return f;
    }

    /**
     * w_sale
     */
    public static class StockSale {
        @SerializedName("total")
        private Integer total;
        @SerializedName("spay")
        private Float shouldPay;
        @SerializedName("hpay")
        private Float hasPay;

        @SerializedName("cash")
        private Float cash;
        @SerializedName("card")
        private Float card;
        @SerializedName("wire")
        private Float wire;
        @SerializedName("veri")
        private Float verificate;
        @SerializedName("shop_id")
        private Integer shop;

        public Integer getTotal() {
            return total;
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

        public Integer getShop() {
            return shop;
        }
    }

    /**
     * w_sale_detail
     */
    public static class StockProfit {
        @SerializedName("total")
        private Integer total;
        @SerializedName("cost")
        private Float cost;
        @SerializedName("balance")
        private Float balance;
        @SerializedName("shop_id")
        private Integer shop;

        public Integer getTotal() {
            return total;
        }

        public Float getCost() {
            return cost;
        }

        public Float getBalance() {
            return balance;
        }

        public Integer getShop() {
            return shop;
        }
    }

    /**
     * stock in, stock out
     */
    public static class StockStore {
        @SerializedName("total")
        private Integer total;
        @SerializedName("cost")
        private Float cost;
        @SerializedName("shop_id")
        private Integer shop;

        public Integer getTotal() {
            return total;
        }

        public Float getCost() {
            return cost;
        }

        public Integer getShop() {
            return shop;
        }
    }

//    /**
//     * stock of current
//     */
//    private static class StockReal {
//        @SerializedName("total")
//        private Integer total;
//        @SerializedName("cost")
//        private Float cost;
//        @SerializedName("shop_id")
//        private Integer shop;
//    }
}
