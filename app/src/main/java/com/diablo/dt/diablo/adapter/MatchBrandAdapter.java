package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.entity.DiabloBrand;
import com.diablo.dt.diablo.entity.DiabloEntity;
import com.diablo.dt.diablo.entity.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/8.
 */

public class MatchBrandAdapter extends DiabloAdapter{
    public MatchBrandAdapter(Context context, Integer resource, Integer textViewResourceId, AutoCompleteTextView view) {
        super(context, resource, textViewResourceId, view);
    }

    @Override
    public List<DiabloEntity> findItems(String s) {
        List<DiabloBrand> brands = Profile.instance().getBrands();
        List<DiabloEntity> suggestions = new ArrayList<>();
        for (DiabloBrand brand: brands) {
            if (brand.getName().toUpperCase().contains(s.toUpperCase())) {
                suggestions.add(brand);
            }
        }
        return suggestions;
    }
}
