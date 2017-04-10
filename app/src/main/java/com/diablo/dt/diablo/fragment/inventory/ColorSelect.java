package com.diablo.dt.diablo.fragment.inventory;


import com.google.gson.Gson;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.DiabloColorKind;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.inventory.GoodCalc;
import com.diablo.dt.diablo.model.inventory.GoodUtils;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.ArrayList;
import java.util.List;

public class ColorSelect extends Fragment {
    private final static DiabloUtils UTILS = DiabloUtils.instance();
    /**
     * params
     */
    private Integer mComeFrom;
    private GoodCalc mGoodCalc;

    private SparseArray<List<DiabloColor>> mSortedColors;

    public void setComeFrom(Integer comeFrom) {
        this.mComeFrom = comeFrom;
    }

    public void setGoodCalc(String calcJson) {
        this.mGoodCalc = new Gson().fromJson(calcJson, GoodCalc.class);
    }

    public ColorSelect() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ColorSelect newInstance(String param1, String param2) {
        ColorSelect fragment = new ColorSelect();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mComeFrom  = getArguments().getInt(DiabloEnum.BUNDLE_PARAM_COME_FORM);
            mGoodCalc = new Gson().fromJson(getArguments().getString(DiabloEnum.BUNDLE_PARAM_GOOD), GoodCalc.class);
        }

        sortColors();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_color_select, container, false);
        initHead((TableLayout) view.findViewById(R.id.t_color_kind_head));
        initContent((TableLayout)view.findViewById(R.id.t_color_kind));
        return view;

    }

    public void sortColors() {
        mSortedColors = new SparseArray<>();
        for (DiabloColorKind kind: Profile.instance().getColorKinds()) {
            mSortedColors.put(kind.getId(), new ArrayList<DiabloColor>());
        }

        for (DiabloColor color: Profile.instance().getColors()) {
            mSortedColors.get(color.getTypeId()).add(color);
        }
    }

    private Integer getMaxSizeOfSortedColors() {
        Integer max = 0;
        for (DiabloColorKind kind: Profile.instance().getColorKinds()) {
            Integer size = mSortedColors.get(kind.getId()).size();
            if (max < size) {
                max = size;
            }
        }

        return max;
    }

    public void initHead(TableLayout table) {
        TableRow row = new TableRow(getContext());
        // row.setBackgroundResource(R.drawable.table_row_bg);
        table.addView(row);

        for (DiabloColorKind kind: Profile.instance().getColorKinds()) {
            TableRow.LayoutParams lp = UTILS.createTableRowParams(1f);
            row.addView(GoodUtils.genCellOfTableHead(getContext(), lp, kind.getName()));
        }
    }

    public void initContent(TableLayout table) {
        TableRow row = null;
        for (int i=0; i<getMaxSizeOfSortedColors(); i++) {
            row = new TableRow(getContext());
            row.setBackgroundResource(R.drawable.table_row_bg);

            for (DiabloColorKind kind: Profile.instance().getColorKinds()) {
                List<DiabloColor> colors = mSortedColors.get(kind.getId());
                if ( i < colors.size()) {
                    View box = genCheckBox(colors.get(i));
                    TextView cell = genColorCell(colors.get(i));
                    row.addView(box);
                    row.addView(cell);
                }
                else {
                    row.addView(genEmptyCell());
                }
            }

            table.addView(row);
        }

        if (null != row) {
            row.setBackgroundResource(R.drawable.table_row_last_bg);
        }
    }

    private View genCheckBox(DiabloColor color) {
        CheckBox box = new CheckBox(getContext());
        box.setTag(color);
        TableRow.LayoutParams lp = UTILS.createTableRowParams(0.3f);
        box.setLayoutParams(lp);
        // box.setTextSize(20);
        box.setGravity(Gravity.CENTER_VERTICAL|Gravity.END);
        return box;
    }

    private TextView genEmptyCell() {
        TextView cell = new TextView(getContext());
        TableRow.LayoutParams lp = UTILS.createTableRowParams(1f);
        cell.setLayoutParams(lp);
        cell.setTextSize(20);
        // cell.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        cell.setGravity(Gravity.CENTER);
        return cell;
    }

    private TextView genColorCell(DiabloColor color) {
        TextView cell = new TextView(getContext());
        TableRow.LayoutParams lp = UTILS.createTableRowParams(0.7f);
        cell.setLayoutParams(lp);
        cell.setTextSize(24);
        // cell.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        cell.setGravity(Gravity.CENTER_VERTICAL|Gravity.START);
        if (null != color) {
            cell.setText(color.getName());
        }
        return cell;
    }

}
