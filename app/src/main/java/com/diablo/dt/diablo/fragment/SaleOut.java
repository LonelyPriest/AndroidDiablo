package com.diablo.dt.diablo.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.controller.DiabloSaleTableController;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.view.DiabloCellLabel;
import com.diablo.dt.diablo.view.DiabloTableView;

public class SaleOut extends Fragment {
    private DiabloCellLabel [] mLabels;

    private SparseArray<DiabloCellLabel> mSparseLabels;

    private DiabloTableView mSaleTableView;
    private DiabloSaleTableController mSaleTableContrller;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale_out, container, false);

        String [] heads = getResources().getStringArray(R.array.thead_sale);
        mSparseLabels = new SparseArray<>();
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
            else if (getResources().getString(R.string.amount).equals(h)) {
                label = new DiabloCellLabel(
                    h,
                    DiabloEnum.DIABLO_EDIT,
                    R.color.red,
                    18,
                    Gravity.CENTER_VERTICAL,
                    InputType.TYPE_CLASS_NUMBER,
                    false,
                    1f);
                label.setLabelId(R.string.amount);
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
                mSparseLabels.put(label.getLabelId(), label);
            }
        }

        ((TableLayout)view.findViewById(R.id.t_sale_out_head)).addView(addHead());

        TableLayout saleTable = (TableLayout)view.findViewById(R.id.t_sale_out);
        saleTable.addView(addEmptyRow());
        mSaleTableView = new DiabloTableView(saleTable);

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

    private TableRow addEmptyRow() {
        TableRow row = new TableRow(getContext());
        for (DiabloCellLabel label: mLabels) {
            View cell = label.createCell(getContext());
            row.addView(cell);
        }

        return row;
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
}
