package com.diablo.dt.diablo.view;

import android.content.Context;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by buxianhui on 17/3/18.
 */

public class DiabloRowView {
    private Map<String, View> mCells;
    private Context mContext;

    public DiabloRowView(Context context){
        this.mContext = context;
        this.mCells  = new HashMap<>();
    }

    public View getView(String title) {
        return mCells.get(title);
    }
}
