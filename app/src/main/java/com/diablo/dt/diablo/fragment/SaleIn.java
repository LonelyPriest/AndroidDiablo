package com.diablo.dt.diablo.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import com.diablo.dt.diablo.Client.StockClient;
import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.EmployeeAdapter;
import com.diablo.dt.diablo.adapter.SpinnerStringAdapter;
import com.diablo.dt.diablo.entity.AuthenShop;
import com.diablo.dt.diablo.entity.DiabloEnum;
import com.diablo.dt.diablo.entity.Employee;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profie;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.entity.SaleCalc;
import com.diablo.dt.diablo.request.MatchStockRequest;
import com.diablo.dt.diablo.rest.StockInterface;
import com.diablo.dt.diablo.task.MatchRetailerTask;
import com.diablo.dt.diablo.task.MatchStockTask;
import com.diablo.dt.diablo.task.TextChangeOnAutoComplete;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SaleIn.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SaleIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaleIn extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private DiabloUtils utils = DiabloUtils.instance();

    private String [] mTitles;
    private String [] mPriceType;
    private String [] mExtraCost;

    private TableLayout mSaleTableHead;
    private TableLayout mSaleTable;
    private TableRow mCurrentSelectRow;

    private Integer mLoginShop;
    private Integer mLoginEmployee;
    private Integer mLoginRetailer;
    private Integer mSelectPrice;

    private List<MatchStock> mMatchStocks;
    private Retailer mSelectRetailer;

    private SaleCalc mSaleCalc;

    private View mViewShouldPay;
    private View mViewSaleTotal;
    private View mViewBalance;
    private View mViewAccBalance;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mTitles = getResources().getStringArray(R.array.thead_sale);
        mPriceType = getResources().getStringArray(R.array.price_type_on_sale);
        mExtraCost = getResources().getStringArray(R.array.extra_cost_on_sale);

        mLoginShop = Profie.getInstance().getLoginShop();
        if ( mLoginShop.equals(DiabloEnum.INVALID_INDEX) ){
            mLoginShop = Profie.getInstance().getAvailableShopIds().get(0);
        }

        mLoginEmployee = Profie.getInstance().getLoginEmployee();
        if ( mLoginEmployee.equals(DiabloEnum.INVALID_INDEX)){
            mLoginEmployee = Profie.getInstance().getEmployees().get(0).getId();
        }

        mLoginRetailer = Profie.getInstance().getLoginRetailer();
        if (mLoginRetailer.equals(DiabloEnum.INVALID_INDEX)){
            mLoginRetailer = Profie.getInstance().getRetailers().get(0).getId();
        }

        mSelectPrice = utils.toInteger(
                Profie.getInstance().getConfig(mLoginShop, DiabloEnum.START_PRICE, DiabloEnum.TAG_PRICE));

        mSaleCalc = new SaleCalc();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale_in, container, false);

        // retailer
        mViewBalance = (EditText)view.findViewById(R.id.sale_balance);
        mViewAccBalance = (EditText)view.findViewById(R.id.sale_accbalance);

        final AutoCompleteTextView autoCompleteRetailer = (AutoCompleteTextView) view.findViewById(R.id.sale_select_retailer);
        resetRetailerBalance(mLoginRetailer);
        resetRetailerBalanceView();
        autoCompleteRetailer.setText(mSelectRetailer.getName());

        new TextChangeOnAutoComplete(autoCompleteRetailer).addListen(new TextChangeOnAutoComplete.TextWatch() {
            @Override
            public void afterTextChanged(String value) {
                if (value.length() > 0) {
                    new MatchRetailerTask(
                            getContext(),
                            autoCompleteRetailer,
                            Profie.getInstance().getRetailers()).execute(value);
                }
            }
        });

        autoCompleteRetailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Retailer selectRetailer = (Retailer) adapterView.getItemAtPosition(i);
                resetRetailerBalance(selectRetailer.getId());
                resetRetailerBalanceView();
                autoCompleteRetailer.setText(selectRetailer.getName());
            }
        });

        // shop
        EditText shopView = (EditText) view.findViewById(R.id.sale_selected_shop);
        AuthenShop shop = DiabloUtils.getInstance().getShop(Profie.getInstance().getSortAvailableShop(), mLoginShop);
        shopView.setText(shop.getName());
        mSaleCalc.setShop(shop.getShop());

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
                Profie.getInstance().getEmployees());
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

        // extra cost
        final Spinner extraCostSpinner = (Spinner) view.findViewById(R.id.sale_select_extra_cost);
        ArrayAdapter<CharSequence> extraCostAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.extra_cost_on_sale, android.R.layout.simple_spinner_item);
        extraCostAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        extraCostSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        extraCostSpinner.setAdapter(extraCostAdapter);

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
        mViewShouldPay = (EditText) view.findViewById(R.id.sale_should_pay);
        mViewSaleTotal = (EditText) view.findViewById(R.id.sale_total);

        // cash
        EditText cash = (EditText) view.findViewById(R.id.sale_cash);
        utils.addTextChangedListenerOfPayment(cash, new DiabloUtils.Payment() {
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
        mSaleTableHead = (TableLayout)view.findViewById(R.id.t_sale_head) ;
        mSaleTableHead.addView(addHead());
        this.getAllMatchStock();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // DiabloUtils.getInstance().openKeyboard(getContext());
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

    private TableRow addEmptyRow(){
        // lp.setMargins(0, 0, 10, 0);
        final TableRow row = new TableRow(this.getContext());
        // row.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final DiabloUtils utils = DiabloUtils.getInstance();

        for (String title: mTitles){
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            if (getResources().getString(R.string.good).equals(title)){
                final AutoCompleteTextView eCell = new AutoCompleteTextView(this.getContext());
                eCell.setRawInputType(InputType.TYPE_CLASS_NUMBER);
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
//
//               eCell.setDropDownHorizontalOffset((eCell.getMeasuredWidth() * 3));
                // eCell.setDropDownVerticalOffset(-1000);

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
                        MatchStock selectStock = (MatchStock) adapterView.getItemAtPosition(position);
                        SaleStock s = new SaleStock(selectStock, mSelectPrice);
                        row.setTag(s);
                        // TableRow selectRow = ((MatchStockAdapter) adapterView.getAdapter()).getRow();
                        for (Integer i=0; i<row.getChildCount(); i++){
                            final View cell = row.getChildAt(i);
                            String name = (String)cell.getTag();

                            if (getResources().getString(R.string.good).equals(name)) {
                                cell.setFocusable(false);
                                ((AutoCompleteTextView )cell).setText(selectStock.getName());
                            }

                            if (getResources().getString(R.string.price).equals(name)) {
                                DiabloUtils.instance().setTextViewValue((TextView) cell, s.getValidPrice());
                            }
                            else if (getResources().getString(R.string.discount).equals(name)){
                                DiabloUtils.instance().setTextViewValue((TextView) cell, s.getDiscount());
                            }
                            else if (getResources().getString(R.string.fprice).equals(name)){
                                DiabloUtils.instance().setEditTextValue((EditText)cell, s.getFinalPrice());
                                ((EditText)cell).addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {
                                        SaleStock s = (SaleStock) row.getTag();
                                        Float price = DiabloUtils.instance().toFloat(editable.toString());
                                        s.setFinalPrice(price);
                                        View calCell = SaleStockHandler.getColumn(getContext(), row, R.string.calculate);
                                        DiabloUtils.instance().setTextViewValue((TextView) calCell, s.getSalePrice());
                                    }
                                });
                            }

                            if (getResources().getString(R.string.amount).equals(name)){
                                cell.requestFocus();
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
                        mPriceType,
                        row);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sCell.setAdapter(adapter);
                sCell.setSelection(mSelectPrice - 1);
                sCell.setTag(title);
                row.addView(sCell);
            }
            else if (getResources().getString(R.string.amount).equals(title)){
                final EditText cell = new EditText(this.getContext());
                // font
                cell.setTextColor(Color.BLACK);
                // right-margin
                cell.setLayoutParams(lp);
                // cell.setText(title);
                cell.setGravity(Gravity.CENTER_VERTICAL);
                cell.setTextColor(Color.RED);
                cell.setTextSize(18);
                cell.setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_SIGNED);
                cell.setTag(title);

                cell.addTextChangedListener(new TextWatcher() {
                    private Timer timer=new Timer();
                    private final long DELAY = 500; // milliseconds
                    private String inputValue;

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (timer != null) timer.cancel();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        inputValue = editable.toString();
                        timer.cancel();
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Message message = Message.obtain(mHandler);
                                message.what = SaleStockHandler.SALE_TOTAL_CHANGED;
                                if (inputValue.length() == 1 && inputValue.startsWith("-")){
                                    message.arg1 = 0;
                                } else {
                                    message.arg1 = utils.toInteger(inputValue);
                                }

                                message.obj = row;
                                message.sendToTarget();
                            }
                        }, DELAY);
                    }
                });
                row.addView(cell);
            } else if (getResources().getString(R.string.order_id).equals(title)){
                TextView cell = new TextView(this.getContext());
                // font
                cell.setTextColor(Color.BLACK);
                // right-margin
                lp.weight = 0.8f;
                cell.setLayoutParams(lp);
                // cell.setText(title);
                cell.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                cell.setTextSize(18);
                cell.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                cell.setTag(title);
                row.addView(cell);
            } else if(getResources().getString(R.string.fprice).equals(title)){
                final EditText cell = new EditText(this.getContext());
                // font
                cell.setTextColor(Color.BLACK);
                // right-margin
                cell.setLayoutParams(lp);
                // cell.setText(title);
                cell.setGravity(Gravity.CENTER_VERTICAL);
                // cell.setTextColor(Color.RED);
                cell.setTextSize(18);
                cell.setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
                cell.setTag(title);
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
                cell.setTag(title);

                cell.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        final String inputValue = editable.toString();
                        if (!inputValue.isEmpty())
                            ((SaleStock)row.getTag()).setComment(editable.toString());
                    }
                });

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

    private void getAllMatchStock(){
        StockInterface face = StockClient.getClient().create(StockInterface.class);
        Call<List<MatchStock>> call = face.matchAllStock(
                Profie.getInstance().getToken(),
                new MatchStockRequest(
                        Profie.getInstance().getConfig(mLoginShop, DiabloEnum.START_TIME, DiabloUtils.getInstance().currentDate()),
                        mLoginShop,
                        DiabloEnum.USE_REPO));
        call.enqueue(new Callback<List<MatchStock>>() {
            @Override
            public void onResponse(Call<List<MatchStock>> call, Response<List<MatchStock>> response) {
                Log.d("LOGIN:", "success to get retailer");
                mMatchStocks = response.body();
                mSaleTable.addView(addEmptyRow());
            }

            @Override
            public void onFailure(Call<List<MatchStock>> call, Throwable t) {
                Log.d("LOGIN:", "failed to get retailer");
            }
        });
    }

    private class SaleStock {
        private Integer orderId;

        private String styleNumber;
        private String brand;
        private String type;

        private Integer brandId;
        private Integer typeId;
        private Integer stockExist;
        private Integer selectedPrice;

        private Float tagPrice;
        private Float pkgPrice;
        private Float price3;
        private Float price4;
        private Float price5;
        private Float discount;
        private Float finalPrice;

        private Integer saleTotal;

        private String comment;

        private Integer state;


        private SaleStock(MatchStock stock, Integer selectedPrice) {
            this.styleNumber = stock.getStyleNumber();
            this.brand = stock.getBrand();
            this.type = stock.getType();
            this.brandId = stock.getBrandId();
            this.typeId = stock.getTypeId();
            this.tagPrice = stock.getTagPrice();
            this.pkgPrice = stock.getPkgPrice();
            this.price3 = stock.getPrice3();
            this.price4 = stock.getPrice4();
            this.price5 = stock.getPrice5();
            this.discount = stock.getDiscount();

            this.selectedPrice = selectedPrice;
            this.state = DiabloEnum.STARTING_SALE;
        }

        private String getStyleNumber() {
            return styleNumber;
        }

        private String getBrand() {
            return brand;
        }

        private String getType() {
            return type;
        }

        private Integer getBrandId() {
            return brandId;
        }

        private Integer getTypeId() {
            return typeId;
        }

        private Integer getSelectedPrice() {
            return selectedPrice;
        }

        private Float getTagPrice() {
            return tagPrice;
        }

        private Float getPkgPrice() {
            return pkgPrice;
        }

        private Float getPrice3() {
            return price3;
        }

        private Float getPrice4() {
            return price4;
        }

        private Float getPrice5() {
            return price5;
        }

        private Float getDiscount() {
            return discount;
        }

        private Integer getOrderId() {
            return orderId;
        }

        private void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        private Integer getStockExist() {
            return stockExist;
        }

        private void setStockExist(Integer stockExist) {
            this.stockExist = stockExist;
        }

        private Integer getSaleTotal() {
            return saleTotal;
        }

        private void setSaleTotal(Integer saleTotal) {
            this.saleTotal = saleTotal;
        }

        private String getComment() {
            return comment;
        }

        private void setComment(String comment) {
            this.comment = comment;
        }

        private void setSelectedPrice(Integer selectedPrice) {
            this.selectedPrice = selectedPrice;
        }

        private Integer getState() {
            return state;
        }

        private void setState(Integer state) {
            this.state = state;
        }

        private Float getValidPrice(){
            Float price;
            switch (mSelectPrice){
                case 1:
                    price = getTagPrice();
                    break;
                case 2:
                    price = getPkgPrice();
                    break;
                case 3:
                    price = getPrice3();
                    break;
                case 4:
                    price = getPrice4();
                    break;
                case 5:
                    price = getPrice5();
                    break;
                default:
                    price = getTagPrice();
                    break;
            }

            setFinalPrice(price);
            return getFinalPrice();
        }

        private void  setFinalPrice(Float price){
            this.finalPrice = price;
        }

        private Float getFinalPrice(){
            return this.finalPrice;
        }

        private Float getSalePrice(){
            return DiabloUtils.getInstance().priceWithDiscount(finalPrice, discount) * saleTotal;
        }
    }

    private static class SaleStockHandler extends Handler{
        private final static Integer SALE_TOTAL_CHANGED = 1;
        private final static Integer SALE_PRICE_TYPE_SELECTED = 2;
        private final static Integer SALE_GOOD_SELECTED = 3;

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
                    DiabloUtils.instance().setTextViewValue((TextView)cell, s.getSalePrice());

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
                                // f.setCurrentSelectRow((TableRow) view);
                                view.showContextMenu();

                                return true;
                            }
                        });

                        f.registerForContextMenu(row);
                        f.addEmptyRowToTable();
                    }
                    f.resetShouldPay();
                    f.calcRetailerAccBalance();

                    f.setCalcView();
                    f.resetRetailerAccBalanceView();
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        setCurrentSelectRow((TableRow) v);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_on_sale, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Integer orderId = ((SaleStock)mCurrentSelectRow.getTag()).getOrderId();

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

            resetShouldPay();
            calcRetailerAccBalance();

            setCalcView();
            resetRetailerAccBalanceView();
            DiabloUtils.instance().makeToast(getContext(), mSaleTable.getChildCount());
        }

        return true;
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
                    - mSaleCalc.getVerificate()
                    - mSaleCalc.getHasPay();
            mSaleCalc.setAccBalance(accBalance);
        }
    }

    public void resetRetailerBalance(Integer retailerId){
        mSaleCalc.setRetailer(retailerId);
        mSelectRetailer = Profie.getInstance().getRetailerById(retailerId);
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
}
