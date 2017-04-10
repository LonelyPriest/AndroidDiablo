package com.diablo.dt.diablo.model.sale;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.List;

/**
 * Created by buxianhui on 17/3/15.
 */

public class DiabloStockSelectTable {
    private TableLayout mTableLayout;
    private List<DiabloColor> mOrderedColors;
    private List<String> mOrderedSizes;
    private Context mContext;

    // batch operation
    private boolean mBatch;

    private TableRow mHead;
    private TableRow[] mRows;

    private OnStockListener mStockListener;

    public DiabloStockSelectTable(Context context, TableLayout table, List<DiabloColor> colors, List<String> sizes){
        this.mContext = context;
        this.mTableLayout = table;
        this.mOrderedColors = colors;
        this.mOrderedSizes = sizes;
        this.mBatch = false;
        // this.mStockAmounts = new ArrayList<>();

        mHead = new TableRow(context);
        mHead.setBackgroundResource(R.drawable.table_row_bg);
        mTableLayout.addView(mHead);

        mRows = new TableRow[colors.size()];
        for (int i=0; i<colors.size(); i++){
            mRows[i] = new TableRow(context);
            mRows[i].setBackgroundResource(R.drawable.table_row_bg);
            mTableLayout.addView(mRows[i]);
        }
        mRows[mRows.length - 1].setBackgroundResource(R.drawable.table_row_last_bg);

    }

    private TableRow.LayoutParams genRowWeight(Float weight){
        return new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, weight);
    }

    private void setTextCellStyle(TextView cell, Integer textSize, Integer height){
        cell.setTextSize(textSize);
        cell.setHeight(height);
    }

    private void setEditCellStyle(EditText cell, Integer textSize, Integer height){
        cell.setTextSize(textSize);
        cell.setHeight(height);
    }

    public void genHead(){
        TableRow.LayoutParams lp02f = genRowWeight(0.2f);
        TableRow.LayoutParams lp08f = genRowWeight(0.8f);

        // empty
        TextView col0 = new TextView(mContext);
        col0.setLayoutParams(lp08f);
        mHead.addView(col0);


        for (Integer i=0; i<mOrderedSizes.size(); i++){
            TextView col = new TextView(mContext);
            col.setLayoutParams(lp02f);
            mHead.addView(col);

            col = new TextView(mContext);
            col.setLayoutParams(lp08f);
            mHead.addView(col);

            setTextCellStyle(col, 20, 100);
            col.setTypeface(null, Typeface.BOLD);
            col.setGravity(Gravity.START|Gravity.CENTER_VERTICAL);

            if (mOrderedSizes.get(i).equals(DiabloEnum.DIABLO_FREE_SIZE)) {
                col.setText(mContext.getString(R.string.free_size));
            } else {
                col.setText(mOrderedSizes.get(i));
            }
        }
    }

    public void genContent(){
        TableRow.LayoutParams lp = genRowWeight(1f);
        TableRow.LayoutParams lp02f = genRowWeight(0.2f);
        TableRow.LayoutParams lp08f = genRowWeight(0.8f);

        for (Integer i=0; i<mRows.length; i++){
            TableRow row = mRows[i];
            TextView col0 = new TextView(mContext);
            col0.setLayoutParams(genRowWeight(0.8f));
            setTextCellStyle(col0, 20, 100);
            col0.setTypeface(null, Typeface.BOLD);

            if (mOrderedColors.get(i).getColorId().equals(DiabloEnum.DIABLO_FREE_COLOR)) {
                col0.setText(mContext.getString(R.string.free_color));
            } else {
                col0.setText(mOrderedColors.get(i).getName());
            }
            
            col0.setGravity(Gravity.CENTER_VERTICAL);
            row.addView(col0);

            for (Integer j=0; j<mOrderedSizes.size(); j++){
                TextView col = new TextView(mContext);
                setTextCellStyle(col, 20, 100);

                SaleStockAmount a = mStockListener.getStockByColorAndSize(
                        mOrderedColors.get(i).getColorId(), mOrderedSizes.get(j));

                if (null != a){
                    col.setLayoutParams(lp02f);
                    col.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    DiabloUtils.instance().setTextViewValue(col, a.getStock());
                    row.addView(col);

                    EditText editCol = new EditText(mContext);
                    editCol.setLayoutParams(lp08f);
                    setEditCellStyle(editCol, 20, 100);
                    editCol.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    editCol.setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_SIGNED);
                    editCol.setTextColor(Color.RED);
                    if (0 != a.getSellCount()) {
                        editCol.setText(DiabloUtils.instance().toString(a.getSellCount()));
                    }
                    row.addView(editCol);

                    mStockListener.onStockSelected(editCol, mOrderedColors.get(i).getColorId(), mOrderedSizes.get(j));


                } else {
                    col.setLayoutParams(lp);
                    row.addView(col);
                }
            }
        }
    }

    public void setStockListener(OnStockListener listener){
        this.mStockListener = listener;
    }

    public interface OnStockListener {
        SaleStockAmount getStockByColorAndSize(Integer colorId, String size);
        void onStockSelected(EditText cell, Integer colorId, String size);
    }


}
