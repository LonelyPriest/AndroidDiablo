package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.view.View;

/**
 * Created by buxianhui on 17/4/14.
 */

public class DiabloFilter {
    // private Integer mFilterId;
    private Context mContext;
    private String  mName;
    private View mView;

    private Object mSelectFilter;

    public DiabloFilter(Context context, String name) {
        // mFilterId = DiabloEnum.INVALID_INDEX;
        this.mContext = context;
        this.mName = name;
        // this.mListener = listener;
    }

    public void init() {

    }

    public void init(View view) {
        this.mView = view;
    }

    public DiabloFilter copy() {
        return null;
    }

    public String getName() {
        return mName;
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        this.mView = view;
    }

    public Context getContext() {
        return mContext;
    }

    public void setSelectFilter (Object filter) {
        this.mSelectFilter = filter;
    }

    public Object getSelect() {
        return mSelectFilter;
    }
}
