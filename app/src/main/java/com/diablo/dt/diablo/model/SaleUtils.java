package com.diablo.dt.diablo.model;

import static com.diablo.dt.diablo.R.string.amount;
import static com.diablo.dt.diablo.R.string.stock;

import com.google.gson.Gson;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.activity.MainActivity;
import com.diablo.dt.diablo.fragment.SaleDetail;
import com.diablo.dt.diablo.fragment.SaleIn;
import com.diablo.dt.diablo.fragment.SaleOut;
import com.diablo.dt.diablo.fragment.StockSelect;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.view.DiabloCellLabel;

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

    public static DiabloCellLabel[] createSaleLabelsFromTitle(Context context) {
        String [] heads = context.getResources().getStringArray(R.array.thead_sale);

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
                    0.8f);
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
                    2f);
                label.setLabelId(R.string.good);
            }
            else if (context.getResources().getString(stock).equals(h)) {
                label = new DiabloCellLabel(h, R.color.red, 18);
                label.setLabelId(R.string.stock);
            }
            else if (context.getResources().getString(R.string.price_type).equals(h)) {
                label = new DiabloCellLabel(
                    h, DiabloEnum.DIABLO_SPINNER, R.color.black, 18, 1.5f);
                label.setLabelId(R.string.price_type);
            }
            else if (context.getResources().getString(R.string.price).equals(h)) {
                label = new DiabloCellLabel(h, R.color.black, 18);
                label.setLabelId(R.string.price);
            }
            else if (context.getResources().getString(R.string.discount).equals(h)) {
                label = new DiabloCellLabel(h, R.color.black, 18);
                label.setLabelId(R.string.discount);
            }
            else if (context.getResources().getString(R.string.fprice).equals(h)) {
                label = new DiabloCellLabel(
                    h,
                    DiabloEnum.DIABLO_EDIT,
                    R.color.black,
                    18,
                    Gravity.CENTER_VERTICAL,
                    InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL,
                    false,
                    1f);
                label.setLabelId(R.string.fprice);
            }
            else if (context.getResources().getString(amount).equals(h)) {
                label = new DiabloCellLabel(
                    h,
                    DiabloEnum.DIABLO_EDIT,
                    R.color.red,
                    18,
                    Gravity.CENTER_VERTICAL,
                    InputType.TYPE_CLASS_NUMBER,
                    false,
                    1f);
                label.setLabelId(amount);
            }
            else if (context.getResources().getString(R.string.calculate).equals(h)) {
                label = new DiabloCellLabel(h, R.color.black, 18);
                label.setLabelId(R.string.calculate);
            }
            else if (context.getResources().getString(R.string.comment).equals(h)) {
                label = new DiabloCellLabel(
                    h,
                    DiabloEnum.DIABLO_TEXT,
                    R.color.black,
                    16,
                    InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS,
                    1.5f);
                label.setLabelId(R.string.comment);
            }

            if (null != label ){
                labels[i] = label;
                // mSparseLabels.put(label.getLabelId(), label);
            }
        }

        return labels;
    }

    public static TableRow createTableHeadFromLabels(Context context, final DiabloCellLabel[] labels) {
        TableRow row = new TableRow(context);

        for (DiabloCellLabel label: labels) {
            TextView cell = new TextView(context);
            cell.setLayoutParams(label.getTableRowLayoutParams());
            cell.setTypeface(null, Typeface.BOLD);
            cell.setTextColor(ContextCompat.getColor(context, R.color.black));
            cell.setTextSize(label.getSize());
            cell.setText(label.getLabel());

            row.addView(cell);
        }

        return row;

    }
}
