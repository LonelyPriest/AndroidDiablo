package com.diablo.dt.diablo.controller;

import com.diablo.dt.diablo.model.DiabloRowModel;
import com.diablo.dt.diablo.view.DiabloRowView;

/**
 * Created by buxianhui on 17/3/18.
 */

public class DiabloSaleRowController {
    private DiabloRowModel mRowModel;
    private DiabloRowView mRowView;

    public DiabloSaleRowController(DiabloRowModel rowModel, DiabloRowView rowView) {
        this.mRowModel = rowModel;
        this.mRowView  = rowView;
    }
}
