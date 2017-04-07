package com.diablo.dt.diablo.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by buxianhui on 17/4/7.
 */

public class StockNoteResponse extends Response{
    @SerializedName("total")
    private Integer total;
    @SerializedName("t_amount")
    private Integer amount;

    @SerializedName("data")
    List<StockNote> stockNotes;

    public Integer getTotal() {
        return total;
    }

    public Integer getAmount() {
        return amount;
    }

    public List<StockNote> getStockNotes() {
        return stockNotes;
    }

    public static class StockNote {
        @SerializedName("id")
        Integer id;
        @SerializedName("order_id")
        Integer orderId;
        @SerializedName("rsn")
        private String rsn;
        // stock-in or stock-out
        @SerializedName("type")
        private Integer type;

        @SerializedName("style_number")
        private String styleNumber;
        @SerializedName("brand_id")
        private Integer brandId;
        @SerializedName("type_id")
        private Integer typeId;
        @SerializedName("season")
        private Integer season;
        @SerializedName("amount")
        private Integer amount;
        @SerializedName("firm_id")
        private Integer firmId;

        @SerializedName("discount")
        private Integer discount;
        @SerializedName("s_group")
        private String sGroup;
        @SerializedName("free")
        private Integer free;
        @SerializedName("year")
        private Integer year;
        @SerializedName("entry_date")
        private String datetime;
        @SerializedName("employee_id")
        private String employeeId;

        public Integer getId() {
            return id;
        }

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public String getRsn() {
            return rsn;
        }

        public Integer getType() {
            return type;
        }

        public String getStyleNumber() {
            return styleNumber;
        }

        public Integer getBrandId() {
            return brandId;
        }

        public Integer getTypeId() {
            return typeId;
        }

        public Integer getSeason() {
            return season;
        }

        public Integer getAmount() {
            return amount;
        }

        public Integer getFirmId() {
            return firmId;
        }

        public Integer getDiscount() {
            return discount;
        }

        public String getsGroup() {
            return sGroup;
        }

        public Integer getFree() {
            return free;
        }

        public Integer getYear() {
            return year;
        }

        public String getDatetime() {
            return datetime;
        }

        public String getEmployeeId() {
            return employeeId;
        }
    }
}
