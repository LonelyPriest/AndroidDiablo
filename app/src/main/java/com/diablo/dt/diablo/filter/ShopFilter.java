package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.diablo.dt.diablo.adapter.SpinnerShopAdapter;
import com.diablo.dt.diablo.entity.Profile;

/**
 * Created by buxianhui on 17/4/14.
 */

public class ShopFilter extends DiabloEntityFilter {
    public ShopFilter(Context context, String name) {
        super(context, name);
    }

    @Override
    public void init() {
        final Spinner view = new Spinner(getContext());
        init(view);
    }

    @Override
    public void init(final View view) {
        super.init(view);

        SpinnerShopAdapter adapter = new SpinnerShopAdapter(
            getContext(),
            android.R.layout.simple_spinner_item,
            Profile.instance().getSortShop());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)view).setAdapter(adapter);

        ((Spinner)view).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
