package com.diablo.dt.diablo.fragment.good;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.good.GoodColorNewDialog;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.List;

public class GoodColorDetail extends Fragment {
    private final static DiabloUtils UTILS = DiabloUtils.instance();
    private String [] mTableTitles;

    private TableLayout mColorDetailTable;

    public GoodColorDetail() {
        // Required empty public constructor
    }

    public static GoodColorDetail newInstance(String param1, String param2) {
        GoodColorDetail fragment = new GoodColorDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        mTableTitles = getResources().getStringArray(R.array.thead_color_detail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_good_color, container, false);

        TableRow row = new TableRow(this.getContext());
        for (String title: mTableTitles){
            TableRow.LayoutParams lp = UTILS.createTableRowParams(1f);
            TextView cell = new TextView(this.getContext());
            cell.setTypeface(null, Typeface.BOLD);
            cell.setTextColor(Color.BLACK);

            if (getResources().getString(R.string.order_id).equals(title)) {
                lp.weight = 0.5f;
            }
            cell.setLayoutParams(lp);
            cell.setText(title);
            cell.setTextSize(20);
            cell.setGravity(Gravity.CENTER);
            row.addView(cell);
        }

        TableLayout tableHead = (TableLayout) view.findViewById(R.id.t_color_detail_head);
        tableHead.addView(row);

        mColorDetailTable = (TableLayout) view.findViewById(R.id.t_color_detail);

        init();

        return view;
    }

    private void init() {
        mColorDetailTable.removeAllViews();
        List<DiabloColor> colors = Profile.instance().getColors();

        Integer orderId = DiabloEnum.START_DEFAULT_INDEX;
        TableRow row = null;
        for (DiabloColor c: colors) {
            row = new TableRow(getContext());
            mColorDetailTable.addView(row);

            for (String title: mTableTitles) {
                TableRow.LayoutParams lp = UTILS.createTableRowParams(1f);

                if (getString(R.string.order_id).equals(title)) {
                    lp.weight = 0.5f;
                    TextView cell = UTILS.addCell(getContext(), row, orderId++, lp);
                    cell.setHeight(100);
                    cell.setTextColor(ContextCompat.getColor(getContext(), R.color.bpDarker_red));
                    cell.setGravity(Gravity.CENTER);
                }
                else if (getString(R.string.color_name).equals(title)) {
                    TextView cell;
                    if (null != c.getName()) {
                        cell = UTILS.addCell(getContext(), row, c.getName(), lp);
                    } else {
                        cell = UTILS.addCell(getContext(), row, DiabloEnum.EMPTY_STRING, lp);
                    }
                    cell.setGravity(Gravity.CENTER);
                }
                else if (getString(R.string.color_type).equals(title)) {
                    TextView cell = UTILS.addCell(getContext(),
                        row,
                        Profile.instance().getColorKind(c.getTypeId()).getName(),
                        lp);
                    cell.setGravity(Gravity.CENTER);
                }

            }
            row.setBackgroundResource(R.drawable.table_row_bg);
        }

        if (null != row) {
            row.setBackgroundResource(R.drawable.table_row_last_bg);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();

        menu.add(Menu.NONE, 200, Menu.NONE, getResources().getString(R.string.btn_add))
            .setIcon(R.drawable.ic_add_circle_black_24dp)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 200: // add
                new GoodColorNewDialog(getContext(), new DiabloColor.OnGoodColorChangeListener() {
                    @Override
                    public void afterAdd(DiabloColor color) {
                        init();
                    }
                }).show();
                break;
            default:
                break;
        }
        return true;
    }
}
