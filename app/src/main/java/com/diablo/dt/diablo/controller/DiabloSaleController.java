package com.diablo.dt.diablo.controller;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.BackendRetailerAdapter;
import com.diablo.dt.diablo.adapter.EmployeeAdapter;
import com.diablo.dt.diablo.entity.DiabloShop;
import com.diablo.dt.diablo.entity.Employee;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.model.sale.SaleCalc;
import com.diablo.dt.diablo.utils.DiabloAutoCompleteTextWatcher;
import com.diablo.dt.diablo.utils.DiabloEditTextWatcher;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.sale.DiabloSaleCalcView;

/**
 * Created by buxianhui on 17/3/19.
 */

public class DiabloSaleController {
    private final static String LOG_TAG = "SaleController:";
    private static DiabloUtils UTILS = DiabloUtils.instance();
    private SaleCalc mSaleCalc;
    private DiabloSaleCalcView mSaleCalcView;


    /**
     * listener
     */
    // retailer
    private DiabloAutoCompleteTextWatcher mOnAutoCompletedRetailerWatcher;
    private AdapterView.OnItemClickListener mOnRetailerClickListener;

    // employee
    private AdapterView.OnItemSelectedListener mOnEmployeeClickListener;
    // extra type
    private AdapterView.OnItemSelectedListener mOnExtraCostTypeClickListener;

    // extra cost
    private android.text.TextWatcher mExtraCostWatcher;
    // comment
    private android.text.TextWatcher mCommentWatcher;
    private android.text.TextWatcher mCashWatcher;
    private android.text.TextWatcher mCardWatcher;
    private android.text.TextWatcher mWireWatcher;
    private android.text.TextWatcher mVerificateWatcher;

    private boolean mSelectRetailerByClick;
    /**
     * interface
     */
    private OnRetailerChangeListener mOnRetailerChangeListener;

    public interface OnRetailerChangeListener {
        void onRetailerChanged(SaleCalc calc, Retailer retailer);
    }

//    public DiabloSaleController(SaleCalc calc) {
//        this.mSaleCalc = calc;
//        this.mSaleCalcView = null;
//
//        mExtraCostWatcher = null;
//        mCommentWatcher = null;
//        mCashWatcher = null;
//        mCardWatcher = null;
//        mWireWatcher = null;
//        mVerificateWatcher = null;
//
//        mOnAutoCompletedRetailerListener = null;
//        mOnRetailerClickListener = null;
//        mOnEmployeeClickListener = null;
//        mOnExtraCostTypeClickListener = null;
//    }


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

        mOnAutoCompletedRetailerWatcher = null;
        mOnRetailerClickListener = null;
        mOnEmployeeClickListener = null;
        mOnExtraCostTypeClickListener = null;

        mSelectRetailerByClick = false;
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

    public void setRetailerWatcher() {
        final AutoCompleteTextView r = (AutoCompleteTextView) mSaleCalcView.getViewRetailer();
        mOnAutoCompletedRetailerWatcher = new DiabloAutoCompleteTextWatcher(
            r,
            new DiabloAutoCompleteTextWatcher.DiabloAutoCompleteTextChangListener() {
                @Override
                public void afterTextChanged(String s) {
                    // Log.d(LOG_TAG, "watcher retailer " + s );
                    if (!mSelectRetailerByClick) {
                        setRetailer(null);
                        setBalance(0f);
                    }

                    mSelectRetailerByClick = false;
                }
            });
    }

    public void setRetailerClickListener(final Context context) {
        final AutoCompleteTextView r = (AutoCompleteTextView) mSaleCalcView.getViewRetailer();

        new BackendRetailerAdapter(context, R.layout.typeahead_retailer, R.id.typeahead_select_retailer, r);

        mOnRetailerClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Retailer selectRetailer = (Retailer) adapterView.getItemAtPosition(i);
                Log.d(LOG_TAG, "click the retailer:" + selectRetailer.getId());
                mSelectRetailerByClick = true;

                setRetailer(selectRetailer);
                if (null != mOnRetailerChangeListener) {
                    mOnRetailerChangeListener.onRetailerChanged(mSaleCalc, selectRetailer);
                }
            }
        };

        r.setOnItemClickListener(mOnRetailerClickListener);
    }

    public void removeRetailerWatcher() {
        if (null != mOnAutoCompletedRetailerWatcher) {
            mOnAutoCompletedRetailerWatcher.remove();
        }
    }

    public void setEmployeeClickListener() {
        mOnEmployeeClickListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee employee = (Employee) adapterView.getItemAtPosition(i);
                mSaleCalc.setEmployee(employee.getNumber());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        ((Spinner)mSaleCalcView.getViewEmployee()).setOnItemSelectedListener(mOnEmployeeClickListener);
    }

    public void setExtraCostTypeClickListener() {
        mOnExtraCostTypeClickListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSaleCalc.setExtraCostType(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
        ((Spinner)mSaleCalcView.getViewExtraCostType())
            .setOnItemSelectedListener(mOnExtraCostTypeClickListener);
    }

    public void setCommentWatcher() {
        mCommentWatcher = new DiabloEditTextWatcher(
            (EditText) mSaleCalcView.getViewComment(),
            new DiabloEditTextWatcher.DiabloEditTextChangListener() {
                @Override
                public void afterTextChanged(String s) {
                    mSaleCalc.setComment(s);
                }
            });
    }

    public void setExtraCostWatcher() {
        mExtraCostWatcher = new DiabloEditTextWatcher(
            (EditText) mSaleCalcView.getViewExtraCost(),
            new DiabloEditTextWatcher.DiabloEditTextChangListener() {
                @Override
                public void afterTextChanged(String s) {
                    mSaleCalc.setExtraCost(UTILS.toFloat(s));
                    resetAccBalance();
                }
            });
    }

    public void setCashWatcher() {
        mCashWatcher = new DiabloEditTextWatcher(
            (EditText) mSaleCalcView.getViewCash(),
            new DiabloEditTextWatcher.DiabloEditTextChangListener() {
                @Override
                public void afterTextChanged(String s) {
                    mSaleCalc.setCash(UTILS.toFloat(s));
                    calcHasPay();
                    resetAccBalance();
                }
            });
    }

    public void setCardWatcher() {
        mCardWatcher = new DiabloEditTextWatcher(
            (EditText) mSaleCalcView.getViewCard(),
            new DiabloEditTextWatcher.DiabloEditTextChangListener() {
                @Override
                public void afterTextChanged(String s) {
                    mSaleCalc.setCard(UTILS.toFloat(s));
                    calcHasPay();
                    resetAccBalance();
                }
            });

    }

    public void setWireWatcher() {
        mWireWatcher = new DiabloEditTextWatcher(
            mSaleCalcView.getViewWire(),
            new DiabloEditTextWatcher.DiabloEditTextChangListener() {
                @Override
                public void afterTextChanged(String s) {
                    mSaleCalc.setWire(UTILS.toFloat(s));
                    calcHasPay();
                    resetAccBalance();
                }
            });
    }

    public void setVerificateWatcher() {
        mVerificateWatcher = new DiabloEditTextWatcher(
            mSaleCalcView.getViewVerificate(),
            new DiabloEditTextWatcher.DiabloEditTextChangListener() {
                @Override
                public void afterTextChanged(String s) {
                    mSaleCalc.setVerificate(UTILS.toFloat(s));
                    resetAccBalance();
                }
            });
    }

    public void setEmployeeAdapter(Context context) {
        new EmployeeAdapter(
            context,
            (Spinner)mSaleCalcView.getViewEmployee(),
            Profile.instance().getEmployees(),
            true);
    }

    public void setExtraCostTypeAdapter(Context context){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            context,
            R.array.extra_cost_on_sale,
            android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)mSaleCalcView.getViewExtraCostType()).setAdapter(adapter);
    }

    public void setRetailerChangeListener(OnRetailerChangeListener listener) {
        mOnRetailerChangeListener = listener;
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
        if (null == retailer) {
            Log.d(LOG_TAG, "set retailer with null");
            mSaleCalc.setRetailer(DiabloEnum.INVALID_INDEX);
        } else {
            Log.d(LOG_TAG, "set retailer:" + retailer.getId());
            mSaleCalc.setRetailer(retailer.getId());
            mSaleCalcView.setRetailerValue(retailer.getName());
        }


//        mSaleCalc.setBalance(retailer.getBalance());
//        mSaleCalcView.setBalanceValue(retailer.getBalance());
//
//        resetAccBalance();
    }

    public void setBalance(Float balance) {
        mSaleCalc.setBalance(balance);
        mSaleCalcView.setBalanceValue(balance);
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
