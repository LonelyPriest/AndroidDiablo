package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.entity.DiabloEntity;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.entity.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/4.
 */

public class FirmAdapter extends DiabloAdapter {

    public FirmAdapter(Context context, Integer resource, Integer textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public FirmAdapter(Context context, Integer resource, Integer textViewResourceId, AutoCompleteTextView view) {
        super(context, resource, textViewResourceId, view);
    }


    @Override
    public List<DiabloEntity> findItems(String s) {
        List<Firm> firms = Profile.instance().getFirms();
        List<DiabloEntity> suggestions = new ArrayList<>();
        for (Firm firm: firms) {
            if (firm.getName().toUpperCase().contains(s.toUpperCase())
                || firm.getPy().contains(s.toUpperCase())) {
                suggestions.add(firm);
            }
        }
        return suggestions;
    }
}
