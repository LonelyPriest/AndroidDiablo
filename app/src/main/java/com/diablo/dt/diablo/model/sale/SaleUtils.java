package com.diablo.dt.diablo.model.sale;

import static android.R.attr.action;
import static android.net.wifi.WifiConfiguration.Protocol.RSN;
import static com.diablo.dt.diablo.R.string.retailer;
import static com.diablo.dt.diablo.R.string.shop;
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
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.activity.MainActivity;
import com.diablo.dt.diablo.client.WSaleClient;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.fragment.firm.FirmPager;
import com.diablo.dt.diablo.fragment.good.GoodColorDetail;
import com.diablo.dt.diablo.fragment.good.GoodDetail;
import com.diablo.dt.diablo.fragment.good.GoodNew;
import com.diablo.dt.diablo.fragment.printer.BlueToothJolimarkFragment;
import com.diablo.dt.diablo.fragment.printer.SalePrint;
import com.diablo.dt.diablo.fragment.report.ReportDaily;
import com.diablo.dt.diablo.fragment.report.ReportMonth;
import com.diablo.dt.diablo.fragment.report.ReportReal;
import com.diablo.dt.diablo.fragment.retailer.RetailerDetail;
import com.diablo.dt.diablo.fragment.sale.SaleDetail;
import com.diablo.dt.diablo.fragment.sale.SaleIn;
import com.diablo.dt.diablo.fragment.sale.SaleNote;
import com.diablo.dt.diablo.fragment.sale.SaleOut;
import com.diablo.dt.diablo.fragment.sale.StockSelect;
import com.diablo.dt.diablo.fragment.stock.StockDetail;
import com.diablo.dt.diablo.fragment.stock.StockIn;
import com.diablo.dt.diablo.fragment.stock.StockNote;
import com.diablo.dt.diablo.fragment.stock.StockOut;
import com.diablo.dt.diablo.fragment.stock.StockStoreDetail;
import com.diablo.dt.diablo.request.sale.SaleNoteDetailRequest;
import com.diablo.dt.diablo.response.sale.GetSaleNewResponse;
import com.diablo.dt.diablo.response.sale.SaleNoteDetailResponse;
import com.diablo.dt.diablo.rest.WSaleInterface;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.DiabloCellLabel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by buxianhui on 17/3/23.
 */

public class SaleUtils {
    private static final Map<String, Integer> SLIDE_MENU_TAGS;
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
        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_STOCK_STORE_DETAIL, 8);


        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_GOOD_DETAIL, 9);
        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_GOOD_NEW, 10);
        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_GOOD_COLOR, 11);

        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_RETAILER_DETAIL, 12);
        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_FIRM_PAGER, 13);
        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_PRINT_SETTING, 14);

        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_REPORT_REAL, 15);
        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_REPORT_DAILY, 16);
        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_REPORT_MONTH, 17);
//        SLIDE_MENU_TAGS.put(DiabloEnum.TAG_RETAILER_NEW, 14);

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
                case DiabloEnum.TAG_STOCK_STORE_DETAIL:
                    f = new StockStoreDetail();
                    break;
                case DiabloEnum.TAG_GOOD_DETAIL:
                    f = new GoodDetail();
                    break;
                case DiabloEnum.TAG_GOOD_NEW:
                    f = new GoodNew();
                    break;
                case DiabloEnum.TAG_GOOD_COLOR:
                    f = new GoodColorDetail();
                    break;
                case DiabloEnum.TAG_RETAILER_DETAIL:
                    f = new RetailerDetail();
                    break;
                case DiabloEnum.TAG_FIRM_PAGER:
                    f = new FirmPager();
                    break;
                case DiabloEnum.TAG_PRINT_SETTING:
                    f = new BlueToothJolimarkFragment();
                    break;
                case DiabloEnum.TAG_REPORT_REAL:
                    f = new ReportReal();
                    break;
                case DiabloEnum.TAG_REPORT_DAILY:
                    f = new ReportDaily();
                    break;
                case DiabloEnum.TAG_REPORT_MONTH:
                    f = new ReportMonth();
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

    public static void switchToSalePrintFrame(String rsn, Fragment from) {
        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        // find
        SalePrint to = (SalePrint) from.getFragmentManager().findFragmentByTag(DiabloEnum.TAG_PRINT_WITH_COMPUTER);

        if (null == to){
            Bundle args = new Bundle();
            args.putString(DiabloEnum.BUNDLE_PARAM_RSN, rsn);
            to = new SalePrint();
            to.setArguments(args);
        } else {
            to.setPrintRSN(rsn);
        }

        if (!to.isAdded()){
            transaction.hide(from).add(R.id.frame_container, to, DiabloEnum.TAG_PRINT_WITH_COMPUTER).commit();
        } else {
            transaction.hide(from).show(to).commit();
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

    public static DiabloCellLabel[] createSaleLabelsFromTitle(Context context, Integer shop) {

        String showDiscount = Profile.instance().getConfig(
            shop,
            DiabloEnum.START_SHOW_DISCOUNT,
            DiabloEnum.DIABLO_CONFIG_YES);

        String showPriceType = Profile.instance().getConfig(
            shop,
            DiabloEnum.START_SHOW_PRICE_TYPE,
            DiabloEnum.DIABLO_CONFIG_NO);

        String reverseSaleTitle = Profile.instance().getConfig(
            shop,
            DiabloEnum.START_REVERSE_SALE_TITLE,
            DiabloEnum.DIABLO_CONFIG_NO);

        String [] saleTitles;
        if (reverseSaleTitle.equals(DiabloEnum.DIABLO_CONFIG_YES)) {
            saleTitles = context.getResources().getStringArray(R.array.thead_sale_reverse);
        } else {
            saleTitles = context.getResources().getStringArray(R.array.thead_sale);
        }

        List<String> titles = new ArrayList<>();
        for(String title: saleTitles) {
            if (title.equals(context.getResources().getString(R.string.discount))
                && !showDiscount.equals(DiabloEnum.DIABLO_CONFIG_YES)) {
                continue;
            }
            else if (title.equals(context.getResources().getString(R.string.price_type))
                && !showPriceType.equals(DiabloEnum.DIABLO_CONFIG_YES)) {
                continue;
            }

            titles.add(title);
        }

        // String [] heads = context.getResources().getStringArray(R.array.thead_sale);
        String [] heads = titles.toArray(new String[titles.size()]);
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
                label.setLabelId(stock);
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
                    DiabloEnum.DIABLO_EDIT,
                    R.color.black,
                    16,
                    Gravity.CENTER_VERTICAL,
                    InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS,
                    true,
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


    public interface OnGetSaleNewFormSeverListener {
        void afterGet(final GetSaleNewResponse response);
    }

    public static void getSaleNewInfoFormServer(final Context context, String rsn, final OnGetSaleNewFormSeverListener listener) {
        WSaleInterface face = WSaleClient.getClient().create(WSaleInterface.class);
        Call<GetSaleNewResponse> call = face.getSale(Profile.instance().getToken(), rsn);

        call.enqueue(new Callback<GetSaleNewResponse>() {
            @Override
            public void onResponse(Call<GetSaleNewResponse> call, Response<GetSaleNewResponse> response) {
                GetSaleNewResponse news = response.body();
                if (DiabloEnum.SUCCESS.equals(news.getCode())) {
                    listener.afterGet(news);
                }
//                mRSNId = news.getSaleCalc().getId();
//                recoverFromResponse(news.getSaleCalc(), news.getSaleNotes());
            }

            @Override
            public void onFailure(Call<GetSaleNewResponse> call, Throwable t) {
                DiabloUtils.instance()
                    .makeToast(context, DiabloError.getError(99), Toast.LENGTH_LONG);
            }
        });
    }

    public interface OnGetSaleNoteDetailFormSeverListener {
        void afterGet(final SaleNoteDetailResponse response);
    }

    public static void getSaleNoteDetailFromServer(
        final Context context,
        SaleNoteDetailRequest request,
        final OnGetSaleNoteDetailFormSeverListener listener) {

        WSaleInterface face = WSaleClient.getClient().create(WSaleInterface.class);
        Call<SaleNoteDetailResponse> call = face.getSaleNoteDetail(Profile.instance().getToken(), request);

        call.enqueue(new Callback<SaleNoteDetailResponse>() {
            @Override
            public void onResponse(Call<SaleNoteDetailResponse> call, Response<SaleNoteDetailResponse> response) {
                SaleNoteDetailResponse noteDetails = response.body();
                if (DiabloEnum.SUCCESS.equals(noteDetails.getCode())) {
                    listener.afterGet(noteDetails);
                }
//                mRSNId = news.getSaleCalc().getId();
//                recoverFromResponse(news.getSaleCalc(), news.getSaleNotes());
            }

            @Override
            public void onFailure(Call<SaleNoteDetailResponse> call, Throwable t) {
                DiabloUtils.instance()
                    .makeToast(context, DiabloError.getError(99), Toast.LENGTH_LONG);
            }
        });
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

    public static SaleStockAmount getSaleStockAmount(final List<SaleStockAmount>amounts, Integer colorId, String size){
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
            // dpd.setPreselectedDate(2017, 2, 1);
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

        /**
         *
         * @param fragment
         * @param preSelectDate format 2017-01-01
         * @param listener
         */
        public static void build(final Fragment fragment, String preSelectDate, final OnDateSetListener listener) {
            CalendarDatePickerDialogFragment dpd = new CalendarDatePickerDialogFragment();
            Integer year = DiabloUtils.instance().toInteger(preSelectDate.substring(0, 4));
            // based 0
            Integer month = DiabloUtils.instance().toInteger(preSelectDate.substring(5, 7)) - 1;
            Integer date = DiabloUtils.instance().toInteger(preSelectDate.substring(8, 10));

            dpd.setPreselectedDate(year, month, date);
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
            void onDateSet(String date, String nextDate);
        }
    }
}
