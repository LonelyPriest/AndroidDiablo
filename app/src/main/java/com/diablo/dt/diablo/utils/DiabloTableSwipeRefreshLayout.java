package com.diablo.dt.diablo.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.AbsListView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

/**
 * Created by buxianhui on 17/3/13.
 */

public class DiabloTableSwipeRefreshLayout extends SwipyRefreshLayout implements AbsListView.OnScrollListener {
    private int mTouchSlop;
    private float mPrevX;

    public DiabloTableSwipeRefreshLayout(Context context) {
        super(context);
    }

    public DiabloTableSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);

                if (xDiff > mTouchSlop) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(event);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

}
