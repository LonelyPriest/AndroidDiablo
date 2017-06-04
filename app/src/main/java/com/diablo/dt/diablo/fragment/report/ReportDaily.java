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


    /**
     * statistic
     */
    Integer mTotal;

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
            UTILS.firstDayOfCurrentMonth());
    }

    private void init() {
        mCurrentPage = DiabloEnum.DEFAULT_PAGE;
        
        mTotal = 0;
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
                mRefreshDialog.dismiss();

                // remove cell except title
                mReportContent.removeViews(1, mReportContent.getChildCount() - 1);
//                int rowCount = mReportContent.getChildCount();
//                for (int j = 1; j < rowCount; j++) {
//                    mReportContent.removeViewAt(j);
//                }

                DailyReportResponse base = response.body();
                if (0 != base.getTotal()) {
                    if (DiabloEnum.DEFAULT_PAGE.equals(mCurrentPage)) {
                        mTotal = base.getTotal();
                        mSell = base.getSell();
                        mSellCost = base.getSellCost();

                        mShouldPay = base.getShouldPay();
                        mHasPay = base.getHasPay();

                        mCash = base.getCash();
                        mCard = base.getCard();
                        mWire = base.getWire();
                        mVerificate = base.getVerificate();

                        mStockIn = base.getStockIn();
                        mStockInCost = base.getStockInCost();
                        mStockOut = base.getStockOut();
                        mStockOutCost = base.getStockOutCost();

                        mTotalPage = UTILS.calcTotalPage(mTotal, request.getCount());
                    }
                }

                List<DailyReportResponse.DailyDetail>  details = base.getDailyDetails();
                Integer orderId = request.getPageStartIndex();


                for (int i = 0; i<details.size(); i++) {
                    TableRow row = new TableRow(getContext());
                    DailyReportResponse.DailyDetail d = details.get(i);

                    for (String title: mReportHeaders) {
                        if (getResources().getString(R.string.shop).equals(title)) {
                            continue;
                        }

                        TableRow.LayoutParams lp = new TableRow.LayoutParams(120, TableRow.LayoutParams.MATCH_PARENT);
                        if (i == details.size() - 1) {
                            lp.setMargins(0, 1, 0, 1);
                        } else {
                            lp.setMargins(0, 1, 0, 0);
                        }

                        TextView cell = null;
                        if (getResources().getString(R.string.order_id).equals(title)) {
                            lp.width = 100;
                            cell = UTILS.addCell(getContext(), row, orderId++, lp);
                        }

                        else if (getResources().getString(R.string.daily_report_date).equals(title)) {
                            cell = UTILS.addCell(getContext(), row, d.getDay(), lp);
                        }
                        else if (getResources().getString(R.string.stock).equals(title)) {
                            cell = UTILS.addCell(getContext(), row, d.getStock(), lp);
                        }
                        else if (getResources().getString(R.string.stock_calculate).equals(title)) {
                            cell = UTILS.addCell(getContext(), row, d.getStockCalc(), lp);
                        }
                        else if (getResources().getString(R.string.stock_cost).equals(title)) {
                            cell = UTILS.addCell(getContext(), row, d.getStockCost(), lp);
                        }

                        else if (getResources().getString(R.string.stock_sell).equals(title)) {
                            cell = UTILS.addCell(getContext(), row, d.getSell(), lp);
                        }
                        else if (getResources().getString(R.string.sell_cost).equals(title)) {
                            cell = UTILS.addCell(getContext(), row, d.getSellCost(), lp);
                        }
                        else if (getResources().getString(R.string.turnover).equals(title)) {
                            cell = UTILS.addCell(getContext(), row, d.getShouldPay(), lp);
                        }
                        else if (getResources().getString(R.string.paid).equals(title)) {
                            cell = UTILS.addCell(getContext(), row, d.getHasPay(), lp);
                        }

                        else if (getResources().getString(R.string.cash).equals(title)) {
                            cell = UTILS.addCell(getContext(), row, d.getCash(), lp);
                        }
                        else if (getResources().getString(R.string.card).equals(title)) {
                            cell = UTILS.addCell(getContext(), row, d.getCard(), lp);
                        }
                        else if (getResources().getString(R.string.wire).equals(title)) {
                            cell = UTILS.addCell(getContext(), row, d.getWire(), lp);
                        }
                        else if (getResources().getString(R.string.verificate).equals(title)) {
                            cell = UTILS.addCell(getContext(), row, d.getVerificate(), lp);
                        }

                        else if (getResources().getString(R.string.stock_in).equals(title)) {
                            cell = UTILS.addCell(getContext(), row, d.getStockIn(), lp);
                        }
                        else if (getResources().getString(R.string.stock_in_cost).equals(title)) {
                            cell = UTILS.addCell(getContext(), row, d.getStockInCost(), lp);
                        }
                        else if (getResources().getString(R.string.stock_out).equals(title)) {
                            cell = UTILS.addCell(getContext(), row, d.getStockOut(), lp);
                        }
                        else if (getResources().getString(R.string.stock_out_cost).equals(title)) {
                            cell = UTILS.addCell(getContext(), row, d.getStockOutCost(), lp);
                        }

                        else if (getResources().getString(R.string.daily_report_gen_date).equals(title)) {
                            cell = UTILS.addCell(getContext(), row, d.getGenDate(), lp);
                        }

                        if (null != cell ) {
                            cell.setGravity(Gravity.CENTER);
                            cell.setBackgroundResource(R.drawable.table_cell_bg);
                        }

                    }

                    row.setBackgroundResource(R.drawable.table_row_bg);
                    mReportContent.addView(row);
                }

                if (0 < mTotalPage ) {
                    TableRow row = new TableRow(getContext());
                    row.setBackgroundResource(R.drawable.table_row_last_bg);
                    mReportContent.addView(row);

                    // cell
                    TableRow.LayoutParams lp = new TableRow.LayoutParams();
                    lp.span = 5;
                    lp.setMargins(0, 1, 0, 1);
                    // addCell(row, mStatistic, lp);

                    String pageInfo = getResources().getString(R.string.current_page) + mCurrentPage.toString()
                        + getResources().getString(R.string.page)
                        + getResources().getString(R.string.space_4)
                        + getResources().getString(R.string.total_page) + mTotalPage.toString()
                        + getResources().getString(R.string.page);

                    TextView cell = UTILS.addCell(getContext(), row, pageInfo, lp);
                    cell.setBackgroundResource(R.drawable.table_cell_bg);
                    UTILS.formatPageInfo(cell);
                    cell.setGravity(Gravity.START);

                    if ( 1 < mTotal) {
                        for (String title: mReportHeaders) {
                            TableRow.LayoutParams lp2 = new TableRow.LayoutParams();
                            lp2.setMargins(0, 1, 0, 1);
                            if (getResources().getString(R.string.stock_sell).equals(title)) {
                                cell = UTILS.addCell(getContext(), row, mSell, lp2);
                            } else if (getResources().getString(R.string.sell_cost).equals(title)) {
                                cell = UTILS.addCell(getContext(), row, mSellCost, lp2);
                            } else if (getResources().getString(R.string.turnover).equals(title)) {
                                cell = UTILS.addCell(getContext(), row, mShouldPay, lp2);
                            } else if (getResources().getString(R.string.paid).equals(title)) {
                                cell = UTILS.addCell(getContext(), row, mHasPay, lp2);
                            } else if (getResources().getString(R.string.cash).equals(title)) {
                                cell = UTILS.addCell(getContext(), row, mCash, lp2);
                            } else if (getResources().getString(R.string.card).equals(title)) {
                                cell = UTILS.addCell(getContext(), row, mCard, lp2);
                            } else if (getResources().getString(R.string.wire).equals(title)) {
                                cell = UTILS.addCell(getContext(), row, mWire, lp2);
                            } else if (getResources().getString(R.string.verificate).equals(title)) {
                                cell = UTILS.addCell(getContext(), row, mVerificate, lp2);
                            } else if (getResources().getString(R.string.stock_in).equals(title)) {
                                cell = UTILS.addCell(getContext(), row, mStockIn, lp2);
                            } else if (getResources().getString(R.string.stock_in_cost).equals(title)) {
                                cell = UTILS.addCell(getContext(), row, mStockInCost, lp2);
                            } else if (getResources().getString(R.string.stock_out).equals(title)) {
                                cell = UTILS.addCell(getContext(), row, mStockOut, lp2);
                            } else if (getResources().getString(R.string.stock_out_cost).equals(title)) {
                                cell = UTILS.addCell(getContext(), row, mStockOutCost, lp2);
                            }

                            cell.setBackgroundResource(R.drawable.table_cell_bg);
                            cell.setGravity(Gravity.CENTER);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DailyReportResponse> call, Throwable t) {
                mTableSwipe.setRefreshing(false);
                mRefreshDialog.dismiss();
                UTILS.setError(getContext(), R.string.nav_report_daily, 99);
            }
        });
    }

    private void synDailyReport() {
        final DailyReportRequest.Condition request = new DailyReportRequest.Condition(mDatePicker.startTime(), mDatePicker.endTime());
        Call<com.diablo.dt.diablo.response.Response> call = mReportClient.synDailyReport(Profile.instance().getToken(), request);
        call.enqueue(new Callback<com.diablo.dt.diablo.response.Response>() {
            @Override
            public void onResponse(Call<com.diablo.dt.diablo.response.Response> call, Response<com.diablo.dt.diablo.response.Response> response) {
                mRefreshDialog.dismiss();
                if (DiabloEnum.HTTP_OK.equals(response.code())) {
                    com.diablo.dt.diablo.response.Response result = response.body();
                    if (DiabloEnum.SUCCESS.equals(result.getCode())) {
                        UTILS.makeToast(getContext(), R.string.syn_daily_report_success);
                    } else {
                        UTILS.setError(getContext(), R.string.nav_report_daily, result.getCode(), result.getError());
                    }
                }
                else {
                    UTILS.setError(getContext(), R.string.nav_report_daily, response.code());
                }
            }

            @Override
            public void onFailure(Call<com.diablo.dt.diablo.response.Response> call, Throwable t) {
                mRefreshDialog.dismiss();
                UTILS.setError(getContext(), R.string.nav_report_daily, 99);
            }
        });
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
            case R.id.report_refresh: {
                mRefreshDialog.show();
                init();
                pageChanged();
                break;
            }
            case R.id.report_syn: {
                mRefreshDialog.show();
                synDailyReport();
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
