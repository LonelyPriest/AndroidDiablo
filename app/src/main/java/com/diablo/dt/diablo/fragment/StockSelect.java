package com.diablo.dt.diablo.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;

import com.diablo.dt.diablo.client.StockClient;
import com.diablo.dt.diablo.client.WSaleClient;
import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.SaleStock;
import com.diablo.dt.diablo.model.SaleStockAmount;
import com.diablo.dt.diablo.entity.Stock;
import com.diablo.dt.diablo.request.LastSaleRequest;
import com.diablo.dt.diablo.request.StockRequest;
import com.diablo.dt.diablo.response.LastSaleResponse;
import com.diablo.dt.diablo.rest.StockInterface;
import com.diablo.dt.diablo.rest.WSaleInterface;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloSaleTable;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StockSelect extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String LOG_TAG = "STOCK_SELECT:";

    private SaleStock mSaleStock;
    private Integer mSelectShop;
    private Integer mSelectRetailer;

    private TableLayout mViewTable;

    private List<String> mOrderedSizes = new ArrayList<>();
    private List<DiabloColor> mOrderColors = new ArrayList<>();
    // private List<SaleStockAmount> mStockAmounts = new ArrayList<>();

    private List<Stock> mStocks;
    private List<LastSaleResponse> mLastStocks;

    private final static DiabloUtils utils = DiabloUtils.instance();

    // private OnFragmentInteractionListener mListener;

    public StockSelect() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StockSelect.
     */
    // TODO: Rename and change types and number of parameters
    public static StockSelect newInstance(String param1, String param2) {
        StockSelect fragment = new StockSelect();
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
            mSelectShop = getArguments().getInt(DiabloEnum.BUNDLE_PARAM_SHOP);
            mSelectRetailer = getArguments().getInt(DiabloEnum.BUNDLE_PARAM_RETAILER);
            mSaleStock = new Gson().fromJson(getArguments().getString(DiabloEnum.BUNDLE_PARAM_SALE_STOCK), SaleStock.class);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock_select, container, false);
        mViewTable = (TableLayout) view.findViewById(R.id.stock_select);

        // get stock
        getStock();
        getLastTransactionOfRetailer();

        ((AppCompatActivity)getActivity()).getSupportActionBar()
                .setTitle(getResources().getString(R.string.select_stock));

        return view;
    }

    private void init(){
        mOrderColors.clear();
        mOrderedSizes.clear();
        mViewTable.removeAllViews();
    }

    private void getStock(){
        StockInterface face = StockClient.getClient().create(StockInterface.class);
        Call<List<Stock>> call = face.getStock(
                Profile.instance().getToken(),
                new StockRequest(
                        mSaleStock.getStyleNumber(),
                        mSaleStock.getBrandId(),
                        mSelectShop,
                        DiabloEnum.USE_REPO));

        call.enqueue(new Callback<List<Stock>>() {
            @Override
            public void onResponse(Call<List<Stock>> call, Response<List<Stock>> response) {
                Log.d(LOG_TAG, "success to get stock");
                mStocks = new ArrayList<Stock>(response.body());

                ArrayList<String> usedSizes = new ArrayList<String>();
                for(Stock s: mStocks){
                    if (!usedSizes.contains(s.getSize())){
                        usedSizes.add(s.getSize());
                    }

                    DiabloColor color = Profile.instance().getColor(s.getColorId());
                    if (!color.includeIn(mOrderColors)){
                        mOrderColors.add(color);
                    }

                    SaleStockAmount amount = new SaleStockAmount(s.getColorId(), s.getSize());
                    amount.setStock(s.getExist());
                    mSaleStock.getAmounts().add(amount);
                }

                ArrayList<String> orderedSizes = Profile.instance().genSortedSizeNamesByGroups(mSaleStock.getSizeGroup());
                for (String s: orderedSizes){
                    if (usedSizes.contains(s)){
                        mOrderedSizes.add(s);
                    }
                }

                if (mOrderedSizes.size() != usedSizes.size()){
                    for (String s: usedSizes){
                        if (!mOrderedSizes.contains(s)){
                            mOrderedSizes.add(0, s);
                        }
                    }
                }

                DiabloSaleTable saleTable = new DiabloSaleTable(getContext(), mViewTable, mOrderColors,mOrderedSizes);
                saleTable.setStockListener(new DiabloSaleTable.OnStockListener() {
                    @Override
                    public Stock getStockByColorAndSize(Integer colorId, String size) {
                        return findStock(colorId, size);
                    }

                    @Override
                    public void onStockSelected(EditText cell, final Integer colorId, final String size) {
                        cell.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                for (SaleStockAmount amount: mSaleStock.getAmounts()){
                                    if (amount.getColorId().equals(colorId) && amount.getSize().equals(size)){
                                        amount.setSellCount(DiabloUtils.instance().toInteger(editable.toString()));
                                    }
                                }
                            }
                        });
                    }
                });

                saleTable.genHead();
                saleTable.genContent();
            }

            @Override
            public void onFailure(Call<List<Stock>> call, Throwable t) {
                Log.d(LOG_TAG, "failed to get stock");
            }
        });
    }

    private void getLastTransactionOfRetailer(){
        WSaleInterface face = WSaleClient.getClient().create(WSaleInterface.class);
        Call<List<LastSaleResponse>> call = face.getLastSale(
                Profile.instance().getToken(),
                new LastSaleRequest(
                        mSaleStock.getStyleNumber(),
                        mSaleStock.getBrandId(),
                        mSelectShop,
                        mSelectRetailer));

        call.enqueue(new Callback<List<LastSaleResponse>>() {
            @Override
            public void onResponse(Call<List<LastSaleResponse>> call, Response<List<LastSaleResponse>> response) {
                Log.d(LOG_TAG, "success to get last stock");
                mLastStocks = new ArrayList<LastSaleResponse>(response.body());
            }

            @Override
            public void onFailure(Call<List<LastSaleResponse>> call, Throwable t) {
                Log.d(LOG_TAG, "fail to get last stock");
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.add(Menu.NONE, 100, Menu.NONE, getResources().getString(R.string.btn_cancel)).setIcon(R.drawable.ic_close_black_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add(Menu.NONE, 101, Menu.NONE, getResources().getString(R.string.btn_save))
                .setIcon(R.drawable.ic_check_black_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 100: // cancel
                switchFragmentToSaleIn(R.integer.action_cancel);
                break;
            case 101: // save
                switchFragmentToSaleIn(R.integer.action_save);
                break;
            default:
                break;
        }



        return true;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onStockSelectFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // mListener = null;
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
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onStockSelectFragmentInteraction(Uri uri);
//    }


    public Stock findStock(Integer colorId, String size){
        for ( Integer i=0; i<mStocks.size(); i++){
            Stock s = mStocks.get(i);
            if (s.getColorId().equals(colorId) && s.getSize().equals(size)){
                return s;
            }
        }

        return null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // utils.makeToast(getContext(), hidden ? "hidden" : "show");
        if (!hidden){
            init();
            getStock();
            getLastTransactionOfRetailer();
        }
    }

    public void setSaleStock(String saleStockJson) {
        this.mSaleStock = new Gson().fromJson(saleStockJson, SaleStock.class);
    }

    public void setSelectShop(Integer shop) {
        this.mSelectShop = shop;
    }

    public void setSelectRetailer(Integer retailer) {
        this.mSelectRetailer = retailer;
    }

    private void switchFragmentToSaleIn(final Integer action){
        SaleIn to = (SaleIn)getFragmentManager().findFragmentByTag(DiabloEnum.TAG_SALE_IN);
        if (null != to){
            to.setComesForm(R.integer.COMES_FORM_STOCK_SELECT_ON_SALE);
            to.setStockListener(new SaleIn.OnStockSelectListener() {
                @Override
                public Integer StockSelectAction() {
                    return action;
                }

                @Override
                public SaleStock afterStockSelected() {
                    return mSaleStock;
                }
            });
        } else {
            to = new SaleIn();
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (!to.isAdded()){
            transaction.hide(StockSelect.this).add(R.id.frame_container, to).commit();
        } else {
            transaction.hide(StockSelect.this).show(to).commit();
        }

    }
}
