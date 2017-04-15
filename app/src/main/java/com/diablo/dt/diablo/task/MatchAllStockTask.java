package com.diablo.dt.diablo.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.MatchStockAdapter;
import com.diablo.dt.diablo.entity.MatchStock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/3/11.
 */

public class MatchAllStockTask extends AsyncTask<String, Void, Void> {
    private Context mContext;
    private AutoCompleteTextView mCompleteView;
    private boolean mShowStyleNumberOnly;
    private List<MatchStock> mOriginStocks;
    private List<MatchStock> mMatchedStocks= new ArrayList<>();

    public MatchAllStockTask(Context context,
                             AutoCompleteTextView view,
                             List<MatchStock> stocks,
                             boolean showStyleNumberOnly){
        this.mContext = context;
        this.mCompleteView = view;
        mOriginStocks = stocks;
        mShowStyleNumberOnly = showStyleNumberOnly;
    }

    @Override
    protected Void doInBackground(String... params) {
        mMatchedStocks.clear();
        for (MatchStock stock : mOriginStocks){
            if (stock.getName().toUpperCase().contains(params[0].toUpperCase())) {
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
            mShowStyleNumberOnly);

        mCompleteView.setDropDownHorizontalOffset(mCompleteView.getWidth());

        if (mMatchedStocks.size() > 10){
            mCompleteView.setDropDownVerticalOffset(-9999);
        }
        else{
            mCompleteView.setDropDownVerticalOffset(-mCompleteView.getHeight());
        }

        mCompleteView.setAdapter(adapter);
        mCompleteView.setThreshold(1);
        adapter.notifyDataSetChanged();
    }
}
