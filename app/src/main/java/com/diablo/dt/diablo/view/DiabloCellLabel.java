package com.diablo.dt.diablo.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.utils.DiabloEnum;

/**
 * Created by buxianhui on 17/3/22.
 */

public class DiabloCellLabel {
    private Integer mLabelId;
    private Integer mType;
    private Integer mColor;
    private Integer mSize;
    private Integer mGravity;
    private Integer mInputType;
    private boolean mFocusable;
    private Float   mWeight;
    private String  mLabel;

//    public DiabloCellLabel(String label, Integer type) {
//        this.mLabel = label;
//        this.mType = type;
//    }

    public DiabloCellLabel(String label, Integer color, Integer size) {
        this.mLabel = label;
        this.mType = DiabloEnum.DIABLO_TEXT;
        this.mColor = color;
        this.mSize = size;
        this.mFocusable = false;
        this.mGravity = Gravity.CENTER_VERTICAL;
        this.mWeight = 1f;
    }

    public DiabloCellLabel(String label, Integer type, Integer color, Integer size, Float weight) {
        this.mLabel = label;
        this.mType = type;
        this.mColor = color;
        this.mSize = size;
        this.mFocusable = false;
        this.mWeight = weight;
    }

    public DiabloCellLabel(String label, Integer type, Integer color, Integer size,
                           Integer inputType, Float weight) {
        this.mLabel = label;
        this.mType = type;
        this.mColor = color;
        this.mSize = size;
        this.mInputType = inputType;
        this.mFocusable = false;
        this.mWeight = weight;
    }

    public DiabloCellLabel(String label, Integer type, Integer color, Integer size,
                           Integer gravity, Integer inputType, boolean focusable, Float weight) {
        this.mLabel = label;
        this.mType = type;
        this.mColor = color;
        this.mSize = size;
        this.mGravity = gravity;
        this.mInputType = inputType;
        this.mFocusable = focusable;
        this.mWeight = weight;
    }

    public void setLabelId (Integer labelId) {
        this.mLabelId = labelId;
    }

    public Integer getLabelId() {
        return mLabelId;
    }

    public Integer getType() {
        return mType;
    }

//    public void setWeight(Float weight) {
//        this.mWeight = weight;
//    }

    public void setGravity(Integer gravity) {
        this.mGravity = gravity;
    }

//    public void setFocusable(boolean focusable) {
//        this.mFocusable = focusable;
//    }

    public String getLabel() {
        return mLabel;
    }

    public Integer getColor () {
        return mColor;
    }

    public Integer getSize() {
        return mSize;
    }

    public TableRow.LayoutParams getTableRowLayoutParams() {
        return new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, mWeight);
    }

    public View createCell(Context context) {
        View cell = null;
        TableRow.LayoutParams lp = getTableRowLayoutParams();
        if (mType.equals(DiabloEnum.DIABLO_TEXT)) {
            cell = new TextView(context);
            cell.setLayoutParams(lp);
            ((TextView)cell).setTextColor(ContextCompat.getColor(context, mColor));
            ((TextView)cell).setTextSize(mSize);
            if ( null != mGravity ){
                ((TextView)cell).setGravity(mGravity);
            }
        }
        else if (mType.equals(DiabloEnum.DIABLO_EDIT)) {
            cell = new EditText(context);
            cell.setLayoutParams(lp);
            ((EditText)cell).setTextColor(ContextCompat.getColor(context, mColor));
            ((EditText)cell).setTextSize(mSize);

            if ( null != mGravity ){
                ((TextView)cell).setGravity(mGravity);
            }

            if ( null != mInputType) {
                ((EditText) cell).setInputType(mInputType);
            }

            cell.setFocusable(mFocusable);
        }
        else if (mType.equals(DiabloEnum.DIABLO_AUTOCOMPLETE)) {
            cell = new AutoCompleteTextView(context);
            cell.setLayoutParams(lp);
            ((AutoCompleteTextView)cell).setTextColor(ContextCompat.getColor(context, mColor));
            ((AutoCompleteTextView)cell).setTextSize(mSize);

//            if ( null != mGravity ){
//                ((AutoCompleteTextView)cell).setGravity(mGravity);
//            }

//            if ( null != mInputType) {
//                ((AutoCompleteTextView) cell).setInputType(mInputType);
//            }
            cell.setFocusable(mFocusable);
        }
        else if (mType.equals(DiabloEnum.DIABLO_SPINNER)) {
            cell = new Spinner(context);
            cell.setLayoutParams(lp);
        }

        return cell;
    }
}
