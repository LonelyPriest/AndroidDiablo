package com.diablo.dt.diablo.controller;

import android.widget.TableLayout;

import com.diablo.dt.diablo.model.sale.SaleStock;
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

    public Integer contains(DiabloSaleRowController controller) {
        Integer orderId = DiabloEnum.INVALID_INDEX;
        for (DiabloSaleRowController c: mControllers) {
            if (0 != c.getOrderId() && c.isSameModel(controller)) {
                orderId = c.getOrderId();
                break;
            }
        }

        return orderId;
    }

    public void replaceRowController(final SaleStock stock) {
        for (DiabloSaleRowController c: mControllers) {
            if (c.getOrderId().equals(stock.getOrderId())) {
                c.replaceSaleStock(stock);
            }
        }
    }

    public void calcSaleInShouldPay(DiabloSaleController saleController){
        // calculate stock
        Integer total     = 0;
        Integer sell      = 0;
        Integer reject    = 0;
        Float   shouldPay = 0f;

        for (DiabloSaleRowController controller: mControllers) {
            if (0 != controller.getOrderId()) {
                Integer saleTotal = controller.getSaleTotal();

                if (saleTotal > 0) {
                    sell += saleTotal;
                } else {
                    reject += saleTotal;
                }

                total += saleTotal;
                shouldPay += controller.getSalePrice();
            }
        }

        saleController.setSaleInfo(total, sell, reject);
        saleController.setShouldPay(shouldPay);
        saleController.resetAccBalance();

    }

    public void calcSaleOutShouldPay(DiabloSaleController saleController){
        Integer total     = 0;
        Float   shouldPay = 0f;

        for (DiabloSaleRowController controller: mControllers) {
            if (0 != controller.getOrderId()) {
                total += controller.getSaleTotal();
                shouldPay += controller.getSalePrice();
            }
        }

        saleController.setSaleInfo(total);
        saleController.setShouldPay(shouldPay);
        saleController.resetAccBalance();
    }

    public Integer size() {
        return mControllers.size();
    }

    public void clear() {
        mTable.removeAllViews();
        mControllers.clear();
    }
}
