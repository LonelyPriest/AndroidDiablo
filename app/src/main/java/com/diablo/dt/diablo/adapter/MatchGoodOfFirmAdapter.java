package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.entity.DiabloEntity;
import com.diablo.dt.diablo.entity.MatchGood;
import com.diablo.dt.diablo.entity.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/17.
 */

public class MatchGoodOfFirmAdapter extends MatchGoodAdapter {
    private Integer mMatchFirm;

    public MatchGoodOfFirmAdapter(
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
        List<MatchGood> goods = Profile.instance().getMatchGoods();
        List<DiabloEntity> suggestions = new ArrayList<>();
        for (MatchGood good: goods) {
            if (good.getFirmId().equals(mMatchFirm)
                && good.getName().toUpperCase().contains(s.toUpperCase())) {
                suggestions.add(good);
            }
        }
        return suggestions;
    }
}
