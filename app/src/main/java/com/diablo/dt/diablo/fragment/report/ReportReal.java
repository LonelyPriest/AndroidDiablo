package com.diablo.dt.diablo.fragment.report;


import static android.graphics.Typeface.BOLD;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.WReportClient;
import com.diablo.dt.diablo.entity.DiabloBrand;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.request.report.DailyReportRealRequest;
import com.diablo.dt.diablo.response.report.DailyReportRealResponse;
import com.diablo.dt.diablo.response.report.DailyReportSaleDetailResponse;
import com.diablo.dt.diablo.rest.WReportInterface;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportReal extends Fragment {
    private static final DiabloUtils UTILS = DiabloUtils.instance();
    private String [] mReportHeaders;
    private String [] mReportSaleHeader;
    private String mCurrentDate;
    private String mNextDate;

    private TableLayout mReportContent;
    private TableLayout mReportSaleContent;
    private TextView mReportDate;

    private Dialog mRefreshDialog;

    private WReportInterface mReportClient;

    public ReportReal() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReportReal newInstance(String param1, String param2) {
        ReportReal fragment = new ReportReal();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mReportClient = WReportClient.getClient().create(WReportInterface.class);
        mReportHeaders = getResources().getStringArray(R.array.thead_daily_report_real);
        mReportSaleHeader = getResources().getStringArray(R.array.thead_daily_sale_report);
        mRefreshDialog = UTILS.createLoadingDialog(getContext());

        mCurrentDate = UTILS.currentDate();
        mNextDate = UTILS.nextDate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_real, container, false);
        TableRow row = new TableRow(this.getContext());

        for (String title: mReportHeaders){
            TextView cell = new TextView(this.getContext());
            cell.setTypeface(null, BOLD);
            cell.setTextColor(Color.BLACK);

            TableRow.LayoutParams lp = new TableRow.LayoutParams(120, TableRow.LayoutParams.MATCH_PARENT);
            // TableRow.LayoutParams lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            lp.setMargins(0, 1, 0, 0);
            if (getResources().getString(R.string.order_id).equals(title)) {
                lp.width = 100;
            }

            else if (getResources().getString(R.string.stock_last).equals(title)) {
                lp.width = 180;
            }

            else if (getResources().getString(R.string.stock_current).equals(title)) {
                lp.width = 180;
            }

            else if (getResources().getString(R.string.sell_cost).equals(title)) {
                lp.width = 180;
            }

            else if (getResources().getString(R.string.turnover).equals(title)) {
                lp.width = 160;
            }

            cell.setLayoutParams(lp);
            cell.setText(title);
            cell.setTextSize(18);
            cell.setGravity(Gravity.CENTER);
            cell.setBackgroundResource(R.drawable.table_cell_bg);
            row.addView(cell);
        }

        row.setBackgroundResource(R.drawable.table_row_bg);
//        TableLayout head = (TableLayout) view.findViewById(R.id.t_daily_report_real_head);
//        head.addView(row);

        mReportContent = (TableLayout) view.findViewById(R.id.t_daily_report_real);
        mReportContent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        mReportContent.addView(row);

        /**
         * sale details
         */
        row = new TableRow(this.getContext());
        row.setBackgroundResource(R.drawable.table_row_bg);
        row.setBackgroundResource(R.drawable.table_row_last_bg);
        for (String title: mReportSaleHeader){
            TextView cell = new TextView(this.getContext());
            cell.setTypeface(null, BOLD);
            cell.setTextColor(Color.BLACK);

            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);
            lp.setMargins(0, 1, 0, 1);

            if (getResources().getString(R.string.order_id).equals(title)) {
                lp.weight = 0.5f;
            }

            cell.setLayoutParams(lp);
            cell.setText(title);
            cell.setTextSize(18);
            cell.setGravity(Gravity.CENTER);
            cell.setBackgroundResource(R.drawable.table_cell_bg);
            row.addView(cell);
        }

        TableLayout head = (TableLayout) view.findViewById(R.id.t_daily_report_detail_real_head);
        head.addView(row);

        mReportSaleContent = (TableLayout) view.findViewById(R.id.t_daily_report_detail_real);
        mReportDate = (TextView) view.findViewById(R.id.report_datetime);

        init();
        return view;
    }

//    private void initTitle() {
//        String title = getResources().getString(R.string.nav_report_real);
//        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        if (null != bar) {
//            bar.setTitle(title);
//        }
//    }

    private void init() {
        mReportDate.setText(mCurrentDate);

        final List<Integer> shopIds = Profile.instance().getShopIds();
        DailyReportRealRequest request = new DailyReportRealRequest(
            mCurrentDate,
            mNextDate,
            shopIds);

        Call<DailyReportRealResponse> call = mReportClient.dailyReportOfRealTime(
            Profile.instance().getToken(),
            DiabloEnum.DAILY_REPORT_BY_SHOP,
            request);

        call.enqueue(new Callback<DailyReportRealResponse>() {
            @Override
            public void onResponse(Call<DailyReportRealResponse> call, Response<DailyReportRealResponse> response) {
                mRefreshDialog.dismiss();
                DailyReportRealResponse result = response.body();

                if (DiabloEnum.HTTP_OK.equals(response.code()) && DiabloEnum.SUCCESS.equals(result.getCode())) {
                    genReportContent(shopIds, result);
                }
                else {
                    if (DiabloEnum.HTTP_OK.equals(response.code())) {
                        UTILS.setError(getContext(), R.string.nav_report_daily, result.getCode(), result.getError());
                    } else {
                        UTILS.setError(getContext(), R.string.nav_report_daily, response.code());
                    }

                }

            }

            @Override
            public void onFailure(Call<DailyReportRealResponse> call, Throwable t) {
                mRefreshDialog.dismiss();
                UTILS.setError(getContext(), R.string.nav_report_daily, 99);
            }
        });

        Call<DailyReportSaleDetailResponse> call2 = mReportClient.dailyReportSaleDetailOfRealTime(
            Profile.instance().getToken(),
            DiabloEnum.DAILY_REPORT_BY_GOOD,
            request);
        call2.enqueue(new Callback<DailyReportSaleDetailResponse>() {
            @Override
            public void onResponse(Call<DailyReportSaleDetailResponse> call, Response<DailyReportSaleDetailResponse> response) {
                mRefreshDialog.dismiss();
                DailyReportSaleDetailResponse result = response.body();

                if (DiabloEnum.HTTP_OK.equals(response.code()) && DiabloEnum.SUCCESS.equals(result.getCode())) {
                    genReportSaleDetailContent(result);
                }
                else {
                    if (DiabloEnum.HTTP_OK.equals(response.code())) {
                        UTILS.setError(getContext(), R.string.nav_report_daily, result.getCode(), result.getError());
                    } else {
                        UTILS.setError(getContext(), R.string.nav_report_daily, response.code());
                    }
                }

            }

            @Override
            public void onFailure(Call<DailyReportSaleDetailResponse> call, Throwable t) {
                mRefreshDialog.dismiss();
                UTILS.setError(getContext(), R.string.nav_report_daily, 99);
            }
        });
    }

    private void genReportContent(List<Integer> shops, DailyReportRealResponse response) {
        TableRow row = null;
        Integer orderId = 1;

        // remove all views except title
        mReportContent.removeViews(1, mReportContent.getChildCount() - 1);

        for (Integer i=0; i<shops.size(); i++) {
            Integer shop = shops.get(i);

            DailyReportRealResponse.StockSale stockSale = response.getStockSale(shop);
            DailyReportRealResponse.StockProfit stockProfit = response.getStockProfit(shop);
            DailyReportRealResponse.StockStore stockIn = response.getStockIn(shop);
            DailyReportRealResponse.StockStore stockOut = response.getStockOut(shop);
            DailyReportRealResponse.StockStore stockReal = response.getStockReal(shop);
            DailyReportRealResponse.StockStore stockLast = response.getStockLast(shop);

            row = new TableRow(getContext());
            TextView cell = null;

            for (String title: mReportHeaders){
                TableRow.LayoutParams lp = new TableRow.LayoutParams(120, TableRow.LayoutParams.MATCH_PARENT);
                if (i == shops.size() - 1) {
                    lp.setMargins(0, 1, 0, 1);
                } else {
                    lp.setMargins(0, 1, 0, 0);
                }

                if (getResources().getString(R.string.order_id).equals(title)) {
                    lp.width = 100;
                    cell = UTILS.addCell(getContext(), row, orderId++, lp);
                }
                else if(getString(R.string.shop).equals(title)){
                    cell = UTILS.addCell(
                        getContext(),
                        row,
                        UTILS.getShop(Profile.instance().getSortShop(), shop).getName(),
                        lp);
                }
                else if (getResources().getString(R.string.stock_last).equals(title)) {
                    lp.width = 180;
                    cell = UTILS.addCell(
                        getContext(),
                        row,
                        null == stockLast ? 0 : stockLast.getTotal(),
                        lp);
                }
                else if (getResources().getString(R.string.stock_current).equals(title)) {
                    lp.width = 180;
                    cell = UTILS.addCell(
                        getContext(),
                        row,
                        null == stockReal ? 0 : stockReal.getTotal(),
                        lp);
                }
                else if (getResources().getString(R.string.stock_in).equals(title)) {
                    cell = UTILS.addCell(
                        getContext(),
                        row,
                        null == stockIn ? 0 : stockIn.getTotal(),
                        lp);
                }
                else if (getResources().getString(R.string.stock_out).equals(title)) {
                    cell = UTILS.addCell(
                        getContext(),
                        row,
                        null == stockOut ? 0 : stockOut.getTotal(),
                        lp);
                }
                else if (getResources().getString(R.string.stock_sell).equals(title)) {
                    cell = UTILS.addCell(
                        getContext(),
                        row,
                        null == stockSale ? 0 : stockSale.getTotal(),
                        lp);
                }
                else if (getResources().getString(R.string.turnover).equals(title)) {
                    lp.width = 160;
                    cell = UTILS.addCell(
                        getContext(),
                        row,
                        null == stockSale ? 0 : stockSale.getShouldPay(),
                        lp);
                }
                else if (getResources().getString(R.string.paid).equals(title)) {
                    cell = UTILS.addCell(
                        getContext(),
                        row,
                        null == stockSale ? 0 : stockSale.getHasPay(),
                        lp);
                }
                else if (getResources().getString(R.string.cash).equals(title)) {
                    cell = UTILS.addCell(
                        getContext(),
                        row,
                        null == stockSale ? 0 : stockSale.getCash(),
                        lp);
                }
                else if (getResources().getString(R.string.card).equals(title)) {
                    cell = UTILS.addCell(
                        getContext(),
                        row,
                        null == stockSale ? 0 : stockSale.getCard(),
                        lp);
                }
                else if (getResources().getString(R.string.wire).equals(title)) {
                    cell = UTILS.addCell(
                        getContext(),
                        row,
                        null == stockSale ? 0 : stockSale.getWire(),
                        lp);
                }
                else if (getResources().getString(R.string.verificate).equals(title)) {
                    cell = UTILS.addCell(
                        getContext(),
                        row,
                        null == stockSale ? 0 : stockSale.getVerificate(),
                        lp);
                }
                else if (getResources().getString(R.string.sell_cost).equals(title)) {
                    lp.width = 180;
                    cell = UTILS.addCell(
                        getContext(),
                        row,
                        null == stockProfit ? 0 : stockProfit.getCost(),
                        lp);
                }
                else if (getResources().getString(R.string.gross_profit).equals(title)) {
                    cell = UTILS.addCell(
                        getContext(),
                        row,
                        null == stockProfit ? 0 : stockProfit.getBalance() / 100 - stockProfit.getCost(),
                        lp);
                }
                else if (getResources().getString(R.string.gross_margin).equals(title)) {
                    Float margin = 0f;
                    if (null != stockProfit) {
                        margin = UTILS.calcGrossMargin(stockProfit.getBalance() / 100, stockProfit.getCost());
                    }
                    cell = UTILS.addCell(
                        getContext(),
                        row,
                        margin,
                        lp);
                }

                if (null != cell ) {
                    cell.setGravity(Gravity.CENTER);
                    cell.setBackgroundResource(R.drawable.table_cell_bg);
                }
            }

            row.setBackgroundResource(R.drawable.table_row_bg);
            mReportContent.addView(row);
        }

        if (null != row) {
            row.setBackgroundResource(R.drawable.table_row_last_bg);
        }
    }

    private void genReportSaleDetailContent( DailyReportSaleDetailResponse response) {
        List<DailyReportSaleDetailResponse.ReportSaleDetail> details = response.getSaleDetails();
        SparseArray< List<DailyReportSaleDetailResponse.ReportSaleDetail> > firmDetails = new SparseArray<>();
        mReportSaleContent.removeAllViews();

        for (DailyReportSaleDetailResponse.ReportSaleDetail d: details) {
            if (null == firmDetails.get(d.getFirm())) {
                List<DailyReportSaleDetailResponse.ReportSaleDetail> sparseDetails = new ArrayList<>();
                sparseDetails.add(d);
                firmDetails.put(d.getFirm(), sparseDetails);
            }
            else {
                List<DailyReportSaleDetailResponse.ReportSaleDetail> sparseDetails = firmDetails.get(d.getFirm());
                sparseDetails.add(d);
                firmDetails.setValueAt(firmDetails.indexOfKey(d.getFirm()), sparseDetails);
            }
        }

        for (int i=0; i<firmDetails.size(); i++) {
            Integer key = firmDetails.keyAt(i);
            // firm
            TableRow row = new TableRow(getContext());
            row.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.menu_btn_yellow));
            // row.setBackgroundResource(R.drawable.table_row_bg);
            mReportSaleContent.addView(row);

            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);
            TextView firmView = UTILS.addCell(getContext(), row, Profile.instance().getFirm(key).getName(), lp);
            firmView.setGravity(Gravity.CENTER);
            firmView.setTextSize(18);
            firmView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            firmView.setTypeface(null, Typeface.BOLD);

            List<DailyReportSaleDetailResponse.ReportSaleDetail> sparseDetails = firmDetails.get(key);
            Integer orderId = 1;

            TableRow row2 = null;

            for (int j=0; j<sparseDetails.size(); j++) {
                DailyReportSaleDetailResponse.ReportSaleDetail detail = sparseDetails.get(j);
                row2 = new TableRow(getContext());
                mReportSaleContent.addView(row2);
                TextView cell = null;

                for (String title: mReportSaleHeader) {
                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);
                    if (j == sparseDetails.size() - 1) {
                        lp2.setMargins(0, 1, 0, 1);
                    } else {
                        lp2.setMargins(0, 1, 0, 0);
                    }

                    if (getResources().getString(R.string.order_id).equals(title)) {
                        lp2.weight = 0.5f;
                        cell = UTILS.addCell(getContext(), row2, orderId++, lp2);
                    }
                    else if(getString(R.string.shop).equals(title)){
                        cell = UTILS.addCell(
                            getContext(),
                            row2,
                            UTILS.getShop(Profile.instance().getSortShop(), detail.getShop()).getName(),
                            lp2);
                    }
                    else if (getString(R.string.style_number).equals(title)) {
                        cell = UTILS.addCell(getContext(), row2, detail.getStyleNumber(), lp2);
                    }
                    else if (getString(R.string.brand).equals(title)) {
                        DiabloBrand brand = Profile.instance().getBrand(detail.getBrand());
                        cell = UTILS.addCell(
                            getContext(),
                            row2,
                            null == brand ? DiabloEnum.EMPTY_STRING : brand.getName(),
                            lp2);
                    }
                    else if (getString(R.string.amount).equals(title)) {
                        cell = UTILS.addCell(getContext(), row2, detail.getTotal(), lp2);
                    }

                    if (null != cell ) {
                        cell.setGravity(Gravity.CENTER);
                        cell.setBackgroundResource(R.drawable.table_cell_bg);
                    }
                }

                row2.setBackgroundResource(R.drawable.table_row_bg);
            }

            if (null != row2) {
                row2.setBackgroundResource(R.drawable.table_row_last_bg);
            }
        }

        // sort by firm

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();

        inflater.inflate(R.menu.action_on_daily_report_real, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.report_real_before_date: {
                mNextDate = mCurrentDate;

                String[] d = mCurrentDate.split(DiabloEnum.DATE_SEPARATOR);
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(UTILS.toInteger(d[0]), UTILS.toInteger(d[1]) - 1, UTILS.toInteger(d[2]));
                calendar.add(Calendar.DATE, -1);
                mCurrentDate = DiabloUtils.mDateFormat.format(calendar.getTime()).trim();

                mRefreshDialog.show();
                init();
                break;
            }
            case R.id.report_real_next_date: {
                String[] d = mCurrentDate.split(DiabloEnum.DATE_SEPARATOR);
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(UTILS.toInteger(d[0]), UTILS.toInteger(d[1]) - 1, UTILS.toInteger(d[2]));
                calendar.add(Calendar.DATE, 1);

                if (calendar.before(Calendar.getInstance())) {
                    mCurrentDate = DiabloUtils.mDateFormat.format(calendar.getTime()).trim();
                    calendar.add(Calendar.DATE, 1);
                    mNextDate = DiabloUtils.mDateFormat.format(calendar.getTime()).trim();
                    mRefreshDialog.show();
                    init();
                }

                break;
            }
            case R.id.report_real_refresh: {
                mCurrentDate = UTILS.currentDate();
                mNextDate = UTILS.nextDate();
                mRefreshDialog.show();
                init();
                break;
            }
            default:
                // return super.onOptionsItemSelected(item);
                break;

        }

        return true;
    }

}
