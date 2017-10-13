package com.diablo.dt.diablo.response.sale;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.Response;

import java.util.List;

/**
 * Created by buxianhui on 17/10/13.
 */

public class SaleNoteDetailResponse extends Response {

    @SerializedName("data")
    List<SaleNoteDetail> saleNoteDetails;

    public List<SaleNoteDetail> getSaleNoteDetails() {
        return this.saleNoteDetails;
    }

    public final SaleNoteDetail getSaleNoteDetail(Integer colorId, String size) {
        SaleNoteDetail found = null;
        for (SaleNoteDetail n: this.saleNoteDetails) {
            if (colorId.equals(n.getColorId()) && size.equals(n.getSize())) {
                found = n;
                break;
            }
        }
        return found;
    }

    public static class SaleNoteDetail {
        @SerializedName("id")
        private Integer id;
        @SerializedName("rsn")
        private String rsn;
        @SerializedName("style_number")
        private String styleNumber;
        @SerializedName("brand_id")
        private Integer brandId;
        @SerializedName("color_id")
        private Integer colorId;
        @SerializedName("size")
        private String size;
        @SerializedName("amount")
        private Integer total;

        public Integer getId() {
            return id;
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

        public Integer getColorId() {
            return colorId;
        }

        public String getSize() {
            return size;
        }

        public Integer getTotal() {
            return total;
        }
    }
}
