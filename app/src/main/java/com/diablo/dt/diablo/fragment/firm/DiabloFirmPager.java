package com.diablo.dt.diablo.fragment.firm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.model.sale.SaleUtils;
import com.diablo.dt.diablo.utils.DiabloEnum;


public class DiabloFirmPager extends Fragment {

    // private TabLayout mRetailerTab;
    private ViewPager mFirmPager;
    private FragmentPagerAdapter mPagerAdapter;

    private SparseArray<Fragment> mFragments;

    public DiabloFirmPager() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DiabloFirmPager newInstance(String param1, String param2) {
        DiabloFirmPager fragment = new DiabloFirmPager();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();
        mFragments = new SparseArray<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.diablo_firm_pager, container, false);

        // mRetailerTab = (TabLayout) view.findViewById(R.id.diablo_tab_layout);
        mFirmPager = (ViewPager) view.findViewById(R.id.diablo_firm_pager);

        mPagerAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = new DiabloFirmDetail();
                        break;
                    case 1:
                        fragment = new DiabloFirmNew();
                    default:
                        break;
                }

                if (null == mFragments.get(position)) {
                    mFragments.put(position, fragment);
                }

                return fragment;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        mFirmPager.setAdapter(mPagerAdapter);
        return view;

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        Fragment f = mFragments.get(1);
        if (null != f) {
            boolean disabled = ((DiabloFirmNew) f).disableSave();
            if (null != menu.findItem(601)) {
                menu.findItem(601).setEnabled(!disabled);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();

        if (0 == mFirmPager.getCurrentItem()) {
            menu.add(Menu.NONE, 500, Menu.NONE, getResources().getString(R.string.btn_stock_in))
                .setIcon(R.drawable.ic_add_shopping_cart_black_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

            menu.add(Menu.NONE, 501, Menu.NONE, getResources().getString(R.string.btn_add))
                .setIcon(R.drawable.ic_add_black_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

            menu.add(Menu.NONE, 502, Menu.NONE, getResources().getString(R.string.btn_refresh))
                .setIcon(R.drawable.ic_refresh_black_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        else if (1 == mFirmPager.getCurrentItem()) {
            menu.add(Menu.NONE, 600, Menu.NONE, getResources().getString(R.string.btn_back))
                .setIcon(R.drawable.ic_arrow_back_black_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

            menu.add(Menu.NONE, 601, Menu.NONE, getResources().getString(R.string.btn_save))
                .setIcon(R.drawable.ic_file_download_black_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 500: // add
                // setMenuVisibility(false);
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_STOCK_IN);
                break;
            case 501:
                mFirmPager.setCurrentItem(1);
                break;
            case 502:
                Fragment f0 = mFragments.get(0);
                if (null != f0) {
                    ((DiabloFirmDetail) f0).refresh();
                }
                break;
            case 600:
                mFirmPager.setCurrentItem(0);
                break;
            case 601:
                Fragment f1 = mFragments.get(1);
                if (null != f1) {
                    ((DiabloFirmNew) f1).startAdd((DiabloFirmDetail) mFragments.get(0));
                }
                break;
            default:
                break;
        }
        return true;
    }
}
