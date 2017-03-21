package com.diablo.dt.diablo.controller;

import com.diablo.dt.diablo.model.SaleTableModel;
import com.diablo.dt.diablo.view.DiabloTableView;

/**
 * Created by buxianhui on 17/3/21.
 */

public class DiabloSaleTableController {
    private DiabloTableView mTableView;
    private SaleTableModel mTableModel;

    public DiabloSaleTableController(DiabloTableView tableView, SaleTableModel tableModel) {
        this.mTableView = tableView;
        this.mTableModel = tableModel;
    }
}
