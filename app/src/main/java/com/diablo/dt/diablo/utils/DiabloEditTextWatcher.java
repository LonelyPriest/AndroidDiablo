package com.diablo.dt.diablo.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * Created by buxianhui on 17/3/19.
 */

public class DiabloEditTextWatcher implements TextWatcher {
    private EditText view;
    private DiabloEditTextChangListener listener;

    public DiabloEditTextWatcher(EditText view, DiabloEditTextChangListener listener) {
        this.view = view;
        this.view.addTextChangedListener(this);
        this.listener = listener;
    }

    public DiabloEditTextWatcher(EditText view) {
        this.view = view;
        this.view.addTextChangedListener(this);
        this.listener = null;
    }

    public DiabloEditTextWatcher() {
        this.view = null;
        this.listener = null;
    }

    public DiabloEditTextWatcher(View view, DiabloEditTextChangListener listener) {
        this.view = (EditText) view;
        this.view.addTextChangedListener(this);
        this.listener = listener;
    }

    public interface DiabloEditTextChangListener {
        void afterTextChanged(String s);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (null != listener && null != editable) {
            listener.afterTextChanged(editable.toString().trim());
        }
    }

    public void remove() {
        if (null != view) {
            view.removeTextChangedListener(this);
        }
    }
}
