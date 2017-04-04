package com.diablo.dt.diablo.controller;

import android.widget.TableLayout;

import com.diablo.dt.diablo.model.sale.SaleStock;
import com.diablo.dt.diablo.utils.DiabloEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/4.
 */

public class DiabloStockTableController {
    private TableLayout mTable;
    private List<DiabloStockRowController> mControllers;

    public DiabloStockTableController (TableLayout table) {
        this.mTable = table;
        mControllers = new ArrayList<>();
    }

    public void addRowControllerAtTop(DiabloStockRowController controller) {
        mTable.addView(controller.getView().getView(), 0);
        mControllers.add(0, controller);
    }

    public Integer getCurrentRows() {
        return mControllers.size();
    }

    public List<DiabloStockRowController> getControllers() {
        return mControllers;
    }

    public void removeRowAtTop() {
        // TableRow row = (TableRow) mTable.getChildAt(0);
        DiabloStockRowController controller = mControllers.get(0);
        controller.remove();
        mTable.removeView(controller.getView().getView());
        mControllers.remove(0);
    }

    public void removeByOrderId(Integer orderId) {
        Integer removeIndex = DiabloEnum.INVALID_INDEX;
        DiabloStockRowController controller = null;
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
        for (DiabloStockRowController c: mControllers) {
            // first order always 0
            if (!c.getOrderId().equals(0)) {
                c.setOrderId(orderId);
                orderId--;
            }
        }
    }

    public Integer contains(DiabloStockRowController controller) {
        Integer orderId = DiabloEnum.INVALID_INDEX;
        for (DiabloStockRowController c: mControllers) {
            if (0 != c.getOrderId() && c.isSameModel(controller)) {
                orderId = c.getOrderId();
                break;
            }
        }

        return orderId;
    }

    public void replaceRowController(final SaleStock stock) {
        for (DiabloStockRowController c: mControllers) {
            if (c.getOrderId().equals(stock.getOrderId())) {
                // c.replaceSaleStock(stock);
            }
        }
    }

    public void calcStockInShouldPay(DiabloStockCalcController stockController){
        // calculate stock
        Integer total     = 0;
        Float shouldPay   = 0f;

        for (DiabloStockRowController controller: mControllers) {
            if (0 != controller.getOrderId()) {

                total += controller.getEntryTotal();
                shouldPay += controller.calcStockPrice();
            }
        }

        stockController.setStockTotal(total);
        stockController.setShouldPay(shouldPay);
        stockController.resetAccBalance();
    }

    public void calcStockOutShouldPay(DiabloStockCalcController saleController){
        Integer total     = 0;
        Float   shouldPay = 0f;

        for (DiabloStockRowController controller: mControllers) {
            if (0 != controller.getOrderId()) {
                total += controller.getEntryTotal();
                // shouldPay += controller.getSalePrice();
            }
        }

//        saleController.setSaleInfo(total);
//        saleController.setShouldPay(shouldPay);
//        saleController.resetAccBalance();
    }

    public boolean checkSameFirm(Integer firm) {
        boolean same = true;
        for (int i=0; i<mControllers.size(); i++) {
            if (!mControllers.get(i).getModel().getFirmId().equals(firm)) {
                same = false;
                break;
            }
        }

        return same;
    }

    public Integer size() {
        return mControllers.size();
    }

    public void clear() {
        mTable.removeAllViews();
        mControllers.clear();
    }
}
