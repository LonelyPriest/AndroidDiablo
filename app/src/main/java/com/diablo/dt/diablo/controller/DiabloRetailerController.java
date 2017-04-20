package com.diablo.dt.diablo.controller;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.utils.DiabloEditTextWatcher;
import com.diablo.dt.diablo.utils.DiabloPattern;

/**
 * Created by buxianhui on 17/4/20.
 */

public class DiabloRetailerController {
    private Context mContext;
    private String mName;
    private String mBalance;
    private String mPhone;
    private String mAddress;

    private EditText mViewName;
    private EditText mViewBalance;
    private EditText mViewPhone;
    private EditText mViewAddress;

    private TextWatcher mNameWatcher;
    private TextWatcher mBalanceWatcher;
    private TextWatcher mPhoneWatcher;
    private TextWatcher mAddressWatcher;

    private boolean isValidName;
    private boolean isValidBalance;
    private boolean isValidPhone;
    private boolean isValidAddress;

    public DiabloRetailerController(
        Context context,
        View name,
        View balance,
        View phone,
        View address) {

        this.mContext = context;
        mViewName = (EditText) name;
        mViewBalance = (EditText) balance;
        mViewPhone = (EditText) phone;
        mViewAddress = (EditText) address;

        init();
    }

    public void init() {
        mName = null;
        mPhone = null;
        mBalance = null;
        mAddress = null;

        isValidName    = false;
        isValidBalance = true;
        isValidPhone   = true;
        isValidAddress = true;
    }

    public boolean invalid() {
        return !isValidName
            || (!isValidBalance && null != mBalance && mBalance.length() > 0)
            || (!isValidPhone && null != mPhone && mPhone.length() > 0)
            || (!isValidAddress && null != mAddress && mAddress.length() > 0);
    }

    public void setWatcher() {
        mNameWatcher = new DiabloEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mName = editable.toString().trim();
                if (!DiabloPattern.isValidRetailer(mName)) {
                    isValidName = false;
                    if (mName.length() > 0){
                        mViewName.setError(mContext.getResources().getString(R.string.invalid_retailer));
                    }
                } else {
                    isValidName = true;
                    mViewName.setError(null);
                }

                ((Activity)mContext).invalidateOptionsMenu();
            }
        };
        mViewName.addTextChangedListener(mNameWatcher);

        mBalanceWatcher = new DiabloEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mBalance = editable.toString().trim();
                if (!DiabloPattern.isValidDecimal(mBalance)) {
                    isValidBalance = false;
                    if (mBalance.length() > 0) {
                        mViewBalance.setError(mContext.getResources().getString(R.string.invalid_price));
                    }
                } else {
                    isValidBalance = true;
                    mViewBalance.setError(null);
                }

                ((Activity)mContext).invalidateOptionsMenu();
            }
        };
        mViewBalance.addTextChangedListener(mBalanceWatcher);

        mPhoneWatcher = new DiabloEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mPhone = editable.toString().trim();
                if (!DiabloPattern.isValidPhone(mPhone)) {
                    isValidPhone = false;
                    if (mPhone.length() > 0) {
                        mViewPhone.setError(mContext.getResources().getString(R.string.invalid_phone));
                    }
                } else {
                    isValidPhone = true;
                    mViewPhone.setError(null);
                }

                ((Activity)mContext).invalidateOptionsMenu();
            }
        };
        mViewPhone.addTextChangedListener(mPhoneWatcher);

        mAddressWatcher = new DiabloEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mAddress = editable.toString().trim();
                if (!DiabloPattern.isValidAddress(mAddress)) {
                    isValidAddress = false;
                    if (mAddress.length() > 0){
                        mViewAddress.setError(mContext.getResources().getString(R.string.invalid_address));
                    }
                } else {
                    isValidAddress = true;
                    mViewAddress.setError(null);
                }

                ((Activity)mContext).invalidateOptionsMenu();
            }
        };
        mViewAddress.addTextChangedListener(mAddressWatcher);
    }

    public void clearWatcher() {
        mViewName.removeTextChangedListener(mNameWatcher);
        mViewBalance.removeTextChangedListener(mBalanceWatcher);
        mViewPhone.removeTextChangedListener(mPhoneWatcher);
        mViewAddress.removeTextChangedListener(mAddressWatcher);

        mViewName.setText(null);
        mViewBalance.setText(null);
        mViewPhone.setText(null);
        mViewAddress.setText(null);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mViewName.setText(name);
        this.mName = name;
    }

    public String getBalance() {
        return mBalance;
    }

    public void setBalance(String balance) {
        mViewBalance.setText(balance);
        this.mBalance = balance;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mViewPhone.setText(phone);
        this.mPhone = phone;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mViewAddress.setText(address);
        this.mAddress = address;
    }

    public void setValidName(boolean valid) {
        this.isValidName = valid;
    }
}
