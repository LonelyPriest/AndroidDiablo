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
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.Client.StockClient;
import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.activity.adapter.EmployeeAdapter;
import com.diablo.dt.diablo.activity.adapter.MatchStockAdapter;
import com.diablo.dt.diablo.activity.adapter.RetailerAdapter;
import com.diablo.dt.diablo.entity.AuthenShop;
import com.diablo.dt.diablo.entity.DiabloEnum;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profie;
import com.diablo.dt.diablo.request.MatchStockRequest;
import com.diablo.dt.diablo.rest.StockInterface;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SaleIn.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SaleIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaleIn extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private String [] mTitles;
    private TableLayout mSaleTable;
    private Integer mLoginShop;
    private String mCurrentDate;

    private List<MatchStock> matchStocks;

    public SaleIn() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SaleIn.
     */
    // TODO: Rename and change types and number of parameters
    public static SaleIn newInstance(String param1, String param2) {
        SaleIn fragment = new SaleIn();
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

        mTitles = getResources().getStringArray(R.array.thead_sale);

        mLoginShop = Profie.getInstance().getLoginShop();
        if ( mLoginShop.equals(DiabloEnum.INVALID_INDEX) ){
            mLoginShop = Profie.getInstance().getAvailableShopIds().get(0);
        }

        mCurrentDate = DiabloUtils.getInstance().currentDate();

        // get all stocks
        this.getAllMatchStock();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale_in, container, false);

        // retailer
        AutoCompleteTextView autoCompleteRetailer =
                (AutoCompleteTextView) view.findViewById(R.id.sale_select_retailer);

        RetailerAdapter retailerAdapter = new RetailerAdapter(
                getContext(),
                R.layout.typeahead_retailer,
                R.id.typeahead_select_retailer,
                Profie.getInstance().getRetailers());

        autoCompleteRetailer.setThreshold(1);
        autoCompleteRetailer.setAdapter(retailerAdapter);

        // shop
        TextView shopView = (TextView) view.findViewById(R.id.sale_selected_shop);
        AuthenShop shop = DiabloUtils.getInstance().getShop(Profie.getInstance().getSortAvailableShop(), mLoginShop);
        shopView.setText(shop.getName());

        // current time
        TextView viewDate = (TextView) view.findViewById(R.id.sale_selected_date);
        viewDate.setText(DiabloUtils.getInstance().currentDatetime());

        // employee
        Spinner employeeSpinner = (Spinner) view.findViewById(R.id.sale_select_employee);

        EmployeeAdapter employeeAdapter = new EmployeeAdapter(
                getContext(),
                R.layout.typeahead_employee,
                R.id.typeahead_select_employee,
                Profie.getInstance().getEmployees());
        employeeSpinner.setAdapter(employeeAdapter);

        // table
        mSaleTable = (TableLayout)view.findViewById(R.id.t_sale);
        mSaleTable.addView(addHead());
        // mSaleTable.addView(addEmptyRow());


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSaleInFragmentInteraction(uri);
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
        void onSaleInFragmentInteraction(Uri uri);
    }

    private TableRow addHead(){
        TableRow row = new TableRow(this.getContext());
        for (String title: mTitles){
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            TextView cell = new TextView(this.getContext());
            // font
            cell.setTypeface(null, Typeface.BOLD);
            cell.setTextColor(Color.BLACK);
            // right-margin

            if (getResources().getString(R.string.good).equals(title)){
                lp.weight = 1.5f;
            }
            cell.setLayoutParams(lp);
            cell.setText(title);
            cell.setTextSize(18);

            row.addView(cell);
        }

        return row;
    };

    private TableRow addEmptyRow(){
        // lp.setMargins(0, 0, 10, 0);
        TableRow row = new TableRow(this.getContext());
        for (String title: mTitles){
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            if (getResources().getString(R.string.good).equals(title)){
                AutoCompleteTextView eCell = new AutoCompleteTextView(this.getContext());
                eCell.setTextColor(Color.BLACK);
                // right-margin
                lp.weight = 1.5f;
                eCell.setLayoutParams(lp);
                eCell.setHint(R.string.please_input_good);
                eCell.setTextSize(16);

                eCell.setDropDownHeight(500);
                eCell.setDropDownWidth(800);
                MatchStockAdapter adapter = new MatchStockAdapter(
                        getContext(),
                        R.layout.typeahead_match_stock_on_sale,
                        R.id.typeahead_select_stock_on_sale,
                        matchStocks);

                eCell.setThreshold(1);
                eCell.setAdapter(adapter);

                row.addView(eCell);
            } else {
                TextView cell = new TextView(this.getContext());
                // font
                cell.setTextColor(Color.BLACK);
                // right-margin
                cell.setLayoutParams(lp);
                // cell.setText(title);
                cell.setTextSize(16);
                row.addView(cell);
            }


        }

        return row;
    };

    private void getAllMatchStock(){
        StockInterface face = StockClient.getClient().create(StockInterface.class);
        Call<List<MatchStock>> call = face.matchAllStock(
                Profie.getInstance().getToken(),
                new MatchStockRequest(
                        Profie.getInstance().getConfig(mLoginShop, DiabloEnum.START_TIME, mCurrentDate),
                        mLoginShop,
                        DiabloEnum.USE_REPO));
        call.enqueue(new Callback<List<MatchStock>>() {
            @Override
            public void onResponse(Call<List<MatchStock>> call, Response<List<MatchStock>> response) {
                Log.d("LOGIN:", "success to get retailer");
                matchStocks = response.body();
                mSaleTable.addView(addEmptyRow());
            }

            @Override
            public void onFailure(Call<List<MatchStock>> call, Throwable t) {
                Log.d("LOGIN:", "failed to get retailer");
            }
        });
    }
}
