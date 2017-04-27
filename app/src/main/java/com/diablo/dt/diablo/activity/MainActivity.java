package com.diablo.dt.diablo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.BaseSettingClient;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.fragment.firm.DiabloFirmPager;
import com.diablo.dt.diablo.fragment.good.GoodColorDetail;
import com.diablo.dt.diablo.fragment.good.GoodDetail;
import com.diablo.dt.diablo.fragment.good.GoodNew;
import com.diablo.dt.diablo.fragment.retailer.DiabloRetailerDetail;
import com.diablo.dt.diablo.fragment.sale.SaleDetail;
import com.diablo.dt.diablo.fragment.sale.SaleIn;
import com.diablo.dt.diablo.fragment.sale.SaleNote;
import com.diablo.dt.diablo.fragment.sale.SaleOut;
import com.diablo.dt.diablo.fragment.stock.StockDetail;
import com.diablo.dt.diablo.fragment.stock.StockIn;
import com.diablo.dt.diablo.fragment.stock.StockNote;
import com.diablo.dt.diablo.fragment.stock.StockOut;
import com.diablo.dt.diablo.fragment.stock.StockStoreDetail;
import com.diablo.dt.diablo.request.LogoutRequest;
import com.diablo.dt.diablo.rest.BaseSettingInterface;
import com.diablo.dt.diablo.utils.DiabloAlertDialog;
import com.diablo.dt.diablo.utils.DiabloDBManager;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = "MainActivity:";
    private NavigationView mNavigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    // index to identify current nav menu item
    private static SparseArray<NavigationTag> mNavTagMap = new SparseArray<>();
    private static String [] mAssistFragmentTag = {
        DiabloEnum.TAG_STOCK_SELECT,
        DiabloEnum.TAG_SALE_IN_UPDATE,
        DiabloEnum.TAG_SALE_OUT_UPDATE,

        DiabloEnum.TAG_GOOD_SELECT,
        DiabloEnum.TAG_STOCK_IN_UPDATE,
        DiabloEnum.TAG_STOCK_OUT_UPDATE,

        DiabloEnum.TAG_GOOD_UPDATE,
        DiabloEnum.TAG_COLOR_SELECT,
        DiabloEnum.TAG_SIZE_SELECT,

        DiabloEnum.TAG_RETAILER_NEW
    };

    private NavigationTag mCurrentNavTag;

    // toolbar titles respected to selected nav menu item
    private String[] mActivityTitles;

    // flag to load home fragment when user presses back key
//    private boolean shouldLoadHomeFragOnBackPress = true;
//    private Handler mHandler;

    private class NavigationTag {
        private Integer titleIndex;
        private String tag;
        private @IdRes Integer resId;

        private NavigationTag(Integer titleIndex, String tag, @IdRes Integer resId){
            this.titleIndex = titleIndex;
            this.tag = tag;
            this.resId = resId;
        }

        private Integer getTitleIndex() {
            return titleIndex;
        }

//        private void setTitleIndex(Integer titleIndex) {
//            this.titleIndex = titleIndex;
//        }

        private String getTag() {
            return tag;
        }

//        private void setTag(String tag) {
//            this.tag = tag;
//        }

//        private Integer getResId() {
//            return resId;
//        }
//
//        private void setResId(Integer resId) {
//            this.resId = resId;
//        }

        private MenuItem getMenuItem(){
            return mNavigationView.getMenu().findItem(resId);
        }

        private String getTitleName(){
            return mActivityTitles[titleIndex];
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DiabloDBManager.instance().init(this);

        Profile.instance().setResource(getResources());
        Profile.instance().setServer(getString(R.string.diablo_server));
        Profile.instance().setDiabloYears(getResources().getStringArray(R.array.years));
        Profile.instance().setSaleTypes(getResources().getStringArray(R.array.sale_types_desc));
        Profile.instance().setStockTypes(getResources().getStringArray(R.array.stock_types_desc));

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        // load toolbar titles from string resources
        mActivityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        mNavTagMap.put(0, new NavigationTag(0, DiabloEnum.TAG_SALE_IN, R.id.nav_sale_in));
        mNavTagMap.put(1, new NavigationTag(1, DiabloEnum.TAG_SALE_OUT, R.id.nav_sale_out));
        mNavTagMap.put(2, new NavigationTag(2, DiabloEnum.TAG_SALE_DETAIL, R.id.nav_sale_detail));
        mNavTagMap.put(3, new NavigationTag(3, DiabloEnum.TAG_SALE_NOTE, R.id.nav_sale_note));

        mNavTagMap.put(4, new NavigationTag(4, DiabloEnum.TAG_STOCK_IN, R.id.nav_stock_in));
        mNavTagMap.put(5, new NavigationTag(5, DiabloEnum.TAG_STOCK_OUT, R.id.nav_stock_out));
        mNavTagMap.put(6, new NavigationTag(6, DiabloEnum.TAG_STOCK_DETAIL, R.id.nav_stock_detail));
        mNavTagMap.put(7, new NavigationTag(7, DiabloEnum.TAG_STOCK_NOTE, R.id.nav_stock_note));
        mNavTagMap.put(8, new NavigationTag(8, DiabloEnum.TAG_STOCK_STORE_DETAIL, R.id.nav_inventory_detail));


        mNavTagMap.put(9, new NavigationTag(9, DiabloEnum.TAG_GOOD_DETAIL, R.id.nav_good_detail));
        mNavTagMap.put(10, new NavigationTag(10, DiabloEnum.TAG_GOOD_NEW, R.id.nav_good_new));
        mNavTagMap.put(11, new NavigationTag(11, DiabloEnum.TAG_GOOD_COLOR, R.id.nav_good_color));

        mNavTagMap.put(12, new NavigationTag(12, DiabloEnum.TAG_RETAILER_DETAIL, R.id.nav_retailer_detail));

        mNavTagMap.put(13, new NavigationTag(13, DiabloEnum.TAG_FIRM_PAGER, R.id.nav_firm_pager));

//        mNavTagMap.put(14, new NavigationTag(14, DiabloEnum.TAG_RETAILER_NEW, R.id.nav_retailer_new));

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            selectMenuItem(0);
            loadHomeFragment();
        }
    }

    public void selectMenuItem(Integer menuItemIndex){
        mCurrentNavTag = mNavTagMap.get(menuItemIndex);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setTitle(mCurrentNavTag.getTitleName());
        }
        unCheckAllMenuItems();
        mCurrentNavTag.getMenuItem().setChecked(true);
    }


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        // selectNavMenu();

        // set toolbar title
        // setActionBarTitle(mCurrentNavTag.getTitleIndex());

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        Fragment f = getSupportFragmentManager().findFragmentByTag(mCurrentNavTag.getTag());
        if (null != f && f.isVisible()) {
            drawer.closeDrawers();
            return;
        }

        Fragment currentFragment = getCurrentSelectedFragment();
        switchFragment(currentFragment, mCurrentNavTag.getTag());

//        Runnable mPendingRunnable = new Runnable() {
//            @Override
//            public void run() {
//                // update the main content by replacing fragment
//                DiabloUtils.instance().replaceFragment(
//                        getSupportFragmentManager(), getCurrentSelectedFragment(), mCurrentNavTag.getTag());
//            }
//        };
//
//        // If mPendingRunnable is not null, then add to the message queue
//        if (mPendingRunnable != null) {
//            mHandler.post(mPendingRunnable);
//        }

        // show or hide the fab button
        // toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    public void switchFragment(Fragment to, String toTag){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!to.isAdded()){
            transaction.add(R.id.frame_container, to, toTag);
        } else {
            transaction.show(to);
        }

        // transaction.hide(from);
        Fragment fragment;
        for (int i=0; i<mNavTagMap.size(); i++) {
            NavigationTag navTag = mNavTagMap.get(i);
            if (!navTag.getTag().equals(toTag)) {
                fragment = getSupportFragmentManager().findFragmentByTag(navTag.getTag());
                if (null != fragment) {
                    transaction.hide(fragment);
                }
            }
        }

        // hide assist fragment
        for (String tag: mAssistFragmentTag ) {
            fragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (null != fragment) {
                transaction.hide(fragment);
            }
        }

        transaction.commitAllowingStateLoss();
    }

    private Fragment getCurrentSelectedFragment() {
        Fragment f = getSupportFragmentManager().findFragmentByTag(mCurrentNavTag.getTag());
        if (f == null) {
            if (mCurrentNavTag.getTitleIndex().equals(0)){
                f = new SaleIn();
            }
            else if (mCurrentNavTag.getTitleIndex().equals(1)) {
                f = new SaleOut();
            }
            else if (mCurrentNavTag.getTitleIndex().equals(2)){
                f = new SaleDetail();
            }
            else if (mCurrentNavTag.getTitleIndex().equals(3)){
                f = new SaleNote();
            }
            else if (mCurrentNavTag.getTitleIndex().equals(4)) {
                f = new StockIn();
            }
            else if (mCurrentNavTag.getTitleIndex().equals(5)) {
                f = new StockOut();
            }
            else if (mCurrentNavTag.getTitleIndex().equals(6)) {
                f = new StockDetail();
            }
            else if (mCurrentNavTag.getTitleIndex().equals(7)) {
                f = new StockNote();
            }
            else if (mCurrentNavTag.getTitleIndex().equals(8)) {
                f = new StockStoreDetail();
            }
            else if (mCurrentNavTag.getTitleIndex().equals(9)) {
                f = new GoodDetail();
            }
            else if (mCurrentNavTag.getTitleIndex().equals(10)) {
                f = new GoodNew();
            }
            else if (mCurrentNavTag.getTitleIndex().equals(11)) {
                f = new GoodColorDetail();
            }
            else if (mCurrentNavTag.getTitleIndex().equals(12)) {
                f = new DiabloRetailerDetail();
            }
            else if (mCurrentNavTag.getTitleIndex().equals(13)) {
                f = new DiabloFirmPager();
            }
//            else if (mCurrentNavTag.getTitleIndex().equals(14)) {
//                f = new DiabloRetailerNew();
//            }
            else {
                f = new SaleDetail();
            }

        }
        return f;
    }

//    public void setActionBarTitle() {
//        getSupportActionBar().setTitle(mCurrentNavTag.getTitleName());
//        // getSupportActionBar().setTitle(activityTitles[mNavItemIndex]);
//    }

    private void selectNavMenu() {
        // navigationView.getMenu().getItem(mNavItemIndex).setChecked(true);
        // navigationView.getMenu().getItem(mNavItemIndex).getSubMenu().getItem()
        // Integer size = navigationView.getMenu().size();
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_sale_in:
                        selectMenuItem(0);
                        break;
                    case R.id.nav_sale_out:
                        selectMenuItem(1);
                        break;
                    case R.id.nav_sale_detail:
                        selectMenuItem(2);
                        break;
                    case R.id.nav_sale_note:
                        selectMenuItem(3);
                        break;
                    case R.id.nav_stock_in:
                        selectMenuItem(4);
                        break;
                    case R.id.nav_stock_out:
                        selectMenuItem(5);
                        break;
                    case R.id.nav_stock_detail:
                        selectMenuItem(6);
                        break;
                    case R.id.nav_stock_note:
                        selectMenuItem(7);
                        break;
                    case R.id.nav_inventory_detail:
                        selectMenuItem(8);
                        break;
                    case R.id.nav_good_detail:
                        selectMenuItem(9);
                        break;
                    case R.id.nav_good_new:
                        selectMenuItem(10);
                        break;
                    case R.id.nav_good_color:
                        selectMenuItem(11);
                        break;
                    case R.id.nav_retailer_detail:
                        selectMenuItem(12);
                        break;
                    case R.id.nav_firm_pager:
                        selectMenuItem(13);
                        break;
//                    case R.id.nav_retailer_new:
//                        selectMenuItem(14);
//                        break;
                    case R.id.nav_logout:
                        logout();
                        break;
                    case R.id.nav_clear_login_user:
                        DiabloDBManager.instance().clearUser();
                        break;
//                    case R.id.nav_about_us:
//                        // launch new intent instead of loading fragment
//                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
//                        drawer.closeDrawers();
//                        return true;
//                    case R.id.nav_privacy_policy:
//                        // launch new intent instead of loading fragment
//                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
//                        drawer.closeDrawers();
//                        return true;
                    default:
                        selectMenuItem(0);
                        break;
                }

                loadHomeFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void unCheckAllMenuItems() {
        final Menu menu = mNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                SubMenu subMenu = item.getSubMenu();
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    subMenuItem.setChecked(false);
                }
            } else {
                item.setChecked(false);
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
//        if (shouldLoadHomeFragOnBackPress) {
//            // checking if user is on other navigation menu
//            // rather than home
//            if (mNavItemIndex != 0) {
//                mNavItemIndex = 0;
//                CURRENT_TAG = TAG_SALE_IN;
//                loadHomeFragment();
//                return;
//            }
//        }

        // super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        // if (mNavItemIndex == 0) {
            // getMenuInflater().inflate(R.menu.main, menu);
        // }

        // when fragment is notifications, load the menu created for notifications
        // if (mNavItemIndex == 3) {
            // getMenuInflater().inflate(R.menu.notifications, menu);
        // }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
//    private void toggleFab() {
//        if (mNavItemIndex == 0)
//            fab.show();
//        else
//            fab.hide();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy called");
//        DiabloDBManager.instance().close();
//        Profile.instance().clear();
    }


    private void logout() {
        BaseSettingInterface face = BaseSettingClient.getClient().create(BaseSettingInterface.class);
        Call<com.diablo.dt.diablo.response.Response> call = face.logout(
            Profile.instance().getToken(), new LogoutRequest("destroy_login_user"));
        call.enqueue(new Callback<com.diablo.dt.diablo.response.Response>() {
            @Override
            public void onResponse(Call<com.diablo.dt.diablo.response.Response> call,
                                   Response<com.diablo.dt.diablo.response.Response> response) {
                Log.d(LOG_TAG, "success to destroy session");
                // clear information
                DiabloDBManager.instance().close();
                Profile.instance().clear();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<com.diablo.dt.diablo.response.Response> call, Throwable t) {
                new DiabloAlertDialog(
                    MainActivity.this,
                    getResources().getString(R.string.title_logout),
                    DiabloError.getError(99)).create();
            }
        });


    }

    //    @Override
//    public void onStockSelectFragmentInteraction(Uri uri) {
//
//    }
}
