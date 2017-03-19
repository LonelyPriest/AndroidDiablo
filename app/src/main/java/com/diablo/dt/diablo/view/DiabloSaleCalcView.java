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

    public View getViewDatetime() {
        return mViewDatetime;
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

    public View getViewSaleTotal() {
        return mViewSaleTotal;
    }

    public void setViewSaleTotal(View saleTotal) {
        this.mViewSaleTotal = saleTotal;
    }

    public View getViewShouldPay() {
        return mViewShouldPay;
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

    public View getViewBalance() {
        return mViewBalance;
    }

    public void setViewBalance(View balance) {
        this.mViewBalance = balance;
    }

    public View getViewHasPay() {
        return mViewHasPay;
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

    public View getViewAccBalance() {
        return mViewAccBalance;
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
        ((EditText)mViewBalance).setText(DiabloUtils.instance().toString(v));
    }

    public void setAccBalanceValue(Float v){
        ((EditText)mViewAccBalance).setText(DiabloUtils.instance().toString(v));
    }

    public void setShopValue(String s) {
        ((EditText)mViewShop).setText(s.trim());
    }

    public void setDatetimeValue(String s) {
        ((EditText)mViewDatetime).setText(s.trim());
    }

    public void setRetailerValue(String s){
        ((AutoCompleteTextView)mViewRetailer).setText(s.trim());
    }

    public void setSaleTotalValue(Integer s) {
        ((EditText)mViewSaleTotal).setText(DiabloUtils.instance().toString(s));
    }

    public void setShouldPayValue(Float s) {
        ((EditText)mViewShouldPay).setText(DiabloUtils.instance().toString(s));
    }

    public void setHasPayValue(Float s){
        ((EditText)mViewHasPay).setText(DiabloUtils.instance().toString(s));
    }

    public void resetCashValue() {
        ((EditText)mViewCash).setText(DiabloEnum.EMPTY_STRING);
    }

    private void resetCardValue() {
        ((EditText)mViewCard).setText(DiabloEnum.EMPTY_STRING);
    }

    private void resetWireValue() {
        ((EditText)mViewWire).setText(DiabloEnum.EMPTY_STRING);
    }

    private void resetVerificateValue() {
        ((EditText)mViewVerificate).setText(DiabloEnum.EMPTY_STRING);
    }

    private void resetExtraCostValue() {
        ((EditText)mViewExtraCost).setText(DiabloEnum.EMPTY_STRING);
    }

    private void resetShouldPayValue() {
        ((EditText)mViewShouldPay).setText(DiabloEnum.EMPTY_STRING);
    }

    private void resetHasPayValue() {
        ((EditText)mViewHasPay).setText(DiabloEnum.EMPTY_STRING);
    }

    private void resetSaleTotalValue() {
        ((EditText)mViewSaleTotal).setText(DiabloEnum.EMPTY_STRING);
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
