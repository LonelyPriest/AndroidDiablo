package com.diablo.dt.diablo.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.MatchStockAdapter;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.model.stock.StockCalc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/5.
 */

public class MatchAllStockOfFirmTask extends AsyncTask<String, Void, Void> {
    private Context mContext;
    private StockCalc mStockCalc;
    private AutoCompleteTextView mCompleteView;
    private List<MatchStock> mMatchStocks;
    private List<MatchStock> mMatchedStocks = new ArrayList<>();

    public MatchAllStockOfFirmTask(
        Context context,
        StockCalc calc,
        AutoCompleteTextView view,
        List<MatchStock> stocks){

        this.mContext = context;
        this.mStockCalc = calc;
        this.mCompleteView = view;
        this.mMatchStocks = stocks;
    }

    @Override
    protected Void doInBackground(String... params) {
        mMatchedStocks.clear();
        for (MatchStock stock : mMatchStocks){
            if (stock.getFirmId().equals(mStockCalc.getFirm()) && stock.getName().toLowerCase().contains(params[0])) {
                mMatchedStocks.add(stock);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        MatchStockAdapter adapter = new MatchStockAdapter(
            mContext,
            R.layout.typeahead_match_stock_on_sale,
            R.id.typeahead_select_stock_on_sale,
            mMatchedStocks,
            null);

        mCompleteView.setDropDownHorizontalOffset(mCompleteView.getWidth());

        if (mMatchedStocks.size() > 10){
            mCompleteView.setDropDownVerticalOffset(-9999);
        }
        else{
            mCompleteView.setDropDownVerticalOffset(-mCompleteView.getHeight());
        }

        mCompleteView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
