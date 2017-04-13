package com.diablo.dt.diablo.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.RetailerAdapter;
import com.diablo.dt.diablo.entity.Retailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/13.
 */

public class FilterRetailerTask extends AsyncTask<String, Void, Void> {
    private Context mContext;
    private AutoCompleteTextView mCompleteView;
    private List<Retailer> mOriginRetailers;
    private List<Retailer> mMatchedRetailers = new ArrayList<>();

    public FilterRetailerTask(Context context, AutoCompleteTextView view, List<Retailer> retailers){
        this.mContext = context;
        this.mCompleteView = view;
        this.mOriginRetailers = retailers;
    }

    @Override
    protected Void doInBackground(String... params) {
        mMatchedRetailers.clear();
        for (Retailer r : mOriginRetailers){
            if (r.getName().toUpperCase().contains(params[0].toUpperCase())) {
                mMatchedRetailers.add(r);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        RetailerAdapter adapter = new RetailerAdapter(
            mContext,
            R.layout.typeahead_retailer,
            R.id.typeahead_select_retailer,
            mMatchedRetailers);

        mCompleteView.setAdapter(adapter);
        mCompleteView.setThreshold(1);
        // mCompleteView.setDropDownVerticalOffset(mCompleteView.getHeight());
        adapter.notifyDataSetChanged();
    }
}
