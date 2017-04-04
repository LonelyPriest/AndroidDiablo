package com.diablo.dt.diablo.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.FirmAdapter;
import com.diablo.dt.diablo.entity.Firm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/4.
 */

public class MatchFirmTask extends AsyncTask<String, Void, Void> {
    private Context mContext;
    private AutoCompleteTextView mCompleteView;
    private List<Firm> mOriginFirms;
    private List<Firm> mMatchedFirms = new ArrayList<>();

    public MatchFirmTask(Context context, AutoCompleteTextView view, List<Firm> firms){
        this.mContext = context;
        this.mCompleteView = view;
        this.mOriginFirms = firms;
    }

    @Override
    protected Void doInBackground(String... params) {
        mMatchedFirms.clear();
        for (Firm firm : mOriginFirms){
            if (firm.getName().contains(params[0])) {
                mMatchedFirms.add(firm);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        FirmAdapter adapter = new FirmAdapter(
            mContext,
            R.layout.typeahead_firm,
            R.id.typeahead_select_firm,
            mMatchedFirms);

        mCompleteView.setAdapter(adapter);

        if (null != mMatchedFirms && mMatchedFirms.size() > 0) {
            mCompleteView.setDropDownHorizontalOffset(mCompleteView.getWidth());
            mCompleteView.setDropDownVerticalOffset(-90);
        }

        adapter.notifyDataSetChanged();
    }

}
