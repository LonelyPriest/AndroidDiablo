package com.diablo.dt.diablo.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.client.RetailerClient;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.rest.RetailerInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by buxianhui on 17/3/12.
 */

public class MatchRetailerTask extends AsyncTask<String, Void, Void> {
    private final static String LOG_TAG = "MatchRetailerTask";
    private Context mContext;
    private AutoCompleteTextView mCompleteView;
    // private List<Retailer> mOriginRetailers;
    private List<Retailer> mMatchedRetailers;

    public MatchRetailerTask(Context context, AutoCompleteTextView view){
        this.mContext = context;
        this.mCompleteView = view;
    }

    @Override
    protected Void doInBackground(String... params) {

        RetailerInterface face = RetailerClient.getClient().create(RetailerInterface.class);
        Call<List<Retailer>> call = face.matchRetailer(Profile.instance().getToken(), params[0]);
        try {
            Response<List<Retailer>> response  = call.execute();
            if (null != response) {
                mMatchedRetailers = new ArrayList<>(response.body());
            }
        }
        catch (IOException e) {
            Log.d(LOG_TAG, "failed to get retailer");
        }

        // matchRetailer(params[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
//        if (null != mMatchedRetailers && mMatchedRetailers.size() > 0) {
//            RetailerAdapter adapter = new RetailerAdapter(
//                mContext,
//                R.layout.typeahead_retailer,
//                R.id.typeahead_select_retailer,
//                mMatchedRetailers);
//
//            mCompleteView.setDropDownHorizontalOffset(mCompleteView.getWidth());
//            mCompleteView.setDropDownVerticalOffset(-90);
//
//            mCompleteView.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
//        }
    }
}
