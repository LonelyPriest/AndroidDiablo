package com.diablo.dt.diablo.response.report;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.Response;

import java.util.List;

/**
 * Created by buxianhui on 17/6/2.
 */

public class DailyReportSaleDetailResponse extends Response {
    @SerializedName("detail")
    List<ReportSaleDetail> saleDetails;

    public List<ReportSaleDetail> getSaleDetails() {
        return saleDetails;
    }

    public static class ReportSaleDetail {
        @SerializedName("style_number")
        private String styleNumber;
        @SerializedName("type_id")
        private Integer type;
        @SerializedName("brand_id")
        private Integer brand;
        @SerializedName("firm_id")
        private Integer firm;
        @SerializedName("total")
        private Integer total;
        @SerializedName("shop_id")
        private Integer shop;

        public String getStyleNumber() {
            return styleNumber;
        }

        public Integer getType() {
            return type;
        }

        public Integer getBrand() {
            return brand;
        }

        public Integer getFirm() {
            return firm;
        }

        public Integer getTotal() {
            return total;
        }

        public Integer getShop() {
            return shop;
        }
    }
}
