package com.diablo.dt.diablo.controller;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.MatchStockAdapter;
import com.diablo.dt.diablo.adapter.StringArrayAdapter;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.model.sale.DiabloSaleAmountChangeWatcher;
import com.diablo.dt.diablo.model.sale.SaleStock;
import com.diablo.dt.diablo.model.sale.SaleStockAmount;
import com.diablo.dt.diablo.utils.DiabloEditTextWatcher;
import com.diablo.dt.diablo.utils.DiabloEnum;
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
    // private DiabloAutoCompleteTextWatcher.DiabloTextWatcher mOnAutoCompletedGoodListener;
    private AdapterView.OnItemClickListener mOnStockClickListener;

    // price changed
    private android.text.TextWatcher mPriceWatcher;

    // price type
    private StringArrayAdapter mSelectPriceAdapter;

    // amount changer
    private android.text.TextWatcher mAmountWatcher;

    // comment changed
    private android.text.TextWatcher mCommentWatcher;

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
        final AutoCompleteTextView cellView = (AutoCompleteTextView)cell.getView();

        cell.setCellFocusable(true);
        cell.requestFocus();

        cellView.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        cellView.setDropDownWidth(500);
        // cellView.setThreshold(1);

        new MatchStockAdapter(
            context,
            R.layout.typeahead_match_stock_on_sale,
            R.id.typeahead_select_stock_on_sale,
            cellView);

        mOnStockClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                MatchStock matchedStock = (MatchStock) adapterView.getItemAtPosition(pos);
                mSaleStock.init(matchedStock, selectPrice);

                for (DiabloCellLabel label: labels){
                    Integer key = label.getLabelId();
                    View v = mRowView.getCell(key).getView();
                    if (key.equals(R.string.good)) {
                        mRowView.getCell(R.string.good).setCellFocusable(false);
                        // mRowView.setCellText(R.string.good, mSaleStock.getName());
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
                        mRowView.setCellText(R.string.discount, mSaleStock.getDiscount());
                    }
                    else if (key.equals(R.string.fprice)){
                        mRowView.setCellText(R.string.fprice, mSaleStock.getFinalPrice());
                        ((EditText)v).addTextChangedListener(mPriceWatcher);
                    }
                    else if (key.equals(R.string.amount)) {
                        listener.onActionOfAmount(DiabloSaleRowController.this, mRowView.getCell(key));
                        ((EditText)v).addTextChangedListener(mAmountWatcher);
                    }
                    else if (key.equals(R.string.comment)) {
                        setCommentWatcher();
                    }
                }
            }
        };

        cellView.setOnItemClickListener(mOnStockClickListener);
    }

    public void setRowWatcher() {
        ((EditText)(mRowView.getCell(R.string.fprice).getView())).addTextChangedListener(mPriceWatcher);
        ((EditText)(mRowView.getCell(R.string.amount).getView())).addTextChangedListener(mAmountWatcher);
        setCommentWatcher();
    }

    public void setFinalPriceWatcher(final OnActionAfterSelectGood listener) {
        mPriceWatcher = new DiabloEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mSaleStock.setFinalPrice(UTILS.toFloat(editable.toString()));
                mRowView.setCellText(R.string.calculate, mSaleStock.getSalePrice());
                listener.onActionOfFPrice();
            }
        };
    }

    public void setAmountWatcher(Handler handler, DiabloSaleRowController controller) {
        mAmountWatcher = new DiabloSaleAmountChangeWatcher(handler, controller);
    }

    public void setCommentWatcher() {
        mCommentWatcher = new DiabloEditTextWatcher(
            mRowView.getCell(R.string.comment).getView(),
            new DiabloEditTextWatcher.DiabloEditTextChangListener() {
            @Override
            public void afterTextChanged(String s) {
                mSaleStock.setComment(s);
            }
        });
    }

    public void setPriceTypeAdapter(Context context, String [] priceTypes) {
        mSelectPriceAdapter = new StringArrayAdapter(
            context,
            android.R.layout.simple_spinner_dropdown_item,
            priceTypes);
        mSelectPriceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void setPriceTypeAdapter(
        Context context, Integer cellId, String [] priceTypes, Integer selectedPrice) {
        setPriceTypeAdapter(context, priceTypes);
        Spinner cell = (Spinner)mRowView.getCell(cellId).getView();
        cell.setAdapter(mSelectPriceAdapter);
        cell.setSelection(selectedPrice - 1);
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

    public boolean isSameModel(DiabloSaleRowController controller) {
        return mSaleStock.isSame(controller.getModel());

    }

    /**
     * adapter view
     */

    public void setCellText(Integer cellId, String text){
        if (null != text) {
            mRowView.setCellText(cellId, text);
        }
    }

    public interface OnActionAfterSelectGood {
        void onActionOfAmount(DiabloSaleRowController controller, DiabloCellView diabloCell);
        void onActionOfFPrice();
    }

    public View focusStyleNumber() {
        final DiabloCellView cell = mRowView.getCell(R.string.good);
        cell.setCellFocusable(true);
        cell.requestFocus();
        return cell.getView();
    }

    public void replaceSaleStock(Context context, final SaleStock stock) {
        mSaleStock.clearAmounts();
        Integer saleTotal = 0;
        Integer exist = 0;

        for (SaleStockAmount a: stock.getAmounts()) {
            mSaleStock.addAmount(a);
            exist += a.getStock();
            if (a.getSellCount() != 0){
                saleTotal += a.getSellCount();
            }
        }

        mSaleStock.setColors(stock.getColors());
        mSaleStock.setOrderSizes(stock.getOrderSizes());
        mSaleStock.setSaleTotal(saleTotal);
        mSaleStock.setStockExist(exist);

        mSaleStock.setFinalPrice(stock.getFinalPrice());
        mSaleStock.setDiscount(stock.getDiscount());
        mSaleStock.setSecond(stock.getSecond());
        mSaleStock.setSelectedPrice(stock.getSelectedPrice());

        if (mSaleStock.getSecond().equals(DiabloEnum.DIABLO_TRUE)) {
            mRowView.setCellText(R.string.fprice, mSaleStock.getFinalPrice());
            mRowView.setCellText(R.string.discount, mSaleStock.getDiscount());
            ((Spinner) mRowView.getCell(R.string.price_type).getView()).setSelection(mSaleStock.getSelectedPrice() - 1);

            mRowView.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.yellowLight));
        }

        mRowView.setCellText(R.string.amount, saleTotal);
        mRowView.setCellText(R.string.stock, exist);
    }

}
