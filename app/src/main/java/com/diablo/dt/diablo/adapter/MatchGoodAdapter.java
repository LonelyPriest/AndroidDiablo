package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.entity.DiabloEntity;
import com.diablo.dt.diablo.entity.MatchGood;
import com.diablo.dt.diablo.entity.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/4.
 */

public class MatchGoodAdapter extends DiabloAdapter {
    public MatchGoodAdapter(Context context, Integer resource, Integer textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public MatchGoodAdapter(Context context, Integer resource, Integer textViewResourceId, AutoCompleteTextView view) {
        super(context, resource, textViewResourceId, view);
    }

    @Override
    public List<DiabloEntity> findItems(String s) {
        List<MatchGood> goods = Profile.instance().getMatchGoods();
        List<DiabloEntity> suggestions = new ArrayList<>();
        for (MatchGood good: goods) {
            if (good.getName().toUpperCase().contains(s.toUpperCase())) {
                suggestions.add(good);
            }
        }
        return suggestions;
    }

    @Override
    public void setDropDownOffset() {
        getView().setDropDownHorizontalOffset(getView().getWidth());

        if (getCount() > 10){
            getView().setDropDownVerticalOffset(-9999);
        }
        else{
            getView().setDropDownVerticalOffset(-getView().getHeight());
        }
    }
}
