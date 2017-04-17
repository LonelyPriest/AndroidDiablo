package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.entity.DiabloEntity;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/3/7.
 */


public class MatchStockAdapter extends DiabloAdapter {
    public MatchStockAdapter(Context context, Integer resource, Integer textViewResourceId, AutoCompleteTextView view) {
        super(context, resource, textViewResourceId, view);
    }

    @Override
    public List<DiabloEntity> findItems(String s) {
        List<MatchStock> stocks = Profile.instance().getMatchStocks();
        List<DiabloEntity> suggestions = new ArrayList<>();
        for (MatchStock stock: stocks) {
            if (stock.getName().toUpperCase().contains(s.toUpperCase())) {
                suggestions.add(stock);
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

