package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.task.FilterRetailerTask;
import com.diablo.dt.diablo.utils.DiabloTextWatcher;

/**
 * Created by buxianhui on 17/4/14.
 */

public class RetailerFilter extends DiabloEntityFilter {
    public RetailerFilter(Context context, String name) {
        super(context, name);
        init();
    }

//    @Override
//    public View createView() {
//        return new AutoCompleteTextView(getContext());
//    }

    @Override
    public void init() {
        final AutoCompleteTextView view = new AutoCompleteTextView(getContext());
        init(view);
//        setView(view);
//        view.addTextChangedListener(new DiabloTextWatcher() {
//            @Override
//            public void afterTextChanged(Editable editable) {
//                String name = editable.toString();
//                new FilterRetailerTask(getContext(), view, Profile.instance().getRetailers()).execute(name);
//            }
//        });
//
//        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Retailer r = ((Retailer)parent.getItemAtPosition(position));
//                getListener().onItemSelect(r);
//            }
//        });
    }

    @Override
    public void init(final View view) {
        super.init(view);

        ((AutoCompleteTextView)view).addTextChangedListener(new DiabloTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                setSelectFilter(null);
                String name = editable.toString();
                new FilterRetailerTask(getContext(),
                    (AutoCompleteTextView) view,
                    Profile.instance().getRetailers()).execute(name);
            }
        });

        ((AutoCompleteTextView)view).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
