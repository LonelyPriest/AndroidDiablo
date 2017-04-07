package com.diablo.dt.diablo.model.stock;

import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.utils.DiabloEnum;

/**
 * Created by buxianhui on 17/4/4.
 */

public class EntryStockAmount {
    private Integer colorId;
    private String  colorName;
    private String  size;
    private Integer count;

    // use to update 'a', 'd', 'u'
    private String operation;

    private EntryStockAmount() {

    }

    public EntryStockAmount(Integer colorId, String size){
        this.colorId = colorId;
        this.size = size;
        if (!DiabloEnum.DIABLO_FREE_COLOR.equals(colorId)) {
            this.colorName = Profile.instance().getColorName(colorId);
        }
        this.count = 0;
    }

    public EntryStockAmount(DiabloColor color, String size){
        this.colorId = color.getColorId();
        this.size = size;
        if (!DiabloEnum.DIABLO_FREE_COLOR.equals(colorId)) {
            this.colorName = color.getName();
        }
        this.count = 0;
    }

    public EntryStockAmount(EntryStockAmount amount, String operation){
        this.colorId   = amount.getColorId();
        this.size      = amount.getSize();
        this.colorName = amount.getColorName();
        this.count     = amount.getCount();
        this.operation = operation;
    }


    public Integer getColorId() {
        return colorId;
    }

    public void setColorId(Integer colorId) {
        this.colorId = colorId;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getOperation() {
        return operation;
    }
}
