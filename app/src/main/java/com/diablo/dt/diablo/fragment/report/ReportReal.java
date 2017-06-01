package com.diablo.dt.diablo.fragment.report;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.request.report.DailyReportRequest;
import com.diablo.dt.diablo.response.report.DailyReportResponse;
import com.diablo.dt.diablo.rest.WReportInterface;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportReal extends Fragment {
    private static final DiabloUtils UTILS = DiabloUtils.instance();
    private String [] mReportHeaders;

    private TableLayout mReportContent;

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
        mReportHeaders = getResources().getStringArray(R.array.thead_daily_report);
        mRefreshDialog = UTILS.createLoadingDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_real, container, false);


        TableRow row = new TableRow(this.getContext());
        for (String title: mReportHeaders){
            TextView cell = new TextView(this.getContext());
            cell.setTypeface(null, Typeface.BOLD);
            cell.setTextColor(Color.BLACK);

            TableRow.LayoutParams lp = new TableRow.LayoutParams(120, ViewGroup.LayoutParams.MATCH_PARENT);
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
            cell.setTextSize(20);
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

        init();
        return view;
    }

    private void init() {
        final List<Integer> shopIds = Profile.instance().getShopIds();
        final DailyReportRequest request = new DailyReportRequest(
            UTILS.currentDate(),
            UTILS.nextDate(),
            shopIds);

        Call<DailyReportResponse> call = mReportClient.dailyReportOfRealTime(
            Profile.instance().getToken(),
            DiabloEnum.DAILY_REPORT_BY_SHOP,
            request);

        call.enqueue(new Callback<DailyReportResponse>() {
            @Override
            public void onResponse(Call<DailyReportResponse> call, Response<DailyReportResponse> response) {
                mRefreshDialog.dismiss();
                DailyReportResponse result = response.body();

                if (DiabloEnum.HTTP_OK.equals(response.code()) && DiabloEnum.SUCCESS.equals(result.getCode())) {
                    TableRow row = null;
                    Integer orderId = 1;
                    int rowCount = mReportContent.getChildCount();
                    for (int j = 1; j < rowCount; j++) {
                        mReportContent.removeViewAt(j);
                    }

                    for (Integer i=0; i<shopIds.size(); i++) {
                        Integer shop = shopIds.get(i);

                        DailyReportResponse.StockSale stockSale = result.getStockSale(shop);
                        DailyReportResponse.StockProfit stockProfit = result.getStockProfit(shop);
                        DailyReportResponse.StockStore stockIn = result.getStockIn(shop);
                        DailyReportResponse.StockStore stockOut = result.getStockOut(shop);
                        DailyReportResponse.StockStore stockReal = result.getStockReal(shop);
                        DailyReportResponse.StockStore stockLast = result.getStockLast(shop);

                        row = new TableRow(getContext());

                        TextView cell = null;
                        for (String title: mReportHeaders){
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(120, ViewGroup.LayoutParams.MATCH_PARENT);
                            if (i == shopIds.size() - 1) {
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
                else {
                    if (DiabloEnum.HTTP_OK.equals(response.code())) {
                        UTILS.setError(getContext(), R.string.nav_report_daily, result.getCode(), result.getError());
                    } else {
                        UTILS.setError(getContext(), R.string.nav_report_daily, response.code());
                    }

                }

            }

            @Override
            public void onFailure(Call<DailyReportResponse> call, Throwable t) {
                mRefreshDialog.dismiss();
                UTILS.setError(getContext(), R.string.nav_report_daily, 99);
            }
        });
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
            case R.id.report_real_refresh:
                mRefreshDialog.show();
                init();
                break;
            default:
                // return super.onOptionsItemSelected(item);
                break;

        }

        return true;
    }

}
