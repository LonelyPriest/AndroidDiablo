package com.diablo.dt.diablo.fragment.sale;


import static com.diablo.dt.diablo.fragment.good.GoodNew.UTILS;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
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
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.WSaleClient;
import com.diablo.dt.diablo.entity.DiabloBrand;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.DiabloType;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.model.sale.SaleUtils;
import com.diablo.dt.diablo.request.sale.SaleNoteRequest;
import com.diablo.dt.diablo.response.sale.GetSaleNewResponse;
import com.diablo.dt.diablo.response.sale.SaleNoteResponse;
import com.diablo.dt.diablo.rest.WSaleInterface;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class SaleDetailToNote extends Fragment {
    private String [] mTableHeads;
    private String[]  mSaleTypes;
    private String mRSN;
    private String mLastRSN;

    private SwipyRefreshLayout mSaleNoteTableSwipe;
    private android.widget.TableLayout mSaleNoteTable;

    private Dialog mRefreshDialog;
    private TableRow mCurrentSelectedRow;

    private Integer mCurrentPage;
    private Integer mTotalPage;

    private String mStatistic;
    private Integer mComesFrom;

    /**
     * request
     */
    private WSaleInterface mSaleRest;

    public SaleDetailToNote() {
        // Required empty public constructor
    }

    public static SaleDetailToNote newInstance(String param1, String param2) {
        SaleDetailToNote fragment = new SaleDetailToNote();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setRSN(String rsn) {
        this.mRSN = rsn;
    }
    public void setComesFrom(Integer comesFrom) {
        this.mComesFrom = comesFrom;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // mComesFrom = R.string.back_from_unknown;
        if (null != getArguments()) {
            mRSN = getArguments().getString(DiabloEnum.BUNDLE_PARAM_RSN);
            mComesFrom = getArguments().getInt(DiabloEnum.BUNDLE_PARAM_COME_FORM, R.string.back_from_unknown);
        }

        mTableHeads = getResources().getStringArray(R.array.thead_sale_detail_note);
        mSaleTypes  = getResources().getStringArray(R.array.sale_type);
        mSaleRest   = WSaleClient.getClient().create(WSaleInterface.class);
        mRefreshDialog = UTILS.createLoadingDialog(getContext());

        initTitle();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale_detail_to_note, container, false);

        mSaleNoteTableSwipe = (SwipyRefreshLayout) view.findViewById(R.id.t_sale_note_swipe);
        // mSaleDetailTableSwipe = (DiabloTableSwipeRefreshLayout) view.findViewById(R.id.t_sale_detail_swipe);
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

        TableRow row = new TableRow(this.getContext());
        for (String title: mTableHeads){
            TableRow.LayoutParams lp = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            TextView cell = new TextView(this.getContext());
            cell.setTypeface(null, Typeface.BOLD);
            cell.setTextColor(Color.BLACK);

            cell.setLayoutParams(lp);
            cell.setText(title);
            cell.setTextSize(20);

            row.addView(cell);
        }

        TableLayout head = (TableLayout) view.findViewById(R.id.t_sale_note_head);
        head.addView(row);

        mSaleNoteTable = (TableLayout) view.findViewById(R.id.t_sale_note);
        mSaleNoteTable.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        init();
        pageChanged();
        return view;
    }

    public void init() {
        mCurrentPage = DiabloEnum.DEFAULT_PAGE;
        mTotalPage = 0;
    }

    private void pageChanged(){
        final SaleNoteRequest request = new SaleNoteRequest(mCurrentPage, DiabloEnum.DEFAULT_ITEMS_PER_PAGE);
        request.addRSN(mRSN);
        request.trim();
        Call<SaleNoteResponse> call = mSaleRest.filterSaleNote(Profile.instance().getToken(), request);

        call.enqueue(new Callback<SaleNoteResponse>() {
            @Override
            public void onResponse(Call<SaleNoteResponse> call, Response<SaleNoteResponse> response) {
                mLastRSN = mRSN;
                mSaleNoteTableSwipe.setRefreshing(false);
                mRefreshDialog.dismiss();

                SaleNoteResponse base = response.body();
                if (0 != base.getTotal()) {
                    if (DiabloEnum.DEFAULT_PAGE.equals(mCurrentPage) && 0 != base.getTotal()){
                        mTotalPage = UTILS.calcTotalPage(base.getTotal(), request.getCount());

                        Resources res = getResources();
                        mStatistic =
                            res.getString(R.string.amount) + res.getString(R.string.colon) + UTILS.toString(base.getAmount())
                                + res.getString(R.string.space_4)
                                + res.getString(R.string.calculate) + res.getString(R.string.colon)
                                + UTILS.toString(base.getBalance() * 0.01f);
                    }
                }

                List<SaleNoteResponse.SaleNote> notes = base.getSaleNotes();
                Integer orderId = request.getPageStartIndex();
                mSaleNoteTable.removeAllViews();
                TableRow row = null;
                for (Integer i=0; i<notes.size(); i++){
                    row = new TableRow(getContext());

                    SaleNoteResponse.SaleNote note = notes.get(i);
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
                            cell = UTILS.addCell(getContext(), row, orderId++, lp);
                        }
                        else if (getResources().getString(R.string.rsn).equals(title)){
                            String [] fs = note.getRsn().split("-");
                            cell = UTILS.addCell(getContext(), row, fs[fs.length - 1], lp);
                        }
                        else if(getResources().getString(R.string.trans).equals(title)){
                            cell = UTILS.addCell(getContext(), row, mSaleTypes[note.getSellType()], lp);
                            if (note.getSellType().equals(DiabloEnum.SALE_OUT)){
                                cell.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                            }
                        }
                        else if (getContext().getString(R.string.retailer).equals(title)){
                            Retailer r = DiabloUtils.getInstance().getRetailer(
                                Profile.instance().getRetailers(), note.getRetailerId());

                            if (null != r) {
                                cell = UTILS.addCell(getContext(), row, r.getName(), lp);
                                cell.setTextColor(ContextCompat.getColor(getContext(), R.color.bpDarker_blue));
                            }
                            else {
                                cell = UTILS.addCell(getContext(), row, DiabloEnum.EMPTY_STRING, lp);
                            }
                        }
                        else if (getContext().getString(R.string.style_number).equals(title)){
                            cell = UTILS.addCell(getContext(), row, note.getStyleNumber(), lp);
                            cell.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                        }
                        else if (getContext().getString(R.string.brand).equals(title)){
                            DiabloBrand brand = Profile.instance().getBrand(note.getBrandId());
                            if (null != brand) {
                                cell = UTILS.addCell(getContext(), row, brand.getName(), lp);
                            }
                            else {
                                cell = UTILS.addCell(getContext(), row, DiabloEnum.EMPTY_STRING, lp);
                            }

                        }
                        else if (getContext().getString(R.string.good_type).equals(title)){
                            DiabloType type = Profile.instance().getDiabloType(note.getTypeId());
                            if (null != type) {
                                cell = UTILS.addCell(getContext(), row, type.getName(), lp);
                            }
                            else {
                                cell = UTILS.addCell(getContext(), row, DiabloEnum.EMPTY_STRING, lp);
                            }
                        }
                        else if (getContext().getString(R.string.firm).equals(title)){
                            Firm firm = Profile.instance().getFirm(note.getFirmId());
                            if (null != firm) {
                                cell = UTILS.addCell(getContext(), row, firm.getName(), lp);
                            } else {
                                cell = UTILS.addCell(getContext(), row, DiabloEnum.EMPTY_STRING, lp);
                            }

                        }
                        else if (getContext().getString(R.string.price).equals(title)){
                            cell = UTILS.addCell(getContext(), row, note.getFprice(), lp);
                        }
                        else if (getContext().getString(R.string.discount).equals(title)){
                            cell = UTILS.addCell(getContext(), row, note.getFdiscount(), lp);
                        }
                        else if (getContext().getString(R.string.amount).equals(title)){
                            cell = UTILS.addCell(getContext(), row, note.getTotal(), lp);
                            if (0 > note.getTotal()) {
                                row.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.pinkLight));
                            }
                        }
                        else if (getContext().getString(R.string.calculate).equals(title)){
                            cell = UTILS.addCell(getContext(), row, note.getFdiscount() * note.getFprice() * 0.01f, lp);
                        }
                        else if (getResources().getString(R.string.date).equals(title)){
                            String shortDate = note.getDatetime().substring(
                                5, note.getDatetime().length() - 3).trim();
                            cell = UTILS.addCell(getContext(), row, shortDate, lp);
                        }
                        else if(getResources().getString(R.string.shop).equals(title)){
                            cell = UTILS.addCell(
                                getContext(),
                                row,
                                DiabloUtils.getInstance().getShop(Profile.instance().getSortShop(),
                                    note.getShopId()).getName(),
                                lp);
                        }
                        else if (getResources().getString(R.string.employee).equals(title)){
                            cell = UTILS.addCell(
                                getContext(),
                                row,
                                DiabloUtils.getInstance().getEmployeeByNumber(
                                    Profile.instance().getEmployees(),
                                    note.getEmployeeId()).getName(),
                                lp);
                        }

                        if (null != cell ) {
                            cell.setGravity(Gravity.CENTER);
                            cell.setBackgroundResource(R.drawable.table_cell_bg);
                        }
                    }

                    row.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
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
                    TableRow.LayoutParams lp = UTILS.createTableRowParams(2f);
                    UTILS.formatTableStatistic(UTILS.addCell(getContext(), row, mStatistic, lp));
//
//                    String pageInfo = getResources().getString(R.string.current_page) + mCurrentPage.toString()
//                        + getResources().getString(R.string.page)
//                        + getResources().getString(R.string.space_4)
//                        + getResources().getString(R.string.total_page) + mTotalPage.toString()
//                        + getResources().getString(R.string.page);

                    String pageInfo = mCurrentPage.toString() + "/" + mTotalPage.toString();

                    TableRow.LayoutParams lp2 = UTILS.createTableRowParams(0.5f);
                    UTILS.formatPageInfo(UTILS.addCell(getContext(), row, pageInfo, lp2));
                    mSaleNoteTable.addView(row);
                }
            }

            @Override
            public void onFailure(Call<SaleNoteResponse> call, Throwable t) {
                UTILS.makeToast(getContext(), DiabloError.getError(99), Toast.LENGTH_LONG);
                mSaleNoteTableSwipe.setRefreshing(false);
                mRefreshDialog.dismiss();
            }
        });
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
        final SaleNoteResponse.SaleNote detail = ((SaleNoteResponse.SaleNote) mCurrentSelectedRow.getTag());

        if (getResources().getString(R.string.note) == item.getTitle()) {
            SaleUtils.getSaleNewInfoFormServer(getContext(), detail.getRsn(), new SaleUtils.OnGetSaleNewFormSeverListener() {
                @Override
                public void afterGet(final GetSaleNewResponse response) {
                    List<DiabloColor> colors = new ArrayList<>();

                    for(GetSaleNewResponse.SaleNote s: response.getSaleNotes()){
                        DiabloColor color = Profile.instance().getColor(s.getColor());
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
                                GetSaleNewResponse.SaleNote note = response.getSaleNote(color, size);
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
            case R.id.sale_note_to_detail: {
                switch (mComesFrom) {
                    case R.string.come_from_retailer_sale_check:
                        DiabloUtils.switchToFrame(
                            this,
                            "com.diablo.dt.diablo.fragment.retailer.RetailerSaleCheck",
                            DiabloEnum.TAG_RETAILER_CHECK_SALE_TRANS);
                        break;
                    case R.string.come_from_unknown:
                        SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_SALE_DETAIL);
                        break;
                    default:
                        SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_SALE_DETAIL);
                        break;
                }
            }
            default:
                // return super.onOptionsItemSelected(item);
                break;

        }

        return true;
    }

    private void initTitle() {
        String title = getResources().getString(R.string.nav_sale_note);
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (null != bar) {
            bar.setTitle(title);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            initTitle();
            if (!mLastRSN.equals(mRSN)) {
                init();
                pageChanged();
            }
        } else {
            mComesFrom = R.string.back_from_unknown;
        }
    }

}
