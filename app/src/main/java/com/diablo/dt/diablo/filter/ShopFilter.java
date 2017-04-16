package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.diablo.dt.diablo.adapter.ShopAdapter;
import com.diablo.dt.diablo.entity.Profile;

/**
 * Created by buxianhui on 17/4/14.
 */

public class ShopFilter extends DiabloFilter {
    public ShopFilter(Context context, String name) {
        super(context, name);
    }

    @Override
    public DiabloFilter copy() {
        return new ShopFilter(getContext(), getName());
    }

    @Override
    public void init() {
        final Spinner view = new Spinner(getContext());
        init(view);
    }

    @Override
    public void init(final View view) {
        // super.init(view);
        // final Spinner view = new Spinner(getContext());
        setView(view);
        ShopAdapter adapter = new ShopAdapter(
            getContext(),
            android.R.layout.simple_spinner_item,
            Profile.instance().getSortShop());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ((Spinner) view).setAdapter(adapter);
        setSelectFilter(((Spinner) view).getSelectedItem());
        // ((AppCompatSpinner) view).setSelection(0);

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
