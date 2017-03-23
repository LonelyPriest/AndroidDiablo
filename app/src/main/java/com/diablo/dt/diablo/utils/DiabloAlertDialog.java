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
    private boolean mNegativeButton;

    private OnOkClickListener mOnOkClickListener;

    public DiabloAlertDialog(Context context, String title, String body) {
        super(context);
        this.mContext = context;
        this.mNegativeButton = false;
        this.mTitle = title;
        this.mBody = body;
    }

    public DiabloAlertDialog(Context context, boolean negativeButton, String title, String body) {
        super(context);
        this.mContext = context;
        this.mNegativeButton = negativeButton;
        this.mTitle = title;
        this.mBody = body;
    }

    public DiabloAlertDialog(Context context, boolean negativeButton, String title, String body, OnOkClickListener listener) {
        super(context);
        this.mContext = context;
        this.mNegativeButton = negativeButton;
        this.mTitle = title;
        this.mBody = body;
        this.mOnOkClickListener = listener;
    }

    public void setOkClickListener( OnOkClickListener listener) {
        this.mOnOkClickListener = listener;
    }

    public void create() {
        DiabloAlertDialog.Builder builder = new DiabloAlertDialog.Builder(mContext);
        builder.setTitle(mTitle);
        builder.setMessage(mBody);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                if ( null != mOnOkClickListener ) {
                    mOnOkClickListener.onOk();
                }
            }
        });

        if (mNegativeButton) {
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });
        }

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public interface OnOkClickListener {
        void onOk();
    }
}
