package com.diablo.dt.diablo.model.stock;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;

import com.diablo.dt.diablo.controller.DiabloStockRowController;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by buxianhui on 17/4/4.
 */

public class DiabloStockAmountChangeWatcher implements TextWatcher {
    private Handler mHandler;
    private Timer    mTimer;
    private DiabloStockRowController mRowController;

    public DiabloStockAmountChangeWatcher(Handler handler, DiabloStockRowController controller){
        this.mTimer         =new Timer();
        this.mHandler       = handler;
        this.mRowController = controller;
    }

//    public DiabloStockAmountChangeWatcher(Handler handler, TableRow row){
//        this.mTimer =new Timer();
//        this.mHandler = handler;
//        this.mRow = row;
//        this.mRowController = null;
//    }

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
                message.what = StockUtils.STOCK_TOTAL_CHANGED;
                if (inputValue.length() == 1 && inputValue.startsWith("-")){
                    message.arg1 = 0;
                } else {
                    message.arg1 = DiabloUtils.instance().toInteger(inputValue);
                }

                message.obj = mRowController;
                message.sendToTarget();
            }
        }, 500);
    }
}
