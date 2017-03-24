package com.diablo.dt.diablo.view;

import android.view.View;
import android.widget.EditText;

import com.diablo.dt.diablo.utils.DiabloEnum;

/**
 * Created by buxianhui on 17/3/22.
 */

public class DiabloCellView {
    private View view;
    private DiabloCellLabel label;

    public DiabloCellView(View view, DiabloCellLabel label) {
        this.view = view;
        this.label = label;
        this.view.setTag(label.getLabelId());
    }

    public View getView () {
        return view;
    }

    public Integer getCellId() {
        return label.getLabelId();
    }

    public Integer getCellType() {
        return label.getType();
    }

    public void setCellFocusable(boolean focusable) {
        view.setFocusableInTouchMode(true);
        view.setFocusable(focusable);
    }

    public void requestFocus() {
        view.requestFocus();
    }

    public void setInputType(Integer inputType) {
        if (DiabloEnum.DIABLO_EDIT.equals(label.getType())) {
            ((EditText)view).setInputType(inputType);
        }
    }
}
