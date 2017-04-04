package com.diablo.dt.diablo.controller;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.EmployeeAdapter;
import com.diablo.dt.diablo.entity.DiabloShop;
import com.diablo.dt.diablo.entity.Employee;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.stock.StockCalc;
import com.diablo.dt.diablo.task.MatchFirmTask;
import com.diablo.dt.diablo.utils.AutoCompleteTextChangeListener;
import com.diablo.dt.diablo.utils.DiabloTextWatcher;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.DiabloStockCalcView;

import java.util.List;

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
    private AutoCompleteTextChangeListener mOnAutoCompletedFirmListener;
    private AdapterView.OnItemClickListener mOnFirmClickListener;

    // employee
    private AdapterView.OnItemSelectedListener mOnEmployeeSelectedListener;
    // extra type
    private AdapterView.OnItemSelectedListener mOnExtraCostTypeSelectedListener;

    // extra cost
    private TextWatcher mExtraCostWatcher;
    // comment
    private TextWatcher mCommentWatcher;
    private TextWatcher mCashWatcher;
    private TextWatcher mCardWatcher;
    private TextWatcher mWireWatcher;
    private TextWatcher mVerificateWatcher;

    /**
     * interface
     */
    private OnDiabloFirmSelectedListener mOnFirmSelectedListener;

    public interface OnDiabloFirmSelectedListener {
        void onFirmSelected(StockCalc calc);
    }

    public DiabloStockCalcController(StockCalc calc) {
        this.mStockCalc = calc;
        this.mStockCalcView = null;

        mExtraCostWatcher = null;
        mCommentWatcher = null;
        mCashWatcher = null;
        mCardWatcher = null;
        mWireWatcher = null;
        mVerificateWatcher = null;

        mOnAutoCompletedFirmListener = null;
        mOnFirmClickListener = null;
        mOnEmployeeSelectedListener = null;
        mOnExtraCostTypeSelectedListener = null;
    }


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

        mOnAutoCompletedFirmListener = null;
        mOnFirmClickListener = null;
        mOnEmployeeSelectedListener = null;
        mOnExtraCostTypeSelectedListener = null;
    }

    public StockCalc getStockCalc() {
        return mStockCalc;
    }

    public void setStockCalcView(final DiabloStockCalcView view) {
        this.mStockCalcView = view;
    }

    public void setFirmWatcher(final Context context, final List<Firm> firms) {
        final AutoCompleteTextView f = (AutoCompleteTextView) mStockCalcView.getViewFirm();
        mOnAutoCompletedFirmListener = new AutoCompleteTextChangeListener(f);

        mOnAutoCompletedFirmListener.addListen(new AutoCompleteTextChangeListener.TextWatch() {
            @Override
            public void afterTextChanged(String s) {
                if (s.trim().length() > 0) {
                    new MatchFirmTask(context, f, firms).execute(s);
                }
            }
        });

        mOnFirmClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Firm selectFirm = (Firm) adapterView.getItemAtPosition(i);
                setFirm(selectFirm);
                if (null != mOnFirmSelectedListener) {
                    mOnFirmSelectedListener.onFirmSelected(mStockCalc);
                }
            }
        };

        f.setOnItemClickListener(mOnFirmClickListener);
    }

    public void removeFirmWatcher() {
        if (null != mOnAutoCompletedFirmListener) {
            mOnAutoCompletedFirmListener.removeListen();
        }
    }

//    public void setRetailerListSelection(Integer position) {
//        ((AutoCompleteTextView) mSaleCalcView.getViewRetailer()).setListSelection(position);
//    }

    public void setEmployeeWatcher() {
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

    public void setExtraCostTypeWatcher() {
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
        mCommentWatcher = new DiabloTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mStockCalc.setComment(editable.toString().trim());
            }
        };

        ((EditText)mStockCalcView.getViewComment()).addTextChangedListener(mCommentWatcher);
    }

    public void setExtraCostWatcher() {
        mExtraCostWatcher = new DiabloTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mStockCalc.setExtraCost(UTILS.toFloat(editable.toString().trim()));
                resetAccBalance();
            }
        };

        ((EditText)mStockCalcView.getViewExtraCost()).addTextChangedListener(mExtraCostWatcher);
    }

    public void setCashWatcher() {
        mCashWatcher = new DiabloTextWatcher() {
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
        mCardWatcher = new DiabloTextWatcher() {
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
        mWireWatcher = new DiabloTextWatcher() {
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
        mVerificateWatcher = new DiabloTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mStockCalc.setVerificate(UTILS.toFloat(editable.toString().trim()));
                resetAccBalance();
            }
        };

        ((EditText)mStockCalcView.getViewVerificate()).addTextChangedListener(mVerificateWatcher);
    }

    public void setEmployeeAdapter(Context context) {
        EmployeeAdapter adapter = new EmployeeAdapter(
            context,
            R.layout.typeahead_employee,
            R.id.typeahead_select_employee,
            Profile.instance().getEmployees());
        ((Spinner)mStockCalcView.getViewEmployee()).setAdapter(adapter);
    }

    public void setExtraCostTypeAdapter(Context context){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            context,
            R.array.extra_cost_on_sale,
            android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)mStockCalcView.getViewExtraCostType()).setAdapter(adapter);
    }

    public void setDiabloOnFirmSelected(OnDiabloFirmSelectedListener listener) {
        mOnFirmSelectedListener = listener;
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
        mStockCalc.setFirm(firm.getId());
        mStockCalcView.setFirmValue(firm.getName());

        mStockCalc.setBalance(firm.getBalance());
        mStockCalcView.setBalanceValue(firm.getBalance());

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
