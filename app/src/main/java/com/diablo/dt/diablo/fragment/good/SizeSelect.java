package com.diablo.dt.diablo.fragment.good;


import static com.diablo.dt.diablo.fragment.good.GoodNew.UTILS;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.diablo.dt.diablo.entity.DiabloSizeGroup;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.good.GoodCalc;
import com.diablo.dt.diablo.utils.DiabloEnum;

import java.util.List;

public class SizeSelect extends Fragment {
    /**
     * params
     */
    private Integer mComeFrom;
    private GoodCalc mGoodCalc;
    private List<DiabloSizeGroup> mImmutableSizeGroups;

    /**
     * menu operation
     */
    private Integer mOperation;

    private TableLayout mSizeSelectTable;


    public void setComeFrom(Integer comeFrom) {
        this.mComeFrom = comeFrom;
    }

    public void setGoodCalc(String calcJson) {
        this.mGoodCalc = new Gson().fromJson(calcJson, GoodCalc.class);
    }

    public void setImmutableSizeGroups(String groupsJson) {
        this.mImmutableSizeGroups = new Gson().fromJson(
            groupsJson, new TypeToken<List<DiabloSizeGroup>>(){}.getType());
    }


    public SizeSelect() {
        // Required empty public constructor
    }


    public static SizeSelect newInstance(String param1, String param2) {
        SizeSelect fragment = new SizeSelect();
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

            String sizeGroups = getArguments().getString(DiabloEnum.BUNDLE_PARAM_IMMUTABLE_SIZE);
            if (null != sizeGroups) {
                mImmutableSizeGroups = new Gson().fromJson(sizeGroups, new TypeToken<List<DiabloSizeGroup>>(){}.getType());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_size_select, container, false);
        setHasOptionsMenu(true);
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (null != bar) {
            bar.setTitle(getResources().getString(R.string.select_size));
        }

        mSizeSelectTable = (TableLayout) view.findViewById(R.id.t_size_select);
        initSizeSelectTable();
        return view;
    }

    public void initSizeSelectTable() {
        mSizeSelectTable.removeAllViews();
        TableRow row = null;
        for (DiabloSizeGroup group: Profile.instance().getSizeGroups()) {
            if (!group.getGroupId().equals(DiabloEnum.DIABLO_FREE_SIZE_GROUP)){
                row = new TableRow(getContext());
                row.setBackgroundResource(R.drawable.table_row_bg);

                View box = genCheckBox(group);
                ((CheckBox)box).setGravity(Gravity.END|Gravity.CENTER_VERTICAL);

                View cell0 = genCell(group.getName());
                ((TextView)cell0).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                ((TextView)cell0).setTypeface(null, Typeface.BOLD);
                ((TextView)cell0).setGravity(Gravity.START|Gravity.CENTER_VERTICAL);

                View cell1 = genCell(
                    android.text.TextUtils.join(
                        DiabloEnum.SIZE_SEPARATOR,
                        group.getSortedSizeNames()));
                ((TextView)cell1).setGravity(Gravity.START|Gravity.CENTER_VERTICAL);

                row.addView(box);
                row.addView(cell0);
                row.addView(cell1);

                mSizeSelectTable.addView(row);
            }
        }

        if (null != row) {
            row.setBackgroundResource(R.drawable.table_row_last_bg);
        }
    }


    private View genCheckBox(final DiabloSizeGroup group) {
        CheckBox box = new CheckBox(getContext());
        box.setTag(group);
        TableRow.LayoutParams lp = UTILS.createTableRowParams(0.2f);
        box.setLayoutParams(lp);
//        box.setScaleX(1.5f);
//        box.setScaleY(1.5f);
        // box.setGravity(Gravity.CENTER_VERTICAL|Gravity.END);

        if (null != mGoodCalc.getSizeGroup(group.getGroupId())) {
            box.setChecked(true);

            for (DiabloSizeGroup immutableGroup: mImmutableSizeGroups) {
                if (group.getGroupId().equals(immutableGroup.getGroupId())) {
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
                    mGoodCalc.addSizeGroup(group);
                } else {
                    mGoodCalc.removeSizeGroup(group);
                }
            }
        });
        return box;
    }


    private TextView genCell(String name) {
        TextView cell = new TextView(getContext());
        TableRow.LayoutParams lp = UTILS.createTableRowParams(0.6f);
        cell.setLayoutParams(lp);
        cell.setTextSize(24);
        // cell.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        // cell.setGravity(Gravity.CENTER_VERTICAL|Gravity.START);
        cell.setText(name);
        return cell;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // utils.makeToast(getContext(), hidden ? "hidden" : "show");
        if (!hidden){
            initSizeSelectTable();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

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

        ((GoodNew)to).setOnSizeSelectListener(mOnSizeSelectListener);
        ((GoodNew)to).setBackFrom(R.string.back_from_size_select);
        return to;
    }

    private Fragment getGoodUpdateFragment() {
        Fragment to = getFragmentManager().findFragmentByTag(DiabloEnum.TAG_GOOD_UPDATE);

        if (null == to ) {
            to = new GoodUpdate();
        }

        ((GoodUpdate)to).setOnSizeSelectListener(mOnSizeSelectListener);
        ((GoodUpdate)to).setBackFrom(R.string.back_from_size_select);
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
                transaction.hide(SizeSelect.this).add(R.id.frame_container, to).commit();
            } else {
                transaction.hide(SizeSelect.this).show(to).commit();
            }
        }
    }

    private OnSizeSelectListener mOnSizeSelectListener = new OnSizeSelectListener () {
        @Override
        public Integer getCurrentOperation() {
            return mOperation;
        }

        @Override
        public GoodCalc afterSelectSizeGroup() {
            return mGoodCalc;
        }
    };

    public interface OnSizeSelectListener {
        Integer    getCurrentOperation();
        GoodCalc   afterSelectSizeGroup();
    }

}
