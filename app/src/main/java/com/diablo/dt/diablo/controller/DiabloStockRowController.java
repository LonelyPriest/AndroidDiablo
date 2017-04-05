package com.diablo.dt.diablo.controller;

import android.content.Context;
import android.os.Handler;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.MatchGood;
import com.diablo.dt.diablo.model.sale.SaleStock;
import com.diablo.dt.diablo.model.sale.SaleStockAmount;
import com.diablo.dt.diablo.model.stock.DiabloStockAmountChangeWatcher;
import com.diablo.dt.diablo.model.stock.EntryStock;
import com.diablo.dt.diablo.model.stock.EntryStockAmount;
import com.diablo.dt.diablo.model.stock.StockCalc;
import com.diablo.dt.diablo.task.MatchAllGoodTask;
import com.diablo.dt.diablo.utils.AutoCompleteTextChangeListener;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.DiabloCellLabel;
import com.diablo.dt.diablo.view.DiabloCellView;
import com.diablo.dt.diablo.view.DiabloRowView;

import java.util.List;

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
    private AutoCompleteTextChangeListener.TextWatch mOnAutoCompletedGoodListener;
    private AdapterView.OnItemClickListener mOnGoodClickListener;

    // amount changer
    private TextWatcher mAmountListener;

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

    public void setGoodWatcher(
        final Context context,
        final StockCalc calc,
        final List<MatchGood> goods,
        final DiabloCellLabel[] labels,
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
                    new MatchAllGoodTask(
                        context,
                        calc,
                        (AutoCompleteTextView) cell.getView(),
                        goods).execute(s);
                }
            }
        };

        new AutoCompleteTextChangeListener((AutoCompleteTextView)cell.getView()).addListen(mOnAutoCompletedGoodListener);

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
                        mRowView.setCellText(R.string.good, matchedGood.getName());
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

//    public void setRowWatcher() {
//        ((EditText)(mRowView.getCell(R.string.amount).getView())).addTextChangedListener(mAmountListener);
//    }

    public void setAmountWatcher(Handler handler, DiabloStockRowController controller) {
        mAmountListener = new DiabloStockAmountChangeWatcher(handler, controller);
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

        mRowView.setCellText(R.string.amount, total);
    }

    /**
     * adapter model operation
     */

    public Integer getEntryTotal() {
        return mEntryStock.getTotal();
    }

    public void setEntryTotal(Integer total) {
        mEntryStock.setTotal(total);
    }

    public Integer getOrderId() {
        return mEntryStock.getOrderId();
    }

    public Float calcStockPrice() {
        return mEntryStock.calcStockPrice();
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
