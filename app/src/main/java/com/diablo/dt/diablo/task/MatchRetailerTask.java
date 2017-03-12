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
 * Created by buxianhui on 17/3/12.
 */

public class MatchRetailerTask extends AsyncTask<String, Void, Void> {
    private Context mContext;
    private AutoCompleteTextView mCompleteView;
    private List<Retailer> mOriginRetailers;
    private List<Retailer> mMatchedRetailers= new ArrayList<Retailer>();

    public MatchRetailerTask(Context context, AutoCompleteTextView view, List<Retailer> retailers){
        this.mContext = context;
        this.mCompleteView = view;
        mOriginRetailers = retailers;
    }

    @Override
    protected Void doInBackground(String... params) {
        mMatchedRetailers.clear();
        for (Retailer retailer : mOriginRetailers){
            if (retailer.getName().contains(params[0])) {
                mMatchedRetailers.add(retailer);
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

        mCompleteView.setDropDownHorizontalOffset(mCompleteView.getWidth());
        mCompleteView.setDropDownVerticalOffset(-90);

        mCompleteView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
