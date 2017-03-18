package com.diablo.dt.diablo.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.R;

import static android.R.attr.inputType;

/**
 * Created by buxianhui on 17/3/18.
 */

public class DiabloAutoCompleteCell extends DiabloCell {
    public DiabloAutoCompleteCell(Context context, String text, Integer color, Integer size, Float weight) {
        super(context, text, color, size, weight);
    }

    public DiabloAutoCompleteCell(Context context, String text, Integer color, Integer size, Float weight, Integer gravity) {
        super(context, text, color, size, weight, gravity);
    }

    @Override
    public View createView(Context context) {
        return new AutoCompleteTextView(context);
    }

    @Override
    public void setLayout(String text, Integer color, Integer size) {
        ((AutoCompleteTextView)getView()).setTextColor(color);
        ((AutoCompleteTextView)getView()).setTextSize(size);
        ((AutoCompleteTextView)getView()).setText(text);

        ((AutoCompleteTextView)getView()).setHintTextColor(Color.GRAY);
        ((AutoCompleteTextView)getView()).setHint(R.string.please_input_good);
        ((AutoCompleteTextView)getView()).setRawInputType(inputType);
    }

    @Override
    public void setGravity(Integer gravity) {
        ((AutoCompleteTextView)getView()).setGravity(gravity);
    }
}
