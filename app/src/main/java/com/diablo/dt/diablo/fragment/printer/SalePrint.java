package com.diablo.dt.diablo.fragment.printer;


import com.google.gson.Gson;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.BankCard;
import com.diablo.dt.diablo.entity.BaseSetting;
import com.diablo.dt.diablo.entity.DiabloShop;
import com.diablo.dt.diablo.entity.Employee;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.model.sale.SaleCalc;
import com.diablo.dt.diablo.model.sale.SaleStock;
import com.diablo.dt.diablo.model.sale.SaleStockAmount;
import com.diablo.dt.diablo.model.sale.SaleUtils;
import com.diablo.dt.diablo.response.sale.GetSaleNewResponse;
import com.diablo.dt.diablo.response.sale.SaleDetailResponse;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SalePrint#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SalePrint extends Fragment {
    private static final String LOG_TAG = "SalePrint:";

    private String mRSN;
    private String mPaperWidth;

    private DiabloShop mShop;
    private Retailer mRetailer;
    private Employee mEmployee;

    private List<String> mComments;
    private List<BaseSetting> mPhones;
    private List<BankCard> mBankCards;

    private SaleCalc mSaleCalc;
    private List<SaleStock> mSaleStocks;


    WebView mWebView;
    WebSettings mWebSettings;
    SwipeRefreshLayout mSwipeRefresh;

    public SalePrint() {
        // Required empty public constructor
    }

    private void initTitle() {
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (null != bar) {
            bar.setTitle(getResources().getString(R.string.start_sale_print));
        }
    }

    private void init() {
        initTitle();
        // get sale
        SaleUtils.getSaleNewInfoFormServer(getContext(), mRSN, new SaleUtils.OnGetSaleNewFormSeverListener() {
            @Override
            public void afterGet(GetSaleNewResponse response) {
                recoverFromResponse(response.getSaleCalc(), response.getSaleNotes());
            }
        });
    }

    private void recoverFromResponse(SaleDetailResponse.SaleDetail detail,
                                     List<GetSaleNewResponse.SaleNote> notes) {
        mSaleCalc = new SaleCalc(detail.getType());

        mSaleCalc.setRetailer(detail.getRetailer());
        mSaleCalc.setShop(detail.getShop());
        mSaleCalc.setTotal(detail.getTotal());

        mSaleCalc.setDatetime(detail.getEntryDate());
        mSaleCalc.setEmployee(detail.getEmployee());
        mSaleCalc.setComment(detail.getComment());

        mSaleCalc.setBalance(detail.getBalance());
        mSaleCalc.setCash(detail.getCash());
        mSaleCalc.setCard(detail.getCard());
        mSaleCalc.setWire(detail.getWire());
        mSaleCalc.setVerificate(detail.getVerificate());
        mSaleCalc.setShouldPay(detail.getShouldPay());

        mSaleCalc.setExtraCostType(detail.getEPayType());
        mSaleCalc.setExtraCost(detail.getEPay());
        mSaleCalc.calcHasPay();
        mSaleCalc.calcAccBalance();

        mSaleStocks  = new ArrayList<>();
        Integer orderId = 0;
        for (GetSaleNewResponse.SaleNote n: notes) {
            String styleNumber = n.getStyleNumber();
            Integer brandId = n.getBrandId();

            SaleStock stock = SaleUtils.getSaleStock(mSaleStocks, styleNumber, brandId);
            if (null == stock) {
                MatchStock matchStock = Profile.instance().getMatchStock(styleNumber, brandId);
                SaleStock s = new SaleStock(matchStock, n.getSelectedPrice());

                orderId++;
                s.setOrderId(orderId);
                s.setState(DiabloEnum.FINISHED_SALE);
                s.setSecond(n.getSecond());
                s.setComment(n.getComment());

                // s.setSelectedPrice(n.getSelectedPrice());
                s.setDiscount(n.getDiscount());
                s.setFinalPrice(n.getFinalPrice());
                s.setSecond(n.getSecond());

                SaleStockAmount amount = new SaleStockAmount(n.getColor(), n.getSize());
                amount.setSellCount(n.getAmount());
                s.setSaleTotal(n.getSaleTotal());

                s.addAmount(amount);
                mSaleStocks.add(s);
            } else {
                SaleStockAmount amount = new SaleStockAmount(n.getColor(), n.getSize());
                amount.setSellCount(n.getAmount());
                // stock.setSaleTotal(stock.getSaleTotal() + n.getSaleTotal());
                stock.addAmount(amount);
            }
        }

        mShop =  DiabloUtils.getInstance().getShop(Profile.instance().getLoginShops(), detail.getShop());
        mRetailer = DiabloUtils.getInstance().getRetailer(Profile.instance().getRetailers(), detail.getRetailer());
        mEmployee = DiabloUtils.getInstance().getEmployeeByNumber(Profile.instance().getEmployees(), detail.getEmployee());

        // mWebView.loadUrl("file:///android_asset/salePrint.html");
        mWebView.loadUrl("file:///android_asset/salePrint.html");
        // mWebView.reload();
    }

    public static SalePrint newInstance(String param1, String param2) {
        SalePrint fragment = new SalePrint();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRSN = getArguments().getString(DiabloEnum.BUNDLE_PARAM_RSN);
        }

        mComments  = Profile.instance().getComments();
        mPhones    = Profile.instance().getPhones();
        mBankCards = Profile.instance().getBankCards();
        mPaperWidth = Profile.instance().getConfig(DiabloEnum.START_PAPER_WIDTH, "11");

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale_print, container, false);
        mWebView = (WebView) view.findViewById(R.id.salePrintWebView);

        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");
        mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.addJavascriptInterface(new PrintJSInterface(this), "PrintJs");

        // mWebView.loadUrl("file:///android_asset/salePrint.html");

//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                mSwipeRefresh.setRefreshing(false);
//            }
//        });

//        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeSalePrint);
//        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mWebView.reload();
//            }
//        });

        init();

        return  view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.add(Menu.NONE, 1001, Menu.NONE, getResources().getString(R.string.btn_back)).setIcon(R.drawable.ic_arrow_back_black_24dp)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add(Menu.NONE, 1002, Menu.NONE, getResources().getString(R.string.btn_print)).setIcon(R.drawable.ic_print_black_24dp)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1001: // cancel
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_SALE_DETAIL);
                break;
            case 1002: // print
                Log.d(LOG_TAG, "javascript:print()");
                mWebView.loadUrl("javascript:print()");
//                mWebView.evaluateJavascript("javascript:print()", new ValueCallback<String>() {
//                @Override
//                public void onReceiveValue(String value) {
//
//                }
//            });
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden){
            init();
        }
    }

    public void setPrintRSN(String rsn) {
        this.mRSN = rsn;
    }

    private class PrintJSInterface {
        WeakReference<Fragment> mFragment;

        PrintJSInterface(Fragment fragment){
            mFragment = new WeakReference<>(fragment);
        }

        @JavascriptInterface
        public String getSaleCalc() {
            // Log.d(LOG_TAG, "start sale new");
            String s = new Gson().toJson(((SalePrint)mFragment.get()).mSaleCalc);
            Log.d(LOG_TAG, s);
            return s;
        }

        @JavascriptInterface
        public String getSaleStock() {
            // Log.d(LOG_TAG, "start sale stock");
            String s = new Gson().toJson(((SalePrint)mFragment.get()).mSaleStocks);
            Log.d(LOG_TAG, s);
            return s;
        }

        @JavascriptInterface
        public String getSaleShop() {
            return ((SalePrint)mFragment.get()).mShop.getName();
        }

        @JavascriptInterface
        public String getEmployee() {
            return ((SalePrint)mFragment.get()).mEmployee.getName();
        }

        @JavascriptInterface
        public String getShopAddress() {
            return ((SalePrint)mFragment.get()).mShop.getAddress();
        }

        @JavascriptInterface
        public String getSaleRetailer() {
            return ((SalePrint)mFragment.get()).mRetailer.getName();
        }

        @JavascriptInterface
        public String getSaleRSN() {
            return ((SalePrint)mFragment.get()).mRSN;
        }

        @JavascriptInterface
        public String getPaperWidth() {
            return ((SalePrint)mFragment.get()).mPaperWidth;
        }

        @JavascriptInterface
        public String getComments() {
            String s = new Gson().toJson(((SalePrint)mFragment.get()).mComments);
            Log.d(LOG_TAG, s);
            return s;
        }

        @JavascriptInterface
        public String getPhones() {
            String s = new Gson().toJson(((SalePrint)mFragment.get()).mPhones);
            Log.d(LOG_TAG, s);
            return s;
        }

        @JavascriptInterface
        public String getBankCards() {
            String s = new Gson().toJson(((SalePrint)mFragment.get()).mBankCards);
            Log.d(LOG_TAG, s);
            return s;
        }

        @JavascriptInterface
        public void log(String msg) {
            Log.d(LOG_TAG, msg);
        }
    }

}
