package com.diablo.dt.diablo.fragment.retailer;


import com.google.gson.Gson;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.BackendRetailerAdapter;
import com.diablo.dt.diablo.client.RetailerClient;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.rest.RetailerInterface;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloPattern;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiabloRetailerDetail extends Fragment {
    private final static String LOG_TAG = "RetailerDetail:";
    private final static DiabloUtils UTILS = DiabloUtils.instance();

    private String [] mTableTitles;
    private TableLayout mRetailerDetailTable;
    private SwipyRefreshLayout mTableSwipe;
    private Dialog mRefreshDialog;

    // private List<Retailer> mRetailers;
    private Integer mCurrentPage;
    private Integer mTotalPage;
    private Integer mItemsPerPage;

    private AutoCompleteTextView mAutoCompleteSearch;
    private Button mBtnSearch;
    private List<Retailer> mMatchedRetailers;

    private Handler mHandler;

    private TableRow mCurrentSelectedRow;



    public DiabloRetailerDetail() {
        // Required empty public constructor
    }

    public static DiabloRetailerDetail newInstance(String param1, String param2) {
        DiabloRetailerDetail fragment = new DiabloRetailerDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        mMatchedRetailers = new ArrayList<>(Profile.instance().getRetailers());
        mRefreshDialog = UTILS.createLoadingDialog(getContext());
        mTableTitles = getResources().getStringArray(R.array.thead_retailer_detail);

        mHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diablo_retailer, container, false);

        mAutoCompleteSearch = (AutoCompleteTextView) view.findViewById(R.id.search_content);
        mBtnSearch = (Button) view.findViewById(R.id.btn_search);

        TableRow row = new TableRow(this.getContext());
        for (String title: mTableTitles){
            TableRow.LayoutParams lp = UTILS.createTableRowParams(1f);
            TextView cell = new TextView(this.getContext());
            cell.setTypeface(null, Typeface.BOLD);
            cell.setTextColor(Color.BLACK);

            if (getResources().getString(R.string.order_id).equals(title)) {
                lp.weight = 0.5f;
            }
            cell.setLayoutParams(lp);
            cell.setText(title);
            cell.setTextSize(20);
            cell.setGravity(Gravity.CENTER);
            row.addView(cell);
        }

        TableLayout tableHead = (TableLayout) view.findViewById(R.id.t_retailer_detail_head);
        tableHead.addView(row);
        mRetailerDetailTable = (TableLayout) view.findViewById(R.id.t_retailer_detail);

        mTableSwipe = (SwipyRefreshLayout) view.findViewById(R.id.t_retailer_detail_swipe);
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

        initSearch();
        init();
        return view;
    }

    public void init() {
        mCurrentPage = DiabloEnum.DEFAULT_PAGE;
        mItemsPerPage = DiabloEnum.DEFAULT_ITEMS_PER_PAGE;

        mTotalPage = UTILS.calcTotalPage(mMatchedRetailers.size(), mItemsPerPage);

        pageChanged();
    }

    public void initSearch() {
        new BackendRetailerAdapter(
            getContext(),
            R.layout.typeahead_retailer,
            R.id.typeahead_select_retailer, mAutoCompleteSearch);

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Retailer selectRetailer = (Retailer) adapterView.getItemAtPosition(i);
                mMatchedRetailers = new ArrayList<>();
                mMatchedRetailers.add(selectRetailer);
                init();
            }
        };

        mAutoCompleteSearch.setOnItemClickListener(listener);

        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String searchText = mAutoCompleteSearch.getText().toString().trim().toUpperCase();
                final boolean isNotStartWithNumber = DiabloPattern.isNotStartWithNumber(searchText);
                mMatchedRetailers.clear();

                Runnable mPendingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        for (Retailer r: Profile.instance().getRetailers()) {
                            if (isNotStartWithNumber) {
                                if (r.getName().toUpperCase().contains(searchText)) {
                                    mMatchedRetailers.add(r);
                                }
                            } else {
                                if (r.getMobile().contains(searchText)) {
                                    mMatchedRetailers.add(r);
                                }
                            }
                        }

                        init();
                    }
                };
                if (searchText.length() != 0) {
                    mHandler.post(mPendingRunnable);
                }
            }
        });
    }

    private void pageChanged() {
        mRetailerDetailTable.removeAllViews();
        mTableSwipe.setRefreshing(false);

        Integer startIndex =  (mCurrentPage - 1) * mItemsPerPage;
        Integer orderId = startIndex + 1;
        TableRow row = null;

        for (int i=startIndex; i<mCurrentPage * mItemsPerPage && i<mMatchedRetailers.size(); i++ ) {
            Retailer r = mMatchedRetailers.get(i);

            row = new TableRow(getContext());
            row.setTag(r);
            registerForContextMenu(row);
            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    v.showContextMenu();
                    return true;
                }
            });

            mRetailerDetailTable.addView(row);

            for (String title: mTableTitles) {
                TableRow.LayoutParams lp = UTILS.createTableRowParams(1f);
                TextView cell = null;
                if (getString(R.string.order_id).equals(title)) {
                    lp.weight = 0.5f;
                    cell = UTILS.addCell(getContext(), row, orderId++, lp);
                    if (cell.getLineHeight() < 100) {
                        cell.setHeight(100);
                    }
                    cell.setTextColor(ContextCompat.getColor(getContext(), R.color.bpDarker_red));
                }
                else if (getString(R.string.diablo_name).equals(title)) {
                    cell = UTILS.addCell(getContext(), row, r.getName(), lp);
                }
                else if (getString(R.string.diablo_arrears).equals(title)) {
                    cell = UTILS.addCell(getContext(), row, r.getBalance(), lp);
                }
                else if (getString(R.string.diablo_phone).equals(title)) {
                    String mobile = null != r.getMobile() ? r.getMobile() : DiabloEnum.EMPTY_STRING;
                    cell = UTILS.addCell(getContext(), row, mobile, lp);
                }
                else if (getString(R.string.diablo_address).equals(title)) {
                    String address = null != r.getAddress() ? r.getAddress() : DiabloEnum.EMPTY_STRING;
                    cell = UTILS.addCell(getContext(), row, address, lp);
                }
                else if (getString(R.string.diablo_datetime).equals(title)) {
                    String datetime = null != r.getEntryDate() ? r.getEntryDate() : DiabloEnum.EMPTY_STRING;
                    cell = UTILS.addCell(getContext(), row, datetime, lp);
                }

                if (null != cell) {
                    cell.setGravity(Gravity.CENTER);
                    cell.setTag(title);
                }
            }
            row.setBackgroundResource(R.drawable.table_row_bg);
        }

        if (null != row) {
            row.setBackgroundResource(R.drawable.table_row_last_bg);
        }

        if (0 < mTotalPage ) {
            row = new TableRow(getContext());

            String pageInfo = getResources().getString(R.string.current_page) + mCurrentPage.toString()
                + getResources().getString(R.string.page)
                + getResources().getString(R.string.space_4)
                + getResources().getString(R.string.total_page) + mTotalPage.toString()
                + getResources().getString(R.string.page);

            TextView cell = UTILS.addCell(getContext(), row, pageInfo, UTILS.createTableRowParams(1f));
            UTILS.formatPageInfo(cell);
            mRetailerDetailTable.addView(row);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.add(Menu.NONE, 401, Menu.NONE, getResources().getString(R.string.btn_add))
            .setIcon(R.drawable.ic_add_black_24dp)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add(Menu.NONE, 402, Menu.NONE, getResources().getString(R.string.btn_refresh))
            .setIcon(R.drawable.ic_refresh_black_24dp)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 401: // add
                DiabloUtils.switchToFrame(
                    this,
                    "com.diablo.dt.diablo.fragment.retailer.DiabloRetailerNew",
                    DiabloEnum.TAG_RETAILER_NEW);
                break;
            case 402:
                refresh();
                break;
            default:
                break;
        }
        return true;
    }

    private void refresh() {
        mRefreshDialog.show();
        getRetailers();
    }

    private void getRetailers(){
        RetailerInterface face = RetailerClient.getClient().create(RetailerInterface.class);
        Call<List<Retailer>> call = face.listRetailer(Profile.instance().getToken());
        call.enqueue(new Callback<List<Retailer>>() {
            @Override
            public void onResponse(Call<List<Retailer>> call, Response<List<Retailer>> response) {
                Log.d(LOG_TAG, "success to get retailer");
                Profile.instance().clearRetailers();
                Profile.instance().setRetailers(response.body());
                mMatchedRetailers = response.body();
                init();
                mRefreshDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Retailer>> call, Throwable t) {
                Log.d(LOG_TAG, "failed to get retailer");
                UTILS.makeToast(getContext(), getString(R.string.failed_to_fetch_retailers), Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        mCurrentSelectedRow = (TableRow) v;
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_on_retailer_detail, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final Retailer retailer = (Retailer) mCurrentSelectedRow.getTag();

        if (getResources().getString(R.string.modify) == item.getTitle()){
            switchToUpdate(retailer);
        }

        return true;
    }

    private void switchToUpdate(Retailer retailer) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // find
        Fragment to = getFragmentManager().findFragmentByTag(DiabloEnum.TAG_RETAILER_UPDATE);

        if (null == to){
            Bundle args = new Bundle();
            args.putString(DiabloEnum.BUNDLE_PARAM_RETAILER, new Gson().toJson(retailer));

            to = new DiabloRetailerUpdate();
            to.setArguments(args);
        } else {
            ((DiabloRetailerUpdate)to).setUpdateRetailer(new Gson().toJson(retailer));
        }

        ((DiabloRetailerUpdate)to).setRetailerUpdateListener(new OnRetailerDetailListener() {
            @Override
            public void onUpdate(Retailer updatedRetailer) {
                // init();
                replaceCurrentSelectRow(updatedRetailer);
            }

            @Override
            public void onAdd() {

            }
        });

        if (!to.isAdded()){
            transaction.hide(this).add(R.id.frame_container, to, DiabloEnum.TAG_RETAILER_UPDATE).commit();
        } else {
            transaction.hide(this).show(to).commit();
        }
    }

    private void replaceCurrentSelectRow(Retailer updateRetailer) {
        for (int i=0; i<mCurrentSelectedRow.getChildCount(); i++) {
            TextView cell = (TextView) mCurrentSelectedRow.getChildAt(i);
            String tag = (String) cell.getTag();
            Retailer oldRetailer = (Retailer) mCurrentSelectedRow.getTag();
            if (tag.equals(getString(R.string.diablo_name))) {
                if (!oldRetailer.getName().equals(updateRetailer.getName())) {
                    cell.setText(updateRetailer.getName());
                }
            }
            else if (tag.equals(getString(R.string.diablo_arrears))) {
                if (!oldRetailer.getBalance().equals(updateRetailer.getBalance())) {
                    cell.setText(UTILS.toString(updateRetailer.getBalance()));
                }

            }
            else if (tag.equals(getString(R.string.diablo_phone))) {
                if (null == oldRetailer.getMobile()
                    || !oldRetailer.getMobile().equals(updateRetailer.getMobile())) {
                    cell.setText(updateRetailer.getMobile());
                }
            }
            else if (tag.equals(getString(R.string.diablo_address))) {
                if (null == oldRetailer.getAddress()
                    || !oldRetailer.getAddress().equals(updateRetailer.getAddress())) {
                    cell.setText(updateRetailer.getAddress());
                }
            }
        }

        mCurrentSelectedRow.setTag(updateRetailer);
    }

    public interface OnRetailerDetailListener {
        void onAdd();
        void onUpdate(Retailer updatedRetailer);
    }
}
