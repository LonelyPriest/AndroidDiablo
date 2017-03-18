package com.diablo.dt.diablo.view;

import android.content.Context;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by buxianhui on 17/3/18.
 */

public class DiabloSpinnerCell extends DiabloCell {
    public DiabloSpinnerCell(Context context, String text, Integer color, Integer size, Float weight) {
        super(context, text, color, size, weight);
    }

    public DiabloSpinnerCell(Context context, String text, Integer color, Integer size, Float weight, Integer gravity) {
        super(context, text, color, size, weight, gravity);
    }

    @Override
    public View createView(Context context) {
        return new Spinner(context);
    }

    @Override
    public void setLayout(String text, Integer color, Integer size) {

    }

    @Override
    public void setGravity(Integer gravity) {
        ((TextView)getView()).setGravity(gravity);
    }
}
