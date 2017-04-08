package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.diablo.dt.diablo.entity.DiabloBrand;

import java.util.List;

/**
 * Created by buxianhui on 17/4/8.
 */

public class MatchBrandAdapter extends ArrayAdapter<DiabloBrand> {
    private Context context;
    private Integer resource;
    private Integer textViewResourceId;
    private List<DiabloBrand> filterBrands;

    public MatchBrandAdapter(Context context, Integer resource, Integer textViewResourceId, List<DiabloBrand> brands) {
        super(context, resource, textViewResourceId, brands);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.filterBrands = brands;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
        }

        DiabloBrand brand = filterBrands.get(position);
        if (brand != null) {
            TextView retailerView = (TextView) view.findViewById(textViewResourceId);
            retailerView.setText(brand.getName());
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
            return ((DiabloBrand) resultValue).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterBrands;
            filterResults.count = filterBrands.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    };
}
