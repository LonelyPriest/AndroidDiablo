package com.diablo.dt.diablo.view;

import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/3/18.
 */

public class DiabloTableView {
    private TableLayout mTable;
    private List<DiabloRowView> mRows;

    public DiabloTableView(TableLayout tableLayout){
        mTable = tableLayout;
        this.mRows = new ArrayList<>();
    }

    public void addRow(DiabloRowView row){
        mRows.add(0, row);
        mTable.addView(row.getView());
    }

    public DiabloRowView top(){
        return mRows.get(0);
    }

    public TableLayout getTable(){
        return this.mTable;
    }
}
