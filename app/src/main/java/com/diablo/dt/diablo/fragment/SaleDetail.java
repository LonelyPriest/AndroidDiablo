package com.diablo.dt.diablo.fragment;

import android.content.Context;
import android.content.res.Resources;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TableLayout mSaleDetailTable;
    private Resources resources;

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

        resources = getContext().getResources();

//        mSaleDetailTable = new TableLayout(this.getContext());
//        mSaleDetailTable.setGravity(Gravity.CENTER);
//        mSaleDetailTable.setBackgroundResource(R.color.colorPrimaryDark);

        // mSaleDetailTable.setBackgroundResource(R.color.colorPrimary);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale_detail, container, false);
        mSaleDetailTable = (TableLayout) view.findViewById(R.id.tab_sale_detail);

        // add table head
        TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        rowLayoutParams.setMargins(1,1,1,1);

        TableRow row = new TableRow(this.getContext());
        row.setLayoutParams(rowLayoutParams);
        row.setBackgroundResource(R.color.colorGray);

        TextView t1 = new TextView(this.getContext());
        TextView t2 = new TextView(this.getContext());
        TextView t3 = new TextView(this.getContext());

        t1.setBackgroundResource(R.color.colorAccent);
        t1.setLayoutParams(rowLayoutParams);
        t1.setText("第一列");
        row.addView(t1);
        t2.setText("第二列");
        row.addView(t2);
        t3.setText("第三列");
        row.addView(t3);
        mSaleDetailTable.addView(row);

        row = new TableRow(this.getContext());
        row.setLayoutParams(rowLayoutParams);
        row.setBackgroundResource(R.color.colorAccent);

        t1 = new TextView(this.getContext());
        t2 = new TextView(this.getContext());
        t3 = new TextView(this.getContext());


        t1.setText("第一列");
        row.addView(t1);
        t2.setText("第二列");
        row.addView(t2);
        t3.setText("第三列");
        row.addView(t3);
        mSaleDetailTable.addView(row);

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
