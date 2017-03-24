package com.diablo.dt.diablo.view;

import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

/**
 * Created by buxianhui on 17/3/18.
 */

public class DiabloRowView {
    private TableRow mRow;
    private SparseArray<DiabloCellView> mCells;

    public DiabloRowView(TableRow row){
        this.mRow = row;
        this.mCells  = new SparseArray<>();
    }

    public TableRow getView() {
        return mRow;
    }

    public SparseArray<DiabloCellView> getCells() {
        return mCells;
    }

    public void addCell(DiabloCellView cell) {
        mRow.addView(cell.getView());
        mCells.put(cell.getCellId(), cell);
    }

    public DiabloCellView getCell(Integer cellId) {
        return mCells.get(cellId);
    }

    public void setCellText(Integer cellId, Integer value) {
        setCellText(cellId, DiabloUtils.instance().toString(value));
    }

    public void setCellText(Integer cellId, Float value) {
        setCellText(cellId, DiabloUtils.instance().toString(value));
    }

    public void setCellText(Integer cellId, String text) {
        DiabloCellView cell = mCells.get(cellId);

        if (DiabloEnum.DIABLO_AUTOCOMPLETE.equals(cell.getCellType())) {
            ((AutoCompleteTextView)cell.getView()).setText(text);
        }
        else if (DiabloEnum.DIABLO_EDIT.equals(cell.getCellType())) {
            ((EditText)cell.getView()).setText(text);
        }

        else if (DiabloEnum.DIABLO_TEXT.equals(cell.getCellType())) {
            ((TextView)cell.getView()).setText(text);
        }

    }

    public void setOnLongClickListener(Fragment fragment) {
        mRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.showContextMenu();
                return true;
            }
        });

        fragment.registerForContextMenu(mRow);
    }

}
