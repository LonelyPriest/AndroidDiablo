package com.diablo.dt.diablo.view;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

/**
 * Created by buxianhui on 17/3/18.
 */

public class DiabloEditTextCell extends DiabloCell {
    public DiabloEditTextCell(Context context, String text, Integer color, Integer size, Float weight) {
        super(context, text, color, size, weight);
    }

    public DiabloEditTextCell(Context context, String text, Integer color, Integer size, Float weight, Integer gravity) {
        super(context, text, color, size, weight, gravity);
    }

    @Override
    public View createView(Context context) {
        return new EditText(context);
    }

    @Override
    public void setLayout(String text, Integer color, Integer size) {
        ((EditText)getView()).setTextColor(color);
        ((EditText)getView()).setTextSize(size);
    }

    @Override
    public void setGravity(Integer gravity) {
        ((EditText)getView()).setGravity(gravity);
    }

    @Override
    public void setColor(Integer color) {
        ((EditText)getView()).setTextColor(color);
    }

    @Override
    public void setSize(Integer size) {
        ((EditText)getView()).setTextSize(size);
    }

    @Override
    public void setInputType(Integer inputType) {
        ((EditText)getView()).setInputType(inputType);
    }
}
