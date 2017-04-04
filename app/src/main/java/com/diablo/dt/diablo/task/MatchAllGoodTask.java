package com.diablo.dt.diablo.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.MatchGoodAdapter;
import com.diablo.dt.diablo.entity.MatchGood;
import com.diablo.dt.diablo.model.stock.StockCalc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/4.
 */

public class MatchAllGoodTask extends AsyncTask<String, Void, Void> {
    private Context mContext;
    private StockCalc mStockCalc;
    private AutoCompleteTextView mCompleteView;
    private List<MatchGood> mMatchGoods;
    private List<MatchGood> mMatchedGoods = new ArrayList<>();

    public MatchAllGoodTask(Context context, StockCalc calc, AutoCompleteTextView view, List<MatchGood> goods){
        this.mContext = context;
        this.mStockCalc = calc;
        this.mCompleteView = view;
        this.mMatchGoods = goods;
    }

    @Override
    protected Void doInBackground(String... params) {
        mMatchedGoods.clear();
        for (MatchGood good : mMatchGoods){
            if (good.getFirmId().equals(mStockCalc.getFirm()) && good.getName().toLowerCase().contains(params[0])) {
                mMatchedGoods.add(good);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        MatchGoodAdapter adapter = new MatchGoodAdapter(
            mContext,
            R.layout.typeahead_match_stock_on_sale,
            R.id.typeahead_select_stock_on_sale,
            mMatchedGoods);

        mCompleteView.setDropDownHorizontalOffset(mCompleteView.getWidth());

        if (mMatchedGoods.size() > 10){
            mCompleteView.setDropDownVerticalOffset(-9999);
        }
        else{
            mCompleteView.setDropDownVerticalOffset(-mCompleteView.getHeight());
        }

        mCompleteView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
