package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.task.FilterRetailerTask;
import com.diablo.dt.diablo.utils.DiabloEditTextWatcher;

/**
 * Created by buxianhui on 17/4/14.
 */

public class RetailerFilter extends DiabloFilter {
    public RetailerFilter(Context context, String name) {
        super(context, name);
    }

    @Override
    public DiabloFilter copy() {
        return new RetailerFilter(getContext(), getName());
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

        ((AutoCompleteTextView) view).addTextChangedListener(new DiabloEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                setSelectFilter(null);
                String name = editable.toString();
                new FilterRetailerTask(
                    getContext(),
                    ((AutoCompleteTextView) view),
                    Profile.instance().getRetailers()).execute(name);
            }
        });

        ((AutoCompleteTextView) view).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelectFilter(parent.getItemAtPosition(position));
            }
        });
    }
}
