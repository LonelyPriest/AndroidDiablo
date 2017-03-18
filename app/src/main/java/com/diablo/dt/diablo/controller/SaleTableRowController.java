package com.diablo.dt.diablo.controller;

import android.content.Context;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.model.SaleStock;
import com.diablo.dt.diablo.view.DiabloCell;
import com.diablo.dt.diablo.view.DiabloTableRow;

/**
 * Created by buxianhui on 17/3/18.
 */

public class SaleTableRowController {
    private DiabloTableRow mRow;
    private SaleStock mSaleStock;

    private OnGoodListener mGoodListener;

    public SaleTableRowController(DiabloTableRow row){
        this.mRow = row;
    }

    public void setSaleStock(SaleStock stock){
        this.mSaleStock = stock;
    }

    public SaleStock getSaleStock(){
        return this.mSaleStock;
    }

    public void setGoodListener(OnGoodListener listener){
        this.mGoodListener = listener;
    }

    public void init(Context context){
        // listen good select
        DiabloCell cell = mRow.getCell(context.getResources().getString(R.string.good));

        ((AutoCompleteTextView)cell.getView()).setDropDownWidth(500);
        ((AutoCompleteTextView)cell.getView()).setThreshold(1);

        mGoodListener.onSelectGood(cell, mSaleStock);
    }

    public interface OnGoodListener {
        public void onSelectGood(DiabloCell cell, SaleStock stock);
    }
}
