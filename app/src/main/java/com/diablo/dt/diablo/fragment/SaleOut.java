package com.diablo.dt.diablo.fragment;

import static com.diablo.dt.diablo.R.string.amount;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.controller.DiabloSaleController;
import com.diablo.dt.diablo.controller.DiabloSaleRowController;
import com.diablo.dt.diablo.controller.DiabloSaleTableController;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.SaleCalc;
import com.diablo.dt.diablo.model.SaleStock;
import com.diablo.dt.diablo.model.SaleStockAmount;
import com.diablo.dt.diablo.model.SaleUtils;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.DiabloCellLabel;
import com.diablo.dt.diablo.view.DiabloCellView;
import com.diablo.dt.diablo.view.DiabloRowView;
import com.diablo.dt.diablo.view.DiabloSaleCalcView;

import java.lang.ref.WeakReference;
import java.util.List;

public class SaleOut extends Fragment {
    private final static String LOG_TAG = "SaleOut:";
    private final static DiabloUtils UTILS = DiabloUtils.instance();

    private DiabloCellLabel [] mLabels;
    private String [] mPriceTypes;

    // private SparseArray<DiabloCellLabel> mSparseLabels;

    private DiabloSaleTableController mSaleTableController;

    private DiabloSaleCalcView mSaleCalcView;
    private DiabloSaleController mSaleCalcController;

    private List<MatchStock> mMatchStocks;
    private Integer mLoginShop;
    private Integer mSelectPrice;

    private final SaleOutHandler mHandler = new SaleOutHandler(this);

    // listener
    private StockSelect.OnNoFreeStockSelectListener mNoFreeStockListener;

    private Integer mBackFrom = R.string.back_from_unknown;

    public void setNoFreeStockSelectListener(StockSelect.OnNoFreeStockSelectListener listener){
        mNoFreeStockListener = listener;
    }

    public void setBackFrom(Integer form){
        mBackFrom = form;
    }

    public SaleOut() {
        // Required empty public constructor
    }

    public static SaleOut newInstance(String param1, String param2) {
        SaleOut fragment = new SaleOut();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initLabel() {
        mPriceTypes = getResources().getStringArray(R.array.price_type_on_sale);
        String [] heads = getResources().getStringArray(R.array.thead_sale);
        // mSparseLabels = new SparseArray<>();
        mLabels = new DiabloCellLabel[heads.length];

        DiabloCellLabel label = null;
        for (int i=0; i< heads.length; i++) {
            String h = heads[i];
            if (getResources().getString(R.string.order_id).equals(h)) {
                label = new DiabloCellLabel(
                    h,
                    DiabloEnum.DIABLO_TEXT,
                    R.color.colorPrimaryDark,
                    18,
                    0.8f);
                label.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                label.setLabelId(R.string.order_id);
            }
            else if (getResources().getString(R.string.good).equals(h)) {
                label = new DiabloCellLabel(
                    h,
                    DiabloEnum.DIABLO_AUTOCOMPLETE,
                    R.color.black,
                    18,
                    InputType.TYPE_CLASS_NUMBER,
                    2f);
                label.setLabelId(R.string.good);
            }
            else if (getResources().getString(R.string.stock).equals(h)) {
                label = new DiabloCellLabel(h, R.color.red, 18);
                label.setLabelId(R.string.stock);
            }
            else if (getResources().getString(R.string.price_type).equals(h)) {
                label = new DiabloCellLabel(
                    h, DiabloEnum.DIABLO_SPINNER, R.color.black, 18, 1.5f);
                label.setLabelId(R.string.price_type);
            }
            else if (getResources().getString(R.string.price).equals(h)) {
                label = new DiabloCellLabel(h, R.color.black, 18);
                label.setLabelId(R.string.price);
            }
            else if (getResources().getString(R.string.discount).equals(h)) {
                label = new DiabloCellLabel(h, R.color.black, 18);
                label.setLabelId(R.string.discount);
            }
            else if (getResources().getString(R.string.fprice).equals(h)) {
                label = new DiabloCellLabel(
                    h,
                    DiabloEnum.DIABLO_EDIT,
                    R.color.black,
                    18,
                    Gravity.CENTER_VERTICAL,
                    InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL,
                    false,
                    1f);
                label.setLabelId(R.string.fprice);
            }
            else if (getResources().getString(amount).equals(h)) {
                label = new DiabloCellLabel(
                    h,
                    DiabloEnum.DIABLO_EDIT,
                    R.color.red,
                    18,
                    Gravity.CENTER_VERTICAL,
                    InputType.TYPE_CLASS_NUMBER,
                    false,
                    1f);
                label.setLabelId(amount);
            }
            else if (getResources().getString(R.string.calculate).equals(h)) {
                label = new DiabloCellLabel(h, R.color.black, 18);
                label.setLabelId(R.string.calculate);
            }
            else if (getResources().getString(R.string.comment).equals(h)) {
                label = new DiabloCellLabel(
                    h,
                    DiabloEnum.DIABLO_TEXT,
                    R.color.black,
                    16,
                    InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS,
                    1.5f);
                label.setLabelId(R.string.comment);
            }

            if (null != label ){
                mLabels[i] = label;
                // mSparseLabels.put(label.getLabelId(), label);
            }
        }
    }

    public void initCalc(View view) {
        mSaleCalcView = new DiabloSaleCalcView();

        mSaleCalcView.setViewRetailer(view.findViewById(R.id.sale_out_select_retailer));
        mSaleCalcView.setViewShop(view.findViewById(R.id.sale_out_selected_shop));
        mSaleCalcView.setViewDatetime(view.findViewById(R.id.sale_out_selected_date));
        mSaleCalcView.setViewEmployee(view.findViewById(R.id.sale_out_select_employee));

        mSaleCalcView.setViewSaleTotal(view.findViewById(R.id.sale_out_total));
        mSaleCalcView.setViewBalance(view.findViewById(R.id.sale_out_balance));
        mSaleCalcView.setViewShouldPay(view.findViewById(R.id.sale_out_should_pay));
        mSaleCalcView.setViewExtraCostType(view.findViewById(R.id.sale_out_select_extra_cost));

        mSaleCalcView.setViewComment(view.findViewById(R.id.sale_out_comment));
        mSaleCalcView.setViewAccBalance(view.findViewById(R.id.sale_out_accbalance));
        mSaleCalcView.setViewVerificate(view.findViewById(R.id.sale_out_verificate));
        mSaleCalcView.setViewExtraCost(view.findViewById(R.id.sale_out_extra_cost));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale_out, container, false);

        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        // base setting
        mMatchStocks = Profile.instance().getMatchStocks();



        initLabel();
        ((TableLayout)view.findViewById(R.id.t_sale_out_head)).addView(addHead());

        initCalc(view);

        mSaleTableController = new DiabloSaleTableController ((TableLayout)view.findViewById(R.id.t_sale_out));
        init();
        return view;
    }

    private void init() {
        Integer retailerId = Profile.instance().getLoginRetailer();
        if (retailerId.equals(DiabloEnum.INVALID_INDEX)){
            retailerId = Profile.instance().getRetailers().get(0).getId();
        }

        mSelectPrice = UTILS.toInteger(
            Profile.instance().getConfig(mLoginShop, DiabloEnum.START_PRICE, DiabloEnum.TAG_PRICE));

        mLoginShop = Profile.instance().getLoginShop();

        mSaleCalcController = new DiabloSaleController(new SaleCalc(DiabloEnum.SALE_OUT), mSaleCalcView);
        mSaleCalcController.setRetailer(retailerId);
        mSaleCalcController.setShop(mLoginShop);
        mSaleCalcController.setDatetime(DiabloUtils.getInstance().currentDatetime());

        // listener
        mSaleCalcController.setRetailerWatcher(getContext(), Profile.instance().getRetailers());
        mSaleCalcController.setEmployeeWatcher();
        mSaleCalcController.setCommentWatcher();
        mSaleCalcController.setVerificateWatcher();
        mSaleCalcController.setExtraCostWatcher();
        mSaleCalcController.setExtraCostTypeWatcher();

        // adapter
        mSaleCalcController.setEmployeeAdapter(getContext());
        mSaleCalcController.setExtraCostTypeAdapter(getContext());

        mSaleTableController.addRowControllerAtTop(addEmptyRow());
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

    private DiabloSaleRowController.OnActionAfterSelectGood mOnActionAfterSelectGood =
        new DiabloSaleRowController.OnActionAfterSelectGood(){
            @Override
            public void onActionOfAmount(DiabloSaleRowController controller, DiabloCellView cell) {
                SaleStock stock = controller.getModel();
                DiabloRowView row = controller.getView();
                row.getCell(R.string.fprice).setCellFocusable(true);
                if ( DiabloEnum.DIABLO_FREE.equals(stock.getFree()) ){
                    cell.setCellFocusable(true);
                    cell.requestFocus();
                    row.getCell(R.string.good).setCellFocusable(false);
                } else {
                    // switchToStockSelectFrame(s, R.string.add);
                    switchToStockSelectFrame(controller.getModel(), R.string.add);
                }
            }

            @Override
            public void onActionOfFPrice() {
                calcShouldPay();
            }
        };

    private DiabloSaleRowController addEmptyRow() {
        TableRow row = new TableRow(getContext());
        DiabloSaleRowController controller = new DiabloSaleRowController(
            new DiabloRowView(row),
            new SaleStock());

        for (DiabloCellLabel label: mLabels) {
            DiabloCellView cell = new DiabloCellView(label.createCell(getContext()), label);
            controller.addCell(cell);
        }

        controller.setGoodWatcher(getContext(), mMatchStocks, mSelectPrice, mLabels, mOnActionAfterSelectGood);
        controller.setFinalPriceWatcher(mOnActionAfterSelectGood);
        controller.setAmountWatcher(mHandler, controller);
        controller.setPriceTypeAdapter(getContext(), mPriceTypes);

        return controller;
    }

    private void switchToStockSelectFrame(SaleStock stock, Integer action) {
        Integer shop = mSaleCalcController.getShop();
        Integer retailer = mSaleCalcController.getRetailer();
        SaleUtils.switchToStockSelectFrame(stock, action, DiabloEnum.SALE_OUT, retailer, shop, this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private static class SaleOutHandler extends Handler {
        WeakReference<Fragment> mFragment;

        SaleOutHandler(Fragment fragment){
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

                final SaleOut f = ((SaleOut)mFragment.get());

                if (DiabloEnum.STARTING_SALE.equals(stock.getState())) {
                    if (0 != stock.getSaleTotal()) {
                        stock.setState(DiabloEnum.FINISHED_SALE);
                        Integer orderId = f.mSaleTableController.getCurrentRows();
                        stock.setOrderId(orderId);
                        row.setCellText(R.string.order_id, orderId);
                        row.setOnLongClickListener(f);

                        f.mSaleTableController.addRowControllerAtTop(f.addEmptyRow());
                    }
                }

                //recalculate
                f.calcShouldPay();
            }

        }
    }

    private void calcShouldPay() {
        // calculate stock
        Integer total     = 0;
        Float   shouldPay = 0f;

        for (int i=1; i<mSaleTableController.getControllers().size(); i++ ){
            DiabloSaleRowController controller = mSaleTableController.getControllers().get(i);
            total += controller.getSaleTotal();
            shouldPay += controller.getSalePrice();
        }

        mSaleCalcController.setSaleInfo(total);
        mSaleCalcController.setShouldPay(shouldPay);
        mSaleCalcController.resetAccBalance();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // utils.makeToast(getContext(), hidden ? "hidden" : "show");
        if (!hidden){
            if (mBackFrom.equals(R.string.back_from_stock_select)){
                SaleStock s = mNoFreeStockListener.afterSelectStock();
                List<SaleStockAmount> amounts = s.getAmounts();

                switch (mNoFreeStockListener.getCurrentOperation()){
                    case R.string.action_save:
                        List<DiabloSaleRowController> controllers = mSaleTableController.getControllers();
                        for (DiabloSaleRowController c: controllers) {
                            if (c.getOrderId().equals(s.getOrderId())) {
                                c.clearAmount();
                                Integer saleTotal = 0;
                                Integer exist = 0;

                                for (SaleStockAmount a: amounts) {
                                    c.addAmount(a);
                                    exist += a.getStock();
                                    if (a.getSellCount() != 0){
                                        saleTotal += a.getSellCount();
                                    }
                                }

                                c.setColors(s.getColors());
                                c.setOrderSizes(s.getOrderSizes());
                                c.setSaleTotal(saleTotal);
                                c.setStockExist(exist);

                                c.getView().setCellText(R.string.amount, saleTotal);
                                c.getView().setCellText(R.string.stock, exist);
                            }
                        }
                        break;
                    case R.string.action_cancel:
                        if (s.getOrderId().equals(0)){
                            mSaleTableController.removeRowAtTop();
                            mSaleTableController.addRowControllerAtTop(addEmptyRow());
                        }
                        break;
                    default:
                        break;
                }

                mBackFrom = R.string.back_from_unknown;
            }
//            else {
//                View cell = ((TableRow) mSaleTable.getChildAt(0)).getChildAt(1);
//                cell.requestFocus();
//                utils.openKeyboard(getContext(), cell);
//            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // setCurrentSelectRow((TableRow) v);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_on_sale, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        return true;
    }

}
