package com.diablo.dt.diablo.model.stock;

import com.google.gson.Gson;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.Gravity;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.fragment.stock.GoodSelect;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.view.DiabloCellLabel;

import java.util.List;

/**
 * Created by buxianhui on 17/3/25.
 */

public class StockUtils {
    public static final Integer STOCK_TOTAL_CHANGED = 1;

    //
    public static final Integer STARTING_STOCK = 0;
    public static final Integer FINISHED_STOCK = 1;

    public static DiabloCellLabel[] createStockLabelsFromTitle(Context context) {
        String [] heads = context.getResources().getStringArray(R.array.thead_stock);

        DiabloCellLabel [] labels = new DiabloCellLabel[heads.length];

        DiabloCellLabel label = null;
        for (int i=0; i< heads.length; i++) {
            String h = heads[i];
            if (context.getResources().getString(R.string.order_id).equals(h)) {
                label = new DiabloCellLabel(
                    h,
                    DiabloEnum.DIABLO_TEXT,
                    R.color.colorPrimaryDark,
                    18,
                    0.5f);
                label.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                label.setLabelId(R.string.order_id);
            }
            else if (context.getResources().getString(R.string.good).equals(h)) {
                label = new DiabloCellLabel(
                    h,
                    DiabloEnum.DIABLO_AUTOCOMPLETE,
                    R.color.black,
                    18,
                    InputType.TYPE_CLASS_NUMBER,
                    1.5f);
                label.setLabelId(R.string.good);
            }
            else if (context.getResources().getString(R.string.year).equals(h)) {
                label = new DiabloCellLabel(
                    h,
                    DiabloEnum.DIABLO_TEXT,
                    R.color.black,
                    18,
                    0.8f);
                label.setGravity(Gravity.CENTER_VERTICAL);
                label.setLabelId(R.string.year);
            }
            else if (context.getResources().getString(R.string.season).equals(h)) {
                label = new DiabloCellLabel(
                    h,
                    DiabloEnum.DIABLO_TEXT,
                    R.color.black,
                    18,
                    1f);
                label.setGravity(Gravity.CENTER_VERTICAL);
                label.setLabelId(R.string.season);
            }
            else if (context.getResources().getString(R.string.org_price).equals(h)) {
                label = new DiabloCellLabel(h, R.color.black, 18);
                // label.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                label.setLabelId(R.string.org_price);
            }
            else if (context.getResources().getString(R.string.amount).equals(h)) {
                label = new DiabloCellLabel(
                    h,
                    DiabloEnum.DIABLO_EDIT,
                    R.color.red,
                    18,
                    Gravity.CENTER_VERTICAL,
                    InputType.TYPE_CLASS_NUMBER,
                    false,
                    1f);
                label.setGravity(Gravity.CENTER_VERTICAL);
                label.setLabelId(R.string.amount);
            }
            else if (context.getResources().getString(R.string.calculate).equals(h)) {
                label = new DiabloCellLabel(h, R.color.black, 18);
                // label.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                label.setLabelId(R.string.calculate);
            }
            if (null != label ){
                labels[i] = label;
                // mSparseLabels.put(label.getLabelId(), label);
            }
        }

        return labels;
    }

    public static void switchToStockSelectFrame(
        Integer shop,
        EntryStock stock,
        Integer operation,
        Integer comeFrom,
        Fragment from) {

        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        // find
        GoodSelect to = (GoodSelect) from.getFragmentManager().findFragmentByTag(DiabloEnum.TAG_GOOD_SELECT);

        if (null == to){
            Bundle args = new Bundle();
            args.putInt(DiabloEnum.BUNDLE_PARAM_SHOP, shop);
            args.putInt(DiabloEnum.BUNDLE_PARAM_ACTION, operation);
            args.putInt(DiabloEnum.BUNDLE_PARAM_COME_FORM, comeFrom);
            args.putString(DiabloEnum.BUNDLE_PARAM_SALE_STOCK, new Gson().toJson(stock));
            to = new GoodSelect();
            to.setArguments(args);
        } else {
            to.setShop(shop);
            to.setSelectAction(operation);
            to.setComeFrom(comeFrom);
            to.setEntryStock(new Gson().toJson(stock));
        }

        if (!to.isAdded()){
            transaction.hide(from).add(R.id.frame_container, to, DiabloEnum.TAG_GOOD_SELECT).commit();
        } else {
            transaction.hide(from).show(to).commit();
        }
    }

    public static EntryStock getEntryStock(List<EntryStock> stocks, String styleNumber, Integer brandId){
        EntryStock stock = null;
        for (EntryStock s: stocks){
            if (styleNumber.equals(s.getStyleNumber()) && brandId.equals(s.getBrandId())){
                stock = s;
                break;
            }
        }

        return stock;
    }

    public static EntryStockAmount getEntryStockAmounts(
        final List<EntryStockAmount>amounts, Integer colorId, String size){
        EntryStockAmount found = null;
        for (EntryStockAmount amount: amounts) {
            if (amount.getColorId().equals(colorId) && amount.getSize().equals(size)) {
                found = amount;
                break;
            }
        }
        return found;
    }
}
