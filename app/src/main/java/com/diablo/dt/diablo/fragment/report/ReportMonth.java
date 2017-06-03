package com.diablo.dt.diablo.fragment.report;


import static android.graphics.Typeface.BOLD;

import android.app.Dialog;
import android.graphics.Color;
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
import com.diablo.dt.diablo.request.report.DailyReportRequest;
import com.diablo.dt.diablo.rest.WReportInterface;
import com.diablo.dt.diablo.utils.DiabloDatePicker;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportMonth#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportMonth extends Fragment {
    private static final DiabloUtils UTILS = DiabloUtils.instance();
    private String [] mReportHeaders;

    private TableLayout mReportContent;
    private SwipyRefreshLayout mTableSwipe;
    private Dialog mRefreshDialog;
    private DiabloDatePicker mDatePicker;

    private WReportInterface mReportClient;

    private Integer mCurrentPage;
    private Integer mTotalPage;


    /**
     * statistic
     */
    Integer mTotal;

    Integer mStock;

    Integer mSell;
    Float mSellCost;

    Float mShouldPay;
    Float mHasPay;

    Float mCash;
    Float mCard;
    Float mWire;
    Float mVerificate;

    Integer mStockIn;
    Float mStockInCost;
    Integer mStockOut;
    Float mStockOutCost;


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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_daily, container, false);

        TableRow row = new TableRow(getContext());
        for (String title: mReportHeaders){
            if (getResources().getString(R.string.shop).equals(title)
                || getResources().getString(R.string.daily_report_date).equals(title)
                || getResources().getString(R.string.daily_report_gen_date).equals(title)
                || getResources().getString(R.string.stock_calculate).equals(title)) {
                continue;
            }

            TextView cell = new TextView(this.getContext());
            cell.setTypeface(null, BOLD);
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
        init();
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

    private void init() {
        mStock = 0;

        mSell = 0;
        mSellCost = 0f;

        mShouldPay = 0f;
        mHasPay = 0f;

        mCash = 0f;
        mCard = 0f;
        mWire = 0f;
        mVerificate = 0f;

        mStockIn = 0;
        mStockInCost = 0f;
        mStockOut = 0;
        mStockOutCost = 0f;
    }


    private DailyReportRequest.Condition createRequest() {
        final DailyReportRequest.Condition request = new DailyReportRequest.Condition();
        request.setStartTime(mDatePicker.startTime());
        request.setEndTime(mDatePicker.endTime());

        return request;
    }

    private void start() {
        final DailyReportRequest.Condition request = createRequest();
        // Call<DailyReportResponse> call = mReportClient.filterDailyReport(Profile.instance().getToken(), request);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();

        inflater.inflate(R.menu.action_on_daily_report, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.report_month_refresh: {
//                mRefreshDialog.show();
//                init();
//                start();
//                break;
//            }
            default:
                // return super.onOptionsItemSelected(item);
                break;

        }

        return true;
    }

}
