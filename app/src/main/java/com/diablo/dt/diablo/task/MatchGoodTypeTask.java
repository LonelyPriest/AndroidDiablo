package com.diablo.dt.diablo.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.MatchGoodTypeAdapter;
import com.diablo.dt.diablo.entity.DiabloType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/8.
 */

public class MatchGoodTypeTask extends AsyncTask<String, Void, Void> {
    private Context mContext;
    private AutoCompleteTextView mCompleteView;
    private List<DiabloType> mOriginGoodTypes;
    private List<DiabloType> mMatchedGoodTypes = new ArrayList<>();

    public MatchGoodTypeTask(Context context, AutoCompleteTextView view, List<DiabloType> types){
        this.mContext = context;
        this.mCompleteView = view;
        this.mOriginGoodTypes = types;
    }

    @Override
    protected Void doInBackground(String... params) {
        mMatchedGoodTypes.clear();
        for (DiabloType type : mOriginGoodTypes){
            if (type.getName().contains(params[0])) {
                mMatchedGoodTypes.add(type);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        MatchGoodTypeAdapter adapter = new MatchGoodTypeAdapter(
            mContext,
            R.layout.typeahead_firm,
            R.id.typeahead_select_firm,
            mMatchedGoodTypes);

        mCompleteView.setAdapter(adapter);
        mCompleteView.setThreshold(1);

        if (null != mMatchedGoodTypes && mMatchedGoodTypes.size() > 0) {
            mCompleteView.setDropDownHorizontalOffset(mCompleteView.getWidth());
            mCompleteView.setDropDownVerticalOffset(-90);
        }

        adapter.notifyDataSetChanged();
    }
}
