package com.diablo.dt.diablo.request.good;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.DiabloSizeGroup;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/8.
 */

public class GoodNewRequest {
    @SerializedName("good")
    private Inventory inventory;

    public GoodNewRequest(Inventory inv) {
        this.inventory = inv;
    }

    public final Inventory getInventory() {
        return inventory;
    }

    public static class Inventory {
        @SerializedName("style_number")
        private String styleNumber;
        @SerializedName("brand_id")
        private Integer brandId;
        @SerializedName("brand")
        private String brand;
        @SerializedName("type_id")
        private Integer typeId;
        @SerializedName("type")
        private String type;

        @SerializedName("sex")
        private Integer sex;
        @SerializedName("firm")
        private Integer firm;
        @SerializedName("season")
        private Integer season;
        @SerializedName("year")
        private Integer year;

        @SerializedName("org_price")
        private Float orgPrice;
        @SerializedName("tag_price")
        private Float tagPrice;
        @SerializedName("pkg_price")
        private Float pkgPrice;
        @SerializedName("p3")
        private Float price3;
        @SerializedName("p4")
        private Float price4;
        @SerializedName("p5")
        private Float price5;
        @SerializedName("discount")
        private Float discount;

        @SerializedName("colors")
        private List<Integer> colors;
        @SerializedName("sizes")
        private List<DiabloSizes> sizes;

        @SerializedName("path")
        private String path;

        @SerializedName("alarm_day")
        private Integer alarmDay;

        public Inventory(final List<DiabloColor> diabloColors,
                         final List<DiabloSizeGroup> diabloSizeGroups) {
            if (0 != diabloColors.size()) {
                this.colors = new ArrayList<>();
                for (DiabloColor c: diabloColors) {
                    this.colors.add(c.getColorId());
                }
            }

            if (0 != diabloSizeGroups.size()) {
                this.sizes = new ArrayList<>();
                for (DiabloSizeGroup g: diabloSizeGroups) {
                    this.sizes.add(new DiabloSizes(g));
                }
            }

        }

        public void setStyleNumber(String styleNumber) {
            this.styleNumber = styleNumber;
        }

        public void setBrandId(Integer brandId) {
            this.brandId = brandId;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setTypeId(Integer typeId) {
            this.typeId = typeId;
        }

        public void setSex(Integer sex) {
            this.sex = sex;
        }

        public void setFirm(Integer firm) {
            this.firm = firm;
        }

        public void setSeason(Integer season) {
            this.season = season;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public void setOrgPrice(Float orgPrice) {
            this.orgPrice = orgPrice;
        }

        public void setTagPrice(Float tagPrice) {
            this.tagPrice = tagPrice;
        }

        public void setPkgPrice(Float pkgPrice) {
            this.pkgPrice = pkgPrice;
        }

        public void setPrice3(Float price3) {
            this.price3 = price3;
        }

        public void setPrice4(Float price4) {
            this.price4 = price4;
        }

        public void setPrice5(Float price5) {
            this.price5 = price5;
        }

        public void setDiscount(Float discount) {
            this.discount = discount;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public void setAlarmDay(Integer alarmDay) {
            this.alarmDay = alarmDay;
        }

        public String getColorsWithComma() {
            if (null != colors) {
                return android.text.TextUtils.join(DiabloEnum.SIZE_SEPARATOR, colors);
            } else {
                return DiabloUtils.instance().toString(DiabloEnum.DIABLO_FREE_COLOR);
            }

        }

        public String getSizeGroupsWithComma() {
            if (null != sizes && sizes.size() > 0) {
                List<Integer> sizeGroups = new ArrayList<>();
                for (DiabloSizes s: sizes) {
                    sizeGroups.add(s.getGroupId());
                }

                return android.text.TextUtils.join(DiabloEnum.SIZE_SEPARATOR, sizeGroups);
            } else {
                return DiabloEnum.DIABLO_FREE_SIZE;
            }

        }

        public String getSizesWithComma() {
            if (null != sizes && sizes.size() > 0) {
                List<Integer> sizeGroups = new ArrayList<>();
                for (DiabloSizes s : sizes) {
                    sizeGroups.add(s.getGroupId());
                }

                return android.text.TextUtils.join(DiabloEnum.SIZE_SEPARATOR,
                    Profile.instance().genSortedSizeNamesByGroups(
                        android.text.TextUtils.join(DiabloEnum.SIZE_SEPARATOR, sizeGroups))
                );
            }
            else {
                return DiabloEnum.DIABLO_FREE_SIZE;
            }
        }
    }

    private static class DiabloSizes {
        @SerializedName("id")
        private Integer groupId;
        @SerializedName("group")
        private List<String> sizesInGroup;

        DiabloSizes(DiabloSizeGroup group) {
            this.groupId = group.getGroupId();
            this.sizesInGroup = group.getSortedSizeNames();
        }

        private Integer getGroupId() {
            return groupId;
        }
    }
}
