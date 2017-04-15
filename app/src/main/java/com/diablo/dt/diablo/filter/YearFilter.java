package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.widget.Spinner;

import com.diablo.dt.diablo.adapter.SpinnerStringAdapter;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.utils.DiabloUtils;

/**
 * Created by buxianhui on 17/4/15.
 */

public class YearFilter extends DiabloFilter {
    public YearFilter(Context context, String name) {
        super(context, name);
    }

    @Override
    public DiabloFilter copy() {
        return new YearFilter(getContext(), getName());
    }

    @Override
    public void init() {
        final Spinner view = new Spinner(getContext());
        init(view);
        addSpinnerWatcher();
    }

    @Override
    public void createAdapter() {
        SpinnerStringAdapter adapter = new SpinnerStringAdapter(
            getContext(),
            android.R.layout.simple_spinner_item,
            Profile.instance().getDiabloYears());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ((Spinner)getView()).setAdapter(adapter);

        Integer position = adapter.getPosition(
            DiabloUtils.instance().toString(DiabloUtils.instance().currentYear()));
        ((Spinner) getView()).setSelection(position);
    }
}
