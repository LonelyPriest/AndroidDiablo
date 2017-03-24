package com.diablo.dt.diablo.model;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TableRow;

import com.diablo.dt.diablo.controller.DiabloSaleRowController;
import com.diablo.dt.diablo.fragment.SaleIn;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by buxianhui on 17/3/17.
 */

public class DiabloSaleAmountChangeWatcher implements TextWatcher {
    private Handler  mHandler;
    private Timer    mTimer;
    private TableRow mRow;
    private DiabloSaleRowController mRowController;

    public DiabloSaleAmountChangeWatcher(Handler handler, DiabloSaleRowController controller){
        this.mTimer         =new Timer();
        this.mHandler       = handler;
        this.mRowController = controller;
        this.mRow           = null;
    }

    public DiabloSaleAmountChangeWatcher(Handler handler, TableRow row){
        this.mTimer =new Timer();
        this.mHandler = handler;
        this.mRow = row;
        this.mRowController = null;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (mTimer != null) mTimer.cancel();
    }

    @Override
    public void afterTextChanged(Editable editable) {
        final String inputValue = editable.toString().trim();

        mTimer.cancel();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = Message.obtain(mHandler);
                message.what = SaleIn.SaleInHandler.SALE_TOTAL_CHANGED;
                if (inputValue.length() == 1 && inputValue.startsWith("-")){
                    message.arg1 = 0;
                } else {
                    message.arg1 = DiabloUtils.instance().toInteger(inputValue);
                }
                if ( null != mRow) {
                    message.obj = mRow;
                } else if ( null != mRowController ){
                    message.obj = mRowController;
                }
                message.sendToTarget();
            }
        }, 500);
    }
}
