package com.diablo.dt.diablo.fragment.retailer;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.RetailerClient;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.rest.RetailerInterface;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

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

    private List<Retailer> mRetailers;
    private Integer mCurrentPage;
    private Integer mTotalPage;
    private Integer mItemsPerPage;

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

        mRefreshDialog = UTILS.createLoadingDialog(getContext());
        mTableTitles = getResources().getStringArray(R.array.thead_retailer_detail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diablo_retailer, container, false);

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

        init();
        return view;
    }

    public void init() {
        mRetailers = Profile.instance().getRetailers();

        mCurrentPage = DiabloEnum.DEFAULT_PAGE;
        mItemsPerPage = DiabloEnum.DEFAULT_ITEMS_PER_PAGE;
        mTotalPage = UTILS.calcTotalPage(mRetailers.size(), mItemsPerPage);

        pageChanged();
    }

    private void pageChanged() {
        mRetailerDetailTable.removeAllViews();
        mTableSwipe.setRefreshing(false);

        Integer startIndex =  (mCurrentPage - 1) * mItemsPerPage;
        Integer orderId = startIndex + 1;
        TableRow row = null;

        for (int i=startIndex; i<mCurrentPage * mItemsPerPage && i<mRetailers.size(); i++ ) {
            Retailer r = mRetailers.get(i);

            row = new TableRow(getContext());
            mRetailerDetailTable.addView(row);

            for (String title: mTableTitles) {
                TableRow.LayoutParams lp = UTILS.createTableRowParams(1f);

                if (getString(R.string.order_id).equals(title)) {
                    lp.weight = 0.5f;
                    TextView cell = UTILS.addCell(getContext(), row, orderId++, lp);
                    if (cell.getLineHeight() < 100) {
                        cell.setHeight(100);
                    }
                    cell.setTextColor(ContextCompat.getColor(getContext(), R.color.bpDarker_red));
                    cell.setGravity(Gravity.CENTER);
                }
                else if (getString(R.string.diablo_name).equals(title)) {
                    TextView cell = UTILS.addCell(getContext(), row, r.getName(), lp);
                    cell.setGravity(Gravity.CENTER);
                }
                else if (getString(R.string.diablo_arrears).equals(title)) {
                    TextView cell = UTILS.addCell(getContext(), row, r.getBalance(), lp);
                    cell.setGravity(Gravity.CENTER);
                }
                else if (getString(R.string.diablo_phone).equals(title)) {
                    TextView cell = UTILS.addCell(getContext(), row, r.getMobile(), lp);
                    cell.setGravity(Gravity.CENTER);
                }
                else if (getString(R.string.diablo_address).equals(title)) {
                    TextView cell = UTILS.addCell(getContext(), row, r.getAddress(), lp);
                    cell.setGravity(Gravity.CENTER);
                }
                else if (getString(R.string.diablo_datetime).equals(title)) {
                    TextView cell = UTILS.addCell(getContext(), row, r.getEntryDate(), lp);
                    cell.setGravity(Gravity.CENTER);
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

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        // menu.clear();
//
//        menu.add(Menu.NONE, 400, Menu.NONE, getResources().getString(R.string.btn_sale_in))
//            .setIcon(R.drawable.ic_add_shopping_cart_black_24dp)
//            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//
//        menu.add(Menu.NONE, 401, Menu.NONE, getResources().getString(R.string.btn_add))
//            .setIcon(R.drawable.ic_add_black_24dp)
//            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//
//        menu.add(Menu.NONE, 402, Menu.NONE, getResources().getString(R.string.btn_refresh))
//            .setIcon(R.drawable.ic_refresh_black_24dp)
//            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case 400: // add
//                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_SALE_IN);
//                break;
//            case 401:
//                DiabloRetailerPager pager = (DiabloRetailerPager) getFragmentManager()
//                    .findFragmentByTag(DiabloEnum.TAG_RETAILER_PAGER);
//                if (null != pager) {
//                    pager.setPager(1);
//                }
//                break;
//            case 402:
//                mRefreshDialog.show();
//                getRetailers();
//                break;
//            default:
//                break;
//        }
//        return true;
//    }

    public void refresh() {
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

}
