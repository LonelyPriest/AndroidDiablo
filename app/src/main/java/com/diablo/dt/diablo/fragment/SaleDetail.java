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

import com.diablo.dt.diablo.Client.WSaleClient;
import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.MainProfile;
import com.diablo.dt.diablo.request.SaleDetailRequest;
import com.diablo.dt.diablo.response.SaleDetailResponse;
import com.diablo.dt.diablo.rest.WSaleInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private String [] mTitles;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TableLayout mSaleDetailTable;

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

        mTitles = getResources().getStringArray(R.array.thead_sale_detail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale_detail, container, false);
        mSaleDetailTable = (TableLayout) view.findViewById(R.id.tab_sale_detail);

        TableRow.LayoutParams lp = new TableRow.LayoutParams();
        lp.setMargins(0, 0, 25, 0);

        TableRow row = new TableRow(this.getContext());
        TextView cell;
        for (String title: mTitles){
            cell = new TextView(this.getContext());
            // font
            cell.setTypeface(null, Typeface.BOLD);
            cell.setTextColor(Color.BLACK);
            // right-margin
            cell.setLayoutParams(lp);
            cell.setText(title);

            row.addView(cell);
        }

        // row.setBackgroundResource(R.drawable.table_row_bg);
        mSaleDetailTable.addView(row);

        // get data from from web server
        WSaleInterface wSaleInterface = WSaleClient.getClient().create(WSaleInterface.class);
        final SaleDetailRequest request = new SaleDetailRequest();
        request.getCondtion().setStartTime("2016-01-01");
        request.getCondtion().setEndTime("2016-12-12");
        Call<SaleDetailResponse> call = wSaleInterface.filterWsaleNew(
                MainProfile.getInstance().getToken(), request);

        call.enqueue(new Callback<SaleDetailResponse>() {
            @Override
            public void onResponse(Call<SaleDetailResponse> call, Response<SaleDetailResponse> response) {
                Log.d("SALE_DETAIL %s", response.toString());
                SaleDetailResponse base = response.body();
                List<SaleDetailResponse.SaleDetail> details = base.getSaleDetail();

                TableRow row;
                Integer orderId = request.getPageStartIndex();
                for (Integer i=0; i<details.size(); i++){
                    row = new TableRow(getContext());
                    SaleDetailResponse.SaleDetail detail = details.get(i);

                    for (String title: mTitles){
                        if (getResources().getString(R.string.order_id).equals(title)) {
                            addCell(row, orderId++);
                        } else if (getResources().getString(R.string.rsn).equals(title)){
                            addCell(row, detail.getRsn());
                        } else if(getResources().getString(R.string.transe).equals(title)){
                            addCell(row, detail.getType());
                        } else if(getResources().getString(R.string.shop).equals(title)){
                            addCell(row, detail.getShop());
                        } else if (getResources().getString(R.string.employee).equals(title)){
                            addCell(row, detail.getEmployee());
                        } else if (getResources().getString(R.string.retailer).equals(title)){
                            addCell(row, detail.getRetailer());
                        } else if (getResources().getString(R.string.amount).equals(title)){
                            addCell(row, detail.getTotal());
                        } else if (getResources().getString(R.string.balace).equals(title)){
                            addCell(row, detail.getBalance());
                        } else if (getResources().getString(R.string.should_pay).equals(title)){
                            addCell(row, detail.getShouldPay());
                        } else if (getResources().getString(R.string.has_pay).equals(title)){
                            addCell(row, detail.getHasPay());
                        } else if (getResources().getString(R.string.verificate).equals(title)){
                            addCell(row, detail.getVerificate());
                        } else if (getResources().getString(R.string.epay).equals(title)){
                            addCell(row, detail.getEpay());
                        } else if (getResources().getString(R.string.acc_balance).equals(title)){
                            addCell(row, detail.getBalance());
                        } else if (getResources().getString(R.string.cash).equals(title)){
                            addCell(row, detail.getCash());
                        } else if (getResources().getString(R.string.card).equals(title)){
                            addCell(row, detail.getCard());
                        } else if (getResources().getString(R.string.card).equals(title)){
                            addCell(row, detail.getWire());
                        } else if (getResources().getString(R.string.date).equals(title)){
                            addCell(row, detail.getEntryDate());
                        } else {

                        }

                    }

                    row.setBackgroundResource(R.drawable.table_row_bg);

                    mSaleDetailTable.addView(row);
                }

            }

            @Override
            public void onFailure(Call<SaleDetailResponse> call, Throwable t) {
//                TextView cell;
//                TableRow row = new TableRow(getContext());
//                TableRow.LayoutParams lp = new TableRow.LayoutParams();
//                lp.setMargins(0, 0, 25, 0);
//
//                for (String title: mTitles){
//                    cell = new TextView(getContext());
//                    // font
//                    cell.setTypeface(null, Typeface.BOLD);
//                    cell.setTextColor(Color.BLACK);
//                    // right-margin
//                    cell.setLayoutParams(lp);
//                    cell.setText(title);
//
//                    row.addView(cell);
//                }
//
//                mSaleDetailTable.addView(row);
            }
        });

        // row = new TableRow(this.getContext());
        // row.setBackgroundResource(R.drawable.table_row_bg);
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

    private TableRow addCell(TableRow row, String value){
        TextView cell = new TextView(getContext());
        TableRow.LayoutParams lp = new TableRow.LayoutParams();
        lp.setMargins(0, 0, 25, 0);
        cell.setText(value);
        cell.setTextSize(16);
        row.addView(cell);
        return  row;
    }

    private TableRow addCell(TableRow row, Integer value){
        TextView cell = new TextView(getContext());
        TableRow.LayoutParams lp = new TableRow.LayoutParams();
        lp.setMargins(0, 0, 25, 0);
        cell.setText(value.toString());
        cell.setTextSize(16);
        row.addView(cell);
        return  row;
    }

    private TableRow addCell(TableRow row, float value){
        TextView cell = new TextView(getContext());
        TableRow.LayoutParams lp = new TableRow.LayoutParams();
        lp.setMargins(0, 0, 25, 0);
        cell.setText(Float.toString(value));
        cell.setTextSize(16);
        row.addView(cell);
        return  row;
    }
}
