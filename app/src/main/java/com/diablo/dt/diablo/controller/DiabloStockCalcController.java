package com.diablo.dt.diablo.controller;

import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.EmployeeAdapter;
import com.diablo.dt.diablo.adapter.FirmAdapter;
import com.diablo.dt.diablo.entity.DiabloShop;
import com.diablo.dt.diablo.entity.Employee;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.stock.StockCalc;
import com.diablo.dt.diablo.utils.DiabloAutoCompleteTextWatcher;
import com.diablo.dt.diablo.utils.DiabloEditTextWatcher;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.stock.DiabloStockCalcView;

/**
 * Created by buxianhui on 17/4/3.
 */

public class DiabloStockCalcController {
    private static DiabloUtils UTILS = DiabloUtils.instance();
    private StockCalc mStockCalc;
    private DiabloStockCalcView mStockCalcView;


    /**
     * listener
     */
    // firm
    private DiabloAutoCompleteTextWatcher mFirmWatcher;
    private AdapterView.OnItemClickListener mOnFirmClickListener;

    // employee
    private AdapterView.OnItemSelectedListener mOnEmployeeSelectedListener;
    // extra type
    private AdapterView.OnItemSelectedListener mOnExtraCostTypeSelectedListener;

    // extra cost
    private android.text.TextWatcher mExtraCostWatcher;
    // comment
    private android.text.TextWatcher mCommentWatcher;
    private android.text.TextWatcher mCashWatcher;
    private android.text.TextWatcher mCardWatcher;
    private android.text.TextWatcher mWireWatcher;
    private android.text.TextWatcher mVerificateWatcher;

    /**
     * interface
     */
    private OnDiabloFirmChangedListener mOnFirmChangedListener;

    public interface OnDiabloFirmChangedListener {
        void onFirmChanged(Firm firm);
    }

//    public DiabloStockCalcController(StockCalc calc) {
//        this.mStockCalc = calc;
//        this.mStockCalcView = null;
//
//        mExtraCostWatcher = null;
//        mCommentWatcher = null;
//        mCashWatcher = null;
//        mCardWatcher = null;
//        mWireWatcher = null;
//        mVerificateWatcher = null;
//
//        // mOnAutoCompletedFirmListener = null;
//        mOnFirmClickListener = null;
//        mOnEmployeeSelectedListener = null;
//        mOnExtraCostTypeSelectedListener = null;
//    }


    public DiabloStockCalcController(StockCalc calc, DiabloStockCalcView view) {
        this.mStockCalc = calc;
        this.mStockCalcView = view;
        this.mStockCalcView.resetValue();

        mExtraCostWatcher = null;
        mCommentWatcher = null;
        mCashWatcher = null;
        mCardWatcher = null;
        mWireWatcher = null;
        mVerificateWatcher = null;

        // mOnAutoCompletedFirmListener = null;
        mOnFirmClickListener = null;
        mOnEmployeeSelectedListener = null;
        mOnExtraCostTypeSelectedListener = null;
    }

    private void removeFirmWatcher() {
        if (null != mFirmWatcher) {
            mFirmWatcher.remove();
        }
    }

    public StockCalc getStockCalc() {
        return mStockCalc;
    }

    public void setStockCalcView(final DiabloStockCalcView view) {
        this.mStockCalcView = view;
    }

    public void setFirmWatcher() {
        final AutoCompleteTextView f = (AutoCompleteTextView) mStockCalcView.getViewFirm();
        mFirmWatcher = new DiabloAutoCompleteTextWatcher(
            f,
            new DiabloAutoCompleteTextWatcher.DiabloAutoCompleteTextChangListener() {
                @Override
                public void afterTextChanged(String s) {
                    setFirm(null);
                    setBalance(0f);
                }
            });
    }

    public void setFirmClickListener(final Context context) {
        final AutoCompleteTextView f = (AutoCompleteTextView) mStockCalcView.getViewFirm();
        new FirmAdapter(context, R.layout.typeahead_firm, R.id.typeahead_select_firm, f);

        mOnFirmClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Firm selectFirm = (Firm) adapterView.getItemAtPosition(i);
                removeFirmWatcher();

                setFirm(selectFirm);

                setFirmWatcher();
                // setBalance(selectFirm.getBalance());
                if (null != mOnFirmChangedListener) {
                    mOnFirmChangedListener.onFirmChanged(selectFirm);
                }
            }
        };

        f.setOnItemClickListener(mOnFirmClickListener);
    }

    public void setEmployeeClickListener() {
        mOnEmployeeSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee employee = (Employee) adapterView.getItemAtPosition(i);
                mStockCalc.setEmployee(employee.getNumber());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        ((Spinner)mStockCalcView.getViewEmployee()).setOnItemSelectedListener(mOnEmployeeSelectedListener);
    }

    public void setExtraCostTypeListener() {
        mOnExtraCostTypeSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mStockCalc.setExtraCostType(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
        ((Spinner)mStockCalcView.getViewExtraCostType())
            .setOnItemSelectedListener(mOnExtraCostTypeSelectedListener);
    }

    public void setCommentWatcher() {
        mCommentWatcher = new DiabloEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mStockCalc.setComment(editable.toString().trim());
            }
        };

        ((EditText)mStockCalcView.getViewComment()).addTextChangedListener(mCommentWatcher);
    }

    public void setExtraCostWatcher() {
        mExtraCostWatcher = new DiabloEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mStockCalc.setExtraCost(UTILS.toFloat(editable.toString().trim()));
                resetAccBalance();
            }
        };

        ((EditText)mStockCalcView.getViewExtraCost()).addTextChangedListener(mExtraCostWatcher);
    }

    public void setCashWatcher() {
        mCashWatcher = new DiabloEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mStockCalc.setCash(UTILS.toFloat(editable.toString().trim()));
                calcHasPay();
                resetAccBalance();
            }
        };

        ((EditText)mStockCalcView.getViewCash()).addTextChangedListener(mCashWatcher);
    }

    public void setCardWatcher() {
        mCardWatcher = new DiabloEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mStockCalc.setCard(UTILS.toFloat(editable.toString().trim()));
                calcHasPay();
                resetAccBalance();
            }
        };

        ((EditText)mStockCalcView.getViewCard()).addTextChangedListener(mCardWatcher);
    }

    public void setWireWatcher() {
        mWireWatcher = new DiabloEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mStockCalc.setWire(UTILS.toFloat(editable.toString().trim()));
                calcHasPay();
                resetAccBalance();
            }
        };

        ((EditText)mStockCalcView.getViewWire()).addTextChangedListener(mWireWatcher);
    }

    public void setVerificateWatcher() {
        mVerificateWatcher = new DiabloEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mStockCalc.setVerificate(UTILS.toFloat(editable.toString().trim()));
                resetAccBalance();
            }
        };

        ((EditText)mStockCalcView.getViewVerificate()).addTextChangedListener(mVerificateWatcher);
    }

    public void setEmployeeAdapter(Context context) {
        new EmployeeAdapter(
            context,
            (Spinner)mStockCalcView.getViewEmployee(),
            Profile.instance().getEmployees(),
            true);
    }

    public void setExtraCostTypeAdapter(Context context){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            context,
            R.array.extra_cost_on_sale,
            android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)mStockCalcView.getViewExtraCostType()).setAdapter(adapter);
    }

    public void setOnFirmChangedListener(OnDiabloFirmChangedListener listener) {
        mOnFirmChangedListener = listener;
    }

    public void setDatetime(String s){
        mStockCalc.setDatetime(s);
        mStockCalcView.setDatetimeValue(s);
    }

    public void setShop(Integer shopId){
        DiabloShop shop = DiabloUtils.getInstance().getShop(Profile.instance().getSortAvailableShop(), shopId);
        mStockCalc.setShop(shop.getShop());
        mStockCalcView.setShopValue(shop.getName());
    }

    public void setFirm(Firm firm){
        if (null == firm) {
            mStockCalc.setFirm(DiabloEnum.INVALID_INDEX);
        } else {
            mStockCalc.setFirm(firm.getId());
            mStockCalcView.setFirmValue(firm.getName());
        }
        // setBalance(firm.getBalance());
    }

    public void setBalance(Float balance) {
        mStockCalc.setBalance(balance);
        mStockCalcView.setBalanceValue(balance);
        resetAccBalance();
    }

    public void setStockTotal(Integer total) {
        mStockCalc.setTotal(total);
        mStockCalcView.setStockTotalValue(total);
    }

    public void setShouldPay(Float shouldPay) {
        mStockCalc.setShouldPay(shouldPay);
        mStockCalcView.setShouldPayValue(shouldPay);
    }

    public Integer getShop() {
        return mStockCalc.getShop();
    }

    public Integer getFirm() {
        return mStockCalc.getFirm();
    }

    public void resetCash() {
        mStockCalc.resetCash();
        mStockCalcView.resetCashValue();
    }

    public void calcHasPay(){
        mStockCalc.calcHasPay();
        mStockCalcView.setHasPayValue(mStockCalc.getHasPay());
    }

    public void resetAccBalance() {
        mStockCalc.calcAccBalance();
        mStockCalcView.setAccBalanceValue(mStockCalc.getAccBalance());
    }
}
