package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.diablo.dt.diablo.entity.DiabloEntity;

import java.util.List;

/**
 * Created by buxianhui on 17/4/16.
 */

public abstract class DiabloAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private Integer resource;
    private Integer textViewResourceId;
    private AutoCompleteTextView view;
    private List<DiabloEntity> matchedItems;

    public DiabloAdapter(Context context, Integer resource, Integer textViewResourceId) {
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.view = null;
    }

    public DiabloAdapter(Context context, Integer resource, Integer textViewResourceId, AutoCompleteTextView view) {
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.view = view;

        view.setAdapter(this);
        view.setThreshold(1);
    }

    public List<DiabloEntity> findItems(String s) {
        return null;
    }

    public Context getContext() {
        return context;
    }

    public void setView(AutoCompleteTextView view) {
        this.view = view;
    }

    public AutoCompleteTextView getView() {
        return view;
    }

    public void setDropDownOffset() {

    }

    public void setMatchedItems(List<DiabloEntity> items) {
        matchedItems = items;
    }

    @Override
    public int getCount() {
        return matchedItems.size();
    }

    @Override
    public DiabloEntity getItem(int index) {
        return matchedItems.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
        }

        DiabloEntity item = matchedItems.get(position);
        if (item != null) {
            TextView retailerView = (TextView) view.findViewById(textViewResourceId);
            retailerView.setText(item.getName());
        }

        setDropDownOffset();
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((DiabloEntity)resultValue).getViewName();
            }

            @SuppressWarnings("unchecked")
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (null != constraint) {
                    List<DiabloEntity> items = findItems(constraint.toString());
                    filterResults.values = items;
                    filterResults.count = items.size();
                }
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    matchedItems =  (List<DiabloEntity>)results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }

            }
        };
    }
}
