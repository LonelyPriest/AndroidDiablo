package com.diablo.dt.diablo.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

/**
 * Created by buxianhui on 17/3/12.
 */

public class AutoCompleteTextChangeListener {
    private AutoCompleteTextView view;

    public AutoCompleteTextChangeListener(AutoCompleteTextView view) {
        this.view = view;
    }

    public interface TextWatch{
        void afterTextChanged(String value);
    }

    public void addListen(final TextWatch watch){
        this.view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                watch.afterTextChanged(editable.toString().trim());
            }
        });
    }
}
