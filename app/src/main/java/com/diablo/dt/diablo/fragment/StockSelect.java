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
import com.diablo.dt.diablo.activity.MainActivity;
import com.diablo.dt.diablo.client.WSaleClient;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Stock;
import com.diablo.dt.diablo.model.sale.DiabloSaleTable;
import com.diablo.dt.diablo.model.sale.SaleStock;
import com.diablo.dt.diablo.model.sale.SaleStockAmount;
import com.diablo.dt.diablo.request.LastSaleRequest;
import com.diablo.dt.diablo.response.LastSaleResponse;
import com.diablo.dt.diablo.rest.WSaleInterface;
import com.diablo.dt.diablo.task.MatchSingleStockTask;
import com.diablo.dt.diablo.utils.DiabloEnum;
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
    private Integer   mSelectShop;
    private Integer   mSelectRetailer;
    private Integer   mComeFrom;
    private Integer   mActionFrom;

    private Integer   mOperation;

    private TableLayout mViewTable;

    private List<Stock> mStocks;
    private List<LastSaleResponse> mLastStocks;

    public StockSelect() {
        // Required empty public constructor
    }

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
            mComeFrom  = getArguments().getInt(DiabloEnum.BUNDLE_PARAM_COME_FORM);
            mActionFrom = getArguments().getInt(DiabloEnum.BUNDLE_PARAM_ACTION);
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
        mViewTable.removeAllViews();

        if (DiabloEnum.SALE_IN.equals(mComeFrom)
            || DiabloEnum.SALE_OUT.equals(mComeFrom)) {
            if (R.string.add == mActionFrom){
                getStockFromServer();
                getLastTransactionOfRetailer();
            }
            else if (R.string.modify == mActionFrom) {
                startModify();
            }
        }
        else if (DiabloEnum.SALE_IN_UPDATE.equals(mComeFrom)) {
            if (R.string.add == mActionFrom){
                getStockFromServer();
            }
            else if (R.string.modify == mActionFrom) {
                startModify();
            }
        }

    }

    private void getStockFromServer(){
        MatchSingleStockTask task = new MatchSingleStockTask(
            mSaleStock.getStyleNumber(),
            mSaleStock.getBrandId(),
            mSelectShop);

        task.setMatchSingleStockTaskListener(new MatchSingleStockTask.OnMatchSingleStockTaskListener() {
            @Override
            public void onMatchSuccess(List<Stock> stocks) {
                Log.d(LOG_TAG, "success to get stock");
                mStocks = stocks;
                startAdd();
            }

            @Override
            public void onMatchFailed(Throwable t) {
                Log.d(LOG_TAG, "failed to get stock");
            }
        });

        task.getStock();
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
        if ( null == mSaleStock.getColors() || null == mSaleStock.getOrderSizes()) {
            ArrayList<String> usedSizes = new ArrayList<>();
            List<String> orderedSizes     = new ArrayList<>();
            List<DiabloColor> orderColors = new ArrayList<>();


            for(SaleStockAmount a: mSaleStock.getAmounts()){
                if (!usedSizes.contains(a.getSize())){
                    usedSizes.add(a.getSize());
                }

                DiabloColor color = Profile.instance().getColor(a.getColorId());
                if (!color.includeIn(orderColors)){
                    orderColors.add(color);
                }
            }

            ArrayList<String> sizes = Profile.instance().genSortedSizeNamesByGroups(mSaleStock.getSizeGroup());
            for (String s: sizes){
                if (usedSizes.contains(s)){
                    orderedSizes.add(s);
                }
            }

            if (orderedSizes.size() != usedSizes.size()){
                for (String s: usedSizes){
                    if (!orderedSizes.contains(s)){
                        orderedSizes.add(0, s);
                    }
                }
            }

            mSaleStock.setColors(orderColors);
            mSaleStock.setOrderSizes(orderedSizes);

            // startSelect(orderColors, orderedSizes);
        }

        startSelect(mSaleStock.getColors(), mSaleStock.getOrderSizes());
    }

    private void startAdd() {
        ArrayList<String> usedSizes   = new ArrayList<>();
        List<String> orderedSizes     = new ArrayList<>();
        List<DiabloColor> orderColors = new ArrayList<>();

        for(Stock s: mStocks){
            if (!usedSizes.contains(s.getSize())){
                usedSizes.add(s.getSize());
            }

            DiabloColor color = Profile.instance().getColor(s.getColorId());
            if (!color.includeIn(orderColors)){
                orderColors.add(color);
            }

            SaleStockAmount amount = mSaleStock.getAmount(s.getColorId(), s.getSize());
            if (null != amount) {
                amount.setStock(s.getExist());
            } else {
                amount = new SaleStockAmount(s.getColorId(), s.getSize());
                amount.setStock(s.getExist());
                mSaleStock.getAmounts().add(amount);
            }
        }

        ArrayList<String> sizes = Profile.instance().genSortedSizeNamesByGroups(mSaleStock.getSizeGroup());
        for (String s: sizes){
            if (usedSizes.contains(s)){
                orderedSizes.add(s);
            }
        }

        if (orderedSizes.size() != usedSizes.size()){
            for (String s: usedSizes){
                if (!orderedSizes.contains(s)){
                    orderedSizes.add(0, s);
                }
            }
        }

        mSaleStock.setColors(orderColors);
        mSaleStock.setOrderSizes(orderedSizes);
        startSelect(orderColors, orderedSizes);
    }

    private void startSelect(List<DiabloColor> colors, List<String> orderedSizes) {
        DiabloSaleTable saleTable = new DiabloSaleTable(getContext(), mViewTable, colors, orderedSizes);
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
                        String inputValue = editable.toString().trim();

                        for (SaleStockAmount amount: mSaleStock.getAmounts()){
                            if (amount.getColorId().equals(colorId) && amount.getSize().equals(size)){
                                if (inputValue.length() == 1 && inputValue.startsWith("-")){
                                    amount.setSellCount(0);
                                } else {
                                    amount.setSellCount(DiabloUtils.instance().toInteger(inputValue));
                                }
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
                mOperation = R.string.action_cancel;
                break;
            case 101: // save
                mOperation = R.string.action_save;
                break;
            default:
                break;
        }

        switchFragmentToSaleIn();
        return true;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // mListener = null;
    }

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

    public void setComeFrom(Integer comeFrom) {
        this.mComeFrom = comeFrom;
    }

    public void setSelectAction(Integer action) {
        this.mActionFrom = action;
    }

    private OnNoFreeStockSelectListener mNoFreeStockSelectListener =
        new OnNoFreeStockSelectListener() {
            @Override
            public Integer getCurrentOperation() {
                return mOperation;
            }

            @Override
            public SaleStock afterSelectStock() {
                return mSaleStock;
            }
        };

    private Fragment getSaleInFragment() {
        Fragment to = getFragmentManager().findFragmentByTag(DiabloEnum.TAG_SALE_IN);
        if (null != to ) {
            ((SaleIn)to).setNoFreeStockSelectListener(mNoFreeStockSelectListener);
        } else {
            to = new SaleIn();
        }

        ((SaleIn)to).setBackFrom(R.string.back_from_stock_select);
        return to;
    }

    private Fragment getSaleOutFragment() {
        Fragment to = getFragmentManager().findFragmentByTag(DiabloEnum.TAG_SALE_OUT);
        if (null != to ) {
            ((SaleOut)to).setNoFreeStockSelectListener(mNoFreeStockSelectListener);
        } else {
            to = new SaleOut();
        }

        ((SaleOut)to).setBackFrom(R.string.back_from_stock_select);

        return to;
    }

    private Fragment getSaleInUpdateFragment() {
        Fragment to = getFragmentManager().findFragmentByTag(DiabloEnum.TAG_SALE_IN_UPDATE);
        if (null != to ) {
            ((SaleInUpdate)to).setNoFreeStockSelectListener(mNoFreeStockSelectListener);
        } else {
            to = new SaleInUpdate();
        }

        ((SaleInUpdate)to).setBackFrom(R.string.back_from_stock_select);

        return to;
    }

    private void switchFragmentToSaleIn(){
        Fragment to = null;
        if (DiabloEnum.SALE_IN.equals(mComeFrom)) {
            to = getSaleInFragment();
            ((MainActivity)getActivity()).selectMenuItem(0);
        }
        else if (DiabloEnum.SALE_OUT.equals(mComeFrom)) {
            to = getSaleOutFragment();
            ((MainActivity)getActivity()).selectMenuItem(1);
        }
        else if (DiabloEnum.SALE_IN_UPDATE.equals(mComeFrom)) {
            to = getSaleInUpdateFragment();
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (null != to) {
            if (!to.isAdded()){
                transaction.hide(StockSelect.this).add(R.id.frame_container, to).commit();
            } else {
                transaction.hide(StockSelect.this).show(to).commit();
            }
        }
    }

    public interface OnNoFreeStockSelectListener {
        Integer   getCurrentOperation();
        SaleStock afterSelectStock();
    }
}
