package com.diablo.dt.diablo.fragment.stock;


import static com.diablo.dt.diablo.R.string.amount;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.StockClient;
import com.diablo.dt.diablo.controller.DiabloStockCalcController;
import com.diablo.dt.diablo.controller.DiabloStockRowController;
import com.diablo.dt.diablo.controller.DiabloStockTableController;
import com.diablo.dt.diablo.entity.DiabloButton;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.entity.MatchGood;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.sale.SaleUtils;
import com.diablo.dt.diablo.model.stock.EntryStock;
import com.diablo.dt.diablo.model.stock.EntryStockAmount;
import com.diablo.dt.diablo.model.stock.StockCalc;
import com.diablo.dt.diablo.model.stock.StockUtils;
import com.diablo.dt.diablo.request.stock.NewStockRequest;
import com.diablo.dt.diablo.response.stock.GetStockNewResponse;
import com.diablo.dt.diablo.response.stock.NewStockResponse;
import com.diablo.dt.diablo.response.stock.StockDetailResponse;
import com.diablo.dt.diablo.rest.StockInterface;
import com.diablo.dt.diablo.utils.DiabloAlertDialog;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.DiabloCellLabel;
import com.diablo.dt.diablo.view.DiabloCellView;
import com.diablo.dt.diablo.view.DiabloRowView;
import com.diablo.dt.diablo.view.stock.DiabloStockCalcView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class StockInUpdate extends Fragment {
    private final static DiabloUtils UTILS = DiabloUtils.instance();
    private final static String LOG_TAG = "StockInUpdate:";

    private DiabloCellLabel[] mLabels;
    private String [] mSeasons;
    private SparseArray<DiabloButton> mButtons;

    private DiabloStockCalcView mStockCalcView;
    private DiabloStockCalcController mStockCalcController;

    private DiabloStockTableController mStockTableController;

    private StockCalc mOldStockCalc;
    private List<EntryStock> mOldEntryStocks;

    private List<MatchGood> mMatchGoods;
    private TableRow mCurrentSelectedRow;
    private View mFragment;

    private StockInUpdateHandler mHandler = new StockInUpdateHandler(this);

    private GoodSelect.OnNoFreeGoodSelectListener mOnNoFreeGoodSelectListener;

    private Integer mBackFrom = R.string.back_from_unknown;

    private String    mRSN;
    private String    mLastRSN;
    private Integer   mRSNId;

    public void setNoFreeGoodSelectListener(GoodSelect.OnNoFreeGoodSelectListener listener){
        mOnNoFreeGoodSelectListener = listener;
    }

    public void setBackFrom(Integer form){
        mBackFrom = form;
    }

    public StockInUpdate() {
        // Required empty public constructor
    }

    public static StockInUpdate newInstance(String param1, String param2) {
        StockInUpdate fragment = new StockInUpdate();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initTitle() {
        String title = getResources().getString(R.string.stock_in_update);
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (null != bar) {
            bar.setTitle(title);
        }
    }

    private void initLabel() {
        mButtons = new SparseArray<>();
        mButtons.put(R.id.stock_in_update_save, new DiabloButton(getContext(), R.id.stock_in_update_save));
        mSeasons = getResources().getStringArray(R.array.seasons);
        mLabels = StockUtils.createStockLabelsFromTitle(getContext());
    }

    private void initCalcView(View view) {
        mStockCalcView = new DiabloStockCalcView();

        mStockCalcView.setViewFirm(view.findViewById(R.id.stock_select_firm));
        mStockCalcView.setViewShop(view.findViewById(R.id.stock_select_shop));
        mStockCalcView.setViewDatetime(view.findViewById(R.id.stock_selected_date));
        mStockCalcView.setViewEmployee(view.findViewById(R.id.stock_select_employee));

        mStockCalcView.setViewStockTotal(view.findViewById(R.id.stock_total));
        mStockCalcView.setViewShouldPay(view.findViewById(R.id.stock_should_pay));
        mStockCalcView.setViewCash(view.findViewById(R.id.stock_cash));
        mStockCalcView.setViewComment(view.findViewById(R.id.stock_comment));

        mStockCalcView.setViewBalance(view.findViewById(R.id.firm_balance));
        mStockCalcView.setViewHasPay(view.findViewById(R.id.stock_has_pay));
        mStockCalcView.setViewCard(view.findViewById(R.id.stock_card));
        mStockCalcView.setViewExtraCostType(view.findViewById(R.id.stock_select_extra_cost));

        mStockCalcView.setViewAccBalance(view.findViewById(R.id.firm_accBalance));
        mStockCalcView.setViewVerificate(view.findViewById(R.id.stock_verificate));
        mStockCalcView.setViewWire(view.findViewById(R.id.stock_wire));
        mStockCalcView.setViewExtraCost(view.findViewById(R.id.stock_extra_cost));
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
        mFragment = inflater.inflate(R.layout.fragment_stock_in_update, container, false);
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        initLabel();
        ((TableLayout)mFragment.findViewById(R.id.t_stock_head)).addView(addHead());

        init();

        return mFragment;
    }

    public void setRSN(String rsn) {
        this.mRSN = rsn;
    }

    private TableRow addHead(){
        TableRow row = new TableRow(getContext());

        for (DiabloCellLabel label: mLabels) {
            TextView cell = new TextView(getContext());
            cell.setLayoutParams(label.getTableRowLayoutParams());
            cell.setTypeface(null, Typeface.BOLD);
            cell.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            cell.setTextSize(label.getSize());
            cell.setText(label.getLabel());

            row.addView(cell);
        }

        return row;
    }

    private void init() {
        mLastRSN = mRSN;
        mMatchGoods = Profile.instance().getMatchGoods();
        if (null != mStockTableController) {
            mStockTableController.clear();
        }

        mStockTableController = new DiabloStockTableController((TableLayout) mFragment.findViewById(R.id.t_stock));

        initCalcView(mFragment);
        getStockInfoFromServer();
    }

    private void recoverFromResponse(StockDetailResponse.StockDetail detail,
                                     List<GetStockNewResponse.StockNote> notes) {
        mOldStockCalc = new StockCalc(detail.getType());

        mOldStockCalc.setFirm(detail.getFirm());
        mOldStockCalc.setShop(detail.getShop());

        mOldStockCalc.setDatetime(detail.getEntryDate());
        mOldStockCalc.setEmployee(detail.getEmployee());
        mOldStockCalc.setComment(detail.getComment());

        mOldStockCalc.setBalance(detail.getBalance());
        mOldStockCalc.setCash(detail.getCash());
        mOldStockCalc.setCard(detail.getCard());
        mOldStockCalc.setWire(detail.getWire());
        mOldStockCalc.setVerificate(detail.getVerificate());
        mOldStockCalc.setShouldPay(detail.getShouldPay());

        mOldStockCalc.setExtraCostType(detail.getEPayType());
        mOldStockCalc.setExtraCost(detail.getEPay());
        mOldStockCalc.calcHasPay();
        mOldStockCalc.calcAccBalance();

        mOldEntryStocks  = new ArrayList<>();
        Integer orderId = 0;
        for (GetStockNewResponse.StockNote n: notes) {
            String styleNumber = n.getStyleNumber();
            Integer brandId = n.getBrandId();

            EntryStock stock = StockUtils.getEntryStock(mOldEntryStocks, styleNumber, brandId);
            if (null == stock) {
                MatchGood matchGood = Profile.instance().getMatchGood(styleNumber, brandId);
                EntryStock s = new EntryStock();
                s.init(matchGood);

                orderId++;
                s.setOrderId(orderId);
                s.setState(StockUtils.FINISHED_STOCK);
                s.setDiscount(n.getDiscount());

                EntryStockAmount amount = new EntryStockAmount(n.getColorId(), n.getSize());
                amount.setCount(n.getAmount());
                s.setTotal(n.getAmount());

                s.addAmount(amount);
                mOldEntryStocks.add(s);
            } else {
                EntryStockAmount amount = new EntryStockAmount(n.getColorId(), n.getSize());
                amount.setCount(n.getAmount());
                stock.setTotal(stock.getTotal() + n.getAmount());
                stock.addAmount(amount);
            }
        }

        buildContent(new StockCalc(mOldStockCalc), mOldEntryStocks);
    }

    private void buildContent(final StockCalc calc, final List<EntryStock> stocks) {
        mStockCalcController = new DiabloStockCalcController(new StockCalc(calc), mStockCalcView);

        mStockCalcController.setShop(calc.getShop());
        mStockCalcController.setDatetime(calc.getDatetime());

        // listener when select firm
        mStockCalcController.setFirm(Profile.instance().getFirm(calc.getFirm()));
        mStockCalcController.removeFirmWatcher();
        mStockCalcController.setFirmWatcher(getContext(), Profile.instance().getFirms());

        mStockCalcView.setCashValue(calc.getCash());
        mStockCalcView.setCardValue(calc.getCard());
        mStockCalcView.setWireValue(calc.getWire());
        mStockCalcView.setVerificateValue(calc.getVerificate());
        mStockCalcView.setExtraCostValue(calc.getExtraCost());

        mStockCalcController.setStockCalcView(mStockCalcView);

        mStockCalcController.setEmployeeWatcher();
        mStockCalcController.setCommentWatcher();

        mStockCalcController.setCashWatcher();
        mStockCalcController.setCardWatcher();
        mStockCalcController.setWireWatcher();
        mStockCalcController.setVerificateWatcher();

        mStockCalcController.setExtraCostWatcher();
        mStockCalcController.setExtraCostTypeWatcher();

        // adapter
        mStockCalcController.setEmployeeAdapter(getContext());
        mStockCalcController.setExtraCostTypeAdapter(getContext());

        for (EntryStock s: stocks) {
            DiabloStockRowController controller = createRowWithStock(s);
            mStockTableController.addRowControllerAtTop(controller);
        }

        mStockTableController.addRowControllerAtTop(addEmptyRow());
        calcShouldPay();
    }

    private DiabloStockRowController createRowWithStock(EntryStock stock) {
        TableRow row = new TableRow(getContext());

        DiabloStockRowController controller = new DiabloStockRowController(
            new DiabloRowView(row), new EntryStock(stock));

        for (DiabloCellLabel label: mLabels) {
            DiabloCellView cell = new DiabloCellView(label.createCell(getContext()), label);
            controller.addCell(cell);

            if (R.string.order_id == cell.getCellId()) {
                controller.setCellText(cell.getCellId(), UTILS.toString(stock.getOrderId()));
            }
            else if (R.string.good == cell.getCellId()){
                controller.setCellText(cell.getCellId(), stock.getName());
            }
            else if (R.string.year == cell.getCellId()) {
                controller.setCellText(cell.getCellId(), UTILS.toString(stock.getYear()));
            }
            else if (R.string.season == cell.getCellId()) {
                controller.setCellText(cell.getCellId(), mSeasons[stock.getSeason()]);
            }
            else if (R.string.org_price == cell.getCellId()) {
                controller.setCellText(cell.getCellId(), UTILS.toString(stock.getOrgPrice()));
            }
            else if (R.string.amount == cell.getCellId()) {
                controller.setCellText(cell.getCellId(), UTILS.toString(stock.getTotal()));
                if (DiabloEnum.DIABLO_FREE.equals(stock.getFree())) {
                    controller.getView().getCell(amount).setCellFocusable(true);
                }
            }
            else if (R.string.calculate == cell.getCellId()) {
                controller.setCellText(cell.getCellId(), UTILS.toString(stock.calcStockPrice()));
            }
        }

        controller.getView().setOnLongClickListener(this);
        controller.setAmountWatcher(mHandler, controller);
        controller.addListenerOfAmountChange();

        return controller;
    }

    private DiabloStockRowController addEmptyRow() {
        TableRow row = new TableRow(getContext());
        DiabloStockRowController controller = new DiabloStockRowController(
            new DiabloRowView(row),
            new EntryStock());

        for (DiabloCellLabel label: mLabels) {
            DiabloCellView cell = new DiabloCellView(label.createCell(getContext()), label);
            controller.addCell(cell);
        }

        controller.setAmountWatcher(mHandler, controller);
        controller.setGoodWatcher(
            getContext(),
            mStockCalcController.getStockCalc(),
            mMatchGoods,
            mLabels,
            mOnActionAfterSelectGood);

        return controller;
    }

    private DiabloStockRowController.OnActionAfterSelectGood mOnActionAfterSelectGood =
        new DiabloStockRowController.OnActionAfterSelectGood(){
            @Override
            public void onActionOfAmount(DiabloStockRowController controller, DiabloCellView cell) {
                if (!mStockTableController.checkSameFirm(mStockCalcController.getFirm())) {
                    controller.setCellText(R.string.good, DiabloEnum.EMPTY_STRING);
                    UTILS.makeToast(
                        getContext(),
                        getResources().getString(R.string.different_good),
                        Toast.LENGTH_SHORT);
                }
                else {
                    Integer orderId = mStockTableController.contains(controller);
                    if (!DiabloEnum.INVALID_INDEX.equals(orderId)) {
                        controller.setCellText(R.string.good, DiabloEnum.EMPTY_STRING);
                        UTILS.makeToast(
                            getContext(),
                            getContext().getResources().getString(R.string.sale_stock_exist)
                                + UTILS.toString(orderId),
                            Toast.LENGTH_SHORT);
                    } else {
                        EntryStock stock = controller.getModel();
                        DiabloRowView row = controller.getView();
                        if ( DiabloEnum.DIABLO_FREE.equals(stock.getFree()) ){
                            cell.setCellFocusable(true);
                            cell.requestFocus();
                            row.getCell(R.string.good).setCellFocusable(false);
                        } else {
                            switchToStockSelectFrame(controller.getModel(), R.string.add);
                        }
                    }
                }
            }
        };

    private void switchToStockSelectFrame(EntryStock stock, Integer operation) {
        StockUtils.switchToStockSelectFrame(
            mStockCalcController.getShop(),
            stock,
            operation,
            DiabloEnum.STOCK_IN_UPDATE,
            this);
    }

    private void calcShouldPay() {
        mStockTableController.calcStockShouldPay(mStockCalcController);
    }

    private static class StockInUpdateHandler extends Handler {
        WeakReference<Fragment> mFragment;

        StockInUpdateHandler(Fragment fragment){
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == StockUtils.STOCK_TOTAL_CHANGED){
                Log.d(LOG_TAG, "receive message->" + msg.toString());
                DiabloStockRowController controller = (DiabloStockRowController) msg.obj;
                EntryStock stock = controller.getModel();
                DiabloRowView row = controller.getView();

                stock.setTotal(msg.arg1);
                row.setCellText(R.string.calculate, stock.calcStockPrice());

                if (DiabloEnum.DIABLO_FREE.equals(stock.getFree())){
                    if (0 != stock.getAmounts().size()) {
                        for (EntryStockAmount amount: stock.getAmounts()) {
                            amount.setCount(msg.arg1);
                        }
                    } else {
                        EntryStockAmount amount = new EntryStockAmount(
                            DiabloEnum.DIABLO_FREE_COLOR,
                            DiabloEnum.DIABLO_FREE_SIZE);
                        amount.setCount(msg.arg1);
                        stock.clearAmounts();
                        stock.addAmount(amount);
                    }
                }

                final StockInUpdate f = ((StockInUpdate)mFragment.get());

                if (StockUtils.STARTING_STOCK.equals(stock.getState())) {
                    if (0 != stock.getTotal()) {
                        stock.setState(StockUtils.FINISHED_STOCK);
                        Integer orderId = f.mStockTableController.getCurrentRows();
                        controller.setOrderId(orderId);
                        row.setOnLongClickListener(f);

//                        if (1 == f.mStockTableController.size()){
//                            f.mButtons.get(R.id.stock_in_update_save).enable();
//                        }

                        f.mStockTableController.addRowControllerAtTop(f.addEmptyRow());
                    }
                }

                // recalculate
                f.calcShouldPay();
            }

        }
    }

    private void getStockInfoFromServer() {
        StockUtils.getStockNewInfoFormServer(mRSN, new StockUtils.OnGetStockNewFormSeverListener() {
            @Override
            public void afterGet(GetStockNewResponse response) {
                mRSNId = response.getStockCalc().getId();
                recoverFromResponse(response.getStockCalc(), response.getStockNotes());
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            initTitle();
            if (mBackFrom.equals(R.string.back_from_good_select)){
                EntryStock s = mOnNoFreeGoodSelectListener.afterSelectGood();

                switch (mOnNoFreeGoodSelectListener.getCurrentOperation()){
                    case R.string.action_save:
                        mStockTableController.replaceRowController(s);
                        break;
                    case R.string.action_cancel:
                        if (s.getOrderId().equals(0)){
                            mStockTableController.removeRowAtTop();
                            mStockTableController.addRowControllerAtTop(addEmptyRow());
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        mCurrentSelectedRow = (TableRow) v;
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_on_stock, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        EntryStock stock = (EntryStock)mCurrentSelectedRow.getTag();
        Integer orderId = stock.getOrderId();
        // DiabloSaleRowController controller = mSaleTableController.getControllerByOrderId(orderId);

        if (getResources().getString(R.string.delete) == item.getTitle()){
            // delete
            mStockTableController.removeByOrderId(orderId);
            // reorder
            mStockTableController.reorder();

//            if (1 == mStockTableController.size()){
//                mButtons.get(R.id.stock_in_update_save).disable();
//            }

            calcShouldPay();
        }

        else if (getResources().getString(R.string.modify) == item.getTitle()){
            if (!DiabloEnum.DIABLO_FREE.equals(stock.getFree())){
                switchToStockSelectFrame(stock, R.string.modify);
            }
        }
        return true;
    }

    /**
     * option menu
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        for (Integer i=0; i<mButtons.size(); i++){
            Integer key = mButtons.keyAt(i);
            DiabloButton button = mButtons.get(key);
            menu.findItem(button.getResId()).setEnabled(button.isEnabled());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();

        inflater.inflate(R.menu.action_on_stock_in_update, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.stock_in_update_back:
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_STOCK_DETAIL);
                break;
            case R.id.stock_in_update_save:
                startUpdate();
                break;
            default:
                // return super.onOptionsItemSelected(item);
                break;

        }

        return true;
    }

    public List<EntryStockAmount> getUpdateEntryStockAmounts (List<EntryStockAmount> newAmounts,
                                                              List<EntryStockAmount> oldAmounts) {

        List<EntryStockAmount> entryStockAmounts = new ArrayList<>();
        for (EntryStockAmount n: newAmounts) {
            EntryStockAmount found = StockUtils.getEntryStockAmounts(oldAmounts, n.getColorId(), n.getSize());
            // new
            if (null == found) {
                EntryStockAmount add = new EntryStockAmount(n, DiabloEnum.ADD_THE_STOCK);
                entryStockAmounts.add(add);
            }
            else {
                // update
                EntryStockAmount update = new EntryStockAmount(n, DiabloEnum.UPDATE_THE_STOCK);
                update.setCount(n.getCount() - found.getCount());
                if (0 != update.getCount()) {
                    entryStockAmounts.add(update);
                }
            }
        }

        // delete
        for (EntryStockAmount old: oldAmounts) {
            EntryStockAmount found = StockUtils.getEntryStockAmounts(newAmounts, old.getColorId(), old.getSize());
            if (null == found) {
                EntryStockAmount delete = new EntryStockAmount(old, DiabloEnum.DELETE_THE_STOCK);
                entryStockAmounts.add(delete);
            }
        }

        return entryStockAmounts;
    }

    public List<EntryStock> getUpdateEntryStocks() {
        List<DiabloStockRowController> controllers = mStockTableController.getControllers();

        List<EntryStock> updateEntryStocks = new ArrayList<>();
        List<EntryStock> newEntryStocks = new ArrayList<>();

        for (DiabloStockRowController controller: controllers) {
            if (0 != controller.getOrderId()) {
                newEntryStocks.add(controller.getModel());
            }
        }

        for (EntryStock stock: newEntryStocks) {
            EntryStock found = StockUtils.getEntryStock(mOldEntryStocks, stock.getStyleNumber(), stock.getBrandId());
            // new
            if (null == found) {
                EntryStock add = new EntryStock(stock);
                add.setOperation(DiabloEnum.ADD_THE_STOCK);
                updateEntryStocks.add(add);
            }
            else {
                // get updated
                List<EntryStockAmount> updateEntryStockAmounts = getUpdateEntryStockAmounts(
                    stock.getAmounts(), found.getAmounts());

                if (0 != updateEntryStockAmounts.size()) {
                    EntryStock update = new EntryStock(stock);
                    update.setOperation(DiabloEnum.UPDATE_THE_STOCK);
                    update.setAmounts(updateEntryStockAmounts);
                    updateEntryStocks.add(update);
                }
                else {
                    if (!stock.getDiscount().equals(found.getDiscount())
                        || !stock.getOrgPrice().equals(found.getOrgPrice())) {
                        EntryStock update = new EntryStock(stock);
                        // only change price, discount, so the amount should be clear
                        update.clearAmounts();
                        update.setOperation(DiabloEnum.UPDATE_THE_STOCK);
                        updateEntryStocks.add(update);
                    }
                }
            }
        }

        // get delete
        for (EntryStock oldStock: mOldEntryStocks) {
            EntryStock found = StockUtils.getEntryStock(newEntryStocks, oldStock.getStyleNumber(), oldStock.getBrandId());
            if (null == found) {
                EntryStock delete = new EntryStock(oldStock);
                delete.setOperation(DiabloEnum.DELETE_THE_STOCK);
                updateEntryStocks.add(delete);
            }
        }

        return updateEntryStocks;
    }

    private void startUpdate() {
        List<EntryStock> updateStocks = getUpdateEntryStocks();

        NewStockRequest stockRequest = new NewStockRequest();
        for (EntryStock u: updateStocks) {

            NewStockRequest.DiabloEntryStock d = new NewStockRequest.DiabloEntryStock();
            d.setStyleNumber(u.getStyleNumber());
            d.setBrandId(u.getBrandId());
            d.setTypeId(u.getTypeId());

            d.setSex(u.getSex());
            d.setSeason(u.getSeason());
            d.setOperation(u.getOperation());
            d.setsGroup(u.getsGroup());
            d.setFree(u.getFree());

            d.setOrgPrice(u.getOrgPrice());
            d.setTagPrice(u.getTagPrice());
            d.setPkgPrice(u.getPkgPrice());
            d.setPrice3(u.getPrice3());
            d.setPrice4(u.getPrice4());
            d.setPrice5(u.getPrice5());
            d.setDiscount(u.getDiscount());

            d.setTotal(u.getTotal());

            List<NewStockRequest.DiabloEntryStockAmount> uAmounts = new ArrayList<>();
            for (EntryStockAmount a: u.getAmounts()) {
                if ( a.getCount() != 0 ){
                    NewStockRequest.DiabloEntryStockAmount stockAmount = new NewStockRequest.DiabloEntryStockAmount();
                    stockAmount.setColorId(a.getColorId());
                    stockAmount.setSize(a.getSize());
                    stockAmount.setCount(a.getCount());
                    stockAmount.setOperation(a.getOperation());
                    uAmounts.add(stockAmount);
                }
            }

            d.setChangedAmounts(uAmounts);

            if (DiabloEnum.DELETE_THE_STOCK.equals(u.getOperation())
                || DiabloEnum.ADD_THE_STOCK.equals(u.getOperation())) {
                uAmounts.clear();
                for (EntryStockAmount a: u.getAmounts()) {
                    if ( a.getCount() != 0 ){
                        NewStockRequest.DiabloEntryStockAmount saleAmount = new NewStockRequest.DiabloEntryStockAmount();
                        saleAmount.setColorId(a.getColorId());
                        saleAmount.setSize(a.getSize());
                        saleAmount.setCount(a.getCount());
                        saleAmount.setOperation(a.getOperation());
                        uAmounts.add(saleAmount);
                    }
                }
                d.setEntryStockAmounts(uAmounts);
            }

            stockRequest.addEntryStock(d);
        }

        NewStockRequest.DiabloStockCalc dCalc = new NewStockRequest.DiabloStockCalc();

        StockCalc calc = mStockCalcController.getStockCalc();
        dCalc.setRsnId(mRSNId);
        dCalc.setRsn(mRSN);
        dCalc.setFirmId(calc.getFirm());
        dCalc.setShopId(calc.getShop());
        dCalc.setDatetime(calc.getDatetime());

        dCalc.setEmployeeId(calc.getEmployee());
        dCalc.setComment(calc.getComment());
        dCalc.setTotal(calc.getTotal());
        dCalc.setBalance(calc.getBalance());

        dCalc.setCash(calc.getCash());
        dCalc.setCard(calc.getCard());
        dCalc.setWire(calc.getWire());
        dCalc.setVerificate(calc.getVerificate());

        dCalc.setExtraCost(calc.getExtraCost());
        dCalc.setShouldPay(calc.getShouldPay());
        dCalc.setHasPay(calc.getHasPay());


        dCalc.setOldFirm(mOldStockCalc.getFirm());
        dCalc.setOldBalance(mOldStockCalc.getBalance());
        dCalc.setOldVerificate(mOldStockCalc.getVerificate());
        dCalc.setOldShouldPay(mOldStockCalc.getShouldPay());
        dCalc.setOldHasPay(mOldStockCalc.getHasPay());
        dCalc.setOldDatetime(mOldStockCalc.getDatetime());

        stockRequest.setStockCalc(dCalc);

        if (0 == updateStocks.size()
            && dCalc.getFirmId().equals(mOldStockCalc.getFirm())
            && dCalc.getCash().equals(mOldStockCalc.getCash())
            && dCalc.getCard().equals(mOldStockCalc.getCard())
            && dCalc.getWire().equals(mOldStockCalc.getWire())
            && dCalc.getVerificate().equals(mOldStockCalc.getVerificate())
            && dCalc.getComment().equals(mOldStockCalc.getComment())) {

            new DiabloAlertDialog(
                getContext(),
                getResources().getString(R.string.sale_in_update),
                DiabloError.getInstance().getError(2699)).create();

        } else {
            startRequest(stockRequest);
        }
    }

    private void startRequest(NewStockRequest request) {
        mButtons.get(R.id.stock_in_update_save).disable();

        final StockInterface face = StockClient.getClient().create(StockInterface.class);
        Call<NewStockResponse> call = face.updateStock(Profile.instance().getToken(), request);

        call.enqueue(new Callback<NewStockResponse>() {
            @Override
            public void onResponse(Call<NewStockResponse> call, retrofit2.Response<NewStockResponse> response) {
                mButtons.get(R.id.stock_in_update_save).enable();

                final NewStockResponse res = response.body();
                if (DiabloEnum.HTTP_OK == response.code() && res.getCode().equals(DiabloEnum.SUCCESS)) {
                    // reset firm balance
                    Firm firm = Profile.instance().getFirm(mStockCalcController.getFirm());
                    firm.setBalance(mStockCalcController.getStockCalc().calcAccBalance());
                    if (mStockCalcController.getFirm().equals(mOldStockCalc.getFirm())) {
                        Firm oldFirm = Profile.instance().getFirm(mOldStockCalc.getFirm());
                        oldFirm.setBalance(mOldStockCalc.getBalance());
                    }

                    new DiabloAlertDialog(
                        getContext(),
                        false,
                        getResources().getString(R.string.stock_in_update),
                        getContext().getString(R.string.stock_in_update_success) + res.getRsn(),
                        new DiabloAlertDialog.OnOkClickListener() {
                            @Override
                            public void onOk() {
                                SaleUtils.switchToSlideMenu(StockInUpdate.this, DiabloEnum.TAG_STOCK_DETAIL);
                            }
                        }).create();
                } else {
                    mButtons.get(R.id.stock_in_update_save).enable();
                    Integer errorCode = response.code() == 0 ? res.getCode() : response.code();
                    String extraMessage = res == null ? "" : res.getError();
                    new DiabloAlertDialog(
                        getContext(),
                        getResources().getString(R.string.stock_in_update),
                        DiabloError.getInstance().getError(errorCode) + extraMessage).create();
                }
            }

            @Override
            public void onFailure(Call<NewStockResponse> call, Throwable t) {
                mButtons.get(R.id.stock_in_update_save).enable();
                new DiabloAlertDialog(
                    getContext(),
                    getResources().getString(R.string.stock_in_update),
                    DiabloError.getInstance().getError(99)).create();
            }
        });
    }
}
