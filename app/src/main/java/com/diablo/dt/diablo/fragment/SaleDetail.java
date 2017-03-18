package com.diablo.dt.diablo.fragment;

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

import com.diablo.dt.diablo.client.WSaleClient;
import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.activity.MainActivity;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.request.SaleDetailRequest;
import com.diablo.dt.diablo.response.SaleDetailResponse;
import com.diablo.dt.diablo.rest.WSaleInterface;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloTableSwipeRefreshLayout;
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
    TableRow[] mRows;

    private android.widget.TableLayout mSaleDetailTable;
    private DiabloTableSwipeRefreshLayout mSaleDetailTableSwipe;

    private Context mContext;

    private Integer mCurrentPage;

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

        mContext = this.getContext();
        mTableHeads = getResources().getStringArray(R.array.thead_sale_detail);
        mSaleTypes = getResources().getStringArray(R.array.sale_type);

        mCurrentPage = DiabloEnum.DEFAULT_PAGE;
        mRequest = new SaleDetailRequest(mCurrentPage, DiabloEnum.DEFAULT_ITEMS_PER_PAGE);
        mRequestCondition = mRequest.new Condition();
        mRequest.setCondtion(mRequestCondition);
        mSaleRest = WSaleClient.getClient().create(WSaleInterface.class);

        mRows = new TableRow[mRequest.getCount()];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale_detail, container, false);

        ((MainActivity)getActivity()).selectMenuItem(2);
        mSaleDetailTable = (TableLayout) view.findViewById(R.id.t_sale_detail);

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

        // mSaleDetailTableSwipe = (SwipyRefreshLayout) view.findViewById(R.id.t_sale_detail_swipe);
        mSaleDetailTableSwipe = (DiabloTableSwipeRefreshLayout) view.findViewById(R.id.t_sale_detail_swipe);
        mSaleDetailTableSwipe.setDirection(SwipyRefreshLayoutDirection.BOTH);

        mSaleDetailTableSwipe.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
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
                                getContext().getResources().getString(R.string.refresh_none));
                        mSaleDetailTableSwipe.setRefreshing(false);
                    }

                } else if (direction == SwipyRefreshLayoutDirection.BOTTOM){
                    mCurrentPage++;
                    mRequest.setPage(mCurrentPage);
                    pageChanged();
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

        TableRow.LayoutParams lp = new TableRow.LayoutParams();
        lp.setMargins(0, 0, 25, 0);

        TableRow row = new TableRow(this.getContext());
        for (String title: mTableHeads){
            TextView cell = new TextView(this.getContext());
            // font
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

        mSaleDetailTable.addView(row);

        for (Integer i = 0; i<mRows.length; i++){
            mRows[i] = new TableRow(this.getContext());
            mSaleDetailTable.addView(mRows[i]);
        }

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
        // get data from from web server
        mRequest.getCondtion().setStartTime("2016-01-01");
        mRequest.getCondtion().setEndTime("2016-12-12");

        Call<SaleDetailResponse> call = mSaleRest.filterWSaleNew(Profile.instance().getToken(), mRequest);

        call.enqueue(new Callback<SaleDetailResponse>() {
            @Override
            public void onResponse(Call<SaleDetailResponse> call, Response<SaleDetailResponse> response) {
                Log.d("SALE_DETAIL %s", response.toString());

                mSaleDetailTableSwipe.setRefreshing(false);
                SaleDetailResponse base = response.body();
                List<SaleDetailResponse.SaleDetail> details = base.getSaleDetail();

                Integer orderId = mRequest.getPageStartIndex();
                DiabloUtils u = DiabloUtils.getInstance();
                // mSaleDetailTable.removeAllViews();
                for (Integer i=0; i<mRequest.getCount(); i++){
                    final TableRow row = mRows[i];
                    // TableRow row = new TableRow(mContext);
                    // mSaleDetailTable.addView(row);
                    row.removeAllViews();
                    SaleDetailResponse.SaleDetail detail = details.get(i);

                    for (String title: mTableHeads){
                        if (getResources().getString(R.string.order_id).equals(title)) {
                            detail.setOrderId(orderId);
                            u.addCell(mContext, row, orderId++);
                        } else if (getResources().getString(R.string.rsn).equals(title)){
                            u.addCell(mContext, row, detail.getRsn());
                        } else if(getResources().getString(R.string.transe).equals(title)){
                            TextView cell = u.addCell(mContext, row, mSaleTypes[detail.getType()]);
                            if (detail.getType().equals(DiabloEnum.SALE_OUT)){
                                cell.setTextColor(getResources().getColor(R.color.red));
                            }
                        } else if(getResources().getString(R.string.shop).equals(title)){
                            u.addCell(mContext,
                                    row,
                                    DiabloUtils.getInstance().getShop(
                                            Profile.instance().getSortShop(),
                                            detail.getShop()).getName());
                        } else if (getResources().getString(R.string.employee).equals(title)){
                            u.addCell(mContext,
                                    row,
                                    DiabloUtils.getInstance().getEmployeeByNumber(
                                            Profile.instance().getEmployees(),
                                            detail.getEmployee()).getName());
                        } else if (getResources().getString(R.string.retailer).equals(title)){
                            u.addCell(mContext,
                                    row,
                                    DiabloUtils.getInstance().getRetailer(
                                            Profile.instance().getRetailers(),
                                            detail.getRetailer()).getName());
                        } else if (getResources().getString(R.string.amount).equals(title)){
                            u.addCell(mContext, row, detail.getTotal());
                        } else if (getResources().getString(R.string.balace).equals(title)){
                            u.addCell(mContext, row, detail.getBalance());
                        } else if (getResources().getString(R.string.should_pay).equals(title)){
                            u.addCell(mContext, row, detail.getShouldPay());
                        } else if (getResources().getString(R.string.has_pay).equals(title)){
                            u.addCell(mContext, row, detail.getHasPay());
                        } else if (getResources().getString(R.string.verificate).equals(title)){
                            u.addCell(mContext, row, detail.getVerificate());
                        } else if (getResources().getString(R.string.epay).equals(title)){
                            u.addCell(mContext, row, detail.getEpay());
                        } else if (getResources().getString(R.string.acc_balance).equals(title)){
                            u.addCell(mContext, row, detail.getBalance());
                        } else if (getResources().getString(R.string.cash).equals(title)){
                            u.addCell(mContext, row, detail.getCash());
                        } else if (getResources().getString(R.string.card).equals(title)){
                            u.addCell(mContext, row, detail.getCard());
                        } else if (getResources().getString(R.string.wire).equals(title)){
                            u.addCell(mContext, row, detail.getWire());
                        } else if (getResources().getString(R.string.date).equals(title)){
                            u.addCell(mContext, row, detail.getEntryDate());
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

                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for(Integer i=0; i<mSaleDetailTable.getChildCount(); i++){
                                View child = mSaleDetailTable.getChildAt(i);
                                if (child instanceof TableRow){
                                    child.setBackgroundResource(R.drawable.table_row_bg);
                                }
                            }
                            view.setBackgroundColor(getResources().getColor(R.color.bluelight));
                            SaleDetailResponse.SaleDetail d = (SaleDetailResponse.SaleDetail)view.getTag();
                            DiabloUtils.instance().makeToast(mContext, d.getOrderId());
                        }
                    });
                    row.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            SaleDetailResponse.SaleDetail d = (SaleDetailResponse.SaleDetail)view.getTag();


                            return true;
                        }
                    });

                    row.setTag(detail);
                    row.setBackgroundResource(R.drawable.table_row_bg);
                    // row.invalidate();
                    // mSaleDetailTable.addView(row);
//                    mSaleDetailTable.invalidate();
//                    mSaleDetailTable.refreshDrawableState();
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
}
