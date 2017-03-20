package com.diablo.dt.diablo.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.diablo.dt.diablo.R;

/**
 * Created by buxianhui on 17/3/20.
 */

public class DiabloAlertDialog extends AlertDialog {
    private Context mContext;
    private String mTitle;
    private String mBody;

    public DiabloAlertDialog(Context context, String title, String body) {
        super(context);
        this.mContext = context;
        this.mTitle = title;
        this.mBody = body;
    }

    public void create() {
        DiabloAlertDialog.Builder builder = new DiabloAlertDialog.Builder(mContext);
        builder.setTitle(mTitle);
        builder.setMessage(mBody);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }




}
