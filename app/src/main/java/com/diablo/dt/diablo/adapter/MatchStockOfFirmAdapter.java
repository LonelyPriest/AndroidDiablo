package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.entity.DiabloEntity;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/17.
 */

public class MatchStockOfFirmAdapter extends MatchStockAdapter {
    private Integer mMatchFirm;

    public MatchStockOfFirmAdapter(
        Context context,
        Integer resource,
        Integer textViewResourceId,
        AutoCompleteTextView view,
        Integer matchFirm) {
        super(context, resource, textViewResourceId, view);
        this.mMatchFirm = matchFirm;
    }

    @Override
    public List<DiabloEntity> findItems(String s) {
        List<MatchStock> stocks = Profile.instance().getMatchStocks();
        List<DiabloEntity> suggestions = new ArrayList<>();
        for (MatchStock stock: stocks) {
            if (stock.getFirmId().equals(mMatchFirm)
                && stock.getName().toUpperCase().contains(s.toUpperCase())) {
                suggestions.add(stock);
            }
        }
        return suggestions;
    }
}
