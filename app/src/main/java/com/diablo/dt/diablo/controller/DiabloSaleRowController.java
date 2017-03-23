package com.diablo.dt.diablo.controller;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.SpinnerStringAdapter;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.model.SaleStock;
import com.diablo.dt.diablo.model.SaleStockAmount;
import com.diablo.dt.diablo.task.MatchAllStockTask;
import com.diablo.dt.diablo.utils.AutoCompleteTextChangeListener;
import com.diablo.dt.diablo.utils.DiabloSaleAmountChangeWatcher;
import com.diablo.dt.diablo.utils.DiabloTextWatcher;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.DiabloCellLabel;
import com.diablo.dt.diablo.view.DiabloCellView;
import com.diablo.dt.diablo.view.DiabloRowView;

import java.util.List;

/**
 * Created by buxianhui on 17/3/18.
 */

public class DiabloSaleRowController {
    private SaleStock mSaleStock;
    private DiabloRowView mRowView;

    private final static DiabloUtils UTILS = DiabloUtils.instance();

    /**
     * listener
     */
    // good select
    private AutoCompleteTextChangeListener.TextWatch mOnAutoCompletedGoodListener;
    private AdapterView.OnItemClickListener mOnGoodClickListener;

    // price changed
    private TextWatcher mPriceListener;

    // price type
    private SpinnerStringAdapter mSelectPriceAdapter;

    // amount changer
    private TextWatcher mAmountListener;

    public DiabloSaleRowController(DiabloRowView rowView, SaleStock stock) {
        this.mSaleStock = stock;
        this.mRowView   = rowView;
        this.mRowView.getView().setTag(stock);
    }

    public void remove() {
        mRowView.getView().removeAllViews();
    }

    public void addCell(DiabloCellView cell) {
         mRowView.addCell(cell);
    }

    public DiabloRowView getView() {
        return mRowView;
    }

    public SaleStock getModel() {
        return  mSaleStock;
    }

    public void setGoodWatcher(
        final Context context,
        final List<MatchStock> stocks,
        final Integer selectPrice,
        final DiabloCellLabel [] labels,
        final OnActionAfterSelectGood listener) {

        final DiabloCellView cell = mRowView.getCell(R.string.good);
        cell.setCellFocusable(true);
        cell.requestFocus();

        ((AutoCompleteTextView)cell.getView()).setRawInputType(InputType.TYPE_CLASS_NUMBER);
        ((AutoCompleteTextView)cell.getView()).setDropDownWidth(500);
        ((AutoCompleteTextView)cell.getView()).setThreshold(1);

        // final AutoCompleteTextView cell = (AutoCompleteTextView) mRowView.getCell(R.string.good).getView();
        mOnAutoCompletedGoodListener = new AutoCompleteTextChangeListener.TextWatch() {
            @Override
            public void afterTextChanged(String s) {
                if (s.trim().length() > 0) {
                    new MatchAllStockTask(
                        context,
                        (AutoCompleteTextView) cell.getView(),
                        mRowView.getView(),
                        stocks).execute(s);
                }
            }
        };

        new AutoCompleteTextChangeListener((AutoCompleteTextView)cell.getView()).addListen(mOnAutoCompletedGoodListener);

        mOnGoodClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                MatchStock matchedStock = (MatchStock) adapterView.getItemAtPosition(pos);
                mSaleStock.init(matchedStock, selectPrice);

                for (DiabloCellLabel label: labels){
                    Integer key = label.getLabelId();
                    View v = mRowView.getCell(key).getView();
                    if (key.equals(R.string.good)) {
                        mRowView.setCellText(R.string.good, mSaleStock.getName());
                    }
                    else if (key.equals(R.string.price_type)) {
                        ((Spinner)v).setAdapter(mSelectPriceAdapter);
                        ((Spinner)v).setSelection(selectPrice - 1);
                    }
                    else if (key.equals(R.string.price)){
                        mRowView.setCellText(R.string.price, mSaleStock.getValidPrice());
                        mSaleStock.setFinalPrice(mSaleStock.getValidPrice());
                    }
                    else if (key.equals(R.string.discount)){
                        mRowView.setCellText(R.string.discount, mSaleStock.getValidPrice());
                    }
                    else if (key.equals(R.string.fprice)){
                        mRowView.setCellText(R.string.fprice, mSaleStock.getFinalPrice());
                        ((EditText)v).addTextChangedListener(mPriceListener);
                    }
                    else if (key.equals(R.string.amount)) {
                        listener.onActionOfAmount(DiabloSaleRowController.this, mRowView.getCell(key));
                        ((EditText)v).addTextChangedListener(mAmountListener);
                    }
                }
            }
        };

        ((AutoCompleteTextView)cell.getView()).setOnItemClickListener(mOnGoodClickListener);
    }

    public void setFinalPriceWatcher(final OnActionAfterSelectGood listener) {
        mPriceListener = new DiabloTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mSaleStock.setFinalPrice(UTILS.toFloat(editable.toString()));
                mRowView.setCellText(R.string.calculate, mSaleStock.getSalePrice());
                listener.onActionOfFPrice();
            }
        };
    }

    public void setAmountWatcher(Handler handler, DiabloSaleRowController controller) {
        mAmountListener = new DiabloSaleAmountChangeWatcher(handler, controller);
    }

    public void setPriceTypeAdapter(Context context, String [] priceTypes) {
        mSelectPriceAdapter = new SpinnerStringAdapter(
            context,
            android.R.layout.simple_spinner_dropdown_item,
            priceTypes);
        mSelectPriceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void setOrderId(Integer orderId) {
        mSaleStock.setOrderId(orderId);
        mRowView.setCellText(R.string.order_id, orderId);
    }

    /**
     * adapter model operation
     */
    public Float getSalePrice() {
        return mSaleStock.getSalePrice();
    }

    public Integer getSaleTotal() {
        return mSaleStock.getSaleTotal();
    }

    public void setSaleTotal(Integer total) {
        mSaleStock.setSaleTotal(total);
    }

    public void setStockExist(Integer exist) {
        mSaleStock.setStockExist(exist);
    }

    public Integer getOrderId() {
        return mSaleStock.getOrderId();
    }

    public void setColors(List<DiabloColor> colors) {
        mSaleStock.setColors(colors);
    }

    public void setOrderSizes(List<String> sizes) {
        mSaleStock.setOrderSizes(sizes);
    }

    public void clearAmount(){
        mSaleStock.clearAmounts();
    }

    public void addAmount(SaleStockAmount amount) {
        mSaleStock.addAmount(amount);
    }

    public interface OnActionAfterSelectGood {
        void onActionOfAmount(DiabloSaleRowController controller, DiabloCellView diabloCell);
        void onActionOfFPrice();
    }

}
