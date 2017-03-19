package com.diablo.dt.diablo.fragment;

import com.google.gson.Gson;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.StockClient;
import com.diablo.dt.diablo.client.WSaleClient;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Stock;
import com.diablo.dt.diablo.model.SaleStock;
import com.diablo.dt.diablo.model.SaleStockAmount;
import com.diablo.dt.diablo.request.LastSaleRequest;
import com.diablo.dt.diablo.request.StockRequest;
import com.diablo.dt.diablo.response.LastSaleResponse;
import com.diablo.dt.diablo.rest.StockInterface;
import com.diablo.dt.diablo.rest.WSaleInterface;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloSaleTable;
import com.diablo.dt.diablo.utils.DiabloTextWatcher;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StockSelect extends Fragment {
    private static final String LOG_TAG = "STOCK_SELECT:";

    private SaleStock mSaleStock;
    private Integer mSelectShop;
    private Integer mSelectRetailer;
    private Integer mSelectAction;

    private TableLayout mViewTable;

    private List<String> mOrderedSizes = new ArrayList<>();
    private List<DiabloColor> mOrderColors = new ArrayList<>();
    // private List<SaleStockAmount> mStockAmounts = new ArrayList<>();

    private List<Stock> mStocks;
    private List<LastSaleResponse> mLastStocks;

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
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSelectShop = getArguments().getInt(DiabloEnum.BUNDLE_PARAM_SHOP);
            mSelectRetailer = getArguments().getInt(DiabloEnum.BUNDLE_PARAM_RETAILER);
            mSelectAction = getArguments().getInt(DiabloEnum.BUNDLE_PARAM_ACTION);
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

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.select_stock));

        init();
        return view;
    }

    private void init(){
        mOrderColors.clear();
        mOrderedSizes.clear();
        mViewTable.removeAllViews();

        // get stock
        if (R.string.add == mSelectAction){
            getStockFromServer();
            getLastTransactionOfRetailer();
        }
        else if (R.string.modify == mSelectAction) {
            startModify();
        }
    }

    private void getStockFromServer(){
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
            public void onResponse(final Call<List<Stock>> call, Response<List<Stock>> response) {
                Log.d(LOG_TAG, "success to get stock");
                mStocks = new ArrayList<>(response.body());
                startAdd();
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
                mLastStocks = new ArrayList<>(response.body());
            }

            @Override
            public void onFailure(Call<List<LastSaleResponse>> call, Throwable t) {
                Log.d(LOG_TAG, "fail to get last stock");
            }
        });
    }


    private void startModify() {
        ArrayList<String> usedSizes = new ArrayList<>();
        for(SaleStockAmount a: mSaleStock.getAmounts()){
            if (!usedSizes.contains(a.getSize())){
                usedSizes.add(a.getSize());
            }

            DiabloColor color = Profile.instance().getColor(a.getColorId());
            if (!color.includeIn(mOrderColors)){
                mOrderColors.add(color);
            }
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

        startSelect();
    }

    private void startAdd() {
        ArrayList<String> usedSizes = new ArrayList<>();
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

        startSelect();
    }

    private void startSelect() {
        DiabloSaleTable saleTable = new DiabloSaleTable(getContext(), mViewTable, mOrderColors, mOrderedSizes);
        saleTable.setStockListener(new DiabloSaleTable.OnStockListener() {
            @Override
            public SaleStockAmount getStockByColorAndSize(Integer colorId, String size) {
                    return findStock(colorId, size);
            }

            @Override
            public void onStockSelected(EditText cell, final Integer colorId, final String size) {
                cell.addTextChangedListener(new DiabloTextWatcher() {
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


    public SaleStockAmount findStock(Integer colorId, String size){
        for ( Integer i=0; i<mSaleStock.getAmounts().size(); i++){
            SaleStockAmount a = mSaleStock.getAmounts().get(i);
            if (a.getColorId().equals(colorId) && a.getSize().equals(size)){
                return a;
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

    public void setSelectAction(Integer action) {
        this.mSelectAction = action;
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
