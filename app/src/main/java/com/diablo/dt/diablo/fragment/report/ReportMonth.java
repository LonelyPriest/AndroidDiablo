package com.diablo.dt.diablo.fragment.report;

import static com.diablo.dt.diablo.R.string.shop;

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
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.WReportClient;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.request.report.MonthReportRequest;
import com.diablo.dt.diablo.response.report.MonthReportResponse;
import com.diablo.dt.diablo.rest.WReportInterface;
import com.diablo.dt.diablo.utils.DiabloDatePicker;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportMonth#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportMonth extends Fragment {
    private static final DiabloUtils UTILS = DiabloUtils.instance();
    private String [] mReportHeaders;

    private TableLayout mReportContent;
    private Dialog mRefreshDialog;
    private DiabloDatePicker mDatePicker;

    private WReportInterface mReportClient;

    private List<Integer> mShopIds;


    public ReportMonth() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReportMonth newInstance(String param1, String param2) {
        ReportMonth fragment = new ReportMonth();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
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
        mShopIds = Profile.instance().getShopIds();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_daily, container, false);

        TableRow row = new TableRow(getContext());
        for (String title: mReportHeaders){
            if (getResources().getString(shop).equals(title)
                || getResources().getString(R.string.daily_report_date).equals(title)
                || getResources().getString(R.string.daily_report_gen_date).equals(title)
                || getResources().getString(R.string.stock_calculate).equals(title)) {
                continue;
            }

            TextView cell = new TextView(this.getContext());
            cell.setTypeface(null, Typeface.BOLD);
            cell.setTextColor(Color.BLACK);

            TableRow.LayoutParams lp = new TableRow.LayoutParams(120, TableRow.LayoutParams.MATCH_PARENT);
            // TableRow.LayoutParams lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            lp.setMargins(0, 1, 0, 0);
            if (getResources().getString(R.string.order_id).equals(title)) {
                lp.width = 100;
            }


            else if (getResources().getString(R.string.stock_cost).equals(title)) {
                lp.width = 180;
            }

            else if (getResources().getString(R.string.sell_cost).equals(title)) {
                lp.width = 180;
            }

            else if (getResources().getString(R.string.stock_in_cost).equals(title)) {
                lp.width = 180;
            }

            else if (getResources().getString(R.string.stock_out_cost).equals(title)) {
                lp.width = 180;
            }

            cell.setLayoutParams(lp);
            cell.setText(title);
            cell.setTextSize(18);
            cell.setGravity(Gravity.CENTER);
            cell.setBackgroundResource(R.drawable.table_cell_bg);
            row.addView(cell);
        }

        row.setBackgroundResource(R.drawable.table_row_bg);

        mReportContent = (TableLayout) view.findViewById(R.id.t_daily_report);
        mReportContent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        mReportContent.addView(row);

        initFilter(view);
        start();
        return view;
    }

    private void initFilter(View view) {
        mDatePicker = new DiabloDatePicker(
            ReportMonth.this,
            view.findViewById(R.id.btn_start_date),
            view.findViewById(R.id.btn_end_date),
            (EditText) view.findViewById(R.id.text_start_date),
            (EditText)view.findViewById(R.id.text_end_date),
            UTILS.firstDayOfCurrentMonth());
    }

    private MonthReportRequest createRequest() {
        final MonthReportRequest request = new MonthReportRequest();
        request.setStartTime(mDatePicker.startTime());
        request.setEndTime(mDatePicker.endTime());
        request.setShops(mShopIds);
        return request;
    }

    private void start() {
        final MonthReportRequest request = createRequest();
        Call<MonthReportResponse> call = mReportClient.filterMonthReport(Profile.instance().getToken(), request);

        call.enqueue(new Callback<MonthReportResponse>() {
            @Override
            public void onResponse(Call<MonthReportResponse> call, Response<MonthReportResponse> response) {
                mRefreshDialog.dismiss();

                MonthReportResponse result = response.body();
                if (DiabloEnum.HTTP_OK.equals(response.code()) && DiabloEnum.SUCCESS.equals(result.getCode())) {
                    genReportContent(result);
                }
                else {
                    if (DiabloEnum.HTTP_OK.equals(response.code())) {
                        UTILS.setError(getContext(), R.string.nav_report_month, result.getCode(), result.getError());
                    } else {
                        UTILS.setError(getContext(), R.string.nav_report_month, response.code());
                    }

                }
            }

            @Override
            public void onFailure(Call<MonthReportResponse> call, Throwable t) {
                mRefreshDialog.dismiss();
                UTILS.setError(getContext(), R.string.nav_report_daily, 99);
            }
        });
    }

    private void genReportContent(MonthReportResponse response) {
        mReportContent.removeViews(1, mReportContent.getChildCount() - 1);

        for (Integer i=0; i<mShopIds.size(); i++) {
            Integer shopId = mShopIds.get(i);
            MonthReportResponse.MonthDetail monthDetail = response.getDetail(shopId);
            MonthReportResponse.StockOfDay stockOfDay = response.getStock(shopId);

            TableRow row = new TableRow(getContext());
            row.setBackgroundResource(R.drawable.table_row_bg);
            mReportContent.addView(row);

            for (String title: mReportHeaders){
                if (getResources().getString(shop).equals(title)
                    || getResources().getString(R.string.daily_report_date).equals(title)
                    || getResources().getString(R.string.daily_report_gen_date).equals(title)
                    || getResources().getString(R.string.stock_calculate).equals(title)) {
                    continue;
                }

                TableRow.LayoutParams lp = new TableRow.LayoutParams(120, TableRow.LayoutParams.MATCH_PARENT);
                if (i == mShopIds.size() - 1) {
                    lp.setMargins(0, 1, 0, 1);
                } else {
                    lp.setMargins(0, 1, 0, 0);
                }

                TextView cell = null;
                if (getResources().getString(R.string.order_id).equals(title)) {
                    cell = UTILS.addCell(getContext(), row, i+1, lp);
                }

                else if (getResources().getString(R.string.stock).equals(title)) {
                    cell = UTILS.addCell(getContext(), row, stockOfDay.getStockCalc(), lp);
                }
                else if (getResources().getString(R.string.stock_cost).equals(title)) {
                    cell = UTILS.addCell(getContext(), row, stockOfDay.getStockCost(), lp);
                }

                else if (getResources().getString(R.string.stock_sell).equals(title)) {
                    cell = UTILS.addCell(getContext(), row, monthDetail.getSell(), lp);
                }
                else if (getResources().getString(R.string.sell_cost).equals(title)) {
                    cell = UTILS.addCell(getContext(), row, monthDetail.getSellCost(), lp);
                }
                else if (getResources().getString(R.string.turnover).equals(title)) {
                    cell = UTILS.addCell(getContext(), row, monthDetail.getShouldPay(), lp);
                }
                else if (getResources().getString(R.string.paid).equals(title)) {
                    cell = UTILS.addCell(getContext(), row, monthDetail.getHasPay(), lp);
                }

                else if (getResources().getString(R.string.cash).equals(title)) {
                    cell = UTILS.addCell(getContext(), row, monthDetail.getCash(), lp);
                }
                else if (getResources().getString(R.string.card).equals(title)) {
                    cell = UTILS.addCell(getContext(), row, monthDetail.getCard(), lp);
                }
                else if (getResources().getString(R.string.wire).equals(title)) {
                    cell = UTILS.addCell(getContext(), row, monthDetail.getWire(), lp);
                }
                else if (getResources().getString(R.string.verificate).equals(title)) {
                    cell = UTILS.addCell(getContext(), row, monthDetail.getVerificate(), lp);
                }

                else if (getResources().getString(R.string.stock_in).equals(title)) {
                    cell = UTILS.addCell(getContext(), row, monthDetail.getStockIn(), lp);
                }
                else if (getResources().getString(R.string.stock_in_cost).equals(title)) {
                    cell = UTILS.addCell(getContext(), row, monthDetail.getStockInCost(), lp);
                }
                else if (getResources().getString(R.string.stock_out).equals(title)) {
                    cell = UTILS.addCell(getContext(), row, monthDetail.getStockOut(), lp);
                }
                else if (getResources().getString(R.string.stock_out_cost).equals(title)) {
                    cell = UTILS.addCell(getContext(), row, monthDetail.getStockOutCost(), lp);
                }

                if (null != cell ) {
                    cell.setGravity(Gravity.CENTER);
                    cell.setBackgroundResource(R.drawable.table_cell_bg);
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();

        inflater.inflate(R.menu.action_on_month_report, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.report_month_refresh: {
                mRefreshDialog.show();
                start();
                break;
            }
            default:
                // return super.onOptionsItemSelected(item);
                break;

        }

        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UTILS.hiddenKeyboard(getContext(), getView());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            UTILS.hiddenKeyboard(getContext(), getView());
        }
    }

}
