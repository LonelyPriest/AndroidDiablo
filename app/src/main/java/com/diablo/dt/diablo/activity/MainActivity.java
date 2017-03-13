package com.diablo.dt.diablo.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.fragment.SaleDetail;
import com.diablo.dt.diablo.fragment.SaleIn;
import com.diablo.dt.diablo.utils.DiabloEnum;

public class MainActivity extends AppCompatActivity implements
        SaleDetail.OnFragmentInteractionListener,
        SaleIn.OnFragmentInteractionListener{

    private NavigationView mNavigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    // index to identify current nav menu item
    private static SparseArray<NavigationTag> mNavTagMap = new SparseArray<>();
    private NavigationTag mCurrentNavTag;

    // toolbar titles respected to selected nav menu item
    private String[] mActivityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

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

        private void setTitleIndex(Integer titleIndex) {
            this.titleIndex = titleIndex;
        }

        private String getTag() {
            return tag;
        }

        private void setTag(String tag) {
            this.tag = tag;
        }

        private Integer getResId() {
            return resId;
        }

        private void setResId(Integer resId) {
            this.resId = resId;
        }

        private MenuItem getMenuItem(){
            return MainActivity.this.mNavigationView.getMenu().findItem(resId);
        }

        private String getTitleName(){
            return mActivityTitles[titleIndex];
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

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

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            selectMenuItem(2);
            loadHomeFragment();
        }
    }

    public void selectMenuItem(Integer menuItemIndex){
        mCurrentNavTag = mNavTagMap.get(menuItemIndex);
        getSupportActionBar().setTitle(mCurrentNavTag.getTitleName());
        uncheckAllMenuItems();
        mCurrentNavTag.getMenuItem().setChecked(true);
    }


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        // setActionBarTitle(mCurrentNavTag.getTitleIndex());

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(mCurrentNavTag.getTag()) != null) {
            drawer.closeDrawers();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, mCurrentNavTag.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        // toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (mCurrentNavTag.getTitleIndex()) {
            case 0:
                return  new SaleIn();
            case 2:
                return new SaleDetail();

            default:
                return new SaleDetail();
        }
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

    private void uncheckAllMenuItems() {
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
    public void onSaleDetailFragmentInteraction(Uri uri){

    }

    @Override
    public void onSaleInFragmentInteraction(Uri uri) {

    }
}
