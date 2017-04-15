package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.task.MatchGoodTypeTask;
import com.diablo.dt.diablo.utils.DiabloTextWatcher;

/**
 * Created by buxianhui on 17/4/15.
 */

public class GoodTypeFilter extends DiabloFilter {
    public GoodTypeFilter(Context context, String name) {
        super(context, name);
    }

    @Override
    public DiabloFilter copy() {
        return new GoodTypeFilter(getContext(), getName());
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

        ((AutoCompleteTextView) view).addTextChangedListener(new DiabloTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                setSelectFilter(null);
                String name = editable.toString();
                new MatchGoodTypeTask(
                    getContext(),
                    ((AutoCompleteTextView) view),
                    Profile.instance().getDiabloTypes()).execute(name);
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
