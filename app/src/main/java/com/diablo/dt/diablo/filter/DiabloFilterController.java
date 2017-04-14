package com.diablo.dt.diablo.filter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.diablo.dt.diablo.adapter.SpinnerFilterAdapter;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/4/14.
 */

public class DiabloFilterController {
    private final static Integer MAX_FILTER_PER_LINE = 3;

    private Context mContext;
    private List<DiabloEntityFilter> mFilters;

    private Integer mCount;
    private SparseArray<DiabloFilterLayout> mFilterLayouts;
    private Integer mMaxCount;

    public DiabloFilterController(Context context,
                                  List<DiabloEntityFilter> filters,
                                  Integer maxCount) {
        this.mContext = context;
        this.mFilters = filters;

        this.mCount   = 0;
        this.mMaxCount = maxCount;
        mFilterLayouts = new SparseArray<>();
    }

    public void init(final LinearLayout parent,
                      @IdRes final Integer child,
                      @NonNull final ImageButton btnAdd,
                      @NonNull final ImageButton btnMinus) {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFilter(parent, child);

                if (mCount.equals(mMaxCount)) {
                    btnAdd.setEnabled(false);
                }

                btnMinus.setEnabled(true);
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFilter(parent);

                if (mCount.equals(0)) {
                    btnMinus.setEnabled(false);
                }
                btnAdd.setEnabled(true);
            }
        });
    }

    public Integer getCount() {
        return mCount;
    }

    public List<DiabloEntityFilter> getEntityFilters() {
        List<DiabloEntityFilter> entityFilters = new ArrayList<>();
        for (int i=0; i<mCount; i++) {
            SparseArray<DiabloEntityFilter> fs = mFilterLayouts.get(i).getFilters();
            for(int j=0; j<fs.size(); j++) {
                entityFilters.add(fs.get(fs.keyAt(j)));
            }

        }
        return entityFilters;
    }

    private void addFilter(LinearLayout parent, @IdRes Integer child) {

        LinearLayout layout = new LinearLayout(mContext);
        DiabloFilterLayout filterLayout = new DiabloFilterLayout(layout);
        for (int i=0; i<MAX_FILTER_PER_LINE; i++){
            addItem(filterLayout, i);
        }
        DiabloUtils.linearLayoutAddView(parent, layout, child);

        this.mCount++;
        mFilterLayouts.put(this.mCount, filterLayout);
    }

    /**
     * remove filter at last
     */
    private void removeFilter(LinearLayout parent) {
        DiabloFilterLayout filterLayout = mFilterLayouts.get(this.mCount);

//        for (int i=0; i<layout.getChildCount(); i++) {
//            View cell = layout.getChildAt(i);
//            if (cell instanceof Spinner) {
//                DiabloEntityFilter filter = (DiabloEntityFilter) ((Spinner)cell).getSelectedItem();
//
//            }
//        }
        parent.removeView(filterLayout.getLayout());
        mFilterLayouts.remove(this.mCount);

        this.mCount--;
    }

    private void addItem(final DiabloFilterLayout filterLayout, Integer index) {
        LinearLayout.LayoutParams lp0 = new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);
        Spinner spinner = new Spinner(mContext);
        spinner.setLayoutParams(lp0);

        SpinnerFilterAdapter adapter = new SpinnerFilterAdapter(
            mContext,
            android.R.layout.simple_spinner_item,
            mFilters);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final LinearLayout layout = filterLayout.getLayout();
        layout.addView(spinner);

        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.MATCH_PARENT, 0.7f);
        Integer position = mCount * MAX_FILTER_PER_LINE + index;
        if (position < mFilters.size()) {
            spinner.setSelection(position);

            mFilters.get(position).init();
            mFilters.get(position).getView().setLayoutParams(lp);

            layout.addView(mFilters.get(position).getView());


            final Integer removeIndex = layout.indexOfChild(mFilters.get(position).getView());
            filterLayout.addEntityFilter(removeIndex, mFilters.get(position));

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    DiabloEntityFilter entityFilter = (DiabloEntityFilter) adapterView.getItemAtPosition(position);
                    layout.removeViewAt(removeIndex);

                    entityFilter.init();
                    entityFilter.getView().setLayoutParams(lp);
                    layout.addView(mFilters.get(position).getView(), removeIndex);
                    filterLayout.setEntityFilter(removeIndex, entityFilter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    /**
     * linear layout of filter, every linear layout have 3 entity filters
     */
    private static class DiabloFilterLayout {
        private LinearLayout mLayout;
        // private List<DiabloEntityFilter> mFilters;
        private SparseArray<DiabloEntityFilter> mFilters;

        private DiabloFilterLayout(LinearLayout layout) {
            mLayout = layout;
            List<DiabloEntityFilter> filters;
            mFilters = new SparseArray<>();
        }

        private void addEntityFilter(Integer key, DiabloEntityFilter filter) {
            mFilters.put(key, filter);
        }

        private void setEntityFilter(Integer key, DiabloEntityFilter filter) {
            mFilters.setValueAt(key, filter);
        }

        private SparseArray<DiabloEntityFilter> getFilters() {
            return mFilters;
        }

        private LinearLayout getLayout() {
            return mLayout;
        }
    }
}
