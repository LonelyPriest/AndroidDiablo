package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.diablo.dt.diablo.utils.DiabloTextWatcher;

/**
 * Created by buxianhui on 17/4/14.
 */

public class RSNFilter extends DiabloFilter {
    public RSNFilter(Context context, String name) {
        super(context, name);
    }

    @Override
    public DiabloFilter copy() {
        return new RSNFilter(getContext(), getName());
    }

    public void init() {
        final EditText view = new EditText(getContext());
        init(view);
    }

    @Override
    public void init(final View view) {
        setView(view);
        ((EditText)view).setSelectAllOnFocus(true);
        ((EditText) view).addTextChangedListener(new DiabloTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString().trim();
                if (0 == s.length()) {
                    setSelectFilter(null);
                } else {
                    setSelectFilter(editable.toString().trim());
                }
            }
        });
    }
}
