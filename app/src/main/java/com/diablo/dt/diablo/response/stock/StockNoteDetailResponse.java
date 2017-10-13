package com.diablo.dt.diablo.response.stock;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.Response;

import java.util.List;

/**
 * Created by buxianhui on 17/10/13.
 */

public class StockNoteDetailResponse extends Response {
    @SerializedName("data")
    List<StockNoteDetail> stockNoteDetails;

    public List<StockNoteDetail> getStockNoteDetails() {
        return this.stockNoteDetails;
    }

    public final StockNoteDetail getStockNoteDetail(Integer colorId, String size) {
        StockNoteDetail found = null;
        for (StockNoteDetail n: this.stockNoteDetails) {
            if (colorId.equals(n.getColorId()) && size.equals(n.getSize())) {
                found = n;
                break;
            }
        }
        return found;
    }

    public static class StockNoteDetail {
        @SerializedName("rsn")
        private String rsn;
        @SerializedName("style_number")
        private String styleNumber;
        @SerializedName("brand_id")
        private Integer brandId;
        @SerializedName("color_id")
        private Integer colorId;
        @SerializedName("color")
        private String color;
        @SerializedName("size")
        private String size;
        @SerializedName("amount")
        private Integer total;

        public String getRsn() {
            return rsn;
        }

        public String getStyleNumber() {
            return styleNumber;
        }

        public Integer getBrandId() {
            return brandId;
        }

        public Integer getColorId() {
            return colorId;
        }

        public String getColor() {
            return color;
        }

        public String getSize() {
            return size;
        }

        public Integer getTotal() {
            return total;
        }
    }
}

