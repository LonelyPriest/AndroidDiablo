package com.diablo.dt.diablo.utils;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import static android.R.attr.direction;

/**
 * Created by buxianhui on 17/2/25.
 */

public abstract class DiabloOnGestureLintener extends GestureDetector.SimpleOnGestureListener {
    private View mView;

    public DiabloOnGestureLintener(View view){
        super();
        this.mView = view;
    }

    public void actionOfOnLongpress(View view) {

    }

    public boolean actionOfOnDown(View view) {
        return false;
    }

    public boolean actionOfScroll(View view, Integer direction) {
        return false;
    }

    public boolean actionOfOnFlint(View view, Integer direction){
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        DiabloUtils u = DiabloUtils.getInstance();
        Integer direction = u.getSlope(e1.getX(), e1.getY(), e2.getX(), e2.getY());
        if (direction.equals(DiabloEnum.SWIPE_TOP)){
            if(e1.getY() - e2.getY() > DiabloEnum.SWIPE_MIN_DISTANCE
                    && Math.abs(velocityY) > DiabloEnum.SWIPE_THRESHOLD_VELOCITY) {
                // bottom to top
                return actionOfOnFlint(this.mView, DiabloEnum.SWIPE_TOP);
            }

        } else if (direction.equals(DiabloEnum.SWIPE_DOWN)){
            if (e2.getY() - e1.getY() > DiabloEnum.SWIPE_MIN_DISTANCE &&
                    Math.abs(velocityY) > DiabloEnum.SWIPE_THRESHOLD_VELOCITY) {
                //top to bottom
                return actionOfOnFlint(this.mView, DiabloEnum.SWIPE_DOWN);
            }
        } else {
            return actionOfOnFlint(this.mView, DiabloEnum.SWIPE_NONE);
        }

        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return actionOfOnDown(this.mView);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        actionOfOnLongpress(this.mView);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return actionOfScroll(this.mView, direction);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return false;
    }
}
