package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.task.MatchAllStockTask;

/**
 * Created by buxianhui on 17/4/15.
 */

public class StyleNumberFilter extends DiabloFilter {

    public StyleNumberFilter(Context context, String name) {
        super(context, name);
    }

    @Override
    public DiabloFilter copy() {
        return new StyleNumberFilter(getContext(), getName());
    }

    @Override
    public void init() {
        final AutoCompleteTextView view = new AutoCompleteTextView(getContext());
        init(view);

    }
    @Override
    public void init(View view) {
        super.init(view);

        ((AutoCompleteTextView)view).setSelectAllOnFocus(true);
        ((AutoCompleteTextView)view).setMaxLines(1);
        ((AutoCompleteTextView)view).setThreshold(1);

        addAutoCompletedTextWatcher();
    }

//    @Override
//    public void init(final View view) {
//        ((AutoCompleteTextView)view).setSelectAllOnFocus(true);
//        setView(view);
//
//        ((AutoCompleteTextView) view).addTextChangedListener(new DiabloEditTextWatcher() {
//            @Override
//            public void afterTextChanged(Editable editable) {
//                setSelectFilter(null);
//                String name = editable.toString();
//                new MatchAllStockTask(
//                    getContext(),
//                    ((AutoCompleteTextView) getView()),
//                    null,
//                    Profile.instance().getMatchStocks()).execute(name);
//            }
//        });
//
//        ((AutoCompleteTextView) view).setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View _view, int position, long id) {
//                MatchStock stock = (MatchStock) parent.getItemAtPosition(position);
//                // ((AutoCompleteTextView) view).setText(stock.getName());
//                setSelectFilter(stock);
//            }
//        });
//    }

    @Override
    public void startAutoComplete(String name) {
        AutoCompleteTextView view = ((AutoCompleteTextView) getView());
        new MatchAllStockTask(
            getContext(),
            view).execute(name);
    }
}
