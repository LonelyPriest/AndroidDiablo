package com.diablo.dt.diablo.activity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.entity.MatchStock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/3/7.
 */


public class MatchStockAdapter extends ArrayAdapter<MatchStock> {
    private Context context;
    private Integer resource;
    private Integer textViewResourceId;
    private TableRow mRow;
    private List<MatchStock> originStocks;
    private List<MatchStock> filterStocks;
    private Filter filter;
    // private List<MatchStock> suggestions;

    public MatchStockAdapter(Context context,
                             Integer resource,
                             Integer textViewResourceId,
                             List<MatchStock> items,
                             TableRow row) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.originStocks = items;
        this.filterStocks = new ArrayList<MatchStock>();

        this.mRow = row;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
        }

        MatchStock stock = this.originStocks.get(position);
        if (stock != null) {
            TextView dropdownView = (TextView) view.findViewById(textViewResourceId);
            if (dropdownView != null)
                dropdownView.setText(stock.getName());
        }
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (null == filter)
            filter = nameFilter;
        return filter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    private Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((MatchStock) resultValue).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            if (constraint != null) {
                FilterResults filterResults = new FilterResults();
                final List<MatchStock> matchStocks = new ArrayList<MatchStock>();

                for (MatchStock obj : originStocks){
                    if (obj.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        matchStocks.add(obj);
                    }
                }

                filterResults.values = matchStocks;
                filterResults.count = matchStocks.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterStocks = (ArrayList<MatchStock>) results.values;
            clear();
            if (results.count > 0) {
                for (MatchStock stock : filterStocks) {
                    add(stock);
                    notifyDataSetChanged();
                }
            }
        }
    };

    public TableRow getRow(){
        return this.mRow;
    }
}

