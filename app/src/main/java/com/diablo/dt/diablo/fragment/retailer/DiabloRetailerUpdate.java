package com.diablo.dt.diablo.fragment.retailer;


import com.google.gson.Gson;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.controller.DiabloRetailerController;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

public class DiabloRetailerUpdate extends Fragment {
    private Retailer mRetailer;
    private DiabloRetailerController mController;

    private DiabloRetailerDetail.OnRetailerDetailListener mOnRetailerUpdateListener;

    public void setRetailerUpdateListener(DiabloRetailerDetail.OnRetailerDetailListener listener) {
        mOnRetailerUpdateListener = listener;
    }

    public static DiabloRetailerUpdate newInstance(String param1, String param2) {
        DiabloRetailerUpdate fragment = new DiabloRetailerUpdate();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public DiabloRetailerUpdate() {
        // Required empty public constructor
    }

    public void setUpdateRetailer(String retailerJson) {
        mRetailer = new Gson().fromJson(retailerJson, Retailer.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRetailer = new Gson().fromJson(getArguments().getString(DiabloEnum.BUNDLE_PARAM_RETAILER), Retailer.class);
        }

        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.fragment_diablo_retailer_new, container, false);

        mController = new DiabloRetailerController(
            getContext(),
            view.findViewById(R.id.retailer_name),
            view.findViewById(R.id.retailer_balance),
            view.findViewById(R.id.retailer_phone),
            view.findViewById(R.id.retailer_address));

        mController.setName(mRetailer.getName());
        mController.setBalance(DiabloUtils.instance().toString(mRetailer.getBalance()));
        mController.setPhone(mRetailer.getMobile());
        mController.setAddress(mRetailer.getAddress());

        mController.setValidName(true);

        mController.setWatcher();

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mController.invalid()) {
            menu.findItem(801).setEnabled(false);
        } else {
            menu.findItem(801).setEnabled(true);
        }

        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.add(Menu.NONE, 800, Menu.NONE, getResources().getString(R.string.btn_cancel)).setIcon(R.drawable.ic_close_black_24dp)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add(Menu.NONE, 801, Menu.NONE, getResources().getString(R.string.btn_save))
            .setIcon(R.drawable.ic_check_black_24dp)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 800: // cancel
                switchToRetailerDetailFragment(800, mRetailer);
                break;
            case 801: // save
                startUpdate();
                break;
            default:
                break;
        }


        return true;
    }

    private void startUpdate() {
        String name = mController.getName();
        String phone = mController.getPhone();
        String balance = mController.getBalance();
        String address = mController.getAddress();

        Retailer r = new Retailer(name, phone);

        if (name.equals(mRetailer.getName())) {
            r.setName(null);
        }

        if (null == balance || 0 == balance.length()) {
            if (0f == mRetailer.getBalance()) {
                r.setBalance(null);
            } else {
                r.setBalance(0f);
            }
        } else {
            Float updatedBalance = DiabloUtils.instance().toFloat(balance);
            if (!updatedBalance.equals(mRetailer.getBalance())) {
                r.setBalance(DiabloUtils.instance().toFloat(balance));
            }
        }

        if (null == phone || 0 == phone.length()) {
            if (DiabloEnum.EMPTY_STRING.equals(mRetailer.getMobile())) {
                r.setMobile(null);
            } else {
                r.setMobile(DiabloEnum.EMPTY_STRING);
            }
        } else {
            if (!phone.equals(mRetailer.getMobile())) {
                r.setMobile(phone);
            }
        }

        if (null == address || 0 == address.length()) {
            if (DiabloEnum.EMPTY_STRING.equals(mRetailer.getAddress())) {
                r.setAddress(null);
            } else {
                r.setAddress(DiabloEnum.EMPTY_STRING);
            }
        } else {
            if (!address.equals(mRetailer.getAddress())) {
                r.setAddress(address);
            }
        }

        r.updateRetailer(getContext(), mRetailer, new Retailer.OnRetailerChangeListener() {
            @Override
            public void afterAdd(Retailer retailer) {
                switchToRetailerDetailFragment(801, retailer);
            }

            @Override
            public void afterGet(Retailer retailer) {

            }
        });
    }

    private void switchToRetailerDetailFragment(final Integer operation, Retailer updatedRetailer) {
        Fragment to= getFragmentManager().findFragmentByTag(DiabloEnum.TAG_RETAILER_DETAIL);
        if (null == to) {
            to = new DiabloRetailerDetail();
        }

        switch (operation){
            case 800:
                break;
            case 801:
                if (null != mOnRetailerUpdateListener) {
                    mOnRetailerUpdateListener.onUpdate(updatedRetailer);
                }
                break;
            default:
                break;
        }


        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (!to.isAdded()){
            transaction.remove(this);
            transaction.add(R.id.frame_container, to, DiabloEnum.TAG_RETAILER_DETAIL);
        } else {
            transaction.remove(this);
            transaction.show(to);
        }

        transaction.commit();
    }

//    public interface OnRetailerUpdateListener {
//        void onRetailerUpdate(Retailer updatedRetailer);
//    }
}
