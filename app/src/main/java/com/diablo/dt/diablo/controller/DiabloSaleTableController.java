package com.diablo.dt.diablo.controller;

import com.diablo.dt.diablo.model.DiabloTableModel;
import com.diablo.dt.diablo.view.DiabloTableView;

/**
 * Created by buxianhui on 17/3/21.
 */

public class DiabloSaleTableController {
    private DiabloTableView mTableView;
    private DiabloTableModel mTableModel;

    public DiabloSaleTableController(DiabloTableView tableView, DiabloTableModel tableModel) {
        this.mTableView = tableView;
        this.mTableModel = tableModel;
    }
}
