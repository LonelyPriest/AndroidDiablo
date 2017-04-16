package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.diablo.dt.diablo.adapter.StringArrayAdapter;
import com.diablo.dt.diablo.entity.Profile;

/**
 * Created by buxianhui on 17/4/16.
 */

public class StockTypeFilter extends DiabloFilter {
    public StockTypeFilter(Context context, String name) {
        super(context, name);
    }

    @Override
    public DiabloFilter copy() {
        return new StockTypeFilter(getContext(), getName());
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
            Profile.instance().getStockTypes());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ((Spinner)view).setAdapter(adapter);
        setSelectFilter( ((Spinner) view).getSelectedItemPosition() );

        ((Spinner) view).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSelectFilter(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setSelectFilter(null);
            }
        });
    }
}
