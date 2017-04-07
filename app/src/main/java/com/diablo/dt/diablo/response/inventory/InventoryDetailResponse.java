package com.diablo.dt.diablo.response.inventory;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/7.
 */

public class InventoryDetailResponse extends Response {
    @SerializedName("total")
    private Integer total;

    @SerializedName("t_amount")
    private Integer amount;

    @SerializedName("t_sell")
    private Integer sell;

    @SerializedName("data")
    private List<inventory> inventories;

    public InventoryDetailResponse() {
        total = 0;
        amount = 0;
        sell = 0;

        inventories = new ArrayList<>();
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getAmount() {
        return amount;
    }

    public Integer getSell() {
        return sell;
    }

    public List<inventory> getInventories() {
        return inventories;
    }

    public static class inventory {
        @SerializedName("order_id")
        private Integer orderId;

        @SerializedName("id")
        private Integer id;
        @SerializedName("style_number")
        private String styleNumber;
        @SerializedName("brand_id")
        private Integer brandId;
        @SerializedName("type_id")
        private Integer typeId;
        @SerializedName("sex")
        private Integer sex;
        @SerializedName("season")
        private Integer season;
        @SerializedName("amount")
        private Integer amount;
        @SerializedName("firm_id")
        private Integer firmId;
        @SerializedName("s_group")
        private String sGroup;
        @SerializedName("free")
        private Integer free;
        @SerializedName("year")
        private Integer year;

        @SerializedName("org_price")
        private Float orgPrice;
        @SerializedName("tag_price")
        private Float tagPrice;
        @SerializedName("pkg_price")
        private Float pkgPrice;
        @SerializedName("price3")
        private Float price3;
        @SerializedName("price4")
        private Float price4;
        @SerializedName("price5")
        private Float price5;
        @SerializedName("discount")
        private Float discount;

        @SerializedName("path")
        private String path;

        @SerializedName("alarm_day")
        private Integer alarmDay;
        @SerializedName("sell")
        private Integer sell;
        @SerializedName("shop_id")
        private Integer shopId;
        @SerializedName("state")
        private Integer state;
        @SerializedName("last_sell")
        private String lastSell;
        @SerializedName("change_date")
        private String changeDate;
        @SerializedName("entry_date")
        private String datetime;
        @SerializedName("shop")
        private String shop;

        public inventory() {
            orgPrice = 0f;
            tagPrice = 0f;
            pkgPrice = 0f;
            price3   = 0f;
            price4   = 0f;
            price5   = 0f;
            discount = 0f;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public Integer getId() {
            return id;
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

        public Integer getSex() {
            return sex;
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

        public String getsGroup() {
            return sGroup;
        }

        public Integer getFree() {
            return free;
        }

        public Integer getYear() {
            return year;
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

        public String getPath() {
            return path;
        }

        public Integer getAlarmDay() {
            return alarmDay;
        }

        public Integer getSell() {
            return sell;
        }

        public Integer getShopId() {
            return shopId;
        }

        public Integer getState() {
            return state;
        }

        public String getLastSell() {
            return lastSell;
        }

        public String getChangeDate() {
            return changeDate;
        }

        public String getDatetime() {
            return datetime;
        }

        public String getShop() {
            return shop;
        }
    }
}
