package com.diablo.dt.diablo.utils;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

/**
 * Created by buxianhui on 17/3/12.
 */

public class DiabloAutoCompleteTextWatcher implements TextWatcher{
    private AutoCompleteTextView view;
    private DiabloAutoCompleteTextChangListener listener;

    public DiabloAutoCompleteTextWatcher(AutoCompleteTextView view, DiabloAutoCompleteTextChangListener listener) {
        this.view = view;
        view.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        view.addTextChangedListener(this);

        this.listener = listener;
    }

    public interface DiabloAutoCompleteTextChangListener {
        void afterTextChanged(String s);
    }

    public void remove() {
        view.removeTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (null != listener && null != editable) {
            listener.afterTextChanged(editable.toString().trim());
        }
    }
}
