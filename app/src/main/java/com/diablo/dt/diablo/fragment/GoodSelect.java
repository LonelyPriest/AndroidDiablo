package com.diablo.dt.diablo.fragment;


import com.google.gson.Gson;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
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
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.model.stock.DiabloGoodSelectTable;
import com.diablo.dt.diablo.model.stock.EntryStock;
import com.diablo.dt.diablo.model.stock.EntryStockAmount;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloTextWatcher;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.List;

public class GoodSelect extends Fragment {
    private TableLayout mViewTable;
    private EntryStock mEntryStock;
    // come from stock-in, stock-out, stock-in-update, stock-out-update
    private Integer mComeFrom;
    // action as add or modify
    private Integer mActionFrom;

    // save, cancel
    private Integer mOperation;

    public GoodSelect() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static GoodSelect newInstance(String param1, String param2) {
        GoodSelect fragment = new GoodSelect();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mComeFrom   = getArguments().getInt(DiabloEnum.BUNDLE_PARAM_COME_FORM);
            mActionFrom = getArguments().getInt(DiabloEnum.BUNDLE_PARAM_ACTION);
            mEntryStock = new Gson().fromJson(getArguments().getString(
                DiabloEnum.BUNDLE_PARAM_SALE_STOCK),
                EntryStock.class);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_good_select, container, false);
        mViewTable = (TableLayout) view.findViewById(R.id.good_select);

        ((AppCompatActivity)getActivity()).getSupportActionBar()
            .setTitle(getResources().getString(R.string.select_stock));

        init();

        return view;
    }

    private void init(){
        mViewTable.removeAllViews();

        if (DiabloEnum.STOCK_IN.equals(mComeFrom)
            || DiabloEnum.STOCK_OUT.equals(mComeFrom)) {
            if (R.string.add == mActionFrom){
                startAdd();

            }
            else if (R.string.modify == mActionFrom) {
                startAdd();
            }
        }
        else if (DiabloEnum.STOCK_IN_UPDATE.equals(mComeFrom)
            || DiabloEnum.STOCK_OUT_UPDATE.equals(mComeFrom)) {
            // getStockFromServer();
        }

    }

    private void startAdd() {
        for(DiabloColor color: mEntryStock.getColors()){
            for (String size: mEntryStock.getOrderSizes()) {
                if (null == mEntryStock.getAmount(color.getColorId(), size)) {
                    EntryStockAmount amount = new EntryStockAmount(color, size);
                    mEntryStock.addAmount(amount);
                }
            }
        }

        startSelect(mEntryStock.getColors(), mEntryStock.getOrderSizes());
    }

    private void startSelect(List<DiabloColor> colors, List<String> orderedSizes) {
        DiabloGoodSelectTable goodTable = new DiabloGoodSelectTable(getContext(), mViewTable, colors, orderedSizes);
        goodTable.setGoodListener(new DiabloGoodSelectTable.OnGoodSelectListener() {
            @Override
            public EntryStockAmount getGoodByColorAndSize(Integer colorId, String size) {
                return mEntryStock.getAmount(colorId, size);
            }

            @Override
            public void onGoodSelected(EditText cell, final Integer colorId, final String size) {
                cell.addTextChangedListener(new DiabloTextWatcher() {
                    @Override
                    public void afterTextChanged(Editable editable) {
                        String inputValue = editable.toString().trim();

                        EntryStockAmount amount = mEntryStock.getAmount(colorId, size);
                        if (null != amount) {
                            if (inputValue.length() == 1 && inputValue.startsWith("-")){
                                amount.setCount(0);
                            } else {
                                amount.setCount(DiabloUtils.instance().toInteger(inputValue));
                            }
                        }
                    }
                });
            }
        });

        goodTable.genHead();
        goodTable.genContent();
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

        switchFragment();
        return true;
    }

    private Fragment getStockInFragment() {
        Fragment to = getFragmentManager().findFragmentByTag(DiabloEnum.TAG_STOCK_IN);
        if (null != to ) {
            ((StockIn)to).setNoFreeGoodSelectListener(mNoFreeGoodSelectListener);
        } else {
            to = new StockIn();
        }

        ((StockIn)to).setBackFrom(R.string.back_from_good_select);
        return to;
    }

    private void switchFragment(){
        Fragment to = null;
        if (DiabloEnum.STOCK_IN.equals(mComeFrom)) {
            to = getStockInFragment();
            ((MainActivity)getActivity()).selectMenuItem(4);
        }
//        else if (DiabloEnum.SALE_OUT.equals(mComeFrom)) {
//            to = getSaleOutFragment();
//            ((MainActivity)getActivity()).selectMenuItem(1);
//        }
//        else if (DiabloEnum.SALE_IN_UPDATE.equals(mComeFrom)) {
//            to = getSaleInUpdateFragment();
//        }
//        else if (DiabloEnum.SALE_OUT_UPDATE.equals(mComeFrom)) {
//            to = getSaleOutUpdateFragment();
//        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (null != to) {
            if (!to.isAdded()){
                transaction.hide(GoodSelect.this).add(R.id.frame_container, to).commit();
            } else {
                transaction.hide(GoodSelect.this).show(to).commit();
            }
        }
    }

    public void setComeFrom(Integer comeFrom) {
        this.mComeFrom = comeFrom;
    }

    public void setSelectAction(Integer action) {
        this.mActionFrom = action;
    }

    public void setEntryStock(String entryStockJson) {
        this.mEntryStock = new Gson().fromJson(entryStockJson, EntryStock.class);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // utils.makeToast(getContext(), hidden ? "hidden" : "show");
        if (!hidden){
            init();
        }
    }

    private OnNoFreeGoodSelectListener mNoFreeGoodSelectListener =
        new OnNoFreeGoodSelectListener() {
            @Override
            public Integer getCurrentOperation() {
                return mOperation;
            }

            @Override
            public EntryStock afterSelectGood() {
                return mEntryStock;
            }
        };

    public interface OnNoFreeGoodSelectListener {
        Integer   getCurrentOperation();
        EntryStock afterSelectGood();
    }

}
