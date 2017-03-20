package com.diablo.dt.diablo.model;

import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.utils.DiabloEnum;

/**
 * Created by buxianhui on 17/3/15.
 */

public class SaleStockAmount {
    private Integer colorId;
    private String  colorName;
    private String  size;
    private Integer stock;
    private Integer sellCount;

    public SaleStockAmount(Integer colorId, String colorName, String size){
        this.colorId = colorId;
        this.size = size;
        this.colorName = colorName;
        this.stock = 0;
        this.sellCount = 0;
    }

//    public SaleStockAmount(Integer colorId, String size, Integer sellCount){
//        this.colorId = colorId;
//        this.size = size;
//        if (!DiabloEnum.DIABLO_FREE_COLOR.equals(colorId)) {
//            this.colorName = Profile.instance().getColorName(colorId);
//        }
//        this.stock = 0;
//        this.sellCount = sellCount;
//    }

    public SaleStockAmount(Integer colorId, String size){
        this.colorId = colorId;
        this.size = size;
        if (!DiabloEnum.DIABLO_FREE_COLOR.equals(colorId)) {
            this.colorName = Profile.instance().getColorName(colorId);
        }
        this.stock = 0;
        this.sellCount = 0;
    }

    public SaleStockAmount(SaleStockAmount amount){
        this.colorId = amount.getColorId();
        this.size = amount.getSize();
        this.colorName = amount.getColorName();
        this.stock = amount.getStock();
        this.sellCount = amount.getSellCount();
    }

    public Integer getColorId() {
        return colorId;
    }

    public String getColorName() {
        return colorName;
    }

    public String getSize() {
        return size;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSellCount() {
        return sellCount;
    }

    public void setSellCount(Integer sellCount) {
        this.sellCount = sellCount;
    }
}
