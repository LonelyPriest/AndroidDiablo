package com.diablo.dt.diablo.utils;

import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TableRow;

import com.diablo.dt.diablo.fragment.SaleIn;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by buxianhui on 17/3/17.
 */

public class DiabloSaleAmountChangeWatcher implements TextWatcher {
    private SaleIn.SaleStockHandler mSaleStockHandler;
    private Timer mTimer;
    private TableRow mRow;
    private final long DELAY = 500; // milliseconds

    public DiabloSaleAmountChangeWatcher(SaleIn.SaleStockHandler handler, TableRow row){
        this.mTimer =new Timer();
        this.mSaleStockHandler = handler;
        this.mRow = row;
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
                Message message = Message.obtain(mSaleStockHandler);
                message.what = SaleIn.SaleStockHandler.SALE_TOTAL_CHANGED;
                if (inputValue.length() == 1 && inputValue.startsWith("-")){
                    message.arg1 = 0;
                } else {
                    message.arg1 = DiabloUtils.instance().toInteger(inputValue);
                }

                message.obj = mRow;
                message.sendToTarget();
            }
        }, DELAY);
    }
}
