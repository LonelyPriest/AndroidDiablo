package com.diablo.dt.diablo.model.stock;

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
 * Created by buxianhui on 17/4/5.
 */

public class DiabloGoodSelectTable {
    private TableLayout mTableLayout;
    private List<DiabloColor> mOrderedColors;
    private List<String> mOrderedSizes;
    private Context mContext;

    // batch operation
    private boolean mBatch;

    private TableRow mHead;
    private TableRow[] mRows;

    private OnGoodSelectListener mGoodListener;

    public DiabloGoodSelectTable(Context context, TableLayout table, List<DiabloColor> colors, List<String> sizes){
        this.mContext       = context;
        this.mTableLayout   = table;
        this.mOrderedColors = colors;
        this.mOrderedSizes  = sizes;
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
        // empty
        TextView cell = new TextView(mContext);
        cell.setLayoutParams(genRowWeight(0.8f));
        mHead.addView(cell);

        TableRow.LayoutParams lp = genRowWeight(1f);
        for (Integer i=0; i<mOrderedSizes.size(); i++){
            cell = new TextView(mContext);
            cell.setLayoutParams(lp);
            setTextCellStyle(cell, 20, 100);
            cell.setTypeface(null, Typeface.BOLD);
            if (mOrderedSizes.get(i).equals(DiabloEnum.DIABLO_FREE_SIZE)) {
                cell.setText(mContext.getString(R.string.free_size));
            } else {
                cell.setText(mOrderedSizes.get(i));
            }

            cell.setGravity(Gravity.CENTER_VERTICAL);
            mHead.addView(cell);
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
                EntryStockAmount a = mGoodListener.getGoodByColorAndSize(
                    mOrderedColors.get(i).getColorId(), mOrderedSizes.get(j));

                if (null != a){
                    EditText editCol = new EditText(mContext);
                    editCol.setLayoutParams(lp);
                    setEditCellStyle(editCol, 20, 100);
                    editCol.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    editCol.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editCol.setTextColor(Color.RED);
                    if (0 != a.getCount()) {
                        editCol.setText(DiabloUtils.instance().toString(a.getCount()));
                    }
                    row.addView(editCol);

                    mGoodListener.onGoodSelected(editCol, mOrderedColors.get(i).getColorId(), mOrderedSizes.get(j));

                } else {
                    TextView col = new TextView(mContext);
                    setTextCellStyle(col, 20, 100);
                    col.setLayoutParams(lp);
                    row.addView(col);
                }
            }
        }
    }

    public void setGoodListener(OnGoodSelectListener listener){
        this.mGoodListener = listener;
    }

    public interface OnGoodSelectListener {
        EntryStockAmount getGoodByColorAndSize(Integer colorId, String size);
        void onGoodSelected(EditText cell, Integer colorId, String size);
    }
}
