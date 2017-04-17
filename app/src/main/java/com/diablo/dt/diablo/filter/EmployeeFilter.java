package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.diablo.dt.diablo.adapter.EmployeeAdapter;
import com.diablo.dt.diablo.entity.Profile;

/**
 * Created by buxianhui on 17/4/15.
 */

public class EmployeeFilter extends DiabloFilter {
    public EmployeeFilter(Context context, String name) {
        super(context, name);
    }

    @Override
    public DiabloFilter copy() {
        return new EmployeeFilter(getContext(), getName());
    }

    @Override
    public void init() {
        final Spinner view = new Spinner(getContext());
        init(view);
    }

    @Override
    public void init(final View view) {
        setView(view);
        
        new EmployeeAdapter(getContext(), (Spinner) view, Profile.instance().getEmployees(), false);

        setSelectFilter( ((Spinner) view).getSelectedItem() );

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
