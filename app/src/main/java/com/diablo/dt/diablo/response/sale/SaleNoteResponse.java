package com.diablo.dt.diablo.response.sale;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.Response;

import java.util.List;

/**
 * Created by buxianhui on 17/3/28.
 */

public class SaleNoteResponse extends Response {
    @SerializedName("total")
    private Integer total;
    @SerializedName("t_amount")
    private Integer amount;
    @SerializedName("t_balance")
    private Integer balance;

    @SerializedName("data")
    List<SaleNote> saleNotes;

    public static class SaleNote {
        @SerializedName("id")
        private Integer id;
        @SerializedName("order_id")
        private Integer orderId;
        @SerializedName("rsn")
        private String rsn;
        @SerializedName("style_number")
        private String styleNumber;
        @SerializedName("brand_id")
        private Integer brandId;
        @SerializedName("type_id")
        private Integer typeId;
        @SerializedName("firm_id")
        private Integer firmId;
        @SerializedName("s_group")
        private String sGroup;
        @SerializedName("free")
        private String free;
        @SerializedName("total")
        private Integer total;
        @SerializedName("fdiscount")
        private Float fdiscount;
        @SerializedName("fprice")
        private Float fprice;
        @SerializedName("path")
        private String path;
        @SerializedName("comment")
        private String comment;
        @SerializedName("entry_date")
        private String datetime;
        @SerializedName("second")
        private Integer second;
        @SerializedName("shop_id")
        private Integer shopId;
        @SerializedName("retailer_id")
        private Integer retailerId;
        @SerializedName("employee_id")
        private String employeeId;
        @SerializedName("sell_type")
        private Integer sellType;

        public Integer getId() {
            return id;
        }

        public Integer getOrderId () {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public String getRsn() {
            return rsn;
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

        public Integer getFirmId() {
            return firmId;
        }

        public String getsGroup() {
            return sGroup;
        }

        public String getFree() {
            return free;
        }

        public Integer getTotal() {
            return total;
        }

        public Float getFdiscount() {
            return fdiscount;
        }

        public Float getFprice() {
            return fprice;
        }

        public String getPath() {
            return path;
        }

        public String getComment() {
            return comment;
        }

        public String getDatetime() {
            return datetime;
        }

        public Integer getSecond() {
            return second;
        }

        public Integer getShopId() {
            return shopId;
        }

        public Integer getRetailerId() {
            return retailerId;
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public Integer getSellType() {
            return sellType;
        }
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getAmount() {
        return amount;
    }

    public Integer getBalance() {
        return balance;
    }

    public List<SaleNote> getSaleNotes() {
        return saleNotes;
    }
}
