package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.entity.DiabloEntity;
import com.diablo.dt.diablo.entity.DiabloType;
import com.diablo.dt.diablo.entity.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/8.
 */

public class MatchGoodTypeAdapter extends DiabloAdapter {
//    public MatchGoodTypeAdapter(Context context, Integer resource, Integer textViewResourceId) {
//        super(context, resource, textViewResourceId);
//    }

    public MatchGoodTypeAdapter(Context context, Integer resource, Integer textViewResourceId, AutoCompleteTextView view) {
        super(context, resource, textViewResourceId, view);
    }

    @Override
    public List<DiabloEntity> findItems(String s) {
        List<DiabloType> types = Profile.instance().getDiabloTypes();
        List<DiabloEntity> suggestions = new ArrayList<>();
        for (DiabloType type: types) {
            if (type.getName().toUpperCase().contains(s.toUpperCase())) {
                suggestions.add(type);
            }
        }
        return suggestions;
    }
}
