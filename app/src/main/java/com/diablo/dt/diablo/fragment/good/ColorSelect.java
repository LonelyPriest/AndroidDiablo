package com.diablo.dt.diablo.fragment.good;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.activity.MainActivity;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.DiabloColorKind;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.good.GoodCalc;
import com.diablo.dt.diablo.model.good.GoodUtils;
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
    private List<DiabloColor> mImmutableColors;

    /**
     * menu operation
     */
    private Integer mOperation;

    private TableLayout mColorSelectTable;

    private SparseArray<List<DiabloColor>> mSortedColors;

    public void setComeFrom(Integer comeFrom) {
        this.mComeFrom = comeFrom;
    }

    public void setGoodCalc(String calcJson) {
        this.mGoodCalc = new Gson().fromJson(calcJson, GoodCalc.class);
    }

    public void setImmutableColors(String colorsJson) {
        this.mImmutableColors = new Gson().fromJson(colorsJson, new TypeToken<List<DiabloColor>>(){}.getType());
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

            String immutableColors = getArguments().getString(DiabloEnum.BUNDLE_PARAM_IMMUTABLE_COLOR);
            if (null != immutableColors) {
                mImmutableColors = new Gson().fromJson(immutableColors, new TypeToken<List<DiabloColor>>(){}.getType());
            }
        }
        setHasOptionsMenu(true);

        sortColors();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_color_select, container, false);

        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (null != bar) {
            bar.setTitle(getResources().getString(R.string.select_color));
        }

        initHead((TableLayout) view.findViewById(R.id.t_color_kind_head));

        mColorSelectTable = (TableLayout)view.findViewById(R.id.t_color_kind);
        initContent();
        return view;
    }

    private
    void sortColors() {
        mSortedColors = new SparseArray<>();
        for (DiabloColorKind kind: Profile.instance().getColorKinds()) {
            if (0 != kind.getId()) {
                mSortedColors.put(kind.getId(), new ArrayList<DiabloColor>());
            }
        }

        for (DiabloColor color: Profile.instance().getColors()) {
            if (!color.getColorId().equals(DiabloEnum.DIABLO_FREE_COLOR)) {
                Integer kindId = color.getTypeId();
                if (null != kindId && 0 != kindId) {
                    mSortedColors.get(color.getTypeId()).add(color);
                }

            }

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

    private void initHead(TableLayout table) {
        TableRow row = new TableRow(getContext());
        // row.setBackgroundResource(R.drawable.table_row_bg);
        table.addView(row);

        for (DiabloColorKind kind: Profile.instance().getColorKinds()) {
            TableRow.LayoutParams lp = UTILS.createTableRowParams(1f);
            row.addView(GoodUtils.genCellOfTableHead(getContext(), lp, kind.getName()));
        }
    }

    private void initContent() {
        mColorSelectTable.removeAllViews();
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

            mColorSelectTable.addView(row);
        }

        if (null != row) {
            row.setBackgroundResource(R.drawable.table_row_last_bg);
        }
    }

    private View genCheckBox(final DiabloColor color) {
        CheckBox box = new CheckBox(getContext());
        box.setTag(color);
        TableRow.LayoutParams lp = UTILS.createTableRowParams(0.3f);
        box.setLayoutParams(lp);
//        box.setScaleX(1.5f);
//        box.setScaleY(1.5f);
        box.setGravity(Gravity.CENTER_VERTICAL|Gravity.END);

        if (null != mGoodCalc.getColor(color.getColorId())) {
            box.setChecked(true);
            for (DiabloColor immutableColor: mImmutableColors) {
                if (color.getColorId().equals(immutableColor.getColorId())) {
                    box.setEnabled(false);
                    break;
                }
            }
        } else {
            box.setChecked(false);
        }

        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mGoodCalc.addColor(color);
                } else {
                    mGoodCalc.removeColor(color);
                }
            }
        });
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // utils.makeToast(getContext(), hidden ? "hidden" : "show");
        if (!hidden){
            sortColors();
            initContent();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();

        menu.add(Menu.NONE, 100, Menu.NONE, getResources().getString(R.string.btn_cancel))
            .setIcon(R.drawable.ic_close_black_24dp)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add(Menu.NONE, 101, Menu.NONE, getResources().getString(R.string.btn_save))
            .setIcon(R.drawable.ic_check_black_24dp)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 100: // cancel
                mOperation = R.string.action_cancel;
                break;
            case 101: // save
                mOperation = R.string.action_save;
                break;
            default:
                break;
        }

        switchFragment();
        return true;
    }

    private Fragment getGoodNewFragment() {
        Fragment to = getFragmentManager().findFragmentByTag(DiabloEnum.TAG_GOOD_NEW);
        if (null == to ) {
            to = new GoodNew();
        }

        ((GoodNew)to).setOnColorSelectListener(mOnColorSelectListener);
        ((GoodNew)to).setBackFrom(R.string.back_from_color_select);
        return to;
    }

    private Fragment getGoodUpdateFragment() {
        Fragment to = getFragmentManager().findFragmentByTag(DiabloEnum.TAG_GOOD_UPDATE);

        if (null == to ) {
            to = new GoodUpdate();
        }

        ((GoodUpdate)to).setOnColorSelectListener(mOnColorSelectListener);
        ((GoodUpdate)to).setBackFrom(R.string.back_from_color_select);
        return to;
    }

    private void switchFragment(){
        Fragment to = null;
        if (DiabloEnum.GOOD_NEW.equals(mComeFrom)) {
            to = getGoodNewFragment();
            ((MainActivity)getActivity()).selectMenuItem(10);
        }

        else if (DiabloEnum.GOOD_UPDATE.equals(mComeFrom)) {
            to = getGoodUpdateFragment();
            ((MainActivity)getActivity()).selectMenuItem(9);
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (null != to) {
            if (!to.isAdded()){
                transaction.hide(ColorSelect.this).add(R.id.frame_container, to).commit();
            } else {
                transaction.hide(ColorSelect.this).show(to).commit();
            }
        }
    }

    private OnColorSelectListener mOnColorSelectListener = new OnColorSelectListener () {
            @Override
            public Integer getCurrentOperation() {
                return mOperation;
            }

            @Override
            public GoodCalc afterSelectColor() {
                return mGoodCalc;
            }
        };

    public interface OnColorSelectListener {
        Integer    getCurrentOperation();
        GoodCalc   afterSelectColor();
    }

}
