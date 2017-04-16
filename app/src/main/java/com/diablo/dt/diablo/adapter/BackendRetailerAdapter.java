package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.diablo.dt.diablo.client.RetailerClient;
import com.diablo.dt.diablo.entity.DiabloEntity;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.rest.RetailerInterface;
import com.diablo.dt.diablo.utils.DiabloError;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by buxianhui on 17/3/5.
 */

public class BackendRetailerAdapter extends DiabloAdapter {
    public BackendRetailerAdapter(Context context, Integer resource, Integer textViewResourceId, AutoCompleteTextView view) {
        super(context, resource, textViewResourceId, view);
    }

    @Override
    public List<DiabloEntity> findItems(String s) {

        RetailerInterface face = RetailerClient.getClient().create(RetailerInterface.class);
        Call<List<Retailer>> call = face.matchRetailer(Profile.instance().getToken(), s);
        List<DiabloEntity> suggestions = new ArrayList<>();
        try {
            Response<List<Retailer>> response  = call.execute();
            if (null != response) {
                for (Retailer retailer: response.body()) {
                    suggestions.add(retailer);
                }
            }
        }
        catch (IOException e) {
            DiabloUtils.instance().makeToast(getContext(), DiabloError.getError(202), Toast.LENGTH_SHORT);
        }

//        List<Retailer> retailers = Profile.instance().getRetailers();
//        List<DiabloEntity> suggestions = new ArrayList<>();
//        for (Retailer retailer: retailers) {
//            if (retailer.getName().toUpperCase().contains(s.toUpperCase())) {
//                suggestions.add(retailer);
//            }
//        }
        return suggestions;
    }
}
