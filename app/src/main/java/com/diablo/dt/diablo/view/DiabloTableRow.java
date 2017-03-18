package com.diablo.dt.diablo.view;

import android.content.Context;
import android.widget.TableRow;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by buxianhui on 17/3/18.
 */

public class DiabloTableRow {
    private Integer mSequence;
    private Map<String, DiabloCell> mCells;
    private TableRow mRow;

    public DiabloTableRow(Context context){
        this.mSequence = 0;
        this.mRow      = new TableRow(context);
        this.mCells    = new HashMap<>();
    }

    public void addCell(DiabloCell cell){
        mRow.addView(cell.getView());
        mCells.put(cell.getText(), cell);
    }

    public DiabloCell getCell(String key){
        return mCells.get(key);
    }

    public Integer getSequence(){
        return mSequence;
    }

    public void setSequence(Integer sequence){
        this.mSequence = sequence;
    }

    public TableRow getView(){
        return mRow;
    }
}
