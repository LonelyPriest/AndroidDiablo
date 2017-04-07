package com.diablo.dt.diablo.model.sale;

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

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.activity.MainActivity;
import com.diablo.dt.diablo.fragment.inventory.InventoryDetail;
import com.diablo.dt.diablo.fragment.sale.SaleDetail;
import com.diablo.dt.diablo.fragment.sale.SaleIn;
import com.diablo.dt.diablo.fragment.sale.SaleNote;
import com.diablo.dt.diablo.fragment.sale.SaleOut;
import com.diablo.dt.diablo.fragment.stock.StockDetail;
import com.diablo.dt.diablo.fragment.stock.StockIn;
import com.diablo.dt.diablo.fragment.stock.StockNote;
import com.diablo.dt.diablo.fragment.stock.StockOut;
import com.diablo.dt.diablo.fragment.sale.StockSelect;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.DiabloCellLabel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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

        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_STOCK_IN, 4);
        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_STOCK_OUT, 5);
        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_STOCK_DETAIL, 6);
        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_STOCK_NOTE, 7);
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
                case DiabloEnum.TAG_SALE_NOTE:
                    f = new SaleNote();
                    break;
                case DiabloEnum.TAG_STOCK_IN:
                    f = new StockIn();
                    break;
                case DiabloEnum.TAG_STOCK_OUT:
                    f = new StockOut();
                    break;
                case DiabloEnum.TAG_STOCK_DETAIL:
                    f = new StockDetail();
                    break;
                case DiabloEnum.TAG_STOCK_NOTE:
                    f = new StockNote();
                    break;
                case DiabloEnum.TAG_INVENTORY_DETAIL:
                    f = new InventoryDetail();
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
            else if (context.getResources().getString(R.string.stock).equals(h)) {
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
                label.setLabelId(R.string.amount);
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

    public static SaleStock getSaleStock(List<SaleStock> stocks, String styleNumber, Integer brandId){
        SaleStock stock = null;
        for (SaleStock s: stocks){
            if (styleNumber.equals(s.getStyleNumber()) && brandId.equals(s.getBrandId())){
                stock = s;
                break;
            }
        }

        return stock;
    }

    public static SaleStockAmount getSaleStockAmounts(final List<SaleStockAmount>amounts, Integer colorId, String size){
        SaleStockAmount found = null;
        for (SaleStockAmount amount: amounts) {
            if (amount.getColorId().equals(colorId) && amount.getSize().equals(size)) {
                found = amount;
                break;
            }
        }
        return found;
    }

    public static class DiabloDatePicker {
        public static void build(final Fragment fragment, final OnDateSetListener listener) {
            CalendarDatePickerDialogFragment dpd = new CalendarDatePickerDialogFragment();
            // dpd.setThemeCustom(R.style.DiabloBetterPickersDialogs);
            dpd.setThemeDark();
            dpd.show(fragment.getFragmentManager(), "datePicker");
            dpd.setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                @Override
                public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(year, monthOfYear, dayOfMonth);
                    String selectDate = DiabloUtils.mDateFormat.format(cal.getTime());

                    cal.add(Calendar.DATE, 1);
                    String nextDate = DiabloUtils.mDateFormat.format(cal.getTime());

                    // cal.clear();
                    listener.onDateSet(selectDate, nextDate);
                    // ((EditText)mViewStartDate).setText(UTILS.formatDate(year, monthOfYear, dayOfMonth));
                }
            });
        }


        public interface OnDateSetListener {
            public void onDateSet(String date, String nextDate);
        }
    }
}
