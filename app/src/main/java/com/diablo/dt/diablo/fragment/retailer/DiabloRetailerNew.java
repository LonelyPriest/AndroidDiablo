package com.diablo.dt.diablo.fragment.retailer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.utils.DiabloEditTextWatcher;
import com.diablo.dt.diablo.utils.DiabloPattern;
import com.diablo.dt.diablo.utils.DiabloUtils;


public class DiabloRetailerNew extends Fragment {
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


    public DiabloRetailerNew() {
        // Required empty public constructor
    }

    public static DiabloRetailerNew newInstance(String param1, String param2) {
        DiabloRetailerNew fragment = new DiabloRetailerNew();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =inflater.inflate(R.layout.fragment_diablo_retailer_new, container, false);

        mViewName = (EditText) view.findViewById(R.id.retailer_name);
        mViewBalance = (EditText) view.findViewById(R.id.retailer_balance);
        mViewPhone = (EditText) view.findViewById(R.id.retailer_phone);
        mViewAddress = (EditText) view.findViewById(R.id.retailer_address);

        setWatcher();
        return view;
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

    public void setWatcher() {
        mNameWatcher = new DiabloEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                mName = editable.toString().trim();
                if (!DiabloPattern.isValidRetailer(mName)) {
                    isValidName = false;
                    if (mName.length() > 0){
                        mViewName.setError(getString(R.string.invalid_retailer));
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

    public void startAdd(final DiabloRetailerDetail fragment) {
        Retailer r = new Retailer(mName, mPhone);

        if (null != mBalance && mBalance.length() > 0) {
            r.setBalance(DiabloUtils.instance().toFloat(mBalance));
        } else {
            r.setBalance(0f);
        }

        if (null != mPhone && mPhone.length() > 0) {
            r.setMobile(mPhone);
        } else {
            r.setMobile(null);
        }

        if (null != mAddress && mAddress.length() > 0) {
            r.setAddress(mAddress);
        }

        r.newRetailer(getContext(), new Retailer.OnRetailerChangeListener() {
            @Override
            public void afterAdd(Retailer retailer) {
                clearWatcher();
                init();
                setWatcher();
                fragment.init();
            }

            @Override
            public void afterGet(Retailer retailer) {

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
//    public void onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        if (!isValidName
//            || (!isValidBalance && null != mBalance && mBalance.length() > 0)
//            || (!isValidPhone && null != mPhone && mPhone.length() > 0)
//            || (!isValidAddress && null != mAddress && mAddress.length() > 0)) {
//
//            menu.findItem(301).setEnabled(false);
//        } else {
//            menu.findItem(301).setEnabled(true);
//        }
//
//        getActivity().invalidateOptionsMenu();
//    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        // menu.clear();
//
//        menu.add(Menu.NONE, 300, Menu.NONE, getResources().getString(R.string.btn_back))
//            .setIcon(R.drawable.ic_arrow_back_black_24dp)
//            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//
//        menu.add(Menu.NONE, 301, Menu.NONE, getResources().getString(R.string.btn_save))
//            .setIcon(R.drawable.ic_file_download_black_24dp)
//            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case 301:
//                startAdd();
//                break;
//            case 300:
//                // find
//                DiabloRetailerPager pager = (DiabloRetailerPager) getFragmentManager()
//                    .findFragmentByTag(DiabloEnum.TAG_RETAILER_PAGER);
//                if (null != pager) {
////                    String tag = "android:switcher:" + R.id.diablo_view_pager + ":" + 0;
////                    Fragment f = getFragmentManager().findFragmentByTag(tag);
////                    ((DiabloRetailerDetail)f).init();
//                    pager.setPager(0);
//                }
//                break;
//            default:
//                break;
//        }
//        return true;
//    }

}
