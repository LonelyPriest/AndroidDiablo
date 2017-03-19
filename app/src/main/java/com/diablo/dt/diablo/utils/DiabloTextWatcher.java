package com.diablo.dt.diablo.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by buxianhui on 17/3/19.
 */

public abstract class DiabloTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public abstract void afterTextChanged(Editable editable);
}
