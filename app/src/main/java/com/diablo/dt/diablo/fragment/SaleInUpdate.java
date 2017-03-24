package com.diablo.dt.diablo.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.WSaleClient;
import com.diablo.dt.diablo.controller.DiabloSaleController;
import com.diablo.dt.diablo.controller.DiabloSaleRowController;
import com.diablo.dt.diablo.controller.DiabloSaleTableController;
import com.diablo.dt.diablo.entity.DiabloButton;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.model.SaleCalc;
import com.diablo.dt.diablo.model.SaleStock;
import com.diablo.dt.diablo.model.SaleStockAmount;
import com.diablo.dt.diablo.model.SaleUtils;
import com.diablo.dt.diablo.response.GetSaleNewResponse;
import com.diablo.dt.diablo.response.SaleDetailResponse;
import com.diablo.dt.diablo.rest.WSaleInterface;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.DiabloCellLabel;
import com.diablo.dt.diablo.view.DiabloCellView;
import com.diablo.dt.diablo.view.DiabloRowView;
import com.diablo.dt.diablo.view.DiabloSaleCalcView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaleInUpdate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaleInUpdate extends Fragment {
    private final static String LOG_TAG = "SaleInUpdate:";
    private final static DiabloUtils UTILS = DiabloUtils.instance();

    private DiabloCellLabel[] mLabels;
    private SparseArray<DiabloButton> mButtons;

    private List<MatchStock> mMatchStocks;
    private List<Retailer> mRetailers;

    private String [] mPriceTypes;
    private Integer   mSelectPrice;
    private String    mRSN;
    private String    mLastRSN;

    private DiabloSaleCalcView mSaleCalcView;
    private DiabloSaleController mSaleCalcController;
    private TableRow mCurrentSelectedRow;
    private View mFragment;

    private DiabloSaleTableController mSaleTableController;

    private StockSelect.OnNoFreeStockSelectListener mNoFreeStockListener;

    private Integer mBackFrom = R.string.back_from_unknown;

    private Retailer mCurrentRetailer;
    private SaleCalc mOldSaleCalc;
    private SaleCalc mNewSalCalc;


    private List<SaleStock> mOldSaleStocks;

    private final SaleInUpdateHandler mHandler = new SaleInUpdateHandler(this);

    public void setNoFreeStockSelectListener(StockSelect.OnNoFreeStockSelectListener listener){
        mNoFreeStockListener = listener;
    }

    public void setBackFrom(Integer form){
        mBackFrom = form;
    }

    public SaleInUpdate() {
        // Required empty public constructor
    }

    public void init() {
        mMatchStocks = Profile.instance().getMatchStocks();
        mPriceTypes = getResources().getStringArray(R.array.price_type_on_sale);
        if (null != mSaleTableController) {
            mSaleTableController.clear();
        }
        mSaleTableController = new DiabloSaleTableController ((TableLayout)mFragment.findViewById(R.id.t_sale));
        initCalcView(mFragment);
        getSaleInfoFromServer();
    }

    public void setRSN(String rsn) {
        this.mRSN = rsn;
    }

    public static SaleInUpdate newInstance(String param1, String param2) {
        SaleInUpdate fragment = new SaleInUpdate();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRSN = getArguments().getString(DiabloEnum.BUNDLE_PARAM_RSN);
            mLastRSN = mRSN;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initTitle();
        // Inflate the layout for this fragment
        mFragment = inflater.inflate(R.layout.fragment_sale_in_update, container, false);

        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        // create head
        mLabels = SaleUtils.createSaleLabelsFromTitle(getContext());
        ((TableLayout)mFragment.findViewById(R.id.t_sale_head))
            .addView(SaleUtils.createTableHeadFromLabels(getContext(), mLabels));

        init();

        return mFragment;
    }

    private void initCalcView(View view) {
        mSaleCalcView = new DiabloSaleCalcView();
        // retailer
        mSaleCalcView.setViewBalance(view.findViewById(R.id.sale_balance));
        mSaleCalcView.setViewAccBalance(view.findViewById(R.id.sale_accbalance));
        mSaleCalcView.setViewRetailer(view.findViewById(R.id.sale_select_retailer));
        mSaleCalcView.setViewShop(view.findViewById(R.id.sale_selected_shop));

        mSaleCalcView.setViewDatetime(view.findViewById(R.id.sale_selected_date));
        mSaleCalcView.setViewEmployee(view.findViewById(R.id.sale_select_employee));
        mSaleCalcView.setViewExtraCostType(view.findViewById(R.id.sale_select_extra_cost));
        mSaleCalcView.setViewExtraCost(view.findViewById(R.id.sale_extra_cost));

        mSaleCalcView.setViewComment(view.findViewById(R.id.sale_comment));
        mSaleCalcView.setViewHasPay(view.findViewById(R.id.sale_has_pay));
        mSaleCalcView.setViewShouldPay(view.findViewById(R.id.sale_should_pay));
        mSaleCalcView.setViewSaleTotal(view.findViewById(R.id.sale_total));

        mSaleCalcView.setViewCash(view.findViewById(R.id.sale_cash));
        mSaleCalcView.setViewCard(view.findViewById(R.id.sale_card));
        mSaleCalcView.setViewWire(view.findViewById(R.id.sale_wire));
        mSaleCalcView.setViewVerificate(view.findViewById(R.id.sale_verificate));
    }

    private void buildContent(SaleCalc calc, final List<SaleStock> stocks) {
        // calc
        mSaleCalcController = new DiabloSaleController(new SaleCalc(calc), mSaleCalcView);

        mSelectPrice = UTILS.toInteger(
            Profile.instance().getConfig(calc.getShop(), DiabloEnum.START_PRICE, DiabloEnum.TAG_PRICE));

        // copy the retailer
        mRetailers = new ArrayList<>();
        for (Retailer r: Profile.instance().getRetailers()) {
            if (r.getId().equals(calc.getRetailer())) {
                mRetailers.add(new Retailer(r, calc.getBalance()));
            } else {
                mRetailers.add(new Retailer(r));
            }
        }

        mSaleCalcController.setRetailer(calc.getRetailer(), mRetailers);
        mSaleCalcController.setShop(calc.getShop());
        mSaleCalcController.setDatetime(calc.getDatetime());

        // listener
        mSaleCalcController.setRetailerWatcher(getContext(), mRetailers);
        mSaleCalcController.setEmployeeWatcher();
        mSaleCalcController.setCommentWatcher();

        mSaleCalcController.setCashWatcher();
        mSaleCalcController.setCardWatcher();
        mSaleCalcController.setWireWatcher();
        mSaleCalcController.setVerificateWatcher();

        mSaleCalcController.setExtraCostWatcher();
        mSaleCalcController.setExtraCostTypeWatcher();

        // adapter
        mSaleCalcController.setEmployeeAdapter(getContext());
        mSaleCalcController.setExtraCostTypeAdapter(getContext());

        // stock
        for (SaleStock s: stocks) {
            DiabloSaleRowController controller = createRowWithStock(s);
            mSaleTableController.addRowControllerAtTop(controller);
        }

        mSaleTableController.addRowControllerAtTop(createEmptyRow());
        calcShouldPay();
    }

    private DiabloSaleRowController createRowWithStock(SaleStock stock) {
        TableRow row = new TableRow(getContext());

        DiabloSaleRowController controller = new DiabloSaleRowController(new DiabloRowView(row), new SaleStock(stock));

        for (DiabloCellLabel label: mLabels) {
            DiabloCellView cell = new DiabloCellView(label.createCell(getContext()), label);
            controller.addCell(cell);
            if (R.string.order_id == cell.getCellId()) {
                controller.setCellText(cell.getCellId(), UTILS.toString(stock.getOrderId()));
            }
            else if (R.string.good == cell.getCellId()){
                controller.setCellText(cell.getCellId(), stock.getName());
            }
            else if (R.string.price_type == cell.getCellId()) {
                controller.setPriceTypeAdapter(
                    getContext(), cell.getCellId(), mPriceTypes, stock.getSelectedPrice());
            }
            else if (R.string.price == cell.getCellId()) {
                controller.setCellText(cell.getCellId(), UTILS.toString(stock.getValidPrice()));
            }
            else if (R.string.discount == cell.getCellId()) {
                controller.setCellText(cell.getCellId(), UTILS.toString(stock.getDiscount()));
            }
            else if (R.string.fprice == cell.getCellId()) {
                controller.setCellText(cell.getCellId(), UTILS.toString(stock.getFinalPrice()));
                controller.getView().getCell(R.string.fprice).setCellFocusable(true);
            }
            else if (R.string.amount == cell.getCellId()) {
                controller.setCellText(cell.getCellId(), UTILS.toString(stock.getSaleTotal()));
                controller.getView().getCell(R.string.amount).setCellFocusable(true);
            }
            else if (R.string.calculate == cell.getCellId()) {
                controller.setCellText(cell.getCellId(), UTILS.toString(stock.getSalePrice()));
            }
            else if (R.string.comment == cell.getCellId()) {
                controller.setCellText(cell.getCellId(), stock.getComment());
            }
        }

        controller.getView().setOnLongClickListener(this);
        controller.setFinalPriceWatcher(mOnActionAfterSelectGood);
        controller.setAmountWatcher(mHandler, controller);
        controller.setRowWatcher();

        return controller;
    }

    private DiabloSaleRowController createEmptyRow() {
        TableRow row = new TableRow(getContext());
        DiabloSaleRowController controller = new DiabloSaleRowController(
            new DiabloRowView(row),
            new SaleStock());

        for (DiabloCellLabel label: mLabels) {
            DiabloCellView cell = new DiabloCellView(label.createCell(getContext()), label);
            controller.addCell(cell);
        }

        controller.setFinalPriceWatcher(mOnActionAfterSelectGood);
        controller.setGoodWatcher(getContext(), mMatchStocks, mSelectPrice, mLabels, mOnActionAfterSelectGood);
        controller.setAmountWatcher(mHandler, controller);
        controller.setPriceTypeAdapter(getContext(), mPriceTypes);

        return controller;
    }

    private DiabloSaleRowController.OnActionAfterSelectGood mOnActionAfterSelectGood =
        new DiabloSaleRowController.OnActionAfterSelectGood(){
            @Override
            public void onActionOfAmount(DiabloSaleRowController controller, DiabloCellView cell) {
                Integer orderId = mSaleTableController.contains(controller);
                if (!DiabloEnum.INVALID_INDEX.equals(orderId)) {
                    controller.setCellText(R.string.good, DiabloEnum.EMPTY_STRING);
                    UTILS.makeToast(
                        getContext(),
                        getContext().getResources().getString(R.string.sale_stock_exist)
                            + UTILS.toString(orderId),
                        Toast.LENGTH_SHORT);
                } else {
                    SaleStock stock = controller.getModel();
                    DiabloRowView row = controller.getView();
                    row.getCell(R.string.fprice).setCellFocusable(true);
                    if ( DiabloEnum.DIABLO_FREE.equals(stock.getFree()) ){
                        // enable amount focus
                        cell.setCellFocusable(true);
                        cell.requestFocus();
                        cell.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                        row.getCell(R.string.good).setCellFocusable(false);
                    } else {
                        switchToStockSelectFrame(controller.getModel(), R.string.add);
                    }
                }
            }

            @Override
            public void onActionOfFPrice() {
                calcShouldPay();
            }
        };

    private void switchToStockSelectFrame(SaleStock stock, Integer action) {
        Integer shop = mSaleCalcController.getShop();
        Integer retailer = mSaleCalcController.getRetailer();
        SaleUtils.switchToStockSelectFrame(stock, action, DiabloEnum.SALE_IN_UPDATE, retailer, shop, this);
    }

    private static class SaleInUpdateHandler extends Handler {
        WeakReference<Fragment> mFragment;

        SaleInUpdateHandler(Fragment fragment){
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SaleIn.SaleInHandler.SALE_TOTAL_CHANGED){
                Log.d(LOG_TAG, "receive message->" + msg.toString());
                DiabloSaleRowController controller = (DiabloSaleRowController) msg.obj;
                SaleStock stock = controller.getModel();
                DiabloRowView row = controller.getView();

                stock.setSaleTotal(msg.arg1);
                row.setCellText(R.string.calculate, stock.getSalePrice());

                if (DiabloEnum.DIABLO_FREE.equals(stock.getFree())){
                    if (0 != stock.getAmounts().size()) {
                        for (SaleStockAmount amount: stock.getAmounts()) {
                            amount.setSellCount(msg.arg1);
                        }
                    } else {
                        SaleStockAmount amount = new SaleStockAmount(
                            DiabloEnum.DIABLO_FREE_COLOR,
                            DiabloEnum.DIABLO_FREE_SIZE);
                        amount.setSellCount(msg.arg1);
                        amount.setStock(stock.getStockExist());
                        stock.clearAmounts();
                        stock.addAmount(amount);
                    }
                }

                final SaleInUpdate f = ((SaleInUpdate)mFragment.get());

                if (DiabloEnum.STARTING_SALE.equals(stock.getState())) {
                    if (0 != stock.getSaleTotal()) {
                        stock.setState(DiabloEnum.FINISHED_SALE);
                        Integer orderId = f.mSaleTableController.getCurrentRows();
                        controller.setOrderId(orderId);
                        row.setOnLongClickListener(f);

                        if (1 == f.mSaleTableController.size()){
                            f.mButtons.get(R.id.sale_out_save).enable();
                        }

                        f.mSaleTableController.addRowControllerAtTop(f.createEmptyRow());
                    }
                }

                //recalculate
                f.calcShouldPay();
            }

        }
    }

    public void calcShouldPay(){
        // calculate stock
        Integer total     = 0;
        Integer sell      = 0;
        Integer reject    = 0;
        Float   shouldPay = 0f;

        for (int i=1; i<mSaleTableController.getControllers().size(); i++){
            DiabloSaleRowController controller = mSaleTableController.getControllers().get(i);
            Integer saleTotal = controller.getSaleTotal();

            if (saleTotal > 0) {
                sell += saleTotal;
            } else {
                reject += saleTotal;
            }

            total += saleTotal;
            shouldPay += controller.getSalePrice();
        }

        mSaleCalcController.setSaleInfo(total, sell, reject);
        mSaleCalcController.setShouldPay(shouldPay);
        mSaleCalcController.resetAccBalance();

    }

    private void initTitle() {
        String title = getResources().getString(R.string.sale_in_update);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            initTitle();
            if (mBackFrom.equals(R.string.back_from_stock_select)){
                //
            }
            else {
                if (!mLastRSN.equals(mRSN)) {
                    init();
                }
            }
        }
    }

    private void getSaleInfoFromServer(){
        WSaleInterface face = WSaleClient.getClient().create(WSaleInterface.class);
        Call<GetSaleNewResponse> call = face.getSale(Profile.instance().getToken(), mRSN);

        call.enqueue(new Callback<GetSaleNewResponse>() {
            @Override
            public void onResponse(Call<GetSaleNewResponse> call, Response<GetSaleNewResponse> response) {
                Log.d(LOG_TAG, "success to get sale new by rsn:" + mRSN);
                GetSaleNewResponse news = response.body();
                recoverFromResponse(news.getSaleCalc(), news.getSaleNotes());
            }

            @Override
            public void onFailure(Call<GetSaleNewResponse> call, Throwable t) {
                Log.d(LOG_TAG, "fail to get sale new by rsn:" + mRSN);
            }
        });
    }

    private void recoverFromResponse(SaleDetailResponse.SaleDetail detail,
                                     List<GetSaleNewResponse.SaleNote> notes) {
        mOldSaleCalc = new SaleCalc(detail.getType());

        mOldSaleCalc.setRetailer(detail.getRetailer());
        mOldSaleCalc.setShop(detail.getShop());

        mOldSaleCalc.setDatetime(detail.getEntryDate());
        mOldSaleCalc.setEmployee(detail.getEmployee());
        mOldSaleCalc.setComment(detail.getComment());

        mOldSaleCalc.setBalance(detail.getBalance());
        mOldSaleCalc.setCash(detail.getCash());
        mOldSaleCalc.setCard(detail.getCard());
        mOldSaleCalc.setWire(detail.getWire());
        mOldSaleCalc.setVerificate(detail.getVerificate());
        mOldSaleCalc.setShouldPay(detail.getShouldPay());

        mOldSaleCalc.setExtraCostType(detail.getEPayType());
        mOldSaleCalc.setExtraCost(detail.getEPay());

        mOldSaleStocks  = new ArrayList<>();
        Integer orderId = 0;
        for (GetSaleNewResponse.SaleNote n: notes) {
            String styleNumber = n.getStyleNumber();
            Integer brandId = n.getBrandId();

            SaleStock stock = getSaleStocks(mOldSaleStocks, styleNumber, brandId);
            if (null == stock) {
                MatchStock matchStock = Profile.instance().getMatchStock(styleNumber, brandId);
                SaleStock s = new SaleStock(matchStock, n.getSelectedPrice());
                orderId++;
                s.setOrderId(orderId);
                s.setState(DiabloEnum.FINISHED_SALE);
                s.setSecond(n.getSecond());

                // s.setSelectedPrice(n.getSelectedPrice());
                s.setDiscount(n.getDiscount());
                s.setFinalPrice(n.getFinalPrice());

                SaleStockAmount amount = new SaleStockAmount(n.getColor(), n.getSize());
                amount.setSellCount(n.getSaleTotal());
                s.setSaleTotal(n.getSaleTotal());

                s.addAmount(amount);
                mOldSaleStocks.add(s);
            } else {
                SaleStockAmount amount = new SaleStockAmount(n.getColor(), n.getSize());
                amount.setSellCount(n.getSaleTotal());
                stock.setSaleTotal(stock.getSaleTotal() + n.getSaleTotal());
                stock.addAmount(amount);
            }
        }

        buildContent(new SaleCalc(mOldSaleCalc), mOldSaleStocks);
    }

    private SaleStock getSaleStocks(List<SaleStock> stocks, String styleNumber, Integer brandId){
        SaleStock stock = null;
        for (SaleStock s: stocks){
            if (styleNumber.equals(s.getStyleNumber()) && brandId.equals(s.getBrandId())){
                stock = s;
                break;
            }
        }

        return stock;
    }

    /**
     * option menu
     */

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_on_sale_in_update, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sale_in_update_back:
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_SALE_DETAIL);
                break;
            case R.id.sale_in_update_save:
                break;
            case R.id.sale_out_save:
                // startSale();
                break;
            default:
                // return super.onOptionsItemSelected(item);
                break;

        }

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        mCurrentSelectedRow = (TableRow) v;
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_on_sale, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        SaleStock stock = (SaleStock)mCurrentSelectedRow.getTag();
        Integer orderId = stock.getOrderId();
        // DiabloSaleRowController controller = mSaleTableController.getControllerByOrderId(orderId);

        if (getResources().getString(R.string.delete) == item.getTitle()){
            // delete
            mSaleTableController.removeByOrderId(orderId);
            // reorder
            mSaleTableController.reorder();

            calcShouldPay();
        }

        else if (getResources().getString(R.string.modify) == item.getTitle()){
            if (!DiabloEnum.DIABLO_FREE.equals(stock.getFree())){
                switchToStockSelectFrame(stock, R.string.modify);
            }
        }
        return true;
    }
}
