package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.MatchBrandAdapter;

/**
 * Created by buxianhui on 17/4/15.
 */

public class BrandFilter extends DiabloFilter {
    public BrandFilter(Context context, String name) {
        super(context, name);
    }

    @Override
    public DiabloFilter copy() {
        return new BrandFilter(getContext(), getName());
    }

    @Override
    public void init() {
        final AutoCompleteTextView view = new AutoCompleteTextView(getContext());
        init(view);
    }

    @Override
    public void init(final View view) {
        ((AutoCompleteTextView)view).setSelectAllOnFocus(true);
        setView(view);
        
        new MatchBrandAdapter(getContext(),
            R.layout.typeahead_firm,
            R.id.typeahead_select_firm,
            ((AutoCompleteTextView) view));

        ((AutoCompleteTextView) view).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelectFilter(parent.getItemAtPosition(position));
            }
        });
    }
}
