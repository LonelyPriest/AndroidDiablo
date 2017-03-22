package com.diablo.dt.diablo.model;

import com.google.gson.Gson;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.fragment.StockSelect;
import com.diablo.dt.diablo.utils.DiabloEnum;

/**
 * Created by buxianhui on 17/3/23.
 */

public class SaleUtils {
    public static void switchToStockSelectFrame(
        SaleStock stock,
        Integer action,
        Integer comeFrom,
        Integer retailer,
        Integer shop,
        Fragment from) {

        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        // find
        StockSelect to = (StockSelect) from.getFragmentManager().findFragmentByTag(DiabloEnum.TAG_STOCK_SELECT);

        if (null == to){
            Bundle args = new Bundle();
            args.putInt(DiabloEnum.BUNDLE_PARAM_SHOP, shop);
            args.putInt(DiabloEnum.BUNDLE_PARAM_RETAILER, retailer);
            args.putInt(DiabloEnum.BUNDLE_PARAM_ACTION, action);
            args.putInt(DiabloEnum.BUNDLE_PARAM_COME_FORM, comeFrom);
            args.putString(DiabloEnum.BUNDLE_PARAM_SALE_STOCK, new Gson().toJson(stock));
            to = new StockSelect();
            to.setArguments(args);
        } else {
            to.setSelectShop(shop);
            to.setSelectRetailer(retailer);
            to.setComeFrom(comeFrom);
            to.setSelectAction(action);
            to.setSaleStock(new Gson().toJson(stock));
        }

        if (!to.isAdded()){
            transaction.hide(from).add(R.id.frame_container, to, DiabloEnum.TAG_STOCK_SELECT).commit();
        } else {
            transaction.hide(from).show(to).commit();
        }
    }
}
