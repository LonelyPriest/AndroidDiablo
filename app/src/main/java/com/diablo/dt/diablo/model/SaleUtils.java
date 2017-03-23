package com.diablo.dt.diablo.model;

import com.google.gson.Gson;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.activity.MainActivity;
import com.diablo.dt.diablo.fragment.SaleDetail;
import com.diablo.dt.diablo.fragment.SaleIn;
import com.diablo.dt.diablo.fragment.SaleOut;
import com.diablo.dt.diablo.fragment.StockSelect;
import com.diablo.dt.diablo.utils.DiabloEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by buxianhui on 17/3/23.
 */

public class SaleUtils {
    public static final Map<String, Integer> SLIDE_MENU_TAGS;
    static {
        SLIDE_MENU_TAGS = new HashMap<>();
        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_SALE_IN, 0);
        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_SALE_OUT, 1);
        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_SALE_DETAIL, 2);
        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_SALE_NOTE, 3);

        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_STOCK_IN, 0);
        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_STOCK_OUT, 1);
        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_STOCK_DETAIL, 2);
        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_STOCK_NOTE, 3);
    }

    public static void switchToSlideMenu(Fragment from, String toTag) {
        Fragment f = from.getFragmentManager().findFragmentByTag(toTag);
        if (null == f){
            switch (toTag) {
                case DiabloEnum.TAG_SALE_DETAIL:
                    f = new SaleDetail();
                    break;
                case DiabloEnum.TAG_SALE_IN:
                    f = new SaleIn();
                    break;
                case DiabloEnum.TAG_SALE_OUT:
                    f = new SaleOut();
                    break;
                default:
                    break;
            }
        }

        if (null != SLIDE_MENU_TAGS.get(toTag)) {
            ((MainActivity)from.getActivity()).selectMenuItem(SLIDE_MENU_TAGS.get(toTag));
            ((MainActivity)from.getActivity()).switchFragment(f, toTag);
        }
    }

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
