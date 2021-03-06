package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.diablo.dt.diablo.adapter.StringArrayAdapter;
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
    }

    @Override
    public void init(final View view) {
        setView(view);

        StringArrayAdapter adapter = new StringArrayAdapter(
            getContext(),
            android.R.layout.simple_spinner_item,
            Profile.instance().getDiabloYears());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ((Spinner)view).setAdapter(adapter);

        Integer currentSelectPosition = adapter.getPosition(
            DiabloUtils.instance().toString(DiabloUtils.instance().currentYear()));
        ((Spinner)view).setSelection(currentSelectPosition);
        
        setSelectFilter( ((Spinner) view).getSelectedItemPosition() );

        ((Spinner) view).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSelectFilter(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setSelectFilter(null);
            }
        });
    }
}
