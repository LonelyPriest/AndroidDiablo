package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.diablo.dt.diablo.entity.Retailer;

import java.util.List;

/**
 * Created by buxianhui on 17/3/5.
 */

public class RetailerAdapter extends ArrayAdapter<Retailer> {
    private Context context;
    private Integer resource;
    private Integer textViewResourceId;
    private List<Retailer> filterRetailers;

    public RetailerAdapter(Context context, Integer resource, Integer textViewResourceId, List<Retailer> retailers) {
        super(context, resource, textViewResourceId, retailers);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.filterRetailers = retailers;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
        }

        Retailer retailer = filterRetailers.get(position);
        if (retailer != null) {
            TextView retailerView = (TextView) view.findViewById(textViewResourceId);
                retailerView.setText(retailer.getName());
        }
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    private Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Retailer) resultValue).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterRetailers;
            filterResults.count = filterRetailers.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
           notifyDataSetChanged();
        }
    };
}
