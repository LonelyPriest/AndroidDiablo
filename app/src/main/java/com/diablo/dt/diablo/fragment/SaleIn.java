package com.diablo.dt.diablo.fragment;

import static com.diablo.dt.diablo.R.string.amount;

import com.google.gson.Gson;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.activity.MainActivity;
import com.diablo.dt.diablo.adapter.SpinnerStringAdapter;
import com.diablo.dt.diablo.controller.DiabloSaleCalcController;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.DiabloButton;
import com.diablo.dt.diablo.model.SaleCalc;
import com.diablo.dt.diablo.model.SaleStock;
import com.diablo.dt.diablo.model.SaleStockAmount;
import com.diablo.dt.diablo.task.MatchStockTask;
import com.diablo.dt.diablo.task.TextChangeOnAutoComplete;
import com.diablo.dt.diablo.utils.DiabloDBManager;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloSaleAmountChangeWatcher;
import com.diablo.dt.diablo.utils.DiabloSaleRow;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.DiabloSaleCalcView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SaleIn extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private OnStockSelectListener mStockSelectListener;

    private Integer mComesFrom;

    // private final static String LOG_TAG = "SALE_IN:";
    private DiabloUtils utils = DiabloUtils.instance();
    private DiabloDBManager dbInstance;

    private String [] mTitles;
    private String [] mPriceType;

    private TableLayout mSaleTable;
    private TableRow mCurrentSelectRow;

    private Integer mLoginShop;
    private Integer mStartRetailer;
    private Integer mSelectPrice;

    private List<MatchStock> mMatchStocks;
    private List<SaleStock> mSaleStocks;

    // private Retailer mSelectRetailer;
    private DiabloSaleCalcView mSaleCalcView;
    private DiabloSaleCalcController mSaleCalcController;

    private SparseArray<DiabloButton> mButtons;

    private Integer mRowSize;

    public SaleIn() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SaleIn.
     */
    // TODO: Rename and change types and number of parameters
    public static SaleIn newInstance(String param1, String param2) {
        SaleIn fragment = new SaleIn();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void incRow (){
        mRowSize++;
    }

    public void decRow (){
        mRowSize--;
    }

    public void setComesForm(Integer form){
        mComesFrom = form;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getArguments().get("1");
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mTitles = getResources().getStringArray(R.array.thead_sale);
        mPriceType = getResources().getStringArray(R.array.price_type_on_sale);

        mButtons= new SparseArray<>();
        mButtons.put(R.id.sale_in_back, new DiabloButton(R.id.sale_in_back));
        mButtons.put(R.id.sale_in_money_off, new DiabloButton(R.id.sale_in_money_off));
        mButtons.put(R.id.sale_in_draft, new DiabloButton(R.id.sale_in_draft));
        mButtons.put(R.id.sale_in_save, new DiabloButton(R.id.sale_in_save));
        mButtons.put(R.id.sale_in_next, new DiabloButton(R.id.sale_in_next));
        mButtons.put(R.id.sale_in_clear_draft, new DiabloButton(R.id.sale_in_clear_draft));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // option menu
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale_in, container, false);
        ((MainActivity)getActivity()).selectMenuItem(0);


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

        mSaleTable = (TableLayout)view.findViewById(R.id.t_sale);
        ((TableLayout)view.findViewById(R.id.t_sale_head)).addView(addHead());

        mMatchStocks = Profile.instance().getMatchStocks();
        init();

        return view;
    }

    public void init(Integer retailer, Integer shop, SaleCalc calc, List<SaleStock> stocks) {
        if (null != mSaleTable) {
            mSaleTable.removeAllViews();
        }

        mSelectPrice = utils.toInteger(
            Profile.instance().getConfig(mLoginShop, DiabloEnum.START_PRICE, DiabloEnum.TAG_PRICE));

        mStartRetailer = retailer;
        mLoginShop = Profile.instance().getLoginShop();

        dbInstance = DiabloDBManager.instance();
        mRowSize = 0;
        mComesFrom = R.integer.COMES_FORM_SELF;
        mSaleStocks = stocks;


        mSaleCalcController = new DiabloSaleCalcController(calc, mSaleCalcView);

        mSaleCalcController.setRetailer(retailer);
        mSaleCalcController.setShop(shop);
        mSaleCalcController.setDatetime(DiabloUtils.getInstance().currentDatetime());

        mSaleCalcController.setDiabloOnRetailerSelected(new DiabloSaleCalcController.OnDiabloRetailerSelectedListener() {
            @Override
            public void onRetailerSelected(SaleCalc c) {
                checkRetailerDraft(c);
            }
        });

        // listener
        mSaleCalcController.setRetailerWatcher(getContext(), Profile.instance().getRetailers());
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
    }

    public void init() {
        Integer retailerId = Profile.instance().getLoginRetailer();
        if (retailerId.equals(DiabloEnum.INVALID_INDEX)){
            retailerId = Profile.instance().getRetailers().get(0).getId();
        }

        mStartRetailer = retailerId;
        mLoginShop = Profile.instance().getLoginShop();

        init(retailerId, mLoginShop, new SaleCalc(), new ArrayList<SaleStock>());

        checkRetailerDraft(mSaleCalcController.getSaleCalc());

        mSaleTable.addView(addEmptyRow());

        mButtons.get(R.id.sale_in_next).disable();
        getActivity().invalidateOptionsMenu();
    }

    private void enableBtnNextSale() {
        mButtons.get(R.id.sale_in_next).enable();
        getActivity().invalidateOptionsMenu();
    }

    private void checkRetailerDraft(SaleCalc calc) {
        if (null != dbInstance.querySaleCalc(calc)){
            utils.makeToast(getContext(), getContext().getResources().getString(R.string.draft_exist));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
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

    private final SaleStockHandler mHandler = new SaleStockHandler(this);

    private TableRow addHead(){
        TableRow row = new TableRow(this.getContext());
        for (String title: mTitles){
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            TextView cell = new TextView(this.getContext());
            // font
            cell.setTypeface(null, Typeface.BOLD);
            cell.setTextColor(Color.BLACK);
            // right-margin

            if (getResources().getString(R.string.good).equals(title)){
                lp.weight = 2f;
            } else if (getResources().getString(R.string.order_id).equals(title)){
                lp.weight = 0.8f;
            } else if (getResources().getString(R.string.price_type).equals(title)){
                lp.weight = 1.5f;
            } else if (getResources().getString(R.string.comment).equals(title)){
                lp.weight = 1.5f;
            }
            cell.setLayoutParams(lp);
            cell.setText(title);
            cell.setTextSize(18);

            row.addView(cell);
        }

        return row;
    };

    private TableRow genRowWithStock(final SaleStock stock){
        final DiabloSaleRow diabloRow = new DiabloSaleRow(getContext(), mTitles, stock);
        diabloRow.genRow(stock.getFree());

        for (String title: mTitles){
            if (getResources().getString(R.string.order_id).equals(title)){
                TextView cell = (TextView)diabloRow.getCell(title);
                utils.setTextViewValue(cell, stock.getOrderId());
            }
            else if (getResources().getString(R.string.good).equals(title)){
                TextView cell = (TextView)diabloRow.getCell(title);
                cell.setText(stock.getName());
            }
            else if (getResources().getString(R.string.stock).equals(title)){
                ((TextView)diabloRow.getCell(title)).setText(utils.toString(stock.getExistStock()));
            }
            else if (getResources().getString(R.string.price_type).equals(title)) {
                Spinner cell = (Spinner)diabloRow.getCell(title);
                SpinnerStringAdapter adapter=new SpinnerStringAdapter(
                        this.getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        mPriceType);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cell.setAdapter(adapter);
                cell.setSelection(stock.getSelectedPrice() - 1);
            }
            else if (getResources().getString(R.string.price).equals(title)){
                TextView cell = (TextView)diabloRow.getCell(title);
                utils.setTextViewValue(cell, stock.getValidPrice());
                // utils.setTextViewValue(cell, stock.getTagPrice());
            }
            else if (getResources().getString(R.string.discount).equals(title)){
                TextView cell = (TextView)diabloRow.getCell(title);
                utils.setTextViewValue(cell, stock.getDiscount());
            }
            else if(getResources().getString(R.string.fprice).equals(title)){
                EditText cell = (EditText) diabloRow.getCell(title);
                utils.setEditTextValue(cell, stock.getFinalPrice());
                utils.addTextChangedListenerOfPayment(cell, new DiabloUtils.Payment() {
                    @Override
                    public void setPayment(String param) {
                        Float price = utils.toFloat(param);
                        diabloRow.getStock().setFinalPrice(price);
                        View calCell = diabloRow.getCell(R.string.calculate);
                        if (null != calCell){
                            utils.setTextViewValue((TextView) calCell, diabloRow.getStock().getSalePrice());
                        }
                    }
                });

            }
            else if (getResources().getString(R.string.amount).equals(title)){
                EditText cell = (EditText) diabloRow.getCell(title);
                utils.setEditTextValue(cell, stock.getSaleTotal());
                cell.addTextChangedListener(new DiabloSaleAmountChangeWatcher(mHandler, diabloRow.getRow()));
            }
            else if (getResources().getString(R.string.calculate).equals(title)){
                TextView cell = (TextView)diabloRow.getCell(title);
                cell.setText(utils.toString(stock.getSalePrice()));
            }
            else if (getResources().getString(R.string.comment).equals(title)){
                EditText cell = (EditText) diabloRow.getCell(title);
                cell.setText(stock.getComment());

                utils.addTextChangedListenerOfPayment(cell, new DiabloUtils.Payment() {
                    @Override
                    public void setPayment(String param) {
                        if (!param.isEmpty())
                            diabloRow.getStock().setComment(param);
                    }
                });
            }
        }
        return diabloRow.getRow();
    };

    private TableRow addEmptyRow(){
        final TableRow row = new TableRow(this.getContext());
        for (String title: mTitles){
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            if (getResources().getString(R.string.good).equals(title)){
                final AutoCompleteTextView eCell = new AutoCompleteTextView(this.getContext());
                eCell.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                // eCell.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                eCell.requestFocus();
                lp.weight = 2f;
                eCell.setLayoutParams(lp);
                eCell.setHint(R.string.please_input_good);
                eCell.setHintTextColor(Color.GRAY);
                eCell.setTextSize(18);
                eCell.setTextColor(Color.BLACK);
                eCell.setDropDownWidth(500);
                eCell.setThreshold(1);
//              eCell.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

                new TextChangeOnAutoComplete(eCell).addListen(new TextChangeOnAutoComplete.TextWatch() {
                    @Override
                    public void afterTextChanged(String value) {
                        if (value.length() > 0) {
                            new MatchStockTask(getContext(), eCell, row, mMatchStocks).execute(value);
                        }
                    }
                });

                eCell.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long extra) {
                        MatchStock matchedStock = (MatchStock) adapterView.getItemAtPosition(position);
                        Integer matchedOrderId = findSaleStock(matchedStock);
                        if (!matchedOrderId.equals(DiabloEnum.INVALID_INDEX)){
                            eCell.setText(DiabloEnum.EMPTY_STRING);
                            utils.makeToast(
                                    getContext(),
                                    getContext().getResources().getString(R.string.sale_stock_exist)
                                            + utils.toString(matchedOrderId));
                        } else {
                            final SaleStock s = new SaleStock(matchedStock, mSelectPrice);
                            row.setTag(s);
                            mSaleStocks.add(s);

                            for (Integer i=0; i<row.getChildCount(); i++){
                                final View cell = row.getChildAt(i);
                                String name = (String)cell.getTag();

                                if (getResources().getString(R.string.good).equals(name)) {
                                    cell.setFocusable(false);
                                    ((AutoCompleteTextView )cell).setText(s.getName());
                                }

                                if (getResources().getString(R.string.price).equals(name)) {
                                    DiabloUtils.instance().setTextViewValue((TextView) cell, s.getValidPrice());
                                }
                                else if (getResources().getString(R.string.discount).equals(name)){
                                    DiabloUtils.instance().setTextViewValue((TextView) cell, s.getDiscount());
                                }
                                else if (getResources().getString(R.string.fprice).equals(name)){
                                    cell.setFocusableInTouchMode(true);
                                    cell.setFocusable(true);
                                    DiabloUtils.instance().setEditTextValue((EditText)cell, s.getFinalPrice());
                                    utils.addTextChangedListenerOfPayment((EditText) cell, new DiabloUtils.Payment() {
                                        @Override
                                        public void setPayment(String param) {
                                            SaleStock s = (SaleStock) row.getTag();
                                            Float price = utils.toFloat(param);
                                            s.setFinalPrice(price);
                                            View calCell = SaleStockHandler.getColumn(getContext(), row, R.string.calculate);
                                            utils.setTextViewValue((TextView) calCell, s.getSalePrice());
                                        }
                                    });
                                }

                                if (getResources().getString(amount).equals(name)){
                                    // free color, free size
                                    if ( DiabloEnum.DIABLO_FREE.equals(s.getFree()) ){
                                        cell.setFocusableInTouchMode(true);
                                        cell.setFocusable(true);
                                        cell.requestFocus();
                                    } else {
                                        switchToStockSelectFrame(s, R.string.add);
                                    }

                                    ((EditText)cell).addTextChangedListener(new DiabloSaleAmountChangeWatcher(mHandler, row));

                                }

                            }
                        }
                    }
                });
                eCell.setTag(title);
                row.addView(eCell);
            } else if (getResources().getString(R.string.price_type).equals(title)) {
                Spinner sCell = new Spinner(this.getContext());
                lp.weight = 1.5f;
                sCell.setLayoutParams(lp);
                SpinnerStringAdapter adapter=new SpinnerStringAdapter(
                        this.getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        mPriceType);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sCell.setAdapter(adapter);
                sCell.setSelection(mSelectPrice - 1);
                sCell.setTag(title);
                row.addView(sCell);
            }
            else if (getResources().getString(amount).equals(title)){
                final EditText cell = new EditText(this.getContext());
                cell.setTextColor(Color.BLACK);
                cell.setLayoutParams(lp);
                cell.setGravity(Gravity.CENTER_VERTICAL);
                cell.setTextColor(Color.RED);
                cell.setTextSize(18);
                cell.setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_SIGNED);
                cell.setTag(title);
                cell.setFocusable(false);
                row.addView(cell);
            } else if (getResources().getString(R.string.order_id).equals(title)){
                TextView cell = new TextView(this.getContext());
                lp.weight = 0.8f;
                cell.setLayoutParams(lp);
                cell.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                cell.setTextSize(18);
                cell.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                cell.setTag(title);
                row.addView(cell);
            } else if(getResources().getString(R.string.fprice).equals(title)){
                final EditText cell = new EditText(this.getContext());
                cell.setTextColor(Color.BLACK);
                cell.setLayoutParams(lp);
                cell.setGravity(Gravity.CENTER_VERTICAL);
                cell.setTextSize(18);
                cell.setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
                cell.setTag(title);
                cell.setFocusable(false);
                row.addView(cell);
            }
            else if (getResources().getString(R.string.comment).equals(title)){
                final EditText cell = new EditText(this.getContext());
                // font
                cell.setTextColor(Color.BLACK);
                lp.weight = 1.5f;
                cell.setLayoutParams(lp);
                cell.setTextSize(16);
                cell.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

                utils.addTextChangedListenerOfPayment(cell, new DiabloUtils.Payment() {
                    @Override
                    public void setPayment(String param) {
                        if (!param.isEmpty())
                            ((SaleStock)row.getTag()).setComment(param);
                    }
                });

                cell.setTag(title);
                row.addView(cell);
            }

            else {
                TextView cell = new TextView(this.getContext());
                // font
                cell.setTextColor(Color.BLACK);
                // right-margin
                cell.setLayoutParams(lp);
                // cell.setText(title);
                cell.setGravity(Gravity.CENTER_VERTICAL);
                cell.setTextSize(18);
                cell.setTag(title);
                row.addView(cell);
            }
        }

        return row;
    }

    private void switchToStockSelectFrame(SaleStock stock, Integer action) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // find
        StockSelect to = (StockSelect)getFragmentManager().findFragmentByTag(DiabloEnum.TAG_STOCK_SELECT);
        Integer shop = mSaleCalcController.getShop();
        Integer retailer = mSaleCalcController.getRetailer();
        if (null == to){
            Bundle args = new Bundle();
            args.putInt(DiabloEnum.BUNDLE_PARAM_SHOP, shop);
            args.putInt(DiabloEnum.BUNDLE_PARAM_RETAILER, retailer);
            args.putInt(DiabloEnum.BUNDLE_PARAM_ACTION, action);
            args.putString(DiabloEnum.BUNDLE_PARAM_SALE_STOCK, new Gson().toJson(stock));
            to = new StockSelect();
            to.setArguments(args);
        } else {
            to.setSelectShop(shop);
            to.setSelectRetailer(retailer);
            to.setSelectAction(action);
            to.setSaleStock(new Gson().toJson(stock));
        }

        if (!to.isAdded()){
            transaction.hide(SaleIn.this).add(R.id.frame_container, to, DiabloEnum.TAG_STOCK_SELECT).commit();
        } else {
            transaction.hide(SaleIn.this).show(to).commit();
        }
    }

    public static class SaleStockHandler extends Handler{
        public final static Integer SALE_TOTAL_CHANGED = 1;
        public final static Integer SALE_PRICE_TYPE_SELECTED = 2;
        public final static Integer SALE_GOOD_SELECTED = 3;

        private final DiabloUtils utils = DiabloUtils.getInstance();

        WeakReference<Fragment> mFragment;

        SaleStockHandler(Fragment fragment){
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SALE_TOTAL_CHANGED){
                TableRow row = (TableRow) msg.obj;
                View cell = getColumn(mFragment.get().getContext(), row, R.string.calculate);
                if (null != cell){
                    SaleStock s = (SaleStock)row.getTag();
                    s.setSaleTotal(msg.arg1);

                    if (DiabloEnum.DIABLO_FREE.equals(s.getFree())){
                        SaleStockAmount amount = new SaleStockAmount(
                                DiabloEnum.DIABLO_FREE_COLOR,
                                DiabloEnum.DIABLO_FREE_SIZE);
                        amount.setSellCount(msg.arg1);
                        s.clearAmounts();
                        s.addAmount(amount);
                    }

                    final SaleIn f = ((SaleIn)mFragment.get());
                    if (DiabloEnum.STARTING_SALE.equals(s.getState())){
                        if (0 == s.getSaleTotal()){
                            return;
                        } else {
                            s.setState(DiabloEnum.FINISHED_SALE);

                            View orderCell =  getColumn(mFragment.get().getContext(), row, R.string.order_id);
                            if (null != orderCell){
                                Integer rowId =  f.getValidRowId();
                                s.setOrderId(rowId);
                                ((TextView)orderCell).setText(utils.toString(rowId));
                            }

                            row.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    view.showContextMenu();
                                    return true;
                                }
                            });

                            f.incRow();
                            f.registerForContextMenu(row);
                            f.addEmptyRowToTable();
                        }
                    }

                    utils.setTextViewValue((TextView)cell, s.getSalePrice());
                    f.calcShouldPay();

                    // save to db
                    // delete old
                    SaleCalc calc = f.mSaleCalcController.getSaleCalc();
                    if (f.mRowSize == 1){
                        f.dbInstance.deleteSaleCalc(calc);
                        f.dbInstance.deleteAllSaleStock(calc);
                        f.dbInstance.addSaleCalc(calc);
                    }
                    f.dbInstance.replaceSaleStock(calc, s, f.mStartRetailer);
                }
            }
        }

        private static View getColumn(Context context, TableRow row, Integer stringResource){
            View cell = null;
            for (int i=0; i<row.getChildCount(); i++){
                cell = row.getChildAt(i);
                String cellName = (String)cell.getTag();
                if (context.getResources().getString(stringResource).equals(cellName)){
                    break;
                }
            }

            return cell;
        }
    }

    public void addEmptyRowToTable(){
        mSaleTable.addView(addEmptyRow(), 0);
        if (mRowSize == 1){
            enableBtnNextSale();
        }
    }

    public Integer getValidRowId(){
        Integer rows = 0;
        for (int i=0; i<mSaleTable.getChildCount(); i++){
            View row = mSaleTable.getChildAt(i);
            if (row instanceof TableRow) {
                rows++;
            }
        }
        return rows;
    }

    public void resetWith(SaleCalc calc, List<SaleStock> stocks){
        init(calc.getRetailer(), calc.getShop(), calc, stocks);

        // restore table
        for(SaleStock s: stocks){
            TableRow row = genRowWithStock(s);
            mSaleTable.addView(row, 0);
            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    view.showContextMenu();
                    return true;
                }
            });
            registerForContextMenu(row);

            incRow();
        }

        if (mRowSize > 0){
            enableBtnNextSale();
        }

        addEmptyRowToTable();
        // mSaleTable.addView(addEmptyRow(), 0);

        calcShouldPay();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        setCurrentSelectRow((TableRow) v);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_on_sale, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        SaleStock stock = (SaleStock)mCurrentSelectRow.getTag();
        Integer orderId = stock.getOrderId();

        if (getResources().getString(R.string.delete) == item.getTitle()){
            mCurrentSelectRow.removeAllViews();
            for (int i=1; i<mSaleTable.getChildCount(); i++){
                View row = mSaleTable.getChildAt(i);
                if (row instanceof TableRow) {
                    if (((SaleStock)row.getTag()).getOrderId().equals(orderId)){
                        mSaleTable.removeView(row);
                        break;
                    }
                }
            }

            // delete from lists
            int index = DiabloEnum.INVALID_INDEX;
            for (int i=0; i<mSaleStocks.size(); i++){
                if (mSaleStocks.get(i).getOrderId().equals(orderId)){
                    index = i;
                    break;
                }
            }

            mSaleStocks.remove(index);
            dbInstance.deleteSaleStock(mSaleCalcController.getSaleCalc(), stock);

            decRow();

            // reorder
            Integer newOrderId = mSaleTable.getChildCount() - 1;
            for (int i=1; i<mSaleTable.getChildCount(); i++){
                View row = mSaleTable.getChildAt(i);
                if (row instanceof TableRow) {
                    ((SaleStock)row.getTag()).setOrderId(newOrderId);
                    ((TextView)((TableRow) row).getChildAt(0)).setText(DiabloUtils.instance().toString(newOrderId));
                    newOrderId--;
                }
            }

            if (mRowSize == 0){
                // mButtons.get(R.id.sale_in_save).disable();
                // mButtons.get(R.id.sale_in_money_off).disable();
                mButtons.get(R.id.sale_in_next).disable();
                // mButtons.get(R.id.sale_in_draft).disable();
                getActivity().invalidateOptionsMenu();
            }

            calcShouldPay();
            DiabloUtils.instance().makeToast(getContext(), mSaleTable.getChildCount());
        }

        else if (getResources().getString(R.string.modify) == item.getTitle()){
            if (!DiabloEnum.DIABLO_FREE.equals(stock.getFree())){
                switchToStockSelectFrame(stock, R.string.modify);
            }
        }

        return true;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
         super.onPrepareOptionsMenu(menu);
        for (Integer i=0; i<mButtons.size(); i++){
            Integer key = mButtons.keyAt(i);
            DiabloButton button = mButtons.get(key);
            menu.findItem(button.getResId()).setEnabled(button.isEnabled());
//            if (!button.getResId().equals(R.id.sale_in_clear_draft)){
//                menu.findItem(button.getResId()).setEnabled(button.isEnabled());
//            }
        }

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_in_sale, menu);
        // MenuItem save = menu.findItem(R.id.sale_in_save);
        // ((Button)save.getActionView()).setTextColor(Color.BLUE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sale_in_money_off:
                mSaleCalcController.resetCash();
                break;
            case R.id.sale_in_back:
                Fragment f = getFragmentManager().findFragmentByTag(DiabloEnum.TAG_SALE_DETAIL);
                if (null == f){
                    f = new SaleDetail();
                }
                ((MainActivity)getActivity()).selectMenuItem(2);
                ((MainActivity)getActivity()).switchFragment(f, DiabloEnum.TAG_SALE_DETAIL);
                break;
            case R.id.sale_in_draft:
                // get draft from db
                List<SaleCalc> calcs = dbInstance.queryAllSaleCalc();
                if (null != calcs && 0 != calcs.size()) {
                    String [] titles = new String[calcs.size()];
                    final SparseArray<SaleCalc> sparseCalcs = new SparseArray<>();
                    int index = 0;
                    for (SaleCalc c: calcs){
                        String name = Profile.instance().getRetailerById(c.getRetailer()).getName();
                        String shop = utils.getShop(Profile.instance().getSortAvailableShop(), c.getShop()).getName();
                        titles[index] = name + "-" + shop;
                        sparseCalcs.put(index, c);
                        index++;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setIcon(R.drawable.ic_drafts_black_24dp);
                    builder.setTitle(getContext().getResources().getString(R.string.draft_select));
                    builder.setSingleChoiceItems(titles, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            // utils.makeToast(getContext(), i);
                            SaleCalc c = sparseCalcs.get(i);
                            resetWith(c, recoverFromDB(c));;
                        }
                    });

                    builder.create().show();
                } else {
                    utils.makeToast(getContext(), item.getTitle().toString());
                }
                break;
            case R.id.sale_in_clear_draft:
                dbInstance.clearAll();
                utils.makeToast(getContext(), getContext().getResources().getString(R.string.draft_clear_success));
                break;
            case R.id.sale_in_next:
                init();
                break;
            case R.id.sale_in_save:

                break;
            default:
                // return super.onOptionsItemSelected(item);
                break;

        }

        return true;
    }

    public List<SaleStock> recoverFromDB(SaleCalc calc){
        return dbInstance.querySaleStock(calc);
    }

    public void setCurrentSelectRow(TableRow selectRow) {
        this.mCurrentSelectRow = selectRow;
    }

    public void calcShouldPay(){
        // calculate stock
        Integer total     = 0;
        Integer sell      = 0;
        Integer reject    = 0;
        Float   shouldPay = 0f;

        for (int i=1; i<mSaleTable.getChildCount(); i++){
            View row = mSaleTable.getChildAt(i);
            if (row instanceof TableRow){
                SaleStock stock = (SaleStock)row.getTag();
                total += stock.getSaleTotal();
                if (stock.getSaleTotal() > 0){
                    sell += stock.getSaleTotal();
                } else {
                    reject += stock.getSaleTotal();
                }

                shouldPay += stock.getSalePrice();
            }
        }

        mSaleCalcController.setSaleInfo(total, sell, reject);
        mSaleCalcController.setShouldPay(shouldPay);
        mSaleCalcController.resetAccBalance();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // utils.makeToast(getContext(), hidden ? "hidden" : "show");
        if (!hidden){
            if (mComesFrom.equals(R.integer.COMES_FORM_STOCK_SELECT_ON_SALE)){
                SaleStock s = mStockSelectListener.afterStockSelected();
                List<SaleStockAmount> amounts = s.getAmounts();

                switch (mStockSelectListener.StockSelectAction()){
                    case R.integer.action_save:
                        for (SaleStock ms: mSaleStocks){
                            if (ms.getOrderId().equals(s.getOrderId())){
                                ms.clearAmounts();
                                Integer saleTotal = 0;
                                for (SaleStockAmount a: amounts){
                                    ms.addAmount(a);
                                    if (a.getSellCount() != 0){
                                        saleTotal += a.getSellCount();
                                    }
                                }
                                ms.setSaleTotal(saleTotal);
                                TableRow row = getRowByOrderId(s.getOrderId());
                                View cell = SaleStockHandler.getColumn(getContext(), row, R.string.amount);
                                utils.setEditTextValue((EditText)cell, saleTotal);
                                break;
                            }
                        }

                        break;
                    case R.integer.action_cancel:
                        if (s.getOrderId().equals(0)){
                            TableRow row = (TableRow) mSaleTable.getChildAt(0);
                            row.removeAllViews();
                            mSaleTable.removeView(row);
                            addEmptyRowToTable();
                        }
                        break;
                    default:
                        break;
                }

                mComesFrom = R.integer.COMES_FORM_SELF;
            }
        }
    }

    private TableRow getRowByOrderId(Integer orderId){
        for (Integer i=0; i<mSaleTable.getChildCount(); i++){
            View row = mSaleTable.getChildAt(i);

            if (row instanceof TableRow
                && null != row.getTag()
                &&((SaleStock)row.getTag()).getOrderId().equals(orderId)){
                return (TableRow) row;
            }
        }

        return null;
    }

    private Integer findSaleStock(MatchStock mStock){
        Integer orderId = DiabloEnum.INVALID_INDEX;
        for (SaleStock s: mSaleStocks){
            if (s.getStyleNumber().equals(mStock.getStyleNumber())
                    && s.getBrandId().equals(mStock.getBrandId())){
                orderId = s.getOrderId();
                break;
            }
        }

        return orderId;
    }

    public void setStockListener(OnStockSelectListener listener){
        mStockSelectListener = listener;
    }

    public interface OnStockSelectListener {
        Integer StockSelectAction();
        SaleStock afterStockSelected();
    }

}
