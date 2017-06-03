package com.diablo.dt.diablo.response.report;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.Response;
import com.diablo.dt.diablo.utils.DiabloEnum;

import java.util.List;

/**
 * Created by buxianhui on 17/6/3.
 */

public class DailyReportResponse extends Response {
    /**
     * statistic
     */
    @SerializedName("total")
    private Integer total;

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


    @SerializedName("data")
    private List<DailyDetail> mDailyDetails;

    public DailyReportResponse() {
        super();
        total = DiabloEnum.INVALID_INDEX;
    }

    public List<DailyDetail> getDailyDetails() {
        return mDailyDetails;
    }

    public Integer getTotal() {
        return total;
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

    /**
     * per daily report
     */
    public static class DailyDetail {
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

        @SerializedName("stock")
        private Integer stock;
        @SerializedName("stockc")
        private Integer stockCalc;
        @SerializedName("stock_cost")
        private Float stockCost;

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

        @SerializedName("day")
        private String day;
        @SerializedName("entry_date")
        private String genDate;

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

        public Integer getStock() {
            return stock;
        }

        public Integer getStockCalc() {
            return stockCalc;
        }

        public Float getStockCost() {
            return stockCost;
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

        public Integer getTStockIn() {
            return tStockIn;
        }

        public Integer getTStockOut() {
            return tStockOut;
        }

        public Float getTStockInCost() {
            return tStockInCost;
        }

        public Float getTStockOutCost() {
            return tStockOutCost;
        }

        public Integer getStockFix() {
            return stockFix;
        }

        public Float getStockFixCost() {
            return stockFixCost;
        }

        public String getDay() {
            return day;
        }

        public String getGenDate() {
            return genDate;
        }
    }
}
