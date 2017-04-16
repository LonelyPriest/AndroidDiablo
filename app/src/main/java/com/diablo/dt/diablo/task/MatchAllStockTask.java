package com.diablo.dt.diablo.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.MatchStockAdapter;
import com.diablo.dt.diablo.entity.DiabloEntity;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/3/11.
 */

public class MatchAllStockTask extends AsyncTask<String, Void, Void> {
    private Context mContext;
    private AutoCompleteTextView mCompleteTextView;
    private List<DiabloEntity> mMatchedStocks;

    public MatchAllStockTask(Context context, AutoCompleteTextView view) {
        this.mContext = context;
        this.mCompleteTextView = view;

        mMatchedStocks = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(String... params) {
        mMatchedStocks.clear();
        for (MatchStock stock : Profile.instance().getMatchStocks()){
            if (stock.getName().toUpperCase().contains(params[0].toUpperCase())) {
                mMatchedStocks.add(stock);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        MatchStockAdapter adapter = new MatchStockAdapter(mContext,
            R.layout.typeahead_match_stock_on_sale,
            R.id.typeahead_select_stock_on_sale);

        adapter.setMatchedItems(mMatchedStocks);
        adapter.setUseFind(false);
        adapter.setView(mCompleteTextView);
        
        mCompleteTextView.setAdapter(adapter);


//        MatchStockAdapter adapter = new MatchStockAdapter(
//            mContext,
//            R.layout.typeahead_match_stock_on_sale,
//            R.id.typeahead_select_stock_on_sale,
//            mMatchedStocks,
//            mShowStyleNumberOnly);
//
//        mCompleteView.setDropDownHorizontalOffset(mCompleteView.getWidth());
//
//        if (mMatchedStocks.size() > 10){
//            mCompleteView.setDropDownVerticalOffset(-9999);
//        }
//        else{
//            mCompleteView.setDropDownVerticalOffset(-mCompleteView.getHeight());
//        }
//
//        mCompleteView.setAdapter(adapter);
//        mCompleteView.setThreshold(1);
//        adapter.notifyDataSetChanged();
    }
}
