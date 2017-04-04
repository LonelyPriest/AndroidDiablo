package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.diablo.dt.diablo.entity.Firm;

import java.util.List;

/**
 * Created by buxianhui on 17/4/4.
 */

public class FirmAdapter extends ArrayAdapter<Firm> {
    private Context context;
    private Integer resource;
    private Integer textViewResourceId;
    private List<Firm> filterFirms;

    public FirmAdapter(Context context, Integer resource, Integer textViewResourceId, List<Firm> firms) {
        super(context, resource, textViewResourceId, firms);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.filterFirms = firms;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
        }

        Firm firm = filterFirms.get(position);
        if (firm != null) {
            TextView retailerView = (TextView) view.findViewById(textViewResourceId);
            retailerView.setText(firm.getName());
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
            return ((Firm) resultValue).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterFirms;
            filterResults.count = filterFirms.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    };
}
