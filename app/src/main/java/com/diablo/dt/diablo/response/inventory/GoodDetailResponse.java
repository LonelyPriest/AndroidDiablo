package com.diablo.dt.diablo.response.inventory;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.response.Response;
import com.diablo.dt.diablo.utils.DiabloEnum;

import java.util.List;

/**
 * Created by buxianhui on 17/4/9.
 */

public class GoodDetailResponse extends Response {
    @SerializedName("total")
    private Integer total;
    @SerializedName("data")
    private List<GoodNote> mGoods;

    public GoodDetailResponse() {
        super();
        total = DiabloEnum.INVALID_INDEX;
    }

    public List<GoodNote> getGoods() {
        return mGoods;
    }

    public Integer getTotal() {
        return total;
    }

    public static class GoodNote {
        @SerializedName("order_id")
        private Integer orderId;

        @SerializedName("id")
        private Integer id;
        @SerializedName("style_number")
        private String styleNumber;
        @SerializedName("brand_id")
        private Integer brandId;
        @SerializedName("firm_id")
        private Integer firmId;
        @SerializedName("type_id")
        private Integer typeId;

        @SerializedName("sex")
        private Integer sex;
        @SerializedName("color")
        private String colors;
        @SerializedName("year")
        private Integer year;
        @SerializedName("season")
        private Integer season;
        @SerializedName("size")
        private String sizes;
        @SerializedName("s_group")
        private String sGroup;
        @SerializedName("free")
        private Integer free;

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
        @SerializedName("entry_date")
        private String datetime;

        public GoodNote() {
            this.orderId = DiabloEnum.INVALID_INDEX;
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

        public Integer getFirmId() {
            return firmId;
        }

        public Integer getTypeId() {
            return typeId;
        }

        public Integer getSex() {
            return sex;
        }

        public String getColors() {
            return colors;
        }

        public Integer getYear() {
            return year;
        }

        public Integer getSeason() {
            return season;
        }

        public String getSizes() {
            return sizes;
        }

        public String getsGroup() {
            return sGroup;
        }

        public Integer getFree() {
            return free;
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

        public String getDatetime() {
            return datetime;
        }
    }
}
