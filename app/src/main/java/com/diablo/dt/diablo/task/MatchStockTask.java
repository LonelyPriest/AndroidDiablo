package com.diablo.dt.diablo.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;
import android.widget.TableRow;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.MatchStockAdapter;
import com.diablo.dt.diablo.entity.MatchStock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/3/11.
 */

public class MatchStockTask extends AsyncTask<String, Void, Void> {
    private Context mContext;
    private AutoCompleteTextView mCompleteView;
    private TableRow mRow;
    private List<MatchStock> mMatchStocks;
    private List<MatchStock> mMatchedStocks= new ArrayList<MatchStock>();

    public MatchStockTask(Context context, AutoCompleteTextView view, TableRow row, List<MatchStock> stocks){
        this.mContext = context;
        this.mCompleteView = view;
        mMatchStocks = stocks;
        mRow = row;
    }

    @Override
    protected Void doInBackground(String... params) {
        mMatchedStocks.clear();
        for (MatchStock stock : mMatchStocks){
            if (stock.getName().contains(params[0])) {
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
                mRow);

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
