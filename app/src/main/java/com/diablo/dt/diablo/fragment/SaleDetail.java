package com.diablo.dt.diablo.fragment;

import static com.diablo.dt.diablo.utils.DiabloEnum.DEFAULT_PAGE;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.activity.MainActivity;
import com.diablo.dt.diablo.client.WSaleClient;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.request.SaleDetailRequest;
import com.diablo.dt.diablo.response.SaleDetailResponse;
import com.diablo.dt.diablo.rest.WSaleInterface;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SaleDetail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SaleDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaleDetail extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String [] mTableHeads;
    private String[]  mSaleTypes;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /*
    * rest request
    * */
    private SaleDetailRequest mRequest;
    private SaleDetailRequest.Condition mRequestCondition;
    private WSaleInterface mSaleRest;

    /*
    * row of table
    * */
    // TableRow[] mRows;

    private android.widget.TableLayout mSaleDetailTable;
    private  SwipyRefreshLayout mSaleDetailTableSwipe;
    // private DiabloTableSwipeRefreshLayout mSaleDetailTableSwipe;

    private Integer mCurrentPage;
    private Integer mTotalPage;

    public SaleDetail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SaleDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static SaleDetail newInstance(String param1, String param2) {
        SaleDetail fragment = new SaleDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mTableHeads = getResources().getStringArray(R.array.thead_sale_detail);
        mSaleTypes = getResources().getStringArray(R.array.sale_type);

        mCurrentPage = DEFAULT_PAGE;
        mTotalPage = 0;
        mRequest = new SaleDetailRequest(mCurrentPage, DiabloEnum.DEFAULT_ITEMS_PER_PAGE);
        mRequestCondition = mRequest.new Condition();
        mRequest.setCondtion(mRequestCondition);
        mSaleRest = WSaleClient.getClient().create(WSaleInterface.class);

        // mRows = new TableRow[mRequest.getCount()];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale_detail, container, false);

        ((MainActivity)getActivity()).selectMenuItem(2);
//        final DiabloTableSwipRefreshLayout tableSwipe = (DiabloTableSwipRefreshLayout)view.findViewById(R.id.t_sale_detail_swipe);
//        tableSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                DiabloUtils.instance().makeToast(getContext(), "refresh");
//                tableSwipe.setRefreshing(false);
//            }
//        });
//
//        tableSwipe.setOnLoadListener(new DiabloTableSwipRefreshLayout.OnLoadListener() {
//            @Override
//            public void onLoad() {
//                DiabloUtils.instance().makeToast(getContext(), "onLoad");
//                tableSwipe.setLoading(false);
//            }
//        });

        mSaleDetailTableSwipe = (SwipyRefreshLayout) view.findViewById(R.id.t_sale_detail_swipe);
        // mSaleDetailTableSwipe = (DiabloTableSwipeRefreshLayout) view.findViewById(R.id.t_sale_detail_swipe);
        mSaleDetailTableSwipe.setDirection(SwipyRefreshLayoutDirection.BOTH);

        mSaleDetailTableSwipe.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP){
                    if (!mCurrentPage.equals(DEFAULT_PAGE)){
                        mCurrentPage--;
                        mRequest.setPage(mCurrentPage);
                        pageChanged();
                    } else {
                        DiabloUtils.instance().makeToast(
                                getContext(),
                                getContext().getResources().getString(R.string.refresh_top));
                        mSaleDetailTableSwipe.setRefreshing(false);
                    }

                } else if (direction == SwipyRefreshLayoutDirection.BOTTOM){
                    if (mCurrentPage.equals(mTotalPage)) {
                        DiabloUtils.instance().makeToast(
                            getContext(),
                            getContext().getResources().getString(R.string.refresh_bottom));
                        mSaleDetailTableSwipe.setRefreshing(false);
                    } else {
                        mCurrentPage++;
                        mRequest.setPage(mCurrentPage);
                        pageChanged();
                    }

                }
            }
        });


        // mSaleDetailTable.setClickable(false);

//        DiabloHorizontalScroll hScrollView = (DiabloHorizontalScroll)view.findViewById(R.id.t_sale_detail_hscroll);
//
//        GestureDetectorCompat gesture = new GestureDetectorCompat(mContext, new DiabloOnGestureLintener(hScrollView) {
//            @Override
//            public boolean actionOfOnFlint(View view, Integer direction) {
//                // DiabloUtils u = DiabloUtils.instance();
//                if (direction.equals(DiabloEnum.SWIP_LEFT)){
//                    return false;
//                } else if (direction.equals(DiabloEnum.SWIP_RIGHT)){
//                    return false;
//                } else if (direction.equals(DiabloEnum.SWIP_TOP)){
//                    // u.debugDialog(mContext, "滑动", "方向->top");
//                    mCurrentPage++;
//                    mRequest.setPage(mCurrentPage);
//                    pageChanged();
//                    return true;
//                } else if (direction.equals(DiabloEnum.SWIP_DOWN)){
//                    // u.debugDialog(mContext, "滑动", "方向->down");
//                    mCurrentPage--;
//                    if (!mCurrentPage.equals(0)){
//                        mRequest.setPage(mCurrentPage);
//                        view.postInvalidateOnAnimation();
//                        pageChanged();
//                    }
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });

//        hScrollView.setGestureDetect(gesture);


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



        // TableLayout head = ((TableLayout)view.findViewById(R.id.t_sale_detail_head));
        // head.addView(row);
        TableLayout head = (TableLayout) view.findViewById(R.id.t_sale_detail_head);
        head.addView(row);

        mSaleDetailTable = (TableLayout) view.findViewById(R.id.t_sale_detail);
//        for (Integer i = 0; i<mRows.length; i++){
//            mRows[i] = new TableRow(this.getContext());
//            mSaleDetailTable.addView(mRows[i]);
//            mRows[i].setBackgroundResource(R.drawable.table_row_bg);
//        }

        pageChanged();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSaleDetailFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSaleDetailFragmentInteraction(Uri uri);
    }

    private void pageChanged(){
        // hidden keyboard
        // DiabloUtils.instance().hiddenKeyboard(getContext());
        // get data from from web server
        // mRequest.getCondtion().setStartTime(DiabloUtils.instance().currentDate());
        mRequest.getCondtion().setStartTime("2016-01-01");
        mRequest.getCondtion().setEndTime(DiabloUtils.instance().nextDate());

        Call<SaleDetailResponse> call = mSaleRest.filterWSaleNew(Profile.instance().getToken(), mRequest);

        call.enqueue(new Callback<SaleDetailResponse>() {
            @Override
            public void onResponse(Call<SaleDetailResponse> call, Response<SaleDetailResponse> response) {
                Log.d("SALE_DETAIL %s", response.toString());

                mSaleDetailTableSwipe.setRefreshing(false);
                SaleDetailResponse base = response.body();
                if (DiabloEnum.DEFAULT_PAGE.equals(mCurrentPage)){
                    mTotalPage = base.getTotal() / mRequest.getCount() + 1;
                }

                List<SaleDetailResponse.SaleDetail> details = base.getSaleDetail();
                Integer orderId = mRequest.getPageStartIndex();
                // mSaleDetailTable.removeAllViews();
                TableRow row = null;
                mSaleDetailTable.removeAllViews();
                for (Integer i=0; i<details.size(); i++){
                    row = new TableRow(getContext());
                    // TableRow row = new TableRow(mContext);
                    // mSaleDetailTable.addView(row);
                    // row.removeAllViews();
                    SaleDetailResponse.SaleDetail detail = details.get(i);

                    for (String title: mTableHeads){
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                        if (getResources().getString(R.string.order_id).equals(title)) {
                            detail.setOrderId(orderId);
                            addCell(row, orderId++, lp);
                        }
                        else if (getResources().getString(R.string.rsn).equals(title)){
                            String [] fs = detail.getRsn().split("-");
                            addCell(row, fs[fs.length - 1], lp);
                        }
                        else if(getResources().getString(R.string.trans).equals(title)){
                            TextView cell = addCell(row, mSaleTypes[detail.getType()], lp);
                            if (detail.getType().equals(DiabloEnum.SALE_OUT)){
                                cell.setTextColor(getResources().getColor(R.color.red));
                            }
                        }
                        else if(getResources().getString(R.string.shop).equals(title)){
                            addCell(row,
                                DiabloUtils.getInstance().getShop(Profile.instance().getSortShop(),
                                    detail.getShop()).getName(),
                                lp);
                        }
                        else if (getResources().getString(R.string.employee).equals(title)){
                            addCell(row,
                                    DiabloUtils.getInstance().getEmployeeByNumber(
                                        Profile.instance().getEmployees(),
                                        detail.getEmployee()).getName(),
                                lp);
                        }
                        else if (getContext().getString(R.string.retailer).equals(title)){
                            addCell(row,
                                    DiabloUtils.getInstance().getRetailer(
                                        Profile.instance().getRetailers(),
                                        detail.getRetailer()).getName(),
                                lp);
                        }
                        else if (getResources().getString(R.string.amount).equals(title)){
                            addCell(row, detail.getTotal(), lp);
                        }
                        else if (getResources().getString(R.string.balance).equals(title)){
                            addCell(row, detail.getBalance(), lp);
                        }
                        else if (getResources().getString(R.string.should_pay).equals(title)){
                            addCell(row, detail.getShouldPay(), lp);
                        }
                        else if (getResources().getString(R.string.has_pay).equals(title)){
                            addCell(row, detail.getHasPay(), lp);
                        }
                        else if (getResources().getString(R.string.verificate).equals(title)){
                            addCell(row, detail.getVerificate(), lp);
                        }
                        else if (getResources().getString(R.string.epay).equals(title)){
                            addCell(row, detail.getEpay(), lp);
                        }
                        else if (getResources().getString(R.string.acc_balance).equals(title)){
                            addCell(row, detail.getBalance(), lp);
                        }
                        else if (getResources().getString(R.string.cash).equals(title)){
                            addCell(row, detail.getCash(), lp);
                        }
                        else if (getResources().getString(R.string.card).equals(title)){
                            addCell(row, detail.getCard(), lp);
                        }
                        else if (getResources().getString(R.string.wire).equals(title)){
                            addCell(row, detail.getWire(), lp);
                        }
                        else if (getResources().getString(R.string.date).equals(title)){
                            String shortDate = detail.getEntryDate().substring(
                                5, detail.getEntryDate().length() - 3).trim();
                            addCell(row, shortDate, lp);
                        }
                    }

//                    final GestureDetectorCompat gesture =
//                            new GestureDetectorCompat(mContext, new DiabloOnGestureLintener(row){
//                                @Override
//                                public void actionOfOnLongpress(View view) {
//                                    DiabloUtils u = DiabloUtils.instance();
//                                    u.debugDialog(mContext, "方向", "长按");
//                                }
//
//                                @Override
//                                public boolean actionOfOnDown(View view) {
//                                    for(Integer i=0; i<mSaleDetailTable.getChildCount(); i++){
//                                        View child = mSaleDetailTable.getChildAt(i);
//                                        if (child instanceof TableRow){
//                                            child.setBackgroundResource(R.drawable.table_row_bg);
//                                        }
//                                    }
//                                    view.setBackgroundColor(getResources().getColor(R.color.bluelight));
//                                    SaleDetailResponse.SaleDetail d = (SaleDetailResponse.SaleDetail)view.getTag();
//                                    return true;
//                                }
//                            });
//
//                    row.setOnTouchListener(new View.OnTouchListener(){
//                        @Override
//                        public boolean onTouch(View view, MotionEvent motionEvent) {
//                            gesture.onTouchEvent(motionEvent);
//                            return true;
//                        }
//
//
//                    });

//                    row.setOnTouchListener(new View.OnTouchListener() {
//                        @Override
//                        public boolean onTouch(View view, MotionEvent event) {
//                            for(Integer i=0; i<mSaleDetailTable.getChildCount(); i++){
//                                View child = mSaleDetailTable.getChildAt(i);
//                                if (child instanceof TableRow){
//                                    child.setBackgroundResource(R.drawable.table_row_bg);
//                                }
//                            }
//                            view.setBackgroundColor(getResources().getColor(R.color.bluelight));
//                            SaleDetailResponse.SaleDetail d = (SaleDetailResponse.SaleDetail)view.getTag();
//                            DiabloUtils.instance().makeToast(mContext, d.getOrderId());
//                            return true;
//                        }
//                    });

                    row.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            SaleDetailResponse.SaleDetail d = (SaleDetailResponse.SaleDetail)view.getTag();
                            return true;
                        }
                    });

                    row.setBackgroundResource(R.drawable.table_row_bg);
                    row.setTag(detail);
                    // row.invalidate();
                    mSaleDetailTable.addView(row);
//                    mSaleDetailTable.invalidate();
//                    mSaleDetailTable.refreshDrawableState();
                }

                if (null != row) {
                    row.setBackgroundResource(R.drawable.table_row_last_bg);
                }
//                mSaleDetailTable.invalidate();
//                mSaleDetailTable.refreshDrawableState();
                // mSaleDetailTable.invalidate();

            }

            @Override
            public void onFailure(Call<SaleDetailResponse> call, Throwable t) {
                mSaleDetailTableSwipe.setRefreshing(false);
            }
        });
    }

    public TextView addCell(TableRow row, String value, TableRow.LayoutParams lp){
        // TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight);
        TextView cell = new TextView(getContext());
        cell.setLayoutParams(lp);
        // cell.setTextColor(context.getResources().getColor(R.color.black));
        cell.setText(value.trim());
        cell.setTextSize(18);
        cell.setHeight(110);
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
        cell.setHeight(110);
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
        cell.setHeight(110);
        // cell.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        row.addView(cell);
        return  cell;
    }
}
