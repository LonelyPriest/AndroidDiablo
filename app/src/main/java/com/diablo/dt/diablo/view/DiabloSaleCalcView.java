package com.diablo.dt.diablo.view;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

/**
 * Created by buxianhui on 17/3/19.
 */

public class DiabloSaleCalcView {
    private View mViewRetailer;
    private View mViewShop;
    private View mViewDatetime;
    private View mViewEmployee;

    private View mViewSaleTotal;
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

    public DiabloSaleCalcView(){

    }

    public View getViewRetailer() {
        return mViewRetailer;
    }

    public void setViewRetailer(View retailer) {
        this.mViewRetailer = retailer;
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

    public void setViewSaleTotal(View saleTotal) {
        this.mViewSaleTotal = saleTotal;
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

    public void setRetailerValue(String s){
        if (null != mViewRetailer) {
            ((AutoCompleteTextView)mViewRetailer).setText(s.trim());
        }
    }

    public void setSaleTotalValue(Integer s) {
        if (null != mViewSaleTotal) {
            ((EditText)mViewSaleTotal).setText(DiabloUtils.instance().toString(s));
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

    private void resetSaleTotalValue() {
        if (null != mViewSaleTotal) {
            ((EditText)mViewSaleTotal).setText(DiabloEnum.EMPTY_STRING);
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

        resetSaleTotalValue();
    }

}
