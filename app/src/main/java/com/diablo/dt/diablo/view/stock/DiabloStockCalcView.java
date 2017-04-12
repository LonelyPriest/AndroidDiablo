package com.diablo.dt.diablo.view.stock;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

/**
 * Created by buxianhui on 17/4/3.
 */

public class DiabloStockCalcView {
    private View mViewFirm;
    private View mViewShop;
    private View mViewDatetime;
    private View mViewEmployee;

    private View mViewStockTotal;
    private View mViewShouldPay;
    private View mViewCash;
    private View mViewComment;

    private View mViewBalance;
    private View mViewHasPay;
    private View mViewCard;
    private View mViewExtraCost;

    private View mViewAccBalance;
    private View mViewVerificate;
    private View mViewWire;
    private View mViewExtraCostType;

    public DiabloStockCalcView(){

    }

    public View getViewFirm() {
        return mViewFirm;
    }

    public void setViewFirm(View firm) {
        this.mViewFirm = firm;
    }

    public View getViewShop() {
        return mViewShop;
    }

    public void setViewShop(View shop) {
        this.mViewShop = shop;
    }

    public void setViewDatetime(View datetime) {
        this.mViewDatetime = datetime;
    }

    public View getViewEmployee() {
        return mViewEmployee;
    }

    public void setViewEmployee(View employee) {
        this.mViewEmployee = employee;
    }

    public void setViewStockTotal(View stockTotal) {
        this.mViewStockTotal = stockTotal;
    }

    public void setViewShouldPay(View shouldPay) {
        this.mViewShouldPay = shouldPay;
    }

    public View getViewCash() {
        return mViewCash;
    }

    public void setViewCash(View cash) {
        this.mViewCash = cash;
    }

    public View getViewComment() {
        return mViewComment;
    }

    public void setViewComment(View comment) {
        this.mViewComment = comment;
    }

    public void setViewBalance(View balance) {
        this.mViewBalance = balance;
    }

    public void setViewHasPay(View hasPay) {
        this.mViewHasPay = hasPay;
    }

    public View getViewCard() {
        return mViewCard;
    }

    public void setViewCard(View card) {
        this.mViewCard = card;
    }

    public View getViewExtraCost() {
        return mViewExtraCost;
    }

    public void setViewExtraCost(View extraCost) {
        this.mViewExtraCost = extraCost;
    }

    public void setViewAccBalance(View balance) {
        this.mViewAccBalance = balance;
    }

    public View getViewVerificate() {
        return mViewVerificate;
    }

    public void setViewVerificate(View verificate) {
        this.mViewVerificate = verificate;
    }

    public View getViewWire() {
        return mViewWire;
    }

    public void setViewWire(View wire) {
        this.mViewWire = wire;
    }

    public View getViewExtraCostType() {
        return mViewExtraCostType;
    }

    public void setViewExtraCostType(View extraCostType) {
        this.mViewExtraCostType = extraCostType;
    }

    public void setBalanceValue(Float v) {
        if (null != mViewBalance) {
            ((EditText)mViewBalance).setText(DiabloUtils.instance().toString(v));
        }
    }

    public void setAccBalanceValue(Float v){
        if (null != mViewAccBalance) {
            ((EditText)mViewAccBalance).setText(DiabloUtils.instance().toString(v));
        }
    }

    public void setShopValue(String s) {
        ((EditText)mViewShop).setText(s.trim());
    }

    public void setDatetimeValue(String s) {
        if (null != mViewDatetime) {
            ((EditText)mViewDatetime).setText(s.trim());
        }
    }

    public void setFirmValue(String s){
        if (null != mViewFirm) {
            ((AutoCompleteTextView)mViewFirm).setText(s.trim());
        }
    }

    public void setStockTotalValue(Integer s) {
        if (null != mViewStockTotal) {
            ((EditText)mViewStockTotal).setText(DiabloUtils.instance().toString(s));
        }
    }

    public void setShouldPayValue(Float s) {
        if (null != mViewShouldPay) {
            ((EditText)mViewShouldPay).setText(DiabloUtils.instance().toString(s));
        }
    }

    public void setHasPayValue(Float s){
        if (null != mViewHasPay) {
            ((EditText)mViewHasPay).setText(DiabloUtils.instance().toString(s));
        }
    }

    public void setCashValue(Float v) {
        if (null != mViewCash) {
            if (null == v) {
                resetCashValue();
            } else {
                ((EditText)mViewCash).setText(DiabloUtils.instance().toString(v));
            }
        }
    }

    public void setCardValue(Float v) {
        if (null != mViewCard) {
            if (null == v) {
                resetCardValue();
            } else {
                ((EditText)mViewCard).setText(DiabloUtils.instance().toString(v));
            }
        }
    }

    public void setWireValue(Float v) {
        if (null != mViewWire) {
            if (null == v) {
                resetWireValue();
            } else {
                ((EditText)mViewWire).setText(DiabloUtils.instance().toString(v));
            }
        }
    }

    public void setVerificateValue(Float v) {
        if ( null != mViewVerificate) {
            if (null == v) {
                resetVerificateValue();
            } else {
                ((EditText)mViewVerificate).setText(DiabloUtils.instance().toString(v));
            }
        }
    }

    public void setExtraCostValue(Float v) {
        if (null != mViewExtraCost) {
            if (null == v) {
                resetExtraCostValue();
            } else {
                ((EditText)mViewExtraCost).setText(DiabloUtils.instance().toString(v));
            }
        }
    }

    public void resetCashValue() {
        if (null != mViewCash) {
            ((EditText)mViewCash).setText(DiabloEnum.EMPTY_STRING);
        }
    }

    private void resetCardValue() {
        if (null != mViewCard) {
            ((EditText)mViewCard).setText(DiabloEnum.EMPTY_STRING);
        }
    }

    private void resetWireValue() {
        if (null != mViewWire) {
            ((EditText)mViewWire).setText(DiabloEnum.EMPTY_STRING);
        }
    }

    private void resetVerificateValue() {
        if ( null != mViewVerificate) {
            ((EditText)mViewVerificate).setText(DiabloEnum.EMPTY_STRING);
        }
    }

    private void resetExtraCostValue() {
        if (null != mViewExtraCost) {
            ((EditText)mViewExtraCost).setText(DiabloEnum.EMPTY_STRING);
        }
    }

    private void resetShouldPayValue() {
        if (null != mViewShouldPay) {
            ((EditText)mViewShouldPay).setText(DiabloEnum.EMPTY_STRING);
        }
    }

    private void resetHasPayValue() {
        if (null != mViewHasPay) {
            ((EditText)mViewHasPay).setText(DiabloEnum.EMPTY_STRING);
        }
    }

    private void resetStockTotalValue() {
        if (null != mViewStockTotal) {
            ((EditText)mViewStockTotal).setText(DiabloEnum.EMPTY_STRING);
        }
    }

    public void resetValue(){
        resetCashValue();
        resetCardValue();
        resetWireValue();
        resetVerificateValue();
        resetExtraCostValue();

        resetShouldPayValue();
        resetHasPayValue();

        resetStockTotalValue();
    }
}
