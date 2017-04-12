package com.diablo.dt.diablo.utils;

import static com.diablo.dt.diablo.fragment.good.GoodNew.UTILS;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.DiabloBrand;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.Profile;

import java.util.List;

/**
 * Created by buxianhui on 17/4/11.
 */

public class DiabloTableStockNote {
    private Context mContext;
    private View mRootLayout;
    private TableLayout mTableLayout;
    private OnStockNoteListener mOnStockNoteListener;

    public DiabloTableStockNote(Context context,
                                String styleNumber,
                                Integer brandId,
                                List<DiabloColor> colors, List<String> sizes) {
        this.mContext = context;
        init();

        createTitle(styleNumber, brandId);
        createHead(sizes);
        createBody(colors, sizes);
    }

    public DiabloTableStockNote(Context context,
                                String styleNumber,
                                Integer brandId,
                                List<DiabloColor> colors,
                                List<String> sizes,
                                OnStockNoteListener listener) {
        this.mContext = context;
        this.mOnStockNoteListener = listener;
        init();

        createTitle(styleNumber, brandId);
        createHead(sizes);
        createBody(colors, sizes);

        // show();
    }

    private void init() {
        mRootLayout = LayoutInflater.from(mContext).inflate(R.layout.diablo_stock_note, null);
        mTableLayout = (TableLayout) mRootLayout.findViewById(R.id.diablo_stock_note);
    }

    private void createTitle(String styleNumber, Integer brandId) {
        TextView titleView = (TextView) mRootLayout.findViewById(R.id.diablo_stock_note_title);

        DiabloBrand brand = Profile.instance().getBrand(brandId);

        if (null != brand) {
            titleView.setText(styleNumber + "-" + brand.getName());
        } else {
            titleView.setText(styleNumber);
        }

    }

    private void createHead(List<String> sizes) {
        TableRow head = new TableRow(mContext);
        mTableLayout.addView(head);

        TextView cell = new TextView(mContext);
        cell.setLayoutParams(UTILS.createTableRowParams(0.8f));
        head.addView(cell);

        TableRow.LayoutParams lp = UTILS.createTableRowParams(1f);
        for (Integer i=0; i<sizes.size(); i++){
            cell = new TextView(mContext);
            cell.setLayoutParams(lp);
            cell.setTypeface(null, Typeface.BOLD);
            cell.setTextSize(20);
            if (sizes.get(i).equals(DiabloEnum.DIABLO_FREE_SIZE)) {
                cell.setText(mContext.getString(R.string.free_size));
            } else {
                cell.setText(sizes.get(i));
            }
            cell.setGravity(Gravity.CENTER_VERTICAL);
            head.addView(cell);
        }
    }

    private void createBody(List<DiabloColor> colors, List<String> sizes) {
        TableRow [] rows = new TableRow[colors.size()];
        for (int i=0; i<colors.size(); i++){
            rows[i] = new TableRow(mContext);
            rows[i].setBackgroundResource(R.drawable.table_row_bg);
            mTableLayout.addView(rows[i]);
        }
        rows[rows.length - 1].setBackgroundResource(R.drawable.table_row_last_bg);

        TableRow.LayoutParams lp = UTILS.createTableRowParams(1f);

        for (Integer i=0; i<rows.length; i++){
            TableRow row = rows[i];
            TextView col0 = new TextView(mContext);
            col0.setLayoutParams(UTILS.createTableRowParams(0.8f));
            col0.setTextSize(20);
            col0.setTypeface(null, Typeface.BOLD);

            if (colors.get(i).getColorId().equals(DiabloEnum.DIABLO_FREE_COLOR)) {
                col0.setText(mContext.getString(R.string.free_color));
            } else {
                col0.setText(colors.get(i).getName());
            }
            col0.setGravity(Gravity.CENTER_VERTICAL);
            row.addView(col0);

            for (Integer j=0; j<sizes.size(); j++){
                TextView col = new TextView(mContext);
                col.setTextSize(20);
                col.setLayoutParams(lp);
                row.addView(col);

                if (null != mOnStockNoteListener) {
                    Integer stock = mOnStockNoteListener.getStockNote(colors.get(i).getColorId(), sizes.get(j));
                    if (null != stock) {
                        if (stock < 0) {
                            col.setTextColor(ContextCompat.getColor(mContext, R.color.bpDarker_red));
                        } else if (stock > 0 ){
                            col.setTextColor(ContextCompat.getColor(mContext, R.color.blueLight));
                        } else {
                            col.setTextColor(ContextCompat.getColor(mContext, R.color.orangeLight));
                        }

                        col.setText(DiabloUtils.instance().toString(stock));
                    }
                }
            }
        }
        
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.setView(mRootLayout).create();
        dialog.show();
    }

    public interface OnStockNoteListener {
        Integer getStockNote(Integer color, String size);
    }
}
