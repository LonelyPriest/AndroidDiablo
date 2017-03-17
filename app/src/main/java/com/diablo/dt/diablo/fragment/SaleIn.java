package com.diablo.dt.diablo.fragment;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.activity.MainActivity;
import com.diablo.dt.diablo.adapter.EmployeeAdapter;
import com.diablo.dt.diablo.adapter.SpinnerStringAdapter;
import com.diablo.dt.diablo.entity.AuthenShop;
import com.diablo.dt.diablo.entity.DiabloButton;
import com.diablo.dt.diablo.entity.Employee;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.entity.SaleCalc;
import com.diablo.dt.diablo.entity.SaleStock;
import com.diablo.dt.diablo.entity.SaleStockAmount;
import com.diablo.dt.diablo.task.MatchRetailerTask;
import com.diablo.dt.diablo.task.MatchStockTask;
import com.diablo.dt.diablo.task.TextChangeOnAutoComplete;
import com.diablo.dt.diablo.utils.DiabloDBManager;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloSaleAmountChangeWatcher;
import com.diablo.dt.diablo.utils.DiabloSaleRow;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.diablo.dt.diablo.R.string.amount;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SaleIn.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SaleIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaleIn extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private OnStockSelectListener mStockSelectListener;

    private Integer mComesFrom;

    private final static String LOG_TAG = "SALE_IN:";
    private DiabloUtils utils = DiabloUtils.instance();
    private DiabloDBManager dbInstance;

    private String [] mTitles;
    private String [] mPriceType;
    // private String [] mExtraCost;

    // private TableLayout mSaleTableHead;
    private TableLayout mSaleTable;
    private TableRow mCurrentSelectRow;

    private Integer mLoginShop;
    private Integer mLoginRetailer;
    private Integer mSelectPrice;

    private List<MatchStock> mMatchStocks;
    private List<SaleStock> mSaleStocks;

    private Retailer mSelectRetailer;
    private SaleCalc mSaleCalc;

    private AutoCompleteTextView mRetailerView;
    private EditText mShopView;
    private View mViewShouldPay;
    private View mViewSaleTotal;
    private View mViewBalance;
    private View mViewAccBalance;
    private View mViewCash;

    private SparseArray<DiabloButton> mButtons= new SparseArray<>();

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
        // mExtraCost = getResources().getStringArray(R.array.extra_cost_on_sale);

        mLoginShop = Profile.instance().getLoginShop();
//        mLoginEmployee = Profile.instance().getLoginEmployee();
//        if ( mLoginEmployee.equals(DiabloEnum.INVALID_INDEX)){
//            mLoginEmployee = Profile.instance().getEmployees().get(0).getId();
//        }

        mLoginRetailer = Profile.instance().getLoginRetailer();
        if (mLoginRetailer.equals(DiabloEnum.INVALID_INDEX)){
            mLoginRetailer = Profile.instance().getRetailers().get(0).getId();
        }

        mSelectPrice = utils.toInteger(
                Profile.instance().getConfig(mLoginShop, DiabloEnum.START_PRICE, DiabloEnum.TAG_PRICE));

        mButtons.put(R.id.sale_in_back, new DiabloButton(R.id.sale_in_back));
        mButtons.put(R.id.sale_in_money_off, new DiabloButton(R.id.sale_in_money_off));
        mButtons.put(R.id.sale_in_draft, new DiabloButton(R.id.sale_in_draft));
        mButtons.put(R.id.sale_in_save, new DiabloButton(R.id.sale_in_save));
        mButtons.put(R.id.sale_in_next, new DiabloButton(R.id.sale_in_next));
        mButtons.put(R.id.sale_in_clear_draft, new DiabloButton(R.id.sale_in_clear_draft));

        dbInstance = DiabloDBManager.instance();
        mRowSize = 0;
        mComesFrom = R.integer.COMES_FORM_SELF;
        mSaleStocks = new ArrayList<>();
        mSaleCalc = new SaleCalc();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale_in, container, false);
        ((MainActivity)getActivity()).selectMenuItem(0);

        // option menu
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        // retailer
        mViewBalance = (EditText)view.findViewById(R.id.sale_balance);
        mViewAccBalance = (EditText)view.findViewById(R.id.sale_accbalance);

        mRetailerView = (AutoCompleteTextView) view.findViewById(R.id.sale_select_retailer);
        resetRetailerBalance(mLoginRetailer);
        resetRetailerBalanceView();
        mRetailerView.setText(mSelectRetailer.getName());
        mSaleCalc.setRetailer(mLoginRetailer);

        new TextChangeOnAutoComplete(mRetailerView).addListen(new TextChangeOnAutoComplete.TextWatch() {
            @Override
            public void afterTextChanged(String value) {
                if (value.length() > 0) {
                    new MatchRetailerTask(
                            getContext(),
                            mRetailerView,
                            Profile.instance().getRetailers()).execute(value);
                }
            }
        });

        mRetailerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Retailer selectRetailer = (Retailer) adapterView.getItemAtPosition(i);
                resetRetailerBalance(selectRetailer.getId());
                resetRetailerBalanceView();
                mRetailerView.setText(selectRetailer.getName());
                mSaleCalc.setRetailer(mLoginRetailer);

                if (null != dbInstance.querySaleCalc(mSaleCalc)){
                    utils.makeToast(getContext(), getContext().getResources().getString(R.string.draft_exist));
                }
            }
        });

        // shop
        mShopView = (EditText) view.findViewById(R.id.sale_selected_shop);
        AuthenShop shop = DiabloUtils.getInstance().getShop(Profile.instance().getSortAvailableShop(), mLoginShop);
        mShopView.setText(shop.getName());
        mSaleCalc.setShop(shop.getShop());

        if (null != dbInstance.querySaleCalc(mSaleCalc)){
            utils.makeToast(getContext(), getContext().getResources().getString(R.string.draft_exist));
        }

        // current time
        String datetime = DiabloUtils.getInstance().currentDatetime();
        EditText viewDate = (EditText) view.findViewById(R.id.sale_selected_date);
        viewDate.setText(datetime);
        mSaleCalc.setDatetime(datetime);

        // employee
        final Spinner employeeSpinner = (Spinner) view.findViewById(R.id.sale_select_employee);
        EmployeeAdapter employeeAdapter = new EmployeeAdapter(
                getContext(),
                R.layout.typeahead_employee,
                R.id.typeahead_select_employee,
                Profile.instance().getEmployees());
        employeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee selectEmployee = (Employee) adapterView.getItemAtPosition(i);
                mSaleCalc.setEmployee(selectEmployee.getNumber());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        employeeSpinner.setAdapter(employeeAdapter);

        // extra cost type
        final Spinner extraCostSpinner = (Spinner) view.findViewById(R.id.sale_select_extra_cost);
        ArrayAdapter<CharSequence> extraCostAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.extra_cost_on_sale, android.R.layout.simple_spinner_item);
        extraCostAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        extraCostSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSaleCalc.setExtraCostType(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        extraCostSpinner.setAdapter(extraCostAdapter);

        // extra cost
        final EditText extraCost = (EditText) view.findViewById(R.id.sale_extra_cost);
        utils.addTextChangedListenerOfPayment(extraCost, new DiabloUtils.Payment() {
            @Override
            public void setPayment(String param) {
                mSaleCalc.setExtraCost(utils.toFloat(param));
                calcRetailerAccBalance();
                resetRetailerAccBalanceView();
            }
        });

        // comment
        EditText comment = (EditText) view.findViewById(R.id.sale_comment);
        utils.addTextChangedListenerOfPayment(comment, new DiabloUtils.Payment() {
            @Override
            public void setPayment(String param) {
                mSaleCalc.setComment(param);
            }
        });

        // haspay
        final EditText hasPay = (EditText) view.findViewById(R.id.sale_has_pay);
        mViewShouldPay = view.findViewById(R.id.sale_should_pay);
        mViewSaleTotal = view.findViewById(R.id.sale_total);

        // cash
        mViewCash = view.findViewById(R.id.sale_cash);
        utils.addTextChangedListenerOfPayment((EditText) mViewCash, new DiabloUtils.Payment() {
            @Override
            public void setPayment(String param) {
                mSaleCalc.setCash(utils.toFloat(param));
                mSaleCalc.resetHasPay();
                calcRetailerAccBalance();

                utils.setEditTextValue(hasPay, mSaleCalc.getHasPay());
                resetRetailerAccBalanceView();
            }
        });

        // card
        final EditText card = (EditText) view.findViewById(R.id.sale_card);
        utils.addTextChangedListenerOfPayment(card, new DiabloUtils.Payment() {
            @Override
            public void setPayment(String param) {
                mSaleCalc.setCard(utils.toFloat(param));
                mSaleCalc.resetHasPay();
                calcRetailerAccBalance();

                utils.setEditTextValue(hasPay, mSaleCalc.getHasPay());
                resetRetailerAccBalanceView();
            }
        });

        // wire
        EditText wire = (EditText) view.findViewById(R.id.sale_wire);
        utils.addTextChangedListenerOfPayment(wire, new DiabloUtils.Payment() {
            @Override
            public void setPayment(String param) {
                mSaleCalc.setWire(utils.toFloat(param));
                mSaleCalc.resetHasPay();
                calcRetailerAccBalance();

                utils.setEditTextValue(hasPay, mSaleCalc.getHasPay());
                resetRetailerAccBalanceView();
            }
        });

        // verificate
        EditText verificate = (EditText)view.findViewById(R.id.sale_verificate);
        utils.addTextChangedListenerOfPayment(verificate, new DiabloUtils.Payment() {
            @Override
            public void setPayment(String param) {
                mSaleCalc.setVerificate(utils.toFloat(param));
                // resetShouldPay();
                calcRetailerAccBalance();
                resetRetailerAccBalanceView();
            }
        });

        // table
        mSaleTable = (TableLayout)view.findViewById(R.id.t_sale);
        ((TableLayout)view.findViewById(R.id.t_sale_head)).addView(addHead());
        getAllMatchStock();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // DiabloUtils.instance().openKeyboard(getContext());
        // get all stocks
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSaleInFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSaleInFragmentInteraction(Uri uri);
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
        // lp.setMargins(0, 0, 10, 0);
        final TableRow row = new TableRow(this.getContext());
        // row.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

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
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        // find
                                        StockSelect to = (StockSelect)getFragmentManager().findFragmentByTag(DiabloEnum.TAG_STOCK_SELECT);
                                        if (null == to){
                                            Bundle args = new Bundle();
                                            args.putInt(DiabloEnum.BUNDLE_PARAM_SHOP, mSaleCalc.getShop());
                                            args.putInt(DiabloEnum.BUNDLE_PARAM_RETAILER, mSaleCalc.getRetailer());
                                            args.putString(DiabloEnum.BUNDLE_PARAM_SALE_STOCK, new Gson().toJson(s));
                                            to = new StockSelect();
                                            to.setArguments(args);
                                        } else {
                                            to.setSelectShop(mSaleCalc.getShop());
                                            to.setSelectRetailer(mSaleCalc.getRetailer());
                                            to.setSaleStock(new Gson().toJson(s));
                                        }

                                        if (!to.isAdded()){
                                            transaction.hide(SaleIn.this).add(R.id.frame_container, to, DiabloEnum.TAG_STOCK_SELECT).commit();
                                        } else {
                                            transaction.hide(SaleIn.this).show(to).commit();
                                        }
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
//            else if (getResources().getString(R.string.operater).equals(title)){
//                TypedValue a = new TypedValue();
//                getContext().getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);
//                Button btnDelete = new Button(getContext());
//                btnDelete.setTextColor(Color.RED);
//                btnDelete.setTextSize(18);
//                btnDelete.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//                if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
//                    // windowBackground is a color
//                    btnDelete.setBackgroundColor(a.data);
//                } else {
//                    // windowBackground is not a color, probably a drawable
//                    btnDelete.setBackground(getContext().getResources().getDrawable(a.resourceId));
//                }
//            }
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

        // row.setBackgroundResource(R.drawable.table_row_bg);
        return row;
    };

    private void getAllMatchStock() {
        mMatchStocks = Profile.instance().getMatchStocks();
        mSaleTable.addView(addEmptyRow());
        mButtons.get(R.id.sale_in_back).enable();
        mButtons.get(R.id.sale_in_draft).enable();
        getActivity().invalidateOptionsMenu();
    }

    public static class SaleStockHandler extends Handler{
        public final static Integer SALE_TOTAL_CHANGED = 1;
        public final static Integer SALE_PRICE_TYPE_SELECTED = 2;
        public final static Integer SALE_GOOD_SELECTED = 3;

        private final DiabloUtils utils = DiabloUtils.getInstance();

        WeakReference<Fragment> mFragment;

        SaleStockHandler(Fragment fragment){
            mFragment = new WeakReference<Fragment>(fragment);
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

                    utils.setTextViewValue((TextView)cell, s.getSalePrice());

                    final SaleIn f = ((SaleIn)mFragment.get());
                    if (DiabloEnum.STARTING_SALE.equals(s.getState())){
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

                    f.reCalculatePayment();

                    // save to db
                    // delete old
                    if (f.mRowSize == 1){
                        f.dbInstance.deleteSaleCalc(f.mSaleCalc);
                        f.dbInstance.deleteAllSaleStock(f.mSaleCalc);
                        f.dbInstance.addSaleCalc(f.mSaleCalc);
                    }

                    f.dbInstance.replaceSaleStock(f.mSaleCalc, s);
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
            mButtons.get(R.id.sale_in_money_off).enable();
            mButtons.get(R.id.sale_in_save).enable();
            mButtons.get(R.id.sale_in_next).enable();
            // mButtons.get(R.id.sale_in_draft).enable();
            getActivity().invalidateOptionsMenu();
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
        mSaleTable.removeAllViews();
        mLoginRetailer = calc.getRetailer();
        mLoginShop = calc.getShop();
        mSaleCalc = calc;
        mSaleStocks = stocks;

        mRowSize = 0;
        mComesFrom = R.integer.COMES_FORM_SELF;

        // reset retailer
        resetRetailerBalance(mLoginRetailer);
        resetRetailerBalanceView();
        mRetailerView.setText(mSelectRetailer.getName());

        // reset shop
        AuthenShop selectShop = DiabloUtils.getInstance().getShop(Profile.instance().getSortAvailableShop(), mLoginShop);
        mShopView.setText(selectShop.getName());

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

        mSaleTable.addView(addEmptyRow(), 0);

        reCalculatePayment();

        mButtons.get(R.id.sale_in_money_off).enable();
        mButtons.get(R.id.sale_in_save).enable();
        mButtons.get(R.id.sale_in_next).enable();
        getActivity().invalidateOptionsMenu();
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
        SaleStock removed = (SaleStock)mCurrentSelectRow.getTag();
        Integer orderId = removed.getOrderId();

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
            dbInstance.deleteSaleStock(mSaleCalc, removed);

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
                mButtons.get(R.id.sale_in_save).disable();
                mButtons.get(R.id.sale_in_money_off).disable();
                mButtons.get(R.id.sale_in_next).disable();
                // mButtons.get(R.id.sale_in_draft).disable();
                getActivity().invalidateOptionsMenu();
            }

            reCalculatePayment();
            DiabloUtils.instance().makeToast(getContext(), mSaleTable.getChildCount());
        }

        return true;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // super.onPrepareOptionsMenu(menu);
        for (Integer i=0; i<mButtons.size(); i++){
            Integer key = mButtons.keyAt(i);
            DiabloButton button = mButtons.get(key);
            if (!button.getResId().equals(R.id.sale_in_clear_draft)){
                menu.findItem(button.getResId()).setEnabled(button.isEnabled());
            }
        }

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sale_in_action, menu);
        // MenuItem save = menu.findItem(R.id.sale_in_save);
        // ((Button)save.getActionView()).setTextColor(Color.BLUE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sale_in_money_off:
                utils.setEditTextValue((EditText) mViewCash, 0);
                break;
            case R.id.sale_in_back:
                Fragment f = getFragmentManager().findFragmentByTag(DiabloEnum.TAG_SALE_DETAIL);
                if (null == f){
                    f = new SaleDetail();
                }
                ((MainActivity)getActivity()).selectMenuItem(2);
                ((MainActivity)getActivity()).switchFragment(f, DiabloEnum.TAG_SALE_DETAIL);
                // utils.replaceFragment(getFragmentManager(), new SaleDetail(), DiabloEnum.TAG_SALE_DETAIL);
                break;
            case R.id.sale_in_draft:
                // get draft from db
                List<SaleCalc> calcs = dbInstance.queryAllSaleCalc();
                if (null != calcs) {
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
                            utils.makeToast(getContext(), i);
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

    public void setCalcView(){
        utils.setEditTextValue((EditText)mViewShouldPay, mSaleCalc.getShouldPay());
        utils.setEditTextValue(
                (EditText) mViewSaleTotal,
                Math.abs(mSaleCalc.getSellTotal()) + Math.abs(mSaleCalc.getRejectTotal()));
    }

    public void calcRetailerAccBalance(){
        if (mSaleCalc.getShouldPay() != null){
            Float accBalance = mSaleCalc.getBalance()
                    + mSaleCalc.getShouldPay()
                    + mSaleCalc.getExtraCost()
                    - mSaleCalc.getVerificate()
                    - mSaleCalc.getHasPay();
            mSaleCalc.setAccBalance(accBalance);
        }
    }

    public void resetRetailerBalance(Integer retailerId){
        mSaleCalc.setRetailer(retailerId);
        mSelectRetailer = Profile.instance().getRetailerById(retailerId);
        mSaleCalc.setBalance(mSelectRetailer.getBalance());
        mSaleCalc.setAccBalance(mSelectRetailer.getBalance());
    }

    public void resetRetailerAccBalanceView(){
        utils.setEditTextValue((EditText)mViewAccBalance, mSaleCalc.getAccBalance());
    }

    public void resetRetailerBalanceView(){
        utils.setEditTextValue((EditText)mViewBalance, mSaleCalc.getBalance());
        utils.setEditTextValue((EditText)mViewAccBalance, mSaleCalc.getAccBalance());
    }

    public void resetShouldPay(){
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

        mSaleCalc.setTotal(total);
        mSaleCalc.setSellTotal(sell);
        mSaleCalc.setRejectTotal(reject);
        mSaleCalc.setShouldPay(shouldPay);
    }

    private void reCalculatePayment(){
        resetShouldPay();
        calcRetailerAccBalance();

        setCalcView();
        resetRetailerAccBalanceView();
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
                    && ((SaleStock)row.getTag()).getOrderId().equals(orderId)){
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
