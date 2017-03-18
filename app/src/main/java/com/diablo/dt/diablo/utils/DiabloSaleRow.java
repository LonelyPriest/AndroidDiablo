package com.diablo.dt.diablo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.model.SaleStock;

import java.util.HashMap;
import java.util.Map;

import static com.diablo.dt.diablo.R.string.amount;

/**
 * Created by buxianhui on 17/3/17.
 */

public class DiabloSaleRow {
    private Context mContext;
    private String [] mColNames;

    private TableRow mRowView;
    private Map<String, View> mCellMap;

    private SaleStock mStock;


    public DiabloSaleRow(Context context, String [] colNames, SaleStock stock){
        this.mContext  = context;
        this.mColNames = colNames;
        this.mStock    = stock;
        mRowView       = new TableRow(mContext);
        this.mCellMap  = new HashMap<>();
    }

    public TableRow getRow(){
        return mRowView;
    }

    public SaleStock getStock(){
        return mStock;
    }

    public View getCell(String colName){
        return mCellMap.get(colName);
    }

    public View getCell(Integer resId){
        return mCellMap.get(mContext.getResources().getString(resId));
    }

    public void genRow(Integer freeRow){
        Resources res = mContext.getResources();

        View cell = null;
        for (String title: mColNames){
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            if (res.getString(R.string.order_id).equals(title)){
                lp.weight = 0.8f;
                cell = new TextView(mContext);
                ((TextView)cell).setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                ((TextView)cell).setTextSize(18);
                ((TextView)cell).setTextColor(res.getColor(R.color.colorPrimaryDark));
            }
            else if (res.getString(R.string.good).equals(title)){
                cell = new AutoCompleteTextView(mContext);
                ((AutoCompleteTextView)cell).setTextSize(18);
                ((AutoCompleteTextView)cell).setTextColor(Color.BLACK);
                lp.weight = 2f;
                cell.setFocusable(false);
            }
            else if (res.getString(R.string.stock).equals(title)){
                cell = new TextView(mContext);
                ((TextView)cell).setGravity(Gravity.CENTER_VERTICAL);
                ((TextView)cell).setTextSize(18);
                ((TextView)cell).setTextColor(Color.RED);
            }
            else if (res.getString(R.string.price_type).equals(title)) {
                cell = new Spinner(mContext);
                lp.weight = 1.5f;
            }
            else if (res.getString(R.string.price).equals(title)){
                cell = new TextView(mContext);
                ((TextView)cell).setTextSize(18);
                ((TextView)cell).setGravity(Gravity.CENTER_VERTICAL);
                ((TextView)cell).setTextColor(Color.BLACK);
            }
            else if (res.getString(R.string.discount).equals(title)){
                cell = new TextView(mContext);
                ((TextView)cell).setTextSize(18);
                ((TextView)cell).setGravity(Gravity.CENTER_VERTICAL);
                ((TextView)cell).setTextColor(Color.BLACK);
            }
            else if(res.getString(R.string.fprice).equals(title)){
                cell = new EditText(mContext);
                ((EditText)cell).setTextSize(18);
                ((EditText)cell).setTextColor(Color.BLACK);
                ((EditText)cell).setGravity(Gravity.CENTER_VERTICAL);
                ((EditText)cell).setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }
            else if (res.getString(amount).equals(title)){
                cell = new EditText(mContext);
                ((EditText)cell).setTextSize(18);
                ((EditText)cell).setTextColor(Color.RED);
                ((EditText)cell).setGravity(Gravity.CENTER_VERTICAL);
                ((EditText)cell).setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_SIGNED);
                if (!DiabloEnum.DIABLO_FREE.equals(freeRow)){
                    cell.setFocusable(false);
                }
            }
            else if (res.getString(R.string.calculate).equals(title)){
                cell = new TextView(mContext);
                ((TextView)cell).setGravity(Gravity.CENTER_VERTICAL);
                ((TextView)cell).setTextSize(18);
                ((TextView)cell).setTextColor(Color.BLACK);
            }
            else if (res.getString(R.string.comment).equals(title)){
                cell = new EditText(mContext);
                ((EditText)cell).setTextSize(16);
                ((EditText)cell).setTextColor(Color.BLACK);
                ((EditText)cell).setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                lp.weight = 1.5f;
            }

            bindCell(cell, lp, title);
        }

        mRowView.setTag(mStock);
    }

    private void bindCell(View cell, TableRow.LayoutParams lp, String cellTitle){
        cell.setLayoutParams(lp);
        cell.setTag(cellTitle);

        mRowView.addView(cell);
        mCellMap.put(cellTitle, cell);
    }
}
