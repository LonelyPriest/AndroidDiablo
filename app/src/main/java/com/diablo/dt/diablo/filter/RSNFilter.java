package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.text.Editable;
import android.widget.EditText;

import com.diablo.dt.diablo.utils.DiabloTextWatcher;

/**
 * Created by buxianhui on 17/4/14.
 */

public class RSNFilter extends DiabloEntityFilter {
    private String mInputRsn;

    public RSNFilter(Context context, String name) {
        super(context, name);
    }

    @Override
    public void init() {
        final EditText view = new EditText(getContext());
        setView(view);
        view.addTextChangedListener(new DiabloTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mInputRsn = editable.toString().trim();
                setSelectFilter(mInputRsn);
//                mLast = rsn;
//                getListener().onItemSelect(rsn);
            }
        });
    }
}
