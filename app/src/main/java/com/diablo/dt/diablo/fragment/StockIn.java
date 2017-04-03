package com.diablo.dt.diablo.fragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.controller.DiabloStockController;
import com.diablo.dt.diablo.entity.DiabloButton;
import com.diablo.dt.diablo.model.stock.StockUtils;
import com.diablo.dt.diablo.view.DiabloCellLabel;
import com.diablo.dt.diablo.view.DiabloStockCalcView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StockIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StockIn extends Fragment {
    private DiabloCellLabel[] mLabels;
    private String [] mPriceTypes;
    private SparseArray<DiabloButton> mButtons;

    private DiabloStockCalcView mStockCalcView;
    private DiabloStockController mStockCalcController;

    public StockIn() {
        // Required empty public constructor
    }

    public static StockIn newInstance(String param1, String param2) {
        return new StockIn();
    }

    private void initLabel() {
        mPriceTypes = getResources().getStringArray(R.array.price_type_on_sale);
        mButtons = new SparseArray<>();
        // mButtons.put(R.id.sale_out_save, new DiabloButton(getContext(), R.id.sale_out_save));
        mLabels = StockUtils.createStockLabelsFromTitle(getContext());
    }

    private void initCalc(View view) {
        mStockCalcView = new DiabloStockCalcView();

        mStockCalcView.setmViewFirm(view.findViewById(R.id.stock_select_firm));
        mStockCalcView.setViewShop(view.findViewById(R.id.stock_select_shop));
        mStockCalcView.setViewDatetime(view.findViewById(R.id.stock_select_date));
        mStockCalcView.setViewEmployee(view.findViewById(R.id.stock_select_employee));

        mStockCalcView.setViewStockTotal(view.findViewById(R.id.stock_total));
        mStockCalcView.setViewShouldPay(view.findViewById(R.id.stock_should_pay));
        mStockCalcView.setViewCash(view.findViewById(R.id.stock_cash));
        mStockCalcView.setViewComment(view.findViewById(R.id.stock_comment));

        mStockCalcView.setViewBalance(view.findViewById(R.id.firm_balance));
        mStockCalcView.setViewHasPay(view.findViewById(R.id.firm_has_pay));
        mStockCalcView.setViewHasPay(view.findViewById(R.id.stock_card));
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

        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        initLabel();
        ((TableLayout)view.findViewById(R.id.t_stock_head)).addView(addHead());

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

}
