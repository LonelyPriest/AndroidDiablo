package com.diablo.dt.diablo.fragment.good;


import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.WGoodClient;
import com.diablo.dt.diablo.entity.DiabloBrand;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.DiabloType;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.entity.MatchGood;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.filter.BrandFilter;
import com.diablo.dt.diablo.filter.DiabloFilter;
import com.diablo.dt.diablo.filter.DiabloFilterController;
import com.diablo.dt.diablo.filter.FirmFilter;
import com.diablo.dt.diablo.filter.GoodStyleNumberFilter;
import com.diablo.dt.diablo.filter.GoodTypeFilter;
import com.diablo.dt.diablo.filter.StockStyleNumberFilter;
import com.diablo.dt.diablo.filter.YearFilter;
import com.diablo.dt.diablo.model.sale.SaleUtils;
import com.diablo.dt.diablo.request.good.GoodDetailRequest;
import com.diablo.dt.diablo.response.good.GoodDetailResponse;
import com.diablo.dt.diablo.rest.WGoodInterface;
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


public class GoodDetail extends Fragment {
    private final static String LOG_TAG = "GoodDetail:";
    private final static DiabloUtils UTILS = DiabloUtils.instance();
    private String [] mTableHeads;
    private String [] mSeasons;

    private WGoodInterface mGoodRest;

    private TableLayout mTable;
    private SwipyRefreshLayout mTableSwipe;
    private Dialog mRefreshDialog;

    private TableRow mCurrentSelectedRow;

    private Integer mCurrentPage;
    private Integer mTotalPage;

    private DiabloDatePicker mDatePicker;
    private GoodStyleNumberFilter mGoodStyleNumberFilter;
    private DiabloFilterController mFilterController;

    public GoodDetail() {
        // Required empty public constructor
    }

    public static GoodDetail newInstance(String param1, String param2) {
        GoodDetail fragment = new GoodDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // mTableHeads =
        List<String> titles = new ArrayList<>();
        String showDiscount = Profile.instance().getConfig(
            DiabloEnum.START_SHOW_DISCOUNT,
            DiabloEnum.DIABLO_YES);

        for (String title: getResources().getStringArray(R.array.thead_good_detail)) {
            if (title.equals(getString(R.string.discount))
                && !showDiscount.equals(DiabloEnum.DIABLO_YES)) {
                continue;
            }
            titles.add(title);
        }

        mTableHeads = titles.toArray(new String[titles.size()]);

        mSeasons = getResources().getStringArray(R.array.seasons);

        mGoodRest = WGoodClient.getClient().create(WGoodInterface.class);
        mRefreshDialog = UTILS.createLoadingDialog(getContext());
    }

    public void init() {
        mCurrentPage = DiabloEnum.DEFAULT_PAGE;
        mTotalPage = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_good_detail, container, false);

        // support action bar
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        mTableSwipe = (SwipyRefreshLayout) view.findViewById(R.id.t_good_detail_swipe);
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
            else if (getResources().getString(R.string.shelfDate).equals(title)) {
                lp.weight = 0.8f;
            }

            cell.setLayoutParams(lp);
            cell.setText(title);
            cell.setTextSize(20);

            row.addView(cell);
        }

        TableLayout head = (TableLayout) view.findViewById(R.id.t_good_detail_head);
        head.addView(row);

        mTable = (TableLayout) view.findViewById(R.id.t_good_detail);

        init();
        initFilter(view);
        pageChanged();

        return view;
    }

    private void initFilter(View view) {
        mDatePicker = new DiabloDatePicker(
            GoodDetail.this,
            view.findViewById(R.id.btn_start_date),
            view.findViewById(R.id.btn_end_date),
            (EditText) view.findViewById(R.id.text_start_date),
            (EditText)view.findViewById(R.id.text_end_date),
            Profile.instance().getConfig(DiabloEnum.START_TIME, UTILS.currentDate()));


        View styleNumberView = view.findViewById(R.id.select_style_number);
        mGoodStyleNumberFilter = new GoodStyleNumberFilter(getContext(), getString(R.string.style_number));
        mGoodStyleNumberFilter.init(styleNumberView);

        ImageButton btnAdd = (ImageButton) view.findViewById(R.id.btn_add_filter);
        ImageButton btnMinus = (ImageButton) view.findViewById(R.id.btn_minus_filter);
        btnMinus.setEnabled(false);

        List<DiabloFilter> entities = new ArrayList<>();
        entities.add(new GoodStyleNumberFilter(getContext(), getString(R.string.style_number)));
        entities.add(new FirmFilter(getContext(), getString(R.string.firm)));
        entities.add(new BrandFilter(getContext(), getString(R.string.brand)));

        entities.add(new GoodTypeFilter(getContext(), getString(R.string.good_type)));
        entities.add(new YearFilter(getContext(), getString(R.string.year)));

        mFilterController = new DiabloFilterController(getContext(), entities, 2);
        mFilterController.init((LinearLayout)view, R.id.t_good_detail_head, btnAdd, btnMinus);
    }

    private GoodDetailRequest createRequest() {
        final GoodDetailRequest request = new GoodDetailRequest(mCurrentPage, DiabloEnum.DEFAULT_ITEMS_PER_PAGE);
        request.setStartTime(mDatePicker.startTime());
        request.setEndTime(mDatePicker.endTime());

        if (null != mGoodStyleNumberFilter.getSelect()) {
            Object select =  mGoodStyleNumberFilter.getSelect();
            request.addStyleNumber( ((MatchGood) select).getStyleNumber() );
        }

        for (DiabloFilter filter: mFilterController.getEntityFilters()) {
            Object select = filter.getSelect();

            if (null != select) {
                if (filter instanceof StockStyleNumberFilter) {
                    request.addStyleNumber( ((MatchGood) select).getStyleNumber() );
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
            }
        }

        request.trim();

        return request;
    }

    private void pageChanged(){

        final GoodDetailRequest request = createRequest();
        Call<GoodDetailResponse> call = mGoodRest.filterGood(Profile.instance().getToken(), request);

        call.enqueue(new Callback<GoodDetailResponse>() {
            @Override
            public void onResponse(Call<GoodDetailResponse> call, Response<GoodDetailResponse> response) {
                Log.d(LOG_TAG, response.toString());

                mTableSwipe.setRefreshing(false);
                mRefreshDialog.dismiss();
                GoodDetailResponse base = response.body();
                if (0 != base.getTotal()) {
                    if (DiabloEnum.DEFAULT_PAGE.equals(mCurrentPage)) {
                        mTotalPage = UTILS.calcTotalPage(base.getTotal(), request.getCount());
                    }
                }

                List<GoodDetailResponse.GoodNote> goodNotes = base.getGoods();
                Integer orderId = request.getPageStartIndex();
                // mSaleDetailTable.removeAllViews();
                mTable.removeAllViews();

                TableRow row = null;
                for (Integer i=0; i<goodNotes.size(); i++){
                    row = new TableRow(getContext());

                    GoodDetailResponse.GoodNote g = goodNotes.get(i);
                    Resources res = getResources();

                    for (String title: mTableHeads){
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                        if (res.getString(R.string.order_id).equals(title)) {
                            g.setOrderId(orderId);
                            lp.weight = 0.8f;
                            TextView cell = addCell(row, orderId++, lp);
                            cell.setTextColor(ContextCompat.getColor(getContext(), R.color.bpDarker_red));
                        }
                        else if (res.getString(R.string.style_number).equals(title)) {
                            addCell(row, g.getStyleNumber(), lp);
                        }
                        else if (res.getString(R.string.brand).equals(title)) {
                            DiabloBrand brand = Profile.instance().getBrand(g.getBrandId());
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
                            DiabloType type = Profile.instance().getDiabloType(g.getTypeId());
                            if (null != type) {
                                addCell(row, type.getName(), lp);
                            } else {
                                addCell(row, DiabloEnum.EMPTY_STRING, lp);
                            }
                        }
                        else if (res.getString(R.string.firm).equals(title)) {
                            Firm firm = Profile.instance().getFirm(g.getFirmId());
                            if ( null != firm ) {
                                addCell(row, firm.getName(), lp);
                            } else {
                                addCell(row, DiabloEnum.EMPTY_STRING, lp);
                            }
                        }
                        else if (res.getString(R.string.season).equals(title)) {
                            addCell(row, mSeasons[g.getSeason()], lp);
                        }
                        else if (res.getString(R.string.year).equals(title)) {
                            addCell(row, g.getYear(), lp);
                        }
                        else if (res.getString(R.string.org_price).equals(title)) {
                            TextView cell = addCell(row, g.getTagPrice(), lp);
                                cell.setTextColor(ContextCompat.getColor(getContext(), R.color.greenDark));
                        }
                        else if (res.getString(R.string.tag_price).equals(title)) {
                            TextView cell = addCell(row, g.getTagPrice(), lp);
                            if (g.getTagPrice() > 0f) {
                                cell.setTextColor(ContextCompat.getColor(getContext(), R.color.greenDark));
                            }
                        }
                        else if (res.getString(R.string.pkg_price).equals(title)) {
                            TextView cell = addCell(row, g.getPkgPrice(), lp);
                            if (g.getPkgPrice() > 0f) {
                                cell.setTextColor(ContextCompat.getColor(getContext(), R.color.orangeDark));
                            }
                        }
                        else if (res.getString(R.string.shelfDate).equals(title)){
                            lp.weight = 0.8f;
                            addCell(row, g.getDatetime(), lp);
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
                    row.setTag(g);
                    mTable.addView(row);
                }

                if ( null != row) {
                    row.setBackgroundResource(R.drawable.table_row_last_bg);
                }

                if (0 < mTotalPage ) {
                    row = new TableRow(getContext());
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                    // addCell(row, mStatistic, lp);

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
            public void onFailure(Call<GoodDetailResponse> call, Throwable t) {
                UTILS.makeToast(getContext(), DiabloError.getError(99), Toast.LENGTH_LONG);
                mTableSwipe.setRefreshing(false);
                mRefreshDialog.dismiss();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        mCurrentSelectedRow = (TableRow) v;
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_on_good_detail, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        GoodDetailResponse.GoodNote detail = ((GoodDetailResponse.GoodNote) mCurrentSelectedRow.getTag());
        if (getResources().getString(R.string.modify) == item.getTitle()){
            switchToStockUpdateFrame(detail.getId(), this, DiabloEnum.TAG_GOOD_UPDATE);
        }
        else if (getResources().getString(R.string.note) == item.getTitle()) {
            List<DiabloColor> colors = UTILS.stringColorToArray((
                (GoodDetailResponse.GoodNote) mCurrentSelectedRow.getTag()).getColors());

            List<String> sizes = Profile.instance().genSortedSizeNamesByGroups(
                ((GoodDetailResponse.GoodNote) mCurrentSelectedRow.getTag()).getsGroup());

            new DiabloTableStockNote(
                getContext(),
                detail.getStyleNumber(),
                detail.getBrandId(),
                colors,
                sizes).show();
        }

        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();

        inflater.inflate(R.menu.action_on_good_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.good_detail_to_stock_in:
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_STOCK_IN);
                break;
            case R.id.good_detail_to_stock_out:
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_STOCK_OUT);
                break;
            case R.id.good_detail_to_add:
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_GOOD_NEW);
                break;
            case R.id.good_detail_refresh:
                init();
                mRefreshDialog.show();
                pageChanged();
                break;
            default:
                break;
        }

        return true;
    }

    public void switchToStockUpdateFrame(Integer goodId, Fragment from, String tag) {

        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        // find
        Fragment to = from.getFragmentManager().findFragmentByTag(tag);

        if (null == to){
            to = new GoodUpdate();
            Bundle args = new Bundle();
            args.putInt(DiabloEnum.BUNDLE_PARAM_ID, goodId);
            to.setArguments(args);

        } else {
            ((GoodUpdate)to).setGoodId(goodId);
        }

        if (!to.isAdded()){
            transaction.hide(from).add(R.id.frame_container, to, tag).commit();
        } else {
            transaction.hide(from).show(to).commit();
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
