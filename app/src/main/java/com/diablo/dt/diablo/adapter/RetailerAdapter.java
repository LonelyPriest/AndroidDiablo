package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.entity.DiabloEntity;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/17.
 */

public class RetailerAdapter extends DiabloAdapter{
    public RetailerAdapter(Context context, Integer resource, Integer textViewResourceId, AutoCompleteTextView view) {
        super(context, resource, textViewResourceId, view);
    }

    @Override
    public List<DiabloEntity> findItems(String s) {
        List<Retailer> retailers = Profile.instance().getRetailers();
        List<DiabloEntity> suggestions = new ArrayList<>();
        for (Retailer retailer: retailers) {
            // String py = Pinyin.toPinyin(retailer.getName(), DiabloEnum.EMPTY_STRING);
            // Log.d("retailer PY:", py);
            if (retailer.getName().toUpperCase().contains(s.toUpperCase())
                || retailer.getPy().contains(s.toUpperCase())) {
                suggestions.add(retailer);
            }
        }
        return suggestions;
    }
}
