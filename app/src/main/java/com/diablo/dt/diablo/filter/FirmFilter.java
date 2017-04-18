package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.FirmAdapter;
import com.diablo.dt.diablo.utils.DiabloAutoCompleteTextWatcher;

/**
 * Created by buxianhui on 17/4/15.
 */

public class FirmFilter extends DiabloFilter {
    public FirmFilter(Context context, String name) {
        super(context, name);
    }

    @Override
    public DiabloFilter copy() {
        return new FirmFilter(getContext(), getName());
    }

    @Override
    public void init() {
        final AutoCompleteTextView view = new AutoCompleteTextView(getContext());
        init(view);
    }

    @Override
    public void init(final View view) {
        setView(view);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view;
        autoCompleteTextView.setSelectAllOnFocus(true);

        new DiabloAutoCompleteTextWatcher(
            autoCompleteTextView,
            new DiabloAutoCompleteTextWatcher.DiabloAutoCompleteTextChangListener() {
                @Override
                public void afterTextChanged(String s) {
                    setSelectFilter(null);
                }
            }
        );

        new FirmAdapter(
            getContext(),
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
