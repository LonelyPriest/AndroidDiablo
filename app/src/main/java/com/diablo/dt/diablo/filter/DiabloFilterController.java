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
    private List<DiabloFilter> mFilters;

    private Integer mCount;
    private SparseArray<DiabloFilterLayout> mFilterLayouts;
    private Integer mMaxCount;

    public DiabloFilterController(Context context,
                                  List<DiabloFilter> filters,
                                  Integer maxCount) {
        this.mContext = context;
        this.mFilters = filters;

        this.mCount   = 0;
        this.mMaxCount = maxCount;
        mFilterLayouts = new SparseArray<>();
    }

    public void init(final LinearLayout parent,
                     final @IdRes Integer childResId,
                     @NonNull final ImageButton btnAdd,
                     @NonNull final ImageButton btnMinus) {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFilter(parent, childResId);

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

    public List<DiabloFilter> getEntityFilters() {
        List<DiabloFilter> entityFilters = new ArrayList<>();
        for (int i=0; i<mCount; i++) {
            SparseArray<DiabloFilter> fs = mFilterLayouts.get(i+1).getFilters();
            for(int j=0; j<fs.size(); j++) {
                entityFilters.add(fs.get(fs.keyAt(j)));
            }

        }
        return entityFilters;
    }

    private void addFilter(LinearLayout parent,  @IdRes Integer childResId) {

        LinearLayout layout = new LinearLayout(mContext);
        DiabloFilterLayout filterLayout = new DiabloFilterLayout(layout);
        for (int i=0; i<MAX_FILTER_PER_LINE; i++){
            addItem(filterLayout, i);
        }
        DiabloUtils.linearLayoutAddView(parent, layout, childResId);

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
//                DiabloFilter filter = (DiabloFilter) ((Spinner)cell).getSelectedItem();
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

        List<DiabloFilter> entityFilters = new ArrayList<>();
        for (DiabloFilter f: mFilters) {
            DiabloFilter copy = f.copy();
            copy.init();
            entityFilters.add(copy);
        }

        SpinnerFilterAdapter adapter = new SpinnerFilterAdapter(
            mContext,
            android.R.layout.simple_spinner_item,
            entityFilters);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final LinearLayout layout = filterLayout.getLayout();
        layout.addView(spinner);

        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f);
        Integer position = mCount * MAX_FILTER_PER_LINE + index;

        // View filterView;
        DiabloFilter filter;
        if (position < entityFilters.size()) {
            spinner.setSelection(position);
            // mFilters.get(position).init();
            filter = entityFilters.get(position);
            // filterView = entityFilters.get(position).getView();

        } else {
            filter = entityFilters.get(0);
            // filterView = entityFilters.get(0).getView();
        }

        filter.getView().setLayoutParams(lp);
        layout.addView(filter.getView());

        final Integer removeIndex = layout.indexOfChild(filter.getView());
        filterLayout.addEntityFilter(removeIndex, filter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                DiabloFilter filter = (DiabloFilter) adapterView.getItemAtPosition(position);
                layout.removeViewAt(removeIndex);

                // entityFilter.init();
                // DiabloUtils.instance().makeToast(mContext, removeIndex, Toast.LENGTH_SHORT);
                filter.getView().setLayoutParams(lp);
                layout.addView(filter.getView(), removeIndex);
                filterLayout.setEntityFilter(removeIndex, filter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * linear layout of filter, every linear layout have 3 entity filters
     */
    private static class DiabloFilterLayout {
        private LinearLayout mLayout;
        // private List<DiabloFilter> mFilters;
        private SparseArray<DiabloFilter> mFilters;

        private DiabloFilterLayout(LinearLayout layout) {
            mLayout = layout;
            mFilters = new SparseArray<>();
        }

        private void addEntityFilter(Integer key, DiabloFilter filter) {
            mFilters.put(key, filter);
        }

        private void setEntityFilter(Integer key, DiabloFilter filter) {
            mFilters.put(key, filter);
        }

        private SparseArray<DiabloFilter> getFilters() {
            return mFilters;
        }

        private LinearLayout getLayout() {
            return mLayout;
        }
    }
}
