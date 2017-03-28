package com.diablo.dt.diablo.fragment;


import static com.diablo.dt.diablo.model.sale.SaleUtils.getSaleStocks;

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
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.model.sale.SaleCalc;
import com.diablo.dt.diablo.model.sale.SaleStock;
import com.diablo.dt.diablo.model.sale.SaleStockAmount;
import com.diablo.dt.diablo.model.sale.SaleUtils;
import com.diablo.dt.diablo.request.NewSaleRequest;
import com.diablo.dt.diablo.response.GetSaleNewResponse;
import com.diablo.dt.diablo.response.SaleDetailResponse;
import com.diablo.dt.diablo.rest.WSaleInterface;
import com.diablo.dt.diablo.utils.DiabloAlertDialog;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;
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

    private List<MatchStock> mMatchStocks;

    private String [] mPriceTypes;
    private SparseArray<DiabloButton> mButtons;
    private Integer   mSelectPrice;
    private Integer   mSysRetailer;
    private String    mRSN;
    private String    mLastRSN;
    private Integer   mRSNId;

    private DiabloSaleCalcView mSaleCalcView;
    private DiabloSaleController mSaleCalcController;
    private TableRow mCurrentSelectedRow;
    private View mFragment;

    private DiabloSaleTableController mSaleTableController;

    private StockSelect.OnNoFreeStockSelectListener mNoFreeStockListener;
    private Integer mBackFrom = R.string.back_from_unknown;

    // old
    private SaleCalc mOldSaleCalc;
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

//    private Retailer.OnRetailerChangeListener mOnRetailerChangeListener = new Retailer.OnRetailerChangeListener() {
//        @Override
//        public void afterAdd(Retailer retailer) {
//
//        }
//
//        @Override
//        public void afterGet(Retailer retailer) {
//            // copy the retailer
//            if (retailer.getId().equals(mOldSaleCalc.getRetailer())) {
//                mSaleCalcController.setRetailer(new Retailer(retailer, mOldSaleCalc.getBalance()));
//            }
//            else {
//                mSaleCalcController.setRetailer(retailer);
//            }
//            mSaleCalcController.removeRetailerWatcher();
//            mSaleCalcController.setRetailerWatcher(getContext());
//        }
//    };

    public void init() {
        mLastRSN = mRSN;
        mMatchStocks = Profile.instance().getMatchStocks();
        mPriceTypes = getResources().getStringArray(R.array.price_type_on_sale);
        mButtons.get(R.id.sale_in_update_save).enable();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initTitle();
        // Inflate the layout for this fragment
        mFragment = inflater.inflate(R.layout.fragment_sale_in_update, container, false);

        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        mSysRetailer = UTILS.toInteger(
            Profile.instance().getConfig(DiabloEnum.START_RETAILER, DiabloEnum.DIABLO_STRING_ZERO));

        // create head
        mLabels = SaleUtils.createSaleLabelsFromTitle(getContext());
        ((TableLayout)mFragment.findViewById(R.id.t_sale_head))
            .addView(SaleUtils.createTableHeadFromLabels(getContext(), mLabels));

        mButtons = new SparseArray<>();
        mButtons.put(R.id.sale_in_update_save, new DiabloButton(getContext(), R.id.sale_in_update_save));

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

    private void buildContent(final SaleCalc calc, final List<SaleStock> stocks) {
        // calc
        // mSaleCalcController = new DiabloSaleController(new SaleCalc(calc), mSaleCalcView);
        Retailer.getRetailer(getContext(), calc.getRetailer(), new Retailer.OnRetailerChangeListener() {
            @Override
            public void afterAdd(Retailer retailer) {

            }

            @Override
            public void afterGet(Retailer retailer) {
                mSaleCalcController = new DiabloSaleController(new SaleCalc(calc), mSaleCalcView);

                // copy the retailer
                if (retailer.getId().equals(mOldSaleCalc.getRetailer())) {
                    mSaleCalcController.setRetailer(new Retailer(retailer, mOldSaleCalc.getBalance()));
                }
                else {
                    mSaleCalcController.setRetailer(retailer);
                }

                mSaleCalcController.removeRetailerWatcher();
                mSaleCalcController.setRetailerWatcher(getContext());

                mSaleCalcView.setCashValue(calc.getCash());
                mSaleCalcView.setCardValue(calc.getCard());
                mSaleCalcView.setWireValue(calc.getWire());
                mSaleCalcView.setVerificateValue(calc.getVerificate());
                mSaleCalcView.setExtraCostValue(calc.getExtraCost());
                mSaleCalcController.setSaleCalcView(mSaleCalcView);
                mSaleCalcController.calcHasPay();

                mSelectPrice = UTILS.toInteger(
                    Profile.instance().getConfig(calc.getShop(), DiabloEnum.START_PRICE, DiabloEnum.TAG_PRICE));

                // mSaleCalcController.setRetailer(calc.getRetailer(), retailers);
                mSaleCalcController.setShop(calc.getShop());
                mSaleCalcController.setDatetime(calc.getDatetime());

                // listener
                // mSaleCalcController.setRetailerWatcher(getContext());
                // Retailer.getRetailer(getContext(), calc.getRetailer(), mOnRetailerChangeListener);
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

                // calculate balance
                mSaleTableController.addRowControllerAtTop(createEmptyRow());
                calcShouldPay();
            }
        });
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

                        f.mSaleTableController.addRowControllerAtTop(f.createEmptyRow());
                    }
                }

                //recalculate
                f.calcShouldPay();
            }

        }
    }

    public void calcShouldPay(){
        mSaleTableController.calcSaleInShouldPay(mSaleCalcController);
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
                SaleStock s = mNoFreeStockListener.afterSelectStock();

                switch (mNoFreeStockListener.getCurrentOperation()){
                    case R.string.action_save:
                        mSaleTableController.replaceRowController(s);
                        break;
                    case R.string.action_cancel:
                        if (s.getOrderId().equals(0)){
                            mSaleTableController.removeRowAtTop();
                            mSaleTableController.addRowControllerAtTop(createEmptyRow());
                        }
                        break;
                    default:
                        break;
                }

                mBackFrom = R.string.back_from_unknown;
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
                final GetSaleNewResponse news = response.body();
                mRSNId = news.getSaleCalc().getId();

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
        mOldSaleCalc.calcHasPay();
        mOldSaleCalc.calcAccBalance();

        mOldSaleStocks  = new ArrayList<>();
        Integer orderId = 0;
        for (GetSaleNewResponse.SaleNote n: notes) {
            String styleNumber = n.getStyleNumber();
            Integer brandId = n.getBrandId();

            SaleStock stock = SaleUtils.getSaleStocks(mOldSaleStocks, styleNumber, brandId);
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
                startUpdate();
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

    public List<SaleStockAmount> getUpdateSaleStockAmounts (List<SaleStockAmount> newAmounts,
                                                            List<SaleStockAmount> oldAmounts) {

        List<SaleStockAmount> saleStockAmounts = new ArrayList<>();
        for (SaleStockAmount n: newAmounts) {
            SaleStockAmount found = SaleUtils.getSaleStockAmounts(oldAmounts, n.getColorId(), n.getSize());
            // new
            if (null == found) {
                SaleStockAmount add = new SaleStockAmount(n, DiabloEnum.ADD_THE_STOCK);
                saleStockAmounts.add(add);
            }
            else {
                // update
                SaleStockAmount update = new SaleStockAmount(n, DiabloEnum.UPDATE_THE_STOCK);
                update.setSellCount(n.getSellCount() - found.getSellCount());
                if (0 != update.getSellCount()) {
                    saleStockAmounts.add(update);
                }
            }
        }

        // delete
        for (SaleStockAmount old: oldAmounts) {
            SaleStockAmount found = SaleUtils.getSaleStockAmounts(newAmounts, old.getColorId(), old.getSize());
            if (null == found) {
                SaleStockAmount delete = new SaleStockAmount(old, DiabloEnum.DELETE_THE_STOCK);
                saleStockAmounts.add(delete);
            }
        }

        return saleStockAmounts;
    }

    public List<SaleStock> getUpdateSaleStocks() {
        List<DiabloSaleRowController> controllers = mSaleTableController.getControllers();

        List<SaleStock> updateSaleStocks = new ArrayList<>();
        List<SaleStock> newSaleStocks = new ArrayList<>();

        for (DiabloSaleRowController controller: controllers) {
            if (0 != controller.getOrderId()) {
                newSaleStocks.add(controller.getModel());
            }
        }

        for (SaleStock stock: newSaleStocks) {
            SaleStock found = getSaleStocks(mOldSaleStocks, stock.getStyleNumber(), stock.getBrandId());
            // new
            if (null == found) {
                SaleStock add = new SaleStock(stock);
                add.setOperation(DiabloEnum.ADD_THE_STOCK);
                add.setColors(stock.getColors());
                add.setOrderSizes(stock.getOrderSizes());
                updateSaleStocks.add(add);
            }
            else {
                // get updated
                List<SaleStockAmount> updateSaleStockAmounts = getUpdateSaleStockAmounts(
                    stock.getAmounts(), found.getAmounts());
                if (0 != updateSaleStockAmounts.size()) {
                    SaleStock update = new SaleStock(stock);
                    update.setOperation(DiabloEnum.UPDATE_THE_STOCK);
                    update.setAmounts(updateSaleStockAmounts);
                    update.setColors(found.getColors());
                    update.setOrderSizes(found.getOrderSizes());
                    updateSaleStocks.add(update);
                }
                else {
                    if (!stock.getFinalPrice().equals(found.getFinalPrice())
                        || !stock.getDiscount().equals(found.getDiscount())
                        || !stock.getComment().equals(found.getComment())) {
                        SaleStock update = new SaleStock(stock);
                        // only change price, discount or comment, so the amount should be clear
                        update.clearAmounts();
                        update.setOperation(DiabloEnum.UPDATE_THE_STOCK);
                        updateSaleStocks.add(update);
                        update.setColors(found.getColors());
                        update.setOrderSizes(found.getOrderSizes());
                    }
                }
            }
        }

        // get delete
        for (SaleStock oldStock: mOldSaleStocks) {
            SaleStock found = SaleUtils.getSaleStocks(newSaleStocks, oldStock.getStyleNumber(), oldStock.getBrandId());
            if (null == found) {
                SaleStock delete = new SaleStock(oldStock);
                delete.setOperation(DiabloEnum.DELETE_THE_STOCK);
                updateSaleStocks.add(delete);
            }
        }

        return updateSaleStocks;
    }

    private void startUpdate() {
        List<SaleStock> updateStocks = getUpdateSaleStocks();

        NewSaleRequest saleRequest = new NewSaleRequest();
        for (SaleStock u: updateStocks) {

            NewSaleRequest.DiabloSaleStock d = new NewSaleRequest.DiabloSaleStock();
            d.setOrderId(u.getOrderId());
            d.setStyleNumber(u.getStyleNumber());
            d.setBrand(u.getBrand());
            d.setBrandId(u.getBrandId());
            d.setTypeId(u.getTypeId());

            d.setFirmId(u.getFirmId());
            d.setSex(u.getSex());
            d.setSeason(u.getSeason());
            d.setYear(u.getYear());

            List<NewSaleRequest.DiabloSaleStockAmount> uAmounts = new ArrayList<>();
            for (SaleStockAmount a: u.getAmounts()) {
                if ( a.getSellCount() != 0 ){
                    NewSaleRequest.DiabloSaleStockAmount saleAmount = new NewSaleRequest.DiabloSaleStockAmount();
                    saleAmount.setColorId(a.getColorId());
                    saleAmount.setSize(a.getSize());
                    saleAmount.setCount(a.getSellCount());
                    saleAmount.setOperation(a.getOperation());
                    uAmounts.add(saleAmount);
                }
            }
            d.setChangedAmounts(uAmounts);
            d.setOperation(u.getOperation());

            if (DiabloEnum.DELETE_THE_STOCK.equals(u.getOperation())
                || DiabloEnum.ADD_THE_STOCK.equals(u.getOperation())) {
                uAmounts.clear();
                for (SaleStockAmount a: u.getAmounts()) {
                    if ( a.getSellCount() != 0 ){
                        NewSaleRequest.DiabloSaleStockAmount saleAmount = new NewSaleRequest.DiabloSaleStockAmount();
                        saleAmount.setColorId(a.getColorId());
                        saleAmount.setSize(a.getSize());
                        saleAmount.setSellCount(a.getSellCount());
                        saleAmount.setOperation(a.getOperation());
                        uAmounts.add(saleAmount);
                    }
                }
                d.setAmount(uAmounts);
            }

            d.setSaleTotal(u.getSaleTotal());
            d.setFdiscount(u.getDiscount());
            d.setFprice(u.getFinalPrice());
            d.setPath(u.getPath());
            d.setComment(u.getComment());

            d.setSizeGroup(u.getSizeGroup());
            d.setOrderSizes(u.getOrderSizes());


            if ( null != u.getColors() && 0 != u.getColors().size()) {
                List<NewSaleRequest.DiabloSaleColor> saleColors = new ArrayList<>();
                for (DiabloColor c: u.getColors()) {
                    NewSaleRequest.DiabloSaleColor saleColor = new NewSaleRequest.DiabloSaleColor();
                    saleColor.setColorId(c.getColorId());
                    saleColor.setColorName(c.getName());
                    saleColors.add(saleColor);
                }
                d.setColors(saleColors);
            }

            d.setFree(u.getFree());
            d.setSellTye(u.getSelectedPrice());

            saleRequest.addStock(d);
        }

        NewSaleRequest.DiabloSaleCalc dCalc = new NewSaleRequest.DiabloSaleCalc();

        SaleCalc calc = mSaleCalcController.getSaleCalc();
        dCalc.setRsnId(mRSNId);
        dCalc.setRsn(mRSN);
        dCalc.setRetailer(calc.getRetailer());
        dCalc.setShop(calc.getShop());
        dCalc.setDatetime(calc.getDatetime());
        dCalc.setEmployee(calc.getEmployee());
        dCalc.setBalance(calc.getBalance());

        dCalc.setCash(calc.getCash());
        dCalc.setCard(calc.getCard());
        dCalc.setWire(calc.getWire());
        dCalc.setVerificate(calc.getVerificate());
        dCalc.setExtraCost(calc.getExtraCost());
        dCalc.setShouldPay(calc.getShouldPay());
        dCalc.setHasPay(calc.getHasPay());
        dCalc.setComment(calc.getComment());

        dCalc.setOldRetailer(mOldSaleCalc.getRetailer());
        dCalc.setOldBalance(mOldSaleCalc.getBalance());
        dCalc.setOldVerifyPay(mOldSaleCalc.getVerificate());
        dCalc.setOldShouldPay(mOldSaleCalc.getShouldPay());
        dCalc.setOldHasPay(mOldSaleCalc.getHasPay());
        dCalc.setOldDatetime(mOldSaleCalc.getDatetime());
        dCalc.setMode(mOldSaleCalc.getSaleType());
        dCalc.setSysRetailer(calc.getRetailer().equals(mSysRetailer));

        dCalc.setTotal(calc.getTotal());

        saleRequest.setSaleCalc(dCalc);

        if (0 == updateStocks.size()
            && dCalc.getCash().equals(mOldSaleCalc.getCash())
            && dCalc.getCard().equals(mOldSaleCalc.getCard())
            && dCalc.getWire().equals(mOldSaleCalc.getWire())
            && dCalc.getVerificate().equals(mOldSaleCalc.getVerificate())
            && dCalc.getComment().equals(mOldSaleCalc.getComment())) {

            new DiabloAlertDialog(
                getContext(),
                getResources().getString(R.string.sale_in_update),
                DiabloError.getInstance().getError(2699)).create();

        } else {
            startRequest(saleRequest);
        }
    }

    private void startRequest(NewSaleRequest request) {
        mButtons.get(R.id.sale_in_update_save).disable();

        final WSaleInterface face = WSaleClient.getClient().create(WSaleInterface.class);
        Call<com.diablo.dt.diablo.response.Response> call = face.updateSale(Profile.instance().getToken(), request);

        call.enqueue(new Callback<com.diablo.dt.diablo.response.Response>() {
            @Override
            public void onResponse(Call<com.diablo.dt.diablo.response.Response> call,
                                   Response<com.diablo.dt.diablo.response.Response> response) {
                // mButtons.get(R.id.sale_out_save).enable();

                final com.diablo.dt.diablo.response.Response res = response.body();
                if ( DiabloEnum.HTTP_OK == response.code() && res.getCode().equals(DiabloEnum.SUCCESS)) {
                    // refresh balance
//                    Retailer newRetailer = Profile.instance().getRetailerById(mSaleCalcController.getRetailer());
//                    Retailer oldRetailer = Profile.instance().getRetailerById(mOldSaleCalc.getRetailer());
//
//                    if (newRetailer.getId().equals(oldRetailer.getId())) {
//                        newRetailer.setBalance(
//                            newRetailer.getBalance()
//                                - mOldSaleCalc.getBalance()
//                                + mSaleCalcController.getSaleCalc().getBalance());
//                    } else {
//                        // back to old
//                        oldRetailer.setBalance(oldRetailer.getBalance() + mOldSaleCalc.getBalance());
//                        // reset new
//                        newRetailer.setBalance(newRetailer.getBalance() - mSaleCalcController.getSaleCalc().getBalance());
//                    }

                    // reset the controller
                    mSaleTableController.clear();
                    new DiabloAlertDialog(
                        getContext(),
                        false,
                        getResources().getString(R.string.sale_in_update),
                        getContext().getString(R.string.sale_in_update_success) + mRSN,
                        new DiabloAlertDialog.OnOkClickListener() {
                            @Override
                            public void onOk() {
                                // reset again to make sure clear certainly
                                mLastRSN = DiabloEnum.DIABLO_INVALID_RSN;
                                SaleUtils.switchToSlideMenu(SaleInUpdate.this, DiabloEnum.TAG_SALE_DETAIL);
                            }
                        }
                    ).create();
                } else {
                    mButtons.get(R.id.sale_in_update_save).enable();
                    Integer errorCode = response.code() == 0 ? res.getCode() : response.code();
                    String extraMessage = res == null ? "" : res.getError();
                    new DiabloAlertDialog(
                        getContext(),
                        getResources().getString(R.string.nav_sale_out),
                        DiabloError.getInstance().getError(errorCode) + extraMessage).create();
                }
            }

            @Override
            public void onFailure(Call<com.diablo.dt.diablo.response.Response> call, Throwable t) {
                mButtons.get(R.id.sale_in_update_save).enable();
                new DiabloAlertDialog(
                    getContext(),
                    getResources().getString(R.string.sale_in_update),
                    DiabloError.getInstance().getError(99)).create();
            }
        });
    }
}
