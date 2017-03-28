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
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.model.sale.SaleCalc;
import com.diablo.dt.diablo.task.MatchRetailerTask;
import com.diablo.dt.diablo.utils.AutoCompleteTextChangeListener;
import com.diablo.dt.diablo.utils.DiabloTextWatcher;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.DiabloSaleCalcView;

/**
 * Created by buxianhui on 17/3/19.
 */

public class DiabloSaleController {
    private static DiabloUtils UTILS = DiabloUtils.instance();
    private SaleCalc mSaleCalc;
    private DiabloSaleCalcView mSaleCalcView;


    /**
     * listener
     */
    // retailer
    private AutoCompleteTextChangeListener mOnAutoCompletedRetailerListener;
    private AdapterView.OnItemClickListener mOnRetailerClickListener;

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
    private OnDiabloRetailerSelectedListener mOnRetailerSelectedListener;

    public interface OnDiabloRetailerSelectedListener {
        void onRetailerSelected(SaleCalc calc);
    }

    public DiabloSaleController(SaleCalc calc) {
        this.mSaleCalc = calc;
        this.mSaleCalcView = null;

        mExtraCostWatcher = null;
        mCommentWatcher = null;
        mCashWatcher = null;
        mCardWatcher = null;
        mWireWatcher = null;
        mVerificateWatcher = null;

        mOnAutoCompletedRetailerListener = null;
        mOnRetailerClickListener = null;
        mOnEmployeeSelectedListener = null;
        mOnExtraCostTypeSelectedListener = null;
    }


    public DiabloSaleController(SaleCalc calc, DiabloSaleCalcView view) {
        this.mSaleCalc = calc;
        this.mSaleCalcView = view;
        this.mSaleCalcView.resetValue();

        mExtraCostWatcher = null;
        mCommentWatcher = null;
        mCashWatcher = null;
        mCardWatcher = null;
        mWireWatcher = null;
        mVerificateWatcher = null;

        mOnAutoCompletedRetailerListener = null;
        mOnRetailerClickListener = null;
        mOnEmployeeSelectedListener = null;
        mOnExtraCostTypeSelectedListener = null;
    }

    public SaleCalc getSaleCalc() {
        return mSaleCalc;
    }

    public void setSaleCalcView(final DiabloSaleCalcView view) {
        this.mSaleCalcView = view;
    }

    public String getSelectRetailerName() {
        return ((AutoCompleteTextView) mSaleCalcView.getViewRetailer()).getText().toString().trim();
    }

    public String getShopName() {
        return ((EditText) mSaleCalcView.getViewShop()).getText().toString().trim();
    }

    public void setRetailerWatcher(final Context context) {
        final AutoCompleteTextView r = (AutoCompleteTextView) mSaleCalcView.getViewRetailer();
        mOnAutoCompletedRetailerListener = new AutoCompleteTextChangeListener(r);

        mOnAutoCompletedRetailerListener.addListen(new AutoCompleteTextChangeListener.TextWatch() {
            @Override
            public void afterTextChanged(String s) {
                if (s.trim().length() > 0) {
                    new MatchRetailerTask(context, r).execute(s);
                }
            }
        });

        mOnRetailerClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Retailer selectRetailer = (Retailer) adapterView.getItemAtPosition(i);
                setRetailer(selectRetailer);
                if (null != mOnRetailerSelectedListener) {
                    mOnRetailerSelectedListener.onRetailerSelected(mSaleCalc);
                }
            }
        };

        r.setOnItemClickListener(mOnRetailerClickListener);
    }

    public void removeRetailerWatcher() {
        if (null != mOnAutoCompletedRetailerListener) {
            mOnAutoCompletedRetailerListener.removeListen();
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
                mSaleCalc.setEmployee(employee.getNumber());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        ((Spinner)mSaleCalcView.getViewEmployee()).setOnItemSelectedListener(mOnEmployeeSelectedListener);
    }

    public void setExtraCostTypeWatcher() {
        mOnExtraCostTypeSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSaleCalc.setExtraCostType(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
        ((Spinner)mSaleCalcView.getViewExtraCostType())
            .setOnItemSelectedListener(mOnExtraCostTypeSelectedListener);
    }

    public void setCommentWatcher() {
        mCommentWatcher = new DiabloTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mSaleCalc.setComment(editable.toString().trim());
            }
        };

        ((EditText)mSaleCalcView.getViewComment()).addTextChangedListener(mCommentWatcher);
    }

    public void setExtraCostWatcher() {
        mExtraCostWatcher = new DiabloTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mSaleCalc.setExtraCost(UTILS.toFloat(editable.toString().trim()));
                resetAccBalance();
            }
        };

        ((EditText)mSaleCalcView.getViewExtraCost()).addTextChangedListener(mExtraCostWatcher);
    }

    public void setCashWatcher() {
        mCashWatcher = new DiabloTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mSaleCalc.setCash(UTILS.toFloat(editable.toString().trim()));
                calcHasPay();
                resetAccBalance();
            }
        };

        ((EditText)mSaleCalcView.getViewCash()).addTextChangedListener(mCashWatcher);
    }

    public void setCardWatcher() {
        mCardWatcher = new DiabloTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mSaleCalc.setCard(UTILS.toFloat(editable.toString().trim()));
                calcHasPay();
                resetAccBalance();
            }
        };

        ((EditText)mSaleCalcView.getViewCard()).addTextChangedListener(mCardWatcher);
    }

    public void setWireWatcher() {
        mWireWatcher = new DiabloTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mSaleCalc.setWire(UTILS.toFloat(editable.toString().trim()));
                calcHasPay();
                resetAccBalance();
            }
        };

        ((EditText)mSaleCalcView.getViewWire()).addTextChangedListener(mWireWatcher);
    }

    public void setVerificateWatcher() {
        mVerificateWatcher = new DiabloTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mSaleCalc.setVerificate(UTILS.toFloat(editable.toString().trim()));
                resetAccBalance();
            }
        };

        ((EditText)mSaleCalcView.getViewVerificate()).addTextChangedListener(mVerificateWatcher);
    }

    public void setEmployeeAdapter(Context context) {
        EmployeeAdapter adapter = new EmployeeAdapter(
            context,
            R.layout.typeahead_employee,
            R.id.typeahead_select_employee,
            Profile.instance().getEmployees());
        ((Spinner)mSaleCalcView.getViewEmployee()).setAdapter(adapter);
    }

    public void setExtraCostTypeAdapter(Context context){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            context,
            R.array.extra_cost_on_sale,
            android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)mSaleCalcView.getViewExtraCostType()).setAdapter(adapter);
    }

    public void setDiabloOnRetailerSelected(OnDiabloRetailerSelectedListener listener) {
        mOnRetailerSelectedListener = listener;
    }

    public void setDatetime(String s){
        mSaleCalc.setDatetime(s);
        mSaleCalcView.setDatetimeValue(s);
    }

    public void setShop(Integer shopId){
        DiabloShop shop = DiabloUtils.getInstance().getShop(Profile.instance().getSortAvailableShop(), shopId);
        mSaleCalc.setShop(shop.getShop());
        mSaleCalcView.setShopValue(shop.getName());
    }

    public void setRetailer(Retailer retailer){
        mSaleCalc.setRetailer(retailer.getId());
        mSaleCalcView.setRetailerValue(retailer.getName());

        mSaleCalc.setBalance(retailer.getBalance());
        mSaleCalcView.setBalanceValue(retailer.getBalance());

        resetAccBalance();
    }

//    public void setRetailer(Integer retailerId){
//        Retailer r = Profile.instance().getRetailerById(retailerId);
//        setRetailer(r);
//    }

//    public void setRetailer(Integer retailerId, final List<Retailer> retailers){
//        for (Retailer r: retailers) {
//            if (r.getId().equals(retailerId)) {
//                setRetailer(r);
//                break;
//            }
//        }
//    }

    public void setSaleInfo(Integer total, Integer sell, Integer reject) {
        mSaleCalc.setTotal(total);
        mSaleCalc.setSellTotal(sell);
        mSaleCalc.setRejectTotal(reject);

        mSaleCalcView.setSaleTotalValue(Math.abs(sell) + Math.abs(reject));
    }

    public void setSaleInfo(Integer total) {
        mSaleCalc.setTotal(total);
        mSaleCalcView.setSaleTotalValue(total);
    }

    public void setShouldPay(Float shouldPay) {
        mSaleCalc.setShouldPay(shouldPay);
        mSaleCalcView.setShouldPayValue(shouldPay);
    }

    public Integer getShop() {
        return mSaleCalc.getShop();
    }

    public Integer getRetailer() {
        return mSaleCalc.getRetailer();
    }

    public void resetCash() {
        mSaleCalc.resetCash();
        mSaleCalcView.resetCashValue();
    }

    public void calcHasPay(){
        mSaleCalc.calcHasPay();
        mSaleCalcView.setHasPayValue(mSaleCalc.getHasPay());
    }

    public void resetAccBalance() {
        mSaleCalc.calcAccBalance();
        mSaleCalcView.setAccBalanceValue(mSaleCalc.getAccBalance());
    }
}
