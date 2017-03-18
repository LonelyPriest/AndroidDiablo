package com.diablo.dt.diablo.view;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

/**
 * Created by buxianhui on 17/3/18.
 */

public class DiabloTextCell extends DiabloCell {
    public DiabloTextCell(Context context, String text, Integer color, Integer size, Float weight) {
        super(context, text, color, size, weight);
    }

    public DiabloTextCell(Context context, String text, Integer color, Integer size, Float weight, Integer gravity) {
        super(context, text, color, size, weight, gravity);
    }

    @Override
    public View createView(Context context) {
        return new TextView(context);
    }

    @Override
    public void setLayout(String text, Integer color, Integer size) {
        ((TextView)getView()).setTextColor(color);
        ((TextView)getView()).setTextSize(size);
        // ((TextView)getView()).setText(text);
    }

    @Override
    public void setGravity(Integer gravity) {
        ((TextView)getView()).setGravity(gravity);
    }

    @Override
    public void setText() {
        ((TextView)getView()).setText(super.getText());
    }

    @Override
    public void setColor(Integer color) {
        ((TextView)getView()).setTextColor(color);
    }

    @Override
    public void setTextBold() {
        ((TextView)getView()).setTypeface(null, Typeface.BOLD);
    }
}
