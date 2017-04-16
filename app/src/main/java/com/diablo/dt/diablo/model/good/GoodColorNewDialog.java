package com.diablo.dt.diablo.model.good;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.utils.DiabloEditTextWatcher;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloPattern;

/**
 * Created by buxianhui on 17/4/12.
 */

public class GoodColorNewDialog {
    private Context mContext;
    private View mRootLayout;

    private View mColorName;
    private View mColorType;

    private String[] mColorKinds;

    private Integer mSelectColorKind;

    private DiabloColor.OnGoodColorChangeListener mOnGoodColorChangeListener;

    public GoodColorNewDialog(final Context context, @Nullable DiabloColor.OnGoodColorChangeListener listener) {
        this.mContext = context;

        mRootLayout = LayoutInflater.from(mContext).inflate(R.layout.diablo_color_new, null);
        mColorName = mRootLayout.findViewById(R.id.color_name);
        mColorType = mRootLayout.findViewById(R.id.color_type);

        mColorKinds = context.getResources().getStringArray(R.array.color_kinds);

        mSelectColorKind = DiabloEnum.INVALID_INDEX;
        mOnGoodColorChangeListener = listener;
        init();
    }

    private void init() {
        // adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            mContext,
            R.layout.diablo_spinner_item,
            mColorKinds);

        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)mColorType).setAdapter(adapter);

        ((Spinner)mColorType).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectColorKind = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.setView(mRootLayout).setPositiveButton(
            R.string.ok,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = ((EditText)mColorName).getText().toString().trim();

                    // add color
                    new DiabloColor(name, mSelectColorKind).newColor(mContext, mOnGoodColorChangeListener);

                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        ((EditText)mColorName).addTextChangedListener(new DiabloEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String name = editable.toString().trim();
                if (!DiabloPattern.isValidColorName(name)) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(false);
                    ((EditText)mColorName).setError(mContext.getString(R.string.invalid_color_name));
                }
                else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(true);
                }
            }
        });
    }
}
