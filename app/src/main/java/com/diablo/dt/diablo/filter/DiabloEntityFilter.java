package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.view.View;

/**
 * Created by buxianhui on 17/4/14.
 */

public class DiabloEntityFilter {
    // private Integer mFilterId;
    private Context mContext;
    private String  mName;
    private View mView;

    private Object mSelectFilter;

    public DiabloEntityFilter(Context context, String name) {
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

    public String getName() {
        return mName;
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        this.mView = view;
    }

//    public View createView() {
//        return null;
//    }

    public Context getContext() {
        return mContext;
    }

    public void setSelectFilter (Object filter) {
        this.mSelectFilter = filter;
    }

    public Object getSelectFilter() {
        return mSelectFilter;
    }

}
