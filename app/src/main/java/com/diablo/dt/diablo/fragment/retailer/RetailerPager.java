package com.diablo.dt.diablo.fragment.retailer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diablo.dt.diablo.R;

public class RetailerPager extends Fragment {

    // private TabLayout mRetailerTab;
    private ViewPager mRetailerPager;
    private FragmentPagerAdapter mPagerAdapter;

    private SparseArray<Fragment> mFragments;

    public RetailerPager() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RetailerPager newInstance(String param1, String param2) {
        RetailerPager fragment = new RetailerPager();
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
        View view = inflater.inflate(R.layout.diablo_retailer_pager, container, false);

        // mRetailerTab = (TabLayout) view.findViewById(R.id.diablo_tab_layout);
        mRetailerPager = (ViewPager) view.findViewById(R.id.diablo_retailer_pager);

        mPagerAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = new RetailerDetail();
                        break;
                    case 1:
                        fragment = new RetailerNew();
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

        mRetailerPager.setAdapter(mPagerAdapter);
        return view;

    }


//    public void setPager(Integer position) {
//        mRetailerPager.setCurrentItem(position);
//    }
//
//    public final SparseArray<Fragment> getFragments() {
//        return mFragments;
//    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//
//        Fragment f = mFragments.get(1);
//        if (null != f) {
//            boolean disabled = ((RetailerNew) f).disableSave();
//            if (null != menu.findItem(301)) {
//                menu.findItem(301).setEnabled(!disabled);
//            }
//        }
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        // menu.clear();
//        if (1 == mRetailerPager.getCurrentItem()) {
//            menu.add(Menu.NONE, 300, Menu.NONE, getResources().getString(R.string.btn_back))
//                .setIcon(R.drawable.ic_arrow_back_black_24dp)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//
//            menu.add(Menu.NONE, 301, Menu.NONE, getResources().getString(R.string.btn_save))
//                .setIcon(R.drawable.ic_file_download_black_24dp)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//        }
//        else if (0 == mRetailerPager.getCurrentItem()) {
//            menu.add(Menu.NONE, 400, Menu.NONE, getResources().getString(R.string.btn_sale_in))
//                .setIcon(R.drawable.ic_add_shopping_cart_black_24dp)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//
//            menu.add(Menu.NONE, 401, Menu.NONE, getResources().getString(R.string.btn_add))
//                .setIcon(R.drawable.ic_add_black_24dp)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//
//            menu.add(Menu.NONE, 402, Menu.NONE, getResources().getString(R.string.btn_refresh))
//                .setIcon(R.drawable.ic_refresh_black_24dp)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//        }
//
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case 300:
//                mRetailerPager.setCurrentItem(0);
//                break;
//            case 301:
//                Fragment f1 = mFragments.get(1);
//                if (null != f1) {
//                    ((RetailerNew) f1).startAdd((RetailerDetail) mFragments.get(0));
//                }
//                break;
//            case 400:
//                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_SALE_IN);
//                break;
//            case 401:
//                mRetailerPager.setCurrentItem(1);
//                break;
//            case 402:
//                Fragment f0 = mFragments.get(0);
//                if (null != f0) {
//                    ((RetailerDetail) f0).refresh();
//                }
//                break;
//            default:
//                break;
//        }
//        return true;
//    }

}
