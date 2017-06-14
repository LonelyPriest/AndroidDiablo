package com.diablo.dt.diablo.fragment.retailer;

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
import android.util.Log;
import android.view.ContextMenu;
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
import com.diablo.dt.diablo.client.WSaleClient;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.model.sale.SaleUtils;
import com.diablo.dt.diablo.request.sale.SaleDetailRequest;
import com.diablo.dt.diablo.response.sale.SaleDetailResponse;
import com.diablo.dt.diablo.rest.WSaleInterface;
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


public class DiabloRetailerSaleCheck extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private final static DiabloUtils UTILS = DiabloUtils.instance();
    private String [] mTableHeads;
    private String[]  mSaleTypes;

    private String mStatistic;

    private View mViewFragment;

    /*
    * rest request
    * */
    private WSaleInterface mSaleRest;

    /*
    * row of table
    * */
    // TableRow[] mRows;

    private android.widget.TableLayout mSaleDetailTable;
    private SwipyRefreshLayout mSaleDetailTableSwipe;
    private Dialog mRefreshDialog;
    // private DiabloTableSwipeRefreshLayout mSaleDetailTableSwipe;

    // private View mViewFragment;
    private TableRow mCurrentSelectedRow;

    private Integer mCurrentPage;
    private Integer mTotalPage;

    private DiabloDatePicker mDatePicker;

    private Integer mStartRetailer;

    public DiabloRetailerSaleCheck() {

    }

    private void initTitle() {
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (null != bar) {
            Retailer r = Profile.instance().getRetailerById(mStartRetailer);
            // String title = getResources().getString(R.string.check_trans);
            // title += null == r ? DiabloEnum.EMPTY_STRING : r.getName();
            bar.setTitle((getResources().getString(R.string.check_trans))
                + "(" + (null == r ? DiabloEnum.EMPTY_STRING : r.getName()) + ")");
        }
    }

    private void initPickerDate() {
        mDatePicker = new DiabloDatePicker(
            DiabloRetailerSaleCheck.this,
            mViewFragment.findViewById(R.id.btn_start_date),
            mViewFragment.findViewById(R.id.btn_end_date),
            (EditText)mViewFragment.findViewById(R.id.text_start_date),
            (EditText)mViewFragment.findViewById(R.id.text_end_date),
            UTILS.firstDayOfCurrentMonth());
    }

    public void init() {
        initTitle();
        // initPickerDate();
        mCurrentPage = DiabloEnum.DEFAULT_PAGE;
        mTotalPage = 0;
    }

    public static DiabloRetailerSaleCheck newInstance(String param1, String param2) {
        DiabloRetailerSaleCheck fragment = new DiabloRetailerSaleCheck();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setStartRetailer(Integer retailer) {
        mStartRetailer = retailer;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStartRetailer = DiabloEnum.INVALID_INDEX;
        if (getArguments() != null) {
            mStartRetailer = getArguments().getInt(DiabloEnum.BUNDLE_PARAM_RETAILER);
        }

        mTableHeads = getResources().getStringArray(R.array.thead_sale_detail);
        mSaleTypes = getResources().getStringArray(R.array.sale_type);

        mSaleRest = WSaleClient.getClient().create(WSaleInterface.class);
        mRefreshDialog = UTILS.createLoadingDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewFragment = inflater.inflate(R.layout.fragment_diablo_retailer_sale_check, container, false);

//        mDatePicker = new DiabloDatePicker(
//            DiabloRetailerSaleCheck.this,
//            view.findViewById(R.id.btn_start_date),
//            view.findViewById(R.id.btn_end_date),
//            (EditText)view.findViewById(R.id.text_start_date),
//            (EditText)view.findViewById(R.id.text_end_date),
//            UTILS.firstDayOfCurrentMonth());

        // support action bar
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        initPickerDate();

        mSaleDetailTableSwipe = (SwipyRefreshLayout) mViewFragment.findViewById(R.id.t_sale_detail_swipe);
        // mSaleDetailTableSwipe = (DiabloTableSwipeRefreshLayout) view.findViewById(R.id.t_sale_detail_swipe);
        mSaleDetailTableSwipe.setDirection(SwipyRefreshLayoutDirection.BOTH);

        mSaleDetailTableSwipe.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP){
                    if (!mCurrentPage.equals(DiabloEnum.DEFAULT_PAGE)){
                        mCurrentPage--;
                        // mRequest.setPage(mCurrentPage);
                        pageChanged();
                    } else {
                        DiabloUtils.instance().makeToast(
                            getContext(),
                            getContext().getResources().getString(R.string.refresh_top),
                            Toast.LENGTH_SHORT);
                        mSaleDetailTableSwipe.setRefreshing(false);
                    }

                } else if (direction == SwipyRefreshLayoutDirection.BOTTOM){
                    if (mCurrentPage.equals(mTotalPage)) {
                        DiabloUtils.instance().makeToast(
                            getContext(),
                            getContext().getResources().getString(R.string.refresh_bottom),
                            Toast.LENGTH_SHORT);
                        mSaleDetailTableSwipe.setRefreshing(false);
                    } else {
                        mCurrentPage++;
                        // mRequest.setPage(mCurrentPage);
                        pageChanged();
                    }

                }
            }
        });


        TableRow row = new TableRow(this.getContext());
        for (String title: mTableHeads){
            if (title.equals(getResources().getString(R.string.retailer))) {
                continue;
            }
            TableRow.LayoutParams lp = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            TextView cell = new TextView(this.getContext());
            cell.setTypeface(null, Typeface.BOLD);
            cell.setTextColor(Color.BLACK);

            // right-margin
            cell.setLayoutParams(lp);
            cell.setText(title);
            cell.setTextSize(20);
            cell.setGravity(Gravity.CENTER);

            row.addView(cell);
        }

        final TableLayout head = (TableLayout) mViewFragment.findViewById(R.id.t_sale_detail_head);
        head.addView(row);

        mSaleDetailTable = (TableLayout) mViewFragment.findViewById(R.id.t_sale_detail);
        mSaleDetailTable.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        init();
        // initFilters();
        pageChanged();

        return mViewFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void pageChanged(){
        final SaleDetailRequest request = new SaleDetailRequest(mCurrentPage, DiabloEnum.DEFAULT_ITEMS_PER_PAGE);
        request.setStartTime(mDatePicker.startTime());
        request.setEndTime(mDatePicker.endTime());

        if (0 == request.getShops().size()) {
            request.setShops(Profile.instance().getShopIds());
        }

        if (!DiabloEnum.INVALID_INDEX.equals(mStartRetailer)) {
            request.addRetailer(mStartRetailer);
        }

        request.trim();
        Call<SaleDetailResponse> call = mSaleRest.filterSaleNew(Profile.instance().getToken(), request);

        call.enqueue(new Callback<SaleDetailResponse>() {
            @Override
            public void onResponse(Call<SaleDetailResponse> call, Response<SaleDetailResponse> response) {
                Log.d("SALE_DETAIL %s", response.toString());

                mSaleDetailTableSwipe.setRefreshing(false);
                mRefreshDialog.dismiss();

                SaleDetailResponse base = response.body();
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

                List<SaleDetailResponse.SaleDetail> details = base.getSaleDetail();
                Integer orderId = request.getPageStartIndex();
                TableRow row = null;
                mSaleDetailTable.removeAllViews();
                for (Integer i=0; i<details.size(); i++){
                    row = new TableRow(getContext());
                    row.setBackgroundResource(R.drawable.table_row_bg);

                    SaleDetailResponse.SaleDetail detail = details.get(i);
                    TextView cell = null;
                    for (String title: mTableHeads){
                        if (title.equals(getResources().getString(R.string.retailer))) {
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
                            cell = addCell(row, orderId++, lp);
                            // cell.setPadding(0, 0, 10, 0);
                        }
                        else if (getResources().getString(R.string.rsn).equals(title)){
                            String [] fs = detail.getRsn().split("-");
                            cell = addCell(row, fs[fs.length - 1], lp);
                            // cell.setPadding(10, 0, 10, 0);
                        }
                        else if(getResources().getString(R.string.trans).equals(title)){
                            String name = DiabloEnum.EMPTY_STRING;
                            switch (detail.getType()) {
                                case 0:
                                    name = mSaleTypes[0];
                                    break;
                                case 1:
                                    name = mSaleTypes[1];
                                    break;
                                case 9:
                                    name = mSaleTypes[2];
                                    break;
                            }

                            cell = addCell(row, name, lp);
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
                        else if (getContext().getString(R.string.retailer).equals(title)){
                            Retailer r = DiabloUtils.getInstance().getRetailer(
                                Profile.instance().getRetailers(), detail.getRetailer());

                            if (null != r) {
                                cell = addCell(row, r.getName(), lp);
                            }
                            else {
                                cell = addCell(row, DiabloEnum.EMPTY_STRING, lp);
                            }
                        }
                        else if (getResources().getString(R.string.amount).equals(title)){
                            cell = addCell(row, detail.getTotal(), lp);
                        }
                        else if (getResources().getString(R.string.balance).equals(title)){
                            cell = addCell(row, detail.getBalance(), lp);
                            cell.setTextColor(ContextCompat.getColor(getContext(), R.color.magenta));
                        }
                        else if (getResources().getString(R.string.should_pay).equals(title)){
                            cell = addCell(row, detail.getShouldPay(), lp);
                        }
                        else if (getResources().getString(R.string.has_pay).equals(title)){
                            Float hasPay = detail.getHasPay();
                            cell = addCell(row, hasPay, lp);
                            if (hasPay > 0) {
                                cell.setTextColor(ContextCompat.getColor(getContext(), R.color.deepPink));
                            }
                        }
                        else if (getResources().getString(R.string.verificate).equals(title)){
                            cell = addCell(row, detail.getVerificate(), lp);
                        }
                        else if (getResources().getString(R.string.epay).equals(title)){
                            cell = addCell(row, detail.getEPay(), lp);
                        }
                        else if (getResources().getString(R.string.acc_balance).equals(title)){
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
                            // cell.setPadding(10, 0, 0, 0);
                        }

                        if (null != cell ) {
                            // cell.setPadding(10, 0, 10, 0);
                            cell.setGravity(Gravity.CENTER);
                            cell.setBackgroundResource(R.drawable.table_cell_bg);
                            // cell.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
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

                    // row.setBackgroundResource(R.drawable.table_row_bg);
                    // row.setBackgroundResource(R.drawable.table_cell_bg);
                    row.setTag(detail);
                    mSaleDetailTable.addView(row);
                }

                if (null != row) {
                    row.setBackgroundResource(R.drawable.table_row_last_bg);
                }

                if (0 < mTotalPage ) {
                    row = new TableRow(getContext());
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                    UTILS.formatTableStatistic(addCell(row, mStatistic, lp));

                    String pageInfo = getResources().getString(R.string.current_page) + mCurrentPage.toString()
                        + getResources().getString(R.string.page)
                        + getResources().getString(R.string.space_4)
                        + getResources().getString(R.string.total_page) + mTotalPage.toString()
                        + getResources().getString(R.string.page);

                    TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
                    UTILS.formatPageInfo(addCell(row, pageInfo, lp2));
                    mSaleDetailTable.addView(row);
                }
            }

            @Override
            public void onFailure(Call<SaleDetailResponse> call, Throwable t) {
                UTILS.makeToast(getContext(), DiabloError.getError(99), Toast.LENGTH_LONG);
                mSaleDetailTableSwipe.setRefreshing(false);
                mRefreshDialog.dismiss();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();

        inflater.inflate(R.menu.action_on_check_retailer_sale, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.check_trans_refresh:
                init();
                mRefreshDialog.show();
                pageChanged();
                break;
            case R.id.check_trans_to_retailer:
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_RETAILER_DETAIL);
            default:
                // return super.onOptionsItemSelected(item);
                break;

        }

        return true;
    }

    public TextView addCell(TableRow row, String value, TableRow.LayoutParams lp){
        return UTILS.addCell(getContext(), row, value, lp);
    }

    public TextView addCell(TableRow row, Integer value, TableRow.LayoutParams lp){
        // TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight);
        return UTILS.addCell(getContext(), row, value, lp);
    }

    public TextView addCell(TableRow row, float value, TableRow.LayoutParams lp){
        return UTILS.addCell(getContext(), row, value, lp);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        mCurrentSelectedRow = (TableRow) v;
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_on_sale_detail, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        SaleDetailResponse.SaleDetail detail = ((SaleDetailResponse.SaleDetail) mCurrentSelectedRow.getTag());
        if (getResources().getString(R.string.note) == item.getTitle()) {

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
            init();
            pageChanged();
        }
    }


}
