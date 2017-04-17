package com.diablo.dt.diablo.fragment.stock;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.sale.SaleUtils;
import com.diablo.dt.diablo.model.stock.EntryStock;
import com.diablo.dt.diablo.model.stock.EntryStockAmount;
import com.diablo.dt.diablo.model.stock.StockCalc;
import com.diablo.dt.diablo.model.stock.StockUtils;
import com.diablo.dt.diablo.request.stock.NewStockRequest;
import com.diablo.dt.diablo.response.stock.NewStockResponse;
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

public class StockIn extends Fragment {
    private final static DiabloUtils UTILS = DiabloUtils.instance();
    private final static String LOG_TAG = "StockIn:";

    private DiabloCellLabel[] mLabels;
    private SparseArray<DiabloButton> mButtons;

    private DiabloStockCalcView mStockCalcView;
    private DiabloStockCalcController mStockCalcController;

    private DiabloStockTableController mStockTableController;

    private List<MatchGood> mMatchGoods;
    private TableRow mCurrentSelectedRow;

    private StockInHandler mHandler = new StockInHandler(this);

    private GoodSelect.OnNoFreeGoodSelectListener mOnNoFreeGoodSelectListener;

    private Integer mBackFrom = R.string.back_from_unknown;

    public void setNoFreeGoodSelectListener(GoodSelect.OnNoFreeGoodSelectListener listener){
        mOnNoFreeGoodSelectListener = listener;
    }

    public void setBackFrom(Integer form){
        mBackFrom = form;
    }

    public StockIn() {
        // Required empty public constructor
    }

    public static StockIn newInstance(String param1, String param2) {
        return new StockIn();
    }

    private void initLabel() {
        mButtons = new SparseArray<>();
        mButtons.put(R.id.stock_in_save, new DiabloButton(getContext(), R.id.stock_in_save));
        mLabels = StockUtils.createStockLabelsFromTitle(getContext());
    }

    private void initCalc(View view) {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock_in, container, false);

        mMatchGoods = Profile.instance().getMatchGoods();

        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        initLabel();
        ((TableLayout)view.findViewById(R.id.t_stock_head)).addView(addHead());

        initCalc(view);
        mStockTableController = new DiabloStockTableController((TableLayout) view.findViewById(R.id.t_stock));

        Integer firmId = Profile.instance().getFirms().get(0).getId();

        init(firmId);
        return view;
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

    private void init(Integer firmId) {
        // Integer firmId = Profile.instance().getFirms().get(0).getId();
        Integer shop = Profile.instance().getLoginShop();

        mStockCalcController = new DiabloStockCalcController(new StockCalc(), mStockCalcView);

        mStockCalcController.setShop(shop);
        mStockCalcController.setDatetime(UTILS.currentDatetime());

        // listener
        mStockCalcController.setFirm(Profile.instance().getFirm(firmId));
        mStockCalcController.removeFirmWatcher();
        mStockCalcController.setFirmWatcher(getContext());

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

        // add empty row
        mStockTableController.clear();
        mStockTableController.addRowControllerAtTop(addEmptyRow());

        mButtons.get(R.id.stock_in_save).disable();
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
        StockUtils.switchToStockSelectFrame(mStockCalcController.getShop(), stock, operation, DiabloEnum.STOCK_IN, this);
    }

    private void calcShouldPay() {
        mStockTableController.calcStockShouldPay(mStockCalcController);
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

            if (1 == mStockTableController.size()){
                mButtons.get(R.id.stock_in_save).disable();
            }

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

        inflater.inflate(R.menu.action_on_stock_in, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.stock_in_back:
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_STOCK_DETAIL);
                break;
            case R.id.stock_in_save:
                startStock();
                break;
            case R.id.stock_in_to_good_new:
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_GOOD_NEW);
                break;
            case R.id.stock_in_next:
                init(mStockCalcController.getFirm());
                break;
            default:
                // return super.onOptionsItemSelected(item);
                break;

        }

        return true;
    }

    private static class StockInHandler extends Handler {
        WeakReference<Fragment> mFragment;

        StockInHandler(Fragment fragment){
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

                final StockIn f = ((StockIn)mFragment.get());

                if (StockUtils.STARTING_STOCK.equals(stock.getState())) {
                    if (0 != stock.getTotal()) {
                        stock.setState(StockUtils.FINISHED_STOCK);
                        Integer orderId = f.mStockTableController.getCurrentRows();
                        controller.setOrderId(orderId);
                        row.setOnLongClickListener(f);

                        if (1 == f.mStockTableController.size()){
                            f.mButtons.get(R.id.stock_in_save).enable();
                        }

                        f.mStockTableController.addRowControllerAtTop(f.addEmptyRow());
                    }
                }

                // recalculate
                f.calcShouldPay();
            }

        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden){
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
        }
    }

    private void startStock() {
        NewStockRequest stockRequest = new NewStockRequest();

        for (DiabloStockRowController controller: mStockTableController.getControllers()) {
            if (0 == controller.getOrderId()) {
                continue;
            }

            EntryStock s = controller.getModel();
            NewStockRequest.DiabloEntryStock d = new NewStockRequest.DiabloEntryStock();
            d.setGoodId(s.getGoodId());
            d.setStyleNumber(s.getStyleNumber());
            d.setBrandId(s.getBrandId());
            d.setFirmId(s.getFirmId());
            d.setTypeId(s.getTypeId());
            d.setSex(s.getSex());
            d.setYear(s.getYear());
            d.setSeason(s.getSeason());
            d.setsGroup(s.getsGroup());
            d.setFree(s.getFree());

            d.setOrgPrice(s.getOrgPrice());
            d.setTagPrice(s.getTagPrice());
            d.setPkgPrice(s.getPkgPrice());
            d.setPrice3(s.getPrice3());
            d.setPrice4(s.getPrice4());
            d.setPrice5(s.getPrice5());
            d.setDiscount(s.getDiscount());

            d.setAlarmDay(s.getAlarmDay());
            d.setTotal(s.getTotal());
            d.setPath(s.getPath());

            List<NewStockRequest.DiabloEntryStockAmount> amounts = new ArrayList<>();
            for (EntryStockAmount a: s.getAmounts()) {
                if ( 0 != a.getCount() ) {
                    NewStockRequest.DiabloEntryStockAmount amount = new NewStockRequest.DiabloEntryStockAmount();
                    amount.setColorId(a.getColorId());
                    amount.setSize(a.getSize());
                    amount.setCount(a.getCount());
                    amounts.add(amount);
                }
            }
            d.setEntryStockAmounts(amounts);

            stockRequest.addEntryStock(d);
        }

        NewStockRequest.DiabloStockCalc dCalc = new NewStockRequest.DiabloStockCalc();
        StockCalc calc = mStockCalcController.getStockCalc();
        dCalc.setFirmId(calc.getFirm());
        dCalc.setShopId(calc.getShop());
        dCalc.setDate(calc.getDatetime().substring(0, 10));
        dCalc.setDatetime(calc.getDatetime());
        dCalc.setEmployeeId(calc.getEmployee());
        dCalc.setComment(calc.getComment());

        dCalc.setTotal(calc.getTotal());
        dCalc.setBalance(calc.getBalance());
        dCalc.setCash(calc.getCash());
        dCalc.setCard(calc.getCard());
        dCalc.setWire(calc.getWire());
        dCalc.setVerificate(calc.getVerificate());
        dCalc.setShouldPay(calc.getShouldPay());
        dCalc.setHasPay(calc.getHasPay());
        dCalc.setExtraCostType(calc.getExtraCostType());
        dCalc.setExtraCost(calc.getExtraCost());

        stockRequest.setStockCalc(dCalc);

        startRequest(stockRequest);
    }

    private void startRequest(final NewStockRequest request) {
        mButtons.get(R.id.stock_in_save).disable();

        final StockInterface face = StockClient.getClient().create(StockInterface.class);
        Call<NewStockResponse> call = face.addStock(Profile.instance().getToken(), request);

        call.enqueue(new Callback<NewStockResponse>() {
            @Override
            public void onResponse(Call<NewStockResponse> call, retrofit2.Response<NewStockResponse> response) {
                mButtons.get(R.id.stock_in_save).enable();

                final NewStockResponse res = response.body();
                if ( DiabloEnum.HTTP_OK == response.code() && res.getCode().equals(DiabloEnum.SUCCESS)) {
                    // get firm
                    Firm firm = Profile.instance().getFirm(mStockCalcController.getFirm());
                    firm.setBalance(mStockCalcController.getStockCalc().calcAccBalance());

                    // add stock to match
                    for (DiabloStockRowController controller: mStockTableController.getControllers()) {
                        if (0 == controller.getOrderId()) {
                            continue;
                        }
                        EntryStock s = controller.getModel();
                        MatchStock matchStock = new MatchStock();
                        matchStock.setStyleNumber(s.getStyleNumber());
                        matchStock.setBrandId(s.getBrandId());
                        matchStock.setBrand(s.getBrand());
                        matchStock.setTypeId(s.getTypeId());
                        matchStock.setType(s.getType());

                        matchStock.setSex(s.getSex());
                        matchStock.setSeason(s.getSeason());
                        matchStock.setFirmId(s.getFirmId());
                        matchStock.setsGroup(s.getsGroup());
                        matchStock.setFree(s.getFree());
                        matchStock.setYear(s.getYear());

                        matchStock.setOrgPrice(s.getOrgPrice());
                        matchStock.setTagPrice(s.getTagPrice());
                        matchStock.setPkgPrice(s.getPkgPrice());
                        matchStock.setPrice3(s.getPrice3());
                        matchStock.setPrice4(s.getPrice4());
                        matchStock.setPrice5(s.getPrice5());
                        matchStock.setDiscount(s.getDiscount());

                        matchStock.setPath(s.getPath());
                        matchStock.setAlarmDay(s.getAlarmDay());

                        Profile.instance().addMatchStock(matchStock);
                    }

                    new DiabloAlertDialog(
                        getContext(),
                        false,
                        getResources().getString(R.string.nav_stock_in),
                        getContext().getString(R.string.stock_in_success) + res.getRsn(),
                        new DiabloAlertDialog.OnOkClickListener() {
                            @Override
                            public void onOk() {
                                // reset the controller
                                init(mStockCalcController.getFirm());
                            }
                        }).create();
                }
                else {
                    mButtons.get(R.id.stock_in_save).enable();
                    Integer errorCode = response.code() == 0 ? res.getCode() : response.code();
                    String extraMessage = res == null ? "" : res.getError();
                    new DiabloAlertDialog(
                        getContext(),
                        getResources().getString(R.string.nav_stock_in),
                        DiabloError.getError(errorCode) + extraMessage).create();
                }
            }

            @Override
            public void onFailure(Call<NewStockResponse> call, Throwable t) {
                mButtons.get(R.id.sale_in_update_save).enable();
                new DiabloAlertDialog(
                    getContext(),
                    getResources().getString(R.string.sale_in_update),
                    DiabloError.getError(99)).create();
            }
        });
    }
}
