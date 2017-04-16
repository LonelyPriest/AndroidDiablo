package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.diablo.dt.diablo.utils.DiabloEditTextWatcher;

/**
 * Created by buxianhui on 17/4/14.
 */

public class DiabloFilter {
    // private Integer mFilterId;
    private Context mContext;
    private String  mName;
    private View mView;

    private Object mSelectFilter;

    public DiabloFilter(Context context, String name) {
        // mFilterId = DiabloEnum.INVALID_INDEX;
        this.mContext = context;
        this.mName = name;
        // this.mListener = listener;
    }

    public void init() {

    }

    public void init(View view) {
        this.mView = view;
    }

    public DiabloFilter copy() {
        return null;
    }

    public String getName() {
        return mName;
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        this.mView = view;
    }

//    public View createView() {
//        return null;
//    }

    public Context getContext() {
        return mContext;
    }

    public void setSelectFilter (Object filter) {
        this.mSelectFilter = filter;
    }

    public Object getSelect() {
        return mSelectFilter;
    }

    public void addAutoCompletedTextWatcher() {
        ((AutoCompleteTextView) mView).addTextChangedListener(new DiabloEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                setSelectFilter(null);
                String name = editable.toString();
                startAutoComplete(name);
            }
        });

        ((AutoCompleteTextView) mView).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelectFilter(parent.getItemAtPosition(position));
            }
        });
    }

    public void startAutoComplete(String name) {

    }

    public void addSpinnerWatcher() {
        createAdapter();

        setSelectFilter(((Spinner) mView).getSelectedItem());
        ((Spinner) mView).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSelectFilter(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setSelectFilter(null);
            }
        });
    }

    public void createAdapter() {

    }

}
