package com.diablo.dt.diablo.fragment.sale;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
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
import com.diablo.dt.diablo.request.sale.NewSaleRequest;
import com.diablo.dt.diablo.response.sale.NewSaleResponse;
import com.diablo.dt.diablo.rest.WSaleInterface;
import com.diablo.dt.diablo.utils.DiabloAlertDialog;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.DiabloCellLabel;
import com.diablo.dt.diablo.view.DiabloCellView;
import com.diablo.dt.diablo.view.DiabloRowView;
import com.diablo.dt.diablo.view.sale.DiabloSaleCalcView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleOut extends Fragment {
    private final static String LOG_TAG = "SaleOut:";
    private final static DiabloUtils UTILS = DiabloUtils.instance();

    private DiabloCellLabel [] mLabels;
    private String [] mPriceTypes;
    private SparseArray<DiabloButton> mButtons;

    // private SparseArray<DiabloCellLabel> mSparseLabels;

    private DiabloSaleTableController mSaleTableController;

    private DiabloSaleCalcView mSaleCalcView;
    private DiabloSaleController mSaleCalcController;

    private List<MatchStock> mMatchStocks;
    private Integer  mLoginShop;
    private Integer  mSelectPrice;
    private TableRow mCurrentSelectedRow;

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
        mButtons = new SparseArray<>();
        mButtons.put(R.id.sale_out_save, new DiabloButton(getContext(), R.id.sale_out_save));
        mLabels = SaleUtils.createSaleLabelsFromTitle(getContext());
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

        mBackFrom = R.string.back_from_unknown;

        mLoginShop = Profile.instance().getLoginShop();
        mSelectPrice = UTILS.toInteger(
            Profile.instance().getConfig(mLoginShop, DiabloEnum.START_PRICE, DiabloEnum.TAG_PRICE));


        Retailer.getRetailer(getContext(), retailerId, mOnRetailerChangeListener);

        mSaleCalcController = new DiabloSaleController(new SaleCalc(DiabloEnum.SALE_OUT), mSaleCalcView);
        // mSaleCalcController.setRetailer(retailerId);
        mSaleCalcController.setShop(mLoginShop);
        mSaleCalcController.setDatetime(DiabloUtils.getInstance().currentDatetime());

        // listener
        // mSaleCalcController.setRetailerWatcher(getContext());
        mSaleCalcController.setEmployeeWatcher();
        mSaleCalcController.setCommentWatcher();
        mSaleCalcController.setVerificateWatcher();
        mSaleCalcController.setExtraCostWatcher();
        mSaleCalcController.setExtraCostTypeWatcher();

        // adapter
        mSaleCalcController.setEmployeeAdapter(getContext());
        mSaleCalcController.setExtraCostTypeAdapter(getContext());

        mSaleTableController.clear();

        mSaleTableController.addRowControllerAtTop(addEmptyRow());

        mButtons.get(R.id.sale_out_save).disable();
    }

    private Retailer.OnRetailerChangeListener mOnRetailerChangeListener = new Retailer.OnRetailerChangeListener() {
        @Override
        public void afterAdd(Retailer retailer) {

        }

        @Override
        public void afterGet(Retailer retailer) {
            mSaleCalcController.setRetailer(retailer);
            mSaleCalcController.removeRetailerWatcher();
            mSaleCalcController.setRetailerWatcher(getContext());
        }
    };

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
                        cell.setCellFocusable(true);
                        cell.requestFocus();
                        row.getCell(R.string.good).setCellFocusable(false);
                    } else {
                        // switchToStockSelectFrame(s, R.string.
                        // add);
                        switchToStockSelectFrame(controller.getModel(), R.string.add);
                    }
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

        controller.setFinalPriceWatcher(mOnActionAfterSelectGood);
        controller.setAmountWatcher(mHandler, controller);

        controller.setGoodWatcher(getContext(), mMatchStocks, mSelectPrice, mLabels, mOnActionAfterSelectGood);
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
                        controller.setOrderId(orderId);
//                        stock.setOrderId(orderId);
//                        row.setCellText(R.string.order_id, orderId);
                        row.setOnLongClickListener(f);

                        if (1 == f.mSaleTableController.size()){
                            f.mButtons.get(R.id.sale_out_save).enable();
                        }

                        f.mSaleTableController.addRowControllerAtTop(f.addEmptyRow());
                    }
                }

                //recalculate
                f.calcShouldPay();
            }

        }
    }

    private void calcShouldPay() {
        mSaleTableController.calcSaleOutShouldPay(mSaleCalcController);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // utils.makeToast(getContext(), hidden ? "hidden" : "show");
        if (!hidden){
            if (mBackFrom.equals(R.string.back_from_stock_select)){
                SaleStock s = mNoFreeStockListener.afterSelectStock();

                switch (mNoFreeStockListener.getCurrentOperation()){
                    case R.string.action_save:
                        mSaleTableController.replaceRowController(s);
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

            if (1 == mSaleTableController.size()){
                mButtons.get(R.id.sale_out_save).disable();
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

        inflater.inflate(R.menu.action_on_sale_out, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sale_out_back:
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_SALE_DETAIL);
                break;
            case R.id.sale_out_back_to_sale_in:
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_SALE_IN);
                break;
            case R.id.sale_out_save:
                startSale();
                break;
            default:
                // return super.onOptionsItemSelected(item);
                break;

        }

        return true;
    }

    private void startSale() {
        NewSaleRequest saleRequest = new NewSaleRequest();
        for (DiabloSaleRowController controller: mSaleTableController.getControllers()) {
            if (0 == controller.getOrderId()) {
                continue;
            }

            SaleStock s = controller.getModel();
            NewSaleRequest.DiabloSaleStock d = new NewSaleRequest.DiabloSaleStock();
            d.setOrderId(s.getOrderId());
            d.setStyleNumber(s.getStyleNumber());
            d.setBrandId(s.getBrandId());
            d.setBrand(s.getBrand());

            d.setSizeGroup(s.getSizeGroup());
            if ( null != s.getColors() && 0 != s.getColors().size()) {
                List<NewSaleRequest.DiabloSaleColor> saleColors = new ArrayList<>();
                for (DiabloColor c: s.getColors()) {
                    NewSaleRequest.DiabloSaleColor saleColor = new NewSaleRequest.DiabloSaleColor();
                    saleColor.setColorId(c.getColorId());
                    saleColor.setColorName(c.getName());
                    saleColors.add(saleColor);
                }
                d.setColors(saleColors);
            }
            d.setFree(s.getFree());


            d.setTypeId(s.getTypeId());
            d.setType(s.getType());

            d.setSex(s.getSex());
            d.setSeason(s.getSeason());
            d.setFirmId(s.getFirmId());
            d.setYear(s.getYear());

            d.setOrgPrice(s.getOrgPrice());
            d.setTagPrice(s.getTagPrice());
            d.setPkgPrice(s.getPkgPrice());
            d.setPrice3(s.getPrice3());
            d.setPrice4(s.getPrice4());
            d.setPrice5(s.getPrice5());
            d.setDiscount(s.getDiscount());

            d.setSaleTotal(s.getSaleTotal());
            d.setFdiscount(s.getDiscount());
            d.setFprice(s.getFinalPrice());
            d.setPath(s.getPath());
            d.setComment(s.getComment());

//            d.setSecond(s.getSecond() );
//
//            if ( null != s.getOrderSizes() && 0 != s.getOrderSizes().size()) {
//                d.setOrderSizes(new ArrayList<>(s.getOrderSizes()));
//            }

            // d.setSellTye(s.getSelectedPrice());

            for (SaleStockAmount a: s.getAmounts()) {
                if ( a.getSellCount() != 0 ){
                    NewSaleRequest.DiabloSaleStockAmount saleAmount = new NewSaleRequest.DiabloSaleStockAmount();
                    saleAmount.setColorId(a.getColorId());
                    saleAmount.setSize(a.getSize());
                    saleAmount.setRejectCount(a.getSellCount());
                    saleAmount.setDirect(DiabloEnum.SALE_OUT);
                    d.addAmount(saleAmount);
                }
            }

            saleRequest.addStock(d);
        }

        NewSaleRequest.DiabloSaleCalc dCalc = new NewSaleRequest.DiabloSaleCalc();
        SaleCalc calc = mSaleCalcController.getSaleCalc();
        dCalc.setRetailer(calc.getRetailer());
        dCalc.setShop(calc.getShop());
        dCalc.setDatetime(calc.getDatetime());
        dCalc.setEmployee(calc.getEmployee());
        dCalc.setComment(calc.getComment());

        dCalc.setBalance(calc.getBalance());
        dCalc.setShouldPay(calc.getShouldPay());

        dCalc.setDirect(DiabloEnum.SALE_OUT);

        dCalc.setTotal(calc.getTotal());

        dCalc.setExtraCostType(calc.getExtraCostType());
        dCalc.setExtraCost(calc.getExtraCost());

        saleRequest.setSaleCalc(dCalc);

        //print info
        NewSaleRequest.DiabloPrintAttr printAttr = new NewSaleRequest.DiabloPrintAttr();
        printAttr.setImmediatelyPrint(0);
        printAttr.setRetailerId(calc.getRetailer());
//        printAttr.setRetailerName(mSaleCalcController.getSelectRetailerName());
        printAttr.setShop(mSaleCalcController.getShopName());
        printAttr.setEmployee(calc.getEmployee());

        saleRequest.setPrintAttr(printAttr);

        startRequest(saleRequest);
    }

    private void startRequest(NewSaleRequest request) {
        final WSaleInterface face = WSaleClient.getClient().create(WSaleInterface.class);
        Call<NewSaleResponse> call = face.startReject(Profile.instance().getToken(), request);

        call.enqueue(new Callback<NewSaleResponse>() {
            @Override
            public void onResponse(Call<NewSaleResponse> call, Response<NewSaleResponse> response) {
                // mButtons.get(R.id.sale_out_save).enable();

                final NewSaleResponse res = response.body();
                if ( DiabloEnum.HTTP_OK == response.code() && res.getCode().equals(DiabloEnum.SUCCESS)) {
                    // refresh balance
                    SaleCalc calc = mSaleCalcController.getSaleCalc();
                    Profile.instance().getRetailerById(calc.getRetailer()).setBalance(calc.getAccBalance());
                    init();

                    new DiabloAlertDialog(
                        getContext(),
                        true,
                        getResources().getString(R.string.nav_sale_out),
                        getContext().getString(R.string.sale_success)
                            + res.getRsn()
                            + getContext().getString(R.string.sale_start_print_or_not) ,
                        new DiabloAlertDialog.OnOkClickListener() {
                            @Override
                            public void onOk() {
                                UTILS.startPrint(getContext(), R.string.nav_sale_out, res.getRsn());
                            }
                        }).create();
                } else {
                    mButtons.get(R.id.sale_out_save).enable();
                    Integer errorCode = response.code() == 0 ? res.getCode() : response.code();
                    String extraMessage = res == null ? "" : res.getError();
                    new DiabloAlertDialog(
                        getContext(),
                        getResources().getString(R.string.nav_sale_out),
                        DiabloError.getError(errorCode) + extraMessage).create();
                }
            }

            @Override
            public void onFailure(Call<NewSaleResponse> call, Throwable t) {
                mButtons.get(R.id.sale_out_save).enable();
                new DiabloAlertDialog(
                    getContext(),
                    getResources().getString(R.string.nav_sale_out),
                    DiabloError.getError(99)).create();
            }
        });
    }

}
