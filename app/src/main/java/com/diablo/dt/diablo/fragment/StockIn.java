package com.diablo.dt.diablo.fragment;


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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.controller.DiabloStockCalcController;
import com.diablo.dt.diablo.controller.DiabloStockRowController;
import com.diablo.dt.diablo.controller.DiabloStockTableController;
import com.diablo.dt.diablo.entity.DiabloButton;
import com.diablo.dt.diablo.entity.MatchGood;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.stock.EntryStock;
import com.diablo.dt.diablo.model.stock.EntryStockAmount;
import com.diablo.dt.diablo.model.stock.StockCalc;
import com.diablo.dt.diablo.model.stock.StockUtils;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.DiabloCellLabel;
import com.diablo.dt.diablo.view.DiabloCellView;
import com.diablo.dt.diablo.view.DiabloRowView;
import com.diablo.dt.diablo.view.DiabloStockCalcView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StockIn#newInstance} factory method to
 * create an instance of this fragment.
 */
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

    public StockIn() {
        // Required empty public constructor
    }

    public static StockIn newInstance(String param1, String param2) {
        return new StockIn();
    }

    private void initLabel() {
        mButtons = new SparseArray<>();
        // mButtons.put(R.id.sale_out_save, new DiabloButton(getContext(), R.id.sale_out_save));
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
        mStockCalcView.setViewHasPay(view.findViewById(R.id.firm_has_pay));
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
        init();
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

    private void init() {
        Integer firmId = Profile.instance().getFirms().get(0).getId();
        Integer shop = Profile.instance().getLoginShop();

        mStockCalcController = new DiabloStockCalcController(new StockCalc(), mStockCalcView);

        mStockCalcController.setShop(shop);
        mStockCalcController.setDatetime(UTILS.currentDate());

        // listener
        mStockCalcController.setFirm(Profile.instance().getFirm(firmId));
        mStockCalcController.removeFirmWatcher();
        mStockCalcController.setFirmWatcher(getContext(), Profile.instance().getFirms());

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
        StockUtils.switchToStockSelectFrame(stock, operation, DiabloEnum.STOCK_IN, this);
    }

    private void calcShouldPay() {
        mStockTableController.calcStockInShouldPay(mStockCalcController);
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
                // mButtons.get(R.id.sale_out_save).disable();
            }

            calcShouldPay();
        }

        else if (getResources().getString(R.string.modify) == item.getTitle()){
            if (!DiabloEnum.DIABLO_FREE.equals(stock.getFree())){
                // switchToStockSelectFrame(stock, R.string.modify);
            }
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
                            // f.mButtons.get(R.id.sale_out_save).enable();
                        }

                        f.mStockTableController.addRowControllerAtTop(f.addEmptyRow());
                    }
                }

                // recalculate
                f.calcShouldPay();
            }

        }
    }
}
