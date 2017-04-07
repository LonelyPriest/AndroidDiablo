package com.diablo.dt.diablo.response.sale;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.Response;

import java.util.List;

/**
 * Created by buxianhui on 17/3/24.
 */

public class GetSaleNewResponse extends Response {
    @SerializedName("sale")
    private SaleDetailResponse.SaleDetail saleCalc;
    @SerializedName("detail")
    private List<SaleNote> saleNotes;
//    @SerializedName("inv")
//    private List<Stock> stocks;

    public GetSaleNewResponse() {
        saleNotes = null;
    }

    public SaleDetailResponse.SaleDetail getSaleCalc() {
        return saleCalc;
    }

    public List<SaleNote> getSaleNotes() {
        return saleNotes;
    }

    public static class SaleNote {
        @SerializedName("id")
        private Integer id;

        @SerializedName("rsn")
        private String rsn;
        @SerializedName("style_number")
        private String styleNumber;
        @SerializedName("s_group")
        private String sizeGroup;

        @SerializedName("brand_id")
        private Integer brandId;
        @SerializedName("type_id")
        private Integer typeId;
        @SerializedName("free")
        private Integer free;
        @SerializedName("season")
        private Integer season;
        @SerializedName("firm_id")
        private Integer firmId;
        @SerializedName("total")
        private Integer saleTotal;
        @SerializedName("sell_style")
        private Integer selectedPrice;
        @SerializedName("second")
        private Integer second;

        @SerializedName("fdiscount")
        private Float discount;
        @SerializedName("fprice")
        private Float finalPrice;

        @SerializedName("path")
        private String path;
        @SerializedName("comment")
        private String comment;

        @SerializedName("color_id")
        private Integer color;
        @SerializedName("size")
        private String size;
        @SerializedName("amount")
        private Integer amount;

        public Integer getId() {
            return id;
        }

        public String getRsn() {
            return rsn;
        }

        public String getStyleNumber() {
            return styleNumber;
        }

        public String getSizeGroup() {
            return sizeGroup;
        }

        public Integer getBrandId() {
            return brandId;
        }

        public Integer getTypeId() {
            return typeId;
        }

        public Integer getFree() {
            return free;
        }

        public Integer getSeason() {
            return season;
        }

        public Integer getFirmId() {
            return firmId;
        }

        public Integer getSaleTotal() {
            return saleTotal;
        }

        public Integer getSelectedPrice() {
            return selectedPrice;
        }

        public Integer getSecond() {
            return second;
        }

        public Float getDiscount() {
            return discount;
        }

        public Float getFinalPrice() {
            return finalPrice;
        }

        public String getPath() {
            return path;
        }

        public String getComment() {
            return comment;
        }

        public Integer getColor() {
            return color;
        }

        public String getSize() {
            return size;
        }

        public Integer getAmount() {
            return amount;
        }
    }
}
