package com.diablo.dt.diablo.fragment.retailer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.controller.DiabloRetailerController;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.model.sale.SaleUtils;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;


public class RetailerNew extends Fragment {
    private DiabloRetailerController mController;

    // private RetailerDetail.OnRetailerDetailListener mOnRetailerAddListener;

//    public void setRetailerAddListener(RetailerDetail.OnRetailerDetailListener listener) {
//        mOnRetailerAddListener = listener;
//    }

    public RetailerNew() {
        // Required empty public constructor
    }

    public static RetailerNew newInstance(String param1, String param2) {
        RetailerNew fragment = new RetailerNew();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initTitle() {
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (null != bar) {
            bar.setTitle(getResources().getString(R.string.title_retailer_new));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        initTitle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =inflater.inflate(R.layout.fragment_diablo_retailer_new, container, false);

        mController = new DiabloRetailerController(
            getContext(),
            view.findViewById(R.id.retailer_name),
            view.findViewById(R.id.retailer_balance),
            view.findViewById(R.id.retailer_phone),
            view.findViewById(R.id.retailer_address));

        mController.setWatcher();
        return view;
    }


    public void startAdd() {
        String name = mController.getName();
        String phone = mController.getPhone();
        String balance = mController.getBalance();
        String address = mController.getAddress();

        Retailer r = new Retailer(name, phone);

        if (null != balance && balance.length() > 0) {
            r.setBalance(DiabloUtils.instance().toFloat(balance));
        } else {
            r.setBalance(0f);
        }

        if (null != phone && phone.length() > 0) {
            r.setMobile(phone);
        } else {
            r.setMobile(null);
        }

        if (null != address && address.length() > 0) {
            r.setAddress(address);
        }

        r.newRetailer(getContext(), new Retailer.OnRetailerChangeListener() {
            @Override
            public void afterAdd(Retailer retailer) {
                mController.clearWatcher();
                mController.init();
                mController.setWatcher();

//                if (null != mOnRetailerAddListener) {
//                    mOnRetailerAddListener.onAdd();
//                }
            }

            @Override
            public void afterGet(Retailer retailer) {

            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mController.invalid()) {
            menu.findItem(301).setEnabled(false);
        } else {
            menu.findItem(301).setEnabled(true);
        }

        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();

        menu.add(Menu.NONE, 300, Menu.NONE, getResources().getString(R.string.btn_back))
            .setIcon(R.drawable.ic_arrow_back_black_24dp)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add(Menu.NONE, 301, Menu.NONE, getResources().getString(R.string.btn_save))
            .setIcon(R.drawable.ic_file_download_black_24dp)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 301:
                startAdd();
                break;
            case 300:
//                DiabloUtils.switchToFrame(
//                    this,
//                    "com.diablo.dt.diablo.fragment.retailer.RetailerDetail",
//                    DiabloEnum.TAG_RETAILER_DETAIL);
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_RETAILER_DETAIL);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initTitle();
        }
    }

}
