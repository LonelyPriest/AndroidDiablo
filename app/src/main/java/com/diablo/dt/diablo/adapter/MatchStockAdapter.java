package com.diablo.dt.diablo.adapter;

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
                             List<MatchStock> stocks,
                             TableRow row) {
        super(context, resource, textViewResourceId, stocks);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        // this.originStocks = stocks;
        this.filterStocks = stocks;

        this.mRow = row;
    }

//    public TableRow getRow(){
//        return this.mRow;
//    }

    @NonNull
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
        }

        MatchStock stock = filterStocks.get(position);
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
            filter = new StockFilter();
        return filter;
    }

    private class StockFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterStocks;
            filterResults.count = filterStocks.size();
//            if (charSequence == null || charSequence.length() == 0){
//                filterResults.values = filterStocks;
//                filterResults.count = filterStocks.size();
//                notifyDataSetChanged();
//            }
//            else  {
//
//                final List<MatchStock> matchStocks = new ArrayList<MatchStock>();
//
//                for (MatchStock obj : filterStocks){
//                    if (obj.getName().contains(charSequence.toString())) {
//                        matchStocks.add(obj);
//                    }
//                }
//
//                filterResults.values = matchStocks;
//                filterResults.count = matchStocks.size();
//            }

            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            notifyDataSetChanged();
        }
    }
}

