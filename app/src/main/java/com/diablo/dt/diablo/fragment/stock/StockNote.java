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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.StockClient;
import com.diablo.dt.diablo.entity.DiabloBrand;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.DiabloShop;
import com.diablo.dt.diablo.entity.DiabloType;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.filter.BrandFilter;
import com.diablo.dt.diablo.filter.DiabloFilter;
import com.diablo.dt.diablo.filter.DiabloFilterController;
import com.diablo.dt.diablo.filter.FirmFilter;
import com.diablo.dt.diablo.filter.GoodTypeFilter;
import com.diablo.dt.diablo.filter.ShopFilter;
import com.diablo.dt.diablo.filter.StockStyleNumberFilter;
import com.diablo.dt.diablo.filter.YearFilter;
import com.diablo.dt.diablo.model.sale.SaleUtils;
import com.diablo.dt.diablo.model.stock.StockUtils;
import com.diablo.dt.diablo.request.stock.StockNoteDetailRequest;
import com.diablo.dt.diablo.request.stock.StockNoteRequest;
import com.diablo.dt.diablo.response.stock.StockNoteDetailResponse;
import com.diablo.dt.diablo.response.stock.StockNoteResponse;
import com.diablo.dt.diablo.rest.StockInterface;
import com.diablo.dt.diablo.utils.DiabloDatePicker;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;
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
    private StockInterface mStockRest;
    private DiabloDatePicker mDatePicker;
    private StockStyleNumberFilter mStockStyleNumberFilter;
    private DiabloFilterController mFilterController;

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

//        mRequest = new StockNoteRequest(mCurrentPage, DiabloEnum.DEFAULT_ITEMS_PER_PAGE);
//        mRequestCondition = new StockNoteRequest.Condition();
//        mRequest.setCondition(mRequestCondition);

        mStockRest = StockClient.getClient().create(StockInterface.class);
        mRefreshDialog = UTILS.createLoadingDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_stock_note, container, false);

        // support action bar
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        mDatePicker = new DiabloDatePicker(
            StockNote.this,
            view.findViewById(R.id.btn_start_date),
            view.findViewById(R.id.btn_end_date),
            (EditText) view.findViewById(R.id.text_start_date),
            (EditText)view.findViewById(R.id.text_end_date),
            UTILS.currentDate());

        mSaleNoteTableSwipe = (SwipyRefreshLayout) view.findViewById(R.id.t_stock_note_swipe);
        mSaleNoteTableSwipe.setDirection(SwipyRefreshLayoutDirection.BOTH);

        mSaleNoteTableSwipe.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
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

        TableLayout head = (TableLayout) view.findViewById(R.id.t_stock_note_head);
        head.addView(row);

        mSaleNoteTable = (TableLayout) view.findViewById(R.id.t_stock_note);
        mSaleNoteTable.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        init();
        initFilter(view);
        pageChanged();

        return view;
    }

    public void init() {
        mCurrentPage = DiabloEnum.DEFAULT_PAGE;
        mTotalPage = 0;
    }

    private void initFilter(View view) {
        View styleNumberView = view.findViewById(R.id.select_style_number);
        mStockStyleNumberFilter = new StockStyleNumberFilter(getContext(), getString(R.string.style_number));
        mStockStyleNumberFilter.init(styleNumberView);

        ImageButton btnAdd = (ImageButton) view.findViewById(R.id.btn_add_filter);
        ImageButton btnMinus = (ImageButton) view.findViewById(R.id.btn_minus_filter);
        btnMinus.setEnabled(false);

        List<DiabloFilter> entities = new ArrayList<>();
        entities.add(new StockStyleNumberFilter(getContext(), getString(R.string.style_number)));
        entities.add(new FirmFilter(getContext(), getString(R.string.firm)));
        entities.add(new BrandFilter(getContext(), getString(R.string.brand)));

        entities.add(new GoodTypeFilter(getContext(), getString(R.string.good_type)));
        entities.add(new YearFilter(getContext(), getString(R.string.year)));
        entities.add(new ShopFilter(getContext(), getString(R.string.shop)));

        mFilterController = new DiabloFilterController(getContext(), entities, 2);
        mFilterController.init((LinearLayout)view, R.id.t_stock_note_head, btnAdd, btnMinus);
    }

    private void pageChanged(){
        // hidden keyboard
        // DiabloUtils.instance().hiddenKeyboard(getContext());
        // get data from from web server
        final StockNoteRequest request = new StockNoteRequest(mCurrentPage, DiabloEnum.DEFAULT_ITEMS_PER_PAGE);
        request.setStartTime(mDatePicker.startTime());
        request.setEndTime(mDatePicker.endTime());

        if (null != mStockStyleNumberFilter.getSelect()) {
            Object select =  mStockStyleNumberFilter.getSelect();
            request.addStyleNumber( ((MatchStock) select).getStyleNumber() );
        }

        for (DiabloFilter filter: mFilterController.getEntityFilters()) {
            Object select = filter.getSelect();

            if (null != select) {
                if (filter instanceof StockStyleNumberFilter) {
                    request.addStyleNumber( ((MatchStock) select).getStyleNumber() );
                }
                else if (filter instanceof BrandFilter) {
                    request.addBrand( ((DiabloBrand)select).getId() );
                }
                else if (filter instanceof GoodTypeFilter) {
                    request.addGoodType( ((DiabloType)select).getId() );
                }
                else if (filter instanceof FirmFilter) {
                    request.addFirm( ((Firm)select).getId() );
                }
                else if (filter instanceof YearFilter) {
                    request.addYear( (String) select );
                }
                else if (filter instanceof ShopFilter) {
                    request.addShop( ((DiabloShop)select).getShop() );
                }
            }
        }

        if (0 == request.getShops().size()) {
            request.setShops(Profile.instance().getShopIds());
        }

        request.trim();

        Call<StockNoteResponse> call = mStockRest.filterStockNote(Profile.instance().getToken(), request);

        call.enqueue(new Callback<StockNoteResponse>() {
            @Override
            public void onResponse(Call<StockNoteResponse> call, Response<StockNoteResponse> response) {
                Log.d("SALE_DETAIL %s", response.toString());

                mSaleNoteTableSwipe.setRefreshing(false);
                mRefreshDialog.dismiss();

                StockNoteResponse base = response.body();
                if (DiabloEnum.DEFAULT_PAGE.equals(mCurrentPage) && 0 != base.getTotal()){
                    mTotalPage = UTILS.calcTotalPage(base.getTotal(), request.getCount());

                    Resources res = getResources();
                    mStatistic =
                        res.getString(amount)
                            + res.getString(R.string.colon)
                            + UTILS.toString(base.getAmount());

                    // pagination
                    mPagination = mCurrentPage.toString() + "/" + mTotalPage.toString();
//                    mPagination = getResources().getString(R.string.current_page) + mCurrentPage.toString()
//                        + getResources().getString(R.string.page)
//                        + getResources().getString(R.string.space_4)
//                        + getResources().getString(R.string.total_page) + mTotalPage.toString()
//                        + getResources().getString(R.string.page);
                }

                List<StockNoteResponse.StockNote> notes = base.getStockNotes();
                Integer orderId = request.getPageStartIndex();
                // mSaleDetailTable.removeAllViews();
                mSaleNoteTable.removeAllViews();
                TableRow row = null;
                for (Integer i=0; i<notes.size(); i++){
                    row = new TableRow(getContext());
                    // TableRow row = new TableRow(mContext);
                    // mSaleDetailTable.addView(row);
                    // row.removeAllViews();
                    StockNoteResponse.StockNote note = notes.get(i);
                    TextView cell = null;
                    for (String title: mTableHeads){
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);
                        if (i == notes.size() - 1) {
                            lp.setMargins(0, 1, 0, 1);
                        } else {
                            lp.setMargins(0, 1, 0, 0);
                        }

                        if (getResources().getString(R.string.order_id).equals(title)) {
                            note.setOrderId(orderId);
                            cell = addCell(row, orderId++, lp);
                        }
                        else if (getResources().getString(R.string.rsn).equals(title)){
                            String [] fs = note.getRsn().split("-");
                            cell = addCell(row, fs[fs.length - 1], lp);
                        }
                        else if(getResources().getString(R.string.trans).equals(title)){
                            cell = addCell(row, mStockTypes[note.getType()], lp);
                            if (note.getType().equals(DiabloEnum.STOCK_OUT)){
                                cell.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                            }
                        }
                        else if (getContext().getString(R.string.firm).equals(title)){
                            cell = addCell(row, Profile.instance().getFirm(note.getFirmId()).getName(), lp);
                        }
                        else if (getContext().getString(R.string.style_number).equals(title)){
                            cell = addCell(row, note.getStyleNumber(), lp);
                        }
                        else if (getContext().getString(R.string.brand).equals(title)){
                            DiabloBrand brand = Profile.instance().getBrand(note.getBrandId());
                            if (null != brand) {
                                cell = addCell(row, brand.getName(), lp);
                            }
                            else {
                                cell = addCell(row, DiabloEnum.EMPTY_STRING, lp);
                            }

                        }
                        else if (getContext().getString(R.string.good_type).equals(title)){
                            DiabloType type = Profile.instance().getDiabloType(note.getTypeId());
                            if (null != type) {
                                cell = addCell(row, type.getName(), lp);
                            }
                            else {
                                cell = addCell(row, DiabloEnum.EMPTY_STRING, lp);
                            }
                        }
                        else if (getContext().getString(R.string.year).equals(title)) {
                            cell = addCell(row, note.getYear(), lp);
                        }
                        else if (getContext().getString(R.string.discount).equals(title)){
                            cell = addCell(row, note.getDiscount(), lp);
                        }
                        else if (getContext().getString(amount).equals(title)){
                            cell = addCell(row, note.getAmount(), lp);
                        }
                        else if (getResources().getString(R.string.date).equals(title)){
                            String shortDate = note.getDatetime().substring(
                                5, note.getDatetime().length() - 3).trim();
                            cell = addCell(row, shortDate, lp);
                        }

                        if (null != cell ) {
                            // cell.setTextColor(Color.BLACK);
                            cell.setGravity(Gravity.CENTER);
                            cell.setBackgroundResource(R.drawable.table_cell_bg);
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
                    UTILS.formatTableStatistic(addCell(row, mStatistic, UTILS.createTableRowParams(2.0f)));
                    UTILS.formatPageInfo(addCell(row, mPagination, UTILS.createTableRowParams(0.5f)));
                    mSaleNoteTable.addView(row);
                }
            }

            @Override
            public void onFailure(Call<StockNoteResponse> call, Throwable t) {
                UTILS.makeToast(getContext(), DiabloError.getError(99), Toast.LENGTH_LONG);
                mSaleNoteTableSwipe.setRefreshing(false);
                mRefreshDialog.dismiss();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();

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
//            case R.id.stock_note_to_stock_detail:
//                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_STOCK_DETAIL);
//                break;
            case R.id.sale_note_to_detail:
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_STOCK_STORE_DETAIL);
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
            StockUtils.getStockNoteDetailFromServer(
                getContext(),
                new StockNoteDetailRequest(detail.getRsn(), detail.getStyleNumber(), detail.getBrandId()),
                new StockUtils.OnGetStockNoteDetailFormSeverListener() {
                @Override
                public void afterGet(final StockNoteDetailResponse response) {
                    List<DiabloColor> colors = new ArrayList<>();

                    for(StockNoteDetailResponse.StockNoteDetail s: response.getStockNoteDetails()){
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
                                StockNoteDetailResponse.StockNoteDetail note = response.getStockNoteDetail(color, size);
                                if (null != note) {
                                    return note.getTotal();
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
