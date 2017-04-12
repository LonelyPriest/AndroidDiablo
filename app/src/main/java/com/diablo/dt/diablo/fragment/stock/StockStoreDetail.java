package com.diablo.dt.diablo.fragment.stock;


import static com.diablo.dt.diablo.R.string.inventory;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextMenu;
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
import com.diablo.dt.diablo.entity.DiabloBrand;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.DiabloType;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Stock;
import com.diablo.dt.diablo.model.sale.SaleStockAmount;
import com.diablo.dt.diablo.model.sale.SaleUtils;
import com.diablo.dt.diablo.request.stock.InventoryDetailRequest;
import com.diablo.dt.diablo.response.good.InventoryDetailResponse;
import com.diablo.dt.diablo.rest.StockInterface;
import com.diablo.dt.diablo.task.MatchSingleStockTask;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloTableStockNote;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockStoreDetail extends Fragment {
    private final static String LOG_TAG = "StockStoreDetail:";
    private final static DiabloUtils UTILS = DiabloUtils.instance();
    private String [] mTableHeads;
    private String [] mSeasons;
    private String mStatistic;

    private InventoryDetailRequest mRequest;
    private InventoryDetailRequest.Condition mRequestCondition;
    private StockInterface mStockRest;

    private TableLayout mTable;
    private SwipyRefreshLayout mTableSwipe;

    private TableRow mCurrentSelectedRow;

    private Integer mCurrentPage;
    private Integer mTotalPage;

    private Dialog mRefreshDialog;

    public StockStoreDetail() {
        // Required empty public constructor
    }

    public static StockStoreDetail newInstance(String param1, String param2) {
        StockStoreDetail fragment = new StockStoreDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTableHeads = getResources().getStringArray(R.array.thead_inventory_detail);
        mSeasons = getResources().getStringArray(R.array.seasons);

        mRequest = new InventoryDetailRequest(mCurrentPage, DiabloEnum.DEFAULT_ITEMS_PER_PAGE);
        mRequestCondition = new InventoryDetailRequest.Condition();
        mRequest.setCondition(mRequestCondition);

        mStockRest = StockClient.getClient().create(StockInterface.class);
        mRefreshDialog = UTILS.createLoadingDialog(getContext());

        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_stock_store_detail, container, false);

        String currentDate = UTILS.currentDate();
        String startDate = Profile.instance().getConfig(DiabloEnum.START_TIME, currentDate);
        ((EditText)view.findViewById(R.id.text_start_date)).setText(startDate);
        mRequest.getCondition().setStartTime(startDate);

        ((EditText)view.findViewById(R.id.text_end_date)).setText(currentDate);
        mRequest.getCondition().setEndTime(UTILS.nextDate());

        (view.findViewById(R.id.btn_start_date)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaleUtils.DiabloDatePicker.build(StockStoreDetail.this, new SaleUtils.DiabloDatePicker.OnDateSetListener() {
                    @Override
                    public void onDateSet(String date, String nextDate) {
                        ((EditText) view.findViewById(R.id.text_start_date)).setText(date);
                        mRequest.getCondition().setStartTime(date);
                    }
                });
            }
        });

        view.findViewById(R.id.btn_end_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaleUtils.DiabloDatePicker.build(StockStoreDetail.this, new SaleUtils.DiabloDatePicker.OnDateSetListener() {
                    @Override
                    public void onDateSet(String date, String nextDate) {
                        ((EditText)view.findViewById(R.id.text_end_date)).setText(date);
                        mRequest.getCondition().setEndTime(nextDate);
                    }
                });
            }
        });

        // support action bar
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        mTableSwipe = (SwipyRefreshLayout) view.findViewById(R.id.t_inventory_detail_swipe);
        // mStockDetailTableSwipe = (DiabloTableSwipeRefreshLayout) view.findViewById(R.id.t_sale_detail_swipe);
        mTableSwipe.setDirection(SwipyRefreshLayoutDirection.BOTH);

        mTableSwipe.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP){
                    if (!mCurrentPage.equals(DiabloEnum.DEFAULT_PAGE)){
                        mCurrentPage--;
                        mRequest.setPage(mCurrentPage);
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
                        mRequest.setPage(mCurrentPage);
                        pageChanged();
                    }

                }
            }
        });

        TableRow row = new TableRow(this.getContext());
        for (String title: mTableHeads){
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
            else if (getResources().getString(R.string.sell).equals(title)) {
                lp.weight = 0.8f;
            }
            else if (getResources().getString(R.string.shelfDate).equals(title)) {
                lp.weight = 0.8f;
            }

            cell.setLayoutParams(lp);
            cell.setText(title);
            cell.setTextSize(20);

            row.addView(cell);
        }



        // TableLayout head = ((TableLayout)view.findViewById(R.id.t_sale_detail_head));
        // head.addView(row);
        TableLayout head = (TableLayout) view.findViewById(R.id.t_inventory_detail_head);
        head.addView(row);

        mTable = (TableLayout) view.findViewById(R.id.t_inventory_detail);

        pageChanged();

        return view;
    }

    private void pageChanged(){
        Call<InventoryDetailResponse> call = mStockRest.filterInventory(Profile.instance().getToken(), mRequest);

        call.enqueue(new Callback<InventoryDetailResponse>() {
            @Override
            public void onResponse(Call<InventoryDetailResponse> call, Response<InventoryDetailResponse> response) {
                Log.d(LOG_TAG, response.toString());

                mTableSwipe.setRefreshing(false);
                mRefreshDialog.dismiss();

                InventoryDetailResponse base = response.body();
                if (0 != base.getTotal()) {
                    if (DiabloEnum.DEFAULT_PAGE.equals(mCurrentPage)) {
                        mTotalPage = UTILS.calcTotalPage(base.getTotal(), mRequest.getCount());
                        Resources res = getResources();
                        mStatistic =
//                            res.getString(R.string.amount) + res.getString(R.string.colon) + UTILS.toString(base.getAmount())
//                                + res.getString(R.string.space_4)
                                res.getString(R.string.stock) + res.getString(R.string.colon) + UTILS.toString(base.getAmount())
                                + res.getString(R.string.space_4)
                                + res.getString(R.string.sell) + res.getString(R.string.colon) + UTILS.toString(base.getSell());
                    }
                }

                List<InventoryDetailResponse.inventory> inventories = base.getInventories();
                Integer orderId = mRequest.getPageStartIndex();
                // mSaleDetailTable.removeAllViews();
                mTable.removeAllViews();
                TableRow row = null;
                for (Integer i=0; i<inventories.size(); i++){
                    row = new TableRow(getContext());

                    InventoryDetailResponse.inventory inv = inventories.get(i);
                    Resources res = getResources();

                    for (String title: mTableHeads){
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                        if (res.getString(R.string.order_id).equals(title)) {
                            inv.setOrderId(orderId);
                            lp.weight = 0.8f;
                            TextView cell = addCell(row, orderId++, lp);
                            cell.setTextColor(ContextCompat.getColor(getContext(), R.color.bpDarker_red));
                        }
                        else if (res.getString(R.string.style_number).equals(title)) {
                            addCell(row, inv.getStyleNumber(), lp);
                        }
                        else if (res.getString(R.string.brand).equals(title)) {
                            DiabloBrand brand = Profile.instance().getBrand(inv.getBrandId());
                            TextView cell;
                            if (null != brand) {
                                cell = addCell(row, brand.getName(), lp);
                            } else {
                                cell = addCell(row, DiabloEnum.EMPTY_STRING, lp);
                            }

                            cell.setTextColor(ContextCompat.getColor(getContext(), R.color.bpRed_focused));
                            // cell.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orangeDark));

                        }
                        else if (res.getString(R.string.good_type).equals(title)) {
                            DiabloType type = Profile.instance().getDiabloType(inv.getTypeId());
                            if (null != type) {
                                addCell(row, type.getName(), lp);
                            } else {
                                addCell(row, DiabloEnum.EMPTY_STRING, lp);
                            }
                        }
                        else if (res.getString(R.string.firm).equals(title)) {
                            Firm firm = Profile.instance().getFirm(inv.getFirmId());
                            if ( null != firm ) {
                                addCell(row, firm.getName(), lp);
                            } else {
                                addCell(row, DiabloEnum.EMPTY_STRING, lp);
                            }
                        }
                        else if (res.getString(R.string.season).equals(title)) {
                            addCell(row, mSeasons[inv.getSeason()], lp);
                        }
                        else if (res.getString(R.string.year).equals(title)) {
                            addCell(row, inv.getYear(), lp);
                        }
                        else if (res.getString(R.string.tag_price).equals(title)) {
                            TextView cell = addCell(row, inv.getTagPrice(), lp);
                            if (inv.getTagPrice() > 0f) {
                                cell.setTextColor(ContextCompat.getColor(getContext(), R.color.greenDark));
                            }
                        }
                        else if (res.getString(R.string.pkg_price).equals(title)) {
                            TextView cell = addCell(row, inv.getPkgPrice(), lp);
                            if (inv.getPkgPrice() > 0f) {
                                cell.setTextColor(ContextCompat.getColor(getContext(), R.color.orangeDark));
                            }
                        }
                        else if (res.getString(inventory).equals(title)) {
                            TextView cell = addCell(row, inv.getAmount(), lp);
                            if (inv.getAmount() > 0) {
                                cell.setTextColor(ContextCompat.getColor(getContext(), R.color.bpBlue));
                            }
                        }
                        else if (res.getString(R.string.sell).equals(title)) {
                            lp.weight = 0.8f;
                            addCell(row, inv.getSell(), lp);
                        }
                        else if (res.getString(R.string.shelfDate).equals(title)){
                            lp.weight = 0.8f;
                            addCell(row, inv.getDatetime(), lp);
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
                    row.setTag(inv);
                    mTable.addView(row);
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

                    UTILS.formatPageInfo(addCell(row, pageInfo, lp));
                    mTable.addView(row);
                }
            }

            @Override
            public void onFailure(Call<InventoryDetailResponse> call, Throwable t) {
                mTableSwipe.setRefreshing(false);
                mRefreshDialog.dismiss();
            }
        });
    }

    public void init() {
        mCurrentPage = DiabloEnum.DEFAULT_PAGE;
        mTotalPage = 0;
        mRequest.setPage(DiabloEnum.DEFAULT_PAGE);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();

        inflater.inflate(R.menu.action_on_stock_store_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.inventory_refresh:
                init();
                mRefreshDialog.show();
                pageChanged();
                break;
            default:
                // return super.onOptionsItemSelected(item);
                break;

        }

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        mCurrentSelectedRow = (TableRow) v;
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_on_stock_store_detail, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        InventoryDetailResponse.inventory stock = ((InventoryDetailResponse.inventory) mCurrentSelectedRow.getTag());
        if (getResources().getString(R.string.note) == item.getTitle()) {
            getStockNoteFromServer(stock);
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


    public TextView addCell(TableRow row, String value, TableRow.LayoutParams lp){
        // TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight);
        TextView cell = new TextView(getContext());
        cell.setLayoutParams(lp);
        // cell.setTextColor(context.getResources().getColor(R.color.black));
        cell.setText(value.trim());
        cell.setTextSize(18);
        // cell.setHeight(105);
        // cell.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        row.addView(cell);
        return  cell;
    }

    public TextView addCell(TableRow row, Integer value, TableRow.LayoutParams lp){
        // TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight);
        TextView cell = new TextView(getContext());
        if (value < 0) {
            cell.setTextColor(getContext().getResources().getColor(R.color.red));
        }
        cell.setLayoutParams(lp);
        cell.setText(DiabloUtils.instance().toString(value).trim());
        cell.setTextSize(20);
        // cell.setHeight(120);
        // cell.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        row.addView(cell);
        return  cell;
    }

    public TextView addCell(TableRow row, float value, TableRow.LayoutParams lp){
        // TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight);
        TextView cell = new TextView(getContext());
        if (value < 0f) {
            cell.setTextColor(getContext().getResources().getColor(R.color.red));
        }

        cell.setLayoutParams(lp);
        cell.setText(DiabloUtils.instance().toString(value).trim());
        cell.setTextSize(20);
        // cell.setHeight(120);
        // cell.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        row.addView(cell);
        return  cell;
    }

    private void getStockNoteFromServer(final InventoryDetailResponse.inventory inventory) {
        MatchSingleStockTask task = new MatchSingleStockTask(
            inventory.getStyleNumber(),
            inventory.getBrandId(),
            inventory.getShopId());

        task.setMatchSingleStockTaskListener(new MatchSingleStockTask.OnMatchSingleStockTaskListener() {
            @Override
            public void onMatchSuccess(List<Stock> matchedStocks) {
                Log.d(LOG_TAG, "success to get stock");

                final List<SaleStockAmount> amounts = new ArrayList<>();
                List<DiabloColor> colors = new ArrayList<>();

                for(Stock s: matchedStocks){
                    DiabloColor color = Profile.instance().getColor(s.getColorId());
                    if (!color.includeIn(colors)){
                        colors.add(color);
                    }

                    SaleStockAmount amount = new SaleStockAmount(s.getColorId(), s.getSize());
                    amount.setStock(s.getExist());
                    amounts.add(amount);
                }

                ArrayList<String> sizes = Profile.instance().genSortedSizeNamesByGroups(inventory.getsGroup());

                new DiabloTableStockNote(
                    getContext(),
                    inventory.getStyleNumber(),
                    inventory.getBrandId(),
                    colors,
                    sizes,
                    new DiabloTableStockNote.OnStockNoteListener() {
                        @Override
                        public Integer getStockNote(Integer color, String size) {
                            SaleStockAmount amount = SaleUtils.getSaleStockAmount(amounts, color, size);
                            if (null != amount) {
                                return amount.getStock();
                            }
                            return null;
                        }
                    }
                ).show();
            }

            @Override
            public void onMatchFailed(Throwable t) {
                Log.d(LOG_TAG, "failed to get stock");
            }
        });

        task.getStock();
    }

}
