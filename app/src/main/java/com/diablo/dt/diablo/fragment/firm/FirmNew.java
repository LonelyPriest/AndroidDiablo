package com.diablo.dt.diablo.fragment.firm;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.utils.DiabloEditTextWatcher;
import com.diablo.dt.diablo.utils.DiabloPattern;
import com.diablo.dt.diablo.utils.DiabloUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirmNew#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirmNew extends Fragment {
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

    public FirmNew() {
        // Required empty public constructor
    }

    public static FirmNew newInstance(String param1, String param2) {
        FirmNew fragment = new FirmNew();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        // initTitle();
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_diablo_firm_new, container, false);

        mViewName = (EditText) view.findViewById(R.id.firm_name);
        mViewBalance = (EditText) view.findViewById(R.id.firm_balance);
        mViewPhone = (EditText) view.findViewById(R.id.firm_phone);
        mViewAddress = (EditText) view.findViewById(R.id.firm_address);

        // initTitle();
        setWatcher();
        return view;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        initTitle();
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

    public void initTitle() {
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (null != bar) {
            bar.setTitle(getResources().getString(R.string.title_firm_new));
        }
    }

    public void setWatcher() {
        mNameWatcher = new DiabloEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mName = editable.toString().trim();
                if (!DiabloPattern.isValidFirm(mName)) {
                    isValidName = false;
                    if (mName.length() > 0){
                        mViewName.setError(getString(R.string.invalid_firm));
                    }
                } else {
                    isValidName = true;
                    mViewName.setError(null);
                }

                getActivity().invalidateOptionsMenu();
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
                        mViewBalance.setError(getString(R.string.invalid_price));
                    }
                } else {
                    isValidBalance = true;
                    mViewBalance.setError(null);
                }

                getActivity().invalidateOptionsMenu();
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
                        mViewPhone.setError(getString(R.string.invalid_phone));
                    }
                } else {
                    isValidPhone = true;
                    mViewPhone.setError(null);
                }

                getActivity().invalidateOptionsMenu();
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
                        mViewAddress.setError(getString(R.string.invalid_address));
                    }
                } else {
                    isValidAddress = true;
                    mViewAddress.setError(null);
                }

                getActivity().invalidateOptionsMenu();
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

    public void startAdd(final FirmDetail fragment) {
        Firm f = new Firm(mName);

        if (null != mBalance && mBalance.length() > 0) {
            f.setBalance(DiabloUtils.instance().toFloat(mBalance));
        } else {
            f.setBalance(0f);
        }

        if (null != mPhone && mPhone.length() > 0) {
            f.setMobile(mPhone);
        } else {
            f.setMobile(null);
        }

        if (null != mAddress && mAddress.length() > 0) {
            f.setAddress(mAddress);
        } else {
            f.setAddress(null);
        }

        f.setDatetime(null);

        f.addFirm(getContext(), new Firm.OnFirmAddListener() {
            @Override
            public void afterAdd(Firm firm) {
                clearWatcher();
                init();
                setWatcher();
                fragment.init();
            }
        });
    }

    public boolean disableSave() {
        return !isValidName
            || (!isValidBalance && null != mBalance && mBalance.length() > 0)
            || (!isValidPhone && null != mPhone && mPhone.length() > 0)
            || (!isValidAddress && null != mAddress && mAddress.length() > 0);
    }

    //    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            initTitle();
//        }
//    }
}
