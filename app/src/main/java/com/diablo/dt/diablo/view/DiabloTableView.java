package com.diablo.dt.diablo.view;

import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/3/18.
 */

public class DiabloTableView {
    TableLayout mTableView;
    private List<DiabloRowView> mRows;

    public DiabloTableView(TableLayout table){
        this.mTableView = table;
        this.mRows = new ArrayList<>();
    }
}
