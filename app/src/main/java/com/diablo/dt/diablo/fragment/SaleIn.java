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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.Client.StockClient;
import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.activity.adapter.EmployeeAdapter;
import com.diablo.dt.diablo.activity.adapter.MatchStockAdapter;
import com.diablo.dt.diablo.activity.adapter.RetailerAdapter;
import com.diablo.dt.diablo.activity.adapter.SpinnerStringAdapter;
import com.diablo.dt.diablo.entity.AuthenShop;
import com.diablo.dt.diablo.entity.DiabloEnum;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profie;
import com.diablo.dt.diablo.request.MatchStockRequest;
import com.diablo.dt.diablo.rest.StockInterface;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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

    private String [] mTitles;
    private String [] mPriceType;
    private TableLayout mSaleTable;
    private Integer mLoginShop;
    private String mCurrentDate;

    private Integer mSelectPrice;

    private List<MatchStock> mMatchStocks;

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

        mLoginShop = Profie.getInstance().getLoginShop();
        if ( mLoginShop.equals(DiabloEnum.INVALID_INDEX) ){
            mLoginShop = Profie.getInstance().getAvailableShopIds().get(0);
        }

        mCurrentDate = DiabloUtils.getInstance().currentDate();

        mSelectPrice = DiabloUtils.getInstance().toInteger(
                Profie.getInstance().getConfig(mLoginShop, DiabloEnum.START_PRICE, "1"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale_in, container, false);

        // retailer
        AutoCompleteTextView autoCompleteRetailer =
                (AutoCompleteTextView) view.findViewById(R.id.sale_select_retailer);

        RetailerAdapter retailerAdapter = new RetailerAdapter(
                getContext(),
                R.layout.typeahead_retailer,
                R.id.typeahead_select_retailer,
                Profie.getInstance().getRetailers());

        autoCompleteRetailer.setThreshold(1);
        autoCompleteRetailer.setAdapter(retailerAdapter);

        // shop
        TextView shopView = (TextView) view.findViewById(R.id.sale_selected_shop);
        AuthenShop shop = DiabloUtils.getInstance().getShop(Profie.getInstance().getSortAvailableShop(), mLoginShop);
        shopView.setText(shop.getName());

        // current time
        TextView viewDate = (TextView) view.findViewById(R.id.sale_selected_date);
        viewDate.setText(DiabloUtils.getInstance().currentDatetime());

        // employee
        Spinner employeeSpinner = (Spinner) view.findViewById(R.id.sale_select_employee);

        EmployeeAdapter employeeAdapter = new EmployeeAdapter(
                getContext(),
                R.layout.typeahead_employee,
                R.id.typeahead_select_employee,
                Profie.getInstance().getEmployees());
        employeeSpinner.setAdapter(employeeAdapter);

        // table
        mSaleTable = (TableLayout)view.findViewById(R.id.t_sale);
        mSaleTable.addView(addHead());
        // mSaleTable.addView(addEmptyRow());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // DiabloUtils.getInstance().openKeyboard(getContext());
        // get all stocks
        this.getAllMatchStock();
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
                lp.weight = 0.5f;
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
                AutoCompleteTextView eCell = new AutoCompleteTextView(this.getContext());
                eCell.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                eCell.requestFocus();
                // utils.focusAndShowKeyboard(getContext(), eCell);
                eCell.setTextColor(Color.BLACK);
                // right-margin
                lp.weight = 2f;
                eCell.setLayoutParams(lp);
                eCell.setHint(R.string.please_input_good);
                eCell.setTextSize(18);
                eCell.setTextColor(Color.BLACK);

                eCell.setDropDownWidth(500);
                eCell.setDropDownHeight(-2);
                eCell.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

                eCell.setDropDownHorizontalOffset((eCell.getMeasuredWidth() * 3));
                eCell.setDropDownVerticalOffset(-1000);
                // Log.d("SaleIn:", "mMatchStocks length " + mMatchStocks.size());
                MatchStockAdapter adapter = new MatchStockAdapter(
                        getContext(),
                        R.layout.typeahead_match_stock_on_sale,
                        R.id.typeahead_select_stock_on_sale,
                        new ArrayList<MatchStock>(mMatchStocks),
                        row);

                eCell.setThreshold(1);
                eCell.setAdapter(adapter);

                eCell.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long extra) {
                        MatchStock selectStock = (MatchStock) adapterView.getItemAtPosition(position);
                        SaleStock s = new SaleStock(selectStock, mSelectPrice);
                        // TableRow selectRow = ((MatchStockAdapter) adapterView.getAdapter()).getRow();
                        for (Integer i=0; i<row.getChildCount(); i++){
                            View cell = row.getChildAt(i);
                            String name = (String)cell.getTag();

                            if (getResources().getString(R.string.good).equals(name)) {
                                ((AutoCompleteTextView)cell).setFocusable(false);
                            }

                            if (getResources().getString(R.string.price).equals(name)) {
                                ((TextView)cell).setText(getValidPrice(s));
                            }
                            else if (getResources().getString(R.string.discount).equals(name)){
                                ((TextView)cell).setText(utils.toString(s.getDiscount()));
                            }
                            else if (getResources().getString(R.string.fprice).equals(name)){
                                ((TextView)cell).setText(
                                        utils.toString(
                                                utils.priceWithDiscount(
                                                        getValidPrice(s),
                                                        selectStock.getDiscount())));
                            }

                            if (getResources().getString(R.string.amount).equals(name)){
                                cell.requestFocus();
                                // utils.focusAndShowKeyboard(getContext(), cell);
                            }

                        }

                        row.setTag(s);
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
                lp.weight = 0.5f;
                cell.setLayoutParams(lp);
                // cell.setText(title);
                cell.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                cell.setTextSize(18);
                cell.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                cell.setTag(title);
                row.addView(cell);
            } else if (getResources().getString(R.string.comment).equals(title)){
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
            } else if (getResources().getString(R.string.operater).equals(title)){
//                LinearLayout LL = new LinearLayout(getContext());
//                LL.setOrientation(LinearLayout.VERTICAL);
//                LL.setLayoutParams(lp);

                TypedValue a = new TypedValue();
                getContext().getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);


                Button btnDelete = new Button(getContext());
                btnDelete.setTextColor(Color.RED);
                btnDelete.setTextSize(18);
                btnDelete.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                    // windowBackground is a color
                    btnDelete.setBackgroundColor(a.data);
                } else {
                    // windowBackground is not a color, probably a drawable
                    btnDelete.setBackground(getContext().getResources().getDrawable(a.resourceId));
                }
                // lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                btnDelete.setLayoutParams(lp);
                btnDelete.setText(R.string.btn_delete);
//                LL.addView(btnDelete);

//                Button btnModify = new Button(getContext());
//                btnModify.setTextColor(Color.YELLOW);
//                btnModify.setTextSize(18);
//                btnModify.setText(R.string.btn_modify);
//                LL.addView(btnModify);
                row.addView(btnDelete);
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
    };

    private void getAllMatchStock(){
        StockInterface face = StockClient.getClient().create(StockInterface.class);
        Call<List<MatchStock>> call = face.matchAllStock(
                Profie.getInstance().getToken(),
                new MatchStockRequest(
                        Profie.getInstance().getConfig(mLoginShop, DiabloEnum.START_TIME, mCurrentDate),
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

    private String getValidPriceName(){
        String name;
        switch (mSelectPrice){
            case 1:
                name = getResources().getString(R.string.tag_price);
                break;
            case 2:
                name = getResources().getString(R.string.pkg_price);
                break;
            case 3:
                name = getResources().getString(R.string.price3);
                break;
            case 4:
                name = getResources().getString(R.string.price4);
                break;
            case 5:
                name = getResources().getString(R.string.price5);
                break;
            default:
                name = getResources().getString(R.string.tag_price);
                break;
        }

        return name;
    }

    private String getValidPrice(SaleStock stock){
        String price;
        DiabloUtils utils = DiabloUtils.getInstance();
        switch (mSelectPrice){
            case 1:
                price = utils.toString(stock.getTagPrice());
                break;
            case 2:
                price = utils.toString(stock.getPkgPrice());
                break;
            case 3:
                price = utils.toString(stock.getPrice3());
                break;
            case 4:
                price = utils.toString(stock.getPrice4());
                break;
            case 5:
                price = utils.toString(stock.getPrice5());
                break;
            default:
                price = utils.toString(stock.getTagPrice());
                break;
        }

        return price;
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

        public Integer getState() {
            return state;
        }

        public void setState(Integer state) {
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

            return price;
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
                    ((TextView)cell).setText(utils.toString((s.getValidPrice() * msg.arg1)));
                    if (DiabloEnum.STARTING_SALE.equals(s.getState())){
                        View orderCell =  getColumn(mFragment.get().getContext(), row, R.string.order_id);
                        if (null != orderCell){
                            ((TextView)orderCell).setText(utils.toString(((SaleIn) mFragment.get()).getValidRow()));
                        }

                        s.setState(DiabloEnum.FINISHED_SALE);
                        ((SaleIn)mFragment.get()).addEmptyRowToTable();
                    }

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
        mSaleTable.addView(addEmptyRow(), 1);
    }

    public Integer getValidRow(){
        Integer rows = 0;
        for (int i=0; i<mSaleTable.getChildCount(); i++){
            View row = mSaleTable.getChildAt(i);
            if (row instanceof TableRow) rows++;
        }

        // head does not include
        return rows - 1;
    }
}
