package com.diablo.dt.diablo.fragment.firm;


import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.diablo.dt.diablo.client.StockClient;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.sale.SaleUtils;
import com.diablo.dt.diablo.request.stock.StockDetailRequest;
import com.diablo.dt.diablo.response.stock.StockDetailResponse;
import com.diablo.dt.diablo.rest.StockInterface;
import com.diablo.dt.diablo.utils.DiabloDatePicker;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirmStockCheck#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirmStockCheck extends Fragment {
    private final static DiabloUtils UTILS = DiabloUtils.instance();
    private String [] mTableHeads;
    private String[]  mStockTypes;

    private String mStatistic;

    /*
    * rest request
    * */
    private StockInterface mStockRest;

    /*
    * row of table
    * */
    private TableLayout mStockDetailTable;
    private SwipyRefreshLayout mStockDetailTableSwipe;
    private Dialog mRefreshDialog;

    private TableRow mCurrentSelectedRow;

    private Integer mCurrentPage;
    private Integer mTotalPage;

    private DiabloDatePicker mDatePicker;


    private Integer mStartFirm;
    private View mViewFragment;

    public FirmStockCheck() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FirmStockCheck newInstance(String param1, String param2) {
        return new FirmStockCheck();
    }

    public void setStartFirm(Integer firm) {
        this.mStartFirm = firm;
    }

    public void init() {
        initTitle();
        mCurrentPage = DiabloEnum.DEFAULT_PAGE;
        mTotalPage = 0;
    }

    private void initTitle() {
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (null != bar) {
            Firm f = Profile.instance().getFirm(mStartFirm);
            bar.setTitle((getResources().getString(R.string.check_trans))
                + "(" + (null == f ? DiabloEnum.EMPTY_STRING : f.getName()) + ")");
        }
    }

    private void initPickerDate() {
        mDatePicker = new DiabloDatePicker(
            FirmStockCheck.this,
            mViewFragment.findViewById(R.id.btn_start_date),
            mViewFragment.findViewById(R.id.btn_end_date),
            (EditText)mViewFragment.findViewById(R.id.text_start_date),
            (EditText)mViewFragment.findViewById(R.id.text_end_date),
            UTILS.firstDayOfCurrentMonth());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStartFirm = DiabloEnum.INVALID_INDEX;
        if (getArguments() != null) {
            mStartFirm = getArguments().getInt(DiabloEnum.BUNDLE_PARAM_FIRM);
        }

        mTableHeads = getResources().getStringArray(R.array.thead_stock_detail);
        mStockTypes  = getResources().getStringArray(R.array.stock_type);

        mStockRest = StockClient.getClient().create(StockInterface.class);
        mRefreshDialog = UTILS.createLoadingDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewFragment =  inflater.inflate(R.layout.fragment_firm_stock_check, container, false);

//        mDatePicker = new DiabloDatePicker(
//            FirmStockCheck.this,
//            mViewFragment.findViewById(R.id.btn_start_date),
//            mViewFragment.findViewById(R.id.btn_end_date),
//            (EditText) mViewFragment.findViewById(R.id.text_start_date),
//            (EditText)mViewFragment.findViewById(R.id.text_end_date),
//            UTILS.currentDate());
        initPickerDate();

        // support action bar
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        mStockDetailTableSwipe = (SwipyRefreshLayout) mViewFragment.findViewById(R.id.t_stock_detail_swipe);
        // mStockDetailTableSwipe = (DiabloTableSwipeRefreshLayout) view.findViewById(R.id.t_sale_detail_swipe);
        mStockDetailTableSwipe.setDirection(SwipyRefreshLayoutDirection.BOTH);

        mStockDetailTableSwipe.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
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
                        mStockDetailTableSwipe.setRefreshing(false);
                    }

                } else if (direction == SwipyRefreshLayoutDirection.BOTTOM){
                    if (mCurrentPage.equals(mTotalPage)) {
                        DiabloUtils.instance().makeToast(
                            getContext(),
                            getContext().getResources().getString(R.string.refresh_bottom),
                            Toast.LENGTH_SHORT);
                        mStockDetailTableSwipe.setRefreshing(false);
                    } else {
                        mCurrentPage++;
                        pageChanged();
                    }

                }
            }
        });

        TableRow row = new TableRow(this.getContext());
        for (String title: mTableHeads){
            if (getResources().getString(R.string.firm).equals(title)) {
                continue;
            }
            TableRow.LayoutParams lp = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f);
            TextView cell = new TextView(this.getContext());
            cell.setTypeface(null, Typeface.BOLD);
            cell.setTextColor(Color.BLACK);

            if (getResources().getString(R.string.order_id).equals(title)) {
                lp.weight = 0.8f;
            }
            else if (getResources().getString(R.string.balance).equals(title)) {
                lp.weight = 1.5f;
            }
            else if (getResources().getString(R.string.acc_balance).equals(title)) {
                lp.weight = 1.5f;
            }
            cell.setGravity(Gravity.CENTER);
            cell.setLayoutParams(lp);
            cell.setText(title);
            cell.setTextSize(20);

            row.addView(cell);
        }



        // TableLayout head = ((TableLayout)view.findViewById(R.id.t_sale_detail_head));
        // head.addView(row);
        TableLayout head = (TableLayout) mViewFragment.findViewById(R.id.t_stock_detail_head);
        head.addView(row);

        mStockDetailTable = (TableLayout) mViewFragment.findViewById(R.id.t_stock_detail);
        mStockDetailTable.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        init();
        pageChanged();

        return mViewFragment;
    }

    private void pageChanged(){

        final StockDetailRequest request   = new StockDetailRequest(mCurrentPage, DiabloEnum.DEFAULT_ITEMS_PER_PAGE);
        request.setStartTime(mDatePicker.startTime());
        request.setEndTime(mDatePicker.endTime());

        if (0 == request.getShops().size()) {
            request.setShops(Profile.instance().getShopIds());
        }

        if (!DiabloEnum.INVALID_INDEX.equals(mStartFirm)) {
            request.addFirm(mStartFirm);
        }

        request.trim();

        Call<StockDetailResponse> call = mStockRest.filterStockNew(Profile.instance().getToken(), request);

        call.enqueue(new Callback<StockDetailResponse>() {
            @Override
            public void onResponse(Call<StockDetailResponse> call, Response<StockDetailResponse> response) {
                mStockDetailTableSwipe.setRefreshing(false);
                mRefreshDialog.dismiss();

                StockDetailResponse base = response.body();
                if (0 != base.getTotal()) {
                    if (DiabloEnum.DEFAULT_PAGE.equals(mCurrentPage)) {
                        mTotalPage = UTILS.calcTotalPage(base.getTotal(), request.getCount());
                        Resources res = getResources();
                        mStatistic =
                            res.getString(R.string.amount) + res.getString(R.string.colon) + UTILS.toString(base.getAmount())
                                + res.getString(R.string.space_4)
                                + res.getString(R.string.should_pay) + res.getString(R.string.colon) + UTILS.toString(base.getSPay())
                                + res.getString(R.string.space_4)
                                + res.getString(R.string.has_pay) + res.getString(R.string.colon) + UTILS.toString(base.getHPay());
                    }
                }

                List<StockDetailResponse.StockDetail> details = base.getSaleDetail();
                Integer orderId = request.getPageStartIndex();
                // mSaleDetailTable.removeAllViews();
                mStockDetailTable.removeAllViews();
                TableRow row = null;
                for (Integer i=0; i<details.size(); i++){
                    row = new TableRow(getContext());
                    StockDetailResponse.StockDetail detail = details.get(i);
                    TextView cell = null;
                    for (String title: mTableHeads){
                        if (getResources().getString(R.string.firm).equals(title)) {
                            continue;
                        }

                        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);
                        if (i == details.size() - 1) {
                            lp.setMargins(0, 1, 0, 1);
                        } else {
                            lp.setMargins(0, 1, 0, 0);
                        }

                        if (getResources().getString(R.string.order_id).equals(title)) {
                            detail.setOrderId(orderId);
                            lp.weight = 0.8f;
                            cell = addCell(row, orderId++, lp);
                        }
                        else if (getResources().getString(R.string.rsn).equals(title)){
                            String [] fs = detail.getRsn().split("-");
                            cell = addCell(row, fs[fs.length - 1], lp);
                        }
                        else if(getResources().getString(R.string.trans).equals(title)){
                            cell = addCell(row, mStockTypes[detail.getType()], lp);
                            if (detail.getType().equals(DiabloEnum.SALE_OUT)){
                                cell.setTextColor(getResources().getColor(R.color.red));
                            }
                        }
                        else if(getResources().getString(R.string.shop).equals(title)){
                            cell = addCell(row,
                                DiabloUtils.getInstance().getShop(Profile.instance().getSortShop(),
                                    detail.getShop()).getName(),
                                lp);
                        }
                        else if (getResources().getString(R.string.employee).equals(title)){
                            cell = addCell(row,
                                DiabloUtils.getInstance().getEmployeeByNumber(
                                    Profile.instance().getEmployees(),
                                    detail.getEmployee()).getName(),
                                lp);
                        }
//                        else if (getContext().getString(R.string.firm).equals(title)){
//                            Firm f = Profile.instance().getFirm(detail.getFirm());
//                            if (null != f) {
//                                cell = addCell(row, f.getName(), lp);
//                            }
//                            else {
//                                cell = addCell(row, DiabloEnum.EMPTY_STRING, lp);
//                            }
//                        }
                        else if (getResources().getString(R.string.amount).equals(title)){
                            cell = addCell(row, detail.getTotal(), lp);
                            // cell.setTextColor(Color.MAGENTA);
                        }
                        else if (getResources().getString(R.string.balance).equals(title)){
                            lp.weight = 1.5f;
                            cell = addCell(row, detail.getBalance(), lp);
                            cell.setTextColor(ContextCompat.getColor(getContext(), R.color.bpBlue));
                        }
                        else if (getResources().getString(R.string.should_pay).equals(title)){
                            cell = addCell(row, detail.getShouldPay(), lp);
                        }
                        else if (getResources().getString(R.string.has_pay).equals(title)){
                            cell = addCell(row, detail.getHasPay(), lp);
                        }
                        else if (getResources().getString(R.string.verificate).equals(title)){
                            cell = addCell(row, detail.getVerificate(), lp);
                        }
                        else if (getResources().getString(R.string.epay).equals(title)){
                            cell = addCell(row, detail.getEPay(), lp);
                        }
                        else if (getResources().getString(R.string.acc_balance).equals(title)){
                            lp.weight = 1.5f;
                            cell = addCell(
                                row,
                                detail.getBalance()
                                    + detail.getShouldPay()
                                    + detail.getEPay()
                                    - detail.getHasPay()
                                    - detail.getVerificate(),
                                lp);
                            cell.setTextColor(ContextCompat.getColor(getContext(), R.color.blueLight));
                        }
                        else if (getResources().getString(R.string.cash).equals(title)){
                            cell = addCell(row, detail.getCash(), lp);
                        }
                        else if (getResources().getString(R.string.card).equals(title)){
                            cell = addCell(row, detail.getCard(), lp);
                        }
                        else if (getResources().getString(R.string.wire).equals(title)){
                            cell = addCell(row, detail.getWire(), lp);
                        }
                        else if (getResources().getString(R.string.date).equals(title)){
                            String shortDate = detail.getEntryDate().substring(
                                5, detail.getEntryDate().length() - 3).trim();
                            cell = addCell(row, shortDate, lp);
                        }

                        if (null != cell ) {
                            cell.setGravity(Gravity.CENTER);
                            cell.setBackgroundResource(R.drawable.table_cell_bg);
                        }
                    }

                    row.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            // SaleDetailResponse.SaleDetail d = (SaleDetailResponse.SaleDetail)view.getTag();
                            view.showContextMenu();
                            return true;
                        }
                    });
                    registerForContextMenu(row);

                    row.setBackgroundResource(R.drawable.table_row_bg);
                    row.setTag(detail);
                    mStockDetailTable.addView(row);
                }

                if (null != row) {
                    row.setBackgroundResource(R.drawable.table_row_last_bg);
                }

                if (0 < mTotalPage ) {
                    row = new TableRow(getContext());
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f);
                    UTILS.formatTableStatistic(addCell(row, mStatistic, lp));

                    String pageInfo = mCurrentPage.toString() + "/" + mTotalPage.toString();
                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
                    UTILS.formatPageInfo(addCell(row, pageInfo, lp2));
                    mStockDetailTable.addView(row);
                }
            }

            @Override
            public void onFailure(Call<StockDetailResponse> call, Throwable t) {
                UTILS.makeToast(getContext(), DiabloError.getError(99), Toast.LENGTH_LONG);
                mStockDetailTableSwipe.setRefreshing(false);
                mRefreshDialog.dismiss();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();
        inflater.inflate(R.menu.action_on_check_firm_stock, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.check_trans_refresh:
                init();
                mRefreshDialog.show();
                pageChanged();
                break;
            case R.id.check_trans_to_firm:
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_FIRM_PAGER);
            default:
                // return super.onOptionsItemSelected(item);
                break;

        }

        return true;
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        mCurrentSelectedRow = (TableRow) v;
//        MenuInflater inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.context_on_stock_detail, menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        StockDetailResponse.StockDetail detail = ((StockDetailResponse.StockDetail) mCurrentSelectedRow.getTag());
//        if (getResources().getString(R.string.modify) == item.getTitle()){
//
//        }
//        return true;
//    }

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
            init();
            pageChanged();
        }
    }

    public TextView addCell(TableRow row, String value, TableRow.LayoutParams lp){
        return UTILS.addCell(getContext(), row, value, lp);
    }

    public TextView addCell(TableRow row, Integer value, TableRow.LayoutParams lp){
        return UTILS.addCell(getContext(), row, value, lp);
    }

    public TextView addCell(TableRow row, float value, TableRow.LayoutParams lp){
        return UTILS.addCell(getContext(), row, value, lp);
    }

}
