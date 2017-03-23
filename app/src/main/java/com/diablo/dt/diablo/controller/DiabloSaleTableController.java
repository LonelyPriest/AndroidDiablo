package com.diablo.dt.diablo.controller;

import android.widget.TableLayout;

import com.diablo.dt.diablo.utils.DiabloEnum;

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

    public void removeByOrderId(Integer orderId) {
        Integer removeIndex = DiabloEnum.INVALID_INDEX;
        DiabloSaleRowController controller = null;
        for (int i=0; i<mControllers.size(); i++) {
            if (mControllers.get(i).getOrderId().equals(orderId)) {
                controller = mControllers.get(i);
                removeIndex = i;
                break;
            }
        }

        if (!DiabloEnum.INVALID_INDEX.equals(removeIndex)) {
            if (null != controller){
                controller.remove();
                mTable.removeView(controller.getView().getView());
            }

            mControllers.remove(removeIndex.intValue());
        }
    }

    public void reorder() {
        Integer orderId = mControllers.size() - 1;
        for (DiabloSaleRowController c: mControllers) {
            // first order always 0
            if (!c.getOrderId().equals(0)) {
                c.setOrderId(orderId);
                orderId--;
            }
        }
    }

    public Integer size() {
        return mControllers.size();
    }

    public void clear() {
        mTable.removeAllViews();
        mControllers.clear();
    }

//    public DiabloSaleRowController getControllerByOrderId(Integer orderId) {
//        DiabloSaleRowController controller = null;
//        for (DiabloSaleRowController c: mControllers) {
//            if (c.getOrderId().equals(orderId)) {
//                controller = c;
//            }
//        }
//
//        return controller;
//    }
}
