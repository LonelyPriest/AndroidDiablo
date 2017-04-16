package com.diablo.dt.diablo.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.entity.DiabloBrand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/8.
 */

public class MatchBrandTask extends AsyncTask <String, Void, Void>{
    private Context mContext;
    private AutoCompleteTextView mCompleteView;
    private List<DiabloBrand> mOriginBrands;
    private List<DiabloBrand> mMatchedBrands = new ArrayList<>();

    public MatchBrandTask(Context context, AutoCompleteTextView view, List<DiabloBrand> brands){
        this.mContext = context;
        this.mCompleteView = view;
        this.mOriginBrands = brands;
    }

    @Override
    protected Void doInBackground(String... params) {
        mMatchedBrands.clear();
        for (DiabloBrand brand : mOriginBrands){
            if (brand.getName().toUpperCase().contains(params[0].toUpperCase())) {
                mMatchedBrands.add(brand);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
//        MatchBrandAdapter adapter = new MatchBrandAdapter(
//            mContext,
//            R.layout.typeahead_firm,
//            R.id.typeahead_select_firm);
//
//        mCompleteView.setAdapter(adapter);
//        mCompleteView.setThreshold(1);
//
//        if (null != mMatchedBrands && mMatchedBrands.size() > 0) {
//            mCompleteView.setDropDownHorizontalOffset(mCompleteView.getWidth());
//            mCompleteView.setDropDownVerticalOffset(-90);
//        }
//
//        adapter.notifyDataSetChanged();
    }
}
