package com.diablo.dt.diablo.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by buxianhui on 17/4/6.
 */

public class GetStockNewResponse extends Response {
    @SerializedName("stock")
    private StockDetailResponse.StockDetail stockCalc;
    @SerializedName("detail")
    private List<StockNote> StockNotes;

    public GetStockNewResponse() {
        StockNotes = null;
    }

    public StockDetailResponse.StockDetail getStockCalc() {
        return stockCalc;
    }

    public List<StockNote> getStockNotes() {
        return StockNotes;
    }

    public static class StockNote {
        @SerializedName("rsn")
        private String rsn;
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
        @SerializedName("firm_id")
        private Integer firmId;
        @SerializedName("s_group")
        private Integer sGroup;
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

        @SerializedName("color_id")
        private Integer colorId;
        @SerializedName("size")
        private String size;

        @SerializedName("amount")
        private Integer amount;

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

        public Integer getSex() {
            return sex;
        }

        public Integer getSeason() {
            return season;
        }

        public Integer getFirmId() {
            return firmId;
        }

        public Integer getsGroup() {
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

        public Integer getColorId() {
            return colorId;
        }

        public String getSize() {
            return size;
        }

        public Integer getAmount() {
            return amount;
        }
    }
}
