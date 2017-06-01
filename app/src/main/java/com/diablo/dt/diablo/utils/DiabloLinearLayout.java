package com.diablo.dt.diablo.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by buxianhui on 17/6/1.
 */

public class DiabloLinearLayout extends LinearLayout {
    public DiabloLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(0, 0);
    }
}
