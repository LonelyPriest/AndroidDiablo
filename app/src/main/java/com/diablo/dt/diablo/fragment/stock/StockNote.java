package com.diablo.dt.diablo.fragment.stock;


import static com.diablo.dt.diablo.R.string.amount;

import android.app.Dialog;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.activity.MainActivity;
import com.diablo.dt.diablo.client.StockClient;
import com.diablo.dt.diablo.entity.DiabloBrand;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.DiabloType;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.sale.SaleUtils;
import com.diablo.dt.diablo.model.stock.StockUtils;
import com.diablo.dt.diablo.request.stock.StockNoteRequest;
import com.diablo.dt.diablo.response.stock.GetStockNewResponse;
import com.diablo.dt.diablo.response.stock.StockNoteResponse;
import com.diablo.dt.diablo.rest.StockInterface;
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

public class StockNote extends Fragment {
    private final static DiabloUtils UTILS = DiabloUtils.instance();
    private String [] mTableHeads;
    private String[]  mStockTypes;

    private String mStatistic;
    private String mPagination;


    private android.widget.TableLayout mSaleNoteTable;
    private SwipyRefreshLayout mSaleNoteTableSwipe;
    private Dialog mRefreshDialog;
    // private DiabloTableSwipeRefreshLayout mSaleDetailTableSwipe;

    private TableRow mCurrentSelectedRow;

    private Integer mCurrentPage;
    private Integer mTotalPage;

    /**
     * request
     */
    private StockNoteRequest mRequest;
    private StockNoteRequest.Condition mRequestCondition;
    private StockInterface mStockRest;

    public StockNote() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static StockNote newInstance(String param1, String param2) {
        StockNote fragment = new StockNote();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTableHeads = getResources().getStringArray(R.array.thead_stock_detail_note);
        mStockTypes = getResources().getStringArray(R.array.stock_type);

        mRequest = new StockNoteRequest(mCurrentPage, DiabloEnum.DEFAULT_ITEMS_PER_PAGE);
        mRequestCondition = new StockNoteRequest.Condition();
        mRequest.setCondition(mRequestCondition);

        mStockRest = StockClient.getClient().create(StockInterface.class);
        mRefreshDialog = UTILS.createLoadingDialog(getContext());

        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sale_note, container, false);
        ((MainActivity)getActivity()).selectMenuItem(SaleUtils.SLIDE_MENU_TAGS.get(DiabloEnum.TAG_SALE_NOTE));

        // support action bar
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        mSaleNoteTableSwipe = (SwipyRefreshLayout) view.findViewById(R.id.t_sale_note_swipe);
        // mSaleDetailTableSwipe = (DiabloTableSwipeRefreshLayout) view.findViewById(R.id.t_sale_detail_swipe);
        mSaleNoteTableSwipe.setDirection(SwipyRefreshLayoutDirection.BOTH);

        mSaleNoteTableSwipe.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
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
                        mSaleNoteTableSwipe.setRefreshing(false);
                    }

                } else if (direction == SwipyRefreshLayoutDirection.BOTTOM){
                    if (mCurrentPage.equals(mTotalPage)) {
                        DiabloUtils.instance().makeToast(
                            getContext(),
                            getContext().getResources().getString(R.string.refresh_bottom),
                            Toast.LENGTH_SHORT);
                        mSaleNoteTableSwipe.setRefreshing(false);
                    } else {
                        mCurrentPage++;
                        mRequest.setPage(mCurrentPage);
                        pageChanged();
                    }

                }
            }
        });

        // add table head
        TableRow row = new TableRow(this.getContext());
        for (String title: mTableHeads){
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

            row.addView(cell);
        }

        TableLayout head = (TableLayout) view.findViewById(R.id.t_sale_note_head);
        head.addView(row);

        mSaleNoteTable = (TableLayout) view.findViewById(R.id.t_sale_note);

        pageChanged();

        return view;
    }

    public void init() {
        mCurrentPage = DiabloEnum.DEFAULT_PAGE;
        mTotalPage = 0;
        mRequest.setPage(DiabloEnum.DEFAULT_PAGE);
    }

    private void pageChanged(){
        // hidden keyboard
        // DiabloUtils.instance().hiddenKeyboard(getContext());
        // get data from from web server
        mRequest.getCondition().setStartTime(DiabloUtils.instance().currentDate());
        // mRequest.getCondition().setStartTime("2016-01-01");
        mRequest.getCondition().setEndTime(DiabloUtils.instance().nextDate());

        Call<StockNoteResponse> call = mStockRest.filterStockNote(Profile.instance().getToken(), mRequest);

        call.enqueue(new Callback<StockNoteResponse>() {
            @Override
            public void onResponse(Call<StockNoteResponse> call, Response<StockNoteResponse> response) {
                Log.d("SALE_DETAIL %s", response.toString());

                mSaleNoteTableSwipe.setRefreshing(false);
                mRefreshDialog.dismiss();

                StockNoteResponse base = response.body();
                if (DiabloEnum.DEFAULT_PAGE.equals(mCurrentPage) && 0 != base.getTotal()){
                    mTotalPage = UTILS.calcTotalPage(base.getTotal(), mRequest.getCount());

                    Resources res = getResources();
                    mStatistic =
                        res.getString(amount)
                            + res.getString(R.string.colon)
                            + UTILS.toString(base.getAmount());

                    // pagination
                    mPagination = getResources().getString(R.string.current_page) + mCurrentPage.toString()
                        + getResources().getString(R.string.page)
                        + getResources().getString(R.string.space_4)
                        + getResources().getString(R.string.total_page) + mTotalPage.toString()
                        + getResources().getString(R.string.page);
                }

                List<StockNoteResponse.StockNote> notes = base.getStockNotes();
                Integer orderId = mRequest.getPageStartIndex();
                // mSaleDetailTable.removeAllViews();
                mSaleNoteTable.removeAllViews();
                TableRow row = null;
                for (Integer i=0; i<notes.size(); i++){
                    row = new TableRow(getContext());
                    // TableRow row = new TableRow(mContext);
                    // mSaleDetailTable.addView(row);
                    // row.removeAllViews();
                    StockNoteResponse.StockNote note = notes.get(i);

                    for (String title: mTableHeads){
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                        if (getResources().getString(R.string.order_id).equals(title)) {
                            note.setOrderId(orderId);
                            addCell(row, orderId++, lp);
                        }
                        else if (getResources().getString(R.string.rsn).equals(title)){
                            String [] fs = note.getRsn().split("-");
                            addCell(row, fs[fs.length - 1], lp);
                        }
                        else if(getResources().getString(R.string.trans).equals(title)){
                            TextView cell = addCell(row, mStockTypes[note.getType()], lp);
                            if (note.getType().equals(DiabloEnum.STOCK_OUT)){
                                cell.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                            }
                        }
                        else if (getContext().getString(R.string.firm).equals(title)){
                            addCell(row, Profile.instance().getFirm(note.getFirmId()).getName(), lp);
                        }
                        else if (getContext().getString(R.string.style_number).equals(title)){
                            addCell(row, note.getStyleNumber(), lp);
                        }
                        else if (getContext().getString(R.string.brand).equals(title)){
                            DiabloBrand brand = Profile.instance().getBrand(note.getBrandId());
                            if (null != brand) {
                                addCell(row, brand.getName(), lp);
                            }
                            else {
                                addCell(row, DiabloEnum.EMPTY_STRING, lp);
                            }

                        }
                        else if (getContext().getString(R.string.good_type).equals(title)){
                            DiabloType type = Profile.instance().getDiabloType(note.getTypeId());
                            if (null != type) {
                                addCell(row, type.getName(), lp);
                            }
                            else {
                                addCell(row, DiabloEnum.EMPTY_STRING, lp);
                            }
                        }
                        else if (getContext().getString(R.string.year).equals(title)) {
                            addCell(row, note.getYear(), lp);
                        }
                        else if (getContext().getString(R.string.discount).equals(title)){
                            addCell(row, note.getDiscount(), lp);
                        }
                        else if (getContext().getString(amount).equals(title)){
                            addCell(row, note.getAmount(), lp);
                        }
                        else if (getResources().getString(R.string.date).equals(title)){
                            String shortDate = note.getDatetime().substring(
                                5, note.getDatetime().length() - 3).trim();
                            addCell(row, shortDate, lp);
                        }
                    }

                    row.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            // SaleNoteResponse.SaleNote n = (SaleNoteResponse.SaleNote) view.getTag();
                            view.showContextMenu();
                            return true;
                        }
                    });
                    registerForContextMenu(row);

                    row.setBackgroundResource(R.drawable.table_row_bg);
                    row.setTag(note);
                    mSaleNoteTable.addView(row);
                }

                if (null != row) {
                    row.setBackgroundResource(R.drawable.table_row_last_bg);
                }

                if (0 < mTotalPage ) {
                    row = new TableRow(getContext());
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                    UTILS.formatTableStatistic(addCell(row, mStatistic, lp));
                    UTILS.formatPageInfo(addCell(row, mPagination, lp));
                    mSaleNoteTable.addView(row);
                }
            }

            @Override
            public void onFailure(Call<StockNoteResponse> call, Throwable t) {
                mSaleNoteTableSwipe.setRefreshing(false);
                mRefreshDialog.dismiss();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_on_sale_note, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sale_note_refresh:
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
        inflater.inflate(R.menu.context_on_sale_note, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final StockNoteResponse.StockNote detail = ((StockNoteResponse.StockNote) mCurrentSelectedRow.getTag());

        if (getResources().getString(R.string.note) == item.getTitle()) {
            StockUtils.getStockNewInfoFormServer(detail.getRsn(), new StockUtils.OnGetStockNewFormSeverListener() {
                @Override
                public void afterGet(final GetStockNewResponse response) {
                    List<DiabloColor> colors = new ArrayList<>();

                    for(GetStockNewResponse.StockNote s: response.getStockNotes()){
                        DiabloColor color = Profile.instance().getColor(s.getColorId());
                        if (!color.includeIn(colors)){
                            colors.add(color);
                        }
                    }

                    ArrayList<String> sizes = Profile.instance().genSortedSizeNamesByGroups(detail.getsGroup());

                    new DiabloTableStockNote(
                        getContext(),
                        detail.getStyleNumber(),
                        detail.getBrandId(),
                        colors,
                        sizes,
                        new DiabloTableStockNote.OnStockNoteListener() {
                            @Override
                            public Integer getStockNote(Integer color, String size) {
                                GetStockNewResponse.StockNote note = response.getStockNote(color, size);
                                if (null != note) {
                                    return note.getAmount();
                                }
                                return null;
                            }
                        }
                    ).show();
                }
            });
        }

        return true;
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

}
