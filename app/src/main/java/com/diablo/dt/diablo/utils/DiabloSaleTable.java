package com.diablo.dt.diablo.utils;

import android.widget.TableLayout;

import com.diablo.dt.diablo.entity.DiabloColor;

import java.util.List;

/**
 * Created by buxianhui on 17/3/15.
 */

public class DiabloSaleTable {
    private TableLayout mTableLayout;
    private List<DiabloColor> mOrderedColors;
    private List<String> mOrderedSizes;

    // batch operation
    private boolean mBatch;

    public DiabloSaleTable (TableLayout table, List<DiabloColor> colors, List<String> sizes){
        this.mTableLayout = table;
        this.mOrderedColors = colors;
        this.mOrderedSizes = sizes;
    }
}
