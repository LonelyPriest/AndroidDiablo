package com.diablo.dt.diablo.fragment.sale;

import static com.diablo.dt.diablo.R.string.amount;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
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
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.activity.MainActivity;
import com.diablo.dt.diablo.adapter.MatchStockAdapter;
import com.diablo.dt.diablo.adapter.StringArrayAdapter;
import com.diablo.dt.diablo.client.WSaleClient;
import com.diablo.dt.diablo.controller.DiabloSaleController;
import com.diablo.dt.diablo.entity.DiabloButton;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.entity.Stock;
import com.diablo.dt.diablo.model.sale.DiabloSaleAmountChangeWatcher;
import com.diablo.dt.diablo.model.sale.DiabloSaleRow;
import com.diablo.dt.diablo.model.sale.SaleCalc;
import com.diablo.dt.diablo.model.sale.SaleStock;
import com.diablo.dt.diablo.model.sale.SaleStockAmount;
import com.diablo.dt.diablo.model.sale.SaleUtils;
import com.diablo.dt.diablo.request.sale.NewSaleRequest;
import com.diablo.dt.diablo.response.sale.NewSaleResponse;
import com.diablo.dt.diablo.rest.WSaleInterface;
import com.diablo.dt.diablo.task.MatchSingleStockTask;
import com.diablo.dt.diablo.utils.DiabloAlertDialog;
import com.diablo.dt.diablo.utils.DiabloDBManager;
import com.diablo.dt.diablo.utils.DiabloEditTextWatcher;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;
import com.diablo.dt.diablo.utils.DiabloPattern;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.sale.DiabloSaleCalcView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleIn extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LOG_TAG = "SaleIn:";
    private StockSelect.OnNoFreeStockSelectListener mNoFreeStockListener;

    private Integer mBackFrom;

    // private final static String LOG_TAG = "SALE_IN:";
    private DiabloUtils utils = DiabloUtils.instance();
    private DiabloDBManager dbInstance;

    private String [] mTitles;
    private String [] mPriceTypes;

    private TableLayout mSaleTable;
    private TableRow mCurrentSelectRow;

    private Integer mLoginShop;
    private Integer mStartRetailer;
    private Integer mSelectPrice;
    private Integer mSysRetailer;

    private List<MatchStock> mMatchStocks;
    private List<SaleStock> mSaleStocks;

    // private Retailer mSelectRetailer;
    private DiabloSaleCalcView mSaleCalcView;
    private DiabloSaleController mSaleCalcController;

    private SparseArray<DiabloButton> mButtons;
    private Integer mRowSize;

    private boolean mIsRecoverFromDraft;

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
        fragment.setArguments(args);
        return fragment;
    }

    public void incRow (){
        mRowSize++;
    }

    public void decRow (){
        mRowSize--;
    }

    public void setBackFrom(Integer form){
        mBackFrom = form;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSysRetailer = utils.toInteger(
            Profile.instance().getConfig(DiabloEnum.START_RETAILER, DiabloEnum.DIABLO_STRING_ZERO));

        mTitles = getResources().getStringArray(R.array.thead_sale);
        mPriceTypes = getResources().getStringArray(R.array.price_type_on_sale);

        mIsRecoverFromDraft = false;

        mButtons= new SparseArray<>();
        mButtons.put(R.id.sale_in_back, new DiabloButton(getContext(), R.id.sale_in_back));
        mButtons.put(R.id.sale_in_money_off, new DiabloButton(getContext(), R.id.sale_in_money_off));
        mButtons.put(R.id.sale_in_draft, new DiabloButton(getContext(), R.id.sale_in_draft));
        mButtons.put(R.id.sale_in_save, new DiabloButton(getContext(), R.id.sale_in_save));
        mButtons.put(R.id.sale_in_next, new DiabloButton(getContext(), R.id.sale_in_next));
        mButtons.put(R.id.sale_in_clear_draft, new DiabloButton(getContext(), R.id.sale_in_clear_draft));
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // option menu
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_sale_in, container, false);
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

        // add retailer
        (view.findViewById(R.id.sale_add_retailer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRetailer(inflater, view);
            }
        });

        mSaleTable = (TableLayout)view.findViewById(R.id.t_sale);
        ((TableLayout)view.findViewById(R.id.t_sale_head)).addView(addHead());

        mMatchStocks = Profile.instance().getMatchStocks();
        init();

        return view;
    }

    private Retailer.OnRetailerChangeListener mOnRetailerChangeListener = new Retailer.OnRetailerChangeListener() {
        @Override
        public void afterAdd(Retailer retailer) {
            mSaleCalcController.removeRetailerWatcher();
            mSaleCalcController.setRetailer(retailer);
            mSaleCalcController.setRetailerWatcher(getContext());
        }

        @Override
        public void afterGet(Retailer retailer) {
            mSaleCalcController.setRetailer(retailer);
            mSaleCalcController.removeRetailerWatcher();
            mSaleCalcController.setRetailerWatcher(getContext());

            if (mIsRecoverFromDraft) {
                calcShouldPay();
                addEmptyRowToTable();
                mIsRecoverFromDraft = false;
            }
        }
    };

    public void addRetailer(LayoutInflater inflater, View parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = inflater.inflate(
            R.layout.shortcut_create_retailer, (ViewGroup) parent.findViewById(R.id.shortcut_create_retailer));

        final AlertDialog dialog = builder.setView(view)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String name = ((EditText) view.findViewById(R.id.retailer_name)).getText().toString().trim();
                    String phone = ((EditText) view.findViewById(R.id.retailer_phone)).getText().toString().trim();

                    final Retailer addedRetailer = new Retailer(name, phone);
                    addedRetailer.newRetailer(getContext(), mOnRetailerChangeListener);
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            }).create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(false);

        // (EditText) addRetailerDialog.findViewById(R.id.retailer_phone);
        final EditText editTextName = ((EditText) view.findViewById(R.id.retailer_name));
        editTextName.addTextChangedListener(new DiabloEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (!DiabloPattern.isValidRetailer(editable.toString().trim())) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(false);
                    editTextName.setError(getString(R.string.invalid_retailer));
                }
                else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(true);
                }
            }
        });
    }

    public void init(Integer retailer, Integer shop, SaleCalc calc, List<SaleStock> stocks) {
        if (null != mSaleTable) {
            mSaleTable.removeAllViews();
        }

        mSelectPrice = utils.toInteger(
            Profile.instance().getConfig(
                mLoginShop, DiabloEnum.START_PRICE, DiabloEnum.TAG_PRICE));

        mStartRetailer = retailer;
        mLoginShop = Profile.instance().getLoginShop();

        dbInstance = DiabloDBManager.instance();
        mRowSize = 0;
        mBackFrom = R.string.back_from_unknown;
        mSaleStocks = stocks;


        mSaleCalcController = new DiabloSaleController(calc, mSaleCalcView);

        Retailer.getRetailer(getContext(), retailer, mOnRetailerChangeListener);
        mSaleCalcController.setShop(shop);
        mSaleCalcController.setDatetime(DiabloUtils.getInstance().currentDatetime());

        mSaleCalcController.setDiabloOnRetailerSelected(new DiabloSaleController.OnDiabloRetailerSelectedListener() {
            @Override
            public void onRetailerSelected(SaleCalc c) {
                checkRetailerDraft(c);
            }
        });

        // listener
        // mSaleCalcController.setRetailerWatcher(getContext());
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
        // getActivity().invalidateOptionsMenu();
    }

//    private void enableBtnNextSale() {
//        mButtons.get(R.id.sale_in_next).enable();
//        getActivity().invalidateOptionsMenu();
//    }

    private void checkRetailerDraft(SaleCalc calc) {
        if (null != dbInstance.querySaleCalc(calc)){
            utils.makeToast(
                getContext(),
                getContext().getResources().getString(R.string.draft_exist),
                Toast.LENGTH_LONG);
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

    private final SaleInHandler mHandler = new SaleInHandler(this);

    private TableRow addHead(){
        TableRow row = new TableRow(this.getContext());
        for (String title: mTitles){
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
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
    }

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
                ((TextView)diabloRow.getCell(title)).setText(utils.toString(stock.calcExistStock()));
            }
            else if (getResources().getString(R.string.price_type).equals(title)) {
                Spinner cell = (Spinner)diabloRow.getCell(title);
                StringArrayAdapter adapter=new StringArrayAdapter(
                        this.getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                    mPriceTypes);
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

                        calcShouldPay();
                        dbInstance.replaceSaleStock(
                            mSaleCalcController.getSaleCalc(),
                            diabloRow.getStock(),
                            mStartRetailer);

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
    }

    private TableRow addEmptyRow(){
        final TableRow row = new TableRow(this.getContext());
        for (String title: mTitles){
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);
            if (getResources().getString(R.string.good).equals(title)){
                final AutoCompleteTextView eCell = new AutoCompleteTextView(this.getContext());
                eCell.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                // eCell.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                eCell.requestFocus();
                utils.openKeyboard(getContext(), eCell);
                lp.weight = 2f;
                eCell.setLayoutParams(lp);
                eCell.setHint(R.string.please_input_good);
                eCell.setHintTextColor(Color.GRAY);
                eCell.setTextSize(18);
                eCell.setTextColor(Color.BLACK);
                eCell.setDropDownWidth(500);
                eCell.setThreshold(1);
//              eCell.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

                new MatchStockAdapter(
                    getContext(),
                    R.layout.typeahead_match_stock_on_sale,
                    R.id.typeahead_select_stock_on_sale,
                    eCell);

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
                                            + utils.toString(matchedOrderId),
                                Toast.LENGTH_LONG);
                        } else {
                            final SaleStock s = new SaleStock(matchedStock, mSelectPrice);
                            row.setTag(s);
                            mSaleStocks.add(s);

                            for (Integer i=0; i<row.getChildCount(); i++){
                                final View cell = row.getChildAt(i);
                                String name = (String)cell.getTag();

                                if (getResources().getString(R.string.good).equals(name)) {
                                    cell.setFocusable(false);
                                    // ((AutoCompleteTextView )cell).setText(s.getName());
                                }

                                if (getResources().getString(R.string.price).equals(name)) {
                                    DiabloUtils.instance().setTextViewValue((TextView) cell, s.getValidPrice());
                                    s.setFinalPrice(s.getValidPrice());
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
                                            View calCell = SaleInHandler.getColumn(getContext(), row, R.string.calculate);
                                            utils.setTextViewValue((TextView) calCell, s.getSalePrice());
                                            calcShouldPay();
                                            dbInstance.replaceSaleStock(mSaleCalcController.getSaleCalc(), s, mStartRetailer);
                                        }
                                    });
                                }

                                if (getResources().getString(amount).equals(name)){
                                    // free color, free size
                                    if ( DiabloEnum.DIABLO_FREE.equals(s.getFree()) ){
                                        getStockOfSelected(s, row);
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
                StringArrayAdapter adapter = new StringArrayAdapter(
                        this.getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                    mPriceTypes);
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
                cell.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
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
        Integer shop = mSaleCalcController.getShop();
        Integer retailer = mSaleCalcController.getRetailer();
        SaleUtils.switchToStockSelectFrame(stock, action, DiabloEnum.SALE_IN, retailer, shop, this);
    }

    private void getStockOfSelected(final SaleStock selectStock, final TableRow row) {
        MatchSingleStockTask task = new MatchSingleStockTask(
            selectStock.getStyleNumber(),
            selectStock.getBrandId(),
            mSaleCalcController.getSaleCalc().getShop());

        task.setMatchSingleStockTaskListener(new MatchSingleStockTask.OnMatchSingleStockTaskListener() {
            @Override
            public void onMatchSuccess(List<Stock> matchedStocks) {
                Log.d(LOG_TAG, "success to get stock");
                for (Stock s: matchedStocks) {
                    selectStock.setStockExist(s.getExist());
                }

                View v = SaleInHandler.getColumn(getContext(), row, R.string.stock);
                ((TextView)v).setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                ((TextView)v).setText(utils.toString(selectStock.getStockExist()));

                if (0 != selectStock.getAmounts().size()) {
                    for (SaleStockAmount amount: selectStock.getAmounts()) {
                        amount.setStock(selectStock.getStockExist());
                    }
                } else {
                    SaleStockAmount amount = new SaleStockAmount(
                        DiabloEnum.DIABLO_FREE_COLOR,
                        DiabloEnum.DIABLO_FREE_SIZE);
                    amount.setStock(selectStock.getStockExist());
                    selectStock.clearAmounts();
                    selectStock.addAmount(amount);
                }

            }

            @Override
            public void onMatchFailed(Throwable t) {
                Log.d(LOG_TAG, "failed to get stock");
            }
        });

        task.getStock();
    }

    public static class SaleInHandler extends Handler{
        public final static Integer SALE_TOTAL_CHANGED = 1;
//        public final static Integer SALE_PRICE_TYPE_SELECTED = 2;
//        public final static Integer SALE_GOOD_SELECTED = 3;

        private final DiabloUtils utils = DiabloUtils.getInstance();

        WeakReference<Fragment> mFragment;

        SaleInHandler(Fragment fragment){
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SALE_TOTAL_CHANGED){
                TableRow row = (TableRow) msg.obj;
                View cell = SaleInHandler.getColumn(mFragment.get().getContext(), row, R.string.calculate);
                if (null != cell){
                    SaleStock s = (SaleStock)row.getTag();
                    s.setSaleTotal(msg.arg1);

                    if (DiabloEnum.DIABLO_FREE.equals(s.getFree())){
                        if (0 != s.getAmounts().size()) {
                            for (SaleStockAmount amount: s.getAmounts()) {
                                amount.setSellCount(msg.arg1);
                            }
                        } else {
                            SaleStockAmount amount = new SaleStockAmount(
                                DiabloEnum.DIABLO_FREE_COLOR,
                                DiabloEnum.DIABLO_FREE_SIZE);
                            amount.setSellCount(msg.arg1);
                            amount.setStock(s.getStockExist());
                            s.clearAmounts();
                            s.addAmount(amount);
                        }
                    }

                    final SaleIn f = ((SaleIn)mFragment.get());
                    if (DiabloEnum.STARTING_SALE.equals(s.getState())){
                        if (0 == s.getSaleTotal()){
                            return;
                        } else {
                            s.setState(DiabloEnum.FINISHED_SALE);

                            View orderCell =  SaleInHandler.getColumn(mFragment.get().getContext(), row, R.string.order_id);
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
            mButtons.get(R.id.sale_in_next).enable();
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
        mIsRecoverFromDraft = true;
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
            mButtons.get(R.id.sale_in_next).enable();
        }

        // addEmptyRowToTable();
        // mSaleTable.addView(addEmptyRow(), 0);

        // calcShouldPay();
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
                // getActivity().invalidateOptionsMenu();
            }

            calcShouldPay();
            // DiabloUtils.instance().makeToast(getContext(), mSaleTable.getChildCount(), Toast.LENGTH_SHORT);
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
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();

        inflater.inflate(R.menu.action_on_sale_in, menu);
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
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_SALE_DETAIL);
                break;
            case R.id.sale_in_draft:
                // get draft from db
                List<SaleCalc> calcs = dbInstance.queryAllSaleCalc();
                if (null != calcs && 0 != calcs.size()) {
                    String [] titles = new String[calcs.size()];
                    final SparseArray<SaleCalc> sparseCalcs = new SparseArray<>();
                    int index = 0;
                    boolean validRetailer = true;
                    for (SaleCalc c: calcs){
                        Retailer retailer = Profile.instance().getRetailerById(c.getRetailer());
                        if (null == retailer) {
                            validRetailer = false;
                            break;
                        }

                        String name = retailer.getName();
                        String shop = utils.getShop(Profile.instance().getSortAvailableShop(), c.getShop()).getName();
                        titles[index] = name + "-" + shop;
                        sparseCalcs.put(index, c);
                        index++;
                    }

                    if (validRetailer) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setIcon(R.drawable.ic_drafts_black_24dp);
                        builder.setTitle(getContext().getResources().getString(R.string.draft_select));
                        builder.setSingleChoiceItems(titles, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                                // utils.makeToast(getContext(), i);
                                SaleCalc c = sparseCalcs.get(i);
                                resetWith(c, recoverFromDB(c));
                            }
                        });

                        builder.create().show();
                    } else {
                        utils.makeToast(
                            getContext(),
                            getContext().getString(R.string.draft_other),
                            Toast.LENGTH_LONG);
                    }


                } else {
                    utils.makeToast(getContext(), getContext().getString(R.string.draft_none), Toast.LENGTH_LONG);
                }
                break;
            case R.id.sale_in_clear_draft:
                dbInstance.clearAll();
                utils.makeToast(
                    getContext(),
                    getContext().getResources().getString(R.string.draft_clear_success),
                    Toast.LENGTH_SHORT);
                break;
            case R.id.sale_in_next:
                init();
                break;
            case R.id.sale_in_save:
                mButtons.get(R.id.sale_in_save).disable();
                startSale();
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

    public void setNoFreeStockSelectListener(StockSelect.OnNoFreeStockSelectListener listener){
        mNoFreeStockListener = listener;
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
                        for (SaleStock ms: mSaleStocks){
                            if (ms.getOrderId().equals(s.getOrderId())){
                                ms.clearAmounts();
                                Integer saleTotal = 0;
                                Integer exist = 0;
                                for (SaleStockAmount a: amounts){
                                    ms.addAmount(a);
                                    exist += a.getStock();
                                    if (a.getSellCount() != 0){
                                        saleTotal += a.getSellCount();
                                    }
                                }

                                ms.setColors(s.getColors());
                                ms.setOrderSizes(s.getOrderSizes());
                                ms.setSaleTotal(saleTotal);
                                ms.setStockExist(exist);

                                TableRow row = getRowByOrderId(s.getOrderId());
                                View cellAmount = SaleInHandler.getColumn(getContext(), row, R.string.amount);
                                View cellStock = SaleInHandler.getColumn(getContext(), row, R.string.stock);

                                utils.setEditTextValue((EditText)cellAmount, saleTotal);
                                utils.setTextViewValue((TextView)cellStock, exist);
                                break;
                            }
                        }
                        break;
                    case R.string.action_cancel:
                        if (s.getOrderId().equals(0)){
                            TableRow row = (TableRow) mSaleTable.getChildAt(0);
                            row.removeAllViews();
                            mSaleTable.removeView(row);
                            // remove old row
                            mSaleStocks.remove(0);
                            // add new
                            addEmptyRowToTable();
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

    private void startSale() {
        NewSaleRequest saleRequest = new NewSaleRequest();
        for (SaleStock s: mSaleStocks) {
            NewSaleRequest.DiabloSaleStock d = new NewSaleRequest.DiabloSaleStock();
            d.setOrderId(s.getOrderId());
            d.setStyleNumber(s.getStyleNumber());
            d.setBrand(s.getBrand());
            d.setType(s.getType());

            d.setBrandId(s.getBrandId());
            d.setTypeId(s.getTypeId());
            d.setFirmId(s.getFirmId());

            d.setSex(s.getSex());
            d.setSeason(s.getSeason());
            d.setYear(s.getYear());

            d.setFdiscount(s.getDiscount());
            d.setFprice(s.getFinalPrice());

            d.setPath(s.getPath());
            d.setSecond(s.getSecond() );

            if ( null != s.getOrderSizes() && 0 != s.getOrderSizes().size()) {
                d.setOrderSizes(new ArrayList<>(s.getOrderSizes()));
            }
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
            d.setComment(s.getComment());

            d.setSaleTotal(s.getSaleTotal());
            d.setSellTye(s.getSelectedPrice());

            for (SaleStockAmount a: s.getAmounts()) {
                if ( a.getSellCount() != 0 ){
                    NewSaleRequest.DiabloSaleStockAmount saleAmount = new NewSaleRequest.DiabloSaleStockAmount();
                    saleAmount.setColorId(a.getColorId());
                    saleAmount.setSize(a.getSize());
                    saleAmount.setSellCount(a.getSellCount());
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
        dCalc.setCash(calc.getCash());
        dCalc.setCard(calc.getCard());
        dCalc.setWire(calc.getWire());
        dCalc.setVerificate(calc.getVerificate());
        dCalc.setShouldPay(calc.getShouldPay());
        dCalc.setHasPay(calc.getHasPay());
        dCalc.setSysRetailer(calc.getRetailer().equals(mSysRetailer));
        dCalc.setAccBalance(calc.getAccBalance());

        dCalc.setExtraCostType(calc.getExtraCostType());
        dCalc.setExtraCost(calc.getExtraCost());

        dCalc.setTotal(calc.getTotal());
        dCalc.setSellTotal(calc.getSellTotal());
        dCalc.setRejectTotal(calc.getRejectTotal());

        saleRequest.setSaleCalc(dCalc);

        //print info
        NewSaleRequest.DiabloPrintAttr printAttr = new NewSaleRequest.DiabloPrintAttr();
        printAttr.setImmediatelyPrint(0);
        printAttr.setRetailerId(calc.getRetailer());
        printAttr.setRetailerName(mSaleCalcController.getSelectRetailerName());
        printAttr.setShop(mSaleCalcController.getShopName());
        printAttr.setEmployee(calc.getEmployee());

        saleRequest.setPrintAttr(printAttr);

        startRequest(saleRequest);
    }

    private void startRequest(NewSaleRequest request) {
        final WSaleInterface face = WSaleClient.getClient().create(WSaleInterface.class);
        Call<NewSaleResponse> call = face.startSale(Profile.instance().getToken(), request);

        call.enqueue(new Callback<NewSaleResponse>() {
            @Override
            public void onResponse(Call<NewSaleResponse> call, Response<NewSaleResponse> response) {
                mButtons.get(R.id.sale_in_save).enable();
                final NewSaleResponse res = response.body();
                if (DiabloEnum.HTTP_OK == response.code() && res.getCode().equals(DiabloEnum.SUCCESS)) {
                    // refresh balance
//                    SaleCalc calc = mSaleCalcController.getSaleCalc();
//                    Profile.instance().getRetailerById(calc.getRetailer()).setBalance(calc.getAccBalance());
                    // delete draft
                    dbInstance.clearRetailerSaleStock(mSaleCalcController.getSaleCalc());
                    init();

                    new DiabloAlertDialog(
                        getContext(),
                        true,
                        getResources().getString(R.string.nav_sale_in),
                        getContext().getString(R.string.sale_success)
                            + res.getRsn()
                            + getContext().getString(R.string.sale_start_print_or_not) ,
                        new DiabloAlertDialog.OnOkClickListener() {
                            @Override
                            public void onOk() {
                                utils.startPrint(getContext(), R.string.nav_sale_in, res.getRsn());
                            }
                        }).create();
                } else {
                    Integer errorCode = response.code() == 0 ? res.getCode() : response.code();
                    String extraMessage = res == null ? "" : res.getError();
                    new DiabloAlertDialog(
                        getContext(),
                        getResources().getString(R.string.nav_sale_in),
                        DiabloError.getError(errorCode) + extraMessage).create();
                }
            }

            @Override
            public void onFailure(Call<NewSaleResponse> call, Throwable t) {
                mButtons.get(R.id.sale_in_save).enable();
                new DiabloAlertDialog(
                    getContext(),
                    getResources().getString(R.string.nav_sale_in),
                    DiabloError.getError(99)).create();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy called");
    }
}
