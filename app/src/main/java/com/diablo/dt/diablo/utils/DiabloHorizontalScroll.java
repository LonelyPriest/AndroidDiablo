package com.diablo.dt.diablo.utils;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created by buxianhui on 17/2/25.
 */

public class DiabloHorizontalScroll extends HorizontalScrollView {
    private GestureDetectorCompat mGestureDetect;

    public DiabloHorizontalScroll(Context context){
        super(context);
    }

    public DiabloHorizontalScroll(Context context, GestureDetectorCompat detect){
        super(context);
        mGestureDetect = detect;
    }

    public DiabloHorizontalScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mGestureDetect.onTouchEvent(ev))
            return true;
        else
            return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mGestureDetect.onTouchEvent(ev))
            return true;
        else
            return super.onInterceptTouchEvent(ev);
    }

    public void setGestureDetect(GestureDetectorCompat gesture){
        this.mGestureDetect = gesture;
    }
}
