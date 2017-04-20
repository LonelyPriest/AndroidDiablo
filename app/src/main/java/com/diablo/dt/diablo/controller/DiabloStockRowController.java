package com.diablo.dt.diablo.controller;

import android.content.Context;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.MatchGoodOfFirmAdapter;
import com.diablo.dt.diablo.adapter.MatchStockOfFirmAdapter;
import com.diablo.dt.diablo.entity.MatchGood;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.model.stock.DiabloStockAmountChangeWatcher;
import com.diablo.dt.diablo.model.stock.EntryStock;
import com.diablo.dt.diablo.model.stock.EntryStockAmount;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.DiabloCellLabel;
import com.diablo.dt.diablo.view.DiabloCellView;
import com.diablo.dt.diablo.view.DiabloRowView;

/**
 * Created by buxianhui on 17/4/4.
 */

public class DiabloStockRowController {
    private EntryStock mEntryStock;
    private DiabloRowView mRowView;

    private final static DiabloUtils UTILS = DiabloUtils.instance();

    /**
     * listener
     */
    // good select
    // private DiabloAutoCompleteTextWatcher.DiabloTextWatcher mOnAutoCompletedGoodListener;
    private AdapterView.OnItemClickListener mOnGoodClickListener;

    // amount changer
    private android.text.TextWatcher mAmountListener;

    public DiabloStockRowController(DiabloRowView rowView, EntryStock stock) {
        this.mEntryStock = stock;
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

    public EntryStock getModel() {
        return  mEntryStock;
    }

    public void setAutoCompleteGoodAdapter(Context context, Integer firm) {
        final DiabloCellView cell = mRowView.getCell(R.string.good);
        final AutoCompleteTextView cellView = (AutoCompleteTextView) cell.getView();

        cellView.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        cellView.setDropDownWidth(500);

        new MatchGoodOfFirmAdapter(
            context,
            R.layout.typeahead_match_stock_on_sale,
            R.id.typeahead_select_stock_on_sale,
            cellView,
            firm);
    }

    public void setAutoCompleteGoodListener(
        final Context context,
        Integer firm,
        final DiabloCellLabel[] labels,
        final OnActionAfterSelectGood listener) {

        final DiabloCellView cell = mRowView.getCell(R.string.good);
        // final AutoCompleteTextView cellView = (AutoCompleteTextView) cell.getView();
        cell.setCellFocusable(true);
        cell.requestFocus();
        // cellView.setThreshold(1);

        setAutoCompleteGoodAdapter(context, firm);

        mOnGoodClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                MatchGood matchedGood = (MatchGood) adapterView.getItemAtPosition(pos);
                mEntryStock.init(matchedGood);

                String [] seasons = context.getResources().getStringArray(R.array.seasons);
                for (DiabloCellLabel label: labels){
                    Integer key = label.getLabelId();
                    View v = mRowView.getCell(key).getView();
                    if (key.equals(R.string.good)) {
                        // mRowView.setCellText(R.string.good, matchedGood.getName());
                        mRowView.getCell(R.string.good).setCellFocusable(false);
                    }
                    else if (key.equals(R.string.year)) {
                        mRowView.setCellText(R.string.year, matchedGood.getYear());
                    }
                    else if (key.equals(R.string.season)) {
                        mRowView.setCellText(R.string.season, seasons[matchedGood.getSeason()]);
                    }
                    else if (key.equals(R.string.org_price)){
                        mRowView.setCellText(R.string.org_price, matchedGood.getOrgPrice());
                    }
                    else if (key.equals(R.string.amount)) {
                        listener.onActionOfAmount(DiabloStockRowController.this, mRowView.getCell(key));
                        ((EditText)v).addTextChangedListener(mAmountListener);
                    }
                }
            }
        };

        ((AutoCompleteTextView)cell.getView()).setOnItemClickListener(mOnGoodClickListener);
    }

    public void setAutoCompleteStockAdapter(Context context, Integer firm) {
        final DiabloCellView cell = mRowView.getCell(R.string.good);
        final AutoCompleteTextView cellView = (AutoCompleteTextView) cell.getView();

        cellView.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        cellView.setDropDownWidth(500);

        new MatchStockOfFirmAdapter(
            context,
            R.layout.typeahead_match_stock_on_sale,
            R.id.typeahead_select_stock_on_sale,
            cellView,
            firm);
    }

    public void setAutoCompleteStockListener(
        final Context context,
        Integer firm,
        final DiabloCellLabel[] labels,
        final OnActionAfterSelectGood listener) {

        final DiabloCellView cell = mRowView.getCell(R.string.good);
        final AutoCompleteTextView cellView = (AutoCompleteTextView)cell.getView();

        cell.setCellFocusable(true);
        cell.requestFocus();

        cellView.setRawInputType(InputType.TYPE_CLASS_NUMBER);
//        cellView.setDropDownWidth(500);
//        cellView.setThreshold(1);

        setAutoCompleteStockAdapter(context, firm);

        mOnGoodClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                MatchStock matchedStock = (MatchStock) adapterView.getItemAtPosition(pos);
                mEntryStock.init(matchedStock);

                String [] seasons = context.getResources().getStringArray(R.array.seasons);
                for (DiabloCellLabel label: labels){
                    Integer key = label.getLabelId();
                    View v = mRowView.getCell(key).getView();
                    if (key.equals(R.string.good)) {
                        // mRowView.setCellText(R.string.good, matchedStock.getName());
                        mRowView.getCell(R.string.good).setCellFocusable(false);
                    }
                    else if (key.equals(R.string.year)) {
                        mRowView.setCellText(R.string.year, matchedStock.getYear());
                    }
                    else if (key.equals(R.string.season)) {
                        mRowView.setCellText(R.string.season, seasons[matchedStock.getSeason()]);
                    }
                    else if (key.equals(R.string.org_price)){
                        mRowView.setCellText(R.string.org_price, matchedStock.getOrgPrice());
                    }
                    else if (key.equals(R.string.amount)) {
                        listener.onActionOfAmount(DiabloStockRowController.this, mRowView.getCell(key));
                        ((EditText)v).addTextChangedListener(mAmountListener);
                    }
                }
            }
        };

        cellView.setOnItemClickListener(mOnGoodClickListener);
    }

//    public void setRowWatcher() {
//        ((EditText)(mRowView.getCell(R.string.amount).getView())).addTextChangedListener(mAmountListener);
//    }

    public void setAmountWatcher(Handler handler, DiabloStockRowController controller) {
        mAmountListener = new DiabloStockAmountChangeWatcher(handler, controller);
    }

    public void addListenerOfAmountChange() {
        ((EditText)(mRowView.getCell(R.string.amount).getView())).addTextChangedListener(mAmountListener);
    }


    public void setOrderId(Integer orderId) {
        mEntryStock.setOrderId(orderId);
        mRowView.setCellText(R.string.order_id, orderId);
    }

    public boolean isSameModel(DiabloStockRowController controller) {
        return mEntryStock.isSame(controller.getModel());

    }

    public void replaceEntryStock(final EntryStock stock) {
        mEntryStock.clearAmounts();
        Integer total = 0;

        for (EntryStockAmount a: stock.getAmounts()) {
            mEntryStock.addAmount(a);
            total += a.getCount();
        }

        mEntryStock.setTotal(total);
        mEntryStock.setColors(stock.getColors());
        mEntryStock.setOrderSizes(stock.getOrderSizes());

        mRowView.setCellText(R.string.amount, total);
    }

    /**
     * adapter model operation
     */

    public Integer getEntryTotal() {
        return mEntryStock.getTotal();
    }

//    public void setEntryTotal(Integer total) {
//        mEntryStock.setTotal(total);
//    }

    public Integer getOrderId() {
        return mEntryStock.getOrderId();
    }

    public Float calcStockPrice() {
        return mEntryStock.calcStockPrice();
    }

    public void focusStyleNumber() {
        final DiabloCellView cell = mRowView.getCell(R.string.good);
        cell.setCellFocusable(true);
        cell.requestFocus();
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
        void onActionOfAmount(DiabloStockRowController controller, DiabloCellView diabloCell);
    }
}
