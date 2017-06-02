package com.diablo.dt.diablo.fragment.report;


import static android.graphics.Typeface.BOLD;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.WReportClient;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.request.report.DailyReportRequest;
import com.diablo.dt.diablo.response.report.DailyReportResponse;
import com.diablo.dt.diablo.rest.WReportInterface;
import com.diablo.dt.diablo.utils.DiabloDatePicker;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportDaily extends Fragment {
    private static final DiabloUtils UTILS = DiabloUtils.instance();
    private String [] mReportHeaders;

    private TableLayout mReportContent;
    private SwipyRefreshLayout mTableSwipe;
    private Dialog mRefreshDialog;
    private DiabloDatePicker mDatePicker;

    private WReportInterface mReportClient;

    private Integer mCurrentPage;
    private Integer mTotalPage;

    public ReportDaily() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReportDaily newInstance(String param1, String param2) {
        ReportDaily fragment = new ReportDaily();
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

        mTableSwipe = (SwipyRefreshLayout) view.findViewById(R.id.t_daily_report_swipe);
        mTableSwipe.setDirection(SwipyRefreshLayoutDirection.BOTH);

        mTableSwipe.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP){
                    if (!mCurrentPage.equals(DiabloEnum.DEFAULT_PAGE)){
                        mCurrentPage--;
                        pageChanged();
                    } else {
                        DiabloUtils.instance().makeToast(
                            getContext(),
                            getContext().getResources().getString(R.string.refresh_top),
                            Toast.LENGTH_SHORT);
                        mTableSwipe.setRefreshing(false);
                    }

                } else if (direction == SwipyRefreshLayoutDirection.BOTTOM){
                    if (mCurrentPage.equals(mTotalPage)) {
                        DiabloUtils.instance().makeToast(
                            getContext(),
                            getContext().getResources().getString(R.string.refresh_bottom),
                            Toast.LENGTH_SHORT);
                        mTableSwipe.setRefreshing(false);
                    } else {
                        mCurrentPage++;
                        pageChanged();
                    }

                }
            }
        });

        TableRow row = new TableRow(getContext());
        for (String title: mReportHeaders){
            if (getResources().getString(R.string.shop).equals(title)) {
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

            else if (getResources().getString(R.string.stock_calculate).equals(title)) {
                lp.width = 180;
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

            else if (getResources().getString(R.string.daily_report_gen_date).equals(title)) {
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
        pageChanged();
        return view;
    }

    private void initFilter(View view) {
        mDatePicker = new DiabloDatePicker(
            ReportDaily.this,
            view.findViewById(R.id.btn_start_date),
            view.findViewById(R.id.btn_end_date),
            (EditText) view.findViewById(R.id.text_start_date),
            (EditText)view.findViewById(R.id.text_end_date),
            UTILS.currentDate());
    }

    private void init() {
        mCurrentPage = DiabloEnum.DEFAULT_PAGE;
        mTotalPage = 0;
    }


    private DailyReportRequest createRequest() {
        final DailyReportRequest request = new DailyReportRequest(mCurrentPage, DiabloEnum.DEFAULT_ITEMS_PER_PAGE);
        request.setStartTime(mDatePicker.startTime());
        request.setEndTime(mDatePicker.endTime());

        return request;
    }

    private void pageChanged() {
        final DailyReportRequest request = createRequest();
        Call<DailyReportResponse> call = mReportClient.filterDailyReport(Profile.instance().getToken(), request);

        call.enqueue(new Callback<DailyReportResponse>() {
            @Override
            public void onResponse(Call<DailyReportResponse> call, Response<DailyReportResponse> response) {
                mTableSwipe.setRefreshing(false);

                DailyReportResponse base = response.body();
                if (0 != base.getTotal()) {
                    if (DiabloEnum.DEFAULT_PAGE.equals(mCurrentPage)) {
                        mTotalPage = UTILS.calcTotalPage(base.getTotal(), request.getCount());
                    }
                }

                List<DailyReportResponse.DailyDetail>  details = base.getDailyDetails();
                Integer orderId = request.getPageStartIndex();
            }

            @Override
            public void onFailure(Call<DailyReportResponse> call, Throwable t) {

            }
        });
    }

}
