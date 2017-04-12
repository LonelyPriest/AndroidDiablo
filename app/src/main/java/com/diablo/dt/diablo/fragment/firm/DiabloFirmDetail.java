package com.diablo.dt.diablo.fragment.firm;


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
import com.diablo.dt.diablo.client.FirmClient;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.rest.FirmInterface;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiabloFirmDetail extends Fragment {
    private final static String LOG_TAG = "RetailerDetail:";
    private final static DiabloUtils UTILS = DiabloUtils.instance();

    private String [] mTableTitles;
    private TableLayout mFirmDetailTable;
    private SwipyRefreshLayout mTableSwipe;
    private Dialog mRefreshDialog;

    private List<Firm> mFirms;
    private Integer mCurrentPage;
    private Integer mTotalPage;
    private Integer mItemsPerPage;

    public DiabloFirmDetail() {
        // Required empty public constructor
    }

    public static DiabloFirmDetail newInstance(String param1, String param2) {
        DiabloFirmDetail fragment = new DiabloFirmDetail();
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
        mTableTitles = getResources().getStringArray(R.array.thead_firm_detail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diablo_firm_detail, container, false);

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

        TableLayout tableHead = (TableLayout) view.findViewById(R.id.t_firm_detail_head);
        tableHead.addView(row);
        mFirmDetailTable = (TableLayout) view.findViewById(R.id.t_firm_detail);

        mTableSwipe = (SwipyRefreshLayout) view.findViewById(R.id.t_firm_detail_swipe);
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
        mFirms = Profile.instance().getFirms();

        mCurrentPage = DiabloEnum.DEFAULT_PAGE;
        mItemsPerPage = DiabloEnum.DEFAULT_ITEMS_PER_PAGE;
        mTotalPage = UTILS.calcTotalPage(mFirms.size(), mItemsPerPage);

        pageChanged();
    }

    private void pageChanged() {
        mFirmDetailTable.removeAllViews();
        mTableSwipe.setRefreshing(false);

        Integer startIndex =  (mCurrentPage - 1) * mItemsPerPage;
        Integer orderId = startIndex + 1;
        TableRow row = null;

        for (int i=startIndex; i<mCurrentPage * mItemsPerPage && i<mFirms.size(); i++ ) {
            Firm f = mFirms.get(i);

            row = new TableRow(getContext());
            mFirmDetailTable.addView(row);

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
                    TextView cell = UTILS.addCell(getContext(), row, f.getName(), lp);
                    cell.setGravity(Gravity.CENTER);
                }
                else if (getString(R.string.diablo_arrears).equals(title)) {
                    TextView cell = UTILS.addCell(getContext(), row, f.getBalance(), lp);
                    cell.setGravity(Gravity.CENTER);
                }
                else if (getString(R.string.diablo_phone).equals(title)) {
                    TextView cell = UTILS.addCell(getContext(), row, f.getMobile(), lp);
                    cell.setGravity(Gravity.CENTER);
                }
                else if (getString(R.string.diablo_address).equals(title)) {
                    TextView cell = UTILS.addCell(getContext(), row, f.getAddress(), lp);
                    cell.setGravity(Gravity.CENTER);
                }
                else if (getString(R.string.diablo_datetime).equals(title)) {
                    TextView cell = UTILS.addCell(getContext(), row, f.getDatetime(), lp);
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
            mFirmDetailTable.addView(row);
        }
    }

    public void refresh() {
        mRefreshDialog.show();
        getFirms();
    }

    private void getFirms(){
        FirmInterface face = FirmClient.getClient().create(FirmInterface.class);
        Call<List<Firm>> call = face.listFirm(Profile.instance().getToken());
        call.enqueue(new Callback<List<Firm>>() {
            @Override
            public void onResponse(Call<List<Firm>> call, Response<List<Firm>> response) {
                Log.d(LOG_TAG, "success to get firm");
                Profile.instance().clearFirms();
                Profile.instance().setFirms(response.body());
                init();
                mRefreshDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Firm>> call, Throwable t) {
                Log.d(LOG_TAG, "failed to get firm");
                UTILS.makeToast(getContext(), getString(R.string.failed_to_fetch_firms), Toast.LENGTH_LONG);
            }
        });
    }
}
