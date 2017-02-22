package com.diablo.dt.diablo.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.Client.WSaleClient;
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

        mTitles = getResources().getStringArray(R.array.thead_sale_detal);
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

        mSaleDetailTable.addView(row);

        // get data from from web server
        WSaleInterface wSaleInterface = WSaleClient.getClient().create(WSaleInterface.class);
        Call<List<String>> call = wSaleInterface.filterWsaleNew("2016-01-01", "2016-12-12");
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                TextView cell;
                TableRow row = new TableRow(getContext());
                TableRow.LayoutParams lp = new TableRow.LayoutParams();
                lp.setMargins(0, 0, 25, 0);

                for (String title: mTitles){
                    cell = new TextView(getContext());
                    // font
                    cell.setTypeface(null, Typeface.BOLD);
                    cell.setTextColor(Color.BLACK);
                    // right-margin
                    cell.setLayoutParams(lp);
                    cell.setText(title);

                    row.addView(cell);
                }

                mSaleDetailTable.addView(row);
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
}
