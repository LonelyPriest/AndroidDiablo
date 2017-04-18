package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.MatchStockAdapter;
import com.diablo.dt.diablo.utils.DiabloAutoCompleteTextWatcher;

/**
 * Created by buxianhui on 17/4/15.
 */

public class StockStyleNumberFilter extends DiabloFilter {

    public StockStyleNumberFilter(Context context, String name) {
        super(context, name);
    }

    @Override
    public DiabloFilter copy() {
        return new StockStyleNumberFilter(getContext(), getName());
    }

    @Override
    public void init() {
        final AutoCompleteTextView view = new AutoCompleteTextView(getContext());
        init(view);

    }
    @Override
    public void init(View view) {
        super.init(view);
        setView(view);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view;
        autoCompleteTextView.setSelectAllOnFocus(true);

        autoCompleteTextView.addTextChangedListener(new DiabloAutoCompleteTextWatcher(
            autoCompleteTextView,
            new DiabloAutoCompleteTextWatcher.DiabloAutoCompleteTextChangListener() {
                @Override
                public void afterTextChanged(String s) {
                    setSelectFilter(null);
                }
            }
        ));

        // does not offset
        new MatchStockAdapter(getContext(),
            R.layout.typeahead_match_stock_on_sale,
            R.id.typeahead_select_stock_on_sale,
            autoCompleteTextView,
            false);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelectFilter(parent.getItemAtPosition(position));
            }
        });
    }

}
