package com.diablo.dt.diablo.utils;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.AbsListView;

/**
 * Created by buxianhui on 17/3/13.
 */

public class DiabloTableSwipRefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {
    private int mTouchSlop;
    // private TableLayout mTableLayout;
    private OnLoadListener mOnLoadListener;

    // private View mListViewFooter;

    private int mYDown;
    private int mLastY;
    private boolean isLoading = false;

    /**
     * @param context
     */
    public DiabloTableSwipRefreshLayout(Context context) {
        super(context);
    }

    public DiabloTableSwipRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
//        mListViewFooter = LayoutInflater.from(context).inflate(R.layout.listview_footer, null,
//                false);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mYDown = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                mLastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                if (canLoad()) {
                    loadData();
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp();
    }

    private boolean isBottom() {
        return true;
    }

    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

    private void loadData() {
        if (mOnLoadListener != null) {
            setLoading(true);
            setRefreshing(true);
            mOnLoadListener.onLoad();
        }
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        if (!isLoading){
            setRefreshing(false);
            mYDown = 0;
            mLastY = 0;
        }
    }

    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (canLoad()) {
            loadData();
        }
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        super.setRefreshing(refreshing);
    }

    public static interface OnLoadListener {
        public void onLoad();
    }
}
