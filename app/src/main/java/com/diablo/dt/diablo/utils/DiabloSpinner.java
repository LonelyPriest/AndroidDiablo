package com.diablo.dt.diablo.utils;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by buxianhui on 17/4/15.
 */

public class DiabloSpinner extends AppCompatSpinner{

    public DiabloSpinner(Context context) {
        super(context);
    }

    public DiabloSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DiabloSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setSelection(int position, boolean animate) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position, animate);

        if (sameSelected) {
            if (null != getOnItemSelectedListener()) {
                getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
            }
        }
    }

    @Override
    public void setSelection(int position) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position);
        if (sameSelected) {
            if (null != getOnItemSelectedListener()) {
                getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
            }
        }
    }

}
