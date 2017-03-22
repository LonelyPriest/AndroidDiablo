package com.diablo.dt.diablo.controller;

import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/3/22.
 */

public class DiabloSaleTableController {
    private TableLayout mTable;
    private List<DiabloSaleRowController> mControllers;

    public DiabloSaleTableController (TableLayout table) {
        this.mTable = table;
        mControllers = new ArrayList<>();
    }

    public void addRowControllerAtTop(DiabloSaleRowController controller) {
        mTable.addView(controller.getView().getView(), 0);
        mControllers.add(0, controller);
    }

    public Integer getCurrentRows() {
        return mControllers.size();
    }

    public List<DiabloSaleRowController> getControllers() {
        return mControllers;
    }

    public void removeRowAtTop() {
        // TableRow row = (TableRow) mTable.getChildAt(0);
        DiabloSaleRowController controller = mControllers.get(0);
        controller.remove();
        mTable.removeView(controller.getView().getView());
        mControllers.remove(0);
    }
}
