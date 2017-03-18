package com.diablo.dt.diablo.view;

import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/3/18.
 */

public class DiabloTable {
    private TableLayout mTable;
    private List<DiabloTableRow> mRows;

    public DiabloTable(TableLayout tableLayout){
        mTable = tableLayout;
        this.mRows = new ArrayList<>();
    }

    public void addRow(DiabloTableRow row){
        mRows.add(0, row);
        mTable.addView(row.getView());
    }

    public DiabloTableRow top(){
        return mRows.get(0);
    }

    public TableLayout getTable(){
        return this.mTable;
    }
}
